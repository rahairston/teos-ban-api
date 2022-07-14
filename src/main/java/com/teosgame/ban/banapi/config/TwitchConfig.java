package com.teosgame.ban.banapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class TwitchConfig {
    
    @Value("${ban.teo.redirect_uris:}")
    private String redirectUri;

    @Value("${twitch.url.auth:}")
    private String tokenUrl;

    @Value("${twitch.url.userinfo:}")
    private String userInfoUrl;

    @Value("${twitch.url.validate:}")
    private String validate;

    @Value("${twitch.url.keys:}")
    private String keys;

    @Value("${twitch.url.issuer:}")
    private String iss;
}
