package com.application.reporteciudadano.controllers.dto;

import com.application.reporteciudadano.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDTO {

    private Long id;
    private String tiposIncidencia;
    private String description;
    private String address;
    private String comments;
    private String status;
    private LocalDateTime createdAt;
    private UserEntity user;
}
