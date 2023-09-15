package com.ohgiraffers.jwt_oauth.configuration;

import com.ohgiraffers.jwt_oauth.common.filter.TokenAuthenticationFilter;
import com.ohgiraffers.jwt_oauth.common.handler.CustomOAuth2FailureHandler;
import com.ohgiraffers.jwt_oauth.common.handler.CustomOAuth2SuccessHandler;
import com.ohgiraffers.jwt_oauth.security.command.application.service.CustomOauth2UserService;
import com.ohgiraffers.jwt_oauth.security.command.application.service.CustomUserDetailService;
import com.ohgiraffers.jwt_oauth.security.command.domain.repository.HttpCookieAuthorizationRequestRepository;
import com.ohgiraffers.jwt_oauth.security.command.domain.service.CustomTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomOauth2UserService customOauth2UserService;
    private final HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Autowired
    private CustomTokenService customTokenService;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    TokenAuthenticationFilter tokenAuthenticationFilter(CustomTokenService customTokenService,
                                                        CustomUserDetailService customUserDetailService) {
        return new TokenAuthenticationFilter(customTokenService, customUserDetailService);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder   (){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()


                .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    .anyRequest()
                    .authenticated()
                .and()


                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize") // 소셜 로그인 url
                    .authorizationRequestRepository(httpCookieAuthorizationRequestRepository)
                .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*") // 소셜 인증 후 redirect url
                .and()
                // userService()는 OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User를 반환하는 클래스를 지정한다 =>????????????????????????????????????
                .userInfoEndpoint()
                    .userService(customOauth2UserService) // 이와 같이 할경우 메소드를 호출하는 것이 아닌데 어떻게 작동하나요???????????????????????
                .and()
                .successHandler(customOAuth2SuccessHandler) //oauth2로 유저정보를 받아오면 oauth2인증 유저 객체로 등록함
                .failureHandler(customOAuth2FailureHandler);

        http
                .addFilterAfter(tokenAuthenticationFilter(customTokenService,customUserDetailService), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
