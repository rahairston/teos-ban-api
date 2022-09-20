package com.teosgame.ban.banapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonAutoDetect
@NoArgsConstructor
public class TwitchSecret {
    String TWITCH_CLIENT_SECRET;
    String TWITCH_CLIENT_ID;
}
