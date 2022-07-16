package com.teosgame.ban.banapi.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.teosgame.ban.banapi.model.SimpleAuthority;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "ban.teo.roles")
@Data
public class RoleConfig {

    private List<String> ADMIN;
    private List<String> DEVELOPER;

    public List<SimpleAuthority> getUserRoles(String username) {
        String lower = username.toLowerCase();
        List<SimpleAuthority> userRoles = new ArrayList<>();
        userRoles.add(SimpleAuthority.SimpleUser());

        if (ADMIN.contains(lower)) {
            userRoles.add(SimpleAuthority.SimpleAdmin());
        }

        if (DEVELOPER.contains(lower)) {
            userRoles.add(SimpleAuthority.SimpleDev());
        }

        return userRoles;
    }
}
