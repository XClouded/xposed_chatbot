package com.taobao.login4android.constants;

public class LoginConstants {
    public static final String BROWSER_REF_URL = "browserRefUrl";
    public static final String EVENT_SESSION_INVALID = "SESSION_INVALID";
    public static final String H5_SEND_CANCEL_BROADCAST = "sendCancelBroadcast";
    public static final String LOGIN_FAIL_BY_APPKEY = "loginFailNullAppkey";
    public static final String LOGIN_FAIL_REASON = "loginFailReason";
    public static final String LOGIN_FROM = "loginFrom";
    public static final String LOGOUT_TYPE = "logoutType";
    public static final String MTOP_API_REFERENCE = "apiReferer";
    public static final String REFRESH_COOKIES_FIRST = "com.taobao.tao.login.REFRESH_COOKIES_FIRST";
    public static final String SHOW_TOAST = "showToast";

    public enum LoginSuccessType {
        TBLoginTypeManualLogin("pwd"),
        TBLoginTypeAutoLogin("autologin"),
        TBLoginTypeTaobaoSSOLogin("taobaosso"),
        TBLoginTypeAlipaySSOLogin("alipaysso"),
        TBLoginTypeCheckSession("checksession"),
        TBLoginTypeMergeAccount("mergeAccount");
        
        private String type;

        private LoginSuccessType(String str) {
            this.type = str;
        }

        public String getType() {
            return this.type;
        }
    }

    public enum LogoutType {
        NORMAL_LOGOUT("logout"),
        CHANGE_ACCOUNT("changeAccount");
        
        private String type;

        private LogoutType(String str) {
            this.type = str;
        }

        public String getType() {
            return this.type;
        }
    }
}
