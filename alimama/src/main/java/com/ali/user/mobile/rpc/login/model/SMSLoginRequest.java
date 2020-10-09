package com.ali.user.mobile.rpc.login.model;

public class SMSLoginRequest extends LoginRequestBase {
    public Long alipayHid;
    public String countryCode;
    public String hid;
    public String loginId;
    public String loginType = "taobao";
    public String phoneCode;
    public String slideCheckcodeSid;
    public String slideCheckcodeSig;
    public String slideCheckcodeToken;
    public String smsCode;
    public String smsSid;
}
