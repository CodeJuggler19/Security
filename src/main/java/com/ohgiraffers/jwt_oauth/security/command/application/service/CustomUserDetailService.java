package com.ohgiraffers.jwt_oauth.security.command.application.service;

import com.ohgiraffers.jwt_oauth.security.command.domain.token.UserPrincipal;
import com.ohgiraffers.jwt_oauth.user.query.application.dto.FindUserDTO;
import com.ohgiraffers.jwt_oauth.user.query.application.service.FindUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final FindUserService findUserService;

    @Autowired
    public CustomUserDetailService(FindUserService findUserService) {
        this.findUserService = findUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        FindUserDTO findUser = findUserService.findByEmail(email);
        return UserPrincipal.create(findUser);
    }

    public UserDetails loadUserById(Long id){
        FindUserDTO findUser= findUserService.findById(id);
        return UserPrincipal.create(findUser);
    }
}
