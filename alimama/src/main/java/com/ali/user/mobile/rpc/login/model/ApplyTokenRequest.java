package com.ali.user.mobile.rpc.login.model;

public class ApplyTokenRequest {
    public String appName;
    public String appVersion;
    public String deviceTokenKey;
    public String deviceTokenSign;
    public String sdkVersion;
    public String sid;
    public int site;
    public long t;
    public boolean useDeviceToken = true;
}
