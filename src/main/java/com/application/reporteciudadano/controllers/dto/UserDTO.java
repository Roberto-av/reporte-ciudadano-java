package com.application.reporteciudadano.controllers.dto;

import com.application.reporteciudadano.entities.ReportEntity;
import com.application.reporteciudadano.entities.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String username;
    private String password;
    private List<ReportEntity> reportEntityList = new ArrayList<>();
    private Set<RoleEntity> roles = new HashSet<>();
}
