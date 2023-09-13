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

// oauth 인증이 성공했을 때 마지막으로 실행되는 부분
// 해당 핸들러에서 security 사용자 인증 정보를 통해 jwt access token을 생성하여, 최초 oauth 인증 요청 시 받았던 redirect_uri를
// 검증하야 해당 uri로 access token을 내려주는 코드가 구현되어 있습니다.
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final IssueTokenService issueTokenService;
    private final AppProperties appProperties;
    private final HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository;

    @Autowired
    CustomOAuth2SuccessHandler(IssueTokenService issueTokenService, AppProperties appProperties,
                               HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository) {
        this.issueTokenService = issueTokenService;
        this.appProperties = appProperties;
        this.httpCookieAuthorizationRequestRepository = httpCookieAuthorizationRequestRepository;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println("1. onAuthenticationSuccess method 호출 in CustomOAuth2SuccessHandler ");
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        // determineTargetUrl에서 반환받은 url(토큰 정보를 queryParam으로 포함하고 있는) 것을 응답해준다.
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        System.out.println("2. determineTargetUrl method 호출 in CustomOAuth2SuccessHandler ");
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        System.out.println("2-1 . redirectUri : " + redirectUri);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())){
            System.out.println("BadRequestException 발생");
        }
        String targetUrl= redirectUri.orElse(getDefaultTargetUrl());

        UserPrincipal userPrincipal= (UserPrincipal) authentication.getPrincipal();

        String token = issueTokenService.issueTokenByUserPrincipal(userPrincipal);

        System.out.println("2-2 targetUrl : "+ targetUrl );
        System.out.println("2-3 userPrincipal : "+ userPrincipal.toString());
        System.out.println("2-4 token : " + token);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
        // 여기서 쿼리 파라미터 형식으로 토큰 정보와  redirectURL 정보를 반환하면 onAuthenticationSuccess로
    }


    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri->{
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
