package com.alibaba.aliweex.adapter.adapter;

import android.text.TextUtils;
import com.alibaba.aliweex.AliWXSDKEngine;
import com.alibaba.aliweex.utils.WXUtil;
import com.taobao.weex.adapter.IWXJsFileLoaderAdapter;
import com.taobao.weex.utils.WXLogUtils;

public class WXJsFileLoaderAdapter implements IWXJsFileLoaderAdapter {
    public String loadJsFramework() {
        return null;
    }

    public String loadRaxApi() {
        String zCacheFromUrl = WXUtil.getZCacheFromUrl("weex", AliWXSDKEngine.FRAMEWORK_JS_RAX_API_URL);
        if (TextUtils.isEmpty(zCacheFromUrl)) {
            WXLogUtils.e("TBWXSDKEngine", "TBWXSDKEngine: WV obtain rax_api failed");
        }
        return zCacheFromUrl;
    }

    public String loadJsFrameworkForSandBox() {
        String zCacheFromUrl = WXUtil.getZCacheFromUrl("weex", AliWXSDKEngine.FRAMEWORK_JS_URL);
        if (TextUtils.isEmpty(zCacheFromUrl)) {
            WXLogUtils.e("TBWXSDKEngine", "TBWXSDKEngine: WV obtain  FRAMEWORK_JS failed");
        }
        return zCacheFromUrl;
    }
}
