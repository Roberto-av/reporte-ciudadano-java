package com.application.reporteciudadano.service.impl;

import com.application.reporteciudadano.entities.EmployEntity;
import com.application.reporteciudadano.persistence.IEmployDAO;
import com.application.reporteciudadano.service.IEmployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployServiceImpl implements IEmployService {

    @Autowired
    private IEmployDAO employDAO;

    @Override
    public List<EmployEntity> findAll() {
        return employDAO.findAll();
    }

    @Override
    public Optional<EmployEntity> findById(Long id) {
        return employDAO.findById(id);
    }

    @Override
    public void save(EmployEntity employ) {
        employDAO.save(employ);
    }

    @Override
    public void deleteById(Long id) {
        employDAO.deleteById(id);
    }
}
