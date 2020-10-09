package com.huawei.hms.update.a;

import android.os.Handler;
import android.os.Looper;
import com.huawei.hms.update.a.a.b;
import com.huawei.hms.update.a.a.c;
import java.io.File;

/* compiled from: ThreadWrapper */
final class d implements b {
    final /* synthetic */ b a;

    d(b bVar) {
        this.a = bVar;
    }

    public void a(int i, c cVar) {
        new Handler(Looper.getMainLooper()).post(new e(this, i, cVar));
    }

    public void a(int i, int i2, int i3, File file) {
        new Handler(Looper.getMainLooper()).post(new f(this, i, i2, i3, file));
    }
}
