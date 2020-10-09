package com.alimama.moon.features.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.alimama.moon.R;
import in.srain.cube.views.banner.PagerIndicator;

public class HomeBannerDotView extends LinearLayout implements PagerIndicator {
    private int mCurrent = 0;
    private View.OnClickListener mDotClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            if ((view instanceof LittleDot) && HomeBannerDotView.this.mOnDotClickHandler != null) {
                HomeBannerDotView.this.mOnDotClickHandler.onDotClick(((LittleDot) view).getIndex());
            }
        }
    };
    /* access modifiers changed from: private */
    public float mDotRadius = 6.0f;
    private int mDotSpan = 36;
    /* access modifiers changed from: private */
    public int mLittleDotSize = -2;
    /* access modifiers changed from: private */
    public OnDotClickHandler mOnDotClickHandler;
    private int mSelectedColor;
    private int mTotal = 0;
    private int mUnSelectedColor;

    public interface OnDotClickHandler {
        void onDotClick(int i);
    }

    public HomeBannerDotView(Context context) {
        super(context);
    }

    public HomeBannerDotView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setGravity(1);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DotView, 0, 0);
        if (obtainStyledAttributes != null) {
            if (obtainStyledAttributes.hasValue(0)) {
                this.mDotRadius = obtainStyledAttributes.getDimension(0, this.mDotRadius);
            }
            if (obtainStyledAttributes.hasValue(1)) {
                this.mDotSpan = (int) obtainStyledAttributes.getDimension(1, (float) this.mDotSpan);
            }
            this.mSelectedColor = obtainStyledAttributes.getColor(3, ContextCompat.getColor(context, R.color.home_banner_dot_selected));
            this.mUnSelectedColor = obtainStyledAttributes.getColor(2, ContextCompat.getColor(context, R.color.home_banner_dot_unselected));
            obtainStyledAttributes.recycle();
        }
        this.mLittleDotSize = (int) (((float) (this.mDotSpan / 2)) + (this.mDotRadius * 2.0f));
    }

    public final void setNum(int i) {
        if (i >= 0) {
            this.mTotal = i;
            removeAllViews();
            setOrientation(0);
            for (int i2 = 0; i2 < i; i2++) {
                LittleDot littleDot = new LittleDot(getContext(), i2);
                if (i2 == 0) {
                    littleDot.setColor(this.mSelectedColor);
                } else {
                    littleDot.setColor(this.mUnSelectedColor);
                }
                littleDot.setLayoutParams(new LinearLayout.LayoutParams(this.mLittleDotSize, ((int) this.mDotRadius) * 2, 1.0f));
                littleDot.setClickable(true);
                littleDot.setOnClickListener(this.mDotClickHandler);
                addView(littleDot);
            }
        }
    }

    public int getTotal() {
        return this.mTotal;
    }

    public int getCurrentIndex() {
        return this.mCurrent;
    }

    public void setOnDotClickHandler(OnDotClickHandler onDotClickHandler) {
        this.mOnDotClickHandler = onDotClickHandler;
    }

    public final void setSelected(int i) {
        if (i < getChildCount() && i >= 0) {
            int childCount = getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                ((LittleDot) getChildAt(i2)).setColor(this.mUnSelectedColor);
            }
            ((LittleDot) getChildAt(i)).setColor(this.mSelectedColor);
            this.mCurrent = i;
        }
    }

    private class LittleDot extends View {
        private int mColor;
        private int mIndex;
        private Paint mPaint = new Paint();

        public LittleDot(Context context, int i) {
            super(context);
            this.mPaint.setAntiAlias(true);
            this.mIndex = i;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public void setColor(int i) {
            if (i != this.mColor) {
                this.mColor = i;
                invalidate();
            }
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.mPaint.setColor(this.mColor);
            canvas.drawCircle((float) (HomeBannerDotView.this.mLittleDotSize / 2), HomeBannerDotView.this.mDotRadius, HomeBannerDotView.this.mDotRadius, this.mPaint);
        }
    }

    public void setSelectedColor(int i) {
        if (this.mSelectedColor != i) {
            this.mSelectedColor = i;
            invalidate();
        }
    }

    public void setColor(int i, int i2) {
        if (this.mSelectedColor != i || this.mUnSelectedColor != i2) {
            this.mSelectedColor = i;
            this.mUnSelectedColor = i2;
            invalidate();
        }
    }

    public void setUnSelectedColor(int i) {
        if (this.mUnSelectedColor != i) {
            this.mSelectedColor = i;
            invalidate();
        }
    }
}
