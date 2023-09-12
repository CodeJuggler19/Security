package com.ohgiraffers.jwt_oauth.security.command.domain.oauth2;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2FailHandler extends SimpleUrlAuthenticationFailureHandler {
}
