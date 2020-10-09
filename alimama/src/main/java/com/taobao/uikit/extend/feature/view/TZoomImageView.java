package com.taobao.uikit.extend.feature.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class TZoomImageView extends TUrlImageView {
    private static final int MAX_SCALE_X = 5;
    private static final int MAX_SCALE_Y = 5;
    private static final int TYPE_DRAG = 1;
    private static final int TYPE_NONE = 0;
    private static final int TYPE_ZOOM = 2;
    private float mBottom;
    private Matrix mCurMatrix = new Matrix();
    private boolean mFirst = true;
    private boolean mIgnoreUpEvent = false;
    private float mLeft;
    private Matrix mMatrix = new Matrix();
    private int mMaxHeight;
    private int mMaxWidth;
    private PointF mMidPoint = new PointF();
    private Matrix mOriginalMatrix = new Matrix();
    private float[] mOriginalValues = new float[9];
    private boolean mOverMaxScale = false;
    private Matrix mPreMatrix = new Matrix();
    private Rect mRect;
    private float mRight;
    private boolean mScaleLarge = false;
    private boolean mScaleSmall = false;
    private TZoomScroller mScroller;
    private float mStartDist;
    private PointF mStartPoint = new PointF();
    private float mTop;
    private int mType = 0;

    public TZoomImageView(Context context) {
        super(context);
        init();
    }

    public TZoomImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public TZoomImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.mScroller = new TZoomScroller(new AccelerateDecelerateInterpolator());
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mMaxWidth = i3 - i;
        this.mMaxHeight = i4 - i2;
    }

    public void reset() {
        if (!this.mFirst) {
            this.mFirst = true;
            this.mMatrix = new Matrix();
            this.mPreMatrix = new Matrix();
            this.mCurMatrix = new Matrix();
            setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r10) {
        /*
            r9 = this;
            android.graphics.drawable.Drawable r0 = r9.getDrawable()
            if (r0 != 0) goto L_0x000b
            boolean r10 = super.onTouchEvent(r10)
            return r10
        L_0x000b:
            int r0 = r10.getAction()
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 9
            r2 = 2
            r3 = 1092616192(0x41200000, float:10.0)
            r4 = 4
            r5 = 1
            r6 = 0
            switch(r0) {
                case 0: goto L_0x01d6;
                case 1: goto L_0x019c;
                case 2: goto L_0x003b;
                case 3: goto L_0x001c;
                case 4: goto L_0x001c;
                case 5: goto L_0x001e;
                case 6: goto L_0x01a3;
                default: goto L_0x001c;
            }
        L_0x001c:
            goto L_0x0250
        L_0x001e:
            float r0 = r9.distance(r10)
            r9.mStartDist = r0
            float r0 = r9.mStartDist
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 <= 0) goto L_0x0250
            android.graphics.Matrix r0 = r9.mCurMatrix
            android.graphics.Matrix r1 = r9.mMatrix
            r0.set(r1)
            r9.mType = r2
            android.graphics.PointF r0 = r9.midPoint(r10)
            r9.mMidPoint = r0
            goto L_0x0250
        L_0x003b:
            int r0 = r9.mType
            if (r0 != 0) goto L_0x0065
            float r0 = r10.getX()
            android.graphics.PointF r1 = r9.mStartPoint
            float r1 = r1.x
            float r0 = r0 - r1
            float r1 = r10.getY()
            android.graphics.PointF r2 = r9.mStartPoint
            float r2 = r2.y
            float r1 = r1 - r2
            float r0 = java.lang.Math.abs(r0)
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 > 0) goto L_0x0061
            float r0 = java.lang.Math.abs(r1)
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 <= 0) goto L_0x0250
        L_0x0061:
            r9.mType = r5
            goto L_0x0250
        L_0x0065:
            int r0 = r9.mType
            if (r0 != r5) goto L_0x0116
            boolean r0 = r9.mScaleLarge
            if (r0 == 0) goto L_0x0116
            float r0 = r10.getX()
            android.graphics.PointF r1 = r9.mStartPoint
            float r1 = r1.x
            float r0 = r0 - r1
            float r1 = r10.getY()
            android.graphics.PointF r2 = r9.mStartPoint
            float r2 = r2.y
            float r1 = r1 - r2
            r2 = 0
            int r3 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r3 <= 0) goto L_0x00a5
            float r3 = r9.mLeft
            int r3 = (r3 > r2 ? 1 : (r3 == r2 ? 0 : -1))
            if (r3 < 0) goto L_0x0093
            android.view.ViewParent r0 = r9.getParent()
            r0.requestDisallowInterceptTouchEvent(r6)
        L_0x0091:
            r0 = 0
            goto L_0x00cd
        L_0x0093:
            android.view.ViewParent r3 = r9.getParent()
            r3.requestDisallowInterceptTouchEvent(r5)
            float r3 = r9.mLeft
            float r3 = r3 + r0
            int r3 = (r3 > r2 ? 1 : (r3 == r2 ? 0 : -1))
            if (r3 <= 0) goto L_0x00cd
            float r0 = r9.mLeft
            float r0 = -r0
            goto L_0x00cd
        L_0x00a5:
            float r3 = r9.mRight
            int r4 = r9.mMaxWidth
            float r4 = (float) r4
            int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r3 > 0) goto L_0x00b6
            android.view.ViewParent r0 = r9.getParent()
            r0.requestDisallowInterceptTouchEvent(r6)
            goto L_0x0091
        L_0x00b6:
            android.view.ViewParent r3 = r9.getParent()
            r3.requestDisallowInterceptTouchEvent(r5)
            float r3 = r9.mRight
            float r3 = r3 + r0
            int r4 = r9.mMaxWidth
            float r4 = (float) r4
            int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r3 >= 0) goto L_0x00cd
            int r0 = r9.mMaxWidth
            float r0 = (float) r0
            float r3 = r9.mRight
            float r0 = r0 - r3
        L_0x00cd:
            int r3 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r3 <= 0) goto L_0x00e4
            float r3 = r9.mTop
            int r3 = (r3 > r2 ? 1 : (r3 == r2 ? 0 : -1))
            if (r3 <= 0) goto L_0x00d9
        L_0x00d7:
            r1 = 0
            goto L_0x00fe
        L_0x00d9:
            float r3 = r9.mTop
            float r3 = r3 + r1
            int r2 = (r3 > r2 ? 1 : (r3 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x00fe
            float r1 = r9.mTop
            float r1 = -r1
            goto L_0x00fe
        L_0x00e4:
            float r3 = r9.mBottom
            int r4 = r9.mMaxHeight
            float r4 = (float) r4
            int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r3 >= 0) goto L_0x00ee
            goto L_0x00d7
        L_0x00ee:
            float r2 = r9.mBottom
            float r2 = r2 + r1
            int r3 = r9.mMaxHeight
            float r3 = (float) r3
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 >= 0) goto L_0x00fe
            int r1 = r9.mMaxHeight
            float r1 = (float) r1
            float r2 = r9.mBottom
            float r1 = r1 - r2
        L_0x00fe:
            android.graphics.Matrix r2 = r9.mMatrix
            android.graphics.Matrix r3 = r9.mCurMatrix
            r2.set(r3)
            android.graphics.Matrix r2 = r9.mMatrix
            r2.postTranslate(r0, r1)
            android.widget.ImageView$ScaleType r0 = android.widget.ImageView.ScaleType.MATRIX
            r9.setScaleType(r0)
            android.graphics.Matrix r0 = r9.mMatrix
            r9.setImageMatrix(r0)
            goto L_0x0250
        L_0x0116:
            int r0 = r9.mType
            if (r0 != r2) goto L_0x0250
            android.view.ViewParent r0 = r9.getParent()
            r0.requestDisallowInterceptTouchEvent(r5)
            float r0 = r9.distance(r10)
            int r2 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r2 <= 0) goto L_0x0190
            android.graphics.Matrix r2 = r9.mMatrix
            android.graphics.Matrix r3 = r9.mCurMatrix
            r2.set(r3)
            float r2 = r9.mStartDist
            float r0 = r0 / r2
            android.graphics.Matrix r2 = r9.mMatrix
            android.graphics.PointF r3 = r9.mMidPoint
            float r3 = r3.x
            android.graphics.PointF r7 = r9.mMidPoint
            float r7 = r7.y
            r2.postScale(r0, r0, r3, r7)
            float[] r0 = new float[r1]
            android.graphics.Matrix r1 = r9.mMatrix
            r1.getValues(r0)
            r1 = r0[r6]
            float[] r2 = r9.mOriginalValues
            r2 = r2[r6]
            float r1 = r1 / r2
            double r1 = (double) r1
            r7 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r3 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r3 < 0) goto L_0x0167
            r1 = r0[r4]
            float[] r2 = r9.mOriginalValues
            r2 = r2[r4]
            float r1 = r1 / r2
            double r1 = (double) r1
            int r3 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r3 >= 0) goto L_0x0162
            goto L_0x0167
        L_0x0162:
            r9.mScaleSmall = r6
            r9.mScaleLarge = r5
            goto L_0x016b
        L_0x0167:
            r9.mScaleSmall = r5
            r9.mScaleLarge = r6
        L_0x016b:
            r1 = r0[r6]
            float[] r2 = r9.mOriginalValues
            r2 = r2[r6]
            float r1 = r1 / r2
            r2 = 1084227584(0x40a00000, float:5.0)
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 > 0) goto L_0x0187
            r0 = r0[r4]
            float[] r1 = r9.mOriginalValues
            r1 = r1[r4]
            float r0 = r0 / r1
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 <= 0) goto L_0x0184
            goto L_0x0187
        L_0x0184:
            r9.mOverMaxScale = r6
            goto L_0x0190
        L_0x0187:
            r9.mOverMaxScale = r5
            android.graphics.Matrix r0 = r9.mPreMatrix
            android.graphics.Matrix r1 = r9.mCurMatrix
            r0.set(r1)
        L_0x0190:
            android.widget.ImageView$ScaleType r0 = android.widget.ImageView.ScaleType.MATRIX
            r9.setScaleType(r0)
            android.graphics.Matrix r0 = r9.mMatrix
            r9.setImageMatrix(r0)
            goto L_0x0250
        L_0x019c:
            boolean r0 = r9.mIgnoreUpEvent
            if (r0 == 0) goto L_0x01a3
            r9.mIgnoreUpEvent = r6
            return r5
        L_0x01a3:
            int r0 = r9.mType
            if (r0 != r5) goto L_0x01aa
            r9.mType = r6
            return r5
        L_0x01aa:
            int r0 = r9.mType
            if (r0 != r2) goto L_0x0250
            r9.mType = r6
            r9.mIgnoreUpEvent = r5
            android.view.ViewParent r10 = r9.getParent()
            r10.requestDisallowInterceptTouchEvent(r6)
            boolean r10 = r9.mScaleSmall
            if (r10 == 0) goto L_0x01c8
            r9.mScaleSmall = r6
            r9.mScaleLarge = r6
            android.graphics.Matrix r10 = r9.mMatrix
            android.graphics.Matrix r0 = r9.mOriginalMatrix
            r9.startScroll(r10, r0)
        L_0x01c8:
            boolean r10 = r9.mOverMaxScale
            if (r10 == 0) goto L_0x01d5
            r9.mOverMaxScale = r6
            android.graphics.Matrix r10 = r9.mMatrix
            android.graphics.Matrix r0 = r9.mPreMatrix
            r9.startScroll(r10, r0)
        L_0x01d5:
            return r5
        L_0x01d6:
            android.graphics.PointF r0 = r9.mStartPoint
            float r3 = r10.getX()
            float r7 = r10.getY()
            r0.set(r3, r7)
            boolean r0 = r9.mFirst
            if (r0 == 0) goto L_0x0202
            android.graphics.Matrix r0 = r9.mOriginalMatrix
            android.graphics.Matrix r3 = r9.getImageMatrix()
            r0.set(r3)
            android.graphics.Matrix r0 = r9.mMatrix
            android.graphics.Matrix r3 = r9.getImageMatrix()
            r0.preConcat(r3)
            r9.mFirst = r6
            android.graphics.Matrix r0 = r9.mOriginalMatrix
            float[] r3 = r9.mOriginalValues
            r0.getValues(r3)
        L_0x0202:
            android.graphics.Matrix r0 = r9.mCurMatrix
            android.graphics.Matrix r3 = r9.mMatrix
            r0.set(r3)
            r9.mType = r6
            android.graphics.drawable.Drawable r0 = r9.getDrawable()
            android.graphics.Rect r0 = r0.getBounds()
            r9.mRect = r0
            float[] r0 = new float[r1]
            android.graphics.Matrix r1 = r9.mCurMatrix
            r1.getValues(r0)
            r1 = r0[r2]
            r9.mLeft = r1
            r1 = 5
            r1 = r0[r1]
            r9.mTop = r1
            float r1 = r9.mLeft
            android.graphics.Rect r2 = r9.mRect
            int r2 = r2.width()
            float r2 = (float) r2
            r3 = r0[r6]
            float r2 = r2 * r3
            float r1 = r1 + r2
            r9.mRight = r1
            float r1 = r9.mTop
            android.graphics.Rect r2 = r9.mRect
            int r2 = r2.height()
            float r2 = (float) r2
            r0 = r0[r4]
            float r2 = r2 * r0
            float r1 = r1 + r2
            r9.mBottom = r1
            boolean r0 = r9.mScaleLarge
            if (r0 == 0) goto L_0x0250
            android.view.ViewParent r0 = r9.getParent()
            r0.requestDisallowInterceptTouchEvent(r5)
        L_0x0250:
            boolean r10 = super.onTouchEvent(r10)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.extend.feature.view.TZoomImageView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private float distance(MotionEvent motionEvent) {
        float x = motionEvent.getX(1) - motionEvent.getX(0);
        float y = motionEvent.getY(1) - motionEvent.getY(0);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private PointF midPoint(MotionEvent motionEvent) {
        return new PointF((motionEvent.getX(1) + motionEvent.getX(0)) / 2.0f, (motionEvent.getY(1) + motionEvent.getY(0)) / 2.0f);
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller.computeScrollOffset()) {
            float[] fArr = new float[9];
            this.mMatrix.getValues(fArr);
            fArr[2] = this.mScroller.getCurrX();
            fArr[5] = this.mScroller.getCurrY();
            fArr[0] = this.mScroller.getCurrZ();
            fArr[4] = this.mScroller.getCurrZ();
            this.mMatrix.setValues(fArr);
            setImageMatrix(this.mMatrix);
            invalidate();
        }
    }

    private void startScroll(Matrix matrix, Matrix matrix2) {
        if (matrix != null && matrix2 != null) {
            float[] fArr = new float[9];
            float[] fArr2 = new float[9];
            matrix.getValues(fArr);
            matrix2.getValues(fArr2);
            float f = fArr[2];
            float f2 = fArr2[2];
            float f3 = fArr[5];
            float f4 = fArr2[5];
            float f5 = fArr[0];
            this.mScroller.startScroll(f, f3, f5, f2 - f, f4 - f3, fArr2[0] - f5, 300);
            invalidate();
        }
    }
}
