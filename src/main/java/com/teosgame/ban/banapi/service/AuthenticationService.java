package com.teosgame.ban.banapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AuthenticationService implements AuthenticationManager {

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("sesh: {}", authentication.getDetails());
        // Check if session ID and token are all currently belonging to USER in our session DB
        return authentication;
    }
}
