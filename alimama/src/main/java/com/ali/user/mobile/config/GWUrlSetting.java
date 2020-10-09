package com.ali.user.mobile.config;

public class GWUrlSetting {
    private static int indexEnv;

    public static String getAlipayGW() {
        switch (indexEnv) {
            case 0:
                return "http://mobilegw.stable.alipay.net/mgw.htm";
            case 1:
                return "http://mobilegw.stable.alipay.net/mgw.htm";
            case 2:
                return "https://mobilegw.alipay.com/mgw.htm";
            case 3:
                return "https://mobilegw.alipay.com/mgw.htm";
            case 4:
                return "http://mobilegw-1-64.test.alipay.net/mgw.htm";
            default:
                return "https://mobilegw.alipay.com/mgw.htm";
        }
    }
}
