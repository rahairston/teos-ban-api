package com.teosgame.ban.banapi.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teosgame.ban.banapi.client.model.request.TwitchTokenRequest;
import com.teosgame.ban.banapi.client.model.response.TwitchUserInfoWrapper;
import com.teosgame.ban.banapi.client.model.response.TwitchTokenResponse;
import com.teosgame.ban.banapi.client.model.response.TwitchUserInfo;
import com.teosgame.ban.banapi.enums.GrantType;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.exception.TwitchResponseException;
import com.teosgame.ban.banapi.exception.UnknownException;
import com.teosgame.ban.banapi.util.Credential;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TwitchClient {
    private final RestTemplate restTemplate;
    private final Credential credential;

    Logger logger = LoggerFactory.getLogger(TwitchClient.class);

    @Value("${ban.teo.redirect_uris:}")
    private String redirectUri;

    @Value("${twitch.url.auth:}")
    private String tokenUrl;

    @Value("${twitch.url.userinfo:}")
    private String userInfoUrl;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public TwitchTokenResponse getTwitchAccessToken(String authCode) 
        throws TwitchResponseException {
        TwitchTokenRequest body = new TwitchTokenRequest(
            credential.getClientId(), 
            credential.getClientSecret(),
            null, 
            authCode,
            GrantType.AUTHORIZATION_CODE.toString(), 
            redirectUri
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request 
            = new HttpEntity<MultiValueMap<String, String>>(convertToFormMap(body), headers);

        try {
            ResponseEntity<TwitchTokenResponse> response = restTemplate.postForEntity(tokenUrl,
                request, 
                TwitchTokenResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error Fetching Twitch Access Token");
            throw new TwitchResponseException(e.getLocalizedMessage(), e.getStatusCode());
        }
    }

    public TwitchTokenResponse refreshTwitchAccessToken(String refreshToken)
        throws TwitchResponseException {
        TwitchTokenRequest body = new TwitchTokenRequest(
            credential.getClientId(), 
            credential.getClientSecret(), 
            refreshToken, 
            null,
            GrantType.REFRESH_TOKEN.toString(), 
            redirectUri
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request 
            = new HttpEntity<MultiValueMap<String, String>>(convertToFormMap(body), headers);

        try {
            ResponseEntity<TwitchTokenResponse> response = restTemplate.postForEntity(tokenUrl,
                request, 
                TwitchTokenResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error Fetching Twitch Refresh Token");
            throw new TwitchResponseException(e.getLocalizedMessage(), e.getStatusCode());
        }
    }

    public TwitchUserInfo getUserInfo(String accessToken) 
        throws TwitchResponseException, UnknownException, NotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add("Client-Id", credential.getClientId());
        HttpEntity<Void> request = new HttpEntity<Void>(headers);

        try {
            ResponseEntity<TwitchUserInfoWrapper> response = restTemplate.exchange(userInfoUrl,
                HttpMethod.GET, 
                request, 
                TwitchUserInfoWrapper.class);
            if (response.getBody() == null || response.getBody().getData() == null) {
                logger.error("Twitch UserInfo Endpoint responded with null data");
                throw new UnknownException("Twitch UserInfo Endpoint responded with null data");
            }

            if (response.getBody().getData().length == 0) {
                logger.error("Twitch UserInfo Endpoint responded empty array. User not found?");
                throw new NotFoundException("UserInfo could not be found.");
            }

            return response.getBody().getData()[0];
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error Fetching Twitch UserInfo");
            throw new TwitchResponseException(e.getLocalizedMessage(), e.getStatusCode());
        }
    }

    private MultiValueMap<String, String> convertToFormMap(Object o) {
        Map<String, String> converted = objectMapper.convertValue(o, Map.class);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        converted.keySet().forEach(key -> {
            if (converted.get(key) != null) {
                requestBody.add(key, converted.get(key));
            }
        });
        return requestBody;
    }
}
