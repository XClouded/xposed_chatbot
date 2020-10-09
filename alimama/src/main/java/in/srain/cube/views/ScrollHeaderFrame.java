package in.srain.cube.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.alimama.unionwl.uiframe.R;

public class ScrollHeaderFrame extends RelativeLayout {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "in.srain.cube.views.ScrollHeaderFrame";
    private int mContainerId;
    private ViewGroup mContentViewContainer;
    private int mCurrentPos;
    private boolean mDisabled;
    private View mHeaderContainer;
    private int mHeaderHeight;
    private int mHeaderId;
    private IScrollHeaderFrameHandler mIScrollHeaderFrameHandler;
    private int mLastPos;
    private long mLastTime;
    private PointF mPtLastMove;

    public ScrollHeaderFrame(Context context) {
        this(context, (AttributeSet) null);
    }

    public ScrollHeaderFrame(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScrollHeaderFrame(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurrentPos = 0;
        this.mLastPos = 0;
        this.mHeaderId = 0;
        this.mContainerId = 0;
        this.mDisabled = false;
        this.mPtLastMove = new PointF();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ScrollHeaderFrame, 0, 0);
        if (obtainStyledAttributes != null) {
            if (obtainStyledAttributes.hasValue(R.styleable.ScrollHeaderFrame_scrollheaderframe_header)) {
                this.mHeaderId = obtainStyledAttributes.getResourceId(R.styleable.ScrollHeaderFrame_scrollheaderframe_header, 0);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.ScrollHeaderFrame_scrollheaderframe_conent_container)) {
                this.mContainerId = obtainStyledAttributes.getResourceId(R.styleable.ScrollHeaderFrame_scrollheaderframe_conent_container, 0);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.ScrollHeaderFrame_scrollheaderframe_disable)) {
                this.mDisabled = obtainStyledAttributes.getBoolean(R.styleable.ScrollHeaderFrame_scrollheaderframe_disable, false);
            }
            obtainStyledAttributes.recycle();
        }
    }

    public View getContentView() {
        return this.mContentViewContainer;
    }

    public View getHeaderView() {
        return this.mHeaderContainer;
    }

    public void setHandler(IScrollHeaderFrameHandler iScrollHeaderFrameHandler) {
        this.mIScrollHeaderFrameHandler = iScrollHeaderFrameHandler;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mHeaderContainer = findViewById(this.mHeaderId);
        this.mContentViewContainer = (ViewGroup) findViewById(this.mContainerId);
        setDrawingCacheEnabled(false);
        setBackgroundDrawable((Drawable) null);
        setClipChildren(false);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int measuredHeight = this.mHeaderContainer.getMeasuredHeight();
        if (measuredHeight != 0) {
            int measuredWidth = getMeasuredWidth();
            int measuredHeight2 = getMeasuredHeight();
            int i5 = measuredHeight + 0;
            this.mHeaderContainer.layout(0, 0, measuredWidth, i5);
            this.mContentViewContainer.layout(0, i5, measuredWidth, measuredHeight2 + measuredHeight);
        }
    }

    private boolean tryToMove(float f) {
        if (f > 0.0f && this.mCurrentPos == 0) {
            return false;
        }
        if (f < 0.0f && this.mCurrentPos == (-this.mHeaderHeight)) {
            return false;
        }
        int i = this.mCurrentPos + ((int) f);
        int i2 = i < (-this.mHeaderHeight) ? -this.mHeaderHeight : i;
        if (i2 > 0) {
            i2 = 0;
        }
        return moveTo(i2);
    }

    private boolean moveTo(int i) {
        if (this.mCurrentPos == i) {
            return false;
        }
        int i2 = this.mCurrentPos;
        this.mCurrentPos = i;
        updatePos();
        return true;
    }

    private void updatePos() {
        scrollTo(0, -(this.mCurrentPos - this.mLastPos));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        this.mHeaderHeight = this.mHeaderContainer.getMeasuredHeight();
        this.mContentViewContainer.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(measuredHeight, Integer.MIN_VALUE));
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled() || this.mDisabled) {
            return super.dispatchTouchEvent(motionEvent);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        switch (motionEvent.getAction()) {
            case 0:
                this.mLastTime = motionEvent.getEventTime();
                this.mPtLastMove.set(motionEvent.getX(), motionEvent.getY());
                break;
            case 2:
                if (this.mHeaderHeight != 0) {
                    float y = (float) ((int) (motionEvent.getY() - this.mPtLastMove.y));
                    this.mPtLastMove.set(motionEvent.getX(), motionEvent.getY());
                    float abs = Math.abs(y / ((float) (this.mLastTime - motionEvent.getEventTime())));
                    this.mLastTime = motionEvent.getEventTime();
                    boolean z = true;
                    boolean z2 = y < 0.0f;
                    boolean z3 = this.mCurrentPos > (-this.mHeaderHeight);
                    boolean z4 = !z2;
                    if (this.mCurrentPos >= 0) {
                        z = false;
                    }
                    if (z4 && this.mIScrollHeaderFrameHandler != null && !this.mIScrollHeaderFrameHandler.hasReachTop()) {
                        return dispatchTouchEvent;
                    }
                    if (abs >= 5.0f && z4 && this.mIScrollHeaderFrameHandler != null) {
                        moveTo(0);
                        return dispatchTouchEvent;
                    } else if (abs < 5.0f || !z2 || this.mIScrollHeaderFrameHandler == null) {
                        if ((z2 && z3) || (z4 && z)) {
                            tryToMove(y);
                            break;
                        }
                    } else {
                        moveTo(-this.mHeaderHeight);
                        return dispatchTouchEvent;
                    }
                }
                break;
        }
        return dispatchTouchEvent;
    }
}
