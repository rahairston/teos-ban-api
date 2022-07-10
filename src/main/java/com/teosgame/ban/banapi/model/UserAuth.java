package com.teosgame.ban.banapi.model;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.teosgame.ban.banapi.client.model.response.TwitchUserInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuth implements Authentication {

    private List<SimpleAuthority> authorities;
    public String credentials;
    public String details;
    public TwitchUserInfo principal;
    public boolean authenticated;

    @Override
    public String getName() {
        return principal.getPreferred_username();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }
    
}
