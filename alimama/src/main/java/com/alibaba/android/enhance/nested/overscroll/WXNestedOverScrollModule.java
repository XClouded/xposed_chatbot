package com.alibaba.android.enhance.nested.overscroll;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class WXNestedOverScrollModule extends WXModule {
    @JSMethod(uiThread = false)
    public boolean setOverScrollEnabled(boolean z) {
        WXNestedOverScrollHelper.setOverScroll(this.mWXSDKInstance, z);
        return true;
    }
}
