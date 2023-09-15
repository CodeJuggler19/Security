package com.ohgiraffers.jwt_oauth.common.handler;

import com.ohgiraffers.jwt_oauth.configuration.AppProperties;
import com.ohgiraffers.jwt_oauth.security.command.application.service.IssueTokenService;
import com.ohgiraffers.jwt_oauth.security.command.domain.aggregate.util.CookieUtils;
import com.ohgiraffers.jwt_oauth.security.command.domain.repository.HttpCookieAuthorizationRequestRepository;
import com.ohgiraffers.jwt_oauth.security.command.domain.token.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.ohgiraffers.jwt_oauth.security.command.domain.repository.HttpCookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {


    private final HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository;

    @Autowired
    public CustomOAuth2FailureHandler(HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository) {
        this.httpCookieAuthorizationRequestRepository = httpCookieAuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String targetUrl;
        targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        httpCookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
