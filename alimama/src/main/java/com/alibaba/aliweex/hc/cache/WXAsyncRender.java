package com.alibaba.aliweex.hc.cache;

import android.content.Context;
import android.net.Uri;
import android.taobao.windvane.WVCookieManager;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.api.WVLocation;
import android.taobao.windvane.webview.IWVWebView;
import android.taobao.windvane.webview.WVWebView;
import android.text.TextUtils;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.aliweex.adapter.module.mtop.WXMtopModule;
import com.alibaba.aliweex.adapter.module.mtop.WXMtopRequest;
import com.alibaba.aliweex.hc.cache.AssembleManager;
import com.alibaba.aliweex.utils.reflection.FieldUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.packet.e;
import com.taobao.tao.log.TLog;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.ModuleFactory;
import com.taobao.weex.bridge.ModuleFactoryImpl;
import com.taobao.weex.bridge.WXModuleManager;
import com.taobao.weex.common.WXModule;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class WXAsyncRender {
    private static WXAsyncRender sInstance;
    private HashMap<String, JSONObject> mData = new HashMap<>();

    public static WXAsyncRender getInstance() {
        if (sInstance == null) {
            synchronized (WXAsyncRender.class) {
                if (sInstance == null) {
                    sInstance = new WXAsyncRender();
                }
            }
        }
        return sInstance;
    }

    private WXAsyncRender() {
    }

    public HashMap<String, Object> asyncOpts(boolean z) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return null;
        }
        String config = configAdapter.getConfig("weex_async", z ? "data_h5_enable" : "data_enable", z ? "false" : "true");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("weex_async_data", config);
        if (z) {
            return hashMap;
        }
        hashMap.put("weex_async_require", configAdapter.getConfig("weex_async", "require_enable", "true"));
        return hashMap;
    }

    public String[] asyncOptsWhiteList(boolean z) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return null;
        }
        String config = configAdapter.getConfig("weex_async", z ? "white_list_h5" : "white_list", "");
        if (!TextUtils.isEmpty(config)) {
            return config.split(",");
        }
        return null;
    }

    public boolean enableRequestAsyncData(Map map, String[] strArr, String str) {
        boolean z;
        boolean z2;
        if (map != null && !TextUtils.isEmpty(str) && "true".equals((String) map.get("weex_async_data"))) {
            Uri parse = Uri.parse(str);
            if (parse != null) {
                z = !TextUtils.isEmpty(parse.getQueryParameter("wh_pid"));
                z2 = parse.getBooleanQueryParameter("wh_kangarooprefetch", false);
            } else {
                z = false;
                z2 = false;
            }
            if (z || z2) {
                return true;
            }
            if (!(strArr == null || parse == null)) {
                String uri = parse.buildUpon().clearQuery().fragment("").build().toString();
                for (String contains : strArr) {
                    if (uri.contains(contains)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void requestAsyncDataForWeex(Context context, final String str, final String str2) {
        WeexCacheMsgPanel.d("数据异步请求开始");
        CachePerf.getInstance().mFSStartTime = System.currentTimeMillis();
        requestAsyncData(context, false, str2, (String) null, new IAsyncDataSuccessListener() {
            /* access modifiers changed from: protected */
            public void onSuccess(Object obj) {
                super.onSuccess(obj);
                if (obj instanceof JSONObject) {
                    final JSONObject jSONObject = (JSONObject) obj;
                    WeexCacheMsgPanel.d("首屏 mtop 请求时间：" + CachePerf.getInstance().mFSMtopTime);
                    JSONArray jSONArray = jSONObject.getJSONArray("seedComboUris");
                    if (!"true".equals(jSONObject.getString("useAsyncRender")) || jSONArray == null || jSONArray.size() <= 0) {
                        WXAsyncRender.this.addAsyncDataToModule(str, jSONObject);
                        return;
                    }
                    JSONObject jSONObject2 = new JSONObject();
                    JSONArray jSONArray2 = new JSONArray();
                    for (int i = 0; i < jSONArray.size(); i++) {
                        JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                        if ("js".equals(jSONObject3.getString("type"))) {
                            jSONArray2.add(jSONObject3.getString("uri"));
                        }
                    }
                    if (jSONArray2.size() > 0) {
                        jSONObject2.put("packages", (Object) jSONArray2);
                        final long currentTimeMillis = System.currentTimeMillis();
                        AssembleManager.getInstance().processAssembleWithDep(str2, jSONObject2.toJSONString(), new AssembleManager.IPageLoaderCallback() {
                            public void onFinished(String str) {
                                CachePerf.getInstance().mFSModuleCacheTime = System.currentTimeMillis() - currentTimeMillis;
                                WeexCacheMsgPanel.d("首屏模块处理时间：" + CachePerf.getInstance().mFSModuleCacheTime);
                                jSONObject.put("comboJSScript", (Object) str);
                                WXAsyncRender.this.addAsyncDataToModule(str, jSONObject);
                            }

                            public void onFailed() {
                                WXAsyncRender.this.fireFail(str);
                            }
                        });
                    }
                }
            }
        }, new IAsyncDataFailListener() {
            /* access modifiers changed from: protected */
            public void onFail(Object obj) {
                super.onFail(obj);
                WXAsyncRender.this.fireFail(str);
            }
        });
    }

    public void fireFail(String str) {
        addAsyncDataToModule(str, (JSONObject) null);
    }

    public void requestAsyncData(Context context, boolean z, String str, String str2, IAsyncDataSuccessListener iAsyncDataSuccessListener, IAsyncDataFailListener iAsyncDataFailListener) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("api", (Object) "mtop.tmall.kangaroo.core.service.route.PageRecommendService");
        jSONObject.put("v", (Object) "1.0");
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("url", (Object) str);
        jSONObject2.put(e.n, (Object) z ? "phone" : "native");
        if (!TextUtils.isEmpty(str2)) {
            jSONObject.put("userAgent", (Object) str2);
        }
        Map<String, String> cookie = getCookie(str);
        String str3 = "";
        String str4 = "";
        if (cookie != null) {
            str3 = cookie.get("sm4") != null ? cookie.get("sm4") : "";
            str4 = cookie.get("hng") != null ? cookie.get("hng") : "";
        }
        String str5 = "sm4=" + str3 + ";hng=" + str4;
        String[] cookieExtraKeys = getCookieExtraKeys(str);
        if (!(cookieExtraKeys == null || cookieExtraKeys.length <= 0 || cookie == null)) {
            for (String str6 : cookieExtraKeys) {
                String str7 = cookie.get(str6);
                if (!TextUtils.isEmpty(str7)) {
                    str5 = str5 + ";" + str6 + SymbolExpUtil.SYMBOL_EQUAL + str7;
                }
            }
        }
        jSONObject2.put("cookie", (Object) str5);
        if (enableLbsLocation(str)) {
            final JSONObject jSONObject3 = jSONObject2;
            final Context context2 = context;
            final JSONObject jSONObject4 = jSONObject;
            final IAsyncDataSuccessListener iAsyncDataSuccessListener2 = iAsyncDataSuccessListener;
            final IAsyncDataFailListener iAsyncDataFailListener2 = iAsyncDataFailListener;
            AnonymousClass3 r0 = new IAsyncDataSuccessListener() {
                /* access modifiers changed from: protected */
                public void onSuccess(Object obj) {
                    if (obj != null) {
                        jSONObject3.put("lbs", obj);
                    }
                    WXAsyncRender.this.mtopRequest(context2, jSONObject4, jSONObject3, iAsyncDataSuccessListener2, iAsyncDataFailListener2);
                }
            };
            final Context context3 = context;
            final JSONObject jSONObject5 = jSONObject;
            final JSONObject jSONObject6 = jSONObject2;
            getLbsLocation(context, r0, new IAsyncDataFailListener() {
                /* access modifiers changed from: protected */
                public void onFail(Object obj) {
                    WXAsyncRender.this.mtopRequest(context3, jSONObject5, jSONObject6, iAsyncDataSuccessListener2, iAsyncDataFailListener2);
                }
            });
            return;
        }
        mtopRequest(context, jSONObject, jSONObject2, iAsyncDataSuccessListener, iAsyncDataFailListener);
    }

    /* access modifiers changed from: private */
    public void mtopRequest(Context context, JSONObject jSONObject, JSONObject jSONObject2, final IAsyncDataSuccessListener iAsyncDataSuccessListener, IAsyncDataFailListener iAsyncDataFailListener) {
        jSONObject.put("data", (Object) jSONObject2);
        final long currentTimeMillis = System.currentTimeMillis();
        new WXMtopRequest(WXMtopModule.MTOP_VERSION.V2).request(context, jSONObject, new IAsyncDataSuccessListener() {
            /* access modifiers changed from: protected */
            public void onSuccess(Object obj) {
                JSONObject jSONObject;
                JSONObject jSONObject2;
                super.onSuccess(obj);
                CachePerf.getInstance().mFSMtopTime = System.currentTimeMillis() - currentTimeMillis;
                if (AssembleManager.SHOW_LOG) {
                    TLog.logd(AssembleManager.TAG, "mtopRequest:" + CachePerf.getInstance().mFSMtopTime);
                }
                if ((obj instanceof JSONObject) && (jSONObject = ((JSONObject) obj).getJSONObject("data")) != null && (jSONObject2 = jSONObject.getJSONObject("resultValue")) != null) {
                    iAsyncDataSuccessListener.onSuccess(jSONObject2);
                }
            }
        }, iAsyncDataFailListener);
    }

    private Map<String, String> getCookie(String str) {
        String cookie = WVCookieManager.getCookie(str);
        if (TextUtils.isEmpty(cookie)) {
            return null;
        }
        String[] split = cookie.replace("\"", "\\\\\"").split(";");
        HashMap hashMap = new HashMap();
        for (String split2 : split) {
            String[] split3 = split2.split(SymbolExpUtil.SYMBOL_EQUAL);
            if (split3.length > 1) {
                hashMap.put(split3[0].trim(), split3[1].trim());
            }
        }
        return hashMap;
    }

    private void getLbsLocation(Context context, IAsyncDataSuccessListener iAsyncDataSuccessListener, IAsyncDataFailListener iAsyncDataFailListener) {
        if (context != null) {
            WVLocation wVLocation = new WVLocation();
            wVLocation.initialize(context, (WVWebView) null);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("enableHighAcuracy", (Object) true);
            jSONObject.put(ILocatable.ADDRESS, (Object) true);
            final IAsyncDataSuccessListener iAsyncDataSuccessListener2 = iAsyncDataSuccessListener;
            final IAsyncDataFailListener iAsyncDataFailListener2 = iAsyncDataFailListener;
            wVLocation.getLocation(new WVCallBackContext((IWVWebView) null, (String) null, (String) null, (String) null) {
                public void success(WVResult wVResult) {
                    wVResult.setSuccess();
                    iAsyncDataSuccessListener2.onSuccess(wVResult.toJsonString());
                }

                public void error(WVResult wVResult) {
                    iAsyncDataFailListener2.onFail(wVResult);
                }
            }, jSONObject.toJSONString());
        }
    }

    private String[] getCookieExtraKeys(String str) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null || !"true".equals(configAdapter.getConfig("weex_async", "cookie_extra_enable", "false"))) {
            return null;
        }
        String config = configAdapter.getConfig("weex_async", "cookie_extra_key", "");
        String config2 = configAdapter.getConfig("weex_async", "cookie_extra_white_list", "");
        if (TextUtils.isEmpty(config) || TextUtils.isEmpty(config2)) {
            return null;
        }
        String[] split = config.split(",");
        for (String contains : config2.split(",")) {
            if (str.contains(contains)) {
                return split;
            }
        }
        return null;
    }

    private boolean enableLbsLocation(String str) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null || !"true".equals(configAdapter.getConfig("weex_async", "lbs_enable", "false"))) {
            return false;
        }
        boolean booleanQueryParameter = Uri.parse(str).getBooleanQueryParameter("wh_prelbs", false);
        if (!booleanQueryParameter) {
            String config = configAdapter.getConfig("weex_async", "lbs_white_list", "");
            if (!TextUtils.isEmpty(config)) {
                for (String contains : config.split(",")) {
                    if (str.contains(contains)) {
                        return true;
                    }
                }
            }
        }
        return booleanQueryParameter;
    }

    /* access modifiers changed from: private */
    public void addAsyncDataToModule(String str, JSONObject jSONObject) {
        ModuleFactoryImpl moduleFactoryImpl;
        ModuleFactory moduleFactory;
        Object staticFieldValue = FieldUtil.getStaticFieldValue(WXModuleManager.class, "sModuleFactoryMap");
        if (staticFieldValue != null && (staticFieldValue instanceof Map) && (moduleFactoryImpl = (ModuleFactoryImpl) ((Map) staticFieldValue).get("asyncRender")) != null && (moduleFactory = moduleFactoryImpl.mFactory) != null) {
            try {
                Method declaredMethod = WXModuleManager.class.getDeclaredMethod("findModule", new Class[]{String.class, String.class, ModuleFactory.class});
                declaredMethod.setAccessible(true);
                Object invoke = declaredMethod.invoke((Object) null, new Object[]{str, "asyncRender", moduleFactory});
                if (invoke != null && (invoke instanceof WXModule)) {
                    WXAsyncRenderModule wXAsyncRenderModule = (WXAsyncRenderModule) invoke;
                    if (jSONObject != null) {
                        wXAsyncRenderModule.addAsyncData(str, jSONObject);
                    } else {
                        wXAsyncRenderModule.setFailFlag(str);
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
    }

    static abstract class IAsyncDataSuccessListener implements JSCallback {
        public void invokeAndKeepAlive(Object obj) {
        }

        /* access modifiers changed from: protected */
        public void onSuccess(Object obj) {
        }

        IAsyncDataSuccessListener() {
        }

        public void invoke(Object obj) {
            onSuccess(obj);
        }
    }

    static abstract class IAsyncDataFailListener implements JSCallback {
        public void invokeAndKeepAlive(Object obj) {
        }

        /* access modifiers changed from: protected */
        public void onFail(Object obj) {
        }

        IAsyncDataFailListener() {
        }

        public void invoke(Object obj) {
            onFail(obj);
        }
    }
}
