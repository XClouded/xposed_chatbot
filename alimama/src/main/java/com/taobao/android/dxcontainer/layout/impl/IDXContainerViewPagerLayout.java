package com.taobao.android.dxcontainer.layout.impl;

import android.content.Context;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dxcontainer.layout.DXContainerLayoutConstant;
import com.taobao.android.dxcontainer.layout.DXContainerLayoutStyleUtils;
import com.taobao.android.dxcontainer.layout.IDXContainerLayout;
import com.taobao.android.dxcontainer.render.TabContentRender;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper;

public class IDXContainerViewPagerLayout extends IDXContainerLayout {
    public String getLayoutType() {
        return DXContainerLayoutConstant.DXC_LAYOUT_TABCONTENT;
    }

    public String getRenderType() {
        return TabContentRender.RENDER_TYPE;
    }

    public boolean isRealView() {
        return true;
    }

    public LayoutHelper onCreateDXCLayout() {
        return new RangeGridLayoutHelper(1);
    }

    public void bindLayoutStyle(Context context, LayoutHelper layoutHelper, JSONObject jSONObject) {
        if ((layoutHelper instanceof RangeGridLayoutHelper) && jSONObject != null) {
            RangeGridLayoutHelper rangeGridLayoutHelper = (RangeGridLayoutHelper) layoutHelper;
            rangeGridLayoutHelper.setItemCount(1);
            DXContainerLayoutStyleUtils.bindMarginLayoutStyle(context, rangeGridLayoutHelper, jSONObject);
        }
    }
}
