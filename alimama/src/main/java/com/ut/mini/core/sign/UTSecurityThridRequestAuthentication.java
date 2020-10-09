package com.ut.mini.core.sign;

public class UTSecurityThridRequestAuthentication implements IUTRequestAuthentication {
    private static final String TAG = "UTSecurityThridRequestAuthentication";
    private String mAppkey = null;
    private String mAuthcode = "";
    private SecuritySDK mSecuritySDK = null;

    public String getAppkey() {
        return this.mAppkey;
    }

    public String getAuthcode() {
        return this.mAuthcode;
    }

    public UTSecurityThridRequestAuthentication(String str, String str2) {
        this.mAppkey = str;
        this.mAuthcode = str2;
        this.mSecuritySDK = new SecuritySDK(str, str2);
    }

    public String getSign(String str) {
        return this.mSecuritySDK.getSign(str);
    }
}
