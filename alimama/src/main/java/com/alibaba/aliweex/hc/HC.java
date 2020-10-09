package com.alibaba.aliweex.hc;

import android.content.Intent;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.hc.cache.WXAsyncRenderModule;
import com.alibaba.aliweex.hc.cache.WXAsyncRequireModule;
import com.alibaba.aliweex.hc.component.HCWXTabbar;
import com.alibaba.aliweex.hc.component.HCWXWVWeb;
import com.alibaba.aliweex.hc.component.WXBubbleComponent;
import com.alibaba.aliweex.hc.module.WXHCModule;
import com.taobao.pha.core.rescache.PackageCache;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXLogUtils;

public class HC {
    private static HC sInstance;
    private IHCModuleAdapter mHCModuleAdapter;

    public void beforeNavTo(Intent intent) {
    }

    public static HC getInstance() {
        if (sInstance == null) {
            synchronized (HC.class) {
                if (sInstance == null) {
                    sInstance = new HC();
                }
            }
        }
        return sInstance;
    }

    public void init(IHCModuleAdapter iHCModuleAdapter) {
        try {
            this.mHCModuleAdapter = iHCModuleAdapter;
            WXSDKEngine.registerModule("hc", WXHCModule.class);
            WXSDKEngine.registerComponent("tabbar", (Class<? extends WXComponent>) HCWXTabbar.class);
            WXSDKEngine.registerComponent("bubble", (Class<? extends WXComponent>) WXBubbleComponent.class);
            WXSDKEngine.registerComponent("web", (Class<? extends WXComponent>) HCWXWVWeb.class);
            WXSDKEngine.registerModule("asyncRequire", WXAsyncRequireModule.class);
            WXSDKEngine.registerModule("asyncRender", WXAsyncRenderModule.class);
            PackageCache.getInstance().init(AliWeex.getInstance().getContext());
        } catch (WXException e) {
            WXLogUtils.e("[HC init] registerModulesAndComponents:" + e.getCause());
        }
    }

    public IHCModuleAdapter getHCModuleAdapter() {
        return this.mHCModuleAdapter;
    }
}
