package com.teosgame.ban.banapi.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teosgame.ban.banapi.model.enums.BanType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonAutoDetect
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BanAppealResponse {
    String appealId;
    String twitchUsername;
    String discordUsername;
    BanType banType;
    String banStatus;
    String banReason;
    Boolean banJustified;
    String appealReason;
    String additionalNotes;
    String previousAppealId;
    String additionalData;
}
