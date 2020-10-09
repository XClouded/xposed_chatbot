package com.taobao.weex.analyzer.view.chart;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import com.facebook.imagepipeline.common.RotationOptions;
import java.util.ArrayList;

public class LegendRenderer {
    private int cachedLegendWidth;
    private final ChartView mGraphView;
    private boolean mIsVisible = false;
    private Paint mPaint = new Paint();
    private Styles mStyles;

    public enum LegendAlign {
        TOP,
        MIDDLE,
        BOTTOM
    }

    private final class Styles {
        LegendAlign align;
        int backgroundColor;
        Point fixedPosition;
        int margin;
        int padding;
        int spacing;
        int textColor;
        float textSize;
        int width;

        private Styles() {
        }
    }

    public LegendRenderer(ChartView chartView) {
        this.mGraphView = chartView;
        this.mPaint.setTextAlign(Paint.Align.LEFT);
        this.mStyles = new Styles();
        this.cachedLegendWidth = 0;
        resetStyles();
    }

    public void resetStyles() {
        this.mStyles.align = LegendAlign.MIDDLE;
        this.mStyles.textSize = this.mGraphView.getGridLabelRenderer().getTextSize();
        this.mStyles.spacing = (int) (this.mStyles.textSize / 5.0f);
        this.mStyles.padding = (int) (this.mStyles.textSize / 2.0f);
        this.mStyles.width = 0;
        this.mStyles.backgroundColor = Color.argb(RotationOptions.ROTATE_180, 100, 100, 100);
        this.mStyles.margin = (int) (this.mStyles.textSize / 5.0f);
        TypedValue typedValue = new TypedValue();
        this.mGraphView.getContext().getTheme().resolveAttribute(16842818, typedValue, true);
        int i = -16777216;
        try {
            TypedArray obtainStyledAttributes = this.mGraphView.getContext().obtainStyledAttributes(typedValue.data, new int[]{16842806});
            int color = obtainStyledAttributes.getColor(0, -16777216);
            obtainStyledAttributes.recycle();
            i = color;
        } catch (Exception unused) {
        }
        this.mStyles.textColor = i;
        this.cachedLegendWidth = 0;
    }

    public void draw(Canvas canvas) {
        float f;
        float f2;
        Canvas canvas2 = canvas;
        if (this.mIsVisible) {
            this.mPaint.setTextSize(this.mStyles.textSize);
            double d = (double) this.mStyles.textSize;
            Double.isNaN(d);
            int i = (int) (d * 0.8d);
            ArrayList<Series> arrayList = new ArrayList<>();
            arrayList.addAll(this.mGraphView.getSeries());
            int i2 = this.mStyles.width;
            int i3 = 0;
            if (i2 == 0 && (i2 = this.cachedLegendWidth) == 0) {
                Rect rect = new Rect();
                for (Series series : arrayList) {
                    if (series.getTitle() != null) {
                        this.mPaint.getTextBounds(series.getTitle(), 0, series.getTitle().length(), rect);
                        i2 = Math.max(i2, rect.width());
                    }
                }
                if (i2 == 0) {
                    i2 = 1;
                }
                i2 += (this.mStyles.padding * 2) + i + this.mStyles.spacing;
                this.cachedLegendWidth = i2;
            }
            float size = ((this.mStyles.textSize + ((float) this.mStyles.spacing)) * ((float) arrayList.size())) - ((float) this.mStyles.spacing);
            if (this.mStyles.fixedPosition == null) {
                f2 = (float) (((this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()) - i2) - this.mStyles.margin);
                switch (this.mStyles.align) {
                    case TOP:
                        f = (float) (this.mGraphView.getGraphContentTop() + this.mStyles.margin);
                        break;
                    case MIDDLE:
                        f = ((float) (this.mGraphView.getHeight() / 2)) - (size / 2.0f);
                        break;
                    default:
                        f = (((float) ((this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()) - this.mStyles.margin)) - size) - ((float) (this.mStyles.padding * 2));
                        break;
                }
            } else {
                f2 = (float) (this.mGraphView.getGraphContentLeft() + this.mStyles.margin + this.mStyles.fixedPosition.x);
                f = (float) (this.mGraphView.getGraphContentTop() + this.mStyles.margin + this.mStyles.fixedPosition.y);
            }
            float f3 = size + f + ((float) (this.mStyles.padding * 2));
            this.mPaint.setColor(this.mStyles.backgroundColor);
            canvas2.drawRoundRect(new RectF(f2, f, ((float) i2) + f2, f3), 8.0f, 8.0f, this.mPaint);
            for (Series series2 : arrayList) {
                this.mPaint.setColor(series2.getColor());
                float f4 = (float) i3;
                float f5 = (float) i;
                int i4 = i;
                canvas2.drawRect(new RectF(((float) this.mStyles.padding) + f2, ((float) this.mStyles.padding) + f + ((this.mStyles.textSize + ((float) this.mStyles.spacing)) * f4), ((float) this.mStyles.padding) + f2 + f5, ((float) this.mStyles.padding) + f + ((this.mStyles.textSize + ((float) this.mStyles.spacing)) * f4) + f5), this.mPaint);
                if (series2.getTitle() != null) {
                    this.mPaint.setColor(this.mStyles.textColor);
                    canvas2.drawText(series2.getTitle(), ((float) this.mStyles.padding) + f2 + f5 + ((float) this.mStyles.spacing), ((float) this.mStyles.padding) + f + this.mStyles.textSize + (f4 * (this.mStyles.textSize + ((float) this.mStyles.spacing))), this.mPaint);
                }
                i3++;
                i = i4;
            }
        }
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public void setVisible(boolean z) {
        this.mIsVisible = z;
    }

    public float getTextSize() {
        return this.mStyles.textSize;
    }

    public void setTextSize(float f) {
        this.mStyles.textSize = f;
        this.cachedLegendWidth = 0;
    }

    public int getSpacing() {
        return this.mStyles.spacing;
    }

    public void setSpacing(int i) {
        this.mStyles.spacing = i;
    }

    public int getPadding() {
        return this.mStyles.padding;
    }

    public void setPadding(int i) {
        this.mStyles.padding = i;
    }

    public int getWidth() {
        return this.mStyles.width;
    }

    public void setWidth(int i) {
        this.mStyles.width = i;
    }

    public int getBackgroundColor() {
        return this.mStyles.backgroundColor;
    }

    public void setBackgroundColor(int i) {
        this.mStyles.backgroundColor = i;
    }

    public int getMargin() {
        return this.mStyles.margin;
    }

    public void setMargin(int i) {
        this.mStyles.margin = i;
    }

    public LegendAlign getAlign() {
        return this.mStyles.align;
    }

    public void setAlign(LegendAlign legendAlign) {
        this.mStyles.align = legendAlign;
    }

    public int getTextColor() {
        return this.mStyles.textColor;
    }

    public void setTextColor(int i) {
        this.mStyles.textColor = i;
    }

    public void setFixedPosition(int i, int i2) {
        this.mStyles.fixedPosition = new Point(i, i2);
    }
}
