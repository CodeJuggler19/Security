package com.ohgiraffers.jwt_oauth.security.command.application.service;

import com.ohgiraffers.jwt_oauth.security.command.domain.aggregate.entity.Token;
import com.ohgiraffers.jwt_oauth.security.command.domain.aggregate.vo.UserVO;
import com.ohgiraffers.jwt_oauth.security.command.domain.exception.TokenNotFoundException;
import com.ohgiraffers.jwt_oauth.security.command.domain.repository.TokenRepository;
import com.ohgiraffers.jwt_oauth.security.command.domain.service.CustomTokenService;
import com.ohgiraffers.jwt_oauth.security.command.domain.service.RequestUser;
import com.ohgiraffers.jwt_oauth.security.command.domain.token.UserPrincipal;
import com.ohgiraffers.jwt_oauth.user.query.application.dto.FindUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IssueTokenService {

    private final CustomTokenService customTokenService;

    private final TokenRepository tokenRepository;

    private final RequestUser requestUser;

    @Autowired
    public IssueTokenService(CustomTokenService customTokenService, TokenRepository tokenRepository, RequestUser requestUser) {
        this.customTokenService = customTokenService;
        this.tokenRepository = tokenRepository;
        this.requestUser = requestUser;
    }

    @Transactional
    public String issueTokenByUserPrincipal(UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        String userRole = userPrincipal.getRole();
        Optional<Token> findToken = tokenRepository.findTokenByUser_Id(userId);
        String issuedToken = customTokenService.createToken(userId, userRole);
        if (findToken.isPresent()) {
            Token updateToken = findToken.get();
            updateToken.setAccessToken(issuedToken);
            tokenRepository.save(updateToken);
        } else {
            Token createdToken = new Token(new UserVO(userId), issuedToken);
            tokenRepository.save(createdToken);
        }
        return issuedToken;
    }

    @Transactional
    public String issueTokenByAccessToken(String accessToken) {
        Token findToken = tokenRepository.findTokenByAccessToken(accessToken).orElseThrow(
                () -> new TokenNotFoundException("해당 Access Token은 폐기된 토큰입니다.")
        );
        Long userId = findToken.getUser().getId();

        FindUserDTO findUser = requestUser.getUserById(userId);

        String issuedToken = customTokenService.createToken(findUser.getId(), findUser.getRole());
        findToken.setAccessToken(issuedToken);
        tokenRepository.save(findToken);

        return issuedToken;
    }
}
