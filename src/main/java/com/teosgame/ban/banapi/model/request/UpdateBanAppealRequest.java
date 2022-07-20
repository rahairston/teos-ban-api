package com.teosgame.ban.banapi.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teosgame.ban.banapi.model.enums.BanType;
import com.teosgame.ban.banapi.util.BanUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateBanAppealRequest {

    @NotNull(message="Please provide a Twitch Username.")
    String twitchUsername;
    String discordUsername;
    BanType banType;
    String banReason;
    Boolean banJustified;
    String appealReason;
    String additionalNotes;
    // for resubmission
    @Pattern(regexp = BanUtils.GUID_PATTERN)
    String previousAppealId;
    String additionalData;
}
