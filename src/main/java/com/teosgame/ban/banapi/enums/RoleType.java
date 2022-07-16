package com.teosgame.ban.banapi.enums;

public enum RoleType {
    ADMIN("ROLE_ADMIN"),
    DEVELOPER("ROLE_DEVELOPER"),
    USER("ROLE_USER");

    private String type;
    private RoleType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString(){
        return type;
    }
}
