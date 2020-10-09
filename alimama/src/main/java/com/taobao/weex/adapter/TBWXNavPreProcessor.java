package com.taobao.weex.adapter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.aliweex.hc.HC;
import com.alibaba.aliweex.preLoad.WXPreLoadManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.android.nav.Nav;
import com.taobao.tao.Globals;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.tao.shop.TBSREngine;
import com.taobao.vessel.utils.Utils;
import com.taobao.weex.TBWXSDKEngine;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.constant.Constants;
import com.taobao.weex.module.NavPrefetchShopFetchManager;
import com.taobao.weex.utils.TBWXConfigManger;
import com.taobao.weex.utils.UriUtil;
import com.taobao.weex.utils.WXLogUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TBWXNavPreProcessor implements Nav.NavPreprocessor {
    private static final String BROWSER_ONLY_CATEGORY = "com.taobao.intent.category.HYBRID_UI";
    private static final String FROM = "_wx_f_";
    private static final String FROM_WEEX = "1";
    private static final String FROM_WEEX_DEGRADE_H5 = "2";
    private static final String TAG = "TBWXNavPreProcessor";
    private static final Map<String, String> URL_CACHE = new HashMap();
    private static final String WEEX_ORIGINAL_URL = "weex_original_url";

    public boolean beforeNavTo(Intent intent) {
        String str;
        String str2;
        String matchedUrl;
        HC.getInstance().beforeNavTo(intent);
        Uri data = intent.getData();
        boolean z = false;
        if (data == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("uri:");
            sb.append(data);
            WXLogUtils.d(TAG, sb.toString() == null ? "uri is null!" : data.toString());
            return false;
        }
        intent.putExtra("WEEX_NAV_PROCESSOR_TIME", System.currentTimeMillis());
        if (TextUtils.isEmpty(data.getHost()) || ((!"shop.m.taobao.com".equals(data.getHost()) && !data.getHost().contains(".m.tmall.com") && (!data.getHost().contains(ImageStrategyConfig.SHOP) || !data.getHost().contains(".taobao.com"))) || (matchedUrl = TBSREngine.matchedUrl(data)) == null || !matchedUrl.contains(Utils.WH_WEEX_TRUE))) {
            Uri addScheme = UriUtil.addScheme(data);
            String str3 = null;
            if (addScheme.isHierarchical()) {
                str3 = addScheme.getQueryParameter(FROM);
                Uri.Builder buildUpon = addScheme.buildUpon();
                if (TextUtils.isEmpty(str3)) {
                    buildUpon.appendQueryParameter(FROM, "1");
                }
                addScheme = buildUpon.build();
                str = addScheme.getQueryParameter(WEEX_ORIGINAL_URL);
            } else {
                str = null;
            }
            String uri = addScheme.toString();
            String str4 = "99500";
            if (TBWXConfigManger.getInstance().isDegrade() || !addScheme.isHierarchical() || intent.hasCategory("com.taobao.intent.category.HYBRID_UI")) {
                WXLogUtils.d(TAG, "degrade:" + TBWXConfigManger.getInstance().isDegrade());
                WXLogUtils.d(TAG, "hierarchical:" + addScheme.isHierarchical());
                WXLogUtils.d(TAG, "category:" + intent.hasCategory("com.taobao.intent.category.HYBRID_UI"));
                if (!TextUtils.isEmpty(str3)) {
                    z = TextUtils.equals(str3, "2");
                }
                if (z) {
                    try {
                        if (addScheme.isHierarchical()) {
                            if (!TextUtils.isEmpty(str)) {
                                Set<String> queryParameterNames = addScheme.getQueryParameterNames();
                                Uri.Builder buildUpon2 = Uri.parse(str).buildUpon();
                                for (String next : queryParameterNames) {
                                    buildUpon2.appendQueryParameter(next, addScheme.getQueryParameter(next));
                                }
                                Uri build = buildUpon2.build();
                                intent.setData(build);
                                WXLogUtils.d(TAG, "weex_degrade_h5:" + build.toString());
                                str4 = "99501";
                            } else {
                                str4 = "99502";
                            }
                        }
                    } catch (Throwable unused) {
                    }
                }
                reportExpURL(uri, str4);
                return true;
            }
            reportExpURL(uri, str4);
            Uri addScheme2 = UriUtil.addScheme(addScheme);
            WXLogUtils.d(TAG, "after addScheme uri:" + addScheme2.toString());
            if (WXEnvironment.isApkDebugable()) {
                String queryParameter = addScheme2.getQueryParameter("_wx_devtool");
                if (!TextUtils.isEmpty(queryParameter)) {
                    WXEnvironment.sRemoteDebugProxyUrl = queryParameter;
                    WXEnvironment.sDebugServerConnectable = true;
                    WXLogUtils.d("sRemoteDebugProxyUrl=" + WXEnvironment.sRemoteDebugProxyUrl);
                    WXSDKEngine.reload(Globals.getApplication(), false);
                    intent.setData(Uri.parse("https://www.taobao.com"));
                    Toast.makeText(Globals.getApplication(), "已经开启devtool功能!", 0).show();
                    return true;
                }
            }
            Uri bundleUri = UriUtil.getBundleUri(addScheme2);
            if (bundleUri == null) {
                str2 = "bundleUri is null!";
            } else {
                str2 = "bundleUrl:" + bundleUri.toString();
            }
            WXLogUtils.d(TAG, str2);
            if (bundleUri != null) {
                String uri2 = bundleUri.toString();
                preInitWeexInstance(uri, bundleUri);
                if (uri2.startsWith("//")) {
                    uri2 = uri2.replaceFirst("//", "http://");
                }
                URL_CACHE.put(uri2, uri);
                WXLogUtils.d(TAG, "WEEX normal put bundleUrl extra :" + uri2);
                intent.putExtra(Constants.WEEX_BUNDLE_URL, uri2);
                intent.putExtra(Constants.WEEX_URL, addScheme2.toString());
                if (addScheme2.getBooleanQueryParameter(Constants.WX_SHOP_RENDER_ACTIVITY, false)) {
                    intent.addCategory(Constants.WEEX_SHOP_RENDER_CATEGORY);
                    WXLogUtils.d(TAG, "WEEX add Shop Category");
                    if (addScheme2.getBooleanQueryParameter(Constants.WX_SHOP_NAV_FETCH, true)) {
                        NavPrefetchShopFetchManager.getInstance().processDataPrefetchInNavAsync(uri2);
                    }
                } else if (addScheme2.getBooleanQueryParameter(Constants.WX_MULTIPLE, false)) {
                    intent.addCategory(Constants.WEEX_MULTIPLE_CATEGORY);
                } else {
                    intent.addCategory(Constants.WEEX_CATEGORY);
                    WXLogUtils.d(TAG, "WEEX add Weex Category");
                }
                TBWXSDKEngine.initSDKEngine(false);
                intent.putExtra("ActivityName", "WXActivity:" + bundleUri.buildUpon().clearQuery().scheme("").toString().replaceFirst("^(/|://|:/|//)", ""));
                if (!addScheme2.getBooleanQueryParameter("_wx_virtual", false)) {
                    intent.setData(addScheme2.buildUpon().authority("h5.m.taobao.com").path("/weex/viewpage.htm").appendQueryParameter(WEEX_ORIGINAL_URL, addScheme2.toString()).build());
                    intent.putExtra(WEEX_ORIGINAL_URL, addScheme2.toString());
                } else {
                    processRedimIntent(intent, addScheme2);
                }
                WXLogUtils.d(TAG, "WEEX intent:" + intent.toString());
                WXLogUtils.d(TAG, "WEEX uri:" + addScheme2.toString());
                WXLogUtils.d(TAG, "WEEX Normal Weex intent Extra:" + intent.getStringExtra(Constants.WEEX_BUNDLE_URL));
            }
            return true;
        }
        WXLogUtils.d(TAG, "WEEX after TBSREngine.matchedUrl and Put BundleUrl:" + matchedUrl);
        intent.putExtra(Constants.WEEX_BUNDLE_URL, matchedUrl);
        intent.putExtra(Constants.WEEX_URL, matchedUrl);
        intent.setData(data.buildUpon().authority("h5.m.taobao.com").path("/weex/viewpage.htm").appendQueryParameter(WEEX_ORIGINAL_URL, data.toString()).build());
        intent.putExtra(WEEX_ORIGINAL_URL, data.toString());
        Uri parse = Uri.parse(matchedUrl);
        if (!parse.isHierarchical() || !parse.getBooleanQueryParameter(Constants.WX_SHOP_RENDER_ACTIVITY, false)) {
            intent.addCategory(Constants.WEEX_CATEGORY);
        } else {
            intent.addCategory(Constants.WEEX_SHOP_RENDER_CATEGORY);
            if (parse.getBooleanQueryParameter(Constants.WX_SHOP_NAV_FETCH, true)) {
                NavPrefetchShopFetchManager.getInstance().processDataPrefetchInNavAsync(matchedUrl);
            }
        }
        intent.putExtra("ActivityName", "ShopRenderActivity");
        WXLogUtils.d(TAG, "WEEX Shop Weex intent Extra:" + intent.getStringExtra(Constants.WEEX_BUNDLE_URL));
        TBWXSDKEngine.initSDKEngine(false);
        return true;
    }

    private void preInitWeexInstance(String str, Uri uri) {
        if (uri != null && str != null) {
            Log.e("test->", "try strart preinit for " + str);
            WXPreLoadManager.getInstance().preInit(uri, str);
        }
    }

    private void processRedimIntent(Intent intent, Uri uri) {
        intent.removeCategory(Constants.WEEX_CATEGORY);
        intent.setData(uri.buildUpon().authority("tb.cn").path("/n/redim").appendQueryParameter(WEEX_ORIGINAL_URL, uri.toString()).build());
    }

    /* access modifiers changed from: package-private */
    public void reportExpURL(String str, String str2) {
        if (!TextUtils.isEmpty(str) && str.indexOf("h5.m.taobao.com/weex/viewpage.htm") != -1) {
            try {
                WXLogUtils.d(TAG, "degrade:" + TBWXConfigManger.getInstance().isDegrade());
                HashMap hashMap = new HashMap();
                hashMap.put("degradeToWindVaneUrl", str);
                AppMonitor.Alarm.commitFail("weex", "WeexErrorUrl", JSON.toJSONString(hashMap), str2, "url match h5.m.taobao.com/weex/viewpage.htm");
                WXLogUtils.d(TAG, "degrade: url [" + str + "] match h5.m.taobao.com/weex/viewpage.htm");
            } catch (Throwable th) {
                Log.e(TAG, "degrade: url [" + str + "] match h5.m.taobao.com/weex/viewpage.htm error", th);
            }
        }
    }

    public static String getOriginalUrl(String str) {
        if (URL_CACHE != null) {
            return URL_CACHE.get(str);
        }
        return null;
    }
}
