package com.huawei.updatesdk.a.a.a;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import com.huawei.updatesdk.service.otaupdate.e;
import com.huawei.updatesdk.support.e.c;

public final class a extends com.huawei.updatesdk.a.a.a {
    public static final String APIMETHOD = "client.https.front";
    public static final int SYSTEM_32 = 1;
    public static final int SYSTEM_64 = 2;
    private String accountZone_;
    private String buildNumber_;
    private String density_;
    private int emuiApiLevel_ = 0;
    private String emuiVer_ = null;
    private String firmwareVersion_;
    private int gmsSupport_;
    private int isSubUser_ = 0;
    private String packageName_;
    private String phoneType_;
    private String resolution_;
    private String screen_;
    private int sysBits_ = 1;
    private String theme_;
    private int versionCode_;
    private String version_;
    private int zone_;

    private a() {
    }

    public static a f() {
        a aVar = new a();
        Context b = com.huawei.updatesdk.sdk.service.a.a.a().b();
        int i = 1;
        aVar.b(true);
        aVar.a((String) null);
        aVar.a(false);
        aVar.u(APIMETHOD);
        aVar.j(com.huawei.updatesdk.sdk.a.d.b.a.h());
        aVar.h(com.huawei.updatesdk.sdk.a.d.b.a.c());
        aVar.a(1);
        aVar.m(com.huawei.updatesdk.sdk.a.d.b.a.e(b));
        aVar.n(com.huawei.updatesdk.sdk.a.d.b.a.a());
        aVar.o(Build.MODEL);
        aVar.l(com.huawei.updatesdk.sdk.a.d.b.a.b());
        aVar.k(com.huawei.updatesdk.sdk.a.d.b.a.d());
        aVar.b(com.huawei.updatesdk.sdk.a.d.b.a.c(b));
        aVar.c(com.huawei.updatesdk.sdk.a.d.b.a.f() ? 1 : 0);
        aVar.p("true");
        aVar.q(com.huawei.updatesdk.sdk.a.d.b.a.d());
        aVar.r(com.huawei.updatesdk.sdk.service.a.a.a().b().getPackageName());
        aVar.s(c.a().c());
        aVar.f(c.a().b());
        aVar.t(e.a().c());
        if (com.huawei.updatesdk.sdk.a.d.b.a.g() == 0) {
            i = 0;
        }
        aVar.d(i);
        aVar.e(j());
        return aVar;
    }

    private static int j() {
        int i = SystemProperties.get("ro.product.cpu.abi", "").contains("arm64") ? 2 : 1;
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("StartupRequest", "systeAbi:" + i);
        return i;
    }

    public void a(int i) {
        this.zone_ = i;
    }

    public void b(int i) {
        this.versionCode_ = i;
    }

    public void c(int i) {
        this.gmsSupport_ = i;
    }

    public void d(int i) {
        this.isSubUser_ = i;
    }

    public void e(int i) {
        this.sysBits_ = i;
    }

    public void f(int i) {
        this.emuiApiLevel_ = i;
    }

    public void j(String str) {
        this.firmwareVersion_ = str;
    }

    public void k(String str) {
        this.screen_ = str;
    }

    public void l(String str) {
        this.density_ = str;
    }

    public void m(String str) {
        this.version_ = str;
    }

    public void n(String str) {
        this.buildNumber_ = str;
    }

    public void o(String str) {
        this.phoneType_ = str;
    }

    public void p(String str) {
        this.theme_ = str;
    }

    public void q(String str) {
        this.resolution_ = str;
    }

    public void r(String str) {
        this.packageName_ = str;
    }

    public void s(String str) {
        this.emuiVer_ = str;
    }

    public void t(String str) {
        this.accountZone_ = str;
    }
}
