package com.application.reporteciudadano.persistence.impl;

import com.application.reporteciudadano.controllers.dto.response.ReportResponseDTO;
import com.application.reporteciudadano.entities.ReportEntity;
import com.application.reporteciudadano.persistence.IReportDAO;
import com.application.reporteciudadano.repositories.ReportRepository;
import com.application.reporteciudadano.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ReportDAOImpl implements IReportDAO {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public List<ReportEntity> findAll() {
        return (List<ReportEntity>) reportRepository.findAll();
    }

    @Override
    public Optional<ReportEntity> findById(Long id) {
        return reportRepository.findById(id);
    }

    @Override
    public void save(ReportEntity report) {
        reportRepository.save(report);
    }

    @Override
    public void deleteById(Long id) {
        reportRepository.deleteById(id);
    }

    @Override
    public Optional<List<ReportEntity>> findAllByUsername(String username) {
        return reportRepository.findAllByUserUsername(username);
    }


}
