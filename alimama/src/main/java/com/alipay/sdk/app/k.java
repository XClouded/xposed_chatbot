package com.alipay.sdk.app;

import com.huawei.hms.support.api.entity.core.JosStatusCodes;

public enum k {
    SUCCEEDED(AlipayResultActivity.a, "处理成功"),
    FAILED(4000, "系统繁忙，请稍后再试"),
    CANCELED(6001, "用户取消"),
    NETWORK_ERROR(6002, "网络连接异常"),
    e(4001, "参数错误"),
    DOUBLE_REQUEST(5000, "重复请求"),
    PAY_WAITTING(JosStatusCodes.RTN_CODE_COMMON_ERROR, "支付结果确认中");
    
    private int h;
    private String i;

    private k(int i2, String str) {
        this.h = i2;
        this.i = str;
    }

    public void a(int i2) {
        this.h = i2;
    }

    public int a() {
        return this.h;
    }

    public void a(String str) {
        this.i = str;
    }

    public String b() {
        return this.i;
    }

    public static k b(int i2) {
        if (i2 == 4001) {
            return e;
        }
        if (i2 == 5000) {
            return DOUBLE_REQUEST;
        }
        if (i2 == 8000) {
            return PAY_WAITTING;
        }
        if (i2 == 9000) {
            return SUCCEEDED;
        }
        switch (i2) {
            case 6001:
                return CANCELED;
            case 6002:
                return NETWORK_ERROR;
            default:
                return FAILED;
        }
    }
}
