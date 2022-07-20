package com.teosgame.ban.banapi.model.enums;

public enum BanStatus {
    PENDING("Pending"),
    REVIEWING("Reviewing"),
    UNBANNED("Unbanned"),
    BAN_UPHELD("Ban_Upheld"),
    RESUBMISSION_REQUIRED("Resubmission_Required");

    private String status;
    private BanStatus(String status) {
        this.status = status;
    }

    public static BanStatus fromStatus(String status) {
        for (BanStatus banStatus : BanStatus.values()) {
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
