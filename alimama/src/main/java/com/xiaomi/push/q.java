package com.xiaomi.push;

import android.content.SharedPreferences;

class q implements Runnable {
    final /* synthetic */ p a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f802a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;

    q(p pVar, String str, String str2, String str3) {
        this.a = pVar;
        this.f802a = str;
        this.b = str2;
        this.c = str3;
    }

    public void run() {
        SharedPreferences.Editor edit = this.a.f795a.getSharedPreferences(this.f802a, 4).edit();
        edit.putString(this.b, this.c);
        edit.commit();
    }
}
