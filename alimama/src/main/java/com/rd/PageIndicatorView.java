package com.rd;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.TextUtilsCompat;
import androidx.viewpager.widget.ViewPager;
import com.rd.animation.AbsAnimation;
import com.rd.animation.AnimationType;
import com.rd.animation.ColorAnimation;
import com.rd.animation.ValueAnimation;
import com.rd.pageindicatorview.R;
import com.rd.utils.DensityUtils;

public class PageIndicatorView extends View implements ViewPager.OnPageChangeListener {
    private static final int COUNT_NOT_SET = -1;
    private static final int DEFAULT_CIRCLES_COUNT = 3;
    private static final int DEFAULT_PADDING_DP = 8;
    private static final int DEFAULT_RADIUS_DP = 6;
    private ValueAnimation animation;
    private long animationDuration;
    private AnimationType animationType = AnimationType.NONE;
    private int count;
    private boolean dynamicCount;
    private Paint fillPaint = new Paint();
    /* access modifiers changed from: private */
    public int frameColor;
    /* access modifiers changed from: private */
    public int frameColorReverse;
    /* access modifiers changed from: private */
    public int frameHeight;
    /* access modifiers changed from: private */
    public int frameLeftX;
    /* access modifiers changed from: private */
    public int frameRadiusPx;
    /* access modifiers changed from: private */
    public int frameRadiusReversePx;
    /* access modifiers changed from: private */
    public int frameRightX;
    /* access modifiers changed from: private */
    public int frameStrokePx;
    /* access modifiers changed from: private */
    public int frameStrokeReversePx;
    /* access modifiers changed from: private */
    public int frameX;
    /* access modifiers changed from: private */
    public int frameY;
    private boolean interactiveAnimation;
    private boolean isCountSet;
    private boolean isFrameValuesSet;
    /* access modifiers changed from: private */
    public int lastSelectedPosition;
    private int paddingPx;
    private int radiusPx;
    private RectF rect = new RectF();
    private RtlMode rtlMode = RtlMode.Off;
    private float scaleFactor;
    private int selectedColor;
    /* access modifiers changed from: private */
    public int selectedPosition;
    /* access modifiers changed from: private */
    public int selectingPosition;
    private DataSetObserver setObserver;
    private Paint strokePaint = new Paint();
    private int strokePx;
    private int unselectedColor;
    /* access modifiers changed from: private */
    public ViewPager viewPager;
    private int viewPagerId;

    public void onPageScrollStateChanged(int i) {
    }

    public PageIndicatorView(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    public PageIndicatorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public PageIndicatorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    @TargetApi(21)
    public PageIndicatorView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViewPager();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        unRegisterSetObserver();
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        PositionSavedState positionSavedState = new PositionSavedState(super.onSaveInstanceState());
        positionSavedState.setSelectedPosition(this.selectedPosition);
        positionSavedState.setSelectingPosition(this.selectingPosition);
        positionSavedState.setLastSelectedPosition(this.lastSelectedPosition);
        return positionSavedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof PositionSavedState) {
            PositionSavedState positionSavedState = (PositionSavedState) parcelable;
            this.selectedPosition = positionSavedState.getSelectedPosition();
            this.selectingPosition = positionSavedState.getSelectingPosition();
            this.lastSelectedPosition = positionSavedState.getLastSelectedPosition();
            super.onRestoreInstanceState(positionSavedState.getSuperState());
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        int i3 = this.radiusPx * 2;
        int i4 = this.strokePx + i3;
        int i5 = this.count != 0 ? (i3 * this.count) + (this.strokePx * 2 * this.count) + (this.paddingPx * (this.count - 1)) : 0;
        if (mode != 1073741824) {
            size = mode == Integer.MIN_VALUE ? Math.min(i5, size) : i5;
        }
        if (mode2 != 1073741824) {
            size2 = mode2 == Integer.MIN_VALUE ? Math.min(i4, size2) : i4;
        }
        if (this.animationType == AnimationType.DROP) {
            size2 *= 2;
        }
        if (size < 0) {
            size = 0;
        }
        if (size2 < 0) {
            size2 = 0;
        }
        setMeasuredDimension(size, size2);
    }

    private boolean isViewMeasured() {
        return (getMeasuredHeight() == 0 && getMeasuredWidth() == 0) ? false : true;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setupFrameValues();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        drawIndicatorView(canvas);
    }

    public void onPageScrolled(int i, float f, int i2) {
        if (isViewMeasured() && this.interactiveAnimation) {
            onPageScroll(i, f);
        }
    }

