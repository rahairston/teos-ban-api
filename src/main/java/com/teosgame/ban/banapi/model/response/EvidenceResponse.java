package com.teosgame.ban.banapi.model.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teosgame.ban.banapi.model.entity.BannedByEntity;
import com.teosgame.ban.banapi.model.entity.EvidenceEntity;

import lombok.Data;

@Data
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvidenceResponse {
    
    String evidenceId;
    String notes;
    String preSignedUrl;
    List<BannedByResponse> bannedBy;

    public EvidenceResponse(EvidenceEntity entity, List<BannedByEntity> bannedByEntities, String preSignedUrl) {
        evidenceId = entity.getId();
        notes = entity.getNotes();
        this.preSignedUrl = preSignedUrl;
        bannedBy = bannedByEntities.stream().map(bannedByEntity -> {
            return new BannedByResponse(bannedByEntity);
        }).collect(Collectors.toList());
        
    }
}
