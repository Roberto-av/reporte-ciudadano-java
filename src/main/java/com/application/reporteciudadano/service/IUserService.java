package com.application.reporteciudadano.service;

import com.application.reporteciudadano.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);

    void save(UserEntity user);

    void deleteById(Long id);

    Optional<UserEntity> findByUsername(String username);
}
