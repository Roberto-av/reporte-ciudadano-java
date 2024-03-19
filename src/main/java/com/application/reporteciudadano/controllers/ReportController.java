package com.application.reporteciudadano.controllers;

import com.application.reporteciudadano.controllers.dto.ReportDTO;
import com.application.reporteciudadano.entities.ReportEntity;
import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.service.IReportService;
import com.application.reporteciudadano.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin("*")
@RequestMapping("/api/report")
public class ReportController {

    private final IReportService reportService;
    private final IUserService userService;

    @Autowired
    public ReportController(IReportService reportService, IUserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<?> findAll(){
        List<ReportDTO> reportDTOList = reportService.findAll()
                .stream()
                .map(report -> ReportDTO.builder()
                        .id(report.getId())
                        .tiposIncidencia(String.valueOf(report.getTipos_incidencia()))
                        .description(report.getDescription())
                        .address(report.getAddress())
                        .comments(report.getComments())
                        .status(String.valueOf(report.getStatus()))
                        .createdAt(report.getCreate_at())
                        .user(report.getUser())
                        .build())
                .toList();
        return ResponseEntity.ok(reportDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<ReportEntity> reportEntityOptional = reportService.findById(id);
        if (reportEntityOptional.isPresent()) {
            ReportEntity report = reportEntityOptional.get();
            ReportDTO reportDTO = ReportDTO.builder()
                    .id(report.getId())
                    .tiposIncidencia(String.valueOf(report.getTipos_incidencia()))
                    .description(report.getDescription())
                    .address(report.getAddress())
                    .comments(report.getComments())
                    .status(String.valueOf(report.getStatus()))
                    .createdAt(report.getCreate_at())
                    .user(report.getUser())
                    .build();
            return ResponseEntity.ok(reportDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ReportDTO reportDTO) throws URISyntaxException {
        Optional<UserEntity> userOptional = userService.findById(reportDTO.getUser().getId());
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Crear y guardar el reporte asociado al usuario
            reportService.save(ReportEntity.builder()
                    .tipos_incidencia(ReportEntity.TIPOS_INCIDENCIA.valueOf(reportDTO.getTiposIncidencia()))
                    .description(reportDTO.getDescription())
                    .address(reportDTO.getAddress())
                    .comments(reportDTO.getComments())
                    .user(user)
                    .build());

            return ResponseEntity.created(new URI("/api/report/save")).build();
        } else {
            // Manejar el caso en que el usuario no se encuentre en el repositorio
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReport(@PathVariable Long id, @RequestBody ReportDTO reportDTO){
        Optional<ReportEntity> reportEntityOptional = reportService.findById(id);

        if(reportEntityOptional.isPresent()){
            ReportEntity report = reportEntityOptional.get();
            report.setStatus(ReportEntity.STATUS.valueOf(reportDTO.getStatus()));
            reportService.save(report);
            return ResponseEntity.ok("Reporte Actualizado");
        }
        return ResponseEntity.notFound().build();
    }


}
