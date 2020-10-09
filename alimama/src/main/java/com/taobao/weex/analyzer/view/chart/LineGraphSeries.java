package com.taobao.weex.analyzer.view.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.animation.AccelerateInterpolator;
import com.facebook.imageutils.JfifUtil;
import com.taobao.weex.analyzer.view.chart.DataPointInterface;

public class LineGraphSeries<E extends DataPointInterface> extends BaseSeries<E> {
    private static final long ANIMATION_DURATION = 333;
    private boolean mAnimated;
    private AccelerateInterpolator mAnimationInterpolator;
    private long mAnimationStart;
    private int mAnimationStartFrameNo;
    private Paint mCustomPaint;
    private boolean mDrawAsPath = false;
    private double mLastAnimatedValue = Double.NaN;
    private Paint mPaint;
    private Paint mPaintBackground;
    private Path mPath;
    private Path mPathBackground;
    private LineGraphSeries<E>.Styles mStyles;

    private final class Styles {
        /* access modifiers changed from: private */
        public int backgroundColor;
        /* access modifiers changed from: private */
        public float dataPointsRadius;
        /* access modifiers changed from: private */
        public boolean drawBackground;
        /* access modifiers changed from: private */
        public boolean drawDataPoints;
        /* access modifiers changed from: private */
        public int thickness;

        private Styles() {
            this.thickness = 5;
            this.drawBackground = false;
            this.drawDataPoints = false;
            this.dataPointsRadius = 10.0f;
            this.backgroundColor = Color.argb(100, 172, JfifUtil.MARKER_SOS, 255);
        }
    }

    public LineGraphSeries() {
        init();
    }

    public LineGraphSeries(E[] eArr) {
        super(eArr);
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.mStyles = new Styles();
        this.mPaint = new Paint();
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaintBackground = new Paint();
        this.mPathBackground = new Path();
        this.mPath = new Path();
        this.mAnimationInterpolator = new AccelerateInterpolator(2.0f);
    }

