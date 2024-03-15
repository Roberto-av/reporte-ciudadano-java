package com.application.reporteciudadano.repositories;

import com.application.reporteciudadano.entities.EmployEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployRepository extends CrudRepository<EmployEntity, Long> {
}
