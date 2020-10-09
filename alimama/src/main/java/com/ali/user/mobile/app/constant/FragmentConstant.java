package com.ali.user.mobile.app.constant;

import java.util.Arrays;
import java.util.List;

public class FragmentConstant {
    public static final String AUTH_FRAGMENT_TAG = "aliuser_auth_fragment";
    public static final String FACE_LOGIN_FRAGMENT_TAG = "aliuser_face_login";
    public static final String GUIDE_FRAGMENT_TAG = "aliuser_guide_login";
    public static final String MOBILE_LOGIN_FRAGMENT_TAG = "aliuser_mobile_login";
    public static final String PWD_AUTH_WITH_FIXED_NICK = "aliuser_pwd_auth_fix_nick";
    public static final String PWD_LOGIN_FRAGMENT_TAG = "aliuser_pwd_login";
    public static final String REG_FRAGMENT_TAG = "aliuser_reg";
    public static final String REG_SMSCODE_FRAGMENT_TAG = "aliuser_smscode_reg";
    public static final String SNS_FAST_REG_OR_LOGIN_BIND = "aliuser_reg_or_login_bind";

    public static List<String> getFragmentTagList() {
        return Arrays.asList(new String[]{GUIDE_FRAGMENT_TAG, AUTH_FRAGMENT_TAG, MOBILE_LOGIN_FRAGMENT_TAG, PWD_LOGIN_FRAGMENT_TAG, FACE_LOGIN_FRAGMENT_TAG});
    }
}
