package com.application.reporteciudadano.security.authResponse;


import com.application.reporteciudadano.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private UserEntity userEntity;
    private String message;


    public RegisterResponse(Long id, String userCreated) {
    }

    public RegisterResponse(String message) {
        this.userEntity = null;
        this.message = message;
    }


}
