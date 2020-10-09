package com.xiaomi.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.taobao.weex.BuildConfig;
import java.util.HashMap;

public final class ek {
    private static volatile ek a;

    /* renamed from: a  reason: collision with other field name */
    private int f310a;

    /* renamed from: a  reason: collision with other field name */
    private Context f311a;

    /* renamed from: a  reason: collision with other field name */
    private eo f312a;

    /* renamed from: a  reason: collision with other field name */
    private String f313a;

    /* renamed from: a  reason: collision with other field name */
    private HashMap<em, en> f314a = new HashMap<>();
    private String b;

    private ek(Context context) {
        this.f311a = context;
        this.f314a.put(em.SERVICE_ACTION, new eq());
        this.f314a.put(em.SERVICE_COMPONENT, new er());
        this.f314a.put(em.ACTIVITY, new ei());
        this.f314a.put(em.PROVIDER, new ep());
    }

    public static ek a(Context context) {
        if (a == null) {
            synchronized (ek.class) {
                if (a == null) {
                    a = new ek(context);
                }
            }
        }
        return a;
    }

    /* access modifiers changed from: private */
    public void a(em emVar, Context context, ej ejVar) {
        this.f314a.get(emVar).a(context, ejVar);
    }

    public int a() {
        return this.f310a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public eo m275a() {
        return this.f312a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m276a() {
        return this.f313a;
    }

    public void a(int i) {
        this.f310a = i;
    }

    public void a(Context context, String str, int i, String str2, String str3) {
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            eg.a(context, "" + str, 1008, "A receive a incorrect message");
            return;
        }
        a(i);
        ai.a(this.f311a).a((Runnable) new el(this, str, context, str2, str3));
    }

    public void a(em emVar, Context context, Intent intent, String str) {
        if (emVar != null) {
            this.f314a.get(emVar).a(context, intent, str);
        } else {
            eg.a(context, BuildConfig.buildJavascriptFrameworkVersion, 1008, "A receive a incorrect message with empty type");
        }
    }

    public void a(eo eoVar) {
        this.f312a = eoVar;
    }

    public void a(String str) {
        this.f313a = str;
    }

    public void a(String str, String str2, int i, eo eoVar) {
        a(str);
        b(str2);
        a(i);
        a(eoVar);
    }

    public String b() {
        return this.b;
    }

    public void b(String str) {
        this.b = str;
    }
}
