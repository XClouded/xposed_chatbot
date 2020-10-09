package alimama.com.unwviewbase.pullandrefrsh;

import alimama.com.unwviewbase.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import androidx.core.view.MotionEventCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PullBase extends LinearLayout {
    private static final int INVALID_POINTER_ID = -1;
    private static final float MAX_PULL_SCROLL_FRICTION = 1.0f;
    private static final int SMOOTH_SCROLL_DURATION_MS = 350;
    private static final String STATE_SUPER = "pull_super";
    private int mActivePointerId = -1;
    private int mActivePointerIndex;
    private PullAdapter mContentView;
    private Mode mCurrentMode = Mode.getDefault();
    private SmoothScrollRunnable mCurrentSmoothScrollRunnable;
    private PullLayout mEndLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private float mLastMotionX;
    private float mLastMotionY;
    private Mode mMode = Mode.getDefault();
    private List<OnPullListener> mOnPullListeners;
    private Interpolator mPullInterpolator;
    private Interpolator mReleaseInterpolator;
    private List<OnBaseScrollListener> mScrollListeners;
    private PullLayout mStartLayout;
    private int mTouchSlop;

    public interface OnBaseScrollListener {
        void onScroll(PullBase pullBase, int i);
    }

    public interface OnPullListener {
        void onPull(PullBase pullBase, Mode mode, float f, int i);

        void onRelease(PullBase pullBase, Mode mode, float f, int i);

        void onReset(PullBase pullBase, Mode mode, float f, int i);
    }

    public interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }

    private int getPullScrollDuration() {
        return 350;
    }

    /* access modifiers changed from: protected */
    public boolean allowCatchPullEndTouch() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean allowCatchPullStartTouch() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean allowCheckPullWhenRollBack() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onContentViewAdded(ViewGroup viewGroup, View view) {
    }

    /* access modifiers changed from: protected */
    public void onContentViewRemoved(ViewGroup viewGroup, View view) {
    }

    /* access modifiers changed from: protected */
    public void onDirectionUpdated(Mode mode, int i) {
    }

    /* access modifiers changed from: protected */
    public void onModeUpdated(Mode mode) {
    }

    /* access modifiers changed from: protected */
    public void onPull(Mode mode, float f, int i) {
    }

    /* access modifiers changed from: protected */
    public void onPullRestoreInstanceState(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    public void onPullSaveInstanceState(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    public int onRelease(Mode mode, float f, int i) {
        return 0;
    }

    public PullBase(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public PullBase(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public PullBase(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public final void addView(View view) {
        checkChild(view);
        super.addView(view);
    }

    public final void addView(View view, int i) {
        checkChild(view);
        super.addView(view, i);
    }

    public final void addView(View view, ViewGroup.LayoutParams layoutParams) {
        checkChild(view);
        super.addView(view, layoutParams);
    }

    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        checkChild(view);
        super.addView(view, i, layoutParams);
    }

    private void checkChild(View view) {
        if (view != this.mStartLayout && view != this.mEndLayout) {
            if (getChildCount() > 0 || !(view instanceof PullAdapter)) {
                throw new IllegalStateException("PullBase can host only one direct and PullAdapter child");
            }
        }
    }

    public final void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof PullAdapter) {
            contentViewAdded(view);
        }
    }

    private void contentViewAdded(View view) {
        PullAdapter pullAdapter = (PullAdapter) view;
        this.mContentView = pullAdapter;
        onContentViewAdded(this, view);
        if (view instanceof PullAdapter) {
            pullAdapter.onPullAdapterAdded(this);
        }
        updateDirection();
    }

    public final void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (view instanceof PullAdapter) {
            contentViewRemoved(view);
        }
    }

    private void contentViewRemoved(View view) {
        this.mContentView = null;
        removeAllPullLayout();
        onContentViewRemoved(this, view);
        if (view instanceof PullAdapter) {
            ((PullAdapter) view).onPullAdapterRemoved(this);
        }
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.mOnPullListeners = new ArrayList();
        this.mScrollListeners = new ArrayList();
        setGravity(17);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.Pull);
        if (obtainStyledAttributes.hasValue(R.styleable.Pull_pullMode)) {
            this.mMode = Mode.mapIntToValue(obtainStyledAttributes.getInteger(R.styleable.Pull_pullMode, Mode.getDefault().getIntValue()));
        }
        this.mStartLayout = createStartPullLayout(context, Mode.PULL_FROM_START, attributeSet);
        this.mEndLayout = createEndPullLayout(context, Mode.PULL_FROM_END, attributeSet);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public PullLayout createStartPullLayout(Context context, Mode mode, AttributeSet attributeSet) {
        return new PullLayout(context, mode, getPullDirectionInternal(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public PullLayout createEndPullLayout(Context context, Mode mode, AttributeSet attributeSet) {
        return new PullLayout(context, mode, getPullDirectionInternal(), attributeSet);
    }

    public final void updateDirection() {
        if (!isContentInvalid()) {
            forceStopCurrentSmoothScrollTask();
            if (getPullDirectionInternal() != 1) {
                setOrientation(1);
            } else {
                setOrientation(0);
            }
            if (isInEditMode()) {
                clipPaddingForEditMode();
            } else {
                resetPullLayout();
            }
            if (this.mMode.permitsPullStart()) {
                this.mStartLayout.updateScrollDirection(this.mMode, getPullDirectionInternal());
            }
            if (this.mMode.permitsPullEnd()) {
                this.mEndLayout.updateScrollDirection(this.mMode, getPullDirectionInternal());
            }
            onDirectionUpdated(this.mMode, getPullDirectionInternal());
        }
    }

    private void removeAllPullLayout() {
        if (this == this.mStartLayout.getParent()) {
            removeView(this.mStartLayout);
        }
        if (this == this.mEndLayout.getParent()) {
            removeView(this.mEndLayout);
        }
    }

    /* access modifiers changed from: protected */
    public final boolean updateCurrentMode(Mode mode) {
        boolean isUnderPermit = this.mMode.isUnderPermit(mode);
        if (isUnderPermit) {
            if (mode == Mode.BOTH) {
                mode = Mode.PULL_FROM_START;
            }
            this.mCurrentMode = mode;
        }
        return isUnderPermit;
    }

    private LinearLayout.LayoutParams getPullLayoutParams() {
        if (getPullDirectionInternal() != 1) {
            return new LinearLayout.LayoutParams(-1, -2);
        }
        return new LinearLayout.LayoutParams(-2, -1);
    }

    private void updateMode(Mode mode, boolean z) {
        if ((this.mMode != mode || z) && mode != null) {
            this.mMode = mode;
            updateCurrentMode(mode);
            if (!isContentInvalid()) {
                if (isInEditMode()) {
                    clipPaddingForEditMode();
                } else {
                    resetPullLayout();
                }
                onModeUpdated(mode);
            }
        }
    }

    private void resetPullLayout() {
        removeAllPullLayout();
        LinearLayout.LayoutParams pullLayoutParams = getPullLayoutParams();
        if (this.mMode.permitsPullStart()) {
            super.addView(this.mStartLayout, 0, pullLayoutParams);
        }
        if (this.mMode.permitsPullEnd()) {
            super.addView(this.mEndLayout, pullLayoutParams);
        }
        refreshPullViewsSize();
    }

    private void clipPaddingForEditMode() {
        setPadding(0, 0, 0, 0);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0055, code lost:
        setPadding(r2, r1, r0, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0058, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000d, code lost:
        r1 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000e, code lost:
        r3 = 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void refreshPullViewsSize() {
        /*
            r4 = this;
            int r0 = r4.getMaximumPullScroll()
            int r1 = r4.getPullDirectionInternal()
            r2 = 0
            switch(r1) {
                case 0: goto L_0x0032;
                case 1: goto L_0x0010;
                default: goto L_0x000c;
            }
        L_0x000c:
            r0 = 0
        L_0x000d:
            r1 = 0
        L_0x000e:
            r3 = 0
            goto L_0x0055
        L_0x0010:
            alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode r1 = r4.mMode
            boolean r1 = r1.permitsPullStart()
            if (r1 == 0) goto L_0x001f
            alimama.com.unwviewbase.pullandrefrsh.PullLayout r1 = r4.mStartLayout
            r1.setWidth(r0)
            int r1 = -r0
            goto L_0x0020
        L_0x001f:
            r1 = 0
        L_0x0020:
            alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode r3 = r4.mMode
            boolean r3 = r3.permitsPullEnd()
            if (r3 == 0) goto L_0x0030
            alimama.com.unwviewbase.pullandrefrsh.PullLayout r3 = r4.mEndLayout
            r3.setWidth(r0)
            int r0 = -r0
            r2 = r1
            goto L_0x000d
        L_0x0030:
            r2 = r1
            goto L_0x000c
        L_0x0032:
            alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode r1 = r4.mMode
            boolean r1 = r1.permitsPullStart()
            if (r1 == 0) goto L_0x0041
            alimama.com.unwviewbase.pullandrefrsh.PullLayout r1 = r4.mStartLayout
            r1.setHeight(r0)
            int r1 = -r0
            goto L_0x0042
        L_0x0041:
            r1 = 0
        L_0x0042:
            alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode r3 = r4.mMode
            boolean r3 = r3.permitsPullEnd()
            if (r3 == 0) goto L_0x0053
            alimama.com.unwviewbase.pullandrefrsh.PullLayout r3 = r4.mEndLayout
            r3.setHeight(r0)
            int r0 = -r0
            r3 = r0
            r0 = 0
            goto L_0x0055
        L_0x0053:
            r0 = 0
            goto L_0x000e
        L_0x0055:
            r4.setPadding(r2, r1, r0, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwviewbase.pullandrefrsh.PullBase.refreshPullViewsSize():void");
    }

    /* access modifiers changed from: protected */
    public final boolean isRunnableScrolling() {
        if (this.mCurrentSmoothScrollRunnable != null) {
            return this.mCurrentSmoothScrollRunnable.mContinueRunning;
        }
        return false;
    }

    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        float f;
        float f2;
        if (isContentInvalid()) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        super.onInterceptTouchEvent(motionEvent);
        if (isRunnableScrolling()) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case 0:
                float y = motionEvent.getY();
                this.mInitialMotionY = y;
                this.mLastMotionY = y;
                float x = motionEvent.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                this.mIsBeingDragged = false;
                break;
            case 1:
            case 3:
                this.mIsBeingDragged = false;
                break;
            case 2:
                if (isReadyForPull()) {
                    float y2 = motionEvent.getY();
                    float x2 = motionEvent.getX();
                    if (getPullDirectionInternal() != 1) {
                        f2 = y2 - this.mLastMotionY;
                        f = x2 - this.mLastMotionX;
                    } else {
                        f2 = x2 - this.mLastMotionX;
                        f = y2 - this.mLastMotionY;
                    }
                    float abs = Math.abs(f2);
                    if (abs > ((float) this.mTouchSlop) && abs > Math.abs(f)) {
                        if (f2 < 1.0f || !isReadyForPullStartInternal()) {
                            if (f2 <= -1.0f && isReadyForPullEndInternal()) {
                                this.mLastMotionY = y2;
                                this.mLastMotionX = x2;
                                if (this.mMode.permitsPullEnd()) {
                                    this.mIsBeingDragged = allowCatchPullEndTouch();
                                    if (this.mIsBeingDragged) {
                                        updateCurrentMode(Mode.PULL_FROM_END);
                                        break;
                                    }
                                }
                            }
                        } else {
                            this.mLastMotionY = y2;
                            this.mLastMotionX = x2;
                            if (this.mMode.permitsPullStart()) {
                                this.mIsBeingDragged = allowCatchPullStartTouch();
                                if (this.mIsBeingDragged) {
                                    updateCurrentMode(Mode.PULL_FROM_START);
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
        }
        return this.mIsBeingDragged;
    }

    private float getActiveX(MotionEvent motionEvent) {
        try {
            return motionEvent.getX(this.mActivePointerIndex);
        } catch (Exception unused) {
            return motionEvent.getX();
        }
    }

    private float getActiveY(MotionEvent motionEvent) {
        try {
            return motionEvent.getY(this.mActivePointerIndex);
        } catch (Exception unused) {
            return motionEvent.getY();
        }
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if (isContentInvalid()) {
            return super.onTouchEvent(motionEvent);
        }
        int i = 0;
        if (isRunnableScrolling()) {
            return false;
        }
        super.onTouchEvent(motionEvent);
        if (motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case 0:
                if (isReadyForPull()) {
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.mActivePointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    float y = motionEvent.getY(0);
                    this.mInitialMotionY = y;
                    this.mLastMotionY = y;
                    float x = motionEvent.getX(0);
                    this.mInitialMotionX = x;
                    this.mLastMotionX = x;
                    return true;
                }
                break;
            case 1:
            case 3:
                this.mIsBeingDragged = false;
                resetMotionData();
                releaseEvent();
                break;
            case 2:
                if (this.mIsBeingDragged) {
                    this.mLastMotionY = getActiveY(motionEvent);
                    this.mLastMotionX = getActiveX(motionEvent);
                    pullEvent();
                    return true;
                }
                break;
            case 5:
                adjustNewPointer(motionEvent, MotionEventCompat.getActionIndex(motionEvent));
                return true;
            case 6:
                int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                if (motionEvent.getPointerId(actionIndex) != this.mActivePointerId) {
                    return true;
                }
                if (actionIndex == 0) {
                    i = 1;
                }
                adjustNewPointer(motionEvent, i);
                return true;
        }
        return false;
    }

    private void resetMotionData() {
        this.mActivePointerId = -1;
        this.mActivePointerIndex = 0;
        this.mInitialMotionY = 0.0f;
        this.mLastMotionY = 0.0f;
        this.mInitialMotionX = 0.0f;
        this.mLastMotionX = 0.0f;
    }

    private void adjustNewPointer(MotionEvent motionEvent, int i) {
        this.mActivePointerId = motionEvent.getPointerId(i);
        this.mActivePointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
        this.mInitialMotionX += motionEvent.getX(i) - this.mLastMotionX;
        this.mInitialMotionY += motionEvent.getY(i) - this.mLastMotionY;
        this.mLastMotionX = motionEvent.getX(i);
        this.mLastMotionY = motionEvent.getY(i);
    }

    private int generateDragValue() {
        float f;
        float f2;
        if (getPullDirectionInternal() != 1) {
            f2 = this.mInitialMotionY;
            f = this.mLastMotionY;
        } else {
            f2 = this.mInitialMotionX;
            f = this.mLastMotionX;
        }
        return Math.round(getInterpolation(f2 - f));
    }

    private float getInterpolation(float f) {
        if (this.mPullInterpolator != null) {
            return this.mPullInterpolator.getInterpolation(f);
        }
        return defaultInterpolation(f);
    }

    private float defaultInterpolation(float f) {
        if (f < 0.0f) {
            return -((float) Math.pow((double) (-f), 0.9090909090909091d));
        }
        return (float) Math.pow((double) f, 0.9090909090909091d);
    }

    private void pullEvent() {
        int generateDragValue = generateDragValue();
        switch (this.mCurrentMode) {
            case PULL_FROM_START:
                generateDragValue = Math.min(0, generateDragValue);
                break;
            case PULL_FROM_END:
                generateDragValue = Math.max(0, generateDragValue);
                break;
        }
        scrollPullLayoutTo(generateDragValue, true);
    }

    /* access modifiers changed from: private */
    public void releaseEvent() {
        smoothScrollTo(callOnRelease(), allowCheckPullWhenRollBack(), (OnSmoothScrollFinishedListener) new OnSmoothScrollFinishedListener() {
            public void onSmoothScrollFinished() {
                PullBase.this.callOnReset();
            }
        });
    }

    private int callOnRelease() {
        int scrollValue = getScrollValue();
        float maximumPullScroll = (((float) scrollValue) * 1.0f) / ((float) getMaximumPullScroll());
        int onRelease = onRelease(this.mCurrentMode, maximumPullScroll, scrollValue);
        for (OnPullListener onRelease2 : this.mOnPullListeners) {
            onRelease2.onRelease(this, this.mCurrentMode, maximumPullScroll, scrollValue);
        }
        return onRelease;
    }

    /* access modifiers changed from: protected */
    public void onReset(Mode mode, float f, int i) {
        updateCurrentMode(Mode.DISABLED);
    }

    /* access modifiers changed from: private */
    public void callOnReset() {
        int scrollValue = getScrollValue();
        float maximumPullScroll = (((float) scrollValue) * 1.0f) / ((float) getMaximumPullScroll());
        for (OnPullListener onReset : this.mOnPullListeners) {
            onReset.onReset(this, this.mCurrentMode, maximumPullScroll, scrollValue);
        }
        onReset(this.mCurrentMode, maximumPullScroll, scrollValue);
    }

    public final void simulateDrag(int i) {
        if (!isContentInvalid()) {
            boolean z = false;
            if (i < 0) {
                z = updateCurrentMode(Mode.PULL_FROM_START);
            } else if (i > 0) {
                z = updateCurrentMode(Mode.PULL_FROM_END);
            }
            if (z) {
                smoothScrollTo(i, true, (OnSmoothScrollFinishedListener) new OnSmoothScrollFinishedListener() {
                    public void onSmoothScrollFinished() {
                        PullBase.this.releaseEvent();
                    }
                });
            }
        }
    }

    /* access modifiers changed from: protected */
    public final Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        onPullSaveInstanceState(bundle);
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());
        return bundle;
    }

    /* access modifiers changed from: protected */
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER));
            onPullRestoreInstanceState(bundle);
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    /* access modifiers changed from: protected */
    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (!isContentInvalid()) {
            if (isInEditMode()) {
                clipPaddingForEditMode();
            } else {
                refreshPullViewsSize();
            }
            refreshContentViewSize(i, i2);
            post(new Runnable() {
                public void run() {
                    PullBase.this.requestLayout();
                }
            });
        }
    }

    private void refreshContentViewSize(int i, int i2) {
        ViewGroup.LayoutParams layoutParams;
        if (this.mContentView != null && (layoutParams = ((View) this.mContentView).getLayoutParams()) != null && (layoutParams instanceof LinearLayout.LayoutParams)) {
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            boolean z = false;
            if (i > 0 && layoutParams2.width != i) {
                layoutParams2.width = (i - layoutParams2.leftMargin) - layoutParams2.rightMargin;
                z = true;
            }
            if (i2 > 0 && layoutParams2.height != i2) {
                layoutParams2.height = (i2 - layoutParams2.topMargin) - layoutParams2.bottomMargin;
                z = true;
            }
            if (z) {
                ((View) this.mContentView).setLayoutParams(layoutParams2);
            }
        }
    }

    public final int getPullSize() {
        return getMaximumPullScroll();
    }

    private int getMaximumPullScroll() {
        if (getPullDirectionInternal() != 1) {
            return Math.round(((float) getHeight()) * 1.0f);
        }
        return Math.round(((float) getWidth()) * 1.0f);
    }

    private int checkScrollValue(int i) {
        int maximumPullScroll = getMaximumPullScroll();
        return Math.min(maximumPullScroll, Math.max(-maximumPullScroll, i));
    }

    private void callOnPull(int i) {
        float maximumPullScroll = (((float) i) * 1.0f) / ((float) getMaximumPullScroll());
        onPull(this.mCurrentMode, maximumPullScroll, i);
        for (OnPullListener onPull : this.mOnPullListeners) {
            onPull.onPull(this, this.mCurrentMode, maximumPullScroll, i);
        }
    }

    /* access modifiers changed from: private */
    public void scrollPullLayoutTo(int i, boolean z) {
        int checkScrollValue = checkScrollValue(i);
        if (z) {
            callOnPull(checkScrollValue);
        }
        callOnScroll(checkScrollValue);
        switch (getPullDirectionInternal()) {
            case 0:
                scrollTo(0, checkScrollValue);
                return;
            case 1:
                scrollTo(checkScrollValue, 0);
                return;
            default:
                return;
        }
    }

    private void callOnScroll(int i) {
        for (OnBaseScrollListener onScroll : this.mScrollListeners) {
            onScroll.onScroll(this, i);
        }
    }

    public void setPullInterpolator(Interpolator interpolator) {
        this.mPullInterpolator = interpolator;
    }

    public void setReleaseInterpolator(Interpolator interpolator) {
        this.mReleaseInterpolator = interpolator;
    }

    public void setMode(Mode mode) {
        updateMode(mode, false);
    }

    public final Mode getMode() {
        return this.mMode;
    }

    public final Mode getCurrentMode() {
        return this.mCurrentMode;
    }

    public PullLayout getEndLayout() {
        return this.mEndLayout;
    }

    public PullLayout getStartLayout() {
        return this.mStartLayout;
    }

    public final PullAdapter getPullAdapter() {
        return this.mContentView;
    }

    public final boolean isContentInvalid() {
        return this.mContentView == null || !(this.mContentView instanceof PullAdapter);
    }

    public final void addOnPullListener(OnPullListener onPullListener) {
        this.mOnPullListeners.add(onPullListener);
    }

    public final void removeOnPullListener(OnPullListener onPullListener) {
        this.mOnPullListeners.remove(onPullListener);
    }

    public void addOnScrollListener(OnBaseScrollListener onBaseScrollListener) {
        this.mScrollListeners.add(onBaseScrollListener);
    }

    public void removeOnScrollListener(OnBaseScrollListener onBaseScrollListener) {
        this.mScrollListeners.remove(onBaseScrollListener);
    }

    private boolean isReadyForPull() {
        return isReadyForPullEndInternal() || isReadyForPullStartInternal();
    }

    private boolean isReadyForPullStartInternal() {
        if (this.mContentView != null) {
            return this.mContentView.isReadyForPullStart();
        }
        return false;
    }

    private boolean isReadyForPullEndInternal() {
        if (this.mContentView != null) {
            return this.mContentView.isReadyForPullEnd();
        }
        return false;
    }

    public final int getPullDirectionInternal() {
        if (this.mContentView != null) {
            return this.mContentView.getPullDirection();
        }
        return 0;
    }

    private Interpolator checkReleaseInterpolator() {
        if (this.mReleaseInterpolator == null) {
            this.mReleaseInterpolator = new DecelerateInterpolator();
        }
        return this.mReleaseInterpolator;
    }

    /* access modifiers changed from: protected */
    public final int getScrollValue() {
        if (getPullDirectionInternal() != 1) {
            return getScrollY();
        }
        return getScrollX();
    }

    /* access modifiers changed from: protected */
    public final void smoothScrollTo(int i) {
        smoothScrollTo(i, checkReleaseInterpolator(), (long) getPullScrollDuration(), 0, false, (OnSmoothScrollFinishedListener) null);
    }

    /* access modifiers changed from: protected */
    public final void smoothScrollTo(int i, OnSmoothScrollFinishedListener onSmoothScrollFinishedListener) {
        smoothScrollTo(i, checkReleaseInterpolator(), (long) getPullScrollDuration(), 0, false, onSmoothScrollFinishedListener);
    }

    /* access modifiers changed from: protected */
    public final void smoothScrollTo(int i, long j, OnSmoothScrollFinishedListener onSmoothScrollFinishedListener) {
        smoothScrollTo(i, checkReleaseInterpolator(), (long) getPullScrollDuration(), j, false, onSmoothScrollFinishedListener);
    }

    /* access modifiers changed from: protected */
    public final void smoothScrollTo(int i, boolean z, OnSmoothScrollFinishedListener onSmoothScrollFinishedListener) {
        smoothScrollTo(i, checkReleaseInterpolator(), (long) getPullScrollDuration(), 0, z, onSmoothScrollFinishedListener);
    }

    private void smoothScrollTo(int i, Interpolator interpolator, long j, long j2, boolean z, OnSmoothScrollFinishedListener onSmoothScrollFinishedListener) {
        int i2 = i;
        long j3 = j2;
        OnSmoothScrollFinishedListener onSmoothScrollFinishedListener2 = onSmoothScrollFinishedListener;
        if (!isContentInvalid()) {
            if (this.mCurrentSmoothScrollRunnable != null) {
                if (this.mCurrentSmoothScrollRunnable.isStopped() || this.mCurrentSmoothScrollRunnable.getTargetValue() != i2) {
                    this.mCurrentSmoothScrollRunnable.stop();
                } else {
                    this.mCurrentSmoothScrollRunnable.appendScrollListener(onSmoothScrollFinishedListener2);
                    return;
                }
            }
            int scrollValue = getScrollValue();
            if (scrollValue != i2) {
                this.mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(this, interpolator, scrollValue, i, j, z, onSmoothScrollFinishedListener);
                if (j3 > 0) {
                    postDelayed(this.mCurrentSmoothScrollRunnable, j3);
                } else {
                    post(this.mCurrentSmoothScrollRunnable);
                }
            } else if (onSmoothScrollFinishedListener2 != null) {
                onSmoothScrollFinishedListener.onSmoothScrollFinished();
            }
        }
    }

    private void forceStopCurrentSmoothScrollTask() {
        if (this.mCurrentSmoothScrollRunnable != null) {
            this.mCurrentSmoothScrollRunnable.stop();
            scrollPullLayoutTo(0, false);
        }
    }

    private static class SmoothScrollRunnable implements Runnable {
        private boolean mCheckOnPull;
        /* access modifiers changed from: private */
        public boolean mContinueRunning;
        private int mCurrentValue;
        private long mDuration;
        private int mFromValue;
        private Interpolator mInterpolator;
        private OnSmoothScrollFinishedListener mListener;
        private Lock mLock;
        private List<OnSmoothScrollFinishedListener> mPendingListeners = new ArrayList();
        private long mStartTime;
        private int mToValue;
        private PullBase mView;

        public SmoothScrollRunnable(PullBase pullBase, Interpolator interpolator, int i, int i2, long j, boolean z, OnSmoothScrollFinishedListener onSmoothScrollFinishedListener) {
            if (pullBase == null || interpolator == null) {
                throw new IllegalArgumentException("view and interpolator param can not be null");
            }
            this.mView = pullBase;
            this.mInterpolator = interpolator;
            this.mFromValue = i;
            this.mToValue = i2;
            this.mDuration = j;
            this.mCheckOnPull = z;
            this.mListener = onSmoothScrollFinishedListener;
            this.mContinueRunning = true;
            this.mLock = new ReentrantLock();
            this.mCurrentValue = -1;
        }

        public void run() {
            if (this.mContinueRunning) {
                if (this.mStartTime == 0) {
                    this.mStartTime = System.currentTimeMillis();
                } else {
                    this.mCurrentValue = this.mFromValue - Math.round(((float) (this.mFromValue - this.mToValue)) * this.mInterpolator.getInterpolation(((float) Math.max(Math.min(((System.currentTimeMillis() - this.mStartTime) * 1000) / this.mDuration, 1000), 0)) / 1000.0f));
                    this.mView.scrollPullLayoutTo(this.mCurrentValue, this.mCheckOnPull);
                }
                if (this.mToValue != this.mCurrentValue) {
                    ViewCompat.postOnAnimation(this.mView, this);
                } else {
                    stop();
                }
            }
        }

        public void appendScrollListener(OnSmoothScrollFinishedListener onSmoothScrollFinishedListener) {
            if (onSmoothScrollFinishedListener != null) {
                this.mPendingListeners.add(onSmoothScrollFinishedListener);
            }
        }

        public int getTargetValue() {
            return this.mToValue;
        }

        public boolean isStopped() {
            return !this.mContinueRunning;
        }

        public void stop() {
            if (this.mContinueRunning) {
                this.mContinueRunning = false;
                if (this.mListener != null) {
                    this.mListener.onSmoothScrollFinished();
                }
                for (OnSmoothScrollFinishedListener onSmoothScrollFinished : this.mPendingListeners) {
                    onSmoothScrollFinished.onSmoothScrollFinished();
                }
            }
        }
    }

    public enum Mode {
        DISABLED(0),
        PULL_FROM_START(1),
        PULL_FROM_END(2),
        BOTH(3);
        
        private int mIntValue;

        public static Mode getDefault() {
            return DISABLED;
        }

        /* access modifiers changed from: package-private */
        public int getIntValue() {
            return this.mIntValue;
        }

        public static Mode mapIntToValue(int i) {
            for (Mode mode : values()) {
                if (i == mode.getIntValue()) {
                    return mode;
                }
            }
            return getDefault();
        }

        public boolean isUnderPermit(Mode mode) {
            if (mode == null) {
                return false;
            }
            switch (this) {
                case PULL_FROM_START:
                case PULL_FROM_END:
                    return this == mode || mode == DISABLED;
                case BOTH:
                case DISABLED:
                    break;
                default:
                    return false;
            }
        }

        private Mode(int i) {
            this.mIntValue = i;
        }

        public boolean permitsPullStart() {
            return this == PULL_FROM_START || this == BOTH;
        }

        public boolean permitsPullEnd() {
            return this == PULL_FROM_END || this == BOTH;
        }
    }
}
