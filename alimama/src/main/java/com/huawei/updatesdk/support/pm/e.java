package com.huawei.updatesdk.support.pm;

import android.content.Context;
import com.huawei.updatesdk.sdk.a.c.a.a.a;
import com.huawei.updatesdk.support.pm.c;

public class e implements Runnable {
    private b a;
    private Context b;

    public e(Context context, b bVar) {
        this.b = context;
        this.a = bVar;
    }

    public void run() {
        a.a("PackageManagerRunnable", "PackageManagerRunnable run!!!!" + this.a.toString());
        if (this.a.g() == c.b.INSTALL) {
            a.a(this.b, this.a);
        }
    }
}
