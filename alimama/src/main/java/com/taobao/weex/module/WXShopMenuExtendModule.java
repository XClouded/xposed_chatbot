package com.taobao.weex.module;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alipay.sdk.util.e;
import com.taobao.uikit.actionbar.TBPublicMenu;
import com.taobao.uikit.actionbar.TBPublicMenuItem;
import com.taobao.weex.WXActivity;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.uc.webview.export.extension.UCCore;
import java.util.ArrayList;

public class WXShopMenuExtendModule extends WXModule {
    @JSMethod
    public void setMenuRecentShops(String str, String str2, JSCallback jSCallback) {
        try {
            Context context = this.mWXSDKInstance.getContext();
            if (context instanceof WXActivity) {
                TBPublicMenu publicMenu = ((WXActivity) context).getPublicMenu();
                publicMenu.needNewMenu(true);
                if (!TextUtils.isEmpty(str)) {
                    publicMenu.setExtensionTitle(str);
                }
                if (!TextUtils.isEmpty(str2)) {
                    publicMenu.setExtensionMenu((ArrayList) JSON.parseArray(str2, TBPublicMenuItem.class));
                }
                jSCallback.invoke("success");
                return;
            }
            jSCallback.invoke(e.a);
        } catch (Exception unused) {
            jSCallback.invoke(UCCore.EVENT_EXCEPTION);
        }
    }
}
