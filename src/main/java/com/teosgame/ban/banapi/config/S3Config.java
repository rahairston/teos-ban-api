package com.teosgame.ban.banapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class S3Config {
    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Value("${cloud.aws.s3.region}")
    private String regionName;
}
