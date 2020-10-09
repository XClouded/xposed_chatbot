package com.taobao.android.dxcontainer.layout;

import android.content.Context;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;

public abstract class IDXContainerLayout {
    public abstract void bindLayoutStyle(Context context, LayoutHelper layoutHelper, JSONObject jSONObject);

    public abstract String getLayoutType();

    public String getRenderType() {
        return null;
    }

    public boolean isRealView() {
        return false;
    }

    public abstract LayoutHelper onCreateDXCLayout();

    public LayoutHelper createDXCLayout(Context context, JSONObject jSONObject) {
        LayoutHelper onCreateDXCLayout = onCreateDXCLayout();
        bindLayoutStyle(context, onCreateDXCLayout, jSONObject);
        return onCreateDXCLayout;
    }
}
