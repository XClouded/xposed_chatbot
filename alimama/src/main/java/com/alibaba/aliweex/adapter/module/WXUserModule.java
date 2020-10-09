package com.alibaba.aliweex.adapter.module;

import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IUserModuleAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class WXUserModule extends WXModule {
    @JSMethod
    public void getUserInfo(JSCallback jSCallback) {
        IUserModuleAdapter userModuleAdapter = AliWeex.getInstance().getUserModuleAdapter();
        if (userModuleAdapter != null) {
            userModuleAdapter.getUserInfo(this.mWXSDKInstance.getContext(), jSCallback);
        }
    }

    @JSMethod
    public void login(JSCallback jSCallback) {
        IUserModuleAdapter userModuleAdapter = AliWeex.getInstance().getUserModuleAdapter();
        if (userModuleAdapter != null) {
            userModuleAdapter.login(this.mWXSDKInstance.getContext(), jSCallback);
        }
    }

    @JSMethod
    public void logout(JSCallback jSCallback) {
        IUserModuleAdapter userModuleAdapter = AliWeex.getInstance().getUserModuleAdapter();
        if (userModuleAdapter != null) {
            userModuleAdapter.logout(this.mWXSDKInstance.getContext(), jSCallback);
        }
    }
}
