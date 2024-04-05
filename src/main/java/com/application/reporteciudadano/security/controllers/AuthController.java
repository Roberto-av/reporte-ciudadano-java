package com.application.reporteciudadano.security.controllers;


import com.application.reporteciudadano.controllers.dto.UserDTO;
import com.application.reporteciudadano.controllers.dto.request.EmployeeRequestDTO;
import com.application.reporteciudadano.entities.Role;
import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.security.authResponse.LoginRequest;
import com.application.reporteciudadano.security.authResponse.LoginResponse;
import com.application.reporteciudadano.security.authResponse.RegisterResponse;
import com.application.reporteciudadano.security.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:9090")
public class AuthController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserEntity user){
        try {
            RegisterResponse response = authenticationService.register(user);
            HttpStatus status = response.getMessage() != null && response.getMessage().contains("already exists") ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;

            String message = status == HttpStatus.CREATED ? "El ususario se ha registrado exitosamente" : response.getMessage();
            return generateResponse(message, status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        try {
            return new ResponseEntity<LoginResponse>(authenticationService.login(loginRequest),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register_admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserEntity user) {
        try {
            RegisterResponse response = authenticationService.registerAdmin(user);
            HttpStatus status = response.getMessage() != null && response.getMessage().contains("already exists") ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;

            String message = status == HttpStatus.CREATED ? "El administrador se ha registrado exitosamente" : response.getMessage();

            return generateResponse(message, status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        try {
            return new ResponseEntity<String>("logout successful",HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register_employee")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RegisterResponse> registerEmployee(@Valid @RequestBody EmployeeRequestDTO employeeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.toString()))) {

            String username = employeeDTO.getUsername();
            String password = employeeDTO.getPassword();

            RegisterResponse response = authenticationService.registerEmployee(employeeDTO, username, password);

            HttpStatus status = response.getMessage() != null && response.getMessage().contains("already exists") ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;

            String message;
            if (status == HttpStatus.CREATED) {
                message = "El empleado se ha registrado exitosamente";
            } else {
                message = response.getMessage();
            }

            RegisterResponse customResponse = new RegisterResponse(message);

            return new ResponseEntity<>(customResponse, status);
        } else {
            return new ResponseEntity<>(new RegisterResponse("Only admin can register employees"), HttpStatus.FORBIDDEN);
        }
    }

    private UserEntity convertToEntity(UserDTO userDTO) {
        return UserEntity.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();
    }

    private ResponseEntity<RegisterResponse> generateResponse(String message, HttpStatus status) {
        RegisterResponse customResponse = new RegisterResponse(message);
        return new ResponseEntity<>(customResponse, status);
    }
}
