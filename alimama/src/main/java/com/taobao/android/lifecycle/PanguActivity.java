package com.taobao.android.lifecycle;

import android.annotation.TargetApi;
import android.os.Build;
import androidx.appcompat.app.ActionBarActivity;
import com.taobao.android.compat.ActivityCompatJellyBean;
import com.taobao.android.compat.ApplicationCompat;

public class PanguActivity extends ActionBarActivity implements ActivityCompatJellyBean {
    private boolean mDestroyed;

    @TargetApi(17)
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT < 17) {
            return this.mDestroyed;
        }
        try {
            return PanguActivity.super.isDestroyed();
        } catch (Throwable unused) {
            return this.mDestroyed;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mDestroyed = true;
        PanguActivity.super.onDestroy();
    }

    public PanguApplication getPanguApplication() {
        return (PanguApplication) getApplication();
    }

    private ApplicationCompat getApplicationCompat() {
        return (ApplicationCompat) getApplication();
    }
}
