package com.alibaba.taffy.core.login;

public interface TLoginManager {
    void addListener(LoginListener loginListener);

    void destroy();

    LoginContext getLoginContext();

    void init();

    boolean isSessionValid();

    void login();

    void logout();

    void removeListener(LoginListener loginListener);
}
