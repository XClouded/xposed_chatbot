package com.taobao.android.sso.v2.launch.model;

import com.taobao.android.sso.v2.launch.util.SSOSignHelper;
import java.util.TreeMap;

public class SSOSlaveParam {
    public String appKey;
    public String sign;
    public String ssoVersion;
    public long t;
    public String targetUrl;
    public String uuidKey;

    public TreeMap<String, String> toMap() {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("slaveAppKey", this.appKey);
        treeMap.put("ssoVersion", this.ssoVersion);
        treeMap.put("t", String.valueOf(this.t));
        treeMap.put(SSOSignHelper.KEY_UUIDKEY, this.uuidKey);
        treeMap.put("targetUrl", this.targetUrl);
        return treeMap;
    }
}
