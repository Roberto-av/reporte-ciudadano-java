package com.application.reporteciudadano.security.services;

import com.application.reporteciudadano.controllers.dto.request.EmployeeRequestDTO;
import com.application.reporteciudadano.controllers.sockets.WebSocketController;
import com.application.reporteciudadano.entities.EmployEntity;
import com.application.reporteciudadano.entities.Role;
import com.application.reporteciudadano.entities.RoleEntity;
import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.repositories.EmployRepository;
import com.application.reporteciudadano.repositories.RoleRepository;
import com.application.reporteciudadano.repositories.UserRepository;
import com.application.reporteciudadano.security.authResponse.LoginRequest;
import com.application.reporteciudadano.security.authResponse.LoginResponse;
import com.application.reporteciudadano.security.authResponse.RegisterResponse;
import com.application.reporteciudadano.security.exceptions.UserRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final EmployRepository employRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    private final WebSocketController webSocketController;

    @Autowired
    public AuthenticationService(UserRepository userRepository, EmployRepository employRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager, RoleRepository roleRepository,
                                 WebSocketController webSocketController) {

        this.userRepository = userRepository;
        this.employRepository = employRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.webSocketController = webSocketController;
    }


    @Transactional
    public RegisterResponse register(UserEntity userEntity) {
        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
            throw new UserRegistrationException("User already exists");
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        RoleEntity userRole = roleRepository.findByName(Role.ROLE_USER)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity(Role.ROLE_USER);
                    newRole.setName(Role.ROLE_USER); // Asignar el nombre del rol
                    return roleRepository.save(newRole);
                });

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);
        userEntity.setRoles(roles);

        UserEntity savedUser = userRepository.save(userEntity);

        webSocketController.updateUsers();

        return new RegisterResponse(savedUser.getId(), "User created");
    }

    public RegisterResponse registerAdmin(UserEntity user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new RegisterResponse("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleEntity adminRole = roleRepository.findByName(Role.ROLE_ADMIN)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity(Role.ROLE_ADMIN);
                    newRole.setName(Role.ROLE_ADMIN); // Asignar el nombre del rol
                    return roleRepository.save(newRole);
                });

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);

        UserEntity savedUser = userRepository.save(user);

        return new RegisterResponse(savedUser.getId(), "User created");
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Generar token JWT si la autenticación es exitosa
                String token = jwtService.generateToken(user);
                return new LoginResponse(token, "Login successful", String.valueOf(user.getId()));
            }
        }

        // Si no se encontró el usuario o las credenciales son incorrectas, devuelve un mensaje de error
        return new LoginResponse(null, "Invalid username or password", null);
    }

    public RegisterResponse registerEmployee(EmployeeRequestDTO employeeDTO, String username, String password) {
        Optional<EmployEntity> existingEmployeeByEmail = employRepository.findByEmail(employeeDTO.getEmail());
        Optional<EmployEntity> existingEmployeeByUsername = employRepository.findByUsername(employeeDTO.getUsername());

        if (existingEmployeeByEmail.isPresent() || existingEmployeeByUsername.isPresent()) {
            return new RegisterResponse("Employee with the same email or username already exists");
        }

        EmployEntity employeeEntity = convertToEntity(employeeDTO);

        String encryptedPassword = passwordEncoder.encode(password);
        employeeEntity.setPassword(encryptedPassword);

        EmployEntity savedEmployee = employRepository.save(employeeEntity);

        // Crear un nuevo objeto UserEntity
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(encryptedPassword);

        RoleEntity employeeRole = roleRepository.findByName(Role.ROLE_EMPLOYEE)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity(Role.ROLE_EMPLOYEE);
                    newRole.setName(Role.ROLE_EMPLOYEE); // Asignar el nombre del rol
                    return roleRepository.save(newRole);
                });

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(employeeRole);
        userEntity.setRoles(roles);

        userRepository.save(userEntity);

        // Asociar el usuario con el empleado
        employeeEntity.setUser(userEntity);
        employRepository.save(employeeEntity);

        return new RegisterResponse(savedEmployee.getId(), "Employee registered successfully");
    }

    public Set<RoleEntity> getUserRoles(Long userId) {
        // Busca el usuario por su ID en la base de datos
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            // Obtiene el usuario de la base de datos
            UserEntity user = optionalUser.get();
            // Devuelve los roles asociados al usuario
            return user.getRoles();
        } else {
            // Si no se encuentra el usuario, lanza una excepción
            throw new RuntimeException("User not found");
        }
    }

    private EmployEntity convertToEntity(EmployeeRequestDTO employeeDTO) {
        return EmployEntity.builder()
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .username(employeeDTO.getUsername())
                .password(employeeDTO.getPassword())
                .phoneNumber(employeeDTO.getPhoneNumber())
                .build();
    }

}
