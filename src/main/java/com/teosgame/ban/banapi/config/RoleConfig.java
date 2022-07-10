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
    
    private String TEO;
    private List<String> ADMIN;
    private List<String> DEVELOPER;

    public List<SimpleAuthority> getUserRoles(String username) {
        List<SimpleAuthority> userRoles = new ArrayList<>();
        userRoles.add(SimpleAuthority.SimpleUser());

        // probably a better way to do this with fancy lambdas
        if (TEO.equalsIgnoreCase(username)) {
            userRoles.add(SimpleAuthority.SimpleTeo());
        }

        if (ADMIN.contains(username)) {
            userRoles.add(SimpleAuthority.SimpleAdmin());
        }

        if (DEVELOPER.contains(username)) {
            userRoles.add(SimpleAuthority.SimpleDev());
        }

        return userRoles;
    }
}
