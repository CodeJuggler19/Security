package com.ohgiraffers.jwt_oauth.user.command.application.service;

import com.ohgiraffers.jwt_oauth.user.command.application.dto.UpdateUserDTO;
import com.ohgiraffers.jwt_oauth.user.command.domain.aggregate.entity.User;
import com.ohgiraffers.jwt_oauth.user.command.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UpdateUserService {

    private final UserRepository userRepository;

    @Autowired
    public UpdateUserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Transactional
    public boolean update(Long userId, UpdateUserDTO updateUserDTO){
        Optional<User> findUser = userRepository.findById(userId);
        if(findUser.isPresent()){
            User updateUser = findUser.get();
            if(updateUserDTO.getName() !=null){
                updateUser.setName(updateUserDTO.getName());
            }
            return true;
        }else{
            return false;
        }
    }
}
