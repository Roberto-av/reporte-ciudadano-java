package com.application.reporteciudadano.persistence;

import com.application.reporteciudadano.entities.EmployEntity;

import java.util.List;
import java.util.Optional;

public interface IEmployDAO {

    List<EmployEntity> findAll();

    Optional<EmployEntity> findById(Long id);

    void save(EmployEntity employ);

    void deleteById(Long id);
}
