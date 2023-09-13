package com.ohgiraffers.jwt_oauth.security.command.domain.repository;

import com.ohgiraffers.jwt_oauth.security.command.domain.aggregate.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTokenByUser_Id(Long userId);

    Optional<Token> findTokenByAccessToken(String accessToken);
}
