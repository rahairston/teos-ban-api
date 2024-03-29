package com.teosgame.ban.banapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class AWSConfig {
    @Value("${cloud.aws.regionName}")
    private String regionName;
}
