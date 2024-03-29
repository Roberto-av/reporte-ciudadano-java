package com.application.reporteciudadano.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String firstName;

    @Column(name = "apellidos")
    private String lastName;

    @Size(max = 100)
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "numero_celular")
    private String phoneNumber;

    @Column(name = "contraseña")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<ReportEntity> reportEntityList = new ArrayList<>();

}
