package com.ohgiraffers.jwt_oauth.user.command.domain.aggregate.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "USER_TB")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String sub;
    private String acccessToken;
    private String email;
    private String provider;
    private String role;
    @CreatedDate
    private LocalDateTime createdDate;

    public User(String name, String sub,String email, String provider, String role) {
        this.name = name;
        this.sub=sub;
        this.email = email;
        this.provider = provider;
        this.role = role;
        this.createdDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sub='" + sub + '\'' +
                ", acccessToken='" + acccessToken + '\'' +
                ", email='" + email + '\'' +
                ", provider='" + provider + '\'' +
                ", role='" + role + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
