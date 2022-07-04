package com.teosgame.ban.banapi.client.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect
public class TwitchUserInfo {
    String email;
    String display_name;
    String profile_image_url;
}
