package com.xiaomi.push;

import android.content.Context;

final class eh implements Runnable {
    final /* synthetic */ int a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ Context f308a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f309a;
    final /* synthetic */ String b;

    eh(Context context, String str, int i, String str2) {
        this.f308a = context;
        this.f309a = str;
        this.a = i;
        this.b = str2;
    }

    public void run() {
        eg.c(this.f308a, this.f309a, this.a, this.b);
    }
}
