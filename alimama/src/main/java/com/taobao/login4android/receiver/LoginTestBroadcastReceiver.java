package com.taobao.login4android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import com.ali.user.mobile.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.taobao.login4android.jsbridge.SDKJSBridgeService;

public class LoginTestBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (!StringUtil.isBlank(action)) {
                char c = 65535;
                if (action.hashCode() == -592639377) {
                    if (action.equals("com.ali.user.sdk.login.TEST_ACCOUNT_SSO")) {
                        c = 0;
                    }
                }
                if (c == 0) {
                    String stringExtra = intent.getStringExtra("token");
                    if (!StringUtil.isBlank(stringExtra)) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("action", (Object) "testAccountSso");
                        jSONObject.put("token", (Object) stringExtra);
                        new SDKJSBridgeService().testSsoLogin((WVCallBackContext) null, jSONObject.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
