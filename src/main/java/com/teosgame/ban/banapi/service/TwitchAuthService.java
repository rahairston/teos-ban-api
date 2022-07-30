package com.teosgame.ban.banapi.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.teosgame.ban.banapi.client.RedisClient;
import com.teosgame.ban.banapi.client.TwitchClient;
import com.teosgame.ban.banapi.client.model.response.TwitchTokenResponse;
import com.teosgame.ban.banapi.client.model.response.TwitchUserInfo;
import com.teosgame.ban.banapi.config.RoleConfig;
import com.teosgame.ban.banapi.exception.InvalidJwtException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.exception.TwitchResponseException;
import com.teosgame.ban.banapi.exception.UnknownException;
import com.teosgame.ban.banapi.exception.UserUnverifiedException;
import com.teosgame.ban.banapi.model.response.TokenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TwitchAuthService {
    private final TwitchClient twitchClient;
    private final JwtValidatorService jwtValidator;
    private final RedisClient redisClient;
    private final RoleConfig roleConfig;

    Logger logger = LoggerFactory.getLogger(TwitchAuthService.class);

    public TokenResponse getTwitchToken(String authCode, String nonce) 
        throws TwitchResponseException, UnknownException, 
            NotFoundException, UserUnverifiedException,
            InvalidJwtException {
        TwitchTokenResponse token = twitchClient.getTwitchAccessToken(authCode);

        if (!token.getNonce().equals(nonce)) {
            throw new InvalidJwtException("Nonce is not equal");
        }

        TwitchUserInfo userInfo = TwitchUserInfo.fromClaims(jwtValidator.validateAndParse(token.getId_token()));

        if (!userInfo.isEmail_verified()) {
            throw new UserUnverifiedException("User has not verified their email address.");
        }

        ServletRequestAttributes attr = (ServletRequestAttributes) 
        RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);

        redisClient.setValue(userInfo.getPreferred_username().toLowerCase(), session.getId());
        
        List<String> roles = roleConfig.getUserRoles(userInfo.getPreferred_username()).stream()
            .map(role -> role.getAuthority().substring("ROLE_".length()))
            .collect(Collectors.toList());

        return TokenResponse.builder()
            .accessToken(token.getAccess_token())
            .refreshToken(token.getRefresh_token())
            .expiresIn(token.getExpires_in())
            .displayName(userInfo.getPreferred_username())
            .email(userInfo.getEmail())
            .profileImageUrl(userInfo.getPicture())
            .roles(roles)
            .build();
    }

    public TokenResponse refreshToken(String refresh) throws TwitchResponseException {
        TwitchTokenResponse token = twitchClient.refreshTwitchAccessToken(refresh);
        return TokenResponse.builder()
            .accessToken(token.getAccess_token())
            .refreshToken(token.getRefresh_token())
            .expiresIn(token.getExpires_in())
            .build();
    }
}
