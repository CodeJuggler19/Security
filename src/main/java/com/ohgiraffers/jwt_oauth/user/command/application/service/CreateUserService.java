package com.ohgiraffers.jwt_oauth.user.command.application.service;

import com.ohgiraffers.jwt_oauth.user.command.application.dto.CreateUserDTO;
import com.ohgiraffers.jwt_oauth.user.command.domain.aggregate.entity.User;
import com.ohgiraffers.jwt_oauth.user.command.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    private final UserRepository userRepository;

    @Autowired
    public CreateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User create(CreateUserDTO createUserDTO){
        User createUser= new User(
                createUserDTO.getName(),
                createUserDTO.getSub(),
                createUserDTO.getEmail(),
                createUserDTO.getProvider(),
                createUserDTO.getRole()
        );
        return userRepository.save(createUser);
    }
}
