package com.teosgame.ban.banapi.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.teosgame.ban.banapi.model.SimpleAuthority;

@Component
public class BanUtils {
    
    public boolean isUserAdmin() {
        return SecurityContextHolder.getContext()
            .getAuthentication().getAuthorities().stream()
            .anyMatch(authority -> authority.equals(SimpleAuthority.SimpleAdmin()));
    }
}
