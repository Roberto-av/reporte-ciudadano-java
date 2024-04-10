package com.application.reporteciudadano.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reportes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"descripcion", "direccion"})
})
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TIPOS_INCIDENCIA  tipos_incidencia;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "direccion")
    private String address;

    @Column(name = "comentarios")
    private String comments;

    @Enumerated(EnumType.STRING)
    private STATUS  status;

    private LocalDateTime create_at;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    public enum TIPOS_INCIDENCIA {
        BACHE,
        PROBLEMA_ALUMBRADO,
        REPORTE_BASURA
    }

    public enum STATUS {
        PENDIENTE,
        PROCESO,
        RESUELTO
    }

    @PrePersist
    protected void onCreate() {
        create_at = LocalDateTime.now();

        if (status == null) {
            status = STATUS.PENDIENTE;
        }
    }
}
