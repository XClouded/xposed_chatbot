package com.alimama.union.app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.alimama.moon.R;

public class AspectRatioImageView extends AppCompatImageView {
    private float mAspectRatio = -1.0f;

    public AspectRatioImageView(Context context) {
        super(context);
        initViews(context, (AttributeSet) null);
    }

    public AspectRatioImageView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews(context, attributeSet);
    }

    public AspectRatioImageView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context, attributeSet);
    }

    private void initViews(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AspectRatioImageView);
            this.mAspectRatio = obtainStyledAttributes.getFloat(0, -1.0f);
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mAspectRatio >= 0.0f) {
            int measuredWidth = getMeasuredWidth();
            setMeasuredDimension(measuredWidth, (int) (((float) measuredWidth) * this.mAspectRatio));
        }
    }
}
