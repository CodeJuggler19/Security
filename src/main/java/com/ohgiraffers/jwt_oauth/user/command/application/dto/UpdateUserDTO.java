package com.ohgiraffers.jwt_oauth.user.command.application.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateUserDTO {

    private String name;
    private String email;
    private String provider;
    private String role;

    public UpdateUserDTO(String name, String email, String provider, String role) {
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.role = role;
    }

    public UpdateUserDTO(String name) {
        this.name = name;
    }
}
