package com.application.reporteciudadano.service.impl;

import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.persistence.IUserDAO;
import com.application.reporteciudadano.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDAO userDAO;

    @Override
    public List<UserEntity> findAll() {
        return userDAO.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public void save(UserEntity user) {
        userDAO.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userDAO.deleteById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }


}
