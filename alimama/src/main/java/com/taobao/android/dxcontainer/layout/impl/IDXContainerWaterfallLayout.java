package com.taobao.android.dxcontainer.layout.impl;

import android.content.Context;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import com.taobao.android.dxcontainer.layout.DXContainerLayoutStyleUtils;
import com.taobao.android.dxcontainer.layout.IDXContainerLayout;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper;

public class IDXContainerWaterfallLayout extends IDXContainerLayout {
    public String getLayoutType() {
        return "waterfall";
    }

    public LayoutHelper onCreateDXCLayout() {
        return new StaggeredGridLayoutHelper();
    }

    public void bindLayoutStyle(Context context, LayoutHelper layoutHelper, JSONObject jSONObject) {
        if (layoutHelper instanceof StaggeredGridLayoutHelper) {
            StaggeredGridLayoutHelper staggeredGridLayoutHelper = (StaggeredGridLayoutHelper) layoutHelper;
            int i = 0;
            if (jSONObject != null) {
                int intValue = jSONObject.getIntValue("column");
                String string = jSONObject.getString("vGap");
                String string2 = jSONObject.getString("hGap");
                staggeredGridLayoutHelper.setVGap(DXScreenTool.getPx(context, string, 0));
                staggeredGridLayoutHelper.setHGap(DXScreenTool.getPx(context, string2, 0));
                DXContainerLayoutStyleUtils.bindMarginLayoutStyle(context, staggeredGridLayoutHelper, jSONObject);
                i = intValue;
            }
            if (i <= 0) {
                i = 2;
            }
            staggeredGridLayoutHelper.setLane(i);
        }
    }
}
