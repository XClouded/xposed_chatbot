package com.xiaomi.push;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.taobao.tao.log.TLogConstant;

public class dz extends dx {
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean d;
    private boolean e;

    public dz(Context context, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        super(context, i);
        this.a = z;
        this.b = z2;
        this.c = z3;
        this.d = z4;
        this.e = z5;
    }

    private String b() {
        if (!this.a) {
            return TLogConstant.TLOG_MODULE_OFF;
        }
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) this.f241a.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels + "," + displayMetrics.widthPixels;
        } catch (Throwable unused) {
            return "";
        }
    }

    private String c() {
        if (!this.b) {
            return TLogConstant.TLOG_MODULE_OFF;
        }
        try {
            return Build.VERSION.RELEASE;
        } catch (Throwable unused) {
            return "";
        }
    }

    private String d() {
        if (!this.c) {
            return TLogConstant.TLOG_MODULE_OFF;
        }
        try {
            return String.valueOf(Build.VERSION.SDK_INT);
        } catch (Throwable unused) {
            return "";
        }
    }

    private String e() {
        if (!this.d) {
            return TLogConstant.TLOG_MODULE_OFF;
        }
        try {
            return Settings.Secure.getString(this.f241a.getContentResolver(), "android_id");
        } catch (Throwable unused) {
            return "";
        }
    }

    private String f() {
        if (!this.e) {
            return TLogConstant.TLOG_MODULE_OFF;
        }
        try {
            return ((TelephonyManager) this.f241a.getSystemService("phone")).getSimOperator();
        } catch (Throwable unused) {
            return "";
        }
    }

    public int a() {
        return 3;
    }

    /* renamed from: a  reason: collision with other method in class */
    public hi m189a() {
        return hi.DeviceInfoV2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m190a() {
        return b() + "|" + c() + "|" + d() + "|" + e() + "|" + f();
    }
}
