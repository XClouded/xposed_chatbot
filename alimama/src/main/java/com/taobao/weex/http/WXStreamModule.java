package com.taobao.weex.http;

import alimama.com.unweventparse.constants.EventConstants;
import android.net.Uri;
import android.text.TextUtils;
import anet.channel.request.Request;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.http.Options;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.utils.WXLogUtils;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WXStreamModule extends WXModule {
    static final Pattern CHARSET_PATTERN = Pattern.compile("charset=([a-z0-9-]+)");
    public static final String STATUS = "status";
    public static final String STATUS_TEXT = "statusText";
    final IWXHttpAdapter mAdapter;

    private interface ResponseCallback {
        void onResponse(WXResponse wXResponse, Map<String, String> map);
    }

    public WXStreamModule() {
        this((IWXHttpAdapter) null);
    }

    public WXStreamModule(IWXHttpAdapter iWXHttpAdapter) {
        this.mAdapter = iWXHttpAdapter;
    }

    @JSMethod(uiThread = false)
    @Deprecated
    public void sendHttp(JSONObject jSONObject, final String str) {
        String string = jSONObject.getString("method");
        String string2 = jSONObject.getString("url");
        JSONObject jSONObject2 = jSONObject.getJSONObject("header");
        String string3 = jSONObject.getString("body");
        int intValue = jSONObject.getIntValue("timeout");
        if (string != null) {
            string = string.toUpperCase(Locale.ROOT);
        }
        Options.Builder builder = new Options.Builder();
        if (!"GET".equals(string) && !"POST".equals(string) && !Request.Method.PUT.equals(string) && !Request.Method.DELETE.equals(string) && !Request.Method.HEAD.equals(string) && !"PATCH".equals(string)) {
            string = "GET";
        }
        Options.Builder timeout = builder.setMethod(string).setUrl(string2).setBody(string3).setTimeout(intValue);
        extractHeaders(jSONObject2, timeout);
        sendRequest(timeout.createOptions(), new ResponseCallback() {
            public void onResponse(WXResponse wXResponse, Map<String, String> map) {
                String str;
                if (str != null && WXStreamModule.this.mWXSDKInstance != null) {
                    WXBridgeManager instance = WXBridgeManager.getInstance();
                    String instanceId = WXStreamModule.this.mWXSDKInstance.getInstanceId();
                    String str2 = str;
                    if (wXResponse == null || wXResponse.originalData == null) {
                        str = "{}";
                    } else {
                        str = WXStreamModule.readAsString(wXResponse.originalData, map != null ? WXStreamModule.getHeader(map, "Content-Type") : "");
                    }
                    instance.callback(instanceId, str2, str);
                }
            }
        }, (JSCallback) null, this.mWXSDKInstance.getInstanceId(), this.mWXSDKInstance.getBundleUrl());
    }

    @JSMethod(uiThread = false)
    public void fetch(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        fetch(jSONObject, jSCallback, jSCallback2, this.mWXSDKInstance.getInstanceId(), this.mWXSDKInstance.getBundleUrl());
    }

    public void fetch(JSONObject jSONObject, final JSCallback jSCallback, JSCallback jSCallback2, final String str, String str2) {
        if (!(jSONObject == null || jSONObject.getString("url") == null)) {
            String string = jSONObject.getString("method");
            String string2 = jSONObject.getString("url");
            JSONObject jSONObject2 = jSONObject.getJSONObject(EventConstants.Mtop.HEADERS);
            String string3 = jSONObject.getString("body");
            String string4 = jSONObject.getString("type");
            int intValue = jSONObject.getIntValue("timeout");
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (!(sDKInstance == null || sDKInstance.getStreamNetworkHandler() == null)) {
                String fetchLocal = sDKInstance.getStreamNetworkHandler().fetchLocal(string2);
                if (!TextUtils.isEmpty(fetchLocal)) {
                    string2 = fetchLocal;
                }
            }
            if (string != null) {
                string = string.toUpperCase(Locale.ROOT);
            }
            Options.Builder builder = new Options.Builder();
            if (!"GET".equals(string) && !"POST".equals(string) && !Request.Method.PUT.equals(string) && !Request.Method.DELETE.equals(string) && !Request.Method.HEAD.equals(string) && !"PATCH".equals(string)) {
                string = "GET";
            }
            Options.Builder timeout = builder.setMethod(string).setUrl(string2).setBody(string3).setType(string4).setTimeout(intValue);
            extractHeaders(jSONObject2, timeout);
            final Options createOptions = timeout.createOptions();
            sendRequest(createOptions, new ResponseCallback() {
                public void onResponse(WXResponse wXResponse, Map<String, String> map) {
                    if (jSCallback != null) {
                        HashMap hashMap = new HashMap();
                        if (wXResponse == null || "-1".equals(wXResponse.statusCode)) {
                            hashMap.put("status", -1);
                            hashMap.put(WXStreamModule.STATUS_TEXT, Status.ERR_CONNECT_FAILED);
                        } else {
                            int parseInt = Integer.parseInt(wXResponse.statusCode);
                            hashMap.put("status", Integer.valueOf(parseInt));
                            hashMap.put("ok", Boolean.valueOf(parseInt >= 200 && parseInt <= 299));
                            if (wXResponse.originalData == null) {
                                hashMap.put("data", (Object) null);
                            } else {
                                try {
                                    hashMap.put("data", WXStreamModule.this.parseData(WXStreamModule.readAsString(wXResponse.originalData, map != null ? WXStreamModule.getHeader(map, "Content-Type") : ""), createOptions.getType()));
                                } catch (JSONException e) {
                                    WXLogUtils.e("", (Throwable) e);
                                    hashMap.put("ok", false);
                                    hashMap.put("data", "{'err':'Data parse failed!'}");
                                }
                            }
                            hashMap.put(WXStreamModule.STATUS_TEXT, Status.getStatusText(wXResponse.statusCode));
                        }
                        hashMap.put(EventConstants.Mtop.HEADERS, map);
                        WXStateRecord instance = WXStateRecord.getInstance();
                        String str = str;
                        StringBuilder sb = new StringBuilder();
                        sb.append("stream response code:");
                        sb.append(wXResponse != null ? wXResponse.statusCode : BuildConfig.buildJavascriptFrameworkVersion);
                        instance.recordAction(str, sb.toString());
                        jSCallback.invoke(hashMap);
                    }
                }
            }, jSCallback2, str, str2);
        } else if (jSCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("ok", false);
            hashMap.put(STATUS_TEXT, Status.ERR_INVALID_REQUEST);
            jSCallback.invoke(hashMap);
        }
    }

    /* access modifiers changed from: package-private */
    public Object parseData(String str, Options.Type type) throws JSONException {
        if (type == Options.Type.json) {
            return JSONObject.parse(str);
        }
        if (type != Options.Type.jsonp) {
            return str;
        }
        if (str == null || str.isEmpty()) {
            return new JSONObject();
        }
        int indexOf = str.indexOf(Operators.BRACKET_START_STR) + 1;
        int lastIndexOf = str.lastIndexOf(Operators.BRACKET_END_STR);
        if (indexOf == 0 || indexOf >= lastIndexOf || lastIndexOf <= 0) {
            return new JSONObject();
        }
        return JSONObject.parse(str.substring(indexOf, lastIndexOf));
    }

    static String getHeader(Map<String, String> map, String str) {
        if (map == null || str == null) {
            return null;
        }
        if (map.containsKey(str)) {
            return map.get(str);
        }
        return map.get(str.toLowerCase(Locale.ROOT));
    }

    static String readAsString(byte[] bArr, String str) {
        String str2 = "utf-8";
        if (str != null) {
            Matcher matcher = CHARSET_PATTERN.matcher(str.toLowerCase(Locale.ROOT));
            if (matcher.find()) {
                str2 = matcher.group(1);
            }
        }
        try {
            return new String(bArr, str2);
        } catch (UnsupportedEncodingException e) {
            WXLogUtils.e("", (Throwable) e);
            return new String(bArr);
        }
    }

    private void extractHeaders(JSONObject jSONObject, Options.Builder builder) {
        String assembleUserAgent = WXHttpUtil.assembleUserAgent(WXEnvironment.getApplication(), WXEnvironment.getConfig());
        if (jSONObject != null) {
            for (String next : jSONObject.keySet()) {
                if (next.equals("user-agent")) {
                    assembleUserAgent = jSONObject.getString(next);
                } else {
                    builder.putHeader(next, jSONObject.getString(next));
                }
            }
        }
        builder.putHeader("user-agent", assembleUserAgent);
    }

    private void sendRequest(Options options, ResponseCallback responseCallback, JSCallback jSCallback, String str, String str2) {
        WXRequest wXRequest = new WXRequest();
        wXRequest.method = options.getMethod();
        wXRequest.url = WXSDKManager.getInstance().getURIAdapter().rewrite(str2, "request", Uri.parse(options.getUrl())).toString();
        wXRequest.body = options.getBody();
        wXRequest.timeoutMs = options.getTimeout();
        wXRequest.instanceId = str;
        if (options.getHeaders() != null) {
            if (wXRequest.paramMap == null) {
                wXRequest.paramMap = options.getHeaders();
            } else {
                wXRequest.paramMap.putAll(options.getHeaders());
            }
        }
        IWXHttpAdapter iWXHttpAdapter = this.mAdapter == null ? WXSDKManager.getInstance().getIWXHttpAdapter() : this.mAdapter;
        if (iWXHttpAdapter != null) {
            iWXHttpAdapter.sendRequest(wXRequest, new StreamHttpListener(responseCallback, jSCallback));
        } else {
            WXLogUtils.e("WXStreamModule", "No HttpAdapter found,request failed.");
        }
    }

    private static class StreamHttpListener implements IWXHttpAdapter.OnHttpListener {
        private ResponseCallback mCallback;
        private JSCallback mProgressCallback;
        private Map<String, String> mRespHeaders;
        private Map<String, Object> mResponse;

        public void onHttpUploadProgress(int i) {
        }

        private StreamHttpListener(ResponseCallback responseCallback, JSCallback jSCallback) {
            this.mResponse = new HashMap();
            this.mCallback = responseCallback;
            this.mProgressCallback = jSCallback;
        }

        public void onHttpStart() {
            if (this.mProgressCallback != null) {
                this.mResponse.put("readyState", 1);
                this.mResponse.put("length", 0);
                this.mProgressCallback.invokeAndKeepAlive(new HashMap(this.mResponse));
            }
        }

        public void onHeadersReceived(int i, Map<String, List<String>> map) {
            this.mResponse.put("readyState", 2);
            this.mResponse.put("status", Integer.valueOf(i));
            HashMap hashMap = new HashMap();
            if (map != null) {
                for (Map.Entry next : map.entrySet()) {
                    if (((List) next.getValue()).size() != 0) {
                        if (((List) next.getValue()).size() == 1) {
                            hashMap.put(next.getKey() == null ? "_" : (String) next.getKey(), ((List) next.getValue()).get(0));
                        } else {
                            hashMap.put(next.getKey() == null ? "_" : (String) next.getKey(), ((List) next.getValue()).toString());
                        }
                    }
                }
            }
            this.mResponse.put(EventConstants.Mtop.HEADERS, hashMap);
            this.mRespHeaders = hashMap;
            if (this.mProgressCallback != null) {
                this.mProgressCallback.invokeAndKeepAlive(new HashMap(this.mResponse));
            }
        }

        public void onHttpResponseProgress(int i) {
            this.mResponse.put("length", Integer.valueOf(i));
            if (this.mProgressCallback != null) {
                this.mProgressCallback.invokeAndKeepAlive(new HashMap(this.mResponse));
            }
        }

        public void onHttpFinish(WXResponse wXResponse) {
            if (this.mCallback != null) {
                this.mCallback.onResponse(wXResponse, this.mRespHeaders);
            }
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("WXStreamModule", (wXResponse == null || wXResponse.originalData == null) ? "response data is NUll!" : new String(wXResponse.originalData));
            }
        }
    }
}
