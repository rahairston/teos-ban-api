package com.teosgame.ban.banapi.model.enums;

public enum BanType {
    TWITCH,
    DISCORD,
    BOTH;

    public static BanType fromType(String type) {
        for (BanType banType : BanType.values()) {
            if (banType.toString().equals(type)) {
                return banType;
            }
        }

        return null;
    }
}
