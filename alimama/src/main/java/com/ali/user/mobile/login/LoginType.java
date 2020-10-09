package com.ali.user.mobile.login;

public enum LoginType {
    ALIPAY_ACCOUNT("alipay"),
    TAOBAO_ACCOUNT("taobao"),
    ICBU_ACCOUNT("icbu"),
    AUTH_ACCOUNT("auth");
    
    private String type;

    private LoginType(String str) {
        this.type = str;
    }

    public String getType() {
        return this.type;
    }
}
