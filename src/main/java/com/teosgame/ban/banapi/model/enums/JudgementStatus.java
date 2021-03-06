package com.teosgame.ban.banapi.model.enums;

public enum JudgementStatus {
    PENDING("Pending"), // user submits
    REVIEWING("Reviewing"), // evidence applied
    UNBANNED("Unbanned"), // decision made
    BAN_UPHELD("Ban Upheld"); // decision made

    private String status;
    private JudgementStatus(String status) {
        this.status = status;
    }

    public static JudgementStatus fromStatus(String status) {
        for (JudgementStatus banStatus : JudgementStatus.values()) {
            if (banStatus.toString().equals(status)) {
                return banStatus;
            }
        }

        return null;
    }

    public boolean isPending() {
        return this.equals(PENDING);
    }
    
    @Override
    public String toString(){
        return status;
    }
}