    public void onPageSelected(int i) {
        if ((this.viewPager != null && this.viewPager.getAdapter() != null && this.viewPager.getAdapter().getCount() < this.count) || !isViewMeasured()) {
            return;
        }
        if (!this.interactiveAnimation || this.animationType == AnimationType.NONE) {
            if (isRtl()) {
                i = (this.count - 1) - i;
            }
            setSelection(i);
        }
    }

    public void setCount(int i) {
        if (this.count != i) {
            this.count = i;
            this.isCountSet = true;
            requestLayout();
        }
    }

    public int getCount() {
        return this.count;
    }

    public void setDynamicCount(boolean z) {
        this.dynamicCount = z;
        if (z) {
            registerSetObserver();
        } else {
            unRegisterSetObserver();
        }
    }

    public void setRadius(int i) {
        if (i < 0) {
            i = 0;
        }
        this.radiusPx = DensityUtils.dpToPx(i);
        invalidate();
    }

    public void setRadius(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.radiusPx = (int) f;
        invalidate();
    }

    public int getRadius() {
        return this.radiusPx;
    }

    public void setPadding(int i) {
        if (i < 0) {
            i = 0;
        }
        this.paddingPx = DensityUtils.dpToPx(i);
        invalidate();
    }

    public void setPadding(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.paddingPx = (int) f;
        invalidate();
    }

    public int getPadding() {
        return this.paddingPx;
    }

    public void setScaleFactor(float f) {
        if (f > 1.0f) {
            f = 1.0f;
        } else if (f < 0.3f) {
            f = 0.3f;
        }
        this.scaleFactor = f;
    }

    public float getScaleFactor() {
        return this.scaleFactor;
    }

