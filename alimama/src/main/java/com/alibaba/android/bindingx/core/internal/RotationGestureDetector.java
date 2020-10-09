package com.alibaba.android.bindingx.core.internal;

import android.view.MotionEvent;

public class RotationGestureDetector {
    private float mAnchorX;
    private float mAnchorY;
    private double mAngleDiff;
    private long mCurrTime;
    private boolean mInProgress;
    private OnRotationGestureListener mListener;
    private int[] mPointerIds = new int[2];
    private double mPrevAngle;
    private long mPrevTime;

    public interface OnRotationGestureListener {
        void onRotation(RotationGestureDetector rotationGestureDetector);

        void onRotationBegin(RotationGestureDetector rotationGestureDetector);

        void onRotationEnd(RotationGestureDetector rotationGestureDetector);
    }

    public RotationGestureDetector(OnRotationGestureListener onRotationGestureListener) {
        this.mListener = onRotationGestureListener;
    }

    private void updateCurrent(MotionEvent motionEvent) {
        this.mPrevTime = this.mCurrTime;
        this.mCurrTime = motionEvent.getEventTime();
        int findPointerIndex = motionEvent.findPointerIndex(this.mPointerIds[0]);
        int findPointerIndex2 = motionEvent.findPointerIndex(this.mPointerIds[1]);
        if (findPointerIndex != -1 && findPointerIndex2 != -1) {
            float x = motionEvent.getX(findPointerIndex);
            float y = motionEvent.getY(findPointerIndex);
            float x2 = motionEvent.getX(findPointerIndex2);
            float y2 = motionEvent.getY(findPointerIndex2);
            this.mAnchorX = (x + x2) * 0.5f;
            this.mAnchorY = (y + y2) * 0.5f;
            double d = -Math.atan2((double) (y2 - y), (double) (x2 - x));
            if (Double.isNaN(this.mPrevAngle)) {
                this.mAngleDiff = 0.0d;
            } else {
                this.mAngleDiff = this.mPrevAngle - d;
            }
            this.mPrevAngle = d;
            if (this.mAngleDiff > 3.141592653589793d) {
                this.mAngleDiff -= 3.141592653589793d;
            } else if (this.mAngleDiff < -3.141592653589793d) {
                this.mAngleDiff += 3.141592653589793d;
            }
            if (this.mAngleDiff > 1.5707963267948966d) {
                this.mAngleDiff -= 3.141592653589793d;
            } else if (this.mAngleDiff < -1.5707963267948966d) {
                this.mAngleDiff += 3.141592653589793d;
            }
        }
    }

    private void finish() {
        if (this.mInProgress) {
            this.mInProgress = false;
            this.mPointerIds[0] = -1;
            this.mPointerIds[1] = -1;
            if (this.mListener != null) {
                this.mListener.onRotationEnd(this);
            }
            this.mAngleDiff = 0.0d;
            this.mPrevAngle = 0.0d;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int pointerId;
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.mInProgress = false;
                this.mPointerIds[0] = motionEvent.getPointerId(motionEvent.getActionIndex());
                this.mPointerIds[1] = -1;
                break;
            case 1:
                finish();
                break;
            case 2:
                if (!(!this.mInProgress || this.mPointerIds[0] == -1 || this.mPointerIds[1] == -1)) {
                    updateCurrent(motionEvent);
                    if (!(this.mListener == null || getRotationInDegrees() == 0.0d)) {
                        this.mListener.onRotation(this);
                        break;
                    }
                }
            case 5:
                if (!this.mInProgress) {
                    this.mPointerIds[1] = motionEvent.getPointerId(motionEvent.getActionIndex());
                    this.mInProgress = true;
                    this.mPrevTime = motionEvent.getEventTime();
                    this.mPrevAngle = Double.NaN;
                    updateCurrent(motionEvent);
                    if (this.mListener != null) {
                        this.mListener.onRotationBegin(this);
                        break;
                    }
                }
                break;
            case 6:
                if (this.mInProgress && ((pointerId = motionEvent.getPointerId(motionEvent.getActionIndex())) == this.mPointerIds[0] || pointerId == this.mPointerIds[1])) {
                    finish();
                    break;
                }
        }
        return true;
    }

    public double getRotation() {
        return this.mAngleDiff;
    }

    public double getRotationInDegrees() {
        return Math.toDegrees(getRotation());
    }

    public long getTimeDelta() {
        return this.mCurrTime - this.mPrevTime;
    }

    public float getAnchorX() {
        return this.mAnchorX;
    }

    public float getAnchorY() {
        return this.mAnchorY;
    }
}
