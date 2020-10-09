package com.ali.user.mobile.filter;

import android.app.Activity;

public interface LoginFilter {
    void onLoginFail(int i, String str);

    void onLoginSuccess(Activity activity, LoginFilterCallback loginFilterCallback);
}
