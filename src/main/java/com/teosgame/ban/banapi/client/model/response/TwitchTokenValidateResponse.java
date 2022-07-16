package com.teosgame.ban.banapi.client.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect
public class TwitchTokenValidateResponse {
    String login;
}
