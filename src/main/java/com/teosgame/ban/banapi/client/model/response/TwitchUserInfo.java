package com.teosgame.ban.banapi.client.model.response;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonAutoDetect
public class TwitchUserInfo {
    String email;
    String preferred_username;
    boolean email_verified;
    String picture;

    public static TwitchUserInfo fromClaims(DecodedJWT jwt) {
        return TwitchUserInfo.builder()
            .email(jwt.getClaim("email").asString())
            .preferred_username(jwt.getClaim("preferred_username").asString())
            .email_verified(jwt.getClaim("email_verified").asBoolean())
            .picture(jwt.getClaim("picture").asString())
            .build();
    }
}
