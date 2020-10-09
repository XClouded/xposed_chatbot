package com.taobao.login4android.biz.logout;

import java.util.Map;

public class LoginRequest {
    public String apdid;
    public String appKey;
    public Map<String, Object> attributes;
    public String deviceId;
    public String ip;
    public String loginType;
    public boolean needAutoLoginToken = true;
    public boolean needLoginCookies;
    public boolean needSsoToken;
    public boolean oldStyleToken = false;
    public String sid;
    public String subSid;
    public String ttid;
    public String umid;
    public String umidToken;
    public long userId;
    public String utdid;
    public String version;
}
