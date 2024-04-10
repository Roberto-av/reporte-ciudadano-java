package com.application.reporteciudadano.service;

import com.application.reporteciudadano.controllers.dto.response.ReportResponseDTO;
import com.application.reporteciudadano.entities.ReportEntity;
import com.application.reporteciudadano.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IReportService {

    List<ReportEntity> findAll();

    Optional<ReportEntity> findById(Long id);

    void save(ReportEntity report);

    void deleteById(Long id);

    Optional<List<ReportEntity>> findAllByUserId(Long id);
}
