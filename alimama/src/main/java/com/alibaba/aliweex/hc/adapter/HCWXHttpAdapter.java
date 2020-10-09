package com.alibaba.aliweex.hc.adapter;

import android.net.Uri;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.utils.AppResInfo;
import android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.adapter.adapter.WXHttpAdapter;
import com.alibaba.aliweex.hc.cache.AssembleManager;
import com.alibaba.aliweex.hc.cache.PackageCache;
import com.alibaba.aliweex.hc.cache.WeexCacheMsgPanel;
import com.alibaba.aliweex.interceptor.network.NetworkTracker;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import java.util.List;
import java.util.Map;

public class HCWXHttpAdapter extends WXHttpAdapter {
    /* access modifiers changed from: protected */
    public void processZCache(String str, WXResponse wXResponse, IWXHttpAdapter.OnHttpListener onHttpListener) {
        if ("true".equals(getWeexCacheHeaderFromAppResInfo(getAppResInfoFromZcache(str)))) {
            hitZCacheWithWeexCache(wXResponse, onHttpListener, str);
        } else {
            super.processZCache(str, wXResponse, onHttpListener);
        }
    }

    /* access modifiers changed from: protected */
    public void processHttp(String str, Uri uri, WXRequest wXRequest, final WXResponse wXResponse, final IWXHttpAdapter.OnHttpListener onHttpListener, NetworkTracker networkTracker) {
        String uri2 = uri.buildUpon().clearQuery().scheme("").build().toString();
        if (uri2.startsWith(HttpConstant.SCHEME_SPLIT)) {
            uri2 = uri2.substring(3);
        }
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null) {
            String config = configAdapter.getConfig("weexcache", uri2, "-1");
            if (!"-1".equals(config)) {
                String pageFromDisk = PackageCache.getInstance().getPageFromDisk(config);
                if (!TextUtils.isEmpty(pageFromDisk)) {
                    wXResponse.statusCode = AlipayAuthConstant.LoginResult.SUCCESS;
                    wXResponse.originalData = pageFromDisk.getBytes();
                    wXResponse.extendParams.put("requestType", "avfs");
                    wXResponse.extendParams.put("connectionType", "avfs");
                    WeexCacheMsgPanel.d("命中本地页面模版");
                    AssembleManager.getInstance().processAssembleWithTemplate(str, wXResponse.originalData, new AssembleManager.IPageLoaderCallback() {
                        public void onFinished(String str) {
                            WeexCacheMsgPanel.d("缓存方案处理结束");
                            wXResponse.extendParams.put("connectionType", "weex-cache-avfs");
                            wXResponse.originalData = str.getBytes();
                            onHttpListener.onHttpFinish(wXResponse);
                        }

                        public void onFailed() {
                            wXResponse.statusCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
                            wXResponse.errorCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
                            wXResponse.errorMsg = "process weex cache failed, degradeToH5";
                            onHttpListener.onHttpFinish(wXResponse);
                        }
                    });
                } else {
                    wXResponse.extendParams.put("throughWeexCache", config);
                }
            }
        }
        super.processHttp(str, uri, wXRequest, wXResponse, onHttpListener, networkTracker);
    }

    /* access modifiers changed from: protected */
    public void onHttpFinish(final IWXHttpAdapter.OnHttpListener onHttpListener, String str, final WXResponse wXResponse, int i, Map<String, List<String>> map) {
        List list = i == 200 ? map.get("weex-cache") : null;
        boolean z = false;
        if (list != null && list.size() > 0 && "true".equals((String) list.get(0))) {
            z = true;
        }
        if (z) {
            String str2 = (String) wXResponse.extendParams.get("throughWeexCache");
            if (!TextUtils.isEmpty(str2)) {
                PackageCache.getInstance().cachePage(str, str2, wXResponse.originalData);
            }
            WeexCacheMsgPanel.d("命中缓存方案");
            AssembleManager.getInstance().processAssembleWithTemplate(str, wXResponse.originalData, new AssembleManager.IPageLoaderCallback() {
                public void onFinished(String str) {
                    WeexCacheMsgPanel.d("缓存方案处理结束");
                    wXResponse.extendParams.put("connectionType", "weex-cache");
                    wXResponse.originalData = str.getBytes();
                    onHttpListener.onHttpFinish(wXResponse);
                }

                public void onFailed() {
                    wXResponse.statusCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
                    wXResponse.errorCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
                    wXResponse.errorMsg = "process weex cache failed, degradeToH5";
                    onHttpListener.onHttpFinish(wXResponse);
                }
            });
            return;
        }
        super.onHttpFinish(onHttpListener, str, wXResponse, i, map);
    }

    private void hitZCacheWithWeexCache(final WXResponse wXResponse, final IWXHttpAdapter.OnHttpListener onHttpListener, String str) {
        WeexCacheMsgPanel.Log.d("命中页面ZCache&缓存方案");
        AssembleManager.getInstance().processAssembleWithTemplate(str, wXResponse.originalData, new AssembleManager.IPageLoaderCallback() {
            public void onFinished(String str) {
                WeexCacheMsgPanel.Log.d("缓存方案处理结束");
                wXResponse.extendParams.put("connectionType", "weex-cache-zcache");
                wXResponse.originalData = str.getBytes();
                onHttpListener.onHttpFinish(wXResponse);
            }

            public void onFailed() {
                wXResponse.statusCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
                wXResponse.errorCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
                wXResponse.errorMsg = "process weex cache failed, degradeToH5";
                onHttpListener.onHttpFinish(wXResponse);
            }
        });
    }

    private String getWeexCacheHeaderFromAppResInfo(AppResInfo appResInfo) {
        if (appResInfo == null || appResInfo.mHeaders == null) {
            return null;
        }
        return appResInfo.mHeaders.optString("weex-cache");
    }

    private AppResInfo getAppResInfoFromZcache(String str) {
        try {
            Uri parse = Uri.parse(str);
            if (parse.getBooleanQueryParameter("wh_weex", false)) {
                String host = parse.getHost();
                return WVZipSecurityManager.getInstance().getAppResInfo((ZipAppInfo) null, Uri.parse(str.replace(host, parse.getHost() + ".local.weex")).buildUpon().scheme("http").build().toString());
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }
}
