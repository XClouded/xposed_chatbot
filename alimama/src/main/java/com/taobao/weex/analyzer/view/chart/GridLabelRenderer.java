package com.taobao.weex.analyzer.view.chart;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import com.taobao.weex.analyzer.view.chart.Viewport;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GridLabelRenderer {
    private final ChartView mGraphView;
    private String mHorizontalAxisTitle;
    private boolean mHumanRounding;
    protected boolean mIsAdjusted;
    private LabelFormatter mLabelFormatter;
    private Integer mLabelHorizontalHeight;
    private boolean mLabelHorizontalHeightFixed;
    private Integer mLabelHorizontalWidth;
    private Integer mLabelVerticalHeight;
    private Integer mLabelVerticalWidth;
    private boolean mLabelVerticalWidthFixed;
    private int mNumHorizontalLabels;
    private int mNumVerticalLabels;
    private Paint mPaintAxisTitle;
    private Paint mPaintLabel;
    private Paint mPaintLine;
    private Map<Integer, Double> mStepsHorizontal;
    private Map<Integer, Double> mStepsVertical;
    protected Styles mStyles = new Styles();
    private String mVerticalAxisTitle;

    public enum VerticalLabelsVAlign {
        ABOVE,
        MID,
        BELOW
    }

    public final class Styles {
        public int gridColor;
        GridStyle gridStyle;
        public boolean highlightZeroLines;
        public int horizontalAxisTitleColor;
        public float horizontalAxisTitleTextSize;
        public float horizontalLabelsAngle;
        public int horizontalLabelsColor;
        boolean horizontalLabelsVisible;
        int labelsSpace;
        public int padding;
        public float textSize;
        public int verticalAxisTitleColor;
        public float verticalAxisTitleTextSize;
        public Paint.Align verticalLabelsAlign;
        public int verticalLabelsColor;
        public Paint.Align verticalLabelsSecondScaleAlign;
        public int verticalLabelsSecondScaleColor;
        VerticalLabelsVAlign verticalLabelsVAlign = VerticalLabelsVAlign.MID;
        boolean verticalLabelsVisible;

        public Styles() {
        }
    }

    public enum GridStyle {
        BOTH,
        VERTICAL,
        HORIZONTAL,
        NONE;

        public boolean drawVertical() {
            return this == BOTH || (this == VERTICAL && this != NONE);
        }

        public boolean drawHorizontal() {
            return this == BOTH || (this == HORIZONTAL && this != NONE);
        }
    }

    public GridLabelRenderer(ChartView chartView) {
        this.mGraphView = chartView;
        setLabelFormatter(new DefaultLabelFormatter());
        resetStyles();
        this.mNumVerticalLabels = 5;
        this.mNumHorizontalLabels = 5;
        this.mHumanRounding = true;
    }

    public void resetStyles() {
        int i;
        TypedValue typedValue = new TypedValue();
        this.mGraphView.getContext().getTheme().resolveAttribute(16842818, typedValue, true);
        int i2 = -7829368;
        int i3 = -16777216;
        int i4 = 20;
        try {
            TypedArray obtainStyledAttributes = this.mGraphView.getContext().obtainStyledAttributes(typedValue.data, new int[]{16842806, 16842808, 16842901, 16843327});
            int color = obtainStyledAttributes.getColor(0, -16777216);
            int color2 = obtainStyledAttributes.getColor(1, -7829368);
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(2, 20);
            i = obtainStyledAttributes.getDimensionPixelSize(3, 20);
            obtainStyledAttributes.recycle();
            i3 = color;
            i2 = color2;
            i4 = dimensionPixelSize;
        } catch (Exception unused) {
            i = 20;
        }
        this.mStyles.verticalLabelsColor = i3;
        this.mStyles.verticalLabelsSecondScaleColor = i3;
        this.mStyles.horizontalLabelsColor = i3;
        this.mStyles.gridColor = i2;
        this.mStyles.textSize = (float) i4;
        this.mStyles.padding = i;
        this.mStyles.labelsSpace = ((int) this.mStyles.textSize) / 5;
        this.mStyles.verticalLabelsAlign = Paint.Align.RIGHT;
        this.mStyles.verticalLabelsSecondScaleAlign = Paint.Align.LEFT;
        this.mStyles.highlightZeroLines = true;
        this.mStyles.verticalAxisTitleColor = this.mStyles.verticalLabelsColor;
        this.mStyles.horizontalAxisTitleColor = this.mStyles.horizontalLabelsColor;
        this.mStyles.verticalAxisTitleTextSize = this.mStyles.textSize;
        this.mStyles.horizontalAxisTitleTextSize = this.mStyles.textSize;
        this.mStyles.horizontalLabelsVisible = true;
        this.mStyles.verticalLabelsVisible = true;
        this.mStyles.horizontalLabelsAngle = 0.0f;
        this.mStyles.gridStyle = GridStyle.BOTH;
        reloadStyles();
    }

    public void reloadStyles() {
        this.mPaintLine = new Paint();
        this.mPaintLine.setColor(this.mStyles.gridColor);
        this.mPaintLine.setStrokeWidth(0.0f);
        this.mPaintLabel = new Paint();
        this.mPaintLabel.setTextSize(getTextSize());
        this.mPaintLabel.setAntiAlias(true);
        this.mPaintAxisTitle = new Paint();
        this.mPaintAxisTitle.setTextSize(getTextSize());
        this.mPaintAxisTitle.setTextAlign(Paint.Align.CENTER);
    }

    public boolean isHumanRounding() {
        return this.mHumanRounding;
    }

    public void setHumanRounding(boolean z) {
        this.mHumanRounding = z;
    }

    public float getTextSize() {
        return this.mStyles.textSize;
    }

    public int getVerticalLabelsColor() {
        return this.mStyles.verticalLabelsColor;
    }

    public Paint.Align getVerticalLabelsAlign() {
        return this.mStyles.verticalLabelsAlign;
    }

    public int getHorizontalLabelsColor() {
        return this.mStyles.horizontalLabelsColor;
    }

    public float getHorizontalLabelsAngle() {
        return this.mStyles.horizontalLabelsAngle;
    }

    public void invalidate(boolean z, boolean z2) {
        if (!z2) {
            this.mIsAdjusted = false;
        }
        if (!z) {
            if (!this.mLabelVerticalWidthFixed) {
                this.mLabelVerticalWidth = null;
            }
            this.mLabelVerticalHeight = null;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0117  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0140  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean adjustVertical(boolean r26) {
        /*
            r25 = this;
            r0 = r25
            r1 = r26
            java.lang.Integer r2 = r0.mLabelHorizontalHeight
            r3 = 0
            if (r2 != 0) goto L_0x000a
            return r3
        L_0x000a:
            com.taobao.weex.analyzer.view.chart.ChartView r2 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r2 = r2.getViewport()
            double r4 = r2.getMinY(r3)
            com.taobao.weex.analyzer.view.chart.ChartView r2 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r2 = r2.getViewport()
            double r6 = r2.getMaxY(r3)
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 != 0) goto L_0x0023
            return r3
        L_0x0023:
            int r2 = r0.mNumVerticalLabels
            double r8 = r6 - r4
            int r10 = r2 + -1
            double r10 = (double) r10
            java.lang.Double.isNaN(r10)
            double r12 = r8 / r10
            r14 = 4696837146684686336(0x412e848000000000, double:1000000.0)
            double r12 = r12 * r14
            long r12 = java.lang.Math.round(r12)
            double r12 = (double) r12
            java.lang.Double.isNaN(r12)
            double r12 = r12 / r14
            boolean r14 = r25.isHumanRounding()
            r15 = 1
            if (r14 == 0) goto L_0x004e
            double r12 = r0.humanRound(r12, r1)
        L_0x004a:
            r23 = r4
            goto L_0x00c3
        L_0x004e:
            java.util.Map<java.lang.Integer, java.lang.Double> r14 = r0.mStepsVertical
            if (r14 == 0) goto L_0x004a
            java.util.Map<java.lang.Integer, java.lang.Double> r14 = r0.mStepsVertical
            int r14 = r14.size()
            if (r14 <= r15) goto L_0x004a
            java.util.Map<java.lang.Integer, java.lang.Double> r14 = r0.mStepsVertical
            java.util.Collection r14 = r14.values()
            java.util.Iterator r14 = r14.iterator()
            r16 = 0
            r19 = r16
            r18 = 0
        L_0x006a:
            boolean r21 = r14.hasNext()
            if (r21 == 0) goto L_0x0084
            java.lang.Object r21 = r14.next()
            java.lang.Double r21 = (java.lang.Double) r21
            if (r18 != 0) goto L_0x007f
            double r19 = r21.doubleValue()
            int r18 = r18 + 1
            goto L_0x006a
        L_0x007f:
            double r21 = r21.doubleValue()
            goto L_0x0086
        L_0x0084:
            r21 = r16
        L_0x0086:
            r14 = 0
            double r18 = r21 - r19
            int r14 = (r18 > r16 ? 1 : (r18 == r16 ? 0 : -1))
            if (r14 <= 0) goto L_0x004a
            r16 = 4611686018427387904(0x4000000000000000, double:2.0)
            r20 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            int r14 = (r18 > r12 ? 1 : (r18 == r12 ? 0 : -1))
            if (r14 <= 0) goto L_0x009a
            double r12 = r18 / r16
            r23 = r4
            goto L_0x00a9
        L_0x009a:
            int r14 = (r18 > r12 ? 1 : (r18 == r12 ? 0 : -1))
            if (r14 >= 0) goto L_0x00a5
            double r16 = r16 * r18
            r23 = r4
            r12 = r16
            goto L_0x00a9
        L_0x00a5:
            r23 = r4
            r12 = r20
        L_0x00a9:
            double r3 = r8 / r18
            int r3 = (int) r3
            double r8 = r8 / r12
            int r4 = (int) r8
            if (r3 > r2) goto L_0x00b7
            if (r4 > r2) goto L_0x00b7
            if (r4 <= r3) goto L_0x00b5
            goto L_0x00b7
        L_0x00b5:
            r3 = 0
            goto L_0x00b8
        L_0x00b7:
            r3 = 1
        L_0x00b8:
            int r5 = (r12 > r20 ? 1 : (r12 == r20 ? 0 : -1))
            if (r5 == 0) goto L_0x00c1
            if (r3 == 0) goto L_0x00c1
            if (r4 > r2) goto L_0x00c1
            goto L_0x00c3
        L_0x00c1:
            r12 = r18
        L_0x00c3:
            com.taobao.weex.analyzer.view.chart.ChartView r2 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r2 = r2.getViewport()
            double r2 = r2.getReferenceY()
            double r4 = r23 - r2
            double r4 = r4 / r12
            double r4 = java.lang.Math.floor(r4)
            double r4 = r4 * r12
            double r4 = r4 + r2
            if (r1 == 0) goto L_0x00ff
            com.taobao.weex.analyzer.view.chart.ChartView r1 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r1 = r1.getViewport()
            r1.setMinY(r4)
            com.taobao.weex.analyzer.view.chart.ChartView r1 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r1 = r1.getViewport()
            java.lang.Double.isNaN(r10)
            double r10 = r10 * r12
            double r10 = r10 + r4
            double r2 = java.lang.Math.max(r6, r10)
            r1.setMaxY(r2)
            com.taobao.weex.analyzer.view.chart.ChartView r1 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r1 = r1.getViewport()
            com.taobao.weex.analyzer.view.chart.Viewport$AxisBoundsStatus r2 = com.taobao.weex.analyzer.view.chart.Viewport.AxisBoundsStatus.AUTO_ADJUSTED
            r1.mYAxisBoundsStatus = r2
        L_0x00ff:
            com.taobao.weex.analyzer.view.chart.ChartView r1 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r1 = r1.getViewport()
            com.taobao.weex.analyzer.view.chart.Viewport$RectD r1 = r1.mCurrentViewport
            double r1 = r1.height()
            r6 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            double r1 = r1 * r6
            double r1 = r1 / r12
            int r1 = (int) r1
            int r1 = r1 + 2
            java.util.Map<java.lang.Integer, java.lang.Double> r2 = r0.mStepsVertical
            if (r2 == 0) goto L_0x011d
            java.util.Map<java.lang.Integer, java.lang.Double> r2 = r0.mStepsVertical
            r2.clear()
            goto L_0x0124
        L_0x011d:
            java.util.LinkedHashMap r2 = new java.util.LinkedHashMap
            r2.<init>(r1)
            r0.mStepsVertical = r2
        L_0x0124:
            com.taobao.weex.analyzer.view.chart.ChartView r2 = r0.mGraphView
            int r2 = r2.getGraphContentHeight()
            double r2 = (double) r2
            com.taobao.weex.analyzer.view.chart.ChartView r8 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r8 = r8.getViewport()
            com.taobao.weex.analyzer.view.chart.Viewport$RectD r8 = r8.mCurrentViewport
            double r8 = r8.height()
            java.lang.Double.isNaN(r2)
            double r2 = r2 / r8
            double r2 = r2 * r6
            r6 = 0
        L_0x013e:
            if (r6 >= r1) goto L_0x0184
            double r7 = (double) r6
            java.lang.Double.isNaN(r7)
            double r7 = r7 * r12
            double r7 = r7 + r4
            com.taobao.weex.analyzer.view.chart.ChartView r9 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r9 = r9.getViewport()
            com.taobao.weex.analyzer.view.chart.Viewport$RectD r9 = r9.mCurrentViewport
            double r9 = r9.top
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 <= 0) goto L_0x0156
            goto L_0x0181
        L_0x0156:
            com.taobao.weex.analyzer.view.chart.ChartView r9 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r9 = r9.getViewport()
            com.taobao.weex.analyzer.view.chart.Viewport$RectD r9 = r9.mCurrentViewport
            double r9 = r9.bottom
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 >= 0) goto L_0x0165
            goto L_0x0181
        L_0x0165:
            com.taobao.weex.analyzer.view.chart.ChartView r9 = r0.mGraphView
            com.taobao.weex.analyzer.view.chart.Viewport r9 = r9.getViewport()
            com.taobao.weex.analyzer.view.chart.Viewport$RectD r9 = r9.mCurrentViewport
            double r9 = r9.bottom
            double r9 = r7 - r9
            double r9 = r9 * r2
            java.util.Map<java.lang.Integer, java.lang.Double> r11 = r0.mStepsVertical
            int r9 = (int) r9
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Double r7 = java.lang.Double.valueOf(r7)
            r11.put(r9, r7)
        L_0x0181:
            int r6 = r6 + 1
            goto L_0x013e
        L_0x0184:
            return r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.view.chart.GridLabelRenderer.adjustVertical(boolean):boolean");
    }

    /* access modifiers changed from: protected */
    public boolean adjustHorizontal(boolean z) {
        double d;
        if (this.mLabelVerticalWidth == null) {
            return false;
        }
        double minX = this.mGraphView.getViewport().getMinX(false);
        double maxX = this.mGraphView.getViewport().getMaxX(false);
        if (minX == maxX) {
            return false;
        }
        int i = this.mNumHorizontalLabels;
        double d2 = maxX - minX;
        double d3 = (double) (i - 1);
        Double.isNaN(d3);
        double round = (double) Math.round((d2 / d3) * 1000000.0d);
        Double.isNaN(round);
        double d4 = round / 1000000.0d;
        if (isHumanRounding()) {
            d4 = humanRound(d4, false);
        } else if (this.mStepsHorizontal != null && this.mStepsHorizontal.size() > 1) {
            Iterator<Double> it = this.mStepsHorizontal.values().iterator();
            double d5 = 0.0d;
            int i2 = 0;
            while (true) {
                if (!it.hasNext()) {
                    d = 0.0d;
                    break;
                }
                Double next = it.next();
                if (i2 != 0) {
                    d = next.doubleValue();
                    break;
                }
                d5 = next.doubleValue();
                i2++;
            }
            double d6 = d - d5;
            if (d6 > 0.0d) {
                d4 = d6 > d4 ? d6 / 2.0d : d6 < d4 ? 2.0d * d6 : Double.NaN;
                int i3 = (int) (d2 / d6);
                int i4 = (int) (d2 / d4);
                boolean z2 = i3 > i || i4 > i || i4 > i3;
                if (d4 == Double.NaN || !z2 || i4 > i) {
                    d4 = d6;
                }
            }
        }
        double referenceX = this.mGraphView.getViewport().getReferenceX();
        double floor = (Math.floor((minX - referenceX) / d4) * d4) + referenceX;
        if (z) {
            this.mGraphView.getViewport().setMinX(floor);
            Viewport viewport = this.mGraphView.getViewport();
            Double.isNaN(d3);
            viewport.setMaxX((d3 * d4) + floor);
            this.mGraphView.getViewport().mXAxisBoundsStatus = Viewport.AxisBoundsStatus.AUTO_ADJUSTED;
        }
        int width = ((int) (this.mGraphView.getViewport().mCurrentViewport.width() / d4)) + 1;
        if (this.mStepsHorizontal != null) {
            this.mStepsHorizontal.clear();
        } else {
            this.mStepsHorizontal = new LinkedHashMap(width);
        }
        double graphContentWidth = (double) this.mGraphView.getGraphContentWidth();
        double width2 = this.mGraphView.getViewport().mCurrentViewport.width();
        Double.isNaN(graphContentWidth);
        double d7 = graphContentWidth / width2;
        for (int i5 = 0; i5 < width; i5++) {
            double d8 = (double) i5;
            Double.isNaN(d8);
            double d9 = (d8 * d4) + floor;
            if (d9 >= this.mGraphView.getViewport().mCurrentViewport.left) {
                this.mStepsHorizontal.put(Integer.valueOf((int) ((d9 - this.mGraphView.getViewport().mCurrentViewport.left) * d7)), Double.valueOf(d9));
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void adjustSteps() {
        this.mIsAdjusted = adjustVertical(!Viewport.AxisBoundsStatus.FIX.equals(this.mGraphView.getViewport().mYAxisBoundsStatus));
        this.mIsAdjusted &= adjustHorizontal(!Viewport.AxisBoundsStatus.FIX.equals(this.mGraphView.getViewport().mXAxisBoundsStatus));
    }

    /* access modifiers changed from: protected */
    public void calcLabelVerticalSize(Canvas canvas) {
        String formatLabel = this.mLabelFormatter.formatLabel(this.mGraphView.getViewport().getMaxY(false), false);
        if (formatLabel == null) {
            formatLabel = "";
        }
        Rect rect = new Rect();
        this.mPaintLabel.getTextBounds(formatLabel, 0, formatLabel.length(), rect);
        this.mLabelVerticalWidth = Integer.valueOf(rect.width());
        this.mLabelVerticalHeight = Integer.valueOf(rect.height());
        String formatLabel2 = this.mLabelFormatter.formatLabel(this.mGraphView.getViewport().getMinY(false), false);
        if (formatLabel2 == null) {
            formatLabel2 = "";
        }
        this.mPaintLabel.getTextBounds(formatLabel2, 0, formatLabel2.length(), rect);
        this.mLabelVerticalWidth = Integer.valueOf(Math.max(this.mLabelVerticalWidth.intValue(), rect.width()));
        this.mLabelVerticalWidth = Integer.valueOf(this.mLabelVerticalWidth.intValue() + 6);
        this.mLabelVerticalWidth = Integer.valueOf(this.mLabelVerticalWidth.intValue() + this.mStyles.labelsSpace);
        int i = 1;
        for (byte b : formatLabel2.getBytes()) {
            if (b == 10) {
                i++;
            }
        }
        this.mLabelVerticalHeight = Integer.valueOf(this.mLabelVerticalHeight.intValue() * i);
    }

    /* access modifiers changed from: protected */
    public void calcLabelHorizontalSize(Canvas canvas) {
        int i = 1;
        String formatLabel = this.mLabelFormatter.formatLabel(((this.mGraphView.getViewport().getMaxX(false) - this.mGraphView.getViewport().getMinX(false)) * 0.783d) + this.mGraphView.getViewport().getMinX(false), true);
        if (formatLabel == null) {
            formatLabel = "";
        }
        Rect rect = new Rect();
        this.mPaintLabel.getTextBounds(formatLabel, 0, formatLabel.length(), rect);
        this.mLabelHorizontalWidth = Integer.valueOf(rect.width());
        if (!this.mLabelHorizontalHeightFixed) {
            this.mLabelHorizontalHeight = Integer.valueOf(rect.height());
            for (byte b : formatLabel.getBytes()) {
                if (b == 10) {
                    i++;
                }
            }
            this.mLabelHorizontalHeight = Integer.valueOf(this.mLabelHorizontalHeight.intValue() * i);
            this.mLabelHorizontalHeight = Integer.valueOf((int) Math.max((float) this.mLabelHorizontalHeight.intValue(), this.mStyles.textSize));
        }
        if (this.mStyles.horizontalLabelsAngle > 0.0f && this.mStyles.horizontalLabelsAngle <= 180.0f) {
            double intValue = (double) this.mLabelHorizontalHeight.intValue();
            double cos = Math.cos(Math.toRadians((double) this.mStyles.horizontalLabelsAngle));
            Double.isNaN(intValue);
            int round = (int) Math.round(Math.abs(intValue * cos));
            double intValue2 = (double) this.mLabelHorizontalWidth.intValue();
            double sin = Math.sin(Math.toRadians((double) this.mStyles.horizontalLabelsAngle));
            Double.isNaN(intValue2);
            double intValue3 = (double) this.mLabelHorizontalHeight.intValue();
            double sin2 = Math.sin(Math.toRadians((double) this.mStyles.horizontalLabelsAngle));
            Double.isNaN(intValue3);
            double intValue4 = (double) this.mLabelHorizontalWidth.intValue();
            double cos2 = Math.cos(Math.toRadians((double) this.mStyles.horizontalLabelsAngle));
            Double.isNaN(intValue4);
            this.mLabelHorizontalHeight = Integer.valueOf(round + ((int) Math.round(Math.abs(intValue2 * sin))));
            this.mLabelHorizontalWidth = Integer.valueOf(((int) Math.round(Math.abs(intValue3 * sin2))) + ((int) Math.round(Math.abs(intValue4 * cos2))));
        }
        this.mLabelHorizontalHeight = Integer.valueOf(this.mLabelHorizontalHeight.intValue() + this.mStyles.labelsSpace);
    }

    public void draw(Canvas canvas) {
        boolean z;
        if (this.mLabelHorizontalWidth == null) {
            calcLabelHorizontalSize(canvas);
            z = true;
        } else {
            z = false;
        }
        if (this.mLabelVerticalWidth == null) {
            calcLabelVerticalSize(canvas);
            z = true;
        }
        if (z) {
            this.mGraphView.drawGraphElements(canvas);
            return;
        }
        if (!this.mIsAdjusted) {
            adjustSteps();
        }
        if (this.mIsAdjusted) {
            drawVerticalSteps(canvas);
            drawHorizontalSteps(canvas);
            drawHorizontalAxisTitle(canvas);
            drawVerticalAxisTitle(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void drawHorizontalAxisTitle(Canvas canvas) {
        if (this.mHorizontalAxisTitle != null && this.mHorizontalAxisTitle.length() > 0) {
            this.mPaintAxisTitle.setColor(getHorizontalAxisTitleColor());
            this.mPaintAxisTitle.setTextSize(getHorizontalAxisTitleTextSize());
            canvas.drawText(this.mHorizontalAxisTitle, (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() - this.mStyles.padding), this.mPaintAxisTitle);
        }
    }

    /* access modifiers changed from: protected */
    public void drawVerticalAxisTitle(Canvas canvas) {
        if (this.mVerticalAxisTitle != null && this.mVerticalAxisTitle.length() > 0) {
            this.mPaintAxisTitle.setColor(getVerticalAxisTitleColor());
            this.mPaintAxisTitle.setTextSize(getVerticalAxisTitleTextSize());
            float verticalAxisTitleWidth = (float) getVerticalAxisTitleWidth();
            float height = (float) (canvas.getHeight() / 2);
            canvas.save();
            canvas.rotate(-90.0f, verticalAxisTitleWidth, height);
            canvas.drawText(this.mVerticalAxisTitle, verticalAxisTitleWidth, height, this.mPaintAxisTitle);
            canvas.restore();
        }
    }

    public int getHorizontalAxisTitleHeight() {
        if (this.mHorizontalAxisTitle == null || this.mHorizontalAxisTitle.length() <= 0) {
            return 0;
        }
        return (int) getHorizontalAxisTitleTextSize();
    }

    public int getVerticalAxisTitleWidth() {
        if (this.mVerticalAxisTitle == null || this.mVerticalAxisTitle.length() <= 0) {
            return 0;
        }
        return (int) getVerticalAxisTitleTextSize();
    }

    /* access modifiers changed from: protected */
    public void drawHorizontalSteps(Canvas canvas) {
        int i;
        Canvas canvas2 = canvas;
        this.mPaintLabel.setColor(getHorizontalLabelsColor());
        int i2 = 0;
        for (Map.Entry next : this.mStepsHorizontal.entrySet()) {
            if (this.mStyles.highlightZeroLines) {
                if (((Double) next.getValue()).doubleValue() == 0.0d) {
                    this.mPaintLine.setStrokeWidth(5.0f);
                } else {
                    this.mPaintLine.setStrokeWidth(0.0f);
                }
            }
            if (this.mStyles.gridStyle.drawVertical() && ((Integer) next.getKey()).intValue() <= this.mGraphView.getGraphContentWidth()) {
                canvas.drawLine((float) (this.mGraphView.getGraphContentLeft() + ((Integer) next.getKey()).intValue()), (float) this.mGraphView.getGraphContentTop(), (float) (this.mGraphView.getGraphContentLeft() + ((Integer) next.getKey()).intValue()), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), this.mPaintLine);
            }
            if (isHorizontalLabelsVisible()) {
                float f = 90.0f;
                if (this.mStyles.horizontalLabelsAngle <= 0.0f || this.mStyles.horizontalLabelsAngle > 180.0f) {
                    this.mPaintLabel.setTextAlign(Paint.Align.CENTER);
                    if (i2 == this.mStepsHorizontal.size() - 1) {
                        this.mPaintLabel.setTextAlign(Paint.Align.RIGHT);
                    }
                    if (i2 == 0) {
                        this.mPaintLabel.setTextAlign(Paint.Align.LEFT);
                    }
                } else if (this.mStyles.horizontalLabelsAngle < 90.0f) {
                    this.mPaintLabel.setTextAlign(Paint.Align.RIGHT);
                } else if (this.mStyles.horizontalLabelsAngle <= 180.0f) {
                    this.mPaintLabel.setTextAlign(Paint.Align.LEFT);
                }
                String formatLabel = this.mLabelFormatter.formatLabel(((Double) next.getValue()).doubleValue(), true);
                if (formatLabel == null) {
                    formatLabel = "";
                }
                String[] split = formatLabel.split("\n");
                if (this.mStyles.horizontalLabelsAngle <= 0.0f || this.mStyles.horizontalLabelsAngle > 180.0f) {
                    i = 0;
                } else {
                    Rect rect = new Rect();
                    this.mPaintLabel.getTextBounds(split[0], 0, split[0].length(), rect);
                    double width = (double) rect.width();
                    double cos = Math.cos(Math.toRadians((double) this.mStyles.horizontalLabelsAngle));
                    Double.isNaN(width);
                    i = (int) Math.abs(width * cos);
                }
                int i3 = 0;
                while (i3 < split.length) {
                    float height = (((float) ((canvas.getHeight() - this.mStyles.padding) - getHorizontalAxisTitleHeight())) - ((((float) ((split.length - i3) - 1)) * getTextSize()) * 1.1f)) + ((float) this.mStyles.labelsSpace);
                    float graphContentLeft = (float) (this.mGraphView.getGraphContentLeft() + ((Integer) next.getKey()).intValue());
                    if (this.mStyles.horizontalLabelsAngle > 0.0f && this.mStyles.horizontalLabelsAngle < f) {
                        canvas.save();
                        float f2 = graphContentLeft + ((float) i);
                        canvas2.rotate(this.mStyles.horizontalLabelsAngle, f2, height);
                        canvas2.drawText(split[i3], f2, height, this.mPaintLabel);
                        canvas.restore();
                    } else if (this.mStyles.horizontalLabelsAngle <= 0.0f || this.mStyles.horizontalLabelsAngle > 180.0f) {
                        canvas2.drawText(split[i3], graphContentLeft, height, this.mPaintLabel);
                    } else {
                        canvas.save();
                        float f3 = graphContentLeft - ((float) i);
                        canvas2.rotate(this.mStyles.horizontalLabelsAngle - 180.0f, f3, height);
                        canvas2.drawText(split[i3], f3, height, this.mPaintLabel);
                        canvas.restore();
                    }
                    i3++;
                    f = 90.0f;
                }
            }
            i2++;
        }
    }

    /* access modifiers changed from: protected */
    public void drawVerticalSteps(Canvas canvas) {
        int i;
        float graphContentLeft = (float) this.mGraphView.getGraphContentLeft();
        this.mPaintLabel.setColor(getVerticalLabelsColor());
        this.mPaintLabel.setTextAlign(getVerticalLabelsAlign());
        int size = this.mStepsVertical.size();
        int i2 = 1;
        for (Map.Entry next : this.mStepsVertical.entrySet()) {
            float graphContentTop = (float) ((this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()) - ((Integer) next.getKey()).intValue());
            if (this.mStyles.highlightZeroLines) {
                if (((Double) next.getValue()).doubleValue() == 0.0d) {
                    this.mPaintLine.setStrokeWidth(5.0f);
                } else {
                    this.mPaintLine.setStrokeWidth(0.0f);
                }
            }
            if (this.mStyles.gridStyle.drawHorizontal()) {
                canvas.drawLine(graphContentLeft, graphContentTop, graphContentLeft + ((float) this.mGraphView.getGraphContentWidth()), graphContentTop, this.mPaintLine);
            }
            boolean z = ((this.mStyles.verticalLabelsVAlign == VerticalLabelsVAlign.ABOVE && i2 == 1) || (this.mStyles.verticalLabelsVAlign == VerticalLabelsVAlign.BELOW && i2 == size)) ? false : true;
            if (isVerticalLabelsVisible() && z) {
                int intValue = this.mLabelVerticalWidth.intValue();
                if (getVerticalLabelsAlign() == Paint.Align.RIGHT) {
                    i = intValue - this.mStyles.labelsSpace;
                } else {
                    i = getVerticalLabelsAlign() == Paint.Align.CENTER ? intValue / 2 : 0;
                }
                int verticalAxisTitleWidth = i + this.mStyles.padding + getVerticalAxisTitleWidth();
                String formatLabel = this.mLabelFormatter.formatLabel(((Double) next.getValue()).doubleValue(), false);
                if (formatLabel == null) {
                    formatLabel = "";
                }
                String[] split = formatLabel.split("\n");
                switch (this.mStyles.verticalLabelsVAlign) {
                    case MID:
                        graphContentTop += ((((float) split.length) * getTextSize()) * 1.1f) / 2.0f;
                        break;
                    case ABOVE:
                        graphContentTop -= 5.0f;
                        break;
                    case BELOW:
                        graphContentTop += (((float) split.length) * getTextSize() * 1.1f) + 5.0f;
                        break;
                }
                for (int i3 = 0; i3 < split.length; i3++) {
                    canvas.drawText(split[i3], (float) verticalAxisTitleWidth, graphContentTop - ((((float) ((split.length - i3) - 1)) * getTextSize()) * 1.1f), this.mPaintLabel);
                }
            }
            i2++;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0036, code lost:
        if (r10 < 10.0d) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        r1 = 10.0d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0057, code lost:
        if (r10 < 15.0d) goto L_0x0038;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public double humanRound(double r10, boolean r12) {
        /*
            r9 = this;
            r0 = 0
        L_0x0001:
            double r1 = java.lang.Math.abs(r10)
            r3 = 4621819117588971520(0x4024000000000000, double:10.0)
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 < 0) goto L_0x000f
            double r10 = r10 / r3
            int r0 = r0 + 1
            goto L_0x0001
        L_0x000f:
            double r1 = java.lang.Math.abs(r10)
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r7 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r7 >= 0) goto L_0x001e
            double r10 = r10 * r3
            int r0 = r0 + -1
            goto L_0x000f
        L_0x001e:
            r1 = 4617315517961601024(0x4014000000000000, double:5.0)
            r7 = 4611686018427387904(0x4000000000000000, double:2.0)
            if (r12 == 0) goto L_0x003a
            int r12 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
            if (r12 != 0) goto L_0x0029
            goto L_0x005a
        L_0x0029:
            int r12 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
            if (r12 > 0) goto L_0x002f
        L_0x002d:
            r1 = r7
            goto L_0x005b
        L_0x002f:
            int r12 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r12 > 0) goto L_0x0034
            goto L_0x005b
        L_0x0034:
            int r12 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1))
            if (r12 >= 0) goto L_0x005a
        L_0x0038:
            r1 = r3
            goto L_0x005b
        L_0x003a:
            int r12 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
            if (r12 != 0) goto L_0x003f
            goto L_0x005a
        L_0x003f:
            r5 = 4617202927970916762(0x401399999999999a, double:4.9)
            int r12 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
            if (r12 > 0) goto L_0x0049
            goto L_0x002d
        L_0x0049:
            r5 = 4621762822593629389(0x4023cccccccccccd, double:9.9)
            int r12 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
            if (r12 > 0) goto L_0x0053
            goto L_0x005b
        L_0x0053:
            r1 = 4624633867356078080(0x402e000000000000, double:15.0)
            int r12 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r12 >= 0) goto L_0x005a
            goto L_0x0038
        L_0x005a:
            r1 = r10
        L_0x005b:
            double r10 = (double) r0
            double r10 = java.lang.Math.pow(r3, r10)
            double r1 = r1 * r10
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.view.chart.GridLabelRenderer.humanRound(double, boolean):double");
    }

    public Styles getStyles() {
        return this.mStyles;
    }

    public int getLabelVerticalWidth() {
        if (this.mStyles.verticalLabelsVAlign == VerticalLabelsVAlign.ABOVE || this.mStyles.verticalLabelsVAlign == VerticalLabelsVAlign.BELOW || this.mLabelVerticalWidth == null || !isVerticalLabelsVisible()) {
            return 0;
        }
        return this.mLabelVerticalWidth.intValue();
    }

    public void setLabelVerticalWidth(Integer num) {
        this.mLabelVerticalWidth = num;
        this.mLabelVerticalWidthFixed = this.mLabelVerticalWidth != null;
    }

    public int getLabelHorizontalHeight() {
        if (this.mLabelHorizontalHeight == null || !isHorizontalLabelsVisible()) {
            return 0;
        }
        return this.mLabelHorizontalHeight.intValue();
    }

    public void setLabelHorizontalHeight(Integer num) {
        this.mLabelHorizontalHeight = num;
        this.mLabelHorizontalHeightFixed = this.mLabelHorizontalHeight != null;
    }

    public int getGridColor() {
        return this.mStyles.gridColor;
    }

    public boolean isHighlightZeroLines() {
        return this.mStyles.highlightZeroLines;
    }

    public int getPadding() {
        return this.mStyles.padding;
    }

    public void setTextSize(float f) {
        this.mStyles.textSize = f;
        reloadStyles();
    }

    public void setVerticalLabelsAlign(Paint.Align align) {
        this.mStyles.verticalLabelsAlign = align;
    }

    public void setVerticalLabelsColor(int i) {
        this.mStyles.verticalLabelsColor = i;
    }

    public void setHorizontalLabelsColor(int i) {
        this.mStyles.horizontalLabelsColor = i;
    }

    public void setHorizontalLabelsAngle(int i) {
        this.mStyles.horizontalLabelsAngle = (float) i;
    }

    public void setGridColor(int i) {
        this.mStyles.gridColor = i;
        reloadStyles();
    }

    public void setHighlightZeroLines(boolean z) {
        this.mStyles.highlightZeroLines = z;
    }

    public void setPadding(int i) {
        this.mStyles.padding = i;
    }

    public LabelFormatter getLabelFormatter() {
        return this.mLabelFormatter;
    }

    public void setLabelFormatter(LabelFormatter labelFormatter) {
        this.mLabelFormatter = labelFormatter;
        labelFormatter.setViewport(this.mGraphView.getViewport());
    }

    public String getHorizontalAxisTitle() {
        return this.mHorizontalAxisTitle;
    }

    public void setHorizontalAxisTitle(String str) {
        this.mHorizontalAxisTitle = str;
    }

    public String getVerticalAxisTitle() {
        return this.mVerticalAxisTitle;
    }

    public void setVerticalAxisTitle(String str) {
        this.mVerticalAxisTitle = str;
    }

    public float getVerticalAxisTitleTextSize() {
        return this.mStyles.verticalAxisTitleTextSize;
    }

    public void setVerticalAxisTitleTextSize(float f) {
        this.mStyles.verticalAxisTitleTextSize = f;
    }

    public int getVerticalAxisTitleColor() {
        return this.mStyles.verticalAxisTitleColor;
    }

    public void setVerticalAxisTitleColor(int i) {
        this.mStyles.verticalAxisTitleColor = i;
    }

    public float getHorizontalAxisTitleTextSize() {
        return this.mStyles.horizontalAxisTitleTextSize;
    }

    public void setHorizontalAxisTitleTextSize(float f) {
        this.mStyles.horizontalAxisTitleTextSize = f;
    }

    public int getHorizontalAxisTitleColor() {
        return this.mStyles.horizontalAxisTitleColor;
    }

    public void setHorizontalAxisTitleColor(int i) {
        this.mStyles.horizontalAxisTitleColor = i;
    }

    public Paint.Align getVerticalLabelsSecondScaleAlign() {
        return this.mStyles.verticalLabelsSecondScaleAlign;
    }

    public void setVerticalLabelsSecondScaleAlign(Paint.Align align) {
        this.mStyles.verticalLabelsSecondScaleAlign = align;
    }

    public int getVerticalLabelsSecondScaleColor() {
        return this.mStyles.verticalLabelsSecondScaleColor;
    }

    public void setVerticalLabelsSecondScaleColor(int i) {
        this.mStyles.verticalLabelsSecondScaleColor = i;
    }

    public boolean isHorizontalLabelsVisible() {
        return this.mStyles.horizontalLabelsVisible;
    }

    public void setHorizontalLabelsVisible(boolean z) {
        this.mStyles.horizontalLabelsVisible = z;
    }

    public boolean isVerticalLabelsVisible() {
        return this.mStyles.verticalLabelsVisible;
    }

    public void setVerticalLabelsVisible(boolean z) {
        this.mStyles.verticalLabelsVisible = z;
    }

    public int getNumVerticalLabels() {
        return this.mNumVerticalLabels;
    }

    public void setNumVerticalLabels(int i) {
        this.mNumVerticalLabels = i;
    }

    public int getNumHorizontalLabels() {
        return this.mNumHorizontalLabels;
    }

    public void setNumHorizontalLabels(int i) {
        this.mNumHorizontalLabels = i;
    }

    public GridStyle getGridStyle() {
        return this.mStyles.gridStyle;
    }

    public void setGridStyle(GridStyle gridStyle) {
        this.mStyles.gridStyle = gridStyle;
    }

    public int getLabelsSpace() {
        return this.mStyles.labelsSpace;
    }

    public void setLabelsSpace(int i) {
        this.mStyles.labelsSpace = i;
    }

    public void setVerticalLabelsVAlign(VerticalLabelsVAlign verticalLabelsVAlign) {
        this.mStyles.verticalLabelsVAlign = verticalLabelsVAlign;
    }

    public VerticalLabelsVAlign getVerticalLabelsVAlign() {
        return this.mStyles.verticalLabelsVAlign;
    }
}
