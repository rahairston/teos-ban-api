package com.teosgame.ban.banapi.client.model.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TwitchTokenRequest implements Serializable {
    String client_id;
    String client_secret;
    String refresh_token;
    String code;
    String grant_type;
    String redirect_uri;   
}