    public void setStrokeWidth(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        } else if (f > ((float) this.radiusPx)) {
            f = (float) this.radiusPx;
        }
        this.strokePx = (int) f;
        invalidate();
    }

    public void setStrokeWidth(int i) {
        int dpToPx = DensityUtils.dpToPx(i);
        if (dpToPx < 0) {
            dpToPx = 0;
        } else if (dpToPx > this.radiusPx) {
            dpToPx = this.radiusPx;
        }
        this.strokePx = dpToPx;
        invalidate();
    }

    public int getStrokeWidth() {
        return this.strokePx;
    }

    public void setUnselectedColor(int i) {
        this.unselectedColor = i;
        invalidate();
    }

    public int getUnselectedColor() {
        return this.unselectedColor;
    }

    public void setSelectedColor(int i) {
        this.selectedColor = i;
        invalidate();
    }

    public int getSelectedColor() {
        return this.selectedColor;
    }

    public void setAnimationDuration(long j) {
        this.animationDuration = j;
    }

    public long getAnimationDuration() {
        return this.animationDuration;
    }

    public void setAnimationType(@Nullable AnimationType animationType2) {
        if (animationType2 != null) {
            this.animationType = animationType2;
        } else {
            this.animationType = AnimationType.NONE;
        }
    }

    public void setInteractiveAnimation(boolean z) {
        this.interactiveAnimation = z;
    }

    public void setProgress(int i, float f) {
        if (this.interactiveAnimation) {
            if (this.count <= 0 || i < 0) {
                i = 0;
            } else if (i > this.count - 1) {
                i = this.count - 1;
            }
            if (f < 0.0f) {
                f = 0.0f;
            } else if (f > 1.0f) {
                f = 1.0f;
            }
            this.selectingPosition = i;
            setAnimationProgress(f);
        }
    }

    public void setSelection(int i) {
        if (i < 0) {
            i = 0;
        } else if (i > this.count - 1) {
            i = this.count - 1;
        }
        this.lastSelectedPosition = this.selectedPosition;
        this.selectedPosition = i;
        switch (this.animationType) {
            case NONE:
                invalidate();
                return;
            case COLOR:
                startColorAnimation();
                return;
            case SCALE:
                startScaleAnimation();
                return;
            case WORM:
                startWormAnimation();
                return;
            case FILL:
                startFillAnimation();
                return;
            case SLIDE:
                startSlideAnimation();
                return;
            case THIN_WORM:
                startThinWormAnimation();
                return;
            case DROP:
                startDropAnimation();
                return;
            case SWAP:
                startSwapAnimation();
                return;
            default:
                return;
        }
    }

    public int getSelection() {
        return this.selectedPosition;
    }

    public void setViewPager(@Nullable ViewPager viewPager2) {
        releaseViewPager();
        if (viewPager2 != null) {
            this.viewPager = viewPager2;
            this.viewPager.addOnPageChangeListener(this);
            setDynamicCount(this.dynamicCount);
            if (!this.isCountSet) {
                int viewPagerCount = getViewPagerCount();
                if (isRtl()) {
                    this.selectedPosition = (viewPagerCount - 1) - this.viewPager.getCurrentItem();
                }
                setCount(viewPagerCount);
            }
        }
    }

    public void releaseViewPager() {
        if (this.viewPager != null) {
            this.viewPager.removeOnPageChangeListener(this);
            this.viewPager = null;
        }
    }

    public void setRtlMode(@Nullable RtlMode rtlMode2) {
        if (rtlMode2 == null) {
            this.rtlMode = RtlMode.Off;
        } else {
            this.rtlMode = rtlMode2;
        }
    }

    private void onPageScroll(int i, float f) {
        Pair<Integer, Float> progress = getProgress(i, f);
        int intValue = ((Integer) progress.first).intValue();
        float floatValue = ((Float) progress.second).floatValue();
        if (floatValue == 1.0f) {
            this.lastSelectedPosition = this.selectedPosition;
            this.selectedPosition = intValue;
        }
        setProgress(intValue, floatValue);
    }

    private void drawIndicatorView(@NonNull Canvas canvas) {
        int yCoordinate = getYCoordinate();
        for (int i = 0; i < this.count; i++) {
            drawCircle(canvas, i, getXCoordinate(i), yCoordinate);
        }
    }

    private void drawCircle(@NonNull Canvas canvas, int i, int i2, int i3) {
        boolean z = true;
        boolean z2 = !this.interactiveAnimation && (i == this.selectedPosition || i == this.lastSelectedPosition);
        if (!this.interactiveAnimation || !(i == this.selectingPosition || i == this.selectedPosition)) {
            z = false;
        }
        if (z2 || z) {
            drawWithAnimationEffect(canvas, i, i2, i3);
        } else {
            drawWithNoEffect(canvas, i, i2, i3);
        }
    }

    private void drawWithAnimationEffect(@NonNull Canvas canvas, int i, int i2, int i3) {
        switch (this.animationType) {
            case NONE:
                drawWithNoEffect(canvas, i, i2, i3);
                return;
            case COLOR:
                drawWithColorAnimation(canvas, i, i2, i3);
                return;
            case SCALE:
                drawWithScaleAnimation(canvas, i, i2, i3);
                return;
            case WORM:
                drawWithWormAnimation(canvas, i2, i3);
                return;
            case FILL:
                drawWithFillAnimation(canvas, i, i2, i3);
                return;
            case SLIDE:
                drawWithSlideAnimation(canvas, i, i2, i3);
                return;
            case THIN_WORM:
                drawWithThinWormAnimation(canvas, i2, i3);
                return;
            case DROP:
                drawWithDropAnimation(canvas, i2, i3);
                return;
            case SWAP:
                drawWithSwapAnimation(canvas, i, i2, i3);
                return;
            default:
                return;
        }
    }

    private void drawWithNoEffect(@NonNull Canvas canvas, int i, int i2, int i3) {
        Paint paint;
        float f = (float) this.radiusPx;
        if (this.animationType == AnimationType.SCALE) {
            f *= this.scaleFactor;
        }
        int i4 = this.unselectedColor;
        if (i == this.selectedPosition) {
            i4 = this.selectedColor;
        }
        if (this.animationType == AnimationType.FILL) {
            paint = this.strokePaint;
            paint.setStrokeWidth((float) this.strokePx);
        } else {
            paint = this.fillPaint;
        }
        paint.setColor(i4);
        canvas.drawCircle((float) i2, (float) i3, f, paint);
    }

    private void drawWithColorAnimation(@NonNull Canvas canvas, int i, int i2, int i3) {
        int i4 = this.unselectedColor;
        if (this.interactiveAnimation) {
            if (i == this.selectingPosition) {
                i4 = this.frameColor;
            } else if (i == this.selectedPosition) {
                i4 = this.frameColorReverse;
            }
        } else if (i == this.selectedPosition) {
            i4 = this.frameColor;
        } else if (i == this.lastSelectedPosition) {
            i4 = this.frameColorReverse;
        }
        this.fillPaint.setColor(i4);
        canvas.drawCircle((float) i2, (float) i3, (float) this.radiusPx, this.fillPaint);
    }

    private void drawWithScaleAnimation(@NonNull Canvas canvas, int i, int i2, int i3) {
        int i4 = this.unselectedColor;
        int i5 = this.radiusPx;
        if (this.interactiveAnimation) {
            if (i == this.selectingPosition) {
                i5 = this.frameRadiusPx;
                i4 = this.frameColor;
            } else if (i == this.selectedPosition) {
                i5 = this.frameRadiusReversePx;
                i4 = this.frameColorReverse;
            }
        } else if (i == this.selectedPosition) {
            i5 = this.frameRadiusPx;
            i4 = this.frameColor;
        } else if (i == this.lastSelectedPosition) {
            i5 = this.frameRadiusReversePx;
            i4 = this.frameColorReverse;
        }
        this.fillPaint.setColor(i4);
        canvas.drawCircle((float) i2, (float) i3, (float) i5, this.fillPaint);
    }

    private void drawWithSlideAnimation(@NonNull Canvas canvas, int i, int i2, int i3) {
        this.fillPaint.setColor(this.unselectedColor);
        float f = (float) i3;
        canvas.drawCircle((float) i2, f, (float) this.radiusPx, this.fillPaint);
        if (this.interactiveAnimation && (i == this.selectingPosition || i == this.selectedPosition)) {
            this.fillPaint.setColor(this.selectedColor);
            canvas.drawCircle((float) this.frameX, f, (float) this.radiusPx, this.fillPaint);
        } else if (this.interactiveAnimation) {
        } else {
            if (i == this.selectedPosition || i == this.lastSelectedPosition) {
                this.fillPaint.setColor(this.selectedColor);
                canvas.drawCircle((float) this.frameX, f, (float) this.radiusPx, this.fillPaint);
            }
        }
    }

    private void drawWithWormAnimation(@NonNull Canvas canvas, int i, int i2) {
        int i3 = this.radiusPx;
        int i4 = this.frameLeftX;
        int i5 = this.frameRightX;
        this.rect.left = (float) i4;
        this.rect.right = (float) i5;
        this.rect.top = (float) (i2 - i3);
        this.rect.bottom = (float) (i2 + i3);
        this.fillPaint.setColor(this.unselectedColor);
        canvas.drawCircle((float) i, (float) i2, (float) i3, this.fillPaint);
        this.fillPaint.setColor(this.selectedColor);
        canvas.drawRoundRect(this.rect, (float) this.radiusPx, (float) this.radiusPx, this.fillPaint);
    }

    private void drawWithFillAnimation(@NonNull Canvas canvas, int i, int i2, int i3) {
        int i4 = this.unselectedColor;
        float f = (float) this.radiusPx;
        int i5 = this.strokePx;
        if (this.interactiveAnimation) {
            if (i == this.selectingPosition) {
                i4 = this.frameColor;
                f = (float) this.frameRadiusPx;
                i5 = this.frameStrokePx;
            } else if (i == this.selectedPosition) {
                i4 = this.frameColorReverse;
                f = (float) this.frameRadiusReversePx;
                i5 = this.frameStrokeReversePx;
            }
        } else if (i == this.selectedPosition) {
            i4 = this.frameColor;
            f = (float) this.frameRadiusPx;
            i5 = this.frameStrokePx;
        } else if (i == this.lastSelectedPosition) {
            i4 = this.frameColorReverse;
            f = (float) this.frameRadiusReversePx;
            i5 = this.frameStrokeReversePx;
        }
        this.strokePaint.setColor(i4);
        this.strokePaint.setStrokeWidth((float) this.strokePx);
        float f2 = (float) i2;
        float f3 = (float) i3;
        canvas.drawCircle(f2, f3, (float) this.radiusPx, this.strokePaint);
        this.strokePaint.setStrokeWidth((float) i5);
        canvas.drawCircle(f2, f3, f, this.strokePaint);
    }

    private void drawWithThinWormAnimation(@NonNull Canvas canvas, int i, int i2) {
        int i3 = this.radiusPx;
        int i4 = this.frameLeftX;
        int i5 = this.frameRightX;
        this.rect.left = (float) i4;
        this.rect.right = (float) i5;
        this.rect.top = (float) (i2 - (this.frameHeight / 2));
        this.rect.bottom = (float) ((this.frameHeight / 2) + i2);
        this.fillPaint.setColor(this.unselectedColor);
        canvas.drawCircle((float) i, (float) i2, (float) i3, this.fillPaint);
        this.fillPaint.setColor(this.selectedColor);
        canvas.drawRoundRect(this.rect, (float) this.radiusPx, (float) this.radiusPx, this.fillPaint);
    }

    private void drawWithDropAnimation(@NonNull Canvas canvas, int i, int i2) {
        this.fillPaint.setColor(this.unselectedColor);
        canvas.drawCircle((float) i, (float) i2, (float) this.radiusPx, this.fillPaint);
        this.fillPaint.setColor(this.selectedColor);
        canvas.drawCircle((float) this.frameX, (float) this.frameY, (float) this.frameRadiusPx, this.fillPaint);
    }

    private void drawWithSwapAnimation(@NonNull Canvas canvas, int i, int i2, int i3) {
        this.fillPaint.setColor(this.unselectedColor);
        if (i == this.selectedPosition) {
            this.fillPaint.setColor(this.selectedColor);
            canvas.drawCircle((float) this.frameX, (float) i3, (float) this.radiusPx, this.fillPaint);
        } else if (this.interactiveAnimation && i == this.selectingPosition) {
            canvas.drawCircle((float) (i2 - (this.frameX - getXCoordinate(this.selectedPosition))), (float) i3, (float) this.radiusPx, this.fillPaint);
        } else if (!this.interactiveAnimation) {
            canvas.drawCircle((float) (i2 - (this.frameX - getXCoordinate(this.selectedPosition))), (float) i3, (float) this.radiusPx, this.fillPaint);
        } else {
            canvas.drawCircle((float) i2, (float) i3, (float) this.radiusPx, this.fillPaint);
        }
    }

    private void init(@Nullable AttributeSet attributeSet) {
        setupId();
        initAttributes(attributeSet);
        initAnimation();
        this.fillPaint.setStyle(Paint.Style.FILL);
        this.fillPaint.setAntiAlias(true);
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setAntiAlias(true);
        this.strokePaint.setStrokeWidth((float) this.strokePx);
    }

    private void setupId() {
        if (getId() == -1) {
            setId(Utils.generateViewId());
        }
    }

    private void initAttributes(@Nullable AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.PageIndicatorView, 0, 0);
        initCountAttribute(obtainStyledAttributes);
        initColorAttribute(obtainStyledAttributes);
        initAnimationAttribute(obtainStyledAttributes);
        initSizeAttribute(obtainStyledAttributes);
    }

    private void initCountAttribute(@NonNull TypedArray typedArray) {
        this.dynamicCount = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_dynamicCount, false);
        this.count = typedArray.getInt(R.styleable.PageIndicatorView_piv_count, -1);
        if (this.count != -1) {
            this.isCountSet = true;
        } else {
            this.count = 3;
        }
        int i = typedArray.getInt(R.styleable.PageIndicatorView_piv_select, 0);
        if (i < 0) {
            i = 0;
        } else if (this.count > 0 && i > this.count - 1) {
            i = this.count - 1;
        }
        this.selectedPosition = i;
        this.selectingPosition = i;
        this.viewPagerId = typedArray.getResourceId(R.styleable.PageIndicatorView_piv_viewPager, 0);
    }

    private void initColorAttribute(@NonNull TypedArray typedArray) {
        this.unselectedColor = typedArray.getColor(R.styleable.PageIndicatorView_piv_unselectedColor, Color.parseColor(ColorAnimation.DEFAULT_UNSELECTED_COLOR));
        this.selectedColor = typedArray.getColor(R.styleable.PageIndicatorView_piv_selectedColor, Color.parseColor(ColorAnimation.DEFAULT_SELECTED_COLOR));
    }

    private void initAnimationAttribute(@NonNull TypedArray typedArray) {
        this.animationDuration = (long) typedArray.getInt(R.styleable.PageIndicatorView_piv_animationDuration, AbsAnimation.DEFAULT_ANIMATION_TIME);
        this.interactiveAnimation = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_interactiveAnimation, false);
        this.animationType = getAnimationType(typedArray.getInt(R.styleable.PageIndicatorView_piv_animationType, AnimationType.NONE.ordinal()));
        this.rtlMode = getRtlMode(typedArray.getInt(R.styleable.PageIndicatorView_piv_rtl_mode, RtlMode.Off.ordinal()));
    }

    private void initSizeAttribute(@NonNull TypedArray typedArray) {
        this.radiusPx = (int) typedArray.getDimension(R.styleable.PageIndicatorView_piv_radius, (float) DensityUtils.dpToPx(6));
        this.paddingPx = (int) typedArray.getDimension(R.styleable.PageIndicatorView_piv_padding, (float) DensityUtils.dpToPx(8));
        this.scaleFactor = typedArray.getFloat(R.styleable.PageIndicatorView_piv_scaleFactor, 0.7f);
        if (this.scaleFactor < 0.3f) {
            this.scaleFactor = 0.3f;
        } else if (this.scaleFactor > 1.0f) {
            this.scaleFactor = 1.0f;
        }
        this.strokePx = (int) typedArray.getDimension(R.styleable.PageIndicatorView_piv_strokeWidth, (float) DensityUtils.dpToPx(1));
        if (this.strokePx > this.radiusPx) {
            this.strokePx = this.radiusPx;
        }
        if (this.animationType != AnimationType.FILL) {
            this.strokePx = 0;
        }
    }

    private void initAnimation() {
        this.animation = new ValueAnimation(new ValueAnimation.UpdateListener() {
            public void onColorAnimationUpdated(int i, int i2) {
                int unused = PageIndicatorView.this.frameColor = i;
                int unused2 = PageIndicatorView.this.frameColorReverse = i2;
                PageIndicatorView.this.invalidate();
            }

            public void onScaleAnimationUpdated(int i, int i2, int i3, int i4) {
                int unused = PageIndicatorView.this.frameColor = i;
                int unused2 = PageIndicatorView.this.frameColorReverse = i2;
                int unused3 = PageIndicatorView.this.frameRadiusPx = i3;
                int unused4 = PageIndicatorView.this.frameRadiusReversePx = i4;
                PageIndicatorView.this.invalidate();
            }

            public void onSlideAnimationUpdated(int i) {
                int unused = PageIndicatorView.this.frameX = i;
                PageIndicatorView.this.invalidate();
            }

            public void onWormAnimationUpdated(int i, int i2) {
                int unused = PageIndicatorView.this.frameLeftX = i;
                int unused2 = PageIndicatorView.this.frameRightX = i2;
                PageIndicatorView.this.invalidate();
            }

            public void onThinWormAnimationUpdated(int i, int i2, int i3) {
                int unused = PageIndicatorView.this.frameLeftX = i;
                int unused2 = PageIndicatorView.this.frameRightX = i2;
                int unused3 = PageIndicatorView.this.frameHeight = i3;
                PageIndicatorView.this.invalidate();
            }

            public void onFillAnimationUpdated(int i, int i2, int i3, int i4, int i5, int i6) {
                int unused = PageIndicatorView.this.frameColor = i;
                int unused2 = PageIndicatorView.this.frameColorReverse = i2;
                int unused3 = PageIndicatorView.this.frameRadiusPx = i3;
                int unused4 = PageIndicatorView.this.frameRadiusReversePx = i4;
                int unused5 = PageIndicatorView.this.frameStrokePx = i5;
                int unused6 = PageIndicatorView.this.frameStrokeReversePx = i6;
                PageIndicatorView.this.invalidate();
            }

            public void onDropAnimationUpdated(int i, int i2, int i3) {
                int unused = PageIndicatorView.this.frameX = i;
                int unused2 = PageIndicatorView.this.frameY = i2;
                int unused3 = PageIndicatorView.this.frameRadiusPx = i3;
                PageIndicatorView.this.invalidate();
            }

            public void onSwapAnimationUpdated(int i) {
                int unused = PageIndicatorView.this.frameX = i;
                PageIndicatorView.this.invalidate();
            }
        });
    }

    private AnimationType getAnimationType(int i) {
        switch (i) {
            case 0:
                return AnimationType.NONE;
            case 1:
                return AnimationType.COLOR;
            case 2:
                return AnimationType.SCALE;
            case 3:
                return AnimationType.WORM;
            case 4:
                return AnimationType.SLIDE;
            case 5:
                return AnimationType.FILL;
            case 6:
                return AnimationType.THIN_WORM;
            case 7:
                return AnimationType.DROP;
            case 8:
                return AnimationType.SWAP;
            default:
                return AnimationType.NONE;
        }
    }

    private RtlMode getRtlMode(int i) {
        switch (i) {
            case 0:
                return RtlMode.On;
            case 1:
                return RtlMode.Off;
            case 2:
                return RtlMode.Auto;
            default:
                return RtlMode.Auto;
        }
    }

    /* access modifiers changed from: private */
    public void resetFrameValues() {
        this.isFrameValuesSet = false;
        setupFrameValues();
    }

    private void setupFrameValues() {
        if (!this.isFrameValuesSet) {
            this.frameColor = this.selectedColor;
            this.frameColorReverse = this.unselectedColor;
            this.frameRadiusPx = this.radiusPx;
            this.frameRadiusReversePx = this.radiusPx;
            int xCoordinate = getXCoordinate(this.selectedPosition);
            if (xCoordinate - this.radiusPx >= 0) {
                this.frameLeftX = xCoordinate - this.radiusPx;
                this.frameRightX = this.radiusPx + xCoordinate;
            } else {
                this.frameLeftX = xCoordinate;
                this.frameRightX = (this.radiusPx * 2) + xCoordinate;
            }
            this.frameX = xCoordinate;
            this.frameY = getYCoordinate();
            this.frameStrokePx = this.radiusPx;
            this.frameStrokeReversePx = this.radiusPx / 2;
            if (this.animationType == AnimationType.FILL) {
                this.frameRadiusPx = this.radiusPx / 2;
                this.frameRadiusReversePx = this.radiusPx;
            }
            this.frameHeight = this.radiusPx * 2;
            this.isFrameValuesSet = true;
        }
    }

    private void startColorAnimation() {
        this.animation.color().end();
        this.animation.color().with(this.unselectedColor, this.selectedColor).duration(this.animationDuration).start();
    }

    private void startScaleAnimation() {
        this.animation.scale().end();
        this.animation.scale().with(this.unselectedColor, this.selectedColor, this.radiusPx, this.scaleFactor).duration(this.animationDuration).start();
    }

    private void startSlideAnimation() {
        int xCoordinate = getXCoordinate(this.lastSelectedPosition);
        int xCoordinate2 = getXCoordinate(this.selectedPosition);
        this.animation.slide().end();
        this.animation.slide().with(xCoordinate, xCoordinate2).duration(this.animationDuration).start();
    }

    private void startWormAnimation() {
        int xCoordinate = getXCoordinate(this.lastSelectedPosition);
        int xCoordinate2 = getXCoordinate(this.selectedPosition);
        boolean z = this.selectedPosition > this.lastSelectedPosition;
        this.animation.worm().end();
        this.animation.worm().duration(this.animationDuration).with(xCoordinate, xCoordinate2, this.radiusPx, z).start();
    }

    private void startFillAnimation() {
        this.animation.fill().end();
        this.animation.fill().with(this.unselectedColor, this.selectedColor, this.radiusPx, this.strokePx).duration(this.animationDuration).start();
    }

    private void startThinWormAnimation() {
        int xCoordinate = getXCoordinate(this.lastSelectedPosition);
        int xCoordinate2 = getXCoordinate(this.selectedPosition);
        boolean z = this.selectedPosition > this.lastSelectedPosition;
        this.animation.thinWorm().end();
        this.animation.thinWorm().duration(this.animationDuration).with(xCoordinate, xCoordinate2, this.radiusPx, z).start();
    }

    private void startDropAnimation() {
        int xCoordinate = getXCoordinate(this.lastSelectedPosition);
        int xCoordinate2 = getXCoordinate(this.selectedPosition);
        int yCoordinate = getYCoordinate();
        this.animation.drop().end();
        this.animation.drop().duration(this.animationDuration).with(xCoordinate, xCoordinate2, yCoordinate, this.radiusPx).start();
    }

    private void startSwapAnimation() {
        int xCoordinate = getXCoordinate(this.lastSelectedPosition);
        int xCoordinate2 = getXCoordinate(this.selectedPosition);
        this.animation.swap().end();
        this.animation.swap().with(xCoordinate, xCoordinate2).duration(this.animationDuration).start();
    }

    @Nullable
    private AbsAnimation setAnimationProgress(float f) {
        switch (this.animationType) {
            case COLOR:
                return this.animation.color().with(this.unselectedColor, this.selectedColor).progress(f);
            case SCALE:
                return this.animation.scale().with(this.unselectedColor, this.selectedColor, this.radiusPx, this.scaleFactor).progress(f);
            case WORM:
            case SLIDE:
            case THIN_WORM:
            case DROP:
            case SWAP:
                int xCoordinate = getXCoordinate(this.selectedPosition);
                int xCoordinate2 = getXCoordinate(this.selectingPosition);
                if (this.animationType == AnimationType.SLIDE) {
                    return this.animation.slide().with(xCoordinate, xCoordinate2).progress(f);
                }
                if (this.animationType == AnimationType.SWAP) {
                    return this.animation.swap().with(xCoordinate, xCoordinate2).progress(f);
                }
                if (this.animationType == AnimationType.WORM || this.animationType == AnimationType.THIN_WORM) {
                    boolean z = this.selectingPosition > this.selectedPosition;
                    if (this.animationType == AnimationType.WORM) {
                        return this.animation.worm().with(xCoordinate, xCoordinate2, this.radiusPx, z).progress(f);
                    }
                    if (this.animationType == AnimationType.THIN_WORM) {
                        return this.animation.thinWorm().with(xCoordinate, xCoordinate2, this.radiusPx, z).progress(f);
                    }
                    return null;
                }
                return this.animation.drop().with(xCoordinate, xCoordinate2, getYCoordinate(), this.radiusPx).progress(f);
            case FILL:
                return this.animation.fill().with(this.unselectedColor, this.selectedColor, this.radiusPx, this.strokePx).progress(f);
            default:
                return null;
        }
    }

    private void registerSetObserver() {
        if (this.setObserver == null && this.viewPager != null && this.viewPager.getAdapter() != null) {
            this.setObserver = new DataSetObserver() {
                public void onChanged() {
                    if (PageIndicatorView.this.viewPager != null && PageIndicatorView.this.viewPager.getAdapter() != null) {
                        int count = PageIndicatorView.this.viewPager.getAdapter().getCount();
                        int currentItem = PageIndicatorView.this.viewPager.getCurrentItem();
                        int unused = PageIndicatorView.this.selectedPosition = currentItem;
                        int unused2 = PageIndicatorView.this.selectingPosition = currentItem;
                        int unused3 = PageIndicatorView.this.lastSelectedPosition = currentItem;
                        PageIndicatorView.this.endAnimation();
                        PageIndicatorView.this.setCount(count);
                        PageIndicatorView.this.resetFrameValues();
                        PageIndicatorView.this.setProgress(PageIndicatorView.this.selectingPosition, 1.0f);
                    }
                }
            };
            try {
                this.viewPager.getAdapter().registerDataSetObserver(this.setObserver);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void endAnimation() {
        AbsAnimation absAnimation;
        switch (this.animationType) {
            case COLOR:
                absAnimation = this.animation.color();
                break;
            case SCALE:
                absAnimation = this.animation.scale();
                break;
            case WORM:
                absAnimation = this.animation.worm();
                break;
            case FILL:
                absAnimation = this.animation.fill();
                break;
            case SLIDE:
                absAnimation = this.animation.slide();
                break;
            case THIN_WORM:
                absAnimation = this.animation.thinWorm();
                break;
            case DROP:
                absAnimation = this.animation.drop();
                break;
            case SWAP:
                absAnimation = this.animation.swap();
                break;
            default:
                absAnimation = null;
                break;
        }
        if (absAnimation != null) {
            absAnimation.end();
        }
    }

    private void unRegisterSetObserver() {
        if (this.setObserver != null && this.viewPager != null && this.viewPager.getAdapter() != null) {
            try {
                this.viewPager.getAdapter().unregisterDataSetObserver(this.setObserver);
                this.setObserver = null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private int getViewPagerCount() {
        if (this.viewPager == null || this.viewPager.getAdapter() == null) {
            return this.count;
        }
        return this.viewPager.getAdapter().getCount();
    }

    private void findViewPager() {
        View findViewById;
        if (this.viewPagerId != 0 && (getContext() instanceof Activity) && (findViewById = ((Activity) getContext()).findViewById(this.viewPagerId)) != null && (findViewById instanceof ViewPager)) {
            setViewPager((ViewPager) findViewById);
        }
    }

    private int getXCoordinate(int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < this.count; i3++) {
            int i4 = i2 + this.radiusPx + this.strokePx;
            if (i == i3) {
                return i4;
            }
            i2 = i4 + this.radiusPx + this.paddingPx;
        }
        return i2;
    }

    private int getYCoordinate() {
        int height = getHeight() / 2;
        return this.animationType == AnimationType.DROP ? height + this.radiusPx : height;
    }

    private Pair<Integer, Float> getProgress(int i, float f) {
        boolean z = false;
        if (isRtl() && (i = (this.count - 1) - i) < 0) {
            i = 0;
        }
        boolean z2 = i > this.selectedPosition;
        boolean z3 = !isRtl() ? i + 1 < this.selectedPosition : i + -1 < this.selectedPosition;
        if (z2 || z3) {
            this.selectedPosition = i;
        }
        if (this.selectedPosition == i && f != 0.0f) {
            z = true;
        }
        if (z) {
            i = isRtl() ? i - 1 : i + 1;
        } else {
            f = 1.0f - f;
        }
        if (f > 1.0f) {
            f = 1.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        return new Pair<>(Integer.valueOf(i), Float.valueOf(f));
    }

    private boolean isRtl() {
        switch (this.rtlMode) {
            case On:
                return true;
            case Off:
                return false;
            case Auto:
                if (TextUtilsCompat.getLayoutDirectionFromLocale(getContext().getResources().getConfiguration().locale) == 1) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
