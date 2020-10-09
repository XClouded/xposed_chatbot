package com.alibaba.aliweex.adapter.module;

import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IPageInfoModuleAdapter;
import com.alibaba.aliweex.adapter.ITBPageInfoModuleAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class WXPageInfoModule extends WXModule {
    @JSMethod
    public void setTitle(String str) {
        IPageInfoModuleAdapter pageInfoModuleAdapter = AliWeex.getInstance().getPageInfoModuleAdapter();
        if (pageInfoModuleAdapter instanceof ITBPageInfoModuleAdapter) {
            ((ITBPageInfoModuleAdapter) pageInfoModuleAdapter).setInstanceId(this.mWXSDKInstance.getInstanceId());
        }
        if (pageInfoModuleAdapter != null) {
            pageInfoModuleAdapter.setTitle(this.mWXSDKInstance.getContext(), str);
        }
    }

    @JSMethod
    public void setIcon(String str) {
        IPageInfoModuleAdapter pageInfoModuleAdapter = AliWeex.getInstance().getPageInfoModuleAdapter();
        if (pageInfoModuleAdapter instanceof ITBPageInfoModuleAdapter) {
            ((ITBPageInfoModuleAdapter) pageInfoModuleAdapter).setInstanceId(this.mWXSDKInstance.getInstanceId());
        }
        if (pageInfoModuleAdapter != null) {
            pageInfoModuleAdapter.setIcon(this.mWXSDKInstance.getContext(), str);
        }
    }
}
