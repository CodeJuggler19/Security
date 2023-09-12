package com.ohgiraffers.jwt_oauth.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfiguration implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Value("${app.cors.allowedOrigins}")
    private String[] allowedOrigins;// 일반적으로 origin을 전부 허락하지 않음
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("/*")    //외부에서 들어오는 모둔 url 을 허용
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")    //허용되는 Method
                .allowedHeaders("*")    //허용되는 헤더
                .allowCredentials(true)    //자격증명 허용
                .maxAge(MAX_AGE_SECS);   //허용 시간
    }
}
