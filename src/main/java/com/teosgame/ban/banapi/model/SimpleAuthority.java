package com.teosgame.ban.banapi.model;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;

import com.teosgame.ban.banapi.enums.RoleType;

@Getter
public class SimpleAuthority implements GrantedAuthority {
    public SimpleAuthority(RoleType type) {
        authority = type.toString();
    }

    private String authority;
}
