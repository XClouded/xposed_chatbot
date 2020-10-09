package com.ali.user.mobile.model;

import com.ali.user.mobile.login.model.LoginConstant;

public enum LoginEntrance {
    passwordLogin("password"),
    smsLogin(LoginConstant.LOGIN_TYPE_SMS),
    taobaoSnsLogin("taobao"),
    alipaySnsLogin("alipay"),
    wechatSnsLogin("wechat"),
    qqSnsLogin("qq"),
    weiboSnsLogin("weibo"),
    unifySsoLogin("unifySsoLogin"),
    mloginTokenLogin("mloginTokenLogin"),
    taobaoQrcdeScanLogin("taobaoQrcdeScanLogin"),
    alipayQrcodeScanLogin("alipayQrcodeScanLogin"),
    youkuQrcodeScanLogin("youkuQrcodeScanLogin");
    
    private String mapping;

    public String getMapping() {
        return this.mapping;
    }

    private LoginEntrance(String str) {
        this.mapping = str;
    }
}
