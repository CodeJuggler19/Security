package com.ohgiraffers.jwt_oauth.user.query.domain.repository;


import com.ohgiraffers.jwt_oauth.user.command.domain.aggregate.entity.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findBySub(String sub);

    User findById(Long userId);
}
