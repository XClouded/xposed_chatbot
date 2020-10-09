package com.xiaomi.push;

class fw implements Runnable {
    final /* synthetic */ ft a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f391a;

    fw(ft ftVar, String str) {
        this.a = ftVar;
        this.f391a = str;
    }

    public void run() {
        cu.a().a(this.f391a, true);
    }
}
