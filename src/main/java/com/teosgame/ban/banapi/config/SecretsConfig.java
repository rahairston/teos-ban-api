package com.teosgame.ban.banapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class SecretsConfig {
    @Value("${cloud.aws.secrets.twitch}")
    private String twitchSecret;

    @Value("${cloud.aws.secrets.mysql}")
    private String dbSecret;
}
