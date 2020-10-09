package com.alimama.union.app.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import com.alimama.moon.view.RotationAnimation;
import com.alimama.unionwl.utils.LocalDisplay;

public class ISLoadingDrawable extends Drawable implements Animatable {
    private static final int HALF_SIZE = LocalDisplay.dp2px(30.0f);
    private static final int SIZE = LocalDisplay.dp2px(60.0f);
    private Animation mAnimation;
    private float mCircleWidth;
    private RectF mCircleZone;
    private int mMainColor;
    private int mOffsetX;
    private int mOffsetY;
    private Paint mPaint;
    private View mParent;
    private float mRotation;
    private boolean mRunning;
    private float mScale;
    private int mTransColor;

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public ISLoadingDrawable(Context context, View view) {
        this(context, view, LocalDisplay.dp2px(15.0f));
    }

    private ISLoadingDrawable(Context context, View view, int i) {
        this.mCircleWidth = 0.0f;
        this.mRotation = 0.0f;
        this.mScale = 0.0f;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mRunning = false;
        this.mMainColor = -6710887;
        this.mTransColor = 865704345;
        this.mParent = view;
        this.mScale = (((float) i) * 1.0f) / ((float) SIZE);
        this.mCircleZone = new RectF();
        this.mCircleZone.set((float) (-HALF_SIZE), (float) (-HALF_SIZE), (float) HALF_SIZE, (float) HALF_SIZE);
        this.mCircleWidth = (float) (i / 3);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(this.mCircleWidth);
        this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
        this.mPaint.setStyle(Paint.Style.STROKE);
        RotationAnimation rotationAnimation = new RotationAnimation(view, RotationAnimation.RotationOrientation.CLOCKWISE);
        rotationAnimation.setRepeatCount(-1);
        this.mAnimation = rotationAnimation;
    }

    public void setColor(int i) {
        this.mMainColor = i;
        this.mTransColor = (i & 16777215) | 855638016;
    }

    public void draw(Canvas canvas) {
        int save = canvas.save();
        canvas.translate((float) this.mOffsetX, (float) this.mOffsetY);
        canvas.scale(this.mScale, this.mScale);
        if (this.mRunning) {
            this.mPaint.setColor(this.mTransColor);
            canvas.drawCircle(0.0f, 0.0f, this.mCircleZone.width() / 2.0f, this.mPaint);
            this.mPaint.setColor(this.mMainColor);
            canvas.drawArc(this.mCircleZone, this.mRotation, 60.0f, false, this.mPaint);
        } else {
            this.mPaint.setColor(this.mMainColor);
            canvas.drawCircle(0.0f, 0.0f, this.mCircleZone.width() / 2.0f, this.mPaint);
        }
        canvas.restoreToCount(save);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mOffsetX = rect.width() / 2;
        this.mOffsetY = rect.height() / 2;
    }

    public void start() {
        this.mRunning = true;
        this.mAnimation.reset();
        this.mParent.startAnimation(this.mAnimation);
    }

    public void stop() {
        this.mRunning = false;
        this.mAnimation.cancel();
        this.mParent.clearAnimation();
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public void reset() {
        stop();
        invalidateSelf();
    }
}
