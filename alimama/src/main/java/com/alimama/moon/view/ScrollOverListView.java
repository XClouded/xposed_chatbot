package com.alimama.moon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ScrollOverListView extends ListView {
    private static final String TAG = "ScrollOverListView";
    private int mBottomPosition;
    private int mLastFocusIndex = -1;
    private View mLastFocusView = null;
    private int mLastMotionMoveX;
    private int mLastMotionMoveY;
    private int mLastY;
    private float mMaxVelY = 0.0f;
    private int mMotionDownPos;
    private int mMotionDownX;
    private int mMotionDownY;
    private int mMotionUpPos;
    private int mMotionUpX;
    private int mMotionUpY;
    private OnItemSlipListener mOnItemSlipListener = new OnItemSlipListener() {
        public void onItemSlip(AdapterView<?> adapterView, View view, int i) {
            Toast.makeText(ScrollOverListView.this.getContext(), "onItemSlip", 0).show();
        }

        public void onItemSlipFocusClear(AdapterView<?> adapterView, View view, int i) {
            Toast.makeText(ScrollOverListView.this.getContext(), "onItemSlipFocusClear", 0).show();
        }
    };
    private OnScrollOverListener mOnScrollOverListener = new OnScrollOverListener() {
        public boolean onListViewBottomAndPullUp(int i) {
            return false;
        }

        public boolean onListViewTopAndPullDown(int i) {
            return false;
        }

        public boolean onMotionDown(MotionEvent motionEvent) {
            return false;
        }

        public boolean onMotionMove(MotionEvent motionEvent, int i) {
            return false;
        }

        public boolean onMotionUp(MotionEvent motionEvent) {
            return false;
        }
    };
    private int mTopPosition;
    private VelocityTracker mVelocityTracker = null;

    public interface OnItemSlipListener {
        void onItemSlip(AdapterView<?> adapterView, View view, int i);

        void onItemSlipFocusClear(AdapterView<?> adapterView, View view, int i);
    }

    public interface OnScrollOverListener {
        boolean onListViewBottomAndPullUp(int i);

        boolean onListViewTopAndPullDown(int i);

        boolean onMotionDown(MotionEvent motionEvent);

        boolean onMotionMove(MotionEvent motionEvent, int i);

        boolean onMotionUp(MotionEvent motionEvent);
    }

    public ScrollOverListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public ScrollOverListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ScrollOverListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mTopPosition = 0;
        this.mBottomPosition = 0;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!handlerScrollOverEvent(motionEvent) && !handlerItemSlipEvent(motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    private boolean handlerScrollOverEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int rawY = (int) motionEvent.getRawY();
        switch (action) {
            case 0:
                this.mLastY = rawY;
                boolean onMotionDown = this.mOnScrollOverListener.onMotionDown(motionEvent);
                if (onMotionDown) {
                    this.mLastY = rawY;
                    return onMotionDown;
                }
                break;
            case 1:
                if (this.mOnScrollOverListener.onMotionUp(motionEvent)) {
                    this.mLastY = rawY;
                    return true;
                }
                break;
            case 2:
                int childCount = getChildCount();
                if (childCount == 0) {
                    return super.onTouchEvent(motionEvent);
                }
                int count = getAdapter().getCount() - this.mBottomPosition;
                int i = rawY - this.mLastY;
                int top = getChildAt(0).getTop();
                int listPaddingTop = getListPaddingTop();
                int bottom = getChildAt(childCount - 1).getBottom();
                int height = getHeight() - getPaddingBottom();
                int firstVisiblePosition = getFirstVisiblePosition();
                if (this.mOnScrollOverListener.onMotionMove(motionEvent, i)) {
                    this.mLastY = rawY;
                    return true;
                } else if (firstVisiblePosition <= this.mTopPosition && top >= listPaddingTop && i > 0 && this.mOnScrollOverListener.onListViewTopAndPullDown(i)) {
                    this.mLastY = rawY;
                    return true;
                } else if (firstVisiblePosition + childCount >= count && bottom <= height && i < 0 && this.mOnScrollOverListener.onListViewBottomAndPullUp(i)) {
                    this.mLastY = rawY;
                    return true;
                }
        }
        this.mLastY = rawY;
        return false;
    }

    private boolean handlerItemSlipEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        switch (action) {
            case 0:
                this.mMaxVelY = 0.0f;
                this.mMotionDownX = x;
                this.mMotionDownY = y;
                this.mMotionDownPos = pointToPosition(x, y);
                if (this.mVelocityTracker != null) {
                    return false;
                }
                this.mVelocityTracker = VelocityTracker.obtain();
                return false;
            case 1:
                this.mMotionUpX = x;
                this.mMotionUpY = y;
                this.mMotionUpPos = pointToPosition(x, y);
                if (this.mMotionDownPos == this.mMotionUpPos && Math.abs(this.mMotionDownX - this.mMotionUpX) > 10 && Math.abs(this.mMotionDownY - this.mMotionUpY) < 20) {
                    if (this.mLastFocusIndex == -1) {
                        View childAt = getChildAt(this.mMotionUpPos - getFirstVisiblePosition());
                        this.mOnItemSlipListener.onItemSlip(this, childAt, this.mMotionUpPos);
                        this.mLastFocusIndex = this.mMotionUpPos;
                        this.mLastFocusView = childAt;
                    } else if (this.mMotionUpPos != this.mLastFocusIndex) {
                        this.mOnItemSlipListener.onItemSlipFocusClear(this, this.mLastFocusView, this.mLastFocusIndex);
                        View childAt2 = getChildAt(this.mMotionUpPos - getFirstVisiblePosition());
                        this.mOnItemSlipListener.onItemSlip(this, childAt2, this.mMotionUpPos);
                        this.mLastFocusIndex = this.mMotionUpPos;
                        this.mLastFocusView = childAt2;
                    } else if (this.mMotionUpPos == this.mLastFocusIndex) {
                        this.mOnItemSlipListener.onItemSlipFocusClear(this, this.mLastFocusView, this.mLastFocusIndex);
                        this.mLastFocusIndex = -1;
                        this.mLastFocusView = null;
                    }
                    return true;
                } else if (this.mLastFocusIndex == -1) {
                    return false;
                } else {
                    this.mOnItemSlipListener.onItemSlipFocusClear(this, this.mLastFocusView, this.mLastFocusIndex);
                    this.mLastFocusIndex = -1;
                    this.mLastFocusView = null;
                    return true;
                }
            case 2:
                this.mLastMotionMoveX = x;
                this.mLastMotionMoveY = y;
                try {
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    float yVelocity = this.mVelocityTracker.getYVelocity();
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    if (Math.abs(this.mMaxVelY) > Math.abs(yVelocity)) {
                        yVelocity = this.mMaxVelY;
                    }
                    this.mMaxVelY = Math.abs(yVelocity);
                    if (this.mMaxVelY <= 50.0f || this.mLastFocusIndex == -1) {
                        return false;
                    }
                    this.mOnItemSlipListener.onItemSlipFocusClear(this, this.mLastFocusView, this.mLastFocusIndex);
                    this.mLastFocusIndex = -1;
                    this.mLastFocusView = null;
                    return true;
                } catch (NullPointerException unused) {
                    return true;
                }
            default:
                return false;
        }
    }

    public void setTopPosition(int i) {
        if (getAdapter() == null) {
            throw new NullPointerException("You must set adapter before setTopPosition!");
        } else if (i >= 0) {
            this.mTopPosition = i;
        } else {
            throw new IllegalArgumentException("Top position must >= 0");
        }
    }

    public void setBottomPosition(int i) {
        if (getAdapter() == null) {
            throw new NullPointerException("You must set adapter before setBottonPosition!");
        } else if (i >= 0) {
            this.mBottomPosition = i;
        } else {
            throw new IllegalArgumentException("Bottom position must >= 0");
        }
    }

    public void setOnScrollOverListener(OnScrollOverListener onScrollOverListener) {
        this.mOnScrollOverListener = onScrollOverListener;
    }

    public void setOnItemSlipListener(OnItemSlipListener onItemSlipListener) {
        this.mOnItemSlipListener = onItemSlipListener;
    }
}
