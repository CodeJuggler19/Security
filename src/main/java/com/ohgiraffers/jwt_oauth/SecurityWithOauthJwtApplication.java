package com.ohgiraffers.jwt_oauth;

import com.ohgiraffers.jwt_oauth.configuration.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SecurityWithOauthJwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityWithOauthJwtApplication.class, args);
    }

}
