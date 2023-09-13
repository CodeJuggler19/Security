package com.ohgiraffers.jwt_oauth.common.handler;

import com.ohgiraffers.jwt_oauth.configuration.AppProperties;
import com.ohgiraffers.jwt_oauth.security.command.application.service.IssueTokenService;
import com.ohgiraffers.jwt_oauth.security.command.domain.aggregate.util.CookieUtils;
import com.ohgiraffers.jwt_oauth.security.command.domain.repository.HttpCookieAuthorizationRequestRepository;
import com.ohgiraffers.jwt_oauth.security.command.domain.token.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.ohgiraffers.jwt_oauth.security.command.domain.repository.HttpCookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class CustomOAuth2FailHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final IssueTokenService issueTokenService;

    private final AppProperties appProperties;

    private final HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository;

    @Autowired
    public CustomOAuth2FailHandler(IssueTokenService issueTokenService, AppProperties appProperties,
                                   HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository) {
        this.issueTokenService = issueTokenService;
        this.appProperties = appProperties;
        this.httpCookieAuthorizationRequestRepository = httpCookieAuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl =determineTargetUrl(request,response,authentication);

        if(response.isCommitted()){
            logger.debug("Response has already been committed. Unable to redirect to "+ targetUrl);
            return;
        }

        clearAuthenticationAttributes(request,response);
        getRedirectStrategy().sendRedirect(request,response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        Optional<String> redirectUri= CookieUtils.getCookie(request,REDIRECT_URI_PARAM_COOKIE_NAME )
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {

        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String token = issueTokenService.issueTokenByUserPrincipal(userPrincipal);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        super.clearAuthenticationAttributes(request);
        httpCookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request,response);
    }
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }

}
