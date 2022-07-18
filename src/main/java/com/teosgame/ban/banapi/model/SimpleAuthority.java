package com.teosgame.ban.banapi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;

import com.teosgame.ban.banapi.model.enums.RoleType;

@Getter
@EqualsAndHashCode
public class SimpleAuthority implements GrantedAuthority {
    public SimpleAuthority(RoleType type) {
        authority = type.toString();
    }

    private String authority;

    public static SimpleAuthority SimpleUser() {
        return new SimpleAuthority(RoleType.USER);
    }

    public static SimpleAuthority SimpleAdmin() {
        return new SimpleAuthority(RoleType.ADMIN);
    }

    public static SimpleAuthority SimpleDev() {
        return new SimpleAuthority(RoleType.DEVELOPER);
    }
}
