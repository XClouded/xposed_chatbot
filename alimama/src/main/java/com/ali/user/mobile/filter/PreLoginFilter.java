package com.ali.user.mobile.filter;

import android.app.Activity;

public interface PreLoginFilter {
    void preHandle(Activity activity, String str, String str2, PreHandlerCallback preHandlerCallback);
}
