package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.xiaomi.push.ai;
import com.xiaomi.push.bw;
import com.xiaomi.push.service.ag;
import com.xiaomi.push.service.be;

public class bk {
    private static volatile bk a;

    /* renamed from: a  reason: collision with other field name */
    private Context f144a;

    /* renamed from: a  reason: collision with other field name */
    private ai.a f145a = new bl(this);

    /* renamed from: a  reason: collision with other field name */
    private bz f146a;

    /* renamed from: a  reason: collision with other field name */
    private ca f147a;

    /* renamed from: a  reason: collision with other field name */
    private final String f148a = "push_stat_sp";
    private ai.a b = new bm(this);

    /* renamed from: b  reason: collision with other field name */
    private final String f149b = "upload_time";
    private ai.a c = new bn(this);

    /* renamed from: c  reason: collision with other field name */
    private final String f150c = "delete_time";
    private final String d = "check_time";
    private String e;
    private String f;

    private bk(Context context) {
        this.f144a = context;
    }

    public static bk a(Context context) {
        if (a == null) {
            synchronized (bk.class) {
                if (a == null) {
                    a = new bk(context);
                }
            }
        }
        return a;
    }

    private boolean a() {
        return ag.a(this.f144a).a(hl.StatDataSwitch.a(), true);
    }

    /* access modifiers changed from: private */
    public void b(String str) {
        SharedPreferences.Editor edit = this.f144a.getSharedPreferences("push_stat_sp", 0).edit();
        edit.putLong(str, System.currentTimeMillis());
        r.a(edit);
    }

    private String c() {
        return this.f144a.getDatabasePath(bo.f152a).getAbsolutePath();
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m122a() {
        return this.e;
    }

    public void a(bw.a aVar) {
        bw.a(this.f144a).a(aVar);
    }

    public void a(hk hkVar) {
        if (a() && be.a(hkVar.e())) {
            a((bw.a) bt.a(this.f144a, c(), hkVar));
        }
    }

    public void a(String str) {
        if (a() && !TextUtils.isEmpty(str)) {
            a(cb.a(this.f144a, str));
        }
    }

    public void a(String str, String str2, Boolean bool) {
        if (this.f146a == null) {
            return;
        }
        if (bool.booleanValue()) {
            this.f146a.a(this.f144a, str2, str);
        } else {
            this.f146a.b(this.f144a, str2, str);
        }
    }

    public String b() {
        return this.f;
    }
}
