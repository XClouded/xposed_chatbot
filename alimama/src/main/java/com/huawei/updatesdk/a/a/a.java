package com.huawei.updatesdk.a.a;

import com.huawei.updatesdk.sdk.a.d.c.b;
import com.huawei.updatesdk.sdk.service.c.a.c;

public class a extends c {
    private String clientPackage_ = null;
    private String cno_ = null;
    private String code_ = null;
    private boolean isSerial = false;
    private String locale_ = null;
    private boolean needSign = true;
    private String net_ = null;
    private int serviceType_ = 0;
    private String sign_ = null;
    private String thirdId_ = null;
    private String ts_ = null;

    public a() {
        a(com.huawei.updatesdk.service.a.a.a().d());
        b(com.huawei.updatesdk.sdk.service.a.a.a().b().getPackageName());
    }

    public String a() {
        return this.net_;
    }

    public void a(String str) {
        this.sign_ = str;
    }

    public void a(boolean z) {
        this.needSign = z;
    }

    public void b(String str) {
        this.clientPackage_ = str;
    }

    public void b(boolean z) {
        this.isSerial = z;
    }

    public boolean b() {
        return this.needSign;
    }

    public void c(String str) {
        this.net_ = str;
    }

    public boolean c() {
        return this.isSerial;
    }

    public String d() throws IllegalAccessException, IllegalArgumentException {
        return super.d();
    }

    public void d(String str) {
        this.cno_ = str;
    }

    /* access modifiers changed from: protected */
    public void e() {
        e(String.valueOf(System.currentTimeMillis()));
        c(String.valueOf(b.c(com.huawei.updatesdk.sdk.service.a.a.a().b())));
        g(com.huawei.updatesdk.service.a.a.a().f());
        d("4010002");
        f("0500");
    }

    public void e(String str) {
        this.ts_ = str;
    }

    public void f(String str) {
        this.code_ = str;
    }

    public void g(String str) {
        this.thirdId_ = str;
    }

    public void h(String str) {
        this.locale_ = str;
    }

    public void i(String str) {
        a(str);
    }

    public String toString() {
        return getClass().getName() + " {\n\tmethod_: " + g() + "\n\tnet_: " + a() + "\n}";
    }
}
