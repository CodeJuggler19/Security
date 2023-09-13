package com.ohgiraffers.jwt_oauth.security.command.domain.service;

import com.ohgiraffers.jwt_oauth.user.query.application.dto.FindUserDTO;
import org.springframework.stereotype.Repository;

public interface RequestUser {
    FindUserDTO getUserById(Long userId);
}
