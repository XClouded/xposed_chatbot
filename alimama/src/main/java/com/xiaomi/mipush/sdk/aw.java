package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;

class aw implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ av f39a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String[] f40a;

    aw(av avVar, String[] strArr, Context context) {
        this.f39a = avVar;
        this.f40a = strArr;
        this.a = context;
    }

    public void run() {
        int i = 0;
        while (i < this.f40a.length) {
            try {
                if (!TextUtils.isEmpty(this.f40a[i])) {
                    if (i > 0) {
                        Thread.sleep(((long) ((Math.random() * 2.0d) + 1.0d)) * 1000);
                    }
                    PackageInfo packageInfo = this.a.getPackageManager().getPackageInfo(this.f40a[i], 4);
                    if (packageInfo != null) {
                        this.f39a.a(this.a, packageInfo);
                    }
                }
                i++;
            } catch (Throwable th) {
                b.a(th);
                return;
            }
        }
    }
}
