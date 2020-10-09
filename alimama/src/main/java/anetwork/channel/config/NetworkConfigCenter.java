package anetwork.channel.config;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.statist.RequestStatistic;
import anet.channel.strategy.dispatch.HttpDispatcher;
import anet.channel.strategy.utils.Utils;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.HttpSslUtil;
import anet.channel.util.HttpUrl;
import anetwork.channel.cache.CacheManager;
import anetwork.channel.http.NetworkSdkSetting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkConfigCenter {
    private static final List<String> ALLOW_ALL_PATH = new ArrayList();
    private static final String CACHE_FLAG = "Cache.Flag";
    public static final String SERVICE_OPTIMIZE = "SERVICE_OPTIMIZE";
    private static final String TAG = "anet.NetworkConfigCenter";
    private static volatile int bgForbidRequestThreshold = 60000;
    private static volatile CopyOnWriteArrayList<String> bizWhiteList = null;
    private static volatile long cacheFlag = 0;
    private static volatile ConcurrentHashMap<String, List<String>> degradeListMap = null;
    private static volatile IRemoteConfig iRemoteConfig = null;
    private static volatile boolean isAllowHttpIpRetry = false;
    private static volatile boolean isBgRequestForbidden = false;
    private static volatile boolean isBindServiceOptimize = false;
    private static volatile boolean isGetSessionAsyncEnable = false;
    private static volatile boolean isHttpCacheEnable = true;
    private static volatile boolean isHttpSessionEnable = true;
    private static volatile boolean isRemoteNetworkServiceEnable = true;
    private static volatile boolean isRequestDelayRetryForNoNetwork = true;
    private static volatile boolean isResponseBufferEnable = true;
    private static volatile boolean isSSLEnabled = true;
    private static volatile boolean isSpdyEnabled = true;
    private static volatile CopyOnWriteArrayList<String> monitorRequestList = null;
    private static volatile int requestStatisticSampleRate = 10000;
    private static volatile int serviceBindWaitTime = 5;
    private static volatile ConcurrentHashMap<String, List<String>> urlWhiteListMap;

    public static void init() {
        cacheFlag = PreferenceManager.getDefaultSharedPreferences(NetworkSdkSetting.getContext()).getLong(CACHE_FLAG, 0);
    }

    public static void setSSLEnabled(boolean z) {
        isSSLEnabled = z;
    }

    public static boolean isSSLEnabled() {
        return isSSLEnabled;
    }

    public static void setSpdyEnabled(boolean z) {
        isSpdyEnabled = z;
    }

    public static boolean isSpdyEnabled() {
        return isSpdyEnabled;
    }

    public static void setHttpsValidationEnabled(boolean z) {
        if (!z) {
            HttpSslUtil.setHostnameVerifier(HttpSslUtil.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpSslUtil.setSslSocketFactory(HttpSslUtil.TRUST_ALL_SSL_SOCKET_FACTORY);
            return;
        }
        HttpSslUtil.setHostnameVerifier((HostnameVerifier) null);
        HttpSslUtil.setSslSocketFactory((SSLSocketFactory) null);
    }

    public static void setServiceBindWaitTime(int i) {
        serviceBindWaitTime = i;
    }

    public static int getServiceBindWaitTime() {
        return serviceBindWaitTime;
    }

    public static void setRemoteNetworkServiceEnable(boolean z) {
        isRemoteNetworkServiceEnable = z;
    }

    public static boolean isRemoteNetworkServiceEnable() {
        return isRemoteNetworkServiceEnable;
    }

    public static void setRemoteConfig(IRemoteConfig iRemoteConfig2) {
        if (iRemoteConfig != null) {
            iRemoteConfig.unRegister();
        }
        if (iRemoteConfig2 != null) {
            iRemoteConfig2.register();
        }
        iRemoteConfig = iRemoteConfig2;
    }

    public static boolean isHttpSessionEnable() {
        return isHttpSessionEnable;
    }

    public static void setHttpSessionEnable(boolean z) {
        isHttpSessionEnable = z;
    }

    public static boolean isAllowHttpIpRetry() {
        return isHttpSessionEnable && isAllowHttpIpRetry;
    }

    public static void setAllowHttpIpRetry(boolean z) {
        isAllowHttpIpRetry = z;
    }

    public static boolean isHttpCacheEnable() {
        return isHttpCacheEnable;
    }

    public static void setHttpCacheEnable(boolean z) {
        isHttpCacheEnable = z;
    }

    public static void setCacheFlag(long j) {
        if (j != cacheFlag) {
            ALog.i(TAG, "set cache flag", (String) null, "old", Long.valueOf(cacheFlag), "new", Long.valueOf(j));
            cacheFlag = j;
            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(NetworkSdkSetting.getContext()).edit();
            edit.putLong(CACHE_FLAG, cacheFlag);
            edit.apply();
            CacheManager.clearAllCache();
        }
    }

    public static void updateWhiteListMap(String str) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "updateWhiteUrlList", (String) null, "White List", str);
        }
        if (TextUtils.isEmpty(str)) {
            urlWhiteListMap = null;
            return;
        }
        ConcurrentHashMap<String, List<String>> concurrentHashMap = new ConcurrentHashMap<>();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                Object obj = jSONObject.get(next);
                try {
                    if ("*".equals(obj)) {
                        concurrentHashMap.put(next, ALLOW_ALL_PATH);
                    } else if (obj instanceof JSONArray) {
                        JSONArray jSONArray = (JSONArray) obj;
                        int length = jSONArray.length();
                        ArrayList arrayList = new ArrayList(length);
                        for (int i = 0; i < length; i++) {
                            Object obj2 = jSONArray.get(i);
                            if (obj2 instanceof String) {
                                arrayList.add((String) obj2);
                            }
                        }
                        if (!arrayList.isEmpty()) {
                            concurrentHashMap.put(next, arrayList);
                        }
                    }
                } catch (JSONException unused) {
                }
            }
        } catch (JSONException e) {
            ALog.e(TAG, "parse jsonObject failed", (String) null, e, new Object[0]);
        }
        urlWhiteListMap = concurrentHashMap;
    }

    public static void updateBizWhiteList(String str) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "updateRequestWhiteList", (String) null, "White List", str);
        }
        if (TextUtils.isEmpty(str)) {
            bizWhiteList = null;
            return;
        }
        try {
            JSONArray jSONArray = new JSONArray(str);
            int length = jSONArray.length();
            CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            for (int i = 0; i < length; i++) {
                String string = jSONArray.getString(i);
                if (!string.isEmpty()) {
                    copyOnWriteArrayList.add(string);
                }
            }
            bizWhiteList = copyOnWriteArrayList;
        } catch (JSONException e) {
            ALog.e(TAG, "parse bizId failed", (String) null, e, new Object[0]);
        }
    }

    public static boolean isUrlInWhiteList(HttpUrl httpUrl) {
        ConcurrentHashMap<String, List<String>> concurrentHashMap;
        List<String> list;
        if (httpUrl == null || (concurrentHashMap = urlWhiteListMap) == null || (list = concurrentHashMap.get(httpUrl.host())) == null) {
            return false;
        }
        if (list == ALLOW_ALL_PATH) {
            return true;
        }
        for (String startsWith : list) {
            if (httpUrl.path().startsWith(startsWith)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBizInWhiteList(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        CopyOnWriteArrayList<String> copyOnWriteArrayList = bizWhiteList;
        if (bizWhiteList == null) {
            return false;
        }
        Iterator<String> it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            if (str.equalsIgnoreCase(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static int getRequestStatisticSampleRate() {
        return requestStatisticSampleRate;
    }

    public static void setRequestStatisticSampleRate(int i) {
        requestStatisticSampleRate = i;
    }

    public static boolean isBgRequestForbidden() {
        return isBgRequestForbidden;
    }

    public static void setBgRequestForbidden(boolean z) {
        isBgRequestForbidden = z;
    }

    public static void setAmdcPresetHosts(String str) {
        if (GlobalAppRuntimeInfo.isTargetProcess()) {
            try {
                JSONArray jSONArray = new JSONArray(str);
                int length = jSONArray.length();
                ArrayList arrayList = new ArrayList(length);
                for (int i = 0; i < length; i++) {
                    String string = jSONArray.getString(i);
                    if (Utils.checkHostValidAndNotIp(string)) {
                        arrayList.add(string);
                    }
                }
                HttpDispatcher.getInstance().addHosts(arrayList);
            } catch (JSONException e) {
                ALog.e(TAG, "parse hosts failed", (String) null, e, new Object[0]);
            }
        }
    }

    public static boolean isResponseBufferEnable() {
        return isResponseBufferEnable;
    }

    public static void setResponseBufferEnable(boolean z) {
        isResponseBufferEnable = z;
    }

    public static boolean isGetSessionAsyncEnable() {
        return isGetSessionAsyncEnable;
    }

    public static void setGetSessionAsyncEnable(boolean z) {
        isGetSessionAsyncEnable = z;
    }

    public static int getBgForbidRequestThreshold() {
        return bgForbidRequestThreshold;
    }

    public static void setBgForbidRequestThreshold(int i) {
        bgForbidRequestThreshold = i;
    }

    public static void setMonitorRequestList(String str) {
        if (TextUtils.isEmpty(str)) {
            monitorRequestList = null;
        }
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray("host");
            int length = jSONArray.length();
            CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            for (int i = 0; i < length; i++) {
                String string = jSONArray.getString(i);
                if (Utils.checkHostValidAndNotIp(string)) {
                    copyOnWriteArrayList.add(string);
                }
            }
            monitorRequestList = copyOnWriteArrayList;
        } catch (JSONException e) {
            ALog.e(TAG, "parse hosts failed", (String) null, e, new Object[0]);
        }
    }

    public static boolean isRequestInMonitorList(RequestStatistic requestStatistic) {
        CopyOnWriteArrayList<String> copyOnWriteArrayList;
        if (requestStatistic == null || (copyOnWriteArrayList = monitorRequestList) == null || TextUtils.isEmpty(requestStatistic.host)) {
            return false;
        }
        Iterator<String> it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            if (requestStatistic.host.equalsIgnoreCase(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static void setDegradeRequestList(String str) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "setDegradeRequestList", (String) null, "Degrade List", str);
        }
        if (TextUtils.isEmpty(str)) {
            degradeListMap = null;
            return;
        }
        ConcurrentHashMap<String, List<String>> concurrentHashMap = new ConcurrentHashMap<>();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                Object obj = jSONObject.get(next);
                try {
                    if ("*".equals(obj)) {
                        concurrentHashMap.put(next, ALLOW_ALL_PATH);
                    } else if (obj instanceof JSONArray) {
                        JSONArray jSONArray = (JSONArray) obj;
                        int length = jSONArray.length();
                        ArrayList arrayList = new ArrayList(length);
                        for (int i = 0; i < length; i++) {
                            Object obj2 = jSONArray.get(i);
                            if (obj2 instanceof String) {
                                arrayList.add((String) obj2);
                            }
                        }
                        if (!arrayList.isEmpty()) {
                            concurrentHashMap.put(next, arrayList);
                        }
                    }
                } catch (JSONException unused) {
                }
            }
        } catch (JSONException e) {
            ALog.e(TAG, "parse jsonObject failed", (String) null, e, new Object[0]);
        }
        degradeListMap = concurrentHashMap;
    }

    public static boolean isUrlInDegradeList(HttpUrl httpUrl) {
        ConcurrentHashMap<String, List<String>> concurrentHashMap;
        List<String> list;
        if (httpUrl == null || (concurrentHashMap = degradeListMap) == null || (list = concurrentHashMap.get(httpUrl.host())) == null) {
            return false;
        }
        if (list == ALLOW_ALL_PATH) {
            return true;
        }
        for (String startsWith : list) {
            if (httpUrl.path().startsWith(startsWith)) {
                return true;
            }
        }
        return false;
    }

    public static void enableNetworkSdkOptimizeTest(boolean z) {
        if (z) {
            setGetSessionAsyncEnable(true);
            ThreadPoolExecutorFactory.setNormalExecutorPoolSize(16);
            AwcnConfig.registerPresetSessions("[{\"host\":\"trade-acs.m.taobao.com\", \"protocol\":\"http2\", \"rtt\":\"0rtt\", \"publicKey\": \"acs\", \"isKeepAlive\":true}]");
            return;
        }
        setGetSessionAsyncEnable(false);
        ThreadPoolExecutorFactory.setNormalExecutorPoolSize(6);
    }

    public static boolean isRequestDelayRetryForNoNetwork() {
        return isRequestDelayRetryForNoNetwork;
    }

    public static void setRequestDelayRetryForNoNetwork(boolean z) {
        isRequestDelayRetryForNoNetwork = z;
    }

    public static boolean isBindServiceOptimize() {
        return isBindServiceOptimize;
    }

    public static void setBindServiceOptimize(boolean z) {
        isBindServiceOptimize = z;
    }
}
