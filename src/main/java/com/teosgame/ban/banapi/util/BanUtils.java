package com.teosgame.ban.banapi.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.teosgame.ban.banapi.model.SimpleAuthority;

@Component
public class BanUtils {

    public static final String GUID_PATTERN="(^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$|)";
    
    public boolean isUserAdmin() {
        return SecurityContextHolder.getContext()
            .getAuthentication().getAuthorities().stream()
            .anyMatch(authority -> authority.equals(SimpleAuthority.SimpleAdmin()));
    }
}
