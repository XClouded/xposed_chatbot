package com.huawei.updatesdk.service.b.a;

import android.text.TextUtils;
import com.huawei.updatesdk.sdk.service.c.a.d;
import com.huawei.updatesdk.sdk.service.c.b;

public class c extends b {
    private int b = 0;

    private static class a {
        public static boolean a() {
            d a = new c(com.huawei.updatesdk.a.a.a.a.f(), (com.huawei.updatesdk.sdk.service.c.a.a) null).b();
            if (!(a instanceof com.huawei.updatesdk.a.a.a.b) || a.d() != 0) {
                return false;
            }
            ((com.huawei.updatesdk.a.a.a.b) a).b();
            return true;
        }

        public static boolean a(com.huawei.updatesdk.sdk.service.c.a.c cVar, d dVar) {
            if (cVar instanceof com.huawei.updatesdk.a.a.a) {
                return (dVar.d() == 0 && dVar.c() == 0) ? false : true;
            }
            return false;
        }

        /* access modifiers changed from: private */
        public static boolean b(int i) {
            return i == 1022 || i == 1021 || i == 1012 || i == 1011;
        }
    }

    public c(com.huawei.updatesdk.sdk.service.c.a.c cVar, com.huawei.updatesdk.sdk.service.c.a.a aVar) {
        super(cVar, aVar);
    }

    private void d() {
        if (this.a != null && (this.a instanceof com.huawei.updatesdk.a.a.a)) {
            com.huawei.updatesdk.a.a.a aVar = (com.huawei.updatesdk.a.a.a) this.a;
            if (aVar.b()) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.c("StoreTaskEx", "checkSign failed! recall front and set sign and hcrid again");
                aVar.i(com.huawei.updatesdk.service.a.a.a().d());
            }
        }
    }

    public void a(d dVar) {
        if (this.a instanceof com.huawei.updatesdk.a.a.a.a) {
            return;
        }
        if (a.b(dVar.d()) || a.a(this.a, dVar)) {
            int i = this.b;
            this.b = i + 1;
            if (i < 3) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.d("StoreTaskEx", "reCallFrontSync, hcrID or sign error! method:" + this.a.g() + ", rtnCode:" + dVar.d());
                if (a.a()) {
                    d();
                    dVar.a(1);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public String c() {
        String b2 = com.huawei.updatesdk.support.a.a.b();
        return TextUtils.isEmpty(b2) ? super.c() : b2;
    }
}
