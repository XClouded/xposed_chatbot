package com.ali.user.mobile.app.constant;

public enum LoginPriority {
    FACE_SMS_PWD("face>sms>pwd"),
    FACE_PWD_SMS("face>pwd>sms");
    
    private String priority;

    private LoginPriority(String str) {
        this.priority = str;
    }

    public String getPriority() {
        return this.priority;
    }
}
