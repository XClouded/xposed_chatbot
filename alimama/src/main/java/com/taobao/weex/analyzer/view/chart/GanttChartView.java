package com.taobao.weex.analyzer.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.utils.ViewUtils;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GanttChartView extends View {
    private final float DEFAULT_VIEW_HEIGHT = ViewUtils.dp2px(getContext(), 250);
    private final float DEFAULT_VIEW_WIDTH = ViewUtils.dp2px(getContext(), 300);
    private int axisStrokeWidth;
    int chartOffset;
    int chartPadding;
    private RectF mContentBounds;
    private Paint mDashPaint;
    private int mDataColor = Color.parseColor("#bccddc39");
    private int mDataColorWarning = Color.parseColor("#bcFF1744");
    private Set<Data> mDataList = new LinkedHashSet();
    private int mGapY;
    private int mHeight;
    private float mMaxDimension;
    private int mMaxTime;
    private Paint mPaint;
    private int mStepY;
    private float mViewMinHeight = this.DEFAULT_VIEW_HEIGHT;
    private float mViewMinWidth = this.DEFAULT_VIEW_WIDTH;
    private int mWidth;

    public GanttChartView(Context context) {
        super(context);
        init();
    }

    public GanttChartView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public GanttChartView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.mPaint = new Paint(1);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mDashPaint = new Paint(1);
        this.mDashPaint.setStyle(Paint.Style.STROKE);
        this.mDashPaint.setColor(-1);
        this.mDashPaint.setPathEffect(new DashPathEffect(new float[]{5.0f, 10.0f, 5.0f}, 0.0f));
        this.mStepY = (int) ViewUtils.dp2px(getContext(), 18);
        this.mGapY = (int) ViewUtils.dp2px(getContext(), 15);
        this.chartOffset = (int) ViewUtils.dp2px(getContext(), 15);
        this.chartPadding = (int) ViewUtils.dp2px(getContext(), 15);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(this.mContentBounds, this.mPaint);
            return;
        }
        drawGraphElements(canvas);
    }

    private void drawGraphElements(Canvas canvas) {
        drawBorders(canvas);
        drawAxis(canvas, true);
        drawData(canvas);
    }

    private void drawBorders(Canvas canvas) {
        canvas.save();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(Color.parseColor("#bccddc39"));
        this.mPaint.setStrokeWidth(2.0f);
        canvas.drawRect(this.mContentBounds, this.mPaint);
        canvas.restore();
    }

    private void drawAxis(Canvas canvas, boolean z) {
        Canvas canvas2 = canvas;
        this.axisStrokeWidth = (int) ViewUtils.dp2px(getContext(), 2);
        PointF pointF = new PointF((float) (getPaddingLeft() + this.chartOffset), (float) ((this.mHeight - getPaddingBottom()) - this.chartOffset));
        PointF pointF2 = new PointF((float) ((this.mWidth - getPaddingRight()) - this.chartPadding), (float) ((this.mHeight - getPaddingBottom()) - this.chartOffset));
        PointF pointF3 = new PointF((float) (getPaddingLeft() + this.chartOffset), (float) (getPaddingTop() + this.chartPadding));
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(-1);
        this.mPaint.setStrokeWidth((float) this.axisStrokeWidth);
        canvas.save();
        Canvas canvas3 = canvas;
        canvas3.drawLine(pointF.x, pointF.y, pointF2.x, pointF2.y, this.mPaint);
        canvas3.drawLine(pointF.x, pointF.y, pointF3.x, pointF3.y, this.mPaint);
        canvas.restore();
        drawAxisArrow(canvas2, pointF2, 90.0f);
        drawAxisArrow(canvas2, pointF3, 0.0f);
        canvas.save();
        this.mPaint.setColor(-1);
        this.mMaxDimension = pointF2.x - pointF.x;
        float f = this.mMaxDimension / ((float) 10);
        this.mMaxTime = getMaxTime();
        int i = this.mMaxTime / 10;
        Path path = null;
        for (int i2 = 0; i2 <= 10; i2++) {
            this.mPaint.setStrokeWidth(ViewUtils.dp2px(getContext(), 1));
            if (i2 != 10) {
                float f2 = ((float) i2) * f;
                canvas.drawLine(pointF.x + f2, pointF.y, pointF.x + f2, pointF.y - ViewUtils.dp2px(getContext(), 5), this.mPaint);
            }
            if (z && i2 != 0) {
                this.mPaint.setStrokeWidth(1.0f);
                if (path == null) {
                    path = new Path();
                }
                float f3 = ((float) i2) * f;
                path.moveTo(pointF.x + f3, pointF.y);
                path.lineTo(pointF.x + f3, (float) getPaddingTop());
            }
            if (i2 % 2 == 0) {
                String str = (i * i2) + "";
                float measureText = this.mPaint.measureText(str);
                this.mPaint.setColor(-1);
                this.mPaint.setStyle(Paint.Style.FILL);
                this.mPaint.setStrokeWidth(ViewUtils.sp2px(getContext(), 1));
                float dp2px = (float) ((int) ViewUtils.dp2px(getContext(), 10));
                this.mPaint.setTextSize(dp2px);
                canvas2.drawText(str, (pointF.x + (((float) i2) * f)) - (measureText / 2.0f), pointF.y + dp2px, this.mPaint);
            }
        }
        if (path != null) {
            canvas2.drawPath(path, this.mDashPaint);
        }
        canvas.restore();
    }

    private int getMaxTime() {
        if (this.mDataList.isEmpty()) {
            return 1000;
        }
        int i = 0;
        for (Data next : this.mDataList) {
            i = Math.max(i, next.end - next.start);
        }
        return Math.max(1000, i);
    }

    private void drawAxisArrow(Canvas canvas, PointF pointF, float f) {
        Path path = new Path();
        double dp2px = (double) ViewUtils.dp2px(getContext(), 10);
        double d = (double) 30.0f;
        double sin = Math.sin(Math.toRadians(d));
        Double.isNaN(dp2px);
        float f2 = (float) (sin * dp2px);
        double cos = Math.cos(Math.toRadians(d));
        Double.isNaN(dp2px);
        float f3 = (float) (dp2px * cos);
        canvas.save();
        PointF pointF2 = new PointF(pointF.x - f2, pointF.y + f3);
        PointF pointF3 = new PointF(pointF.x + f2, pointF.y + f3);
        path.moveTo(pointF2.x, pointF2.y);
        path.lineTo(pointF.x, pointF.y);
        path.lineTo(pointF3.x, pointF3.y);
        Matrix matrix = new Matrix();
        matrix.preRotate(f, pointF.x, pointF.y);
        path.transform(matrix);
        canvas.drawPath(path, this.mPaint);
        canvas.restore();
    }

    private void drawData(Canvas canvas) {
        PointF pointF;
        int i;
        Canvas canvas2 = canvas;
        canvas.save();
        int dp2px = (int) ViewUtils.dp2px(getContext(), 15);
        int i2 = this.mStepY;
        int i3 = this.mGapY;
        PointF pointF2 = new PointF((float) (getPaddingLeft() + this.chartOffset), (float) (getPaddingTop() + this.chartPadding + dp2px));
        PointF pointF3 = new PointF((float) (getPaddingLeft() + this.chartOffset + (this.axisStrokeWidth / 2)), (float) ((((this.mHeight - getPaddingBottom()) - this.chartOffset) - dp2px) - (this.axisStrokeWidth / 2)));
        new PointF((float) ((this.mWidth - getPaddingRight()) - this.chartPadding), (float) (((this.mHeight - getPaddingBottom()) - this.chartOffset) - dp2px));
        float f = this.mMaxDimension / ((float) this.mMaxTime);
        Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
        float f2 = fontMetrics.descent - fontMetrics.ascent;
        int i4 = 0;
        for (Data next : this.mDataList) {
            if (next.end - next.start > 0) {
                int i5 = (int) (pointF2.y + ((float) ((i3 + i2) * i4)));
                int i6 = (int) (pointF3.x + (((float) next.start) * f));
                int i7 = (int) (((float) (next.end - next.start)) * f);
                i = i3;
                this.mPaint.setColor(judgeColor(next.end - next.start));
                this.mPaint.setStyle(Paint.Style.FILL);
                float f3 = (float) i6;
                float f4 = (float) (i6 + i7);
                float f5 = (float) (i5 + i2);
                int i8 = i7;
                int i9 = i6;
                float f6 = f4;
                pointF = pointF2;
                Data data = next;
                canvas.drawRect(f3, (float) i5, f6, f5, this.mPaint);
                this.mPaint.setTextSize(ViewUtils.sp2px(getContext(), 8));
                this.mPaint.setStrokeWidth(ViewUtils.sp2px(getContext(), 1));
                this.mPaint.setColor(-1);
                this.mPaint.setStyle(Paint.Style.FILL);
                canvas.save();
                this.mPaint.setTextAlign(Paint.Align.LEFT);
                canvas2.drawText(data.name, f3, f5 + f2, this.mPaint);
                canvas.restore();
                this.mPaint.setTextSize(ViewUtils.sp2px(getContext(), 10));
                canvas.save();
                String str = (data.end - data.start) + "ms";
                this.mPaint.setColor(Color.parseColor("#80D8FF"));
                this.mPaint.setTextAlign(Paint.Align.CENTER);
                canvas2.drawText(str, (float) (i9 + (i8 / 2)), ((float) (i5 + (i2 / 2))) + (f2 / 2.0f), this.mPaint);
                canvas.restore();
            } else {
                i = i3;
                pointF = pointF2;
            }
            i4++;
            i3 = i;
            pointF2 = pointF;
        }
        canvas.restore();
    }

    private int judgeColor(int i) {
        if (i > 1500) {
            return this.mDataColorWarning;
        }
        return this.mDataColor;
    }

    public void addData(@NonNull Data data) {
        if (this.mDataList.contains(data)) {
            this.mDataList.remove(data);
        }
        this.mDataList.add(data);
        invalidate();
    }

    public void setData(@NonNull List<Data> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
        this.mContentBounds = new RectF((float) getPaddingLeft(), (float) getPaddingTop(), (float) (i - getPaddingRight()), (float) (i2 - getPaddingBottom()));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(getResolvedSize((int) (this.mViewMinWidth + ((float) getPaddingLeft()) + ((float) getPaddingRight())), i), getResolvedSize((int) (this.mViewMinHeight + ((float) getPaddingTop()) + ((float) getPaddingBottom())), i2));
    }

    private int getResolvedSize(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        if (mode == 1073741824) {
            return size;
        }
        return mode == Integer.MIN_VALUE ? Math.min(i, size) : i;
    }

    public static class Data {
        public int end;
        public String name;
        public int start;

        public Data(int i, int i2, String str) {
            this.start = i;
            this.end = i2;
            this.name = str;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Data data = (Data) obj;
            if (this.name != null) {
                return this.name.equals(data.name);
            }
            if (data.name == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (this.name != null) {
                return this.name.hashCode();
            }
            return 0;
        }
    }
}
