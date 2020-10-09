package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.LayoutCallback;

public class AutoScaleFeature extends AbsFeature<TextView> implements LayoutCallback {
    private float minTextSize = 10.0f;

    public void beforeOnLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        if (context != null && attributeSet != null && (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AutoScaleFeature)) != null) {
            this.minTextSize = obtainStyledAttributes.getDimension(R.styleable.AutoScaleFeature_uik_minTextSize, 10.0f);
            obtainStyledAttributes.recycle();
        }
    }

    public void setMinTextSize(float f) {
        this.minTextSize = f;
        ((TextView) getHost()).requestLayout();
    }

    public void afterOnLayout(boolean z, int i, int i2, int i3, int i4) {
        int width = (((TextView) getHost()).getWidth() - ((TextView) getHost()).getPaddingLeft()) - ((TextView) getHost()).getPaddingRight();
        String charSequence = ((TextView) getHost()).getText().toString();
        if (width > 0 && !TextUtils.isEmpty(charSequence)) {
            Paint paint = new Paint(((TextView) getHost()).getPaint());
            paint.setTextSize(((TextView) getHost()).getTextSize());
            float textSize = ((TextView) getHost()).getTextSize();
            while (textSize > this.minTextSize && paint.measureText(charSequence) > ((float) width)) {
                textSize -= 1.0f;
                paint.setTextSize(textSize);
            }
            ((TextView) getHost()).setTextSize(0, textSize);
        }
    }
}
