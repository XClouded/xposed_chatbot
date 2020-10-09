package com.xiaomi.push;

import android.content.Context;
import java.io.File;

final class w extends v {
    final /* synthetic */ Runnable a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    w(Context context, File file, Runnable runnable) {
        super(context, file, (w) null);
        this.a = runnable;
    }

    /* access modifiers changed from: protected */
    public void a(Context context) {
        if (this.a != null) {
            this.a.run();
        }
    }
}
