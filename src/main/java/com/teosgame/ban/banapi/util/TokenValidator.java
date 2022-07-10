package com.teosgame.ban.banapi.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.teosgame.ban.banapi.client.TwitchClient;
import com.teosgame.ban.banapi.client.model.response.TwitchUserInfo;
import com.teosgame.ban.banapi.enums.RoleType;
import com.teosgame.ban.banapi.model.SimpleAuthority;
import com.teosgame.ban.banapi.model.UserAuth;
import com.teosgame.ban.banapi.exception.InvalidTokenException;
import com.teosgame.ban.banapi.exception.TwitchResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenValidator {
    public static final String BEARER = "Bearer ";

    private final TwitchClient client;

    Logger logger = LoggerFactory.getLogger(JwtValidator.class);

    public Authentication validateToken(String authorizationHeader, String sessionId) throws AuthenticationException  {
        try {
            String tokenValue = subStringBearer(authorizationHeader);
            TwitchUserInfo userInfo = client.getUserInfo(tokenValue);
            List<SimpleAuthority> authorities = getUserAuthorities(userInfo.getPreferred_username());
            
            return new UserAuth(authorities, tokenValue, sessionId, userInfo, true);
        } catch (InvalidTokenException e) {
            logger.error(e.getMessage());
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        } catch (TwitchResponseException e) {
            logger.error(e.getMessage());
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    private String subStringBearer(String authorizationHeader) throws InvalidTokenException {
        try {
            return authorizationHeader.substring(TokenValidator.BEARER.length());
        } catch (Exception ex) {
            throw new InvalidTokenException("There is no AccessToken in a request header");
        }
    }

    private List<SimpleAuthority> getUserAuthorities(String username) {
        List<SimpleAuthority> authorities = new ArrayList<>();
        // check against list or something
        authorities.add(new SimpleAuthority(RoleType.USER));
        return authorities;
    }
}
