package android.taobao.windvane.prefetch;

import android.net.Uri;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.jsbridge.api.WVAPI;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.cons.c;
import com.taobao.weaver.prefetch.PrefetchDataCallback;
import com.taobao.weaver.prefetch.PrefetchDataResponse;
import com.taobao.weaver.prefetch.PrefetchHandler;
import com.taobao.weaver.prefetch.PrefetchType;
import com.taobao.weaver.prefetch.WMLPrefetchDecision;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class WVPrefetchHandler implements PrefetchHandler {
    private static String GET_DATA = "Prefetch.getData";
    private static String REQUEST_DATA = "Prefetch.requestData";
    private static String TEST_KEY = "test";

    public WMLPrefetchDecision isSupported(String str, Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject(map);
        boolean booleanValue = jSONObject.getBoolean("isSupport") != null ? jSONObject.getBoolean("isSupport").booleanValue() : false;
        String queryParameter = Uri.parse(str).getQueryParameter(c.n);
        WMLPrefetchDecision wMLPrefetchDecision = new WMLPrefetchDecision();
        if (GET_DATA.equals(queryParameter) || REQUEST_DATA.equals(queryParameter) || booleanValue) {
            wMLPrefetchDecision.externalKey = TEST_KEY;
            if (map.containsKey("externalKey")) {
                wMLPrefetchDecision.externalKey = jSONObject.getString("externalKey");
            }
            wMLPrefetchDecision.status = PrefetchType.SUPPORTED;
        }
        return wMLPrefetchDecision;
    }

    public String prefetchData(String str, Map<String, Object> map, final PrefetchDataCallback prefetchDataCallback) {
        JSONObject jSONObject = new JSONObject(map);
        if (jSONObject.getBoolean("isLocal") != null ? jSONObject.getBoolean("isLocal").booleanValue() : true) {
            HashMap hashMap = new HashMap();
            hashMap.put("client", "TBClient");
            hashMap.put(c.n, WVAPI.PluginName.API_PREFETCH);
            hashMap.put("type", "Local");
            PrefetchDataResponse prefetchDataResponse = new PrefetchDataResponse();
            prefetchDataResponse.data = hashMap;
            prefetchDataResponse.usageLimit = 10;
            prefetchDataResponse.maxAge = 500;
            prefetchDataCallback.onComplete(prefetchDataResponse);
            return null;
        }
        ConnectManager.getInstance().connect(str, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse httpResponse, int i) {
                if (httpResponse == null || httpResponse.getData().length == 0) {
                    prefetchDataCallback.onError("-4", "getData Error");
                    return;
                }
                try {
                    String str = new String(httpResponse.getData(), "utf-8");
                    PrefetchDataResponse prefetchDataResponse = new PrefetchDataResponse();
                    prefetchDataResponse.data = JSONObject.parseObject(str);
                    prefetchDataResponse.maxAge = 500;
                    prefetchDataResponse.usageLimit = 10;
                    prefetchDataCallback.onComplete(prefetchDataResponse);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }
}
