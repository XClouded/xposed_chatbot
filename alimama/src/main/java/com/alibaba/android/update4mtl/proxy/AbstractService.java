package com.alibaba.android.update4mtl.proxy;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public abstract class AbstractService {
    public String getClientVersion(Context context) {
        String str;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo == null || TextUtils.isEmpty(packageInfo.versionName)) {
                return "0.0.1";
            }
            if (packageInfo.versionName.contains("_")) {
                String[] split = packageInfo.versionName.split("_");
                if (split == null || split.length <= 0 || TextUtils.isEmpty(split[0])) {
                    return "0.0.1";
                }
                str = split[0];
            } else {
                str = packageInfo.versionName;
            }
            return str;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0.0.1";
        }
    }
}
