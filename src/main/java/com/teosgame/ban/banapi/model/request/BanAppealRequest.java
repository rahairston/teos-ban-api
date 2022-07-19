package com.teosgame.ban.banapi.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teosgame.ban.banapi.model.enums.BanType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class BanAppealRequest {

    @NotNull(message="Please provide a Twitch Username.")
    String twitchUsername;
    String discordUsername;

    @NotNull(message="Please provide ban type.")
    BanType banType;

    @NotNull(message="Please provide a reason for your ban.")
    @Size(min = 10)
    String banReason;

    @NotNull(message="Please provide if your ban was justified or not.")
    Boolean banJustified;

    @NotNull(message="Please provide the reason for your appeal.")
    @Size(min = 10)
    String appealReason;

    String additionalNotes;

    // for resubmission
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String previousAppealId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String additionalData;
}
