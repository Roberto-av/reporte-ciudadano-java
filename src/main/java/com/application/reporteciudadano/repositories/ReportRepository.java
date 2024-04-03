package com.application.reporteciudadano.repositories;

import com.application.reporteciudadano.controllers.dto.response.ReportResponseDTO;
import com.application.reporteciudadano.entities.ReportEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends CrudRepository<ReportEntity, Long> {

    List<ReportResponseDTO> findAllByUsername(String username);
}
