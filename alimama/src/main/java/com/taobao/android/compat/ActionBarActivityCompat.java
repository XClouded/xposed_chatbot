package com.taobao.android.compat;

import android.annotation.TargetApi;
import android.os.Build;
import androidx.appcompat.app.ActionBarActivity;

public class ActionBarActivityCompat extends ActionBarActivity implements ActivityCompatJellyBean {
    private boolean mDestroyed;

    @TargetApi(17)
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT < 17) {
            return this.mDestroyed;
        }
        try {
            return ActionBarActivityCompat.super.isDestroyed();
        } catch (Throwable unused) {
            return this.mDestroyed;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mDestroyed = true;
        ActionBarActivityCompat.super.onDestroy();
    }

    private ApplicationCompat getApplicationCompat() {
        return (ApplicationCompat) getApplication();
    }
}
