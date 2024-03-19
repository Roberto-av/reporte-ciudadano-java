package com.application.reporteciudadano.controllers;

import com.application.reporteciudadano.controllers.dto.EmployDTO;
import com.application.reporteciudadano.entities.EmployEntity;
import com.application.reporteciudadano.service.IEmployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/api/employee")
public class EmployController {

    @Autowired
    private IEmployService employService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<EmployDTO> employDTOS = employService.findAll()
                .stream()
                .map(employ -> EmployDTO.builder()
                        .id(employ.getId())
                        .firstName(employ.getFirstName())
                        .lastName(employ.getLastName())
                        .email(employ.getEmail())
                        .password(employ.getPassword())
                        .build())
                .toList();
        return ResponseEntity.ok(employDTOS);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody EmployDTO employDTO) throws URISyntaxException {
        if (employDTO.getFirstName().isBlank() && employDTO.getLastName().isBlank()){
            return ResponseEntity.badRequest().build();
        }
        employService.save(EmployEntity.builder()
                .firstName(employDTO.getFirstName())
                .lastName(employDTO.getLastName())
                .email(employDTO.getEmail())
                .password(employDTO.getPassword())
                .build());

        return ResponseEntity.created(new URI("/api/employee/save")).build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        if(id != null){
            employService.deleteById(id);
            return ResponseEntity.ok("Se ah eliminado el empleado con id: " +id+ " con exito");
        }
        return ResponseEntity.notFound().build();
    }

}
