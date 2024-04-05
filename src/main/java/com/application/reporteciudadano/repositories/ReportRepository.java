package com.application.reporteciudadano.repositories;

import com.application.reporteciudadano.controllers.dto.response.ReportResponseDTO;
import com.application.reporteciudadano.entities.ReportEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends CrudRepository<ReportEntity, Long> {

    Optional<List<ReportEntity>> findAllByUserUsername(String username);
}
