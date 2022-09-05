package com.teosgame.ban.banapi.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teosgame.ban.banapi.model.entity.AppealEntity;
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
    String previousAppealId;
    String additionalData;
    ////////////////// ADMIN VARIABLES //////////////////
    String adminNotes;
    List<EvidenceResponse> evidence;
    JudgementResponse judgement;
    List<BannedByResponse> bannedBy;
    ////////////////// PAGING VARIABLES //////////////////
    String prevPageId;
    String nextPageId;

    public static BanAppealResponse fromEntity(AppealEntity entity, 
        String previousId, 
        List<EvidenceResponse> evidence,
        List<BannedByResponse> bannedBy,
        String prevPageId,
        String nextPageId) {
        return BanAppealResponse.builder()
            .appealId(entity.getId())
            .twitchUsername(entity.getTwitchUsername())
            .discordUsername(entity.getDiscordUsername())
            .banType(entity.getBanType())
            .banReason(entity.getBanReason())
            .banJustified(entity.getBanJustified())
            .appealReason(entity.getAppealReason())
            .additionalNotes(entity.getAdditionalNotes())
            .adminNotes(entity.getAdminNotes())
            .previousAppealId(previousId)
            .additionalData(entity.getAdditionalData())
            .evidence(evidence)
            .judgement(new JudgementResponse(entity.getJudgement()))
            .prevPageId(prevPageId)
            .nextPageId(nextPageId)
            .bannedBy(bannedBy)
        .build();
    }
}
