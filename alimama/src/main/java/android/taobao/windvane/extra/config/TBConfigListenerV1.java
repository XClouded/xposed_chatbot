package android.taobao.windvane.extra.config;

import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TBConfigListenerV1 implements OrangeConfigListenerV1 {
    public void onConfigUpdate(String str, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            TaoLog.d("TBConfigReceiver", "ConfigName: " + str + " isFromLocal:" + z);
            if (str.equalsIgnoreCase(TBConfigManager.ANDROID_WINDVANE_CONFIG)) {
                getAndroidWindvaneConfigData();
            } else if (str.equalsIgnoreCase(TBConfigManager.WINDVANE_COMMMON_CONFIG)) {
                WVJsBridge.enableGetParamByJs = OrangeConfig.getInstance().getConfig(TBConfigManager.WINDVANE_COMMMON_CONFIG, "enableGetParamByJs", "0").equals("1");
            }
        }
    }

    private void getAndroidWindvaneConfigData() {
        String config = OrangeConfig.getInstance().getConfig(TBConfigManager.ANDROID_WINDVANE_CONFIG, WVConfigManager.CONFIGNAME_PACKAGE, "");
        TaoLog.d("TBConfigReceiver", "receive : packageApp: " + config);
        if (!TextUtils.isEmpty(config)) {
            try {
                JSONArray jSONArray = new JSONArray(config);
                int length = jSONArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    String optString = optJSONObject.optString("v", "");
                    String optString2 = optJSONObject.optString("v0", "");
                    String optString3 = optJSONObject.optString("v1", "");
                    String optString4 = optJSONObject.optString("s", "");
                    String optString5 = optJSONObject.optString("e", "");
                    if (!TextUtils.isEmpty(optString) || !TextUtils.isEmpty(optString2) || !TextUtils.isEmpty(optString3)) {
                        if (optString4 == null || "".equals(optString4) || "*".equals(optString4)) {
                            optString4 = "0";
                        }
                        if (optString5 == null || "".equals(optString5) || "*".equals(optString5)) {
                            optString5 = String.valueOf(Integer.MAX_VALUE);
                        }
                        String appVersion = GlobalConfig.getInstance().getAppVersion();
                        if (!TextUtils.isEmpty(appVersion)) {
                            int compareVersion = compareVersion(appVersion, optString4);
                            int compareVersion2 = compareVersion(optString5, appVersion);
                            if ((compareVersion >= 0 && compareVersion2 > 0) || (compareVersion == compareVersion2 && compareVersion2 == 0)) {
                                if (WVMonitorService.getConfigMonitor() != null) {
                                    WVMonitorService.getConfigMonitor().didOccurUpdateConfigSuccess("package_push");
                                }
                                WVConfigManager.getInstance().updateConfig(WVConfigManager.CONFIGNAME_PACKAGE, optString, (String) null, WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypePush);
                                WVConfigManager.getInstance().updateConfig(WVConfigManager.CONFIGNAME_CUSTOM, optString2, (String) null, WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypePush);
                                WVConfigManager.getInstance().updateConfig(WVConfigManager.CONFIGNAME_PREFIXES, optString3, (String) null, WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypePush);
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
            } catch (JSONException unused) {
                if (WVMonitorService.getConfigMonitor() != null) {
                    WVMonitorService.getConfigMonitor().didOccurUpdateConfigError(WVConfigManager.CONFIGNAME_PACKAGE, WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR.ordinal(), "package_push parse failed");
                }
            }
        }
    }

    private int compareVersion(String str, String str2) {
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int length = split.length > split2.length ? split2.length : split.length;
        if (length > 3) {
            length = 3;
        }
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            i = Integer.valueOf(split[i2]).intValue() - Integer.valueOf(split2[i2]).intValue();
            if (i != 0) {
                break;
            }
        }
        return i;
    }
}
