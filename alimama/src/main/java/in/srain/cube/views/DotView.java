package in.srain.cube.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.alimama.unionwl.uiframe.R;
import in.srain.cube.views.banner.PagerIndicator;

public class DotView extends LinearLayout implements PagerIndicator {
    private int mCurrent = 0;
    private View.OnClickListener mDotClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            if ((view instanceof LittleDot) && DotView.this.mOnDotClickHandler != null) {
                DotView.this.mOnDotClickHandler.onDotClick(((LittleDot) view).getIndex());
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
    private int mSelectedColor = -13141010;
    private int mTotal = 0;
    private int mUnSelectedColor = -3813669;

    public interface OnDotClickHandler {
        void onDotClick(int i);
    }

    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setGravity(1);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DotView, 0, 0);
        if (obtainStyledAttributes != null) {
            if (obtainStyledAttributes.hasValue(R.styleable.DotView_dot_radius)) {
                this.mDotRadius = obtainStyledAttributes.getDimension(R.styleable.DotView_dot_radius, this.mDotRadius);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.DotView_dot_span)) {
                this.mDotSpan = (int) obtainStyledAttributes.getDimension(R.styleable.DotView_dot_span, (float) this.mDotSpan);
            }
            this.mSelectedColor = obtainStyledAttributes.getColor(R.styleable.DotView_dot_selected_color, this.mSelectedColor);
            this.mUnSelectedColor = obtainStyledAttributes.getColor(R.styleable.DotView_dot_unselected_color, this.mUnSelectedColor);
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
        if (i < getChildCount() && i >= 0 && this.mCurrent != i) {
            if (this.mCurrent < getChildCount() && this.mCurrent >= 0) {
                ((LittleDot) getChildAt(this.mCurrent)).setColor(this.mUnSelectedColor);
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
            canvas.drawCircle((float) (DotView.this.mLittleDotSize / 2), DotView.this.mDotRadius, DotView.this.mDotRadius, this.mPaint);
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
