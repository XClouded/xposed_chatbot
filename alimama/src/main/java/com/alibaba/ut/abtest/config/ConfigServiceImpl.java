package com.alibaba.ut.abtest.config;

import com.alibaba.ut.abtest.UTABMethod;
import com.alibaba.ut.abtest.internal.config.OrangeConfigService;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import java.util.HashSet;
import java.util.Set;

public class ConfigServiceImpl implements ConfigService {
    private static final String TAG = "ConfigServiceImpl";
    private UTABMethod method = UTABMethod.Pull;
    private final Object navIgnoredLock = new Object();
    private Set<String> navIgnores = buildNavIgnores();
    private boolean sdkDowngrade = false;
    private int syncCrowdDelayed = 120000;

    private Set<String> buildNavIgnores() {
        HashSet hashSet = new HashSet();
        hashSet.add("http://m.taobao.com/channel/act/other/taobao_android");
        hashSet.add("http://m.taobao.com/index.htm");
        hashSet.add("taobao://message/root");
        hashSet.add("http://h5.m.taobao.com/we/index.htm");
        hashSet.add("http://h5.m.taobao.com/awp/base/newcart.htm");
        hashSet.add("http://h5.m.taobao.com/awp/mtb/mtb.htm");
        return hashSet;
    }

    public void initialize() {
        try {
            OrangeConfigService.getInstance().initialize();
        } catch (Throwable unused) {
            LogUtils.logW(TAG, "初始化Orange配置错误。");
        }
    }

    public UTABMethod getMethod() {
        return this.method;
    }

    public void setMethod(UTABMethod uTABMethod) {
        this.method = uTABMethod;
    }

    public boolean isSdkDowngrade() {
        return this.sdkDowngrade;
    }

    public void setSdkDowngrade(boolean z) {
        this.sdkDowngrade = z;
    }

    public boolean isSdkEnabled() {
        if (isSdkDowngrade()) {
            return false;
        }
        try {
            return OrangeConfigService.getInstance().isEnabled();
        } catch (Throwable unused) {
            LogUtils.logW(TAG, "Orange配置读取错误。");
            return true;
        }
    }

    public boolean isTrackAutoEnabled() {
        try {
            return isSdkEnabled() && OrangeConfigService.getInstance().isTrackAutoEnabled();
        } catch (Throwable unused) {
            LogUtils.logW(TAG, "Orange配置读取错误。");
            return true;
        }
    }

    public boolean isDataTriggerEnabled() {
        try {
            return isSdkEnabled() && OrangeConfigService.getInstance().isDataTriggerEnabled();
        } catch (Throwable unused) {
            LogUtils.logW(TAG, "Orange配置读取错误。");
            return true;
        }
    }

    public boolean isNavEnabled() {
        try {
            return isSdkEnabled() && OrangeConfigService.getInstance().isNavEnabled();
        } catch (Throwable unused) {
            LogUtils.logW(TAG, "Orange配置读取错误。");
            return true;
        }
    }

    public boolean isNavIgnored(String str) {
        synchronized (this.navIgnoredLock) {
            if (this.navIgnores.contains(str)) {
                return true;
            }
            try {
                return OrangeConfigService.getInstance().isNavIgnored(str);
            } catch (Throwable unused) {
                LogUtils.logW(TAG, "Orange配置读取错误。");
                return false;
            }
        }
    }

    public int getSyncCrowdDelayed() {
        return this.syncCrowdDelayed;
    }

    public boolean isTrack1022ExperimentDisabled(Long l) {
        if (l == null) {
            return false;
        }
        try {
            return OrangeConfigService.getInstance().isTrack1022ExperimentDisabled(l);
        } catch (Throwable unused) {
            LogUtils.logW(TAG, "Orange配置读取错误。");
            return false;
        }
    }

    public boolean isTrack1022GroupDisabled(Long l) {
        if (l == null) {
            return false;
        }
        try {
            return OrangeConfigService.getInstance().isTrack1022GroupDisabled(l);
        } catch (Throwable unused) {
            LogUtils.logW(TAG, "Orange配置读取错误。");
            return false;
        }
    }

    public long getRequestExperimentDataIntervalTime() {
        try {
            return OrangeConfigService.getInstance().getRequestExperimentDataIntervalTime();
        } catch (Throwable unused) {
            LogUtils.logW(TAG, "Orange配置读取错误。");
            return 60000;
        }
    }

    public long getDownloadExperimentDataDelayTime() {
        try {
            return OrangeConfigService.getInstance().getDownloadExperimentDataDelayTime();
        } catch (Throwable unused) {
            LogUtils.logW(TAG, "Orange配置读取错误。");
            return 60000;
        }
    }
}
