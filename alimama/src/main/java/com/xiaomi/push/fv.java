package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.xiaomi.push.service.XMPushService;

class fv extends XMPushService.i {
    final /* synthetic */ ft a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ Exception f390a;
    final /* synthetic */ int b;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    fv(ft ftVar, int i, int i2, Exception exc) {
        super(i);
        this.a = ftVar;
        this.b = i2;
        this.f390a = exc;
    }

    public String a() {
        return "shutdown the connection. " + this.b + AVFSCacheConstants.COMMA_SEP + this.f390a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m321a() {
        this.a.b.a(this.b, this.f390a);
    }
}
