package com.ali.user.mobile.model;

import android.text.TextUtils;

public class TokenType {
    public static final String ALIPAY_SSO = "AlipaySSO";
    public static final String FIND_PWD = "FindPwd";
    public static final String LOGIN = "Login";
    public static final String MERGE_ACCOUNT = "mergeAccount";
    public static final String REG = "Reg";
    public static final String SMS_LOGIN = "SMSLogin";
    public static final String SNS = "SNS";
    public static final String TAOBAO_SSO = "TaobaoSSO";

    public @interface ITokenType {
    }

    public static boolean isAuthType(@ITokenType String str) {
        return TextUtils.equals(str, TAOBAO_SSO) || TextUtils.equals(str, ALIPAY_SSO) || TextUtils.equals(str, SNS);
    }
}
