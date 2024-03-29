package com.teosgame.ban.banapi.model.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teosgame.ban.banapi.model.enums.BannedByAction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BannedByRequest {

  String bannedById;
  BannedByAction action;

  @NotNull(message="Please provide a Username for who banned it.")
  String name;

  @NotNull(message="Please provide a Ban Date.")
  String banDate;
}
