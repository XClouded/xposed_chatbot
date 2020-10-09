package com.taobao.android.dinamic.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamic.DinamicViewCreator;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import java.util.Map;

public class DHorizontalScrollLayout extends DFrameLayout {
    String SL_LAYOUT_TYPE_FRAME = "frame";
    String SL_LAYOUT_TYPE_LINEAR = "linear";
    String SL_SCROLLBAR_INVISIBLE = DAttrConstant.VISIBILITY_INVISIBLE;
    String SL_SCROLLBAR_VISIBLE = "visible";
    ViewGroup containerView;
    FrameLayout scrollView;

    public DHorizontalScrollLayout(Context context) {
        super(context);
    }

    public DHorizontalScrollLayout(Context context, AttributeSet attributeSet, DinamicParams dinamicParams) {
        super(context, attributeSet);
        generateChildView(attributeSet, dinamicParams);
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (this.containerView != null) {
            this.containerView.addView(view, i, layoutParams);
        }
    }

    private void generateChildView(AttributeSet attributeSet, DinamicParams dinamicParams) {
        Map<String, Object> map = Dinamic.getViewConstructor(DinamicConstant.D_HORIZONTAL_SCROLL_LAYOUT).handleAttributeSet(attributeSet).fixedProperty;
        String str = (String) map.get(DAttrConstant.SL_LAYOUT_TYPE);
        this.scrollView = new HorizontalScrollView(getContext());
        this.scrollView.setOverScrollMode(2);
        this.scrollView.setVerticalScrollBarEnabled(false);
        if (!TextUtils.equals((String) map.get(DAttrConstant.SL_SCOLLBAR), this.SL_SCROLLBAR_VISIBLE)) {
            this.scrollView.setHorizontalScrollBarEnabled(false);
        }
        if (TextUtils.equals(str, this.SL_LAYOUT_TYPE_FRAME)) {
            this.containerView = (DFrameLayout) DinamicViewCreator.createView(DinamicConstant.D_FRAME_LAYOUT, getContext(), attributeSet, dinamicParams);
            this.scrollView.addView(this.containerView);
        } else {
            this.containerView = (DLinearLayout) DinamicViewCreator.createView(DinamicConstant.D_LINEAR_LAYOUT, getContext(), attributeSet, dinamicParams);
            this.scrollView.addView(this.containerView);
        }
        super.addView(this.scrollView, -1, generateLayoutParams(attributeSet));
        map.remove(DAttrConstant.SL_LAYOUT_TYPE);
        map.remove(DAttrConstant.SL_SCOLLBAR);
    }
}
