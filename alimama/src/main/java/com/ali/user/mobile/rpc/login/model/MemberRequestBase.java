package com.ali.user.mobile.rpc.login.model;

import com.ali.user.mobile.info.AppInfo;
import java.util.Map;

public class MemberRequestBase {
    public String appName;
    public String appVersion = AppInfo.getInstance().getAndroidAppVersion();
    public String deviceId;
    public Map<String, Object> ext;
    public String locale;
    public String sdkVersion;
    public int site;
    public Map<String, String> sysExt;
    public String ttid;
    public String utdid;
}
