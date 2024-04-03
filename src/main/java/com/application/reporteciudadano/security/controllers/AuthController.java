package com.application.reporteciudadano.security.controllers;


import com.application.reporteciudadano.controllers.dto.UserDTO;
import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.security.authResponse.LoginRequest;
import com.application.reporteciudadano.security.authResponse.LoginResponse;
import com.application.reporteciudadano.security.authResponse.RegisterResponse;
import com.application.reporteciudadano.security.exceptions.UserRegistrationException;
import com.application.reporteciudadano.security.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return new ResponseEntity<RegisterResponse>(authenticationService.register(user),HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserEntity user){
        try {
            return new ResponseEntity<RegisterResponse>(authenticationService.registerAdmin(user),HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
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
}
