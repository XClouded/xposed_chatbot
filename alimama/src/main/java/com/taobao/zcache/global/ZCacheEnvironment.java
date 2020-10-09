package com.taobao.zcache.global;

import android.app.Application;

public class ZCacheEnvironment {
    public static final String VERSION = "8.3.0";
    private static String appKey;
    private static String appVersion;
    private static ZCacheEnvironment config;
    public static Application context;
    public static EnvEnum env = EnvEnum.ONLINE;
    private static String packageZipPrefix = null;
    private static String ttid = null;
    private String groupName;
    private String groupVersion;

    private ZCacheEnvironment() {
    }

    public static synchronized ZCacheEnvironment getInstance() {
        ZCacheEnvironment zCacheEnvironment;
        synchronized (ZCacheEnvironment.class) {
            if (config == null) {
                config = new ZCacheEnvironment();
            }
            zCacheEnvironment = config;
        }
        return zCacheEnvironment;
    }

    public static void initParams(Application application, String str, String str2) {
        context = application;
        appKey = str;
        appVersion = str2;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Application getApplicationContext() {
        return context;
    }

    public static void setPackageZipPrefix(String str) {
        packageZipPrefix = str;
    }

    public static void setTtid(String str) {
        ttid = str;
    }

    public static String getTtid() {
        return ttid;
    }

    public static String getPackageZipPrefix() {
        return packageZipPrefix;
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
}
