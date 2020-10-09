package com.xiaomi.push;

import com.xiaomi.push.ai;

class aj extends ai.b {
    final /* synthetic */ ai a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f115a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    aj(ai aiVar, ai.a aVar, String str) {
        super(aVar);
        this.a = aiVar;
        this.f115a = str;
    }

    /* access modifiers changed from: package-private */
    public void a() {
        super.a();
    }

    /* access modifiers changed from: package-private */
    public void b() {
        ai.a(this.a).edit().putLong(this.f115a, System.currentTimeMillis()).commit();
    }
}
