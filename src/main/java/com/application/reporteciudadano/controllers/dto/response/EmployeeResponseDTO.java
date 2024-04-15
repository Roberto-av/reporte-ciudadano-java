package com.application.reporteciudadano.controllers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String email;
}
