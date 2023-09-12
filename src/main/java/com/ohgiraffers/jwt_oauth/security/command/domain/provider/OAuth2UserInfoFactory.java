package com.ohgiraffers.jwt_oauth.security.command.domain.provider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase("GOOGLE")){
            return new Google(attributes);
        } else {
            throw new IllegalArgumentException("해당 OAuth2 제공자는 지원하지 않습니다");
        }
    }
}
