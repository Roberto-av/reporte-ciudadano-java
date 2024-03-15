package com.application.reporteciudadano.service.impl;

import com.application.reporteciudadano.entities.ReportEntity;
import com.application.reporteciudadano.persistence.IReportDAO;
import com.application.reporteciudadano.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private IReportDAO reportDAO;

    @Override
    public List<ReportEntity> findAll() {
        return reportDAO.findAll();
    }

    @Override
    public Optional<ReportEntity> findById(Long id) {
        return reportDAO.findById(id);
    }

    @Override
    public void save(ReportEntity report) {
        reportDAO.save(report);
    }

    @Override
    public void deleteById(Long id) {
        reportDAO.deleteById(id);
    }
}
