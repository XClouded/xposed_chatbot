package com.taobao.login4android.constants;

public enum LoginEnvType {
    ONLINE(3),
    PRE(2),
    DEV(1);
    
    private int aliuserSdkEvnType;

    private LoginEnvType(int i) {
        this.aliuserSdkEvnType = 3;
        this.aliuserSdkEvnType = i;
    }

    public static LoginEnvType getType(int i) {
        for (LoginEnvType loginEnvType : values()) {
            if (loginEnvType.aliuserSdkEvnType == i) {
                return loginEnvType;
            }
        }
        return ONLINE;
    }

    public int getSdkEnvType() {
        return this.aliuserSdkEvnType;
    }
}
