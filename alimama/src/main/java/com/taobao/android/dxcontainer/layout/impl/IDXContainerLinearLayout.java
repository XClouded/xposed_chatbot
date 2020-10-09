package com.taobao.android.dxcontainer.layout.impl;

import android.content.Context;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import com.taobao.android.dxcontainer.layout.DXContainerLayoutStyleUtils;
import com.taobao.android.dxcontainer.layout.IDXContainerLayout;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper;

public class IDXContainerLinearLayout extends IDXContainerLayout {
    public String getLayoutType() {
        return "linear";
    }

    public LayoutHelper onCreateDXCLayout() {
        return new RangeGridLayoutHelper(1);
    }

    public void bindLayoutStyle(Context context, LayoutHelper layoutHelper, JSONObject jSONObject) {
        if ((layoutHelper instanceof RangeGridLayoutHelper) && jSONObject != null) {
            RangeGridLayoutHelper rangeGridLayoutHelper = (RangeGridLayoutHelper) layoutHelper;
            rangeGridLayoutHelper.setVGap(DXScreenTool.getPx(context, jSONObject.getString("dividerHeight"), 0));
            DXContainerLayoutStyleUtils.bindMarginLayoutStyle(context, rangeGridLayoutHelper, jSONObject);
        }
    }
}
