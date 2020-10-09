package com.ali.user.mobile.log;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.alibaba.fastjson.JSON;
import com.taobao.login4android.constants.LoginConstants;
import com.taobao.login4android.session.SessionManager;
import com.taobao.login4android.session.constants.SessionConstants;

public class ApiReferer {
    public static final String TAG = "loginsdk.ApiReferer";

    public static class Refer {
        public String errorCode;
        public String errorMessage;
        public String eventName;
        public String site;
        public String userId;

        public Refer() {
        }

        public Refer(String str) {
            this.eventName = str;
        }
    }

    public static String generateApiReferer() {
        try {
            String eventTrace = SessionManager.getInstance(DataProviderFactory.getApplicationContext()).getEventTrace();
            return TextUtils.isEmpty(eventTrace) ? JSON.toJSONString(new Refer(LoginConstants.EVENT_SESSION_INVALID)) : eventTrace;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getApiRefer() {
        if (DataProviderFactory.getApplicationContext() != null) {
            return DataProviderFactory.getApplicationContext().getSharedPreferences(SessionManager.USERINFO, 4).getString(SessionConstants.EVENT_TRACE, "");
        }
        return "";
    }
}
