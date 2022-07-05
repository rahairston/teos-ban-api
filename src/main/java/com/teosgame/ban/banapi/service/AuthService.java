package com.teosgame.ban.banapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.teosgame.ban.banapi.client.TwitchClient;
import com.teosgame.ban.banapi.client.model.response.TwitchTokenResponse;
import com.teosgame.ban.banapi.client.model.response.TwitchUserInfo;
import com.teosgame.ban.banapi.exception.InvalidJwtException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.exception.TwitchResponseException;
import com.teosgame.ban.banapi.exception.UnknownException;
import com.teosgame.ban.banapi.exception.UserUnverifiedException;
import com.teosgame.ban.banapi.model.response.TokenResponse;
import com.teosgame.ban.banapi.util.JwtValidator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthService {
    private final TwitchClient twitchClient;
    private final JwtValidator jwtValidator;

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    public TokenResponse getTwitchToken(String authCode, String refresh, String nonce) 
        throws TwitchResponseException, UnknownException, 
            NotFoundException, UserUnverifiedException,
            InvalidJwtException {
        TwitchTokenResponse token = null;
        if (authCode != null) {
            token = twitchClient.getTwitchAccessToken(authCode);
        } else {
            token = twitchClient.refreshTwitchAccessToken(refresh);
        }

        if (!token.getNonce().equals(nonce)) {

        }

        TwitchUserInfo userInfo = TwitchUserInfo.fromClaims(jwtValidator.validateAndParse(token.getId_token()));

        if (!userInfo.isEmail_verified()) {
            throw new UserUnverifiedException("User has not verified their email address.");
        }

        return TokenResponse.builder()
            .accessToken(token.getAccess_token())
            .refreshToken(token.getRefresh_token())
            .expiresIn(token.getExpires_in())
            .displayName(userInfo.getPreferred_username())
            .email(userInfo.getEmail())
            .profileImageUrl(userInfo.getPicture())
            .build();
    }
}
