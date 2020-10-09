package android.taobao.windvane.config;

import android.app.Application;
import android.text.TextUtils;

public class GlobalConfig {
    public static final String DEFAULT_TTID = "hybrid@windvane_android_8.5.0";
    public static final String DEFAULT_UA = " WindVane/8.5.0";
    public static final String VERSION = "8.5.0";
    private static GlobalConfig config;
    public static Application context;
    public static EnvEnum env = EnvEnum.ONLINE;
    public static boolean isBackground = false;
    public static String zType = "2";
    private String appKey;
    private String appSecret;
    private String appTag;
    private String appVersion;
    private UCHASettings.ConfigRate configRates = new UCHASettings.ConfigRate();
    private String deviceId;
    private String groupName;
    private String groupVersion;
    private String imei;
    private String imsi;
    private boolean needSpeed = false;
    private boolean open4GDownload = false;
    private boolean openUCDebug = true;
    private String ttid;
    private String uc7ZPath = null;
    private boolean ucCoreOuterControl = false;
    private UCHASettings ucHASettings = new UCHASettings();
    private String ucLibDir = null;
    private boolean ucSdkInternationalEnv = false;
    private String[] ucsdkappkeySec = null;
    private boolean zcacheOldConfig = true;
    private boolean zcacheSpeed = false;
    private boolean zcacheType3 = true;

    private GlobalConfig() {
    }

    public static synchronized GlobalConfig getInstance() {
        GlobalConfig globalConfig;
        synchronized (GlobalConfig.class) {
            if (config == null) {
                synchronized (GlobalConfig.class) {
                    if (config == null) {
                        config = new GlobalConfig();
                    }
                }
            }
            globalConfig = config;
        }
        return globalConfig;
    }

    public boolean initParams(WVAppParams wVAppParams) {
        if (wVAppParams == null) {
            return false;
        }
        if (!TextUtils.isEmpty(wVAppParams.appKey)) {
            if (TextUtils.isEmpty(wVAppParams.ttid)) {
                this.ttid = DEFAULT_TTID;
            } else {
                this.ttid = wVAppParams.ttid;
            }
            this.imei = wVAppParams.imei;
            this.imsi = wVAppParams.imsi;
            this.deviceId = wVAppParams.deviceId;
            this.appKey = wVAppParams.appKey;
            this.appSecret = wVAppParams.appSecret;
            this.appTag = wVAppParams.appTag;
            this.appVersion = wVAppParams.appVersion;
            setUcsdkappkeySec(wVAppParams.ucsdkappkeySec);
            if (!TextUtils.isEmpty(wVAppParams.ucLibDir)) {
                this.ucLibDir = wVAppParams.ucLibDir;
            }
            if (!TextUtils.isEmpty(wVAppParams.uc7ZPath)) {
                this.uc7ZPath = wVAppParams.uc7ZPath;
            }
            this.ucSdkInternationalEnv = wVAppParams.ucSdkInternationalEnv;
            this.needSpeed = wVAppParams.needSpeed;
            this.zcacheSpeed = wVAppParams.zcacheSpeed;
            this.openUCDebug = wVAppParams.openUCDebug;
            if (wVAppParams.ucHASettings != null) {
                this.ucHASettings = wVAppParams.ucHASettings;
            }
            this.configRates = wVAppParams.configRates;
            this.zcacheOldConfig = wVAppParams.zcacheOldConfig;
            this.zcacheType3 = wVAppParams.zcacheType3;
            this.open4GDownload = wVAppParams.open4GDownload;
            this.ucCoreOuterControl = wVAppParams.ucCoreOuterControl;
            return true;
        }
        throw new NullPointerException("initParams error, appKey is empty");
    }

    public String getTtid() {
        return this.ttid;
    }

    public String getImei() {
        return this.imei;
    }

    public String getImsi() {
        return this.imsi;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public String getAppTag() {
        return this.appTag;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public String[] getUcsdkappkeySec() {
        return this.ucsdkappkeySec;
    }

    public String getUcLibDir() {
        return this.ucLibDir;
    }

    public String getUc7ZPath() {
        return this.uc7ZPath;
    }

    public void setUc7ZPath(String str) {
        this.uc7ZPath = str;
    }

    public boolean isUcSdkInternationalEnv() {
        return this.ucSdkInternationalEnv;
    }

    public boolean isUcCoreOuterControl() {
        return this.ucCoreOuterControl;
    }

    public void setUcsdkappkeySec(String[] strArr) {
        if (strArr != null) {
            this.ucsdkappkeySec = strArr;
        }
    }

    public static String getMtopUrl() {
        return "http://api." + env.getValue() + ".taobao.com/rest/api3.do";
    }

    public static String getCdnConfigUrlPre() {
        return getH5Host() + "/bizcache/";
    }

    public static String getH5Host() {
        String str = EnvEnum.ONLINE.equals(env) ? "https://h5." : "http://h5.";
        return str + env.getValue() + ".taobao.com";
    }

    public static String getCdnHost() {
        String str = EnvEnum.ONLINE.equals(env) ? "https://h5." : "http://h5.";
        return str + EnvEnum.ONLINE.getValue() + ".taobao.com";
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }

    public String getGroupVersion() {
        return this.groupVersion;
    }

    public void setGroupVersion(String str) {
        this.groupVersion = str;
    }

    public boolean needSpeed() {
        return this.needSpeed;
    }

    public boolean openUCDebug() {
        return this.openUCDebug;
    }

    public UCHASettings getUcHASettings() {
        return this.ucHASettings;
    }

    public UCHASettings.ConfigRate getConfigRates() {
        return this.configRates;
    }

    public boolean useZcacheOldConfig() {
        return this.zcacheOldConfig;
    }

    public boolean isZcacheType3() {
        return this.zcacheType3;
    }

    public boolean isOpen4GDownload() {
        return this.open4GDownload;
    }
}
