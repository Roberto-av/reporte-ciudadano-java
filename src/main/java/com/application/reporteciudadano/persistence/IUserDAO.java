package com.application.reporteciudadano.persistence;

import com.application.reporteciudadano.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {

    List<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);

    void save(UserEntity user);

    void deleteById(Long id);
}
