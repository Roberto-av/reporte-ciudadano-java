package com.application.reporteciudadano.security.services;

import com.application.reporteciudadano.controllers.dto.UserDTO;
import com.application.reporteciudadano.entities.Role;
import com.application.reporteciudadano.entities.RoleEntity;
import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.repositories.RoleRepository;
import com.application.reporteciudadano.repositories.UserRepository;
import com.application.reporteciudadano.security.authResponse.LoginRequest;
import com.application.reporteciudadano.security.authResponse.LoginResponse;
import com.application.reporteciudadano.security.authResponse.RegisterResponse;
import com.application.reporteciudadano.security.exceptions.UserRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    public RegisterResponse register(UserEntity userEntity) {
        // Verificar si el usuario ya existe
        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
            throw new UserRegistrationException("User already exists");
        }

        // Codificar la contraseña
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // Obtener o crear el rol de usuario si no existe
        RoleEntity userRole = roleRepository.findByName(Role.ROLE_USER)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity(Role.ROLE_USER);
                    newRole.setName(Role.ROLE_USER); // Asignar el nombre del rol
                    return roleRepository.save(newRole);
                });

        // Asignar el rol de usuario al usuario
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);
        userEntity.setRoles(roles);

        // Guardar el usuario en la base de datos
        UserEntity savedUser = userRepository.save(userEntity);

        // Devolver respuesta de registro exitoso
        return new RegisterResponse(savedUser.getId(), "User created");
    }
    public RegisterResponse registerAdmin(UserEntity user) {
        // Verificar si el usuario ya existe
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new RegisterResponse((Long) null, "User already exists");
        }

        // Codificar la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Obtener el rol ADMIN de la base de datos
        RoleEntity adminRole = roleRepository.findByName(Role.ROLE_ADMIN)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity(Role.ROLE_ADMIN);
                    newRole.setName(Role.ROLE_ADMIN); // Asignar el nombre del rol
                    return roleRepository.save(newRole);
                });

        // Asignar el rol de administrador al usuario
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);

        // Guardar el usuario en la base de datos
        UserEntity savedUser = userRepository.save(user);

        // Devolver respuesta de registro exitoso
        return new RegisterResponse(savedUser.getId(), "User created");
    }

    public LoginResponse login(LoginRequest loginRequest) {
        // Buscar al usuario en la base de datos por nombre de usuario
        Optional<UserEntity> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        // Verificar si el usuario existe
        if (!userOptional.isPresent()) {
            return new LoginResponse(null, null, "Username not found");
        }

        // Obtener el usuario de la opción opcional
        UserEntity user = userOptional.get();

        // Verificar si la contraseña proporcionada coincide con la contraseña encriptada almacenada en la base de datos
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponse(null, null, "Invalid username or password");
        }

        // Generar token JWT si la autenticación es exitosa
        String token = jwtService.generateToken(user);

        // Devolver respuesta de inicio de sesión exitoso
        return new LoginResponse(token, "Login successful", String.valueOf(user.getId()));
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
