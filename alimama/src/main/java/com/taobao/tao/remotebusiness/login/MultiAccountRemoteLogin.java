package com.taobao.tao.remotebusiness.login;

import androidx.annotation.Nullable;

public abstract class MultiAccountRemoteLogin implements IRemoteLogin {
    public abstract LoginContext getLoginContext(@Nullable String str);

    public abstract boolean isLogining(@Nullable String str);

    public abstract boolean isSessionValid(@Nullable String str);

    public abstract void login(@Nullable String str, onLoginListener onloginlistener, boolean z);

    @Deprecated
    public void login(onLoginListener onloginlistener, boolean z) {
        login((String) null, onloginlistener, z);
    }

    @Deprecated
    public boolean isSessionValid() {
        return isSessionValid((String) null);
    }

    @Deprecated
    public boolean isLogining() {
        return isLogining((String) null);
    }

    @Deprecated
    public LoginContext getLoginContext() {
        return getLoginContext((String) null);
    }
}
