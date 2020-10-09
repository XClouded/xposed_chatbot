package com.huawei.hms.core.aidl;

import com.huawei.hms.core.aidl.a.a;
import java.util.List;

public class RequestHeader implements IMessageEntity {
    @a
    private List<String> apiNameList;
    @a
    private String appId;
    @a
    private String packageName;
    @a
    private int sdkVersion;
    @a
    private String sessionId;

    public RequestHeader() {
    }

    public RequestHeader(String str, String str2, int i, String str3) {
        this.appId = str;
        this.packageName = str2;
        this.sdkVersion = i;
        this.sessionId = str3;
    }

    public String getAppID() {
        return this.appId;
    }

    public void setAppID(String str) {
        this.appId = str;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public int getSdkVersion() {
        return this.sdkVersion;
    }

    public void setSdkVersion(int i) {
        this.sdkVersion = i;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String str) {
        this.sessionId = str;
    }

    public List<String> getApiNameList() {
        return this.apiNameList;
    }

    public void setApiNameList(List<String> list) {
        this.apiNameList = list;
    }
}
