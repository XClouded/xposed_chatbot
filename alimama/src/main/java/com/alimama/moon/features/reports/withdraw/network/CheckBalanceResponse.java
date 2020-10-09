package com.alimama.moon.features.reports.withdraw.network;

import com.ali.user.mobile.rpc.ApiConstants;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class CheckBalanceResponse {
    private String alipayAccount;
    private String alipayMobile;
    private String email;
    private String mobile;

    public static CheckBalanceResponse fromJson(SafeJSONObject safeJSONObject) {
        SafeJSONObject optJSONObject = safeJSONObject.optJSONObject("data");
        CheckBalanceResponse checkBalanceResponse = new CheckBalanceResponse();
        checkBalanceResponse.alipayAccount = optJSONObject.optString("alipay");
        checkBalanceResponse.mobile = optJSONObject.optString(ApiConstants.ApiField.MOBILE);
        checkBalanceResponse.alipayMobile = optJSONObject.optString("alipaymobile");
        checkBalanceResponse.email = optJSONObject.optString("email");
        return checkBalanceResponse;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAlipayAccount() {
        return this.alipayAccount;
    }

    public String getAlipayMobile() {
        return this.alipayMobile;
    }

    public String getMobile() {
        return this.mobile;
    }
}
