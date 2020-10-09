package com.xiaomi.push.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import com.xiaomi.push.ab;

public class as {
    private static as a;

    /* renamed from: a  reason: collision with other field name */
    private int f866a = 0;

    /* renamed from: a  reason: collision with other field name */
    private Context f867a;

    private as(Context context) {
        this.f867a = context.getApplicationContext();
    }

    public static as a(Context context) {
        if (a == null) {
            a = new as(context);
        }
        return a;
    }

    @SuppressLint({"NewApi"})
    public int a() {
        if (this.f866a != 0) {
            return this.f866a;
        }
        this.f866a = Build.VERSION.SDK_INT >= 17 ? Settings.Global.getInt(this.f867a.getContentResolver(), "device_provisioned", 0) : Settings.Secure.getInt(this.f867a.getContentResolver(), "device_provisioned", 0);
        return this.f866a;
    }

    @SuppressLint({"NewApi"})
    /* renamed from: a  reason: collision with other method in class */
    public Uri m579a() {
        return Build.VERSION.SDK_INT >= 17 ? Settings.Global.getUriFor("device_provisioned") : Settings.Secure.getUriFor("device_provisioned");
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m580a() {
        return ab.f108a.contains("xmsf") || ab.f108a.contains("xiaomi") || ab.f108a.contains("miui");
    }
}
