package com.alibaba.aliweex.adapter;

import android.view.Menu;
import com.alibaba.aliweex.WXError;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;

public abstract class INavigationBarModuleAdapter {

    public interface OnItemClickListener {
        void onClick(int i);
    }

    public abstract WXError getHeight(WXSDKInstance wXSDKInstance);

    public abstract WXError getStatusBarHeight(WXSDKInstance wXSDKInstance);

    public abstract WXError hasMenu(WXSDKInstance wXSDKInstance, JSONObject jSONObject);

    public abstract WXError hide(WXSDKInstance wXSDKInstance, JSONObject jSONObject);

    public abstract boolean onCreateOptionsMenu(WXSDKInstance wXSDKInstance, Menu menu);

    public abstract WXError setBadgeStyle(WXSDKInstance wXSDKInstance, JSONObject jSONObject);

    public abstract WXError setLeftItem(WXSDKInstance wXSDKInstance, JSONObject jSONObject, OnItemClickListener onItemClickListener);

    public abstract WXError setMoreItem(WXSDKInstance wXSDKInstance, JSONObject jSONObject, OnItemClickListener onItemClickListener);

    public abstract WXError setRightItem(WXSDKInstance wXSDKInstance, JSONObject jSONObject, OnItemClickListener onItemClickListener);

    public abstract WXError setStyle(WXSDKInstance wXSDKInstance, JSONObject jSONObject);

    public abstract WXError setTitle(WXSDKInstance wXSDKInstance, JSONObject jSONObject);

    public abstract WXError setTransparent(WXSDKInstance wXSDKInstance, JSONObject jSONObject);

    public abstract WXError show(WXSDKInstance wXSDKInstance, JSONObject jSONObject);

    public WXError showMenu(WXSDKInstance wXSDKInstance, JSONObject jSONObject) {
        WXError wXError = new WXError();
        wXError.result = "WX_NOT_SUPPORTED";
        wXError.message = "Only Taobao app support showMenu(), check implement in TBNavBarAdapter";
        return wXError;
    }
}
