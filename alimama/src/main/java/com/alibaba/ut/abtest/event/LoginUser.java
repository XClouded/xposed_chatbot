package com.alibaba.ut.abtest.event;

public class LoginUser {
    private String userId;
    private String userNick;

    public LoginUser() {
    }

    public LoginUser(String str, String str2) {
        this.userId = str;
        this.userNick = str2;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public String getUserNick() {
        return this.userNick;
    }

    public void setUserNick(String str) {
        this.userNick = str;
    }
}
