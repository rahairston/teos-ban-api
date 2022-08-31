package com.teosgame.ban.banapi.service;

import java.net.URL;
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
    private final S3Config config;

    // Check connectivity
    @PostConstruct
    public void postConstruct() {
        client.listObjects(config.getBucketName());
    }

    // Only need GET, PUT, and DELETE
    public URL generatePreSignedUrl(String filePath,
                                       HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (httpMethod.equals(HttpMethod.GET)) {
          calendar.add(Calendar.MINUTE, 30); //GET for 30 minutes
        } else {
          calendar.add(Calendar.MINUTE, 1); //validity of 1 minute
        }
        return client.generatePresignedUrl(config.getBucketName(), config.getFileBasePath() + filePath, calendar.getTime(), httpMethod);
    }
}
