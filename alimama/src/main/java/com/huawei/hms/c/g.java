package com.huawei.hms.c;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;

/* compiled from: PackageManagerHelper */
public class g {
    private final PackageManager a;

    /* compiled from: PackageManagerHelper */
    public enum a {
        ENABLED,
        DISABLED,
        NOT_INSTALLED
    }

    public g(Context context) {
        this.a = context.getPackageManager();
    }

    public a a(String str) {
        try {
            if (this.a.getApplicationInfo(str, 0).enabled) {
                return a.ENABLED;
            }
            return a.DISABLED;
        } catch (PackageManager.NameNotFoundException unused) {
            return a.NOT_INSTALLED;
        }
    }

    public int b(String str) {
        try {
            PackageInfo packageInfo = this.a.getPackageInfo(str, 16);
            if (packageInfo != null) {
                return packageInfo.versionCode;
            }
            return 0;
        } catch (PackageManager.NameNotFoundException unused) {
            return 0;
        }
    }

    public String c(String str) {
        byte[] d = d(str);
        if (d == null || d.length == 0) {
            return null;
        }
        return d.b(i.a(d), true);
    }

    private byte[] d(String str) {
        try {
            PackageInfo packageInfo = this.a.getPackageInfo(str, 64);
            if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0)) {
                return packageInfo.signatures[0].toByteArray();
            }
        } catch (PackageManager.NameNotFoundException e) {
            com.huawei.hms.support.log.a.d("PackageManagerHelper", "Failed to get application signature certificate fingerprint." + e.getMessage());
        }
        com.huawei.hms.support.log.a.d("PackageManagerHelper", "Failed to get application signature certificate fingerprint.");
        return new byte[0];
    }

    public boolean a(String str, String str2) {
        try {
            PackageInfo packageInfo = this.a.getPackageInfo(str, 8);
            if (!(packageInfo == null || packageInfo.providers == null)) {
                for (ProviderInfo providerInfo : packageInfo.providers) {
                    if (str2.equals(providerInfo.authority)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
