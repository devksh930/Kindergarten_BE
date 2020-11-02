package com.kindergarten.api.model.entity;

public enum AccessInfo {
    //    현재 유치원 다니는 학생 CURRENT, 다녔었던 학생 NOT_CURRENT
    ACCESS("인증된 리뷰"), NOT_ACCESS("인증되지 않은 리뷰");

    private String AccessInfo;

    AccessInfo(String AccessInfo) {
        this.AccessInfo = AccessInfo;
    }

    public String getAccessInfo() {
        return AccessInfo;
    }

}
