package com.application.reporteciudadano.controllers.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRequestDTO {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String password;
    private String email;
}
