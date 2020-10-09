package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;

final class ah implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String[] f30a;

    ah(String[] strArr, Context context) {
        this.f30a = strArr;
        this.a = context;
    }

    public void run() {
        try {
            for (String str : this.f30a) {
                if (!TextUtils.isEmpty(str)) {
                    PackageInfo packageInfo = this.a.getPackageManager().getPackageInfo(str, 4);
                    if (packageInfo != null) {
                        MiPushClient.awakePushServiceByPackageInfo(this.a, packageInfo);
                    }
                }
            }
        } catch (Throwable th) {
            b.a(th);
        }
    }
}
