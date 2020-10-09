package com.taobao.android.dxcontainer.layout.impl;

import android.content.Context;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import com.taobao.android.dxcontainer.layout.IDXContainerLayout;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.StickyLayoutHelper;

public class IDXContainerStickyLayout extends IDXContainerLayout {
    public String getLayoutType() {
        return "sticky";
    }

    public LayoutHelper onCreateDXCLayout() {
        return new StickyLayoutHelper();
    }

    public void bindLayoutStyle(Context context, LayoutHelper layoutHelper, JSONObject jSONObject) {
        if ((layoutHelper instanceof StickyLayoutHelper) && jSONObject != null) {
            ((StickyLayoutHelper) layoutHelper).setOffset(DXScreenTool.getPx(context, jSONObject.getString("offset"), 0));
        }
    }
}
