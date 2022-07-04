package com.teosgame.ban.banapi.enums;

public enum GrantType {
    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token");

    private String grant;
    private GrantType(String grant) {
        this.grant = grant;
    }
    
    @Override
    public String toString(){
        return grant;
    }
}
