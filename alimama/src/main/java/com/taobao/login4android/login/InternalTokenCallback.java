package com.taobao.login4android.login;

public interface InternalTokenCallback {
    void onFail(String str, String str2);

    void onSucess(String str);
}
