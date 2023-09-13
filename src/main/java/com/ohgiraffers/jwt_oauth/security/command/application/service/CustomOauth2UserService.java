package com.ohgiraffers.jwt_oauth.security.command.application.service;

import com.ohgiraffers.jwt_oauth.security.command.domain.provider.OAuth2UserInfo;
import com.ohgiraffers.jwt_oauth.security.command.domain.provider.OAuth2UserInfoFactory;
import com.ohgiraffers.jwt_oauth.security.command.domain.token.UserPrincipal;
import com.ohgiraffers.jwt_oauth.user.command.application.dto.CreateUserDTO;
import com.ohgiraffers.jwt_oauth.user.command.application.service.CreateUserService;
import com.ohgiraffers.jwt_oauth.user.command.domain.aggregate.entity.User;
import com.ohgiraffers.jwt_oauth.user.query.application.dto.FindUserDTO;
import com.ohgiraffers.jwt_oauth.user.query.application.service.FindUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final FindUserService findUserService;
    private final CreateUserService createUserService;

    @Autowired
    public CustomOauth2UserService(FindUserService findUserService, CreateUserService createUserService) {
        this.findUserService = findUserService;
        this.createUserService = createUserService;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        System.out.println("oAuth2User = " + oAuth2User);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        System.out.println("registrationId = " + registrationId);

        OAuth2UserInfo attributes = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        System.out.println("attributes = " + attributes);
        System.out.println("attributes.getAttributes() = " + attributes.getAttributes());

        UserPrincipal socialUser = saveOrUpdate(attributes, registrationId);

        return socialUser;
    }

    private UserPrincipal saveOrUpdate(OAuth2UserInfo attributes, String provider) {
        FindUserDTO user = findUserService.findBySub((String) attributes.getAttributes().get("sub"));
        UserPrincipal oauthUser = null;
        if (user == null) {
            CreateUserDTO createUserDTO = new CreateUserDTO((String) attributes.getAttributes().get("name"), (String) attributes.getAttributes().get("sub"),
                    (String) attributes.getAttributes().get("email"), attributes.getProvider(), "ROLE_USER");
            User newUser = createUserService.create(createUserDTO);
            oauthUser = UserPrincipal.create(newUser, attributes.getAttributes());
        } else {
            oauthUser = UserPrincipal.create(user, attributes.getAttributes());
        }
        return oauthUser;
    }


}
