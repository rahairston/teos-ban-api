package com.teosgame.ban.banapi.service;

import java.util.Base64;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.teosgame.ban.banapi.config.AWSConfig;
import com.teosgame.ban.banapi.config.SecretsConfig;
import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.model.TwitchSecret;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class SecretsManagerService {
    private final AWSConfig awsConfig;
    private final SecretsConfig config;

    private String clientId;
    private String clientSecret;
    private static ObjectMapper objectMapper = new ObjectMapper();

    Logger logger = LoggerFactory.getLogger(SecretsManagerService.class);

    @PostConstruct
    public void postConstruct() throws BadRequestException, JsonMappingException, JsonProcessingException {
        String secretValue = getSecret();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_SNAKE_CASE);
        TwitchSecret secret = objectMapper.readValue(secretValue, TwitchSecret.class);
        setClientId(secret.getTWITCH_CLIENT_ID());
        setClientSecret(secret.getTWITCH_CLIENT_SECRET());
    }
  
    private String getSecret() throws BadRequestException {
        String secretName = config.getSecretName();
        String region = awsConfig.getRegionName();

        // Create a Secrets Manager client
        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
                                        .withRegion(region)
                                        .build();
        
        // In this sample we only handle the specific exceptions for the 'GetSecretValue' API.
        // See https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
        // We rethrow the exception by default.
        
        String secret = null, decodedBinarySecret = null;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                        .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            logger.error("Error initializing Twitch App Secrets: {}", e);
            throw new BadRequestException(e.getLocalizedMessage());
        }

        // Decrypts secret using the associated KMS key.
        // Depending on whether the secret is a string or binary, one of these fields will be populated.
        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
        }
        else {
            decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
        }

        if (secret != null) {
            return secret;
        } else if (decodedBinarySecret != null) {
            return decodedBinarySecret;
        } else {
            throw new BadRequestException("Unable to decode secret.");
        }
    }
}
