package com.ali.user.mobile.service;

public interface SessionService {
    void appendEventTrace(String str);

    boolean checkSessionValid();

    void clearAutoLoginInfo();

    void clearCookieManager();

    void clearSessionInfo();

    void clearSessionOnlyCookie();

    String getDisplayNick();

    String getEcode();

    String getEmail();

    String getEventTrace();

    String getExtJson();

    long getHavanaSsoTokenExpiredTime();

    String getHeadPicLink();

    int getInjectCookieCount();

    String getLoginPhone();

    int getLoginSite();

    long getLoginTime();

    String getLoginToken();

    String getNick();

    String getOldNick();

    String getOldSid();

    String getOldUserId();

    String getOneTimeToken();

    long getSessionExpiredTime();

    String getSid();

    String getSsoToken();

    String getUidDigest();

    String getUserId();

    String getUserName();

    void injectCookie(String[] strArr, String[] strArr2);

    void injectExternalCookies(String[] strArr);

    void onLoginSuccess(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, long j, String[] strArr, String[] strArr2, String[] strArr3, long j2, long j3, String str9);

    boolean recoverCookie();

    void removeEventTrace();

    boolean sendClearSessionBroadcast();

    void setDisplayNick(String str);

    void setEcode(String str);

    void setEmail(String str);

    void setExtJson(String str);

    void setHavanaSsoTokenExpiredTime(long j);

    void setHeadPicLink(String str);

    void setInjectCookieCount(int i);

    void setLoginSite(int i);

    void setLoginTime(long j);

    void setLoginToken(String str);

    void setNick(String str);

    void setOneTimeToken(String str);

    void setSessionExpiredTime(long j);

    void setSid(String str);

    void setSsoToken(String str);

    void setUidDigest(String str);

    void setUserId(String str);

    void setUserName(String str);
}
