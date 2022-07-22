package com.teosgame.ban.banapi.model.response;

import java.util.List;

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
    String banReason;
    Boolean banJustified;
    String appealReason;
    String additionalNotes;
    ////////////////// RESUBMISSION VARIABLES //////////////////
    BanAppealResponse previous;
    String additionalData;
    ////////////////// ADMIN VARIABLES //////////////////
    List<EvidenceResponse> evidence;
    JudgementResponse judgement;
}
