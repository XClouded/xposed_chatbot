package com.taobao.weex.ui.view.refresh.circlebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;

@SuppressLint({"AppCompatCustomView"})
public class CircleProgressBar extends ImageView {
    public static final int DEFAULT_CIRCLE_BG_LIGHT = -328966;
    public static final int DEFAULT_CIRCLE_COLOR = -1048576;
    private static final int DEFAULT_CIRCLE_DIAMETER = 40;
    private static final int FILL_SHADOW_COLOR = 1023410176;
    private static final int KEY_SHADOW_COLOR = 503316480;
    private static final int SHADOW_ELEVATION = 4;
    private static final float SHADOW_RADIUS = 3.5f;
    private static final int STROKE_WIDTH_LARGE = 3;
    private static final float X_OFFSET = 0.0f;
    private static final float Y_OFFSET = 1.75f;
    private int mArrowHeight;
    private int mArrowWidth;
    private int mBackGroundColor;
    private ShapeDrawable mBgCircle;
    private boolean mCircleBackgroundEnabled;
    private int[] mColors = {-16777216};
    private int mDiameter;
    private int mInnerRadius;
    private Animation.AnimationListener mListener;
    private int mMax;
    private int mProgress;
    private int mProgressColor;
    public MaterialProgressDrawable mProgressDrawable;
    private int mProgressStokeWidth;
    private int mShadowRadius;
    private boolean mShowArrow;

    public CircleProgressBar(Context context) {
        super(context);
        init(context, (AttributeSet) null, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        float f = getContext().getResources().getDisplayMetrics().density;
        this.mBackGroundColor = DEFAULT_CIRCLE_BG_LIGHT;
        this.mProgressColor = DEFAULT_CIRCLE_COLOR;
        this.mColors = new int[]{this.mProgressColor};
        this.mInnerRadius = -1;
        this.mProgressStokeWidth = (int) (f * 3.0f);
        this.mArrowWidth = -1;
        this.mArrowHeight = -1;
        this.mShowArrow = true;
        this.mCircleBackgroundEnabled = true;
        this.mProgress = 0;
        this.mMax = 100;
        this.mProgressDrawable = new MaterialProgressDrawable(getContext(), this);
        super.setImageDrawable(this.mProgressDrawable);
    }

    public void setProgressBackGroundColor(int i) {
        this.mBackGroundColor = i;
    }

    private boolean elevationSupported() {
        return Build.VERSION.SDK_INT >= 21;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (!elevationSupported()) {
            setMeasuredDimension(getMeasuredWidth() + (this.mShadowRadius * 2), getMeasuredHeight() + (this.mShadowRadius * 2));
        }
    }

    public int getProgressStokeWidth() {
        return this.mProgressStokeWidth;
    }

    public void setProgressStokeWidth(int i) {
        this.mProgressStokeWidth = (int) (((float) i) * getContext().getResources().getDisplayMetrics().density);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        float f = getContext().getResources().getDisplayMetrics().density;
        this.mDiameter = Math.min(getMeasuredWidth(), getMeasuredHeight());
        if (this.mDiameter <= 0) {
            this.mDiameter = ((int) f) * 40;
        }
        if (getBackground() == null && this.mCircleBackgroundEnabled) {
            int i5 = (int) (Y_OFFSET * f);
            int i6 = (int) (f * 0.0f);
            this.mShadowRadius = (int) (SHADOW_RADIUS * f);
            if (elevationSupported()) {
                if (this.mBgCircle == null) {
                    this.mBgCircle = new ShapeDrawable(new OvalShape());
                }
                ViewCompat.setElevation(this, f * 4.0f);
            } else {
                if (this.mBgCircle == null || !(this.mBgCircle.getShape() instanceof OvalShadow) || ((OvalShadow) this.mBgCircle.getShape()).mCircleDiameter != this.mDiameter - (this.mShadowRadius * 2) || ((OvalShadow) this.mBgCircle.getShape()).mShadowRadius != this.mShadowRadius) {
                    this.mBgCircle = new ShapeDrawable(new OvalShadow(this.mShadowRadius, this.mDiameter - (this.mShadowRadius * 2)));
                }
                ViewCompat.setLayerType(this, 1, this.mBgCircle.getPaint());
                this.mBgCircle.getPaint().setShadowLayer((float) this.mShadowRadius, (float) i6, (float) i5, KEY_SHADOW_COLOR);
                int i7 = this.mShadowRadius;
                setPadding(i7, i7, i7, i7);
            }
            this.mBgCircle.getPaint().setColor(this.mBackGroundColor);
            setBackgroundDrawable(this.mBgCircle);
        }
        this.mProgressDrawable.setBackgroundColor(this.mBackGroundColor);
        this.mProgressDrawable.setColorSchemeColors(this.mColors);
        if (isShowArrow()) {
            this.mProgressDrawable.setArrowScale(1.0f);
            this.mProgressDrawable.showArrow(true);
        }
        super.setImageDrawable((Drawable) null);
        super.setImageDrawable(this.mProgressDrawable);
        this.mProgressDrawable.setAlpha(255);
        if (getVisibility() == 0) {
            this.mProgressDrawable.setStartEndTrim(0.0f, 0.8f);
        }
    }

    public boolean isShowArrow() {
        return this.mShowArrow;
    }

    public void setShowArrow(boolean z) {
        this.mShowArrow = z;
    }

    public void setAnimationListener(Animation.AnimationListener animationListener) {
        this.mListener = animationListener;
    }

    public void onAnimationStart() {
        super.onAnimationStart();
        if (this.mListener != null) {
            this.mListener.onAnimationStart(getAnimation());
        }
    }

    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (this.mListener != null) {
            this.mListener.onAnimationEnd(getAnimation());
        }
    }

