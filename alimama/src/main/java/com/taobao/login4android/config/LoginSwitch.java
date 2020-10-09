package com.taobao.login4android.config;

import android.text.TextUtils;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.orange.OrangeConfig;

public class LoginSwitch {
    public static final String ACCS_LOGIN_SWITCH = "accs_login_switch";
    public static final String ALIPAY_SSO_SWITCH = "alipay_sso_switch";
    public static final String APDID_DEGRADE_SWITCH = "apdid_degrade_switch";
    public static final String BIND_ALIPAY_SWITCH = "bind_alipay_switch";
    public static final String CONFIG_GROUP_LOGIN = "login4android";
    public static final String FAMILY_ACCOUNT_TIP_SWITCH = "family_account_tip_switch";
    public static final String FINDPWD_DEGRADE_SWITCH = "findpwd_degrade_switch";
    public static final String FORBIDDEN_REFRESH_COOKIE_IN_AUTO_LOGIN = "forbidden_refresh_cookie_in_autologin";
    public static final String FORBID_LOGIN_FROM_BACKGROUND = "forbid_login_from_background_new";
    public static final String FORCE_SID_CHECK = "force_sid_check";
    public static final String MULTI_ACCOUNT_CHANGE_DEGRADE_SWITCH = "multi_change_account_degrade_switch";
    public static final String REFRESH_COOKIE_DEGRADE_SWITCH = "refresh_cookie_degrade";
    public static final String REPORTDEVICE_DEGRADE_SWITCH = "reportdevice_degrade_switch";
    public static final String SECURITY_SETTING_PERCENT = "security_setting_percent";
    public static final String SUPPORT_FACE_LOGIN = "support_face_login";
    public static final String SUPPORT_MINI_PROGRAME = "support_mini_program";
    public static final String TAG = "login.LoginSwitch";
    public static final String USE_SEPARATE_THREADPOOL = "use_separate_threadpool";

    public static boolean getSwitch(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            String config = OrangeConfig.getInstance().getConfig("login4android", str, str2);
            LoginTLogAdapter.e(TAG, "LoginSwitch:getSwitch, switchName=" + str + ", value=" + config);
            return Boolean.parseBoolean(config);
        } catch (Throwable th) {
            th.printStackTrace();
            LoginTLogAdapter.e(TAG, "LoginSwitch:getSwitch, switchName=" + str, th);
            return false;
        }
    }

    public static int getSwitch(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        try {
            OrangeConfig instance = OrangeConfig.getInstance();
            String config = instance.getConfig("login4android", str, i + "");
            LoginTLogAdapter.e(TAG, "LoginSwitch:getSwitch, switchName=" + str + ", value=" + config);
            return Integer.parseInt(config);
        } catch (Throwable th) {
            th.printStackTrace();
            LoginTLogAdapter.e(TAG, "LoginSwitch:getSwitch, switchName=" + str, th);
            return i;
        }
    }

    public static String getConfig(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        try {
            String config = OrangeConfig.getInstance().getConfig("login4android", str, str2);
            LoginTLogAdapter.e(TAG, "LoginSwitch:getConfig, confighName=" + str + ", value=" + config);
            return config;
        } catch (Throwable th) {
            th.printStackTrace();
            LoginTLogAdapter.e(TAG, "LoginSwitch:getConfig, confighName=" + str, th);
            return str2;
        }
    }
}
