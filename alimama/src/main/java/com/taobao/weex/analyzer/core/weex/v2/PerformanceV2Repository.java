package com.taobao.weex.analyzer.core.weex.v2;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.core.debug.DataRepository;
import com.taobao.weex.performance.IWXAnalyzer;
import com.taobao.weex.performance.WXAnalyzerDataTransfer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PerformanceV2Repository implements IWXAnalyzer {
    static final String TYPE_DETAIL = "details";
    static final String TYPE_INTERACTION = "wxinteraction";
    static final String TYPE_PROPERTIES = "properties";
    static final String TYPE_STAGE = "stage";
    static final String TYPE_STATS = "stats";
    private Context mContext;
    private final Map<String, APMInfo> mPerformanceStorage = new HashMap(2);
    private final Map<String, OnDataChangedListener> mSubscribers = new HashMap();

    public interface OnDataChangedListener {
        void onDataChanged(@Nullable String str, @NonNull APMInfo aPMInfo);
    }

    private PerformanceV2Repository(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static PerformanceV2Repository create(Context context) {
        return new PerformanceV2Repository(context);
    }

    public void init() {
        WXAnalyzerDataTransfer.isOpenPerformance = true;
        WXSDKManager.getInstance().addWXAnalyzer(this);
    }

    public void destroy() {
        WXSDKManager.getInstance().rmWXAnalyzer(this);
        this.mPerformanceStorage.clear();
        this.mSubscribers.clear();
        WXAnalyzerDataTransfer.isOpenPerformance = false;
    }

    public void transfer(String str, String str2, String str3, String str4) {
        JSONObject jSONObject;
        if (!isValid(str, str2, str3, str4)) {
            Log.e("weex-analyzer", "transfer data is invalid");
            return;
        }
        sendBroadcast(str, str2, str3, str4);
        APMInfo aPMInfo = this.mPerformanceStorage.get(str2);
        if (aPMInfo == null) {
            aPMInfo = new APMInfo(str2);
            this.mPerformanceStorage.put(str2, aPMInfo);
        }
        try {
            jSONObject = JSON.parseObject(str4);
        } catch (Throwable th) {
            Log.e("weex-analyzer", "parse json failed." + th.getMessage());
            jSONObject = null;
        }
        if (jSONObject != null) {
            char c = 65535;
            switch (str3.hashCode()) {
                case -926053069:
                    if (str3.equals(TYPE_PROPERTIES)) {
                        c = 1;
                        break;
                    }
                    break;
                case -401057647:
                    if (str3.equals(TYPE_INTERACTION)) {
                        c = 3;
                        break;
                    }
                    break;
                case 109757182:
                    if (str3.equals(TYPE_STAGE)) {
                        c = 0;
                        break;
                    }
                    break;
                case 109757599:
                    if (str3.equals(TYPE_STATS)) {
                        c = 2;
                        break;
                    }
                    break;
                case 1557721666:
                    if (str3.equals(TYPE_DETAIL)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    aPMInfo.stageMap.putAll(jSONObject);
                    break;
                case 1:
                    aPMInfo.propertyMap.putAll(jSONObject);
                    break;
                case 2:
                    aPMInfo.statsMap.putAll(jSONObject);
                    break;
                case 3:
                    aPMInfo.wxinteractionArray.add(jSONObject);
                    break;
                case 4:
                    aPMInfo.detailMap.putAll(jSONObject);
                    break;
            }
            notifyOnDataChanged(str2, str3);
        }
    }

    private void sendBroadcast(String str, String str2, String str3, String str4) {
        if (this.mContext != null) {
            Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
            intent.putExtra("type", Config.TYPE_WEEX_PERFORMANCE_STATISTICS_V2);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("group", (Object) str);
            jSONObject.put("module", (Object) str2);
            jSONObject.put("type", (Object) str3);
            jSONObject.put("data", (Object) str4);
            intent.putExtra(Config.TYPE_WEEX_PERFORMANCE_STATISTICS_V2, jSONObject.toJSONString());
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
        }
    }

    private void notifyOnDataChanged(@NonNull String str, String str2) {
        OnDataChangedListener onDataChangedListener = this.mSubscribers.get(str);
        if (onDataChangedListener != null) {
            APMInfo aPMInfo = this.mPerformanceStorage.get(str);
            if (aPMInfo != null) {
                onDataChangedListener.onDataChanged(str2, aPMInfo);
            } else {
                Log.w("weex-analyzer", "apm info not found");
            }
        }
    }

    private boolean isValid(String str, String str2, String str3, String str4) {
        return !TextUtils.isEmpty(str) && str.equals(WXInitConfigManager.WXAPM_CONFIG_GROUP) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4);
    }

    public void subscribe(@NonNull String str, @NonNull OnDataChangedListener onDataChangedListener) {
        this.mSubscribers.put(str, onDataChangedListener);
        notifyOnDataChanged(str, (String) null);
    }

    public boolean unSubscribe(@NonNull String str) {
        return this.mSubscribers.remove(str) != null;
    }

    static class APMInfo {
        final Map<String, Object> detailMap = new ConcurrentHashMap();
        final String moduleName;
        final Map<String, Object> propertyMap = new ConcurrentHashMap();
        final Map<String, Object> stageMap = new ConcurrentHashMap();
        final Map<String, Object> statsMap = new ConcurrentHashMap();
        final List<Map> wxinteractionArray = new LinkedList();

        APMInfo(String str) {
            this.moduleName = str;
        }

        public String toString() {
            return "APMInfo{moduleName='" + this.moduleName + '\'' + ", stageMap=" + this.stageMap + ", propertyMap=" + this.propertyMap + ", statsMap=" + this.statsMap + ", wxinteractionArray=" + this.wxinteractionArray + '}';
        }
    }
}
