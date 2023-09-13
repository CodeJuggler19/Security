package com.ohgiraffers.jwt_oauth.user.query.application.service;

import com.ohgiraffers.jwt_oauth.user.command.domain.aggregate.entity.User;
import com.ohgiraffers.jwt_oauth.user.query.application.dto.FindUserDTO;
import com.ohgiraffers.jwt_oauth.user.query.domain.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindUserService {

    private final UserMapper userMapper;

    @Autowired
    public FindUserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public FindUserDTO findBySub(String sub){
        User findUser= userMapper.findBySub(sub);
        if(findUser == null){
            return null;
        }else{
            return new FindUserDTO(
                    findUser.getId(),
                    findUser.getSub(),
                    findUser.getName(),
                    findUser.getEmail(),
                    findUser.getProvider(),
                    findUser.getRole()
            );
        }
    }

    public FindUserDTO findById(Long userId){
        User findUser = userMapper.findById(userId);

        return new FindUserDTO(
                findUser.getId(),
                findUser.getSub(),
                findUser.getName(),
                findUser.getEmail(),
                findUser.getProvider(),
                findUser.getRole()
        );
    }
}
