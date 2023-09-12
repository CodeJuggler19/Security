package com.ohgiraffers.jwt_oauth.user.command.domain.repository;

import com.ohgiraffers.jwt_oauth.user.command.domain.aggregate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}

