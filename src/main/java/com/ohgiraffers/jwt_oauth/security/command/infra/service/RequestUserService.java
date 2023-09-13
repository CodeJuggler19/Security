package com.ohgiraffers.jwt_oauth.security.command.infra.service;

import com.ohgiraffers.jwt_oauth.common.annotation.InfraService;
import com.ohgiraffers.jwt_oauth.security.command.domain.service.RequestUser;
import com.ohgiraffers.jwt_oauth.user.query.application.dto.FindUserDTO;
import com.ohgiraffers.jwt_oauth.user.query.application.service.FindUserService;
import org.springframework.beans.factory.annotation.Autowired;

@InfraService
public class RequestUserService implements RequestUser {
    private final FindUserService findUserService;

    @Autowired
    public RequestUserService(FindUserService findUserService) {
        this.findUserService = findUserService;
    }


    @Override
    public FindUserDTO getUserById(Long userId) {
        return findUserService.findById(userId);
    }
}
