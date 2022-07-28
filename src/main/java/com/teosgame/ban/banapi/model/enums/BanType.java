package com.teosgame.ban.banapi.model.enums;

import com.teosgame.ban.banapi.exception.BadRequestException;

public enum BanType {
    TWITCH,
    DISCORD,
    BOTH;

    public static BanType fromType(String type) throws BadRequestException  {
        if (type == null) {
            return null;
        }

        for (BanType banType : BanType.values()) {
            if (banType.toString().equals(type)) {
                return banType;
            }
        }

        throw new BadRequestException("Unknown ban type: " + type);
    }
}
