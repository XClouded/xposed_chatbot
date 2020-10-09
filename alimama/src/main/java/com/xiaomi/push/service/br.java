package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.push.ab;
import com.xiaomi.push.fn;
import com.xiaomi.push.service.XMPushService;

class br extends XMPushService.i {
    final /* synthetic */ XMPushService a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f895a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ byte[] f896a;
    final /* synthetic */ int b;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    br(XMPushService xMPushService, int i, int i2, byte[] bArr, String str) {
        super(i);
        this.a = xMPushService;
        this.b = i2;
        this.f896a = bArr;
        this.f895a = str;
    }

    public String a() {
        return "clear account cache.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m592a() {
        l.a((Context) this.a);
        al.a().a("5");
        ab.a(this.b);
        XMPushService.a(this.a).c(fn.a());
        this.a.a(this.f896a, this.f895a);
    }
}
