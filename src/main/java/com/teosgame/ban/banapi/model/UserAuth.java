package com.teosgame.ban.banapi.model;

import java.util.List;

import org.springframework.security.core.Authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuth implements Authentication {

    private List<SimpleAuthority> authorities;
    public String credentials;
    public String details;
    public String principal;
    public boolean authenticated;

    @Override
    public String getName() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }
    
}
