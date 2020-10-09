package com.ali.user.mobile.rpc.login.model;

public class PasswordLoginRequest extends LoginRequestBase {
    public Long alipayHid;
    public String ccId;
    public String checkCode;
    public String hid;
    public String loginId;
    public String loginType = "taobao";
    public String password;
    public boolean pwdEncrypted = false;
    public String slideCheckcodeSid;
    public String slideCheckcodeSig;
    public String slideCheckcodeToken;
}
