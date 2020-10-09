package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.ek;
import com.xiaomi.push.hl;
import com.xiaomi.push.service.ag;

final class r extends ag.a {
    final /* synthetic */ Context a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    r(int i, String str, Context context) {
        super(i, str);
        this.a = context;
    }

    /* access modifiers changed from: protected */
    public void a() {
        ek.a(this.a).a(ag.a(this.a).a(hl.AwakeInfoUploadWaySwitch.a(), 0));
    }
}
