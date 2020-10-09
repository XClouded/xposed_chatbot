package com.alimama.union.app.infrastructure.image.picPreviewer;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;

public class ImageTouchView extends EtaoDraweeView {
    static final int CLICK = 3;
    static final int DRAG = 1;
    static final int NONE = 0;
    static final int ZOOM = 2;
    Context context;
    PointF last = new PointF();
    float[] m;
    ScaleGestureDetector mScaleDetector;
    private boolean mViewCanMove = false;
    Matrix matrix;
    float maxScale = 4.0f;
    float minScale = 1.0f;
    int mode = 0;
    int oldMeasuredHeight;
    int oldMeasuredWidth;
    protected float origHeight;
    protected float origWidth;
    float saveScale = 1.0f;
    PointF start = new PointF();
    int viewHeight;
    int viewWidth;

    /* access modifiers changed from: package-private */
    public float getFixDragTrans(float f, float f2, float f3) {
        if (f3 <= f2) {
            return 0.0f;
        }
        return f;
    }

    /* access modifiers changed from: package-private */
    public float getFixTrans(float f, float f2, float f3) {
        float f4;
        float f5;
        if (f3 <= f2) {
            f4 = f2 - f3;
            f5 = 0.0f;
        } else {
            f5 = f2 - f3;
            f4 = 0.0f;
        }
        if (f < f5) {
            return (-f) + f5;
        }
        if (f > f4) {
            return (-f) + f4;
        }
        return 0.0f;
    }

    public ImageTouchView(Context context2) {
        super(context2);
        sharedConstructing(context2);
    }

    public ImageTouchView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        sharedConstructing(context2);
    }

    private void sharedConstructing(Context context2) {
        super.setClickable(true);
        this.context = context2;
        this.mScaleDetector = new ScaleGestureDetector(context2, new ScaleListener());
        this.matrix = new Matrix();
        this.m = new float[9];
        setImageMatrix(this.matrix);
        setScaleType(ImageView.ScaleType.MATRIX);
        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageTouchView.this.mScaleDetector.onTouchEvent(motionEvent);
                PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
                int action = motionEvent.getAction();
                if (action != 6) {
                    switch (action) {
                        case 0:
                            ImageTouchView.this.last.set(pointF);
                            ImageTouchView.this.start.set(ImageTouchView.this.last);
                            ImageTouchView.this.mode = 1;
                            break;
                        case 1:
                            ImageTouchView.this.mode = 0;
                            int abs = (int) Math.abs(pointF.x - ImageTouchView.this.start.x);
                            int abs2 = (int) Math.abs(pointF.y - ImageTouchView.this.start.y);
                            if (abs < 3 && abs2 < 3) {
                                ImageTouchView.this.performClick();
                                break;
                            }
                        case 2:
                            if (ImageTouchView.this.mode == 1) {
                                float f = pointF.x - ImageTouchView.this.last.x;
                                float f2 = pointF.y - ImageTouchView.this.last.y;
                                ImageTouchView.this.matrix.postTranslate(ImageTouchView.this.getFixDragTrans(f, (float) ImageTouchView.this.viewWidth, ImageTouchView.this.origWidth * ImageTouchView.this.saveScale), ImageTouchView.this.getFixDragTrans(f2, (float) ImageTouchView.this.viewHeight, ImageTouchView.this.origHeight * ImageTouchView.this.saveScale));
                                ImageTouchView.this.fixTrans();
                                ImageTouchView.this.last.set(pointF.x, pointF.y);
                                break;
                            }
                            break;
                    }
                } else {
                    ImageTouchView.this.mode = 0;
                }
                ImageTouchView.this.setImageMatrix(ImageTouchView.this.matrix);
                ImageTouchView.this.invalidate();
                return false;
            }
        });
    }

    public void setMaxZoom(float f) {
        this.maxScale = f;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            ImageTouchView.this.mode = 2;
            return true;
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f = ImageTouchView.this.saveScale;
            ImageTouchView.this.saveScale *= scaleFactor;
            if (ImageTouchView.this.saveScale > ImageTouchView.this.maxScale) {
                ImageTouchView.this.saveScale = ImageTouchView.this.maxScale;
                scaleFactor = ImageTouchView.this.maxScale / f;
            } else if (ImageTouchView.this.saveScale < ImageTouchView.this.minScale) {
                ImageTouchView.this.saveScale = ImageTouchView.this.minScale;
                scaleFactor = ImageTouchView.this.minScale / f;
            }
            if (ImageTouchView.this.origWidth * ImageTouchView.this.saveScale <= ((float) ImageTouchView.this.viewWidth) || ImageTouchView.this.origHeight * ImageTouchView.this.saveScale <= ((float) ImageTouchView.this.viewHeight)) {
                ImageTouchView.this.matrix.postScale(scaleFactor, scaleFactor, (float) (ImageTouchView.this.viewWidth / 2), (float) (ImageTouchView.this.viewHeight / 2));
            } else {
                ImageTouchView.this.matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            }
            ImageTouchView.this.fixTrans();
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public void fixTrans() {
        this.matrix.getValues(this.m);
        float f = this.m[2];
        float f2 = this.m[5];
        float fixTrans = getFixTrans(f, (float) this.viewWidth, this.origWidth * this.saveScale);
        float fixTrans2 = getFixTrans(f2, (float) this.viewHeight, this.origHeight * this.saveScale);
        this.mViewCanMove = false;
        if (fixTrans != 0.0f || fixTrans2 != 0.0f) {
            this.mViewCanMove = true;
            this.matrix.postTranslate(fixTrans, fixTrans2);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.viewWidth = View.MeasureSpec.getSize(i);
        this.viewHeight = View.MeasureSpec.getSize(i2);
        if ((this.oldMeasuredHeight != this.viewWidth || this.oldMeasuredHeight != this.viewHeight) && this.viewWidth != 0 && this.viewHeight != 0) {
            this.oldMeasuredHeight = this.viewHeight;
            this.oldMeasuredWidth = this.viewWidth;
            if (this.saveScale == 1.0f) {
                resetState();
            }
            fixTrans();
        }
    }

    public boolean viewCanMove() {
        return this.mViewCanMove;
    }

    public boolean pagerCanScroll() {
        return this.saveScale == this.minScale;
    }

    public void resetScale() {
        resetState();
        this.mViewCanMove = false;
        this.saveScale = 1.0f;
        invalidate();
    }

    private void resetState() {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            float f = (float) intrinsicWidth;
            float intrinsicHeight = (float) drawable.getIntrinsicHeight();
            float min = Math.min(((float) this.viewWidth) / f, ((float) this.viewHeight) / intrinsicHeight);
            this.matrix.setScale(min, min);
            float f2 = (((float) this.viewHeight) - (intrinsicHeight * min)) / 2.0f;
            float f3 = (((float) this.viewWidth) - (min * f)) / 2.0f;
            this.matrix.postTranslate(f3, f2);
            this.origWidth = ((float) this.viewWidth) - (f3 * 2.0f);
            this.origHeight = ((float) this.viewHeight) - (f2 * 2.0f);
            setImageMatrix(this.matrix);
        }
    }
}
