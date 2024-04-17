package com.application.reporteciudadano.controllers.sockets;

import com.application.reporteciudadano.controllers.dto.response.UserResponseDTO;
import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ws")
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);


    private final SimpMessagingTemplate messagingTemplate;
    private final IUserService userService;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate, IUserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @GetMapping("/update-users")
    public void updateUsers() {
        List<UserResponseDTO> updatedUsers = getUsersFromService();
        messagingTemplate.convertAndSend("/topic/users", updatedUsers);
    }

    private List<UserResponseDTO> getUsersFromService() {
        List<UserEntity> users = userService.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserResponseDTO convertToDto(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .created_at(user.getCreatedAt())
                .build();
    }
}
