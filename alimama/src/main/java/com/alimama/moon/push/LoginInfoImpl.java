package com.alimama.moon.push;

import com.taobao.accs.ILoginInfo;
import com.taobao.login4android.Login;

public class LoginInfoImpl implements ILoginInfo {
    private static LoginInfoImpl instance;

    private LoginInfoImpl() {
    }

    public static LoginInfoImpl getInstance() {
        if (instance == null) {
            instance = new LoginInfoImpl();
        }
        return instance;
    }

    public String getSid() {
        return Login.getSid();
    }

    public String getUserId() {
        return Login.getUserId();
    }

    public String getNick() {
        return Login.getNick();
    }

    public String getEcode() {
        return Login.getEcode();
    }

    public String getHeadPicLink() {
        return Login.getHeadPicLink();
    }

    public String getSsoToken() {
        return Login.session != null ? Login.session.getSsoToken() : "";
    }

    public boolean getCommentUsed() {
        return Login.getCommentUsed();
    }
}
