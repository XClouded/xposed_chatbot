package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.view.ViewHelper;

public class RatioFeature extends AbsFeature<View> implements MeasureCallback {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static float sDefaultRatio = 0.5f;
    private int mOrientation = 0;
    private float mRatio = sDefaultRatio;

    public void beforeOnMeasure(int i, int i2) {
    }

    public void setHost(View view) {
        super.setHost(view);
        view.requestLayout();
    }

    public void afterOnMeasure(int i, int i2) {
        int i3;
        if (this.mRatio > 0.0f) {
            boolean z = true;
            int i4 = 0;
            if (this.mOrientation == 0) {
                i4 = View.MeasureSpec.getSize(i);
                i3 = (int) (((float) i4) * this.mRatio);
            } else if (this.mOrientation == 1) {
                int size = View.MeasureSpec.getSize(i2);
                int i5 = size;
                i4 = (int) (((float) size) * this.mRatio);
                i3 = i5;
            } else {
                i3 = 0;
                z = false;
            }
            if (z && (getHost() instanceof ViewHelper)) {
                ((ViewHelper) getHost()).setMeasuredDimension((long) i4, (long) i3);
            }
        }
    }

    public void setRatio(float f) {
        if (f > 0.0f && this.mRatio != f) {
            this.mRatio = f;
            if (getHost() != null) {
                getHost().requestLayout();
            }
        }
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        if (attributeSet != null && (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RatioFeature)) != null) {
            this.mRatio = obtainStyledAttributes.getFloat(R.styleable.RatioFeature_uik_ratio, sDefaultRatio);
            this.mOrientation = obtainStyledAttributes.getInt(R.styleable.RatioFeature_uik_orientation, 0);
            obtainStyledAttributes.recycle();
        }
    }
}
