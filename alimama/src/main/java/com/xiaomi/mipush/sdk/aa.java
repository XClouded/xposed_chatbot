package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

final class aa implements Runnable {
    final /* synthetic */ Context a;

    aa(Context context) {
        this.a = context;
    }

    public void run() {
        try {
            PackageInfo packageInfo = this.a.getPackageManager().getPackageInfo(this.a.getPackageName(), 4612);
            z.c(this.a);
            z.d(this.a, packageInfo);
            z.c(this.a, packageInfo);
        } catch (Throwable th) {
            Log.e("ManifestChecker", "", th);
        }
    }
}
