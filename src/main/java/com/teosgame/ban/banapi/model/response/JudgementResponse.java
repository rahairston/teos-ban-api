package com.teosgame.ban.banapi.model.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teosgame.ban.banapi.model.entity.JudgementEntity;

import lombok.Data;

@Data
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JudgementResponse {
    String judgementId;
    String status;
    String notes;
    Date resubmitAfterDate;

    public JudgementResponse(JudgementEntity entity) {
        judgementId = entity.getId();
        status = entity.getStatus().toString();
        notes = entity.getNotes();
        resubmitAfterDate = entity.getResubmitAfterDate();
    }
}
