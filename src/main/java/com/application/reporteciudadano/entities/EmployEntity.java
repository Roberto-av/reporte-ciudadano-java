package com.application.reporteciudadano.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "empleados")
public class EmployEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String firstName;

    @Column(name = "apellidos")
    private String lastName;

    @Column(name = "numero_celular")
    private String phoneNumber;

    @Column(name = "email", unique = true)
    @Size(max = 100)
    @Email
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @Size(min = 8, max = 60, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

}
