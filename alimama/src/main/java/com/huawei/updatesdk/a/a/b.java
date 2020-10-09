package com.huawei.updatesdk.a.a;

import com.huawei.updatesdk.sdk.service.c.a.d;

public class b extends d {
    public static final int ENCRYPT_API_HCRID_ERROR = 1022;
    public static final int ENCRYPT_API_SIGN_ERROR = 1021;
    public static final int STORE_API_HCRID_ERROR = 1012;
    public static final int STORE_API_SIGN_ERROR = 1011;

    public String toString() {
        return getClass().getName() + " {\n\tresponseCode: " + c() + "\n\trtnCode_: " + d() + "\n}";
    }
}
