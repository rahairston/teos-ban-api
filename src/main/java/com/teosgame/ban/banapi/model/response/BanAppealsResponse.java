package com.teosgame.ban.banapi.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonAutoDetect
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BanAppealsResponse {
    int pageCount;  
    int pageSize;
    long totalPages;
    long totalSize;
    List<BanAppealResponse> appeals;
}
