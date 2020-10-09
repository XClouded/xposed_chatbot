package com.alibaba.ut.abtest.internal.windvane;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.text.TextUtils;
import com.alibaba.ut.abtest.UTABTest;
import com.alibaba.ut.abtest.Variation;
import com.alibaba.ut.abtest.VariationSet;
import com.alibaba.ut.abtest.internal.util.Analytics;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.taobao.weex.bridge.WXBridgeManager;
import java.util.HashMap;
import org.json.JSONObject;

public class UTABTestApiPluginV2 extends WVApiPlugin {
    public static final String API_NAME = "WVABTestApi";
    private static final String TAG = "UTABTestApiPluginV2";

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        try {
            if (TextUtils.equals("activate", str)) {
                activate(str2, wVCallBackContext);
                return true;
            } else if (!TextUtils.equals(Analytics.EXPERIMENT_ACTIVATE_STAT_TYPE_ACTIVATE_SERVER, str)) {
                return false;
            } else {
                activateServer(str2, wVCallBackContext);
                return true;
            }
        } catch (Exception e) {
            LogUtils.logEAndReport(TAG, "WindVane Api " + str + " 执行错误！", e);
            if (wVCallBackContext == null) {
                return false;
            }
            wVCallBackContext.success(new WVApiResponse(1000).toJsonString());
            return false;
        }
    }

    private void activate(String str, WVCallBackContext wVCallBackContext) throws Exception {
        getVariations(1, str, wVCallBackContext);
    }

    private void getVariations(int i, String str, WVCallBackContext wVCallBackContext) throws Exception {
        VariationSet variationSet;
        if (!TextUtils.isEmpty(str)) {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString(WXBridgeManager.COMPONENT);
            String optString2 = jSONObject.optString("module");
            if (!TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString2)) {
                if (i == 1) {
                    variationSet = UTABTest.activate(optString, optString2);
                } else {
                    variationSet = UTABTest.getVariations(optString, optString2);
                }
                HashMap hashMap = new HashMap();
                for (Variation next : variationSet) {
                    hashMap.put(next.getName(), next.getValue((Object) null));
                }
                WVActivateApiResponseData wVActivateApiResponseData = new WVActivateApiResponseData();
                if (variationSet.getExperimentBucketId() > 0 || !hashMap.isEmpty()) {
                    wVActivateApiResponseData.setExperimentBucketId(variationSet.getExperimentBucketId());
                    wVActivateApiResponseData.setExperimentId(variationSet.getExperimentId());
                    wVActivateApiResponseData.setExperimentReleaseId(variationSet.getExperimentReleaseId());
                    wVActivateApiResponseData.setVariations(hashMap);
                    if (wVCallBackContext != null) {
                        wVCallBackContext.success(new WVApiResponse(wVActivateApiResponseData).toJsonString());
                    }
                } else if (wVCallBackContext != null) {
                    wVCallBackContext.success(new WVApiResponse(1000).toJsonString());
                }
            } else if (wVCallBackContext != null) {
                wVCallBackContext.success(new WVApiResponse(1001).toJsonString());
            }
        } else if (wVCallBackContext != null) {
            wVCallBackContext.success(new WVApiResponse(1001).toJsonString());
        }
    }

    private void activateServer(String str, WVCallBackContext wVCallBackContext) throws Exception {
        if (!TextUtils.isEmpty(str)) {
            String optString = new JSONObject(str).optString("data");
            if (!TextUtils.isEmpty(optString)) {
                UTABTest.activateServer(optString);
                if (wVCallBackContext != null) {
                    wVCallBackContext.success(new WVApiResponse(0).toJsonString());
                }
            } else if (wVCallBackContext != null) {
                wVCallBackContext.success(new WVApiResponse(1001).toJsonString());
            }
        } else if (wVCallBackContext != null) {
            wVCallBackContext.success(new WVApiResponse(1001).toJsonString());
        }
    }
}
