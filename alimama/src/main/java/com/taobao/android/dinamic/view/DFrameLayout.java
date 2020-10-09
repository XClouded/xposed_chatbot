package com.taobao.android.dinamic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamic.DinamicViewUtils;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.DAttrUtils;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import java.util.Map;

public class DFrameLayout extends FrameLayout {
    private int calculateSize(String str, String str2, int i) {
        return i;
    }

    public DFrameLayout(Context context) {
        super(context);
    }

    public DFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        Map<String, Object> map = Dinamic.getViewConstructor(DinamicConstant.D_FRAME_LAYOUT).handleAttributeSet(attributeSet).fixedProperty;
        int layoutGravity = DAttrUtils.getLayoutGravity(map);
        String str = (String) map.get(DAttrConstant.VIEW_WIDTH);
        String str2 = (String) map.get(DAttrConstant.VIEW_HEIGHT);
        int[] viewSizeAndMargin = DAttrUtils.getViewSizeAndMargin(getContext(), map);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(viewSizeAndMargin[0], viewSizeAndMargin[1]);
        layoutParams.gravity = layoutGravity;
        layoutParams.setMargins(viewSizeAndMargin[2], viewSizeAndMargin[3], viewSizeAndMargin[4], viewSizeAndMargin[5]);
        if (layoutGravity == -1) {
            layoutGravity = 51;
        }
        if ((layoutGravity & 3) != 0 && viewSizeAndMargin[0] > 0) {
            String str3 = (String) map.get(DAttrConstant.VIEW_MARGIN_LEFT);
            if (!TextUtils.isEmpty(str3) && !str.toLowerCase().contains(DXBindingXConstant.NP) && !str3.toLowerCase().contains(DXBindingXConstant.NP)) {
                layoutParams.width = calculateSize(str, str3, viewSizeAndMargin[0]);
            }
        }
        if ((layoutGravity & 5) != 0 && viewSizeAndMargin[0] > 0) {
            String str4 = (String) map.get(DAttrConstant.VIEW_MARGIN_RIGHT);
            if (!TextUtils.isEmpty(str4) && !str.toLowerCase().contains(DXBindingXConstant.NP) && !str4.toLowerCase().contains(DXBindingXConstant.NP)) {
                layoutParams.width = calculateSize(str, str4, viewSizeAndMargin[0]);
            }
        }
        if ((layoutGravity & 48) != 0 && viewSizeAndMargin[1] > 0) {
            String str5 = (String) map.get(DAttrConstant.VIEW_MARGIN_TOP);
            if (!TextUtils.isEmpty(str5) && !str2.toLowerCase().contains(DXBindingXConstant.NP) && !str5.toLowerCase().contains(DXBindingXConstant.NP)) {
                layoutParams.height = calculateSize(str2, str5, viewSizeAndMargin[1]);
            }
        }
        if ((layoutGravity & 80) != 0 && viewSizeAndMargin[1] > 0) {
            String str6 = (String) map.get(DAttrConstant.VIEW_MARGIN_BOTTOM);
            if (!TextUtils.isEmpty(str6) && !str2.toLowerCase().contains(DXBindingXConstant.NP) && !str6.toLowerCase().contains(DXBindingXConstant.NP)) {
                layoutParams.height = calculateSize(str2, str6, viewSizeAndMargin[1]);
            }
        }
        return layoutParams;
    }

    public void draw(Canvas canvas) {
        DinamicViewUtils.clipCorner(this, canvas);
        super.draw(canvas);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        DinamicViewUtils.clipCorner(this, canvas);
        super.dispatchDraw(canvas);
    }
}
