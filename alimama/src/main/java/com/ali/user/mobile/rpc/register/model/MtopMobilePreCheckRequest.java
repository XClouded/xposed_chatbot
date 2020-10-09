package com.ali.user.mobile.rpc.register.model;

import com.ali.user.mobile.rpc.login.model.MemberRequestBase;

public class MtopMobilePreCheckRequest extends MemberRequestBase {
    public String codeType;
    public String countryCode;
    public String imageCheckcode;
    public String mobileNum;
}
