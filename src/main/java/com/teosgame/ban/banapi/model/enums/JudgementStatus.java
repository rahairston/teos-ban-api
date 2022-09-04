package com.teosgame.ban.banapi.model.enums;

import com.teosgame.ban.banapi.exception.BadRequestException;

public enum JudgementStatus {
    PENDING("Pending"), // user submits
    REVIEWING("Reviewing"), // evidence applied
    UNBANNED("Unbanned"), // decision made
    BAN_UPHELD("Ban Upheld"); // decision made

    private String status;
    private JudgementStatus(String status) {
        this.status = status;
    }

    public static JudgementStatus fromStatus(String status) throws BadRequestException {
        if (status == null) {
            return null;
        }

        for (JudgementStatus banStatus : JudgementStatus.values()) {
            if (banStatus.toString().equalsIgnoreCase(status)) {
                return banStatus;
            }
        }

        throw new BadRequestException("Unknown judgement status: " + status);
    }

    public boolean isPending() {
        return this.equals(PENDING);
    }

    public boolean isUpheld() {
        return this.equals(BAN_UPHELD);
    }
    
    @Override
    public String toString(){
        return status;
    }
}
