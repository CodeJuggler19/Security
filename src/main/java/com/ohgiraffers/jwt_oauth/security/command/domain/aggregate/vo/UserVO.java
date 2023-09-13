package com.ohgiraffers.jwt_oauth.security.command.domain.aggregate.vo;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Getter
@Embeddable
public class UserVO {
    @Column(nullable = false, name = "user_id")
    private Long id;

    public UserVO(Long id){
        this.id=id;
    }

    protected UserVO() {
    }
}
