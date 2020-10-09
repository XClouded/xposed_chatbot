package com.alimama.moon.features.reports.withdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import androidx.core.content.ContextCompat;
import com.alimama.moon.R;
import com.alimama.moon.view.ClearableEditTextView;

public class RMBEditText extends ClearableEditTextView {
    private static final String RMB_PREFIX = "¥";
    private int mOriginalLeftPadding = -1;
    private TextPaint rmbPaint;

    public RMBEditText(Context context) {
        super(context);
        initViews();
    }

    public RMBEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public RMBEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {
        this.rmbPaint = new TextPaint();
        this.rmbPaint.setAntiAlias(true);
        this.rmbPaint.setFakeBoldText(true);
        this.rmbPaint.setColor(ContextCompat.getColor(getContext(), R.color.common_black_text_color));
        this.rmbPaint.setTextSize((float) getResources().getDimensionPixelOffset(R.dimen.common_text_size_14));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mOriginalLeftPadding < 0) {
            calculatePrefix();
        }
    }

    private void calculatePrefix() {
        float[] fArr = new float[RMB_PREFIX.length()];
        this.rmbPaint.getTextWidths(RMB_PREFIX, fArr);
        int i = 0;
        for (float f : fArr) {
            i = (int) (((float) i) + f);
        }
        this.mOriginalLeftPadding = getCompoundPaddingLeft();
        setPadding(i + this.mOriginalLeftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(RMB_PREFIX, 0.0f, (float) getLineBounds(0, (Rect) null), this.rmbPaint);
    }
}
