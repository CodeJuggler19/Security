package com.ohgiraffers.jwt_oauth.user.query.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindUserDTO {
    private Long id;
    private String name;
    private String sub;
    private String email;
    private String provider;
    private String role;

    public FindUserDTO(Long id,String sub, String name, String email, String provider, String role) {
        this.id = id;
        this.sub=sub;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.role = role;
    }
}
