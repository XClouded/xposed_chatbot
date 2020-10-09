package com.huawei.updatesdk.support.pm;

import com.huawei.updatesdk.support.pm.c;
import com.taobao.weex.BuildConfig;
import java.io.Serializable;
import java.util.Comparator;

public class b implements Serializable, Comparator<b> {
    private int a;
    private c.a b = c.a.NOT_HANDLER;
    private Object c;
    private boolean d = false;
    private String e;
    private String f;
    private c.b g = c.b.INSTALL;
    private int h = -1;

    protected b() {
    }

    protected b(String str, String str2, Object obj) {
        b(str2);
        a(str);
        a(obj);
    }

    /* renamed from: a */
    public int compare(b bVar, b bVar2) {
        return (!bVar.a() && bVar.b() > bVar2.b()) ? 1 : -1;
    }

    public void a(int i) {
        this.a = i;
    }

    public void a(c.a aVar) {
        this.b = (c.a) com.huawei.updatesdk.support.e.b.a(aVar);
    }

    public void a(c.b bVar) {
        this.g = bVar;
    }

    public void a(Object obj) {
        this.c = obj;
    }

    public void a(String str) {
        this.e = str;
    }

    public void a(boolean z) {
        this.d = z;
    }

    /* access modifiers changed from: protected */
    public boolean a() {
        return b() == -1;
    }

    public int b() {
        return this.a;
    }

    public void b(int i) {
        this.h = i;
    }

    public void b(String str) {
        this.f = str;
    }

    public c.a c() {
        return this.b;
    }

    public Object d() {
        return this.c;
    }

    public String e() {
        return this.e;
    }

    public String f() {
        return this.f;
    }

    public c.b g() {
        return this.g;
    }

    public int h() {
        return this.h;
    }

    public String toString() {
        String obj = d() == null ? BuildConfig.buildJavascriptFrameworkVersion : d().toString();
        return getClass().getName() + " {\n\tindex: " + b() + "\n\tstatus: " + c() + "\n\tparam: " + obj + "\n\tpackageName: " + e() + "\n\tpath: " + f() + "\n\tprocessType: " + g() + "\n}";
    }
}
