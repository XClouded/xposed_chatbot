package com.ali.user.mobile.filter;

import android.app.Activity;
import android.content.Intent;

public interface OnActivityResultHandler {
    void onActivityResult(int i, int i2, Intent intent, Activity activity, String str, String str2, PreHandlerCallback preHandlerCallback);
}
