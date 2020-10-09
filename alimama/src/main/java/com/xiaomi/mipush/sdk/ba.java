package com.xiaomi.mipush.sdk;

import android.database.ContentObserver;
import android.os.Handler;
import com.xiaomi.push.service.as;

class ba extends ContentObserver {
    final /* synthetic */ ay a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ba(ay ayVar, Handler handler) {
        super(handler);
        this.a = ayVar;
    }

    public void onChange(boolean z) {
        Integer unused = this.a.f46a = Integer.valueOf(as.a(ay.a(this.a)).a());
        if (ay.a(this.a).intValue() != 0) {
            ay.a(this.a).getContentResolver().unregisterContentObserver(this);
            if (com.xiaomi.push.as.b(ay.a(this.a))) {
                this.a.c();
            }
        }
    }
}
