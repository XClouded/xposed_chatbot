package android.taobao.windvane.monitor;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigHandler;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.config.WVConfigUtils;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class WVMonitorConfigManager {
    /* access modifiers changed from: private */
    public static final String TAG = WVPackageMonitorImpl.class.getSimpleName();
    private static volatile WVMonitorConfigManager instance = null;
    public WVMonitorConfig config = new WVMonitorConfig();

    public static WVMonitorConfigManager getInstance() {
        if (instance == null) {
            synchronized (WVMonitorConfigManager.class) {
                if (instance == null) {
                    instance = new WVMonitorConfigManager();
                }
            }
        }
        return instance;
    }

    private static class PageFinshWVEventListener implements WVEventListener {
        private PageFinshWVEventListener() {
        }

        public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
            if (i != 1002) {
                return null;
            }
            try {
                double d = WVMonitorConfigManager.getInstance().config.perfCheckSampleRate;
                String str = WVMonitorConfigManager.getInstance().config.perfCheckURL;
                if (TextUtils.isEmpty("scriptUrl") || d <= Math.random()) {
                    return null;
                }
                wVEventContext.webView.evaluateJavascript(String.format("(function(d){var s = d.createElement('script');s.src='%s';d.head.appendChild(s);})(document)", new Object[]{str}));
                return null;
            } catch (Exception unused) {
                return null;
            }
        }
    }

    public void init() {
        try {
            String stringVal = ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "monitorwv-data", "");
            if (!TextUtils.isEmpty(stringVal)) {
                this.config = parseRule(stringVal);
            }
        } catch (Exception unused) {
        }
        WVConfigManager.getInstance().registerHandler("monitor", new WVConfigHandler() {
            public void update(String str, WVConfigUpdateCallback wVConfigUpdateCallback) {
                WVMonitorConfigManager.this.updateMonitorConfig(wVConfigUpdateCallback, str, getSnapshotN());
            }
        });
        WVEventService.getInstance().addEventListener(new PageFinshWVEventListener());
    }

    public WVMonitorConfig parseRule(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            WVMonitorConfig wVMonitorConfig = new WVMonitorConfig();
            wVMonitorConfig.v = jSONObject.optString("v", "");
            if (TextUtils.isEmpty(wVMonitorConfig.v)) {
                return null;
            }
            wVMonitorConfig.stat.onLoad = jSONObject.optLong("minLoadTime", 0);
            wVMonitorConfig.stat.onDomLoad = jSONObject.optLong("minDomLoadTime", 0);
            wVMonitorConfig.stat.resTime = jSONObject.optLong("minResTime", 0);
            wVMonitorConfig.stat.netstat = jSONObject.optBoolean("reportNetStat", false);
            wVMonitorConfig.stat.resSample = jSONObject.optInt("resSample", 100);
            wVMonitorConfig.isErrorBlacklist = jSONObject.optString("errorType", "b").equals("b");
            JSONArray optJSONArray = jSONObject.optJSONArray("errorRule");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        wVMonitorConfig.errorRule.add(wVMonitorConfig.newErrorRuleInstance(optJSONObject.optString("url", ""), optJSONObject.optString("msg", ""), optJSONObject.optString("code", "")));
                    }
                }
            }
            wVMonitorConfig.perfCheckSampleRate = jSONObject.optDouble("perfCheckSampleRate", 0.0d);
            wVMonitorConfig.perfCheckURL = jSONObject.optString("perfCheckURL", "");
            return wVMonitorConfig;
        } catch (JSONException unused) {
            String str2 = TAG;
            TaoLog.e(str2, "parseRule error. content=" + str);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public boolean needSaveConfig(String str) {
        WVMonitorConfig parseRule;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        JSONObject jSONObject = null;
        ApiResponse apiResponse = new ApiResponse();
        if (apiResponse.parseJsonResult(str).success) {
            jSONObject = apiResponse.data;
        }
        if (jSONObject == null || (parseRule = parseRule(jSONObject.toString())) == null) {
            return false;
        }
        this.config = parseRule;
        return true;
    }

    /* access modifiers changed from: private */
    public void updateMonitorConfig(final WVConfigUpdateCallback wVConfigUpdateCallback, final String str, String str2) {
        if (WVCommonConfig.commonConfig.monitorStatus != 2) {
            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UPDATE_DISABLED, 0);
            return;
        }
        if (TextUtils.isEmpty(str)) {
            str = WVConfigManager.getInstance().getConfigUrl("3", this.config.v, WVConfigUtils.getTargetValue(), str2);
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
                        if (WVMonitorConfigManager.this.needSaveConfig(str)) {
                            ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, "monitorwv-data", str);
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 1);
                            return;
                        }
                        wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                    } catch (UnsupportedEncodingException unused) {
                        wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                    }
                }
            }

            public void onError(int i, String str) {
                if (wVConfigUpdateCallback != null) {
                    wVConfigUpdateCallback.updateError(str, str);
                    wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR, 0);
                }
                String access$200 = WVMonitorConfigManager.TAG;
                TaoLog.d(access$200, "update moniter failed! : " + str);
                super.onError(i, str);
            }
        });
    }
}
