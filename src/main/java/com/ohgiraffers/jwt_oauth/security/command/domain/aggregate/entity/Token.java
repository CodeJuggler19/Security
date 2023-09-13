package com.ohgiraffers.jwt_oauth.security.command.domain.aggregate.entity;

import com.ohgiraffers.jwt_oauth.security.command.domain.aggregate.vo.UserVO;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name="TOKEN_TB")
@Getter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserVO user;

    @Column(length = 1024, nullable = false, name = "access_token")
    private String accessToken;

    protected Token(){}

    public Token(UserVO user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
