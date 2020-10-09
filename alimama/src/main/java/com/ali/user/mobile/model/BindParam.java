package com.ali.user.mobile.model;

import java.io.Serializable;

public class BindParam implements Serializable {
    private static final long serialVersionUID = -3090063464818391781L;
    public String apdid;
    public String appId;
    public String appKey;
    public String bizSence;
    public String signData;

    public String toString() {
        return "bizScene=" + this.bizSence + "&signData=" + this.signData + "&appKey=" + this.appKey + "&appId=" + this.appId + "&apdid=" + this.apdid;
    }
}
