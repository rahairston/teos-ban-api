package com.teosgame.ban.banapi.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teosgame.ban.banapi.model.entity.EvidenceEntity;

import lombok.Data;

@Data
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvidenceResponse {
    
    String evidenceId;
    String notes;
    String preSignedUrl;

    public EvidenceResponse(EvidenceEntity entity, String preSignedUrl) {
        evidenceId = entity.getId();
        notes = entity.getNotes();
        this.preSignedUrl = preSignedUrl;
    }
}
