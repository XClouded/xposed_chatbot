package com.taobao.android.dinamic.constructor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.ScreenTool;
import com.taobao.android.dinamic.view.DFrameLayout;
import java.util.ArrayList;
import java.util.Map;

public class DFrameLayoutConstructor extends DinamicViewAdvancedConstructor {
    public View initializeView(String str, Context context, AttributeSet attributeSet) {
        return new DFrameLayout(context, attributeSet);
    }

    public void setAttributes(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        super.setAttributes(view, map, arrayList, dinamicParams);
        if (arrayList.contains(DAttrConstant.VIEW_CLIP_TOP_LEFT_RADIUS) || arrayList.contains(DAttrConstant.VIEW_CLIP_TOP_RIGHT_RADIUS) || arrayList.contains(DAttrConstant.VIEW_CLIP_BOTTOM_LEFT_RADIUS) || arrayList.contains(DAttrConstant.VIEW_CLIP_BOTTOM_RIGHT_RADIUS)) {
            int px = ScreenTool.getPx(view.getContext(), map.get(DAttrConstant.VIEW_CLIP_TOP_LEFT_RADIUS), 0);
            int px2 = ScreenTool.getPx(view.getContext(), map.get(DAttrConstant.VIEW_CLIP_TOP_RIGHT_RADIUS), 0);
            int px3 = ScreenTool.getPx(view.getContext(), map.get(DAttrConstant.VIEW_CLIP_BOTTOM_LEFT_RADIUS), 0);
            float f = (float) px;
            float f2 = (float) px2;
            float px4 = (float) ScreenTool.getPx(view.getContext(), map.get(DAttrConstant.VIEW_CLIP_BOTTOM_RIGHT_RADIUS), 0);
            float f3 = (float) px3;
            view.setTag(DinamicTagKey.LAYOUT_RADII, new float[]{f, f, f2, f2, px4, px4, f3, f3});
        }
    }
}
