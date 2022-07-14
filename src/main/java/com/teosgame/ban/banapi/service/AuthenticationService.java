package com.teosgame.ban.banapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import com.teosgame.ban.banapi.client.RedisClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationManager {

    private final RedisClient redisClient;

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Check if user has a session
        String storedSession = redisClient.getValue(authentication.getName());
        if (storedSession == null) {
            logger.error("No Session found for this User");
            throw new SessionAuthenticationException("No Session found for this User.");
        }
        if (!storedSession.equals(authentication.getDetails())) {
            logger.error("Stored session does not match User Session!");
            throw new SessionAuthenticationException("Session User did not match attempted auth user.");
        }

        return authentication;
    }
}
