package com.teosgame.ban.banapi.model.enums;

import lombok.Getter;

@Getter
public enum BannedByAction {
  CREATE("CREATE"),
  UPDATE("UPDATE"),
  DELETE("DELETE");

  private String action;
  private BannedByAction(String action) {
    this.action = action;
  }

  public boolean isCreating() {
    return this.equals(CREATE);
  }

  public boolean isUpdating() {
    return this.equals(UPDATE);
  }

  public boolean isDeleting() {
    return this.equals(DELETE);
  }
}
