package com.ohgiraffers.jwt_oauth.security.command.domain.repository;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.ohgiraffers.jwt_oauth.security.command.domain.aggregate.util.CookieUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 각각 Override된 메소드들이 언제 요청이 발동되는지 살짝 이해가 안됨?????????????????????????????????????????
@Component
public class HttpCookieAuthorizationRequestRepository implements AuthorizationRequestRepository {

    public static final String  OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDS = 180;
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        System.out.println(" 1. loadAuthorizationRequest method 호출 in HttpCookieAuthorizationRequestRepository");
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME).
                map(cookie -> CookieUtils.deserialize(cookie,OAuth2AuthorizationRequest.class)).orElse(null);
    } // 쿠키를 생성하는 것 같은데 정확한 작동 시기를 모르겠음 ???????????????????????????????????????????????

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if(authorizationRequest ==null){
            CookieUtils.deleteCookie(request,response,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME); // 이름으로 쿠키를 지움 어떤 요청일 때 ???????????????????????????
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
        String redirectURiAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        // 리다이렉트 URL 값 설정????????????????

        if(StringUtils.isNotBlank(redirectURiAfterLogin)){
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectURiAfterLogin, COOKIE_EXPIRE_SECONDS);
        }

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response){
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        // 쿠키를 지우는 메소드를 두개 호출하는 이유는??????????????????
    }
}
