package com.ohgiraffers.jwt_oauth.security.command.domain.provider;

import java.util.Map;

public class Google extends OAuth2UserInfo{
    public Google(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProvider() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
