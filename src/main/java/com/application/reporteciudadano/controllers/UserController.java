package com.application.reporteciudadano.controllers;

import com.application.reporteciudadano.controllers.dto.UserDTO;
import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<UserDTO> userDTOList = userService.findAll()
                .stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .password(user.getPassword())
                        .build())
                .toList();
        return ResponseEntity.ok(userDTOList);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserDTO userDTO) throws URISyntaxException {
        if (userDTO.getFirstName().isBlank() && userDTO.getLastName().isBlank()){
            return ResponseEntity.badRequest().build();
        }
        userService.save(UserEntity.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .build());

        return ResponseEntity.created(new URI("/api/user/save")).build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        if(id != null){
            userService.deleteById(id);
            return ResponseEntity.ok("Se ah eliminado el usuario con id: " +id+ " con exito");
        }
        return ResponseEntity.notFound().build();
    }
}
