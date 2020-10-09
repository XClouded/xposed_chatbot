package com.taobao.weex.analyzer.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class ChartView extends View {
    private boolean isTouchEnabled = true;
    private GridLabelRenderer mGridLabelRenderer;
    private LegendRenderer mLegendRenderer;
    private Paint mPaintTitle;
    private Paint mPreviewPaint;
    private List<Series> mSeries;
    private Styles mStyles;
    private TapDetector mTapDetector;
    private String mTitle;
    private Viewport mViewport;

    private static final class Styles {
        int titleColor;
        float titleTextSize;

        private Styles() {
        }
    }

    private class TapDetector {
        private long lastDown;
        private PointF lastPoint;

        private TapDetector() {
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                this.lastDown = System.currentTimeMillis();
                this.lastPoint = new PointF(motionEvent.getX(), motionEvent.getY());
                return false;
            } else if (this.lastDown <= 0 || motionEvent.getAction() != 2) {
                if (motionEvent.getAction() != 1 || System.currentTimeMillis() - this.lastDown >= 400) {
                    return false;
                }
                return true;
            } else if (Math.abs(motionEvent.getX() - this.lastPoint.x) <= 60.0f && Math.abs(motionEvent.getY() - this.lastPoint.y) <= 60.0f) {
                return false;
            } else {
                this.lastDown = 0;
                return false;
            }
        }
    }

    public ChartView(Context context) {
        super(context);
        init();
    }

    public ChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.mPreviewPaint = new Paint();
        this.mPreviewPaint.setTextAlign(Paint.Align.CENTER);
        this.mPreviewPaint.setColor(-16777216);
        this.mPreviewPaint.setTextSize(50.0f);
        this.mStyles = new Styles();
        this.mViewport = new Viewport(this);
        this.mGridLabelRenderer = new GridLabelRenderer(this);
        this.mLegendRenderer = new LegendRenderer(this);
        this.mSeries = new ArrayList();
        this.mPaintTitle = new Paint();
        this.mTapDetector = new TapDetector();
        loadStyles();
    }

    /* access modifiers changed from: protected */
    public void loadStyles() {
        this.mStyles.titleColor = this.mGridLabelRenderer.getHorizontalLabelsColor();
        this.mStyles.titleTextSize = this.mGridLabelRenderer.getTextSize();
    }

    public GridLabelRenderer getGridLabelRenderer() {
        return this.mGridLabelRenderer;
    }

    public void addSeries(Series series) {
        series.onGraphViewAttached(this);
        this.mSeries.add(series);
        onDataChanged(false, false);
    }

    public List<Series> getSeries() {
        return this.mSeries;
    }

    public void onDataChanged(boolean z, boolean z2) {
        this.mViewport.calcCompleteRange();
        this.mGridLabelRenderer.invalidate(z, z2);
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void drawGraphElements(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= 11 && !canvas.isHardwareAccelerated()) {
            Log.d("ChartView", "use android:hardwareAccelerated=\"true\" for better performance");
        }
        try {
            drawTitle(canvas);
            this.mViewport.drawFirst(canvas);
            this.mGridLabelRenderer.draw(canvas);
            for (Series draw : this.mSeries) {
                draw.draw(this, canvas);
            }
            this.mViewport.draw(canvas);
            this.mLegendRenderer.draw(canvas);
        } catch (Exception e) {
            Log.d("ChartView", e.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            canvas.drawColor(Color.rgb(200, 200, 200));
            canvas.drawText("GraphView: No Preview available", (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2), this.mPreviewPaint);
            return;
        }
        drawGraphElements(canvas);
    }

    /* access modifiers changed from: protected */
    public void drawTitle(Canvas canvas) {
        if (this.mTitle != null && this.mTitle.length() > 0) {
            this.mPaintTitle.setColor(this.mStyles.titleColor);
            this.mPaintTitle.setTextSize(this.mStyles.titleTextSize);
            this.mPaintTitle.setTextAlign(Paint.Align.CENTER);
            float textSize = this.mPaintTitle.getTextSize();
            canvas.drawText(this.mTitle, (float) (canvas.getWidth() / 2), textSize, this.mPaintTitle);
        }
    }

    /* access modifiers changed from: protected */
    public int getTitleHeight() {
        if (this.mTitle == null || this.mTitle.length() <= 0) {
            return 0;
        }
        return (int) this.mPaintTitle.getTextSize();
    }

    public Viewport getViewport() {
        return this.mViewport;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        onDataChanged(false, false);
    }

    public int getGraphContentLeft() {
        return getGridLabelRenderer().getStyles().padding + getGridLabelRenderer().getLabelVerticalWidth() + getGridLabelRenderer().getVerticalAxisTitleWidth();
    }

    public int getGraphContentTop() {
        return getGridLabelRenderer().getStyles().padding + getTitleHeight();
    }

    public int getGraphContentHeight() {
        return (((getHeight() - (getGridLabelRenderer().getStyles().padding * 2)) - getGridLabelRenderer().getLabelHorizontalHeight()) - getTitleHeight()) - getGridLabelRenderer().getHorizontalAxisTitleHeight();
    }

    public int getGraphContentWidth() {
        return (getWidth() - (getGridLabelRenderer().getStyles().padding * 2)) - getGridLabelRenderer().getLabelVerticalWidth();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.isTouchEnabled) {
            return false;
        }
        boolean onTouchEvent = this.mViewport.onTouchEvent(motionEvent);
        boolean onTouchEvent2 = super.onTouchEvent(motionEvent);
        if (this.mTapDetector.onTouchEvent(motionEvent)) {
            for (Series onTap : this.mSeries) {
                onTap.onTap(motionEvent.getX(), motionEvent.getY());
            }
        }
        if (onTouchEvent || onTouchEvent2) {
            return true;
        }
        return false;
    }

    public void setTouchEnabled(boolean z) {
        this.isTouchEnabled = z;
    }

    public boolean isTouchEnabled() {
        return this.isTouchEnabled;
    }

    public void computeScroll() {
        super.computeScroll();
        this.mViewport.computeScroll();
    }

    public LegendRenderer getLegendRenderer() {
        return this.mLegendRenderer;
    }

    public void setLegendRenderer(LegendRenderer legendRenderer) {
        this.mLegendRenderer = legendRenderer;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public float getTitleTextSize() {
        return this.mStyles.titleTextSize;
    }

    public void setTitleTextSize(float f) {
        this.mStyles.titleTextSize = f;
    }

    public int getTitleColor() {
        return this.mStyles.titleColor;
    }

    public void setTitleColor(int i) {
        this.mStyles.titleColor = i;
    }

    public void removeAllSeries() {
        this.mSeries.clear();
        onDataChanged(false, false);
    }

    public void removeSeries(Series<?> series) {
        this.mSeries.remove(series);
        onDataChanged(false, false);
    }
}
