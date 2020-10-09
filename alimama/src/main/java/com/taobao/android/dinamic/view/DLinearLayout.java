package com.taobao.android.dinamic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamic.DinamicViewUtils;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.DAttrUtils;
import java.util.Map;

public class DLinearLayout extends LinearLayout {
    public DLinearLayout(Context context) {
        super(context);
    }

    public DLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public LinearLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        LinearLayout.LayoutParams layoutParams;
        Map<String, Object> map = Dinamic.getViewConstructor(DinamicConstant.D_LINEAR_LAYOUT).handleAttributeSet(attributeSet).fixedProperty;
        int[] viewSizeAndMargin = DAttrUtils.getViewSizeAndMargin(getContext(), map);
        if (map.containsKey(DAttrConstant.LL_WEIGHT)) {
            try {
                float parseFloat = Float.parseFloat((String) map.get(DAttrConstant.LL_WEIGHT));
                if (parseFloat <= 0.0f) {
                    parseFloat = 0.0f;
                }
                if (getOrientation() == 0) {
                    layoutParams = new LinearLayout.LayoutParams(0, viewSizeAndMargin[1], parseFloat);
                } else {
                    layoutParams = new LinearLayout.LayoutParams(viewSizeAndMargin[0], 0, parseFloat);
                }
            } catch (NumberFormatException unused) {
                layoutParams = new LinearLayout.LayoutParams(viewSizeAndMargin[0], viewSizeAndMargin[1]);
            }
        } else {
            layoutParams = new LinearLayout.LayoutParams(viewSizeAndMargin[0], viewSizeAndMargin[1]);
        }
        layoutParams.setMargins(viewSizeAndMargin[2], viewSizeAndMargin[3], viewSizeAndMargin[4], viewSizeAndMargin[5]);
        layoutParams.gravity = DAttrUtils.getLayoutGravity(map);
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        DinamicViewUtils.clipCorner(this, canvas);
        super.dispatchDraw(canvas);
    }

    public void draw(Canvas canvas) {
        DinamicViewUtils.clipCorner(this, canvas);
        super.draw(canvas);
    }
}
