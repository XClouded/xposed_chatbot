package android.taobao.windvane.config;

import android.os.Build;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.huawei.hms.support.api.entity.core.JosStatusCodes;
import com.taobao.android.dinamic.DinamicConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class WVCommonConfig {
    private static final String TAG = "WVCommonConfig";
    public static final WVCommonConfigData commonConfig = new WVCommonConfigData();
    private static volatile WVCommonConfig instance = null;
    public String commonCfg = "{}";

    public static WVCommonConfig getInstance() {
        if (instance == null) {
            synchronized (WVCommonConfig.class) {
                if (instance == null) {
                    instance = new WVCommonConfig();
                }
            }
        }
        return instance;
    }

    public void init() {
        parseConfig(ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "commonwv-data"));
    }

    public void updateCommonRule(final WVConfigUpdateCallback wVConfigUpdateCallback, final String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            str = WVConfigManager.getInstance().getConfigUrl("1", commonConfig.v, WVConfigUtils.getTargetValue(), str2);
            if ("3".equals(GlobalConfig.zType)) {
                str = WVConfigManager.getInstance().getConfigUrl("1", "0", WVConfigUtils.getTargetValue(), str2);
            }
        }
        ConnectManager.getInstance().connectSync(str, new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse httpResponse, int i) {
                if (wVConfigUpdateCallback != null) {
                    if (httpResponse == null || httpResponse.getData() == null) {
                        wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NULL_DATA, 0);
                        return;
                    }
                    try {
                        String str = new String(httpResponse.getData(), "utf-8");
                        if ("3".equals(GlobalConfig.zType)) {
                            WVCommonConfig.this.commonCfg = str;
                        }
                        int access$000 = WVCommonConfig.this.parseConfig(str);
                        if (access$000 > 0) {
                            ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, "commonwv-data", str);
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, access$000);
                            return;
                        }
                        wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                    } catch (UnsupportedEncodingException e) {
                        wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                        TaoLog.e(WVCommonConfig.TAG, "config encoding error. " + e.getMessage());
                    }
                }
            }

            public void onError(int i, String str) {
                if (wVConfigUpdateCallback != null) {
                    wVConfigUpdateCallback.updateError(str, str);
                    wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR, 0);
                }
                TaoLog.d(WVCommonConfig.TAG, "update common failed! : " + str);
                super.onError(i, str);
            }
        });
    }

    /* access modifiers changed from: private */
    public int parseConfig(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        JSONObject jSONObject = null;
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException unused) {
        }
        if (jSONObject == null) {
            return 0;
        }
        String optString = jSONObject.optString("v", "");
        if (TextUtils.isEmpty(optString)) {
            return 0;
        }
        commonConfig.v = optString;
        long optLong = jSONObject.optLong("configUpdateInterval", 0);
        if (optLong >= 0) {
            commonConfig.updateInterval = optLong;
            WVConfigManager.getInstance().setUpdateInterval(optLong);
        }
        commonConfig.packagePriorityWeight = jSONObject.optDouble("packagePriorityWeight", 0.1d);
        commonConfig.packageAppStatus = jSONObject.optInt("packageAppStatus", 2);
        commonConfig.monitorStatus = jSONObject.optInt("monitorStatus", 2);
        commonConfig.urlRuleStatus = jSONObject.optInt("urlRuleStatus", 2);
        commonConfig.packageMaxAppCount = jSONObject.optInt("packageMaxAppCount", 100);
        String optString2 = jSONObject.optString("urlScheme", "http");
        commonConfig.urlScheme = optString2.replace(":", "");
        JSONObject optJSONObject = jSONObject.optJSONObject("verifySampleRate");
        if (optJSONObject != null) {
            commonConfig.verifySampleRate = optJSONObject.toString();
        }
        JSONArray optJSONArray = jSONObject.optJSONArray("monitoredApps");
        if (optJSONArray != null) {
            String[] strArr = new String[optJSONArray.length()];
            for (int i = 0; i < optJSONArray.length(); i++) {
                try {
                    strArr[i] = optJSONArray.getString(i);
                } catch (JSONException e) {
                    TaoLog.e(TAG, "obtain monitoredApp error ==>", e.getMessage());
                }
            }
            commonConfig.monitoredApps = strArr;
        }
        JSONArray optJSONArray2 = jSONObject.optJSONArray("aliNetworkDegradeDomains");
        if (optJSONArray2 != null) {
            String[] strArr2 = new String[optJSONArray2.length()];
            for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                try {
                    strArr2[i2] = optJSONArray2.getString(i2);
                } catch (JSONException e2) {
                    TaoLog.e(TAG, "obtain needDegradeDomains error ==>", e2.getMessage());
                }
            }
            commonConfig.aliNetworkDegradeDomains = strArr2;
        }
        String optString3 = jSONObject.optString("disableInstallPeriod");
        if (!TextUtils.isEmpty(optString3)) {
            String[] split = optString3.trim().split("-");
            if (split.length == 2) {
                if (split[0].matches("^[0-9]*$")) {
                    commonConfig.disableInstallPeriod_start = Long.parseLong(split[0]);
                }
                if (split[1].matches("^[0-9]*$")) {
                    commonConfig.disableInstallPeriod_end = Long.parseLong(split[1]);
                }
            }
        }
        String optString4 = jSONObject.optString("ucParam", "");
        if (!TextUtils.isEmpty(optString4) && commonConfig.ucParam != null) {
            commonConfig.ucParam.parse(optString4);
        }
        commonConfig.enableUcShareCore = jSONObject.optBoolean("enableUCShareCore", true);
        commonConfig.useSystemWebView = jSONObject.optBoolean("useSystemWebView", false);
        commonConfig.ucsdk_alinetwork_rate = jSONObject.optDouble("ucsdk_alinetwork_rate", 1.0d);
        commonConfig.ucsdk_image_strategy_rate = jSONObject.optDouble("ucsdk_image_strategy_rate", 1.0d);
        commonConfig.cookieUrlRule = jSONObject.optString("cookieUrlRule", "");
        commonConfig.ucCoreUrl = jSONObject.optString("ucCoreUrl", "");
        commonConfig.shareBlankList = jSONObject.optString("shareBlankList", "");
        commonConfig.excludeUCVersions = jSONObject.optString("excludeUCVersions", "1.12.11.0, 1.15.15.0, 1.14.13.0, 1.13.12.0");
        commonConfig.isOpenCombo = jSONObject.optBoolean("isOpenCombo", false);
        commonConfig.isCheckCleanup = jSONObject.optBoolean("isCheckCleanup", true);
        commonConfig.isAutoRegisterApp = jSONObject.optBoolean("isAutoRegisterApp", false);
        commonConfig.isUseTBDownloader = jSONObject.optBoolean("isUseTBDownloader", true);
        commonConfig.isUseAliNetworkDelegate = jSONObject.optBoolean("isUseAliNetworkDelegate", true);
        commonConfig.packageDownloadLimit = jSONObject.optInt("packageDownloadLimit", 30);
        commonConfig.packageAccessInterval = jSONObject.optInt("packageAccessInterval", 3000);
        commonConfig.packageRemoveInterval = jSONObject.optInt("packageRemoveInterval", 432000000);
        commonConfig.recoveryInterval = jSONObject.optInt("recoveryInterval", 432000000);
        commonConfig.customsComboLimit = jSONObject.optInt("customsComboLimit", 1);
        commonConfig.customsDirectQueryLimit = jSONObject.optInt("customsDirectQueryLimit", 10);
        commonConfig.packageZipPrefix = jSONObject.optString("packageZipPrefix", "");
        commonConfig.packageZipPreviewPrefix = jSONObject.optString("packageZipPreviewPrefix", "");
        commonConfig.ucSkipOldKernel = jSONObject.optBoolean("ucSkipOldKernel", true);
        commonConfig.useUCPlayer = jSONObject.optBoolean("useUCPlayer", false);
        commonConfig.enableUCPrecache = jSONObject.optBoolean("enableUCPrecache", false);
        commonConfig.precachePackageName = jSONObject.optString("precachePackageName", "");
        commonConfig.enableUCPrecacheDoc = jSONObject.optBoolean("enableUCPrecacheDoc", false);
        commonConfig.initUCCorePolicy = jSONObject.optInt("initUCCorePolicy", commonConfig.initUCCorePolicy);
        commonConfig.initWebPolicy = jSONObject.optInt("initWebPolicy", 19);
        commonConfig.initOldCoreVersions = jSONObject.optString("initOldCoreVersions", "3.*");
        commonConfig.webMultiPolicy = jSONObject.optInt("webMultiPolicy", commonConfig.webMultiPolicy);
        commonConfig.gpuMultiPolicy = jSONObject.optInt("gpuMultiPolicy", commonConfig.gpuMultiPolicy);
        commonConfig.ucMultiTimeOut = jSONObject.optInt("ucMultiTimeOut", JosStatusCodes.RTN_CODE_COMMON_ERROR);
        commonConfig.ucMultiServiceSpeedUp = jSONObject.optBoolean("ucMultiServiceSpeedUp", false);
        commonConfig.downloadCoreType = jSONObject.optInt("downloadCoreType", commonConfig.downloadCoreType);
        commonConfig.openLog = jSONObject.optBoolean("openLog", false);
        parseUnzipDegradeConfig(jSONObject);
        WVEventService.getInstance().onEvent(6012);
        return jSONObject.length();
    }

    private void parseUnzipDegradeConfig(JSONObject jSONObject) {
        String[] split;
        try {
            commonConfig.zipDegradeMode = jSONObject.optInt("zipDegradeMode", 0);
            commonConfig.zipDegradeList = jSONObject.optString("zipDegradeList", "");
            String str = Build.BRAND + DinamicConstant.DINAMIC_PREFIX_AT + Build.VERSION.RELEASE;
            String str2 = commonConfig.zipDegradeList;
            if (!TextUtils.isEmpty(str2) && (split = str2.split(",")) != null) {
                for (String equalsIgnoreCase : split) {
                    if (str.equalsIgnoreCase(equalsIgnoreCase)) {
                        TaoLog.e(TAG, "Degrade unzip: " + str);
                        commonConfig.needZipDegrade = true;
                        if (commonConfig.zipDegradeMode == 2) {
                            commonConfig.packageAppStatus = 0;
                            TaoLog.w(TAG, "Disable package app");
                            return;
                        }
                        return;
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
