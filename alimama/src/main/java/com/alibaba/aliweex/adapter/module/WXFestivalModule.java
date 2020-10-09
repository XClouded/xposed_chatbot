package com.alibaba.aliweex.adapter.module;

import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IFestivalModuleAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.util.Map;

public class WXFestivalModule extends WXModule {
    @JSMethod(uiThread = false)
    public Map<String, String> queryFestivalStyle() {
        IFestivalModuleAdapter festivalModuleAdapter = AliWeex.getInstance().getFestivalModuleAdapter();
        if (festivalModuleAdapter != null) {
            return festivalModuleAdapter.queryFestivalStyle();
        }
        return null;
    }

    @JSMethod
    public void setFestivalStyle(String str, JSCallback jSCallback, JSCallback jSCallback2) {
        IFestivalModuleAdapter festivalModuleAdapter = AliWeex.getInstance().getFestivalModuleAdapter();
        if (festivalModuleAdapter != null) {
            festivalModuleAdapter.setFestivalStyle(this.mWXSDKInstance.getContext(), str, jSCallback, jSCallback2);
        }
    }
}
