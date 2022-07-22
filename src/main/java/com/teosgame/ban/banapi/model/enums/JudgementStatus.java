package com.teosgame.ban.banapi.model.enums;

public enum JudgementStatus {
    PENDING("Pending"),
    REVIEWING("Reviewing"),
    UNBANNED("Unbanned"),
    BAN_UPHELD("Ban_Upheld"),
    RESUBMISSION_REQUIRED("Resubmission_Required");

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
    
    @Override
    public String toString(){
        return status;
    }
}