    /* JADX WARNING: Removed duplicated region for block: B:72:0x021a  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0240  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0246  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0258  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0277  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void draw(com.taobao.weex.analyzer.view.chart.ChartView r60, android.graphics.Canvas r61) {
        /*
            r59 = this;
            r0 = r59
            r1 = r61
            r59.resetDataPoints()
            com.taobao.weex.analyzer.view.chart.Viewport r2 = r60.getViewport()
            r3 = 0
            double r4 = r2.getMaxX(r3)
            com.taobao.weex.analyzer.view.chart.Viewport r2 = r60.getViewport()
            double r6 = r2.getMinX(r3)
            com.taobao.weex.analyzer.view.chart.Viewport r2 = r60.getViewport()
            double r8 = r2.getMaxY(r3)
            com.taobao.weex.analyzer.view.chart.Viewport r2 = r60.getViewport()
            double r10 = r2.getMinY(r3)
            java.util.Iterator r2 = r0.getValues(r6, r4)
            android.graphics.Paint r12 = r0.mPaint
            com.taobao.weex.analyzer.view.chart.LineGraphSeries<E>$Styles r13 = r0.mStyles
            int r13 = r13.thickness
            float r13 = (float) r13
            r12.setStrokeWidth(r13)
            android.graphics.Paint r12 = r0.mPaint
            int r13 = r59.getColor()
            r12.setColor(r13)
            android.graphics.Paint r12 = r0.mPaintBackground
            com.taobao.weex.analyzer.view.chart.LineGraphSeries<E>$Styles r13 = r0.mStyles
            int r13 = r13.backgroundColor
            r12.setColor(r13)
            android.graphics.Paint r12 = r0.mCustomPaint
            if (r12 == 0) goto L_0x0053
            android.graphics.Paint r12 = r0.mCustomPaint
            goto L_0x0055
        L_0x0053:
            android.graphics.Paint r12 = r0.mPaint
        L_0x0055:
            android.graphics.Path r13 = r0.mPath
            r13.reset()
            com.taobao.weex.analyzer.view.chart.LineGraphSeries<E>$Styles r13 = r0.mStyles
            boolean r13 = r13.drawBackground
            if (r13 == 0) goto L_0x0067
            android.graphics.Path r13 = r0.mPathBackground
            r13.reset()
        L_0x0067:
            r13 = 0
            double r8 = r8 - r10
            double r4 = r4 - r6
            int r13 = r60.getGraphContentHeight()
            float r13 = (float) r13
            int r14 = r60.getGraphContentWidth()
            float r14 = (float) r14
            int r15 = r60.getGraphContentLeft()
            float r15 = (float) r15
            int r3 = r60.getGraphContentTop()
            float r3 = (float) r3
            r16 = 0
            r17 = -1082130432(0xffffffffbf800000, float:-1.0)
            r18 = 0
            r1 = r15
            r20 = r18
            r22 = r20
            r25 = r22
            r27 = r25
            r16 = 0
            r24 = -1082130432(0xffffffffbf800000, float:-1.0)
            r29 = -1082130432(0xffffffffbf800000, float:-1.0)
            r30 = 0
        L_0x0095:
            boolean r31 = r2.hasNext()
            if (r31 == 0) goto L_0x0366
            java.lang.Object r31 = r2.next()
            r32 = r2
            r2 = r31
            com.taobao.weex.analyzer.view.chart.DataPointInterface r2 = (com.taobao.weex.analyzer.view.chart.DataPointInterface) r2
            double r33 = r2.getY()
            double r33 = r33 - r10
            double r33 = r33 / r8
            r35 = r8
            double r8 = (double) r13
            java.lang.Double.isNaN(r8)
            double r33 = r33 * r8
            r37 = r10
            double r10 = r2.getX()
            double r39 = r10 - r6
            double r39 = r39 / r4
            r41 = r4
            double r4 = (double) r14
            java.lang.Double.isNaN(r4)
            r43 = r6
            double r6 = r4 * r39
            r39 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r31 = 1134985216(0x43a68000, float:333.0)
            r45 = 0
            r47 = 1065353216(0x3f800000, float:1.0)
            if (r16 <= 0) goto L_0x02ca
            r25 = 1
            int r26 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r26 <= 0) goto L_0x00ec
            java.lang.Double.isNaN(r4)
            double r26 = r4 - r20
            double r48 = r33 - r22
            double r26 = r26 * r48
            double r48 = r6 - r20
            double r26 = r26 / r48
            double r26 = r22 + r26
            r28 = 1
            goto L_0x00f1
        L_0x00ec:
            r4 = r6
            r26 = r33
            r28 = 0
        L_0x00f1:
            int r48 = (r26 > r18 ? 1 : (r26 == r18 ? 0 : -1))
            if (r48 >= 0) goto L_0x0117
            int r28 = (r22 > r18 ? 1 : (r22 == r18 ? 0 : -1))
            if (r28 >= 0) goto L_0x00fc
            r26 = 1
            goto L_0x010e
        L_0x00fc:
            r28 = 0
            double r48 = r18 - r22
            double r4 = r4 - r20
            double r48 = r48 * r4
            double r26 = r26 - r22
            double r48 = r48 / r26
            double r48 = r20 + r48
            r4 = r48
            r26 = 0
        L_0x010e:
            r28 = r26
            r48 = 1
            r49 = 1
            r26 = r18
            goto L_0x011d
        L_0x0117:
            r48 = r28
            r28 = 0
            r49 = 0
        L_0x011d:
            int r50 = (r26 > r8 ? 1 : (r26 == r8 ? 0 : -1))
            if (r50 <= 0) goto L_0x013f
            int r48 = (r22 > r8 ? 1 : (r22 == r8 ? 0 : -1))
            if (r48 <= 0) goto L_0x0128
            r28 = 1
            goto L_0x0139
        L_0x0128:
            java.lang.Double.isNaN(r8)
            double r48 = r8 - r22
            double r4 = r4 - r20
            double r48 = r48 * r4
            double r26 = r26 - r22
            double r48 = r48 / r26
            double r48 = r20 + r48
            r4 = r48
        L_0x0139:
            r26 = r8
            r48 = 1
            r49 = 1
        L_0x013f:
            int r50 = (r20 > r18 ? 1 : (r20 == r18 ? 0 : -1))
            if (r50 >= 0) goto L_0x0156
            double r50 = r18 - r4
            double r22 = r26 - r22
            double r50 = r50 * r22
            double r20 = r20 - r4
            double r50 = r50 / r20
            double r20 = r26 - r50
            r52 = r6
            r54 = r14
            r6 = r18
            goto L_0x015e
        L_0x0156:
            r52 = r6
            r54 = r14
            r6 = r20
            r20 = r22
        L_0x015e:
            float r14 = (float) r6
            float r47 = r15 + r47
            float r14 = r14 + r47
            int r22 = (r20 > r18 ? 1 : (r20 == r18 ? 0 : -1))
            if (r22 >= 0) goto L_0x0179
            if (r28 != 0) goto L_0x0175
            double r22 = r18 - r26
            double r6 = r4 - r6
            double r22 = r22 * r6
            double r20 = r20 - r26
            double r22 = r22 / r20
            double r6 = r4 - r22
        L_0x0175:
            r20 = r18
            r49 = 1
        L_0x0179:
            int r22 = (r20 > r8 ? 1 : (r20 == r8 ? 0 : -1))
            if (r22 <= 0) goto L_0x0192
            if (r28 != 0) goto L_0x018e
            java.lang.Double.isNaN(r8)
            double r22 = r8 - r26
            double r6 = r4 - r6
            double r22 = r22 * r6
            double r20 = r20 - r26
            double r22 = r22 / r20
            double r6 = r4 - r22
        L_0x018e:
            r20 = r8
            r49 = 1
        L_0x0192:
            float r6 = (float) r6
            float r6 = r6 + r47
            double r7 = (double) r3
            java.lang.Double.isNaN(r7)
            r56 = r14
            r55 = r15
            double r14 = r7 - r20
            float r9 = (float) r14
            float r9 = r9 + r13
            float r4 = (float) r4
            float r4 = r4 + r47
            java.lang.Double.isNaN(r7)
            double r7 = r7 - r26
            float r5 = (float) r7
            float r5 = r5 + r13
            int r7 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r7 >= 0) goto L_0x01b1
            r28 = 1
        L_0x01b1:
            if (r28 != 0) goto L_0x0279
            boolean r7 = java.lang.Float.isNaN(r9)
            if (r7 != 0) goto L_0x0279
            boolean r7 = java.lang.Float.isNaN(r5)
            if (r7 != 0) goto L_0x0279
            boolean r7 = r0.mAnimated
            if (r7 == 0) goto L_0x0217
            double r7 = r0.mLastAnimatedValue
            boolean r7 = java.lang.Double.isNaN(r7)
            if (r7 != 0) goto L_0x01d5
            double r7 = r0.mLastAnimatedValue
            int r14 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1))
            if (r14 >= 0) goto L_0x01d2
            goto L_0x01d5
        L_0x01d2:
            r1 = r4
            r7 = r1
            goto L_0x0218
        L_0x01d5:
            long r7 = java.lang.System.currentTimeMillis()
            long r14 = r0.mAnimationStart
            int r20 = (r14 > r45 ? 1 : (r14 == r45 ? 0 : -1))
            if (r20 != 0) goto L_0x01e5
            r0.mAnimationStart = r7
            r14 = 0
            r0.mAnimationStartFrameNo = r14
            goto L_0x01f3
        L_0x01e5:
            int r14 = r0.mAnimationStartFrameNo
            r15 = 15
            if (r14 >= r15) goto L_0x01f3
            r0.mAnimationStart = r7
            int r14 = r0.mAnimationStartFrameNo
            int r14 = r14 + 1
            r0.mAnimationStartFrameNo = r14
        L_0x01f3:
            long r14 = r0.mAnimationStart
            long r7 = r7 - r14
            float r7 = (float) r7
            float r7 = r7 / r31
            android.view.animation.AccelerateInterpolator r8 = r0.mAnimationInterpolator
            float r8 = r8.getInterpolation(r7)
            double r14 = (double) r7
            int r7 = (r14 > r39 ? 1 : (r14 == r39 ? 0 : -1))
            if (r7 > 0) goto L_0x0215
            float r6 = r6 - r1
            float r6 = r6 * r8
            float r6 = r6 + r1
            float r6 = java.lang.Math.max(r6, r1)
            float r7 = r4 - r1
            float r7 = r7 * r8
            float r7 = r7 + r1
            androidx.core.view.ViewCompat.postInvalidateOnAnimation(r60)
            goto L_0x0218
        L_0x0215:
            r0.mLastAnimatedValue = r10
        L_0x0217:
            r7 = r4
        L_0x0218:
            if (r48 != 0) goto L_0x0240
            com.taobao.weex.analyzer.view.chart.LineGraphSeries<E>$Styles r8 = r0.mStyles
            boolean r8 = r8.drawDataPoints
            if (r8 == 0) goto L_0x023a
            android.graphics.Paint$Style r8 = r12.getStyle()
            android.graphics.Paint$Style r10 = android.graphics.Paint.Style.FILL
            r12.setStyle(r10)
            com.taobao.weex.analyzer.view.chart.LineGraphSeries<E>$Styles r10 = r0.mStyles
            float r10 = r10.dataPointsRadius
            r14 = r61
            r14.drawCircle(r7, r5, r10, r12)
            r12.setStyle(r8)
            goto L_0x023c
        L_0x023a:
            r14 = r61
        L_0x023c:
            r0.registerDataPoint(r4, r5, r2)
            goto L_0x0242
        L_0x0240:
            r14 = r61
        L_0x0242:
            boolean r2 = r0.mDrawAsPath
            if (r2 == 0) goto L_0x024b
            android.graphics.Path r2 = r0.mPath
            r2.moveTo(r6, r9)
        L_0x024b:
            float r2 = r4 - r30
            float r2 = java.lang.Math.abs(r2)
            r8 = 1050253722(0x3e99999a, float:0.3)
            int r2 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r2 <= 0) goto L_0x0277
            boolean r2 = r0.mDrawAsPath
            if (r2 == 0) goto L_0x0263
            android.graphics.Path r2 = r0.mPath
            r2.lineTo(r7, r5)
            r8 = 0
            goto L_0x0274
        L_0x0263:
            r2 = 4
            float[] r2 = new float[r2]
            r8 = 0
            r2[r8] = r6
            r2[r25] = r9
            r10 = 2
            r2[r10] = r7
            r10 = 3
            r2[r10] = r5
            r0.renderLine(r14, r2, r12)
        L_0x0274:
            r30 = r4
            goto L_0x027d
        L_0x0277:
            r8 = 0
            goto L_0x027d
        L_0x0279:
            r8 = 0
            r14 = r61
            r7 = r4
        L_0x027d:
            com.taobao.weex.analyzer.view.chart.LineGraphSeries<E>$Styles r2 = r0.mStyles
            boolean r2 = r2.drawBackground
            if (r2 == 0) goto L_0x02bc
            if (r49 == 0) goto L_0x02a0
            r2 = r24
            int r4 = (r2 > r17 ? 1 : (r2 == r17 ? 0 : -1))
            if (r4 != 0) goto L_0x0299
            android.graphics.Path r2 = r0.mPathBackground
            r4 = r56
            r2.moveTo(r4, r9)
            r24 = r4
            r29 = r9
            goto L_0x029b
        L_0x0299:
            r24 = r2
        L_0x029b:
            android.graphics.Path r2 = r0.mPathBackground
            r2.lineTo(r6, r9)
        L_0x02a0:
            r2 = r24
            int r4 = (r2 > r17 ? 1 : (r2 == r17 ? 0 : -1))
            if (r4 != 0) goto L_0x02b0
            android.graphics.Path r2 = r0.mPathBackground
            r2.moveTo(r6, r9)
            r24 = r6
            r29 = r9
            goto L_0x02b2
        L_0x02b0:
            r24 = r2
        L_0x02b2:
            android.graphics.Path r2 = r0.mPathBackground
            r2.lineTo(r6, r9)
            android.graphics.Path r2 = r0.mPathBackground
            r2.lineTo(r7, r5)
        L_0x02bc:
            r2 = r24
            double r6 = (double) r7
            double r4 = (double) r5
            r24 = r2
            r25 = r4
            r27 = r6
            r57 = r52
            goto L_0x0350
        L_0x02ca:
            r52 = r6
            r54 = r14
            r55 = r15
            r2 = r24
            r8 = 0
            r14 = r61
            com.taobao.weex.analyzer.view.chart.LineGraphSeries<E>$Styles r4 = r0.mStyles
            boolean r4 = r4.drawDataPoints
            if (r4 == 0) goto L_0x034c
            r4 = r52
            float r6 = (float) r4
            float r15 = r55 + r47
            float r6 = r6 + r15
            double r8 = (double) r3
            java.lang.Double.isNaN(r8)
            double r8 = r8 - r33
            float r7 = (float) r8
            float r7 = r7 + r13
            int r8 = (r6 > r55 ? 1 : (r6 == r55 ? 0 : -1))
            if (r8 < 0) goto L_0x0349
            float r8 = r3 + r13
            int r8 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1))
            if (r8 > 0) goto L_0x0349
            boolean r8 = r0.mAnimated
            if (r8 == 0) goto L_0x0331
            double r8 = r0.mLastAnimatedValue
            boolean r8 = java.lang.Double.isNaN(r8)
            if (r8 != 0) goto L_0x0307
            double r8 = r0.mLastAnimatedValue
            int r15 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r15 >= 0) goto L_0x0331
        L_0x0307:
            long r8 = java.lang.System.currentTimeMillis()
            r57 = r4
            long r4 = r0.mAnimationStart
            int r15 = (r4 > r45 ? 1 : (r4 == r45 ? 0 : -1))
            if (r15 != 0) goto L_0x0315
            r0.mAnimationStart = r8
        L_0x0315:
            long r4 = r0.mAnimationStart
            long r8 = r8 - r4
            float r4 = (float) r8
            float r4 = r4 / r31
            android.view.animation.AccelerateInterpolator r5 = r0.mAnimationInterpolator
            float r5 = r5.getInterpolation(r4)
            double r8 = (double) r4
            int r4 = (r8 > r39 ? 1 : (r8 == r39 ? 0 : -1))
            if (r4 > 0) goto L_0x032e
            float r6 = r6 - r1
            float r6 = r6 * r5
            float r6 = r6 + r1
            androidx.core.view.ViewCompat.postInvalidateOnAnimation(r60)
            goto L_0x0333
        L_0x032e:
            r0.mLastAnimatedValue = r10
            goto L_0x0333
        L_0x0331:
            r57 = r4
        L_0x0333:
            android.graphics.Paint$Style r4 = r12.getStyle()
            android.graphics.Paint$Style r5 = android.graphics.Paint.Style.FILL
            r12.setStyle(r5)
            com.taobao.weex.analyzer.view.chart.LineGraphSeries<E>$Styles r5 = r0.mStyles
            float r5 = r5.dataPointsRadius
            r14.drawCircle(r6, r7, r5, r12)
            r12.setStyle(r4)
            goto L_0x034e
        L_0x0349:
            r57 = r4
            goto L_0x034e
        L_0x034c:
            r57 = r52
        L_0x034e:
            r24 = r2
        L_0x0350:
            int r16 = r16 + 1
            r2 = r32
            r22 = r33
            r8 = r35
            r10 = r37
            r4 = r41
            r6 = r43
            r14 = r54
            r15 = r55
            r20 = r57
            goto L_0x0095
        L_0x0366:
            r2 = r24
            r14 = r61
            boolean r1 = r0.mDrawAsPath
            if (r1 == 0) goto L_0x0373
            android.graphics.Path r1 = r0.mPath
            r14.drawPath(r1, r12)
        L_0x0373:
            com.taobao.weex.analyzer.view.chart.LineGraphSeries<E>$Styles r1 = r0.mStyles
            boolean r1 = r1.drawBackground
            if (r1 == 0) goto L_0x03a4
            int r1 = (r2 > r17 ? 1 : (r2 == r17 ? 0 : -1))
            if (r1 == 0) goto L_0x03a4
            float r13 = r13 + r3
            double r3 = (double) r13
            int r1 = (r25 > r3 ? 1 : (r25 == r3 ? 0 : -1))
            if (r1 == 0) goto L_0x038d
            android.graphics.Path r1 = r0.mPathBackground
            r3 = r27
            float r3 = (float) r3
            r1.lineTo(r3, r13)
        L_0x038d:
            android.graphics.Path r1 = r0.mPathBackground
            r1.lineTo(r2, r13)
            r1 = r29
            int r3 = (r1 > r13 ? 1 : (r1 == r13 ? 0 : -1))
            if (r3 == 0) goto L_0x039d
            android.graphics.Path r3 = r0.mPathBackground
            r3.lineTo(r2, r1)
        L_0x039d:
            android.graphics.Path r1 = r0.mPathBackground
            android.graphics.Paint r2 = r0.mPaintBackground
            r14.drawPath(r1, r2)
        L_0x03a4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.view.chart.LineGraphSeries.draw(com.taobao.weex.analyzer.view.chart.ChartView, android.graphics.Canvas):void");
    }

    private void renderLine(Canvas canvas, float[] fArr, Paint paint) {
        canvas.drawLines(fArr, paint);
    }

    public int getThickness() {
        return this.mStyles.thickness;
    }

    public void setThickness(int i) {
        int unused = this.mStyles.thickness = i;
    }

    public boolean isDrawBackground() {
        return this.mStyles.drawBackground;
    }

    public void setDrawBackground(boolean z) {
        boolean unused = this.mStyles.drawBackground = z;
    }

    public boolean isDrawDataPoints() {
        return this.mStyles.drawDataPoints;
    }

    public void setDrawDataPoints(boolean z) {
        boolean unused = this.mStyles.drawDataPoints = z;
    }

    public float getDataPointsRadius() {
        return this.mStyles.dataPointsRadius;
    }

    public void setDataPointsRadius(float f) {
        float unused = this.mStyles.dataPointsRadius = f;
    }

    public int getBackgroundColor() {
        return this.mStyles.backgroundColor;
    }

    public void setBackgroundColor(int i) {
        int unused = this.mStyles.backgroundColor = i;
    }

    public void setCustomPaint(Paint paint) {
        this.mCustomPaint = paint;
    }

    public void setAnimated(boolean z) {
        this.mAnimated = z;
    }

    public boolean isDrawAsPath() {
        return this.mDrawAsPath;
    }

    public void setDrawAsPath(boolean z) {
        this.mDrawAsPath = z;
    }

    public void appendData(E e, boolean z, int i, boolean z2) {
        if (!isAnimationActive()) {
            this.mAnimationStart = 0;
        }
        super.appendData(e, z, i, z2);
    }

    private boolean isAnimationActive() {
        if (!this.mAnimated || System.currentTimeMillis() - this.mAnimationStart > ANIMATION_DURATION) {
            return false;
        }
        return true;
    }
}
