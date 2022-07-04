package com.teosgame.ban.banapi.util;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class LocalCredential implements Credential {

    public String getClientId() {
        return System.getenv("clientId");
    }

    public String getClientSecret() {
        return System.getenv("clientSecret");
    }
}
