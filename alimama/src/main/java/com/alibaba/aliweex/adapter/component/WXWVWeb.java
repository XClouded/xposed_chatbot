package com.alibaba.aliweex.adapter.component;

import android.content.Intent;
import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.WXWeb;

public class WXWVWeb extends WXWeb {
    WXSDKInstance.OnActivityResultHandler handler = null;

    public WXWVWeb(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        final String valueOf = String.valueOf(hashCode());
        this.handler = new WXSDKInstance.OnActivityResultHandler(valueOf) {
            public boolean onActivityResult(int i, int i2, Intent intent, String str) {
                if (!TextUtils.equals(str, valueOf) || !(WXWVWeb.this.mWebView instanceof WXWVWebView)) {
                    return super.onActivityResult(i, i2, intent);
                }
                ((WXWVWebView) WXWVWeb.this.mWebView).onActivityResult(i, i2, intent);
                return true;
            }
        };
        wXSDKInstance.registerOnActivityResultHandler(this.handler);
    }

    public void destroy() {
        if (!(this.handler == null || getInstance() == null)) {
            getInstance().unRegisterOnActivityResultHandler(this.handler);
        }
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void createViewImpl() {
        super.createViewImpl();
    }

    /* access modifiers changed from: protected */
    public void createWebView() {
        this.mWebView = new WXWVWebView(getInstance(), this);
    }
}
