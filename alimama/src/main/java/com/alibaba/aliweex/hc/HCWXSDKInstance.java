package com.alibaba.aliweex.hc;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.aliweex.AliWXSDKInstance;
import com.alibaba.aliweex.adapter.component.WXWVWeb;
import com.alibaba.aliweex.hc.cache.WXAsyncRender;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import java.util.HashMap;
import java.util.Map;

public class HCWXSDKInstance extends AliWXSDKInstance {
    private HashMap<String, Object> mAsyncOpts = WXAsyncRender.getInstance().asyncOpts(false);
    private String[] mAsyncOptsWhiteList = WXAsyncRender.getInstance().asyncOptsWhiteList(false);
    private Map<String, WXWVWeb> mWVWebMap = new HashMap();

    public HCWXSDKInstance(Context context, String str) {
        super(context, str);
    }

    public void renderByUrl(String str, String str2, Map<String, Object> map, String str3, WXRenderStrategy wXRenderStrategy) {
        startAsyncData(str2);
        super.renderByUrl(str, str2, addHCRenderOpts(map), str3, wXRenderStrategy);
    }

    /* access modifiers changed from: protected */
    public WXSDKInstance newNestedInstance() {
        HCWXSDKInstance hCWXSDKInstance = new HCWXSDKInstance(getContext(), this.mFtag);
        hCWXSDKInstance.setWXNavBarAdapter(getWXNavBarAdapter());
        return hCWXSDKInstance;
    }

    private Map<String, Object> addHCRenderOpts(Map<String, Object> map) {
        if (this.mAsyncOpts != null) {
            if (map == null) {
                map = new HashMap<>();
            }
            map.putAll(this.mAsyncOpts);
        }
        return map;
    }

    private void startAsyncData(String str) {
        if (WXAsyncRender.getInstance().enableRequestAsyncData(this.mAsyncOpts, this.mAsyncOptsWhiteList, str)) {
            if (WXEnvironment.isApkDebugable()) {
                String queryParameter = Uri.parse(str).getQueryParameter("mock_url");
                if (!TextUtils.isEmpty(queryParameter)) {
                    str = queryParameter;
                }
            }
            WXAsyncRender.getInstance().requestAsyncDataForWeex(getContext(), getInstanceId(), str);
            return;
        }
        WXAsyncRender.getInstance().fireFail(getInstanceId());
    }

    public WXWVWeb getWXWVWeb(String str) {
        return this.mWVWebMap.get(str);
    }

    public void putWXWVWeb(String str, WXWVWeb wXWVWeb) {
        this.mWVWebMap.put(str, wXWVWeb);
    }
}
