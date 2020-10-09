package android.taobao.windvane.config;

import android.os.Environment;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

import java.io.File;

public class UCParamData {
    private static final String[] COMMON_CLIENT_APP_PACKAGE_NAMES = {"com.tmall.wireless", "com.youku.phone", "com.huawei.hwvplayer.youku", "cn.damai"};
    private static final String DEFAULT_HOST_UCM_VERSIONS = "";
    private static final String[] HIGH_SECURITY_CLIENT_APP_PACKAGE_NAMES = {AgooConstants.TAOBAO_PACKAGE};
    private static final String[] HOST_APP_PACKAGE_NAMES = {AgooConstants.TAOBAO_PACKAGE, "com.youku.phone"};
    private static final String LOAD_POLICY_CD_ONLY_LEGAL_VALUE = "sc_lshco";
    private static final String OTHER_APPS_CLIENT_UCM_VERSIONS = "";
    private static final String SECURITY_CLIENT_UCM_VERSIONS = "";
    public String cdResourceEmbedSurfaceEmbedViewEnableList = "0^^*,map,video,camera,ai-camera,canvas";
    public String hostUcmVersionsCd;
    public String scCopyToSdcardCd;
    public String scLoadPolicyCd;
    public String scPkgNames;
    public String scStillUpd;
    public String scWaitMilts;
    public String sdCopyPathCd;
    public String thirtyUcmVersionsCd;
    public String u4FocusAutoPopupInputHostList = "";
    public String ucPageTimerCount = "20000";

    public UCParamData(String str) {
        parse(str);
    }

    public void parse(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                TaoLog.d("UCParamData", str);
                JSONObject jSONObject = new JSONObject(str);
                this.sdCopyPathCd = getSdCopyPathCd(jSONObject.optString("sdCopyPathCd", "/alipay/com.eg.android.AlipayGphone/"));
                this.hostUcmVersionsCd = jSONObject.optString("hostUcmVersionsCd", "");
                this.scLoadPolicyCd = jSONObject.optString("scLoadPolicyCd", needLoadNeedShareCoreApp() ? "sc_lshco" : "");
                this.scCopyToSdcardCd = jSONObject.optString("scCopyToSdcardCd", "true");
                this.thirtyUcmVersionsCd = jSONObject.optString("thirtyUcmVersionsCd", getClientUCMVersionConfig());
                this.scPkgNames = jSONObject.optString("scPkgNames", "com.eg.android.AlipayGphone^^com.taobao.taobao");
                this.scStillUpd = jSONObject.optString("scStillUpd", "true");
                this.scWaitMilts = jSONObject.optString("scWaitMilts", needLoadNeedShareCoreApp() ? "1" : "600000");
                this.u4FocusAutoPopupInputHostList = jSONObject.optString("u4FocusAutoPopupInputHostList", this.u4FocusAutoPopupInputHostList);
                this.ucPageTimerCount = jSONObject.optString("ucPageTimerCount", this.ucPageTimerCount);
            }
        } catch (Throwable unused) {
            TaoLog.w("UCParamData", "failed to parse uc params", str);
        }
    }

    private String getSdCopyPathCd(String str) {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath(), str).getAbsolutePath();
    }

    private boolean notEmpty(String str) {
        return !TextUtils.isEmpty(str);
    }

    private static boolean includedAppPackageName(String str, String[] strArr) {
        for (String equals : strArr) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    private static boolean commonClientApp() {
        if (GlobalConfig.context != null) {
            return includedAppPackageName(GlobalConfig.context.getPackageName(), COMMON_CLIENT_APP_PACKAGE_NAMES);
        }
        return false;
    }

    private static boolean highSecurityClientApp() {
        if (GlobalConfig.context != null) {
            return includedAppPackageName(GlobalConfig.context.getPackageName(), HIGH_SECURITY_CLIENT_APP_PACKAGE_NAMES);
        }
        return false;
    }

    private static String getClientUCMVersionConfig() {
        return highSecurityClientApp() ? "" : "";
    }

    public static boolean hostApp() {
        if (GlobalConfig.context != null) {
            return includedAppPackageName(GlobalConfig.context.getPackageName(), HOST_APP_PACKAGE_NAMES);
        }
        return false;
    }

    public static boolean needLoadNeedShareCoreApp() {
        return commonClientApp() || highSecurityClientApp();
    }

    public boolean validShareCoreToSdcardParams() {
        return notEmpty(this.scCopyToSdcardCd) && notEmpty(this.sdCopyPathCd) && notEmpty(this.hostUcmVersionsCd);
    }

    public boolean validShareCoreFromSdcardParams() {
        return notEmpty(this.sdCopyPathCd) && notEmpty(this.thirtyUcmVersionsCd) && notEmpty(this.scPkgNames) && "sc_lshco".equals(this.scLoadPolicyCd);
    }
}
