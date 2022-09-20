package com.teosgame.ban.banapi.util;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.teosgame.ban.banapi.service.SecretsManagerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile("!dev")
public class SecretsCredential implements Credential {

    private final SecretsManagerService service;

    public String getClientId() {
        return service.getClientId();
    }

    public String getClientSecret() {
        return service.getClientSecret();
    }
}
