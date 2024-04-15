package com.application.reporteciudadano.controllers;

import com.application.reporteciudadano.controllers.dto.response.UserResponseDTO;
import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:9090")
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<UserEntity> users = userService.findAll();
        List<UserResponseDTO> userResponseDTOS = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDTOS);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<UserEntity> userEntityOptional = userService.findById(id);

        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();
            UserResponseDTO userResponseDTO = convertToDto(user);
            return ResponseEntity.ok(userResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró ningún usuario con el ID " + id);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<UserEntity> userEntityOptional = userService.findById(id);
        if (userEntityOptional.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.ok("El usuario con ID " + id + " ha sido eliminado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró ningún usuario con el ID " + id);
        }
    }

    private UserResponseDTO convertToDto(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
