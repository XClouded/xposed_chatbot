package com.alimama.moon.config;

import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;

public final class MoonConfigCenter {
    private static final String BUNDLE_JS_VERSION = "bundle_js_version";
    public static final String BUNDLE_JS_VERSION_DEFAULT = "5.6.12";
    private static final String HOME_USE_WEEX = "home_use_weex";
    private static final String PHONE_NUMBER_PATTERN_DEFAULT = "^((1[3,5,7,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))[0-9]{8}$";
    private static final String SHARE_USE_FLUTTER = "share_use_flutter";
    private static final String SHARE_WEEX_SWITCH = "share_use_weex";
    private static final String VUE_BUNDLE_JS_VERSION = "vue_bundle_js_version";
    public static final String VUE_BUNDLE_JS_VERSION_DEFAULT = "0.0.2";

    public static String getPhoneNumberPattern() {
        return PHONE_NUMBER_PATTERN_DEFAULT;
    }

    public static String getBundleJsVersion() {
        return EtaoConfigCenter.getInstance().getSwitch(ConfigKeyList.UNION_SWITCH, BUNDLE_JS_VERSION, BUNDLE_JS_VERSION_DEFAULT);
    }

    public static String getVueBundleJsVersion() {
        return EtaoConfigCenter.getInstance().getSwitch(ConfigKeyList.UNION_SWITCH, VUE_BUNDLE_JS_VERSION, VUE_BUNDLE_JS_VERSION_DEFAULT);
    }

    public static boolean isShareWeexSwitchOn() {
        return EtaoConfigCenter.getInstance().getSwitch(ConfigKeyList.UNION_SWITCH, SHARE_WEEX_SWITCH, false);
    }

    public static boolean isShareFlutterSwitchOn() {
        return EtaoConfigCenter.getInstance().getSwitch(ConfigKeyList.UNION_SWITCH, SHARE_USE_FLUTTER, true);
    }

    public static boolean isHomeWeexSwitchOn() {
        return EtaoConfigCenter.getInstance().getSwitch(ConfigKeyList.UNION_SWITCH, HOME_USE_WEEX, false);
    }
}
