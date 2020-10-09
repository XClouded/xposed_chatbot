package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.tao.log.TLogConstant;

public class dy extends dx {
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean d;

    public dy(Context context, int i, boolean z, boolean z2, boolean z3, boolean z4) {
        super(context, i);
        this.a = z;
        this.b = z2;
        if (l.d()) {
            this.b = false;
        }
        this.c = z3;
        this.d = z4;
    }

    private String a(Context context) {
        return !this.d ? TLogConstant.TLOG_MODULE_OFF : "";
    }

    private String b() {
        if (!this.a) {
            return TLogConstant.TLOG_MODULE_OFF;
        }
        try {
            String c2 = c();
            if (TextUtils.isEmpty(c2)) {
                return "";
            }
            return ay.a(c2) + "," + ay.b(c2);
        } catch (Throwable unused) {
            return "";
        }
    }

    private String c() {
        return "";
    }

    private String d() {
        return !this.b ? TLogConstant.TLOG_MODULE_OFF : "";
    }

    private String e() {
        return !this.c ? TLogConstant.TLOG_MODULE_OFF : "";
    }

    public int a() {
        return 13;
    }

    /* renamed from: a  reason: collision with other method in class */
    public hi m187a() {
        return hi.DeviceBaseInfo;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m188a() {
        return b() + "|" + d() + "|" + e() + "|" + a(this.f241a);
    }
}
