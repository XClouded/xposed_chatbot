package com.taobao.android.sns4android;

public enum SNSPlatform {
    PLATFORM_GOOGLE("Google"),
    PLATFORM_FACEBOOK("Facebook"),
    PLATFORM_TWITTER("Twitter"),
    PLATFORM_LINKEDIN("Linkedin"),
    PLATFORM_QQ("QQ"),
    PLATFORM_WEIXIN("WeiXin"),
    PLATFORM_WEIBO("Weibo"),
    PLATFORM_ALIPAY3("Alipay3"),
    PLATFORM_LINE("Line"),
    PLATFORM_TAOBAO("Taobao");
    
    private String platform;

    public String getPlatform() {
        return this.platform;
    }

    private SNSPlatform(String str) {
        this.platform = str;
    }
}
