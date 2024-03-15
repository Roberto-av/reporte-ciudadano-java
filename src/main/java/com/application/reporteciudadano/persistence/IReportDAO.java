package com.application.reporteciudadano.persistence;

import com.application.reporteciudadano.entities.ReportEntity;

import java.util.List;
import java.util.Optional;

public interface IReportDAO {

    List<ReportEntity> findAll();

    Optional<ReportEntity> findById(Long id);

    void save(ReportEntity report);

    void deleteById(Long id);
}
