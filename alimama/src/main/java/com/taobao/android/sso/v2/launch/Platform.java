package com.taobao.android.sso.v2.launch;

public enum Platform {
    PLATFORM_TAOBAO("Taobao"),
    PLATFORM_ALIPAY("Alipay");
    
    private String platform;

    public String getPlatform() {
        return this.platform;
    }

    private Platform(String str) {
        this.platform = str;
    }
}
