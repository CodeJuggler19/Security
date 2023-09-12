package com.ohgiraffers.jwt_oauth.configuration;

import com.ohgiraffers.jwt_oauth.security.command.application.service.CustomOauth2UserService;
import com.ohgiraffers.jwt_oauth.security.command.domain.oauth2.OAuth2FailHandler;
import com.ohgiraffers.jwt_oauth.security.command.domain.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2FailHandler oAuth2FailHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;



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
                .baseUri("/oauth2/authorize")
                //해당 URL로 접근시 oauth2 로그인을 요청
                .and()
                .userInfoEndpoint()

                .userService(customOauth2UserService);

                //oauth2로 유저정보를 받아오면 oauth2인증 유저 객체로 등록함

        return http.build();

    }
}
