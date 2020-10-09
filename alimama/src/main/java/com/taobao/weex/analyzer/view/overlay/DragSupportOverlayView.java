package com.taobao.weex.analyzer.view.overlay;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import androidx.annotation.NonNull;

public abstract class DragSupportOverlayView extends AbstractOverlayView implements View.OnTouchListener {
    private static boolean hasMoved = false;
    private float downX;
    private float downY;
    private boolean isDragEnabled;
    private int mCurrentX;
    private int mCurrentY;
    private float mDx;
    private float mDy;

    public DragSupportOverlayView(Context context) {
        super(context);
        this.mCurrentX = 0;
        this.mCurrentY = 0;
        this.isDragEnabled = true;
    }

    public DragSupportOverlayView(Context context, boolean z) {
        this(context);
        this.isDragEnabled = z;
    }

    /* access modifiers changed from: protected */
    public void onViewCreated(@NonNull View view) {
        super.onViewCreated(view);
        this.mCurrentX = this.mX;
        this.mCurrentY = this.mY;
        this.mWholeView.setOnTouchListener(this);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.isDragEnabled) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mDx = ((float) this.mCurrentX) - motionEvent.getRawX();
            this.mDy = ((float) this.mCurrentY) - motionEvent.getRawY();
            this.downX = motionEvent.getX();
            this.downY = motionEvent.getY();
            hasMoved = false;
        } else if (action == 2) {
            this.mCurrentX = (int) (motionEvent.getRawX() + this.mDx);
            this.mCurrentY = (int) (motionEvent.getRawY() + this.mDy);
            if (isValidMove(this.mContext, motionEvent.getX() - this.downX) || isValidMove(this.mContext, motionEvent.getY() - this.downY)) {
                updateViewPosition(this.mCurrentX, this.mCurrentY);
                hasMoved = true;
            }
        }
        return false;
    }

    private static boolean isValidMove(Context context, float f) {
        return hasMoved || ((float) ViewConfiguration.get(context).getScaledTouchSlop()) < Math.abs(f);
    }

    /* access modifiers changed from: protected */
    public void updateViewPosition(int i, int i2) {
        if (this.mWholeView != null && this.mWindowManager != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mWholeView.getLayoutParams();
            layoutParams.x = i;
            layoutParams.y = i2;
            this.mWindowManager.updateViewLayout(this.mWholeView, layoutParams);
            this.mCurrentX = i;
            this.mCurrentY = i2;
        }
    }

    public void setDragEnabled(boolean z) {
        this.isDragEnabled = z;
    }
}
