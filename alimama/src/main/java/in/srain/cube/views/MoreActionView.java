package in.srain.cube.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.View;
import com.alimama.unionwl.uiframe.R;

public class MoreActionView extends View {
    private int mColor = -1;
    private float mDotRadius = 3.0f;
    private float mDotSpan = 5.0f;
    private Paint mPaint;

    public MoreActionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.MoreActionView, 0, 0);
        if (obtainStyledAttributes != null) {
            if (obtainStyledAttributes.hasValue(R.styleable.MoreActionView_more_action_dot_radius)) {
                this.mDotRadius = obtainStyledAttributes.getDimension(R.styleable.MoreActionView_more_action_dot_radius, this.mDotRadius);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.MoreActionView_more_action_dot_span)) {
                this.mDotSpan = (float) ((int) obtainStyledAttributes.getDimension(R.styleable.MoreActionView_more_action_dot_span, this.mDotSpan));
            }
            this.mColor = obtainStyledAttributes.getColor(R.styleable.MoreActionView_more_action_dot_color, this.mColor);
            obtainStyledAttributes.recycle();
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(this.mColor);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3 = (int) (this.mDotRadius * 2.0f);
        int i4 = (int) ((this.mDotRadius * 6.0f) + (this.mDotSpan * 2.0f));
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        if (mode == 1073741824) {
            i4 = size;
        } else if (mode == Integer.MIN_VALUE) {
            i4 = Math.min(i4, size);
        }
        if (mode2 == 1073741824) {
            i3 = size2;
        } else if (mode2 == Integer.MIN_VALUE) {
            i3 = Math.min(i3, size2);
        }
        setMeasuredDimension(i4, i3);
    }

    public void setColor(int i) {
        this.mPaint.setColor(i);
    }

    public void setColorFilter(int i) {
        setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
    }

    public final void setColorFilter(int i, PorterDuff.Mode mode) {
        this.mPaint.setColorFilter(new PorterDuffColorFilter(i, mode));
        invalidate();
    }

    public final void clearColorFilter() {
        this.mPaint.setColorFilter((ColorFilter) null);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            canvas.drawCircle((((float) i) * ((this.mDotRadius * 2.0f) + this.mDotSpan)) + this.mDotRadius, this.mDotRadius, this.mDotRadius, this.mPaint);
        }
    }
}
