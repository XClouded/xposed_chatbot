package com.alibaba.android.prefetchx.adapter;

import com.taobao.tao.remotebusiness.login.RemoteLogin;

public class LoginAdapterImpl implements LoginAdapter {
    public boolean isLoginIn() {
        return RemoteLogin.isSessionValid();
    }
}
