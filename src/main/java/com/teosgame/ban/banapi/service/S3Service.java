package com.teosgame.ban.banapi.service;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.teosgame.ban.banapi.config.S3Config;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
    
    private final AmazonS3Client client;
    private final S3Config s3Config;

    // Check connectivity
    @PostConstruct
    public void postConstruct() {
        client.listObjects(s3Config.getBucketName());
    }

    public String generatePreSignedUrl(String filePath,
                                       HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 1); //validity of 1 minute
        return client.generatePresignedUrl(s3Config.getBucketName(), filePath, calendar.getTime(), httpMethod).toString();
    }
}
