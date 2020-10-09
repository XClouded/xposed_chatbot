package com.ali.user.mobile.model;

import java.io.Serializable;
import java.util.Map;

public class LoginParam implements Serializable {
    private static final long serialVersionUID = 1;
    public Long alipayHid;
    public String checkCode;
    public String checkCodeId;
    public String countryCode;
    public String deviceTokenKey;
    public boolean enableVoiceSMS = false;
    public Map<String, String> externParams;
    public String h5QueryString;
    public long havanaId;
    public String headImg;
    public boolean isFamilyLoginToReg = false;
    public boolean isFoundPassword;
    public boolean isFromAccount = false;
    public boolean isFromRegister;
    public boolean isToPhoneFragment = false;
    public String localTokenScene;
    public String loginAccount;
    public String loginPassword;
    public int loginSite;
    public String loginType;
    public String mobileArea;
    public String phoneCode;
    public String scene;
    public String slideCheckcodeSid;
    public String slideCheckcodeSig;
    public String slideCheckcodeToken;
    public String smsCode;
    public String smsSid;
    public String snsToken;
    public String snsType;
    public String source;
    public String tid;
    public String token;
    public String tokenType = TokenType.LOGIN;
}
