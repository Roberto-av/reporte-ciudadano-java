package com.application.reporteciudadano.controllers;

import com.application.reporteciudadano.controllers.dto.request.ReportRequestDTO;
import com.application.reporteciudadano.controllers.dto.response.ReportResponseDTO;
import com.application.reporteciudadano.entities.ReportEntity;
import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.security.exceptions.UserNotFoundException;
import com.application.reporteciudadano.service.IReportService;
import com.application.reporteciudadano.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:9090")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<ReportResponseDTO>> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_EMPLOYEE"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ReportEntity> reports = reportService.findAll();
        List<ReportResponseDTO> reportResponseDTOList = reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportResponseDTOList);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<ReportEntity> reportEntityOptional = reportService.findById(id);
        return reportEntityOptional.map(report -> {
            ReportResponseDTO reportResponseDTO = convertToDto(report);
            return ResponseEntity.ok(reportResponseDTO);
        }).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ReportRequestDTO reportRequestDTO) throws URISyntaxException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = getUserByUsername(username);

        // Crear y guardar el reporte asociado al usuario
        ReportEntity report = ReportEntity.builder()
                .tipos_incidencia(ReportEntity.TIPOS_INCIDENCIA.valueOf(reportRequestDTO.getTiposIncidencia()))
                .description(reportRequestDTO.getDescription())
                .address(reportRequestDTO.getAddress())
                .comments(reportRequestDTO.getComments())
                .user(user)
                .build();

        reportService.save(report);

        return ResponseEntity.created(new URI("/api/report/save")).build();
    }



    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<?> updateReport(@PathVariable Long id, @RequestBody ReportRequestDTO reportRequestDTO){
        Optional<ReportEntity> reportEntityOptional = reportService.findById(id);

        if(reportEntityOptional.isPresent()){
            ReportEntity report = reportEntityOptional.get();
            report.setStatus(ReportEntity.STATUS.valueOf(reportRequestDTO.getStatus()));
            reportService.save(report);
            return ResponseEntity.ok("Update report");
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/findAll/user")
    public ResponseEntity<List<ReportResponseDTO>> getMyReports() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<List<ReportEntity>> optionalReports = reportService.findAllByUsername(username);

        if (optionalReports.isPresent()) {
            List<ReportEntity> reports = optionalReports.get();
            List<ReportResponseDTO> userReports = reports.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userReports);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    private ReportResponseDTO convertToDto(ReportEntity report) {
        return ReportResponseDTO.builder()
                .id(report.getId())
                .tiposIncidencia(String.valueOf(report.getTipos_incidencia()))
                .description(report.getDescription())
                .address(report.getAddress())
                .comments(report.getComments())
                .status(String.valueOf(report.getStatus()))
                .createdAt(report.getCreate_at())
                .user(report.getUser())
                .build();
    }

    private UserEntity getUserByUsername(String username) {
        Optional<UserEntity> userOptional = userService.findByUsername(username);
        return userOptional.orElseThrow(() -> new UserNotFoundException("User not fund: " + username));
    }


}
