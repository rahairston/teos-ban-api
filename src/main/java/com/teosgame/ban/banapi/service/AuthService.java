package com.teosgame.ban.banapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.teosgame.ban.banapi.client.TwitchClient;
import com.teosgame.ban.banapi.client.model.response.TwitchTokenResponse;
import com.teosgame.ban.banapi.client.model.response.TwitchUserInfo;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.exception.TwitchResponseException;
import com.teosgame.ban.banapi.exception.UnknownException;
import com.teosgame.ban.banapi.exception.UserUnverifiedException;
import com.teosgame.ban.banapi.model.response.TokenResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthService {
    private final TwitchClient twitchClient;

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    public TokenResponse getTwitchToken(String authCode, String refresh) 
        throws TwitchResponseException, UnknownException, NotFoundException, UserUnverifiedException {
        TwitchTokenResponse token = null;
        if (authCode != null) {
            token = twitchClient.getTwitchAccessToken(authCode);
        } else {
            token = twitchClient.refreshTwitchAccessToken(refresh);
        }

        TwitchUserInfo userInfo = twitchClient.getUserInfo(token.getAccess_token());

        if (userInfo.getEmail() == null) {
            throw new UserUnverifiedException("User has not verified their email address.");
        }

        return TokenResponse.builder()
            .accessToken(token.getAccess_token())
            .refreshToken(token.getRefresh_token())
            .expiresIn(token.getExpires_in())
            .displayName(userInfo.getDisplay_name())
            .email(userInfo.getEmail())
            .profileImageUrl(userInfo.getProfile_image_url())
            .build();
    }
}
