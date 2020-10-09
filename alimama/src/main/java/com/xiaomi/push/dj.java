package com.xiaomi.push;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

public class dj implements Application.ActivityLifecycleCallbacks {
    private Context a;

    /* renamed from: a  reason: collision with other field name */
    private String f235a = "";
    private String b;

    public dj(Context context, String str) {
        this.a = context;
        this.f235a = str;
    }

    private void a(String str) {
        ho hoVar = new ho();
        hoVar.a(str);
        hoVar.a(System.currentTimeMillis());
        hoVar.a(hi.ActivityActiveTimeStamp);
        dx.a(this.a, hoVar);
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
        String localClassName = activity.getLocalClassName();
        if (!TextUtils.isEmpty(this.f235a) && !TextUtils.isEmpty(localClassName)) {
            this.b = "";
            if (TextUtils.isEmpty(this.b) || TextUtils.equals(this.b, localClassName)) {
                a(this.a.getPackageName() + "|" + localClassName + ":" + this.f235a + "," + String.valueOf(System.currentTimeMillis() / 1000));
                this.f235a = "";
                this.b = "";
                return;
            }
            this.f235a = "";
        }
    }

    public void onActivityResumed(Activity activity) {
        if (TextUtils.isEmpty(this.b)) {
            this.b = activity.getLocalClassName();
        }
        this.f235a = String.valueOf(System.currentTimeMillis() / 1000);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }
}
