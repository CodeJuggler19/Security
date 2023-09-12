package com.ohgiraffers.jwt_oauth.user.command.application.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateUserDTO {
    private final String name;
    private final String sub;
    private final String email;
    private final String provider;
    private final String role;
    public CreateUserDTO(String name,String sub, String email, String provider, String role) {
        this.name = name;
        this.sub=sub;
        this.email = email;
        this.provider = provider;
        this.role = role;
    }
}
