package com.alibaba.taffy.core.login;

public class LoginContext {
    private String ecode;
    private String nick;
    private String sessionId;
    private String userId;

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String str) {
        this.sessionId = str;
    }

    public String getEcode() {
        return this.ecode;
    }

    public void setEcode(String str) {
        this.ecode = str;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String str) {
        this.nick = str;
    }
}
