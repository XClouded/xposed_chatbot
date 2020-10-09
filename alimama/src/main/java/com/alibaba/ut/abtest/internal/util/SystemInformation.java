package com.alibaba.ut.abtest.internal.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.alibaba.ut.abtest.internal.ABContext;
import com.ut.device.UTDevice;

public final class SystemInformation {
    private static final String TAG = "SystemInformation";
    private static SystemInformation instance;
    private final Integer appVersionCode;
    private final String appVersionName;

    public static SystemInformation getInstance() {
        if (instance == null) {
            synchronized (SystemInformation.class) {
                if (instance == null) {
                    instance = new SystemInformation();
                }
            }
        }
        return instance;
    }

    private SystemInformation() {
        String str;
        Integer num;
        try {
            PackageInfo packageInfo = ABContext.getInstance().getContext().getPackageManager().getPackageInfo(ABContext.getInstance().getContext().getPackageName(), 0);
            str = packageInfo.versionName;
            try {
                num = Integer.valueOf(packageInfo.versionCode);
            } catch (PackageManager.NameNotFoundException unused) {
                LogUtils.logW(TAG, "System information constructed with a context that apparently doesn't exist.");
                num = null;
                this.appVersionName = str;
                this.appVersionCode = num;
            }
        } catch (PackageManager.NameNotFoundException unused2) {
            str = null;
            LogUtils.logW(TAG, "System information constructed with a context that apparently doesn't exist.");
            num = null;
            this.appVersionName = str;
            this.appVersionCode = num;
        }
        this.appVersionName = str;
        this.appVersionCode = num;
    }

    public String getUtdid() {
        return UTDevice.getUtdid(ABContext.getInstance().getContext());
    }

    public String getAppVersionName() {
        return this.appVersionName;
    }

    public Integer getAppVersionCode() {
        return this.appVersionCode;
    }

    public String getChannel() {
        return UTBridge.getChannel();
    }
}
