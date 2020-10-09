package com.ali.user.mobile.rpc.login.model;

import android.os.Build;
import com.taobao.weex.el.parse.Operators;

public class LoginRequestBase extends MemberRequestBase {
    public String deviceName = (Build.BRAND + Operators.BRACKET_START_STR + Build.MODEL + Operators.BRACKET_END_STR);
    public String deviceTokenKey;
    public String deviceTokenSign;
    public String hid;
    public String sid;
    public String snsToken;
    public long t;
    public boolean useAcitonType = true;
    public boolean useDeviceToken = true;
}
