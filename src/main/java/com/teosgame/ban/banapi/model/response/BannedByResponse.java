package com.teosgame.ban.banapi.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teosgame.ban.banapi.model.entity.BannedByEntity;

import lombok.Data;

@Data
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BannedByResponse {
  String bannedById;
  String name;
  String banDate;

  public BannedByResponse(BannedByEntity entity) {
      bannedById = entity.getId();
      name = entity.getName();
      banDate = entity.getBanDate();
  }
}
