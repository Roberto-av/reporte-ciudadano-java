package com.application.reporteciudadano.persistence.impl;

import com.application.reporteciudadano.entities.EmployEntity;
import com.application.reporteciudadano.persistence.IEmployDAO;
import com.application.reporteciudadano.repositories.EmployRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmployDAOImpl implements IEmployDAO {

    @Autowired
    private EmployRepository employRepository;

    @Override
    public List<EmployEntity> findAll() {
        return (List<EmployEntity>) employRepository.findAll();
    }

    @Override
    public Optional<EmployEntity> findById(Long id) {
        return employRepository.findById(id);
    }

    @Override
    public void save(EmployEntity employ) {
        employRepository.save(employ);
    }

    @Override
    public void deleteById(Long id) {
        employRepository.deleteById(id);
    }
}
