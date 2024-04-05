package com.application.reporteciudadano.repositories;

import com.application.reporteciudadano.entities.EmployEntity;
import com.application.reporteciudadano.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployRepository extends CrudRepository<EmployEntity, Long> {

    Optional<EmployEntity> findByEmail(String email);

    Optional<EmployEntity> findByUsername(String username);
}
