package com.teosgame.ban.banapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TokenResponse {
    String email;
    String displayName;
    String profileImageUrl;
    String sessionId;
    String accessToken;
    String refreshToken;
    int expiresIn;
}
