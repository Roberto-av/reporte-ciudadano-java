package com.application.reporteciudadano.controllers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportRequestDTO {
    private String tiposIncidencia;
    private String description;
    private String status;
    private String address;
    private String comments;
}

