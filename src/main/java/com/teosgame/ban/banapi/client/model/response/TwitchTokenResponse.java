package com.teosgame.ban.banapi.client.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect
public class TwitchTokenResponse {
    String access_token;
    int expires_in;
    String refresh_token;
    String id_token;
    String nonce;
}
