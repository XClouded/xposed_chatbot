package com.taobao.weex.analyzer.view.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;
import androidx.core.view.ViewCompat;
import androidx.core.widget.EdgeEffectCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Viewport {
    private int mBackgroundColor;
    private Integer mBorderColor;
    private Paint mBorderPaint;
    protected RectD mCompleteRange = new RectD();
    protected RectD mCurrentViewport = new RectD();
    private boolean mDrawBorder;
    /* access modifiers changed from: private */
    public EdgeEffectCompat mEdgeEffectBottom;
    /* access modifiers changed from: private */
    public EdgeEffectCompat mEdgeEffectLeft;
    /* access modifiers changed from: private */
    public EdgeEffectCompat mEdgeEffectRight;
    /* access modifiers changed from: private */
    public EdgeEffectCompat mEdgeEffectTop;
    protected GestureDetector mGestureDetector;
    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return true;
        }

        public boolean onDown(MotionEvent motionEvent) {
            if (!Viewport.this.mIsScrollable || Viewport.this.mScalingActive) {
                return false;
            }
            Viewport.this.releaseEdgeEffects();
            Viewport.this.mScroller.forceFinished(true);
            ViewCompat.postInvalidateOnAnimation(Viewport.this.mGraphView);
            return true;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            int i;
            if (!Viewport.this.mIsScrollable || Viewport.this.mScalingActive) {
                return false;
            }
            double d = (double) f;
            double width = Viewport.this.mCurrentViewport.width();
            Double.isNaN(d);
            double d2 = d * width;
            double graphContentWidth = (double) Viewport.this.mGraphView.getGraphContentWidth();
            Double.isNaN(graphContentWidth);
            double d3 = d2 / graphContentWidth;
            double d4 = (double) f2;
            double height = Viewport.this.mCurrentViewport.height();
            Double.isNaN(d4);
            double d5 = d4 * height;
            double graphContentHeight = (double) Viewport.this.mGraphView.getGraphContentHeight();
            Double.isNaN(graphContentHeight);
            double d6 = d5 / graphContentHeight;
            double width2 = Viewport.this.mCompleteRange.width() / Viewport.this.mCurrentViewport.width();
            double graphContentWidth2 = (double) Viewport.this.mGraphView.getGraphContentWidth();
            Double.isNaN(graphContentWidth2);
            int i2 = (int) (width2 * graphContentWidth2);
            double height2 = Viewport.this.mCompleteRange.height() / Viewport.this.mCurrentViewport.height();
            double graphContentHeight2 = (double) Viewport.this.mGraphView.getGraphContentHeight();
            Double.isNaN(graphContentHeight2);
            int i3 = (int) (height2 * graphContentHeight2);
            double d7 = (double) i2;
            Double.isNaN(d7);
            int width3 = (int) ((d7 * ((Viewport.this.mCurrentViewport.left + d3) - Viewport.this.mCompleteRange.left)) / Viewport.this.mCompleteRange.width());
            double d8 = (double) i3;
            Double.isNaN(d8);
            int height3 = (int) (((d8 * ((Viewport.this.mCurrentViewport.bottom + d6) - Viewport.this.mCompleteRange.bottom)) / Viewport.this.mCompleteRange.height()) * -1.0d);
            boolean z = Viewport.this.mCurrentViewport.left > Viewport.this.mCompleteRange.left || Viewport.this.mCurrentViewport.right < Viewport.this.mCompleteRange.right;
            boolean access$400 = (Viewport.this.mCurrentViewport.bottom > Viewport.this.mCompleteRange.bottom || Viewport.this.mCurrentViewport.top < Viewport.this.mCompleteRange.top) & Viewport.this.scrollableY;
            if (z) {
                if (d3 < 0.0d) {
                    i = i2;
                    double d9 = (Viewport.this.mCurrentViewport.left + d3) - Viewport.this.mCompleteRange.left;
                    if (d9 < 0.0d) {
                        d3 -= d9;
                    }
                } else {
                    i = i2;
                    double d10 = (Viewport.this.mCurrentViewport.right + d3) - Viewport.this.mCompleteRange.right;
                    if (d10 > 0.0d) {
                        d3 -= d10;
                    }
                }
                Viewport.this.mCurrentViewport.left += d3;
                Viewport.this.mCurrentViewport.right += d3;
                if (Viewport.this.mOnXAxisBoundsChangedListener != null) {
                    Viewport.this.mOnXAxisBoundsChangedListener.onXAxisBoundsChanged(Viewport.this.getMinX(false), Viewport.this.getMaxX(false), OnXAxisBoundsChangedListener.Reason.SCROLL);
                }
            } else {
                i = i2;
            }
            if (access$400) {
                if (d6 < 0.0d) {
                    double d11 = (Viewport.this.mCurrentViewport.bottom + d6) - Viewport.this.mCompleteRange.bottom;
                    if (d11 < 0.0d) {
                        d6 -= d11;
                    }
                } else {
                    double d12 = (Viewport.this.mCurrentViewport.top + d6) - Viewport.this.mCompleteRange.top;
                    if (d12 > 0.0d) {
                        d6 -= d12;
                    }
                }
                Viewport.this.mCurrentViewport.top += d6;
                Viewport.this.mCurrentViewport.bottom += d6;
            }
            if (z && width3 < 0) {
                Viewport.this.mEdgeEffectLeft.onPull(((float) width3) / ((float) Viewport.this.mGraphView.getGraphContentWidth()));
            }
            if (access$400 && height3 < 0) {
                Viewport.this.mEdgeEffectBottom.onPull(((float) height3) / ((float) Viewport.this.mGraphView.getGraphContentHeight()));
            }
            if (z && width3 > i - Viewport.this.mGraphView.getGraphContentWidth()) {
                Viewport.this.mEdgeEffectRight.onPull(((float) ((width3 - i) + Viewport.this.mGraphView.getGraphContentWidth())) / ((float) Viewport.this.mGraphView.getGraphContentWidth()));
            }
            if (access$400 && height3 > i3 - Viewport.this.mGraphView.getGraphContentHeight()) {
                Viewport.this.mEdgeEffectTop.onPull(((float) ((height3 - i3) + Viewport.this.mGraphView.getGraphContentHeight())) / ((float) Viewport.this.mGraphView.getGraphContentHeight()));
            }
            Viewport.this.mGraphView.onDataChanged(true, false);
            ViewCompat.postInvalidateOnAnimation(Viewport.this.mGraphView);
            return true;
        }
    };
    /* access modifiers changed from: private */
    public final ChartView mGraphView;
    /* access modifiers changed from: private */
    public boolean mIsScalable;
    /* access modifiers changed from: private */
    public boolean mIsScrollable;
    protected double mMaxXAxisSize = 0.0d;
    protected double mMaxYAxisSize = 0.0d;
    protected OnXAxisBoundsChangedListener mOnXAxisBoundsChangedListener;
    private Paint mPaint;
    protected ScaleGestureDetector mScaleGestureDetector;
    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float f;
            double width = Viewport.this.mCurrentViewport.width();
            if (Viewport.this.mMaxXAxisSize != 0.0d && width > Viewport.this.mMaxXAxisSize) {
                width = Viewport.this.mMaxXAxisSize;
            }
            double d = Viewport.this.mCurrentViewport.left + (width / 2.0d);
            if (Build.VERSION.SDK_INT < 11 || !Viewport.this.scalableY) {
                f = scaleGestureDetector.getScaleFactor();
            } else {
                f = scaleGestureDetector.getCurrentSpanX() / scaleGestureDetector.getPreviousSpanX();
            }
            double d2 = (double) f;
            Double.isNaN(d2);
            double d3 = width / d2;
            Viewport.this.mCurrentViewport.left = d - (d3 / 2.0d);
            Viewport.this.mCurrentViewport.right = Viewport.this.mCurrentViewport.left + d3;
            double minX = Viewport.this.getMinX(true);
            if (Viewport.this.mCurrentViewport.left < minX) {
                Viewport.this.mCurrentViewport.left = minX;
                Viewport.this.mCurrentViewport.right = Viewport.this.mCurrentViewport.left + d3;
            }
            double maxX = Viewport.this.getMaxX(true);
            if (d3 == 0.0d) {
                Viewport.this.mCurrentViewport.right = maxX;
            }
            double d4 = (Viewport.this.mCurrentViewport.left + d3) - maxX;
            if (d4 > 0.0d) {
                if (Viewport.this.mCurrentViewport.left - d4 > minX) {
                    Viewport.this.mCurrentViewport.left -= d4;
                    Viewport.this.mCurrentViewport.right = Viewport.this.mCurrentViewport.left + d3;
                } else {
                    Viewport.this.mCurrentViewport.left = minX;
                    Viewport.this.mCurrentViewport.right = maxX;
                }
            }
            if (Viewport.this.scalableY && Build.VERSION.SDK_INT >= 11) {
                double height = Viewport.this.mCurrentViewport.height() * -1.0d;
                if (Viewport.this.mMaxYAxisSize != 0.0d && height > Viewport.this.mMaxYAxisSize) {
                    height = Viewport.this.mMaxYAxisSize;
                }
                double d5 = Viewport.this.mCurrentViewport.bottom + (height / 2.0d);
                double currentSpanY = (double) (scaleGestureDetector.getCurrentSpanY() / scaleGestureDetector.getPreviousSpanY());
                Double.isNaN(currentSpanY);
                double d6 = height / currentSpanY;
                Viewport.this.mCurrentViewport.bottom = d5 - (d6 / 2.0d);
                Viewport.this.mCurrentViewport.top = Viewport.this.mCurrentViewport.bottom + d6;
                double minY = Viewport.this.getMinY(true);
                if (Viewport.this.mCurrentViewport.bottom < minY) {
                    Viewport.this.mCurrentViewport.bottom = minY;
                    Viewport.this.mCurrentViewport.top = Viewport.this.mCurrentViewport.bottom + d6;
                }
                double maxY = Viewport.this.getMaxY(true);
                if (d6 == 0.0d) {
                    Viewport.this.mCurrentViewport.top = maxY;
                }
                double d7 = (Viewport.this.mCurrentViewport.bottom + d6) - maxY;
                if (d7 > 0.0d) {
                    if (Viewport.this.mCurrentViewport.bottom - d7 > minY) {
                        Viewport.this.mCurrentViewport.bottom -= d7;
                        Viewport.this.mCurrentViewport.top = Viewport.this.mCurrentViewport.bottom + d6;
                    } else {
                        Viewport.this.mCurrentViewport.bottom = minY;
                        Viewport.this.mCurrentViewport.top = maxY;
                    }
                }
            }
            Viewport.this.mGraphView.onDataChanged(true, false);
            ViewCompat.postInvalidateOnAnimation(Viewport.this.mGraphView);
            return true;
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            if (!Viewport.this.mIsScalable) {
                return false;
            }
            Viewport.this.mScalingActive = true;
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            Viewport.this.mScalingActive = false;
            if (Viewport.this.mOnXAxisBoundsChangedListener != null) {
                Viewport.this.mOnXAxisBoundsChangedListener.onXAxisBoundsChanged(Viewport.this.getMinX(false), Viewport.this.getMaxX(false), OnXAxisBoundsChangedListener.Reason.SCALE);
            }
            ViewCompat.postInvalidateOnAnimation(Viewport.this.mGraphView);
        }
    };
    protected boolean mScalingActive;
    protected OverScroller mScroller;
    private boolean mXAxisBoundsManual;
    protected AxisBoundsStatus mXAxisBoundsStatus;
    private boolean mYAxisBoundsManual;
    protected AxisBoundsStatus mYAxisBoundsStatus;
    protected double referenceX = Double.NaN;
    protected double referenceY = Double.NaN;
    protected boolean scalableY;
    /* access modifiers changed from: private */
    public boolean scrollableY;

    public enum AxisBoundsStatus {
        INITIAL,
        AUTO_ADJUSTED,
        FIX
    }

    public interface OnXAxisBoundsChangedListener {

        public enum Reason {
            SCROLL,
            SCALE
        }

        void onXAxisBoundsChanged(double d, double d2, Reason reason);
    }

    public void computeScroll() {
    }

    /* access modifiers changed from: protected */
    public double getReferenceX() {
        if (!isXAxisBoundsManual() || this.mGraphView.getGridLabelRenderer().isHumanRounding()) {
            return 0.0d;
        }
        if (Double.isNaN(this.referenceX)) {
            this.referenceX = getMinX(false);
        }
        return this.referenceX;
    }

    Viewport(ChartView chartView) {
        this.mScroller = new OverScroller(chartView.getContext());
        this.mEdgeEffectTop = new EdgeEffectCompat(chartView.getContext());
        this.mEdgeEffectBottom = new EdgeEffectCompat(chartView.getContext());
        this.mEdgeEffectLeft = new EdgeEffectCompat(chartView.getContext());
        this.mEdgeEffectRight = new EdgeEffectCompat(chartView.getContext());
        this.mGestureDetector = new GestureDetector(chartView.getContext(), this.mGestureListener);
        this.mScaleGestureDetector = new ScaleGestureDetector(chartView.getContext(), this.mScaleGestureListener);
        this.mGraphView = chartView;
        this.mXAxisBoundsStatus = AxisBoundsStatus.INITIAL;
        this.mYAxisBoundsStatus = AxisBoundsStatus.INITIAL;
        this.mBackgroundColor = 0;
        this.mPaint = new Paint();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mGestureDetector.onTouchEvent(motionEvent) | this.mScaleGestureDetector.onTouchEvent(motionEvent);
    }

    public void setXAxisBoundsStatus(AxisBoundsStatus axisBoundsStatus) {
        this.mXAxisBoundsStatus = axisBoundsStatus;
    }

    public void setYAxisBoundsStatus(AxisBoundsStatus axisBoundsStatus) {
        this.mYAxisBoundsStatus = axisBoundsStatus;
    }

    public boolean isScrollable() {
        return this.mIsScrollable;
    }

    public void setScrollable(boolean z) {
        this.mIsScrollable = z;
    }

    public AxisBoundsStatus getXAxisBoundsStatus() {
        return this.mXAxisBoundsStatus;
    }

    public AxisBoundsStatus getYAxisBoundsStatus() {
        return this.mYAxisBoundsStatus;
    }

    public void calcCompleteRange() {
        List<Series> series = this.mGraphView.getSeries();
        ArrayList<Series> arrayList = new ArrayList<>(this.mGraphView.getSeries());
        this.mCompleteRange.set(0.0d, 0.0d, 0.0d, 0.0d);
        if (!arrayList.isEmpty() && !((Series) arrayList.get(0)).isEmpty()) {
            double lowestValueX = ((Series) arrayList.get(0)).getLowestValueX();
            for (Series series2 : arrayList) {
                if (!series2.isEmpty() && lowestValueX > series2.getLowestValueX()) {
                    lowestValueX = series2.getLowestValueX();
                }
            }
            this.mCompleteRange.left = lowestValueX;
            double highestValueX = ((Series) arrayList.get(0)).getHighestValueX();
            for (Series series3 : arrayList) {
                if (!series3.isEmpty() && highestValueX < series3.getHighestValueX()) {
                    highestValueX = series3.getHighestValueX();
                }
            }
            this.mCompleteRange.right = highestValueX;
            if (!series.isEmpty() && !series.get(0).isEmpty()) {
                double lowestValueY = series.get(0).getLowestValueY();
                for (Series next : series) {
                    if (!next.isEmpty() && lowestValueY > next.getLowestValueY()) {
                        lowestValueY = next.getLowestValueY();
                    }
                }
                this.mCompleteRange.bottom = lowestValueY;
                double highestValueY = series.get(0).getHighestValueY();
                for (Series next2 : series) {
                    if (!next2.isEmpty() && highestValueY < next2.getHighestValueY()) {
                        highestValueY = next2.getHighestValueY();
                    }
                }
                this.mCompleteRange.top = highestValueY;
            }
        }
        if (this.mYAxisBoundsStatus == AxisBoundsStatus.AUTO_ADJUSTED) {
            this.mYAxisBoundsStatus = AxisBoundsStatus.INITIAL;
        }
        if (this.mYAxisBoundsStatus == AxisBoundsStatus.INITIAL) {
            this.mCurrentViewport.top = this.mCompleteRange.top;
            this.mCurrentViewport.bottom = this.mCompleteRange.bottom;
        }
        if (this.mXAxisBoundsStatus == AxisBoundsStatus.AUTO_ADJUSTED) {
            this.mXAxisBoundsStatus = AxisBoundsStatus.INITIAL;
        }
        if (this.mXAxisBoundsStatus == AxisBoundsStatus.INITIAL) {
            this.mCurrentViewport.left = this.mCompleteRange.left;
            this.mCurrentViewport.right = this.mCompleteRange.right;
        } else if (this.mXAxisBoundsManual && !this.mYAxisBoundsManual && this.mCompleteRange.width() != 0.0d) {
            double d = Double.MAX_VALUE;
            for (Series values : series) {
                Iterator values2 = values.getValues(this.mCurrentViewport.left, this.mCurrentViewport.right);
                while (values2.hasNext()) {
                    double y = ((DataPointInterface) values2.next()).getY();
                    if (d > y) {
                        d = y;
                    }
                }
            }
            if (d != Double.MAX_VALUE) {
                this.mCurrentViewport.bottom = d;
            }
            double d2 = Double.MIN_VALUE;
            for (Series values3 : series) {
                Iterator values4 = values3.getValues(this.mCurrentViewport.left, this.mCurrentViewport.right);
                while (values4.hasNext()) {
                    double y2 = ((DataPointInterface) values4.next()).getY();
                    if (d2 < y2) {
                        d2 = y2;
                    }
                }
            }
            if (d2 != Double.MIN_VALUE) {
                this.mCurrentViewport.top = d2;
            }
        }
        if (this.mCurrentViewport.left == this.mCurrentViewport.right) {
            this.mCurrentViewport.right += 1.0d;
        }
        if (this.mCurrentViewport.top == this.mCurrentViewport.bottom) {
            this.mCurrentViewport.top += 1.0d;
        }
    }

    public double getMinX(boolean z) {
        if (z) {
            return this.mCompleteRange.left;
        }
        return this.mCurrentViewport.left;
    }

    public double getMaxX(boolean z) {
        if (z) {
            return this.mCompleteRange.right;
        }
        return this.mCurrentViewport.right;
    }

    public double getMinY(boolean z) {
        if (z) {
            return this.mCompleteRange.bottom;
        }
        return this.mCurrentViewport.bottom;
    }

    public double getMaxY(boolean z) {
        if (z) {
            return this.mCompleteRange.top;
        }
        return this.mCurrentViewport.top;
    }

    public void setMaxY(double d) {
        this.mCurrentViewport.top = d;
    }

    public void setMinY(double d) {
        this.mCurrentViewport.bottom = d;
    }

    public void setMaxX(double d) {
        this.mCurrentViewport.right = d;
    }

    public void setMinX(double d) {
        this.mCurrentViewport.left = d;
    }

    /* access modifiers changed from: private */
    public void releaseEdgeEffects() {
        this.mEdgeEffectLeft.onRelease();
        this.mEdgeEffectRight.onRelease();
        this.mEdgeEffectTop.onRelease();
        this.mEdgeEffectBottom.onRelease();
    }

    private void fling(int i, int i2) {
        releaseEdgeEffects();
        double width = this.mCurrentViewport.width() / this.mCompleteRange.width();
        double graphContentWidth = (double) ((float) this.mGraphView.getGraphContentWidth());
        Double.isNaN(graphContentWidth);
        int graphContentWidth2 = ((int) (width * graphContentWidth)) - this.mGraphView.getGraphContentWidth();
        double height = this.mCurrentViewport.height() / this.mCompleteRange.height();
        double graphContentHeight = (double) ((float) this.mGraphView.getGraphContentHeight());
        Double.isNaN(graphContentHeight);
        int graphContentHeight2 = ((int) (height * graphContentHeight)) - this.mGraphView.getGraphContentHeight();
        int width2 = ((int) ((this.mCurrentViewport.left - this.mCompleteRange.left) / this.mCompleteRange.width())) * graphContentWidth2;
        int height2 = ((int) ((this.mCurrentViewport.top - this.mCompleteRange.top) / this.mCompleteRange.height())) * graphContentHeight2;
        this.mScroller.forceFinished(true);
        this.mScroller.fling(width2, height2, i, 0, 0, graphContentWidth2, 0, graphContentHeight2, this.mGraphView.getGraphContentWidth() / 2, this.mGraphView.getGraphContentHeight() / 2);
        ViewCompat.postInvalidateOnAnimation(this.mGraphView);
    }

    private void drawEdgeEffectsUnclipped(Canvas canvas) {
        boolean z;
        if (!this.mEdgeEffectTop.isFinished()) {
            int save = canvas.save();
            canvas.translate((float) this.mGraphView.getGraphContentLeft(), (float) this.mGraphView.getGraphContentTop());
            this.mEdgeEffectTop.setSize(this.mGraphView.getGraphContentWidth(), this.mGraphView.getGraphContentHeight());
            z = this.mEdgeEffectTop.draw(canvas);
            canvas.restoreToCount(save);
        } else {
            z = false;
        }
        if (!this.mEdgeEffectBottom.isFinished()) {
            int save2 = canvas.save();
            canvas.translate((float) this.mGraphView.getGraphContentLeft(), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()));
            canvas.rotate(180.0f, (float) (this.mGraphView.getGraphContentWidth() / 2), 0.0f);
            this.mEdgeEffectBottom.setSize(this.mGraphView.getGraphContentWidth(), this.mGraphView.getGraphContentHeight());
            if (this.mEdgeEffectBottom.draw(canvas)) {
                z = true;
            }
            canvas.restoreToCount(save2);
        }
        if (!this.mEdgeEffectLeft.isFinished()) {
            int save3 = canvas.save();
            canvas.translate((float) this.mGraphView.getGraphContentLeft(), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()));
            canvas.rotate(-90.0f, 0.0f, 0.0f);
            this.mEdgeEffectLeft.setSize(this.mGraphView.getGraphContentHeight(), this.mGraphView.getGraphContentWidth());
            if (this.mEdgeEffectLeft.draw(canvas)) {
                z = true;
            }
            canvas.restoreToCount(save3);
        }
        if (!this.mEdgeEffectRight.isFinished()) {
            int save4 = canvas.save();
            canvas.translate((float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()), (float) this.mGraphView.getGraphContentTop());
            canvas.rotate(90.0f, 0.0f, 0.0f);
            this.mEdgeEffectRight.setSize(this.mGraphView.getGraphContentHeight(), this.mGraphView.getGraphContentWidth());
            if (this.mEdgeEffectRight.draw(canvas)) {
                z = true;
            }
            canvas.restoreToCount(save4);
        }
        if (z) {
            ViewCompat.postInvalidateOnAnimation(this.mGraphView);
        }
    }

    public void drawFirst(Canvas canvas) {
        Paint paint;
        if (this.mBackgroundColor != 0) {
            this.mPaint.setColor(this.mBackgroundColor);
            canvas.drawRect((float) this.mGraphView.getGraphContentLeft(), (float) this.mGraphView.getGraphContentTop(), (float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), this.mPaint);
        }
        if (this.mDrawBorder) {
            if (this.mBorderPaint != null) {
                paint = this.mBorderPaint;
            } else {
                paint = this.mPaint;
                paint.setColor(getBorderColor());
            }
            Paint paint2 = paint;
            canvas.drawLine((float) this.mGraphView.getGraphContentLeft(), (float) this.mGraphView.getGraphContentTop(), (float) this.mGraphView.getGraphContentLeft(), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), paint2);
            canvas.drawLine((float) this.mGraphView.getGraphContentLeft(), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), (float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), paint2);
        }
    }

    public void draw(Canvas canvas) {
        drawEdgeEffectsUnclipped(canvas);
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
    }

    public boolean isScalable() {
        return this.mIsScalable;
    }

    public void setScalable(boolean z) {
        this.mIsScalable = z;
        if (z) {
            this.mIsScrollable = true;
            setXAxisBoundsManual(true);
        }
    }

    public boolean isXAxisBoundsManual() {
        return this.mXAxisBoundsManual;
    }

    public void setXAxisBoundsManual(boolean z) {
        this.mXAxisBoundsManual = z;
        if (z) {
            this.mXAxisBoundsStatus = AxisBoundsStatus.FIX;
        }
    }

    public boolean isYAxisBoundsManual() {
        return this.mYAxisBoundsManual;
    }

    public void setYAxisBoundsManual(boolean z) {
        this.mYAxisBoundsManual = z;
        if (z) {
            this.mYAxisBoundsStatus = AxisBoundsStatus.FIX;
        }
    }

    public void scrollToEnd() {
        if (this.mXAxisBoundsManual) {
            double width = this.mCurrentViewport.width();
            this.mCurrentViewport.right = this.mCompleteRange.right;
            this.mCurrentViewport.left = this.mCompleteRange.right - width;
            this.mGraphView.onDataChanged(true, false);
            return;
        }
        Log.w("GraphView", "scrollToEnd works only with manual x axis bounds");
    }

    public OnXAxisBoundsChangedListener getOnXAxisBoundsChangedListener() {
        return this.mOnXAxisBoundsChangedListener;
    }

    public void setOnXAxisBoundsChangedListener(OnXAxisBoundsChangedListener onXAxisBoundsChangedListener) {
        this.mOnXAxisBoundsChangedListener = onXAxisBoundsChangedListener;
    }

    public void setDrawBorder(boolean z) {
        this.mDrawBorder = z;
    }

    public int getBorderColor() {
        if (this.mBorderColor != null) {
            return this.mBorderColor.intValue();
        }
        return this.mGraphView.getGridLabelRenderer().getGridColor();
    }

    public void setBorderColor(Integer num) {
        this.mBorderColor = num;
    }

    public void setBorderPaint(Paint paint) {
        this.mBorderPaint = paint;
    }

    public void setScrollableY(boolean z) {
        this.scrollableY = z;
    }

    /* access modifiers changed from: protected */
    public double getReferenceY() {
        if (!isYAxisBoundsManual() || this.mGraphView.getGridLabelRenderer().isHumanRounding()) {
            return 0.0d;
        }
        if (Double.isNaN(this.referenceY)) {
            this.referenceY = getMinY(false);
        }
        return this.referenceY;
    }

    public void setScalableY(boolean z) {
        if (z) {
            this.scrollableY = true;
            setScalable(true);
            if (Build.VERSION.SDK_INT < 11) {
                Log.w("GraphView", "Vertical scaling requires minimum Android 3.0 (API Level 11)");
            }
        }
        this.scalableY = z;
    }

    public double getMaxXAxisSize() {
        return this.mMaxXAxisSize;
    }

    public double getMaxYAxisSize() {
        return this.mMaxYAxisSize;
    }

    public void setMaxXAxisSize(double d) {
        this.mMaxXAxisSize = d;
    }

    public void setMaxYAxisSize(double d) {
        this.mMaxYAxisSize = d;
    }

    public class RectD {
        public double bottom;
        public double left;
        public double right;
        public double top;

        public RectD() {
        }

        public double width() {
            return this.right - this.left;
        }

        public double height() {
            return this.bottom - this.top;
        }

        public void set(double d, double d2, double d3, double d4) {
            this.left = d;
            this.right = d3;
            this.top = d2;
            this.bottom = d4;
        }
    }
}
