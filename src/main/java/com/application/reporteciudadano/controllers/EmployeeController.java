package com.application.reporteciudadano.controllers;

import com.application.reporteciudadano.controllers.dto.response.EmployeeResponseDTO;
import com.application.reporteciudadano.entities.EmployEntity;
import com.application.reporteciudadano.service.IEmployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:9090")
@RequestMapping("/api/employee")
public class EmployeeController {

    private final IEmployService employService;

    @Autowired
    public EmployeeController(IEmployService employService) {
        this.employService = employService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<EmployeeResponseDTO>> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<EmployEntity> employee = employService.findAll();
        List<EmployeeResponseDTO> employeeResponseDTOS = employee.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeResponseDTOS);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<EmployEntity> employEntityOptional = employService.findById(id);

        if (employEntityOptional.isPresent()) {
            EmployEntity employee = employEntityOptional.get();
            EmployeeResponseDTO employeeResponseDTO = convertToDto(employee);
            return ResponseEntity.ok(employeeResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró ningún empleado con el ID " + id);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        Optional<EmployEntity> employEntityOptional = employService.findById(id);
        if (employEntityOptional.isPresent()) {
            employService.deleteById(id);
            return ResponseEntity.ok("El empleado con ID " + id + " ha sido eliminado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró ningún empleado con el ID " + id);
        }
    }

    private EmployeeResponseDTO convertToDto(EmployEntity employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .build();
    }
}