    public void setColorSchemeColors(int... iArr) {
        this.mColors = iArr;
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setColorSchemeColors(iArr);
        }
    }

    public void setBackgroundColorResource(int i) {
        if (getBackground() instanceof ShapeDrawable) {
            ((ShapeDrawable) getBackground()).getPaint().setColor(getResources().getColor(i));
        }
    }

    public void setBackgroundColor(int i) {
        if (getBackground() instanceof ShapeDrawable) {
            ((ShapeDrawable) getBackground()).getPaint().setColor(i);
        }
    }

    public int getMax() {
        return this.mMax;
    }

    public void setMax(int i) {
        this.mMax = i;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void setProgress(int i) {
        if (getMax() > 0) {
            this.mProgress = i;
        }
        invalidate();
    }

    public boolean circleBackgroundEnabled() {
        return this.mCircleBackgroundEnabled;
    }

    public void setCircleBackgroundEnabled(boolean z) {
        this.mCircleBackgroundEnabled = z;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.stop();
            this.mProgressDrawable.setVisible(getVisibility() == 0, false);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.stop();
            this.mProgressDrawable.setVisible(false, false);
        }
    }

    private class OvalShadow extends OvalShape {
        /* access modifiers changed from: private */
        public int mCircleDiameter;
        private RadialGradient mRadialGradient;
        private Paint mShadowPaint = new Paint();
        /* access modifiers changed from: private */
        public int mShadowRadius;

        public OvalShadow(int i, int i2) {
            this.mShadowRadius = i;
            this.mCircleDiameter = i2;
            this.mRadialGradient = new RadialGradient((float) (this.mCircleDiameter / 2), (float) (this.mCircleDiameter / 2), (float) this.mShadowRadius, new int[]{CircleProgressBar.FILL_SHADOW_COLOR, 0}, (float[]) null, Shader.TileMode.CLAMP);
            this.mShadowPaint.setShader(this.mRadialGradient);
        }

        public void draw(Canvas canvas, Paint paint) {
            float width = (float) (CircleProgressBar.this.getWidth() / 2);
            float height = (float) (CircleProgressBar.this.getHeight() / 2);
            canvas.drawCircle(width, height, (float) ((this.mCircleDiameter / 2) + this.mShadowRadius), this.mShadowPaint);
            canvas.drawCircle(width, height, (float) (this.mCircleDiameter / 2), paint);
        }
    }

    public void start() {
        this.mProgressDrawable.start();
    }

    public void setStartEndTrim(float f, float f2) {
        this.mProgressDrawable.setStartEndTrim(f, f2);
    }

    public void stop() {
        this.mProgressDrawable.stop();
    }

    public void setProgressRotation(float f) {
        this.mProgressDrawable.setProgressRotation(f);
    }
}
