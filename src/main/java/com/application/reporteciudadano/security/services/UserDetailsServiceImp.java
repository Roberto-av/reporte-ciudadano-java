package com.application.reporteciudadano.security.services;

import com.application.reporteciudadano.entities.UserEntity;
import com.application.reporteciudadano.repositories.UserRepository;
import com.application.reporteciudadano.security.services.impl.UserDetailsImpl;
import com.application.reporteciudadano.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return (UserDetails) UserDetailsImpl.build(userEntity);
    }
}
