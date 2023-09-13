package com.ohgiraffers.jwt_oauth.security.command.domain.token;


import com.ohgiraffers.jwt_oauth.user.command.domain.aggregate.entity.User;
import com.ohgiraffers.jwt_oauth.user.query.application.dto.FindUserDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails {

    @Getter
    private final Long id;
    private final String name;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    private UserPrincipal(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.role = builder.role;
        this.authorities = builder.authorities;
        this.attributes = builder.attributes;
    }

    public static Builder builder(Long id, String name, String role, Map<String, Object> attributes) {
        return new Builder(id, name, role, attributes);
    }

    public static Builder builder(Long id, String name, String role) {
        return new Builder(id, name, role);
    }

    public static class Builder {
        private final Long id;
        private final String name;
        private final String role;
        private final Collection<? extends GrantedAuthority> authorities;
        private Map<String, Object> attributes;

        public Builder(Long id, String name, String role, Map<String, Object> attributes) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
            this.attributes = attributes;
        }

        public Builder(Long id, String name, String role) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        }

        /**
         * 위 생성자에서 만약 입력 값 중 옵셔널 값일 경우 Setter를 통해 값을 초기화
         * 위 경우 Builder 클래스의 옵셔널 필드는 final 키워드 사용 불가
         * 이러한 경우 Builder 패턴을 사용하는 의미가 크게 낮아짐.
         */

        public UserPrincipal build() {
            return new UserPrincipal(this);
        }
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        return UserPrincipal.builder(user.getId(), user.getName(), user.getRole(), attributes).build();
    }

    public static UserPrincipal create(FindUserDTO userDTO, Map<String, Object> attributes){
        return UserPrincipal.builder(userDTO.getId(), userDTO.getName(), userDTO.getRole(), attributes).build();
    }
//    public static UserPrincipal create(FindMemberDTO member, Map<String, Object> attributes) {
//        return UserPrincipal.builder(member.getId(), member.getName(), Role.valueOf(member.getRole()).getKey(), attributes).build();
//    }
//
//    public static UserPrincipal create(FindMemberDTO member) {
//        return UserPrincipal.builder(member.getId(), member.getName(), Role.valueOf(member.getRole()).getKey()).build();
//    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }


    //test용 toString

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", authorities=" + authorities +
                ", attributes=" + attributes +
                '}';
    }
}
