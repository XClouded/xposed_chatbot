package com.alibaba.taffy.core.login;

public class LoginEvent {
    private int action;
    private LoginContext context;

    public int getAction() {
        return this.action;
    }

    public void setAction(int i) {
        this.action = i;
    }

    public LoginContext getContext() {
        return this.context;
    }

    public void setContext(LoginContext loginContext) {
        this.context = loginContext;
    }
}
