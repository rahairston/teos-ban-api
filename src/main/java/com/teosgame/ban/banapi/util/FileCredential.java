package com.teosgame.ban.banapi.util;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!dev")
public class FileCredential implements Credential {
    public String getClientId() {
        return "";
    }

    public String getClientSecret() {
        return "";
    }
}
