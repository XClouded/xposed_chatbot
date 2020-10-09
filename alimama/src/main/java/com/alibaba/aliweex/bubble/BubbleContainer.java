package com.alibaba.aliweex.bubble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import androidx.annotation.RequiresApi;
import com.alibaba.aliweex.bubble.BubbleEventCenter;
import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BubbleContainer extends ViewGroup implements GestureDetector.OnGestureListener, BubbleEventCenter.IBubbleAnimationListener {
    private static final int SWIPE_THRESHOLD = 50;
    private static final int SWIPE_VELOCITY_THRESHOLD = 50;
    private static final String TAG = "BubbleContainer";
    private static final int sScreenLock = 4608;
    private static final int sScreenOn = 4864;
    /* access modifiers changed from: private */
    public int clickedBubbleId = -1;
    private int lastX = -1;
    private int lastY = -1;
    private ArrayList<IAnimationListener> mAnimationListeners = new ArrayList<>();
    private HashMap<BubbleEventCenter.AnimationType, HashSet<BubbleAnimateWrapper>> mAnimationRecorder = new HashMap<>();
    private Set<Integer> mAppearedAnims = new HashSet();
    private ArrayList<IBubbleClickListener> mBubbleClickListeners = new ArrayList<>();
    private BubbleMode mBubbleMode = BubbleMode.Swipe;
    private ArrayList<BubblePosition> mBubblePositions = new ArrayList<>();
    private int mColumnCount;
    private final GestureDetector mGestureDetector = new GestureDetector(getContext(), this);
    private boolean mHasLayouted = false;
    private CopyOnWriteArrayList<BubbleAnimateWrapper> mHeadNailViews = new CopyOnWriteArrayList<>();
    private ArrayList<BubblePosition> mHeadNails = new ArrayList<>();
    private boolean mIsAnimationShow = false;
    private boolean mIsBubbleReplacing = false;
    private boolean mIsDetached = false;
    /* access modifiers changed from: private */
    public AtomicBoolean mIsLayoutAnimRunning = new AtomicBoolean(true);
    private boolean mIsPositionDirty = false;
    private boolean mIsReattached = false;
    private CopyOnWriteArrayList<BubbleAnimateWrapper> mPositionCache = new CopyOnWriteArrayList<>();
    private int mRowCount;
    private ScreenBroadcastReceiver mScreenReceiver = new ScreenBroadcastReceiver();
    /* access modifiers changed from: private */
    public int mScreenState = sScreenOn;
    private CopyOnWriteArrayList<BubbleAnimateWrapper> mTailNailViews = new CopyOnWriteArrayList<>();
    private ArrayList<BubblePosition> mTailNails = new ArrayList<>();
    private int mTotal = 18;
    /* access modifiers changed from: private */
    public ArrayList<BubbleAnimateWrapper> mWrapperList = new ArrayList<>();
    int moveTimes = 1;
    int touch_down_x = 0;

    public enum BubbleMode {
        Swipe,
        Scroll
    }

    public interface IAnimationListener {
        void onAnimationEnd(BubbleEventCenter.AnimationType animationType);

        void onAnimationStart(BubbleEventCenter.AnimationType animationType);
    }

    public interface IBubbleClickListener {
        void onClick(int i);
    }

    private void destroy() {
    }

    public void onCancel(BubbleEventCenter.AnimationType animationType, BubbleAnimateWrapper bubbleAnimateWrapper) {
    }

    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return true;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public void setBubbleMode(BubbleMode bubbleMode) {
        this.mBubbleMode = bubbleMode;
    }

    public BubbleContainer(Context context) {
        super(context);
    }

    public BubbleContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BubbleContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @RequiresApi(api = 21)
    public BubbleContainer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setTotal(int i) {
        if (i > 0 && i != this.mTotal) {
            this.mTotal = i;
            requestLayout();
        }
    }

    private void init() {
        setLayerType(2, (Paint) null);
        final AtomicInteger atomicInteger = new AtomicInteger();
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(scaleAnimation);
        layoutAnimationController.setOrder(2);
        layoutAnimationController.setDelay(0.1f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setInterpolator(new SpringInterpolator());
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                atomicInteger.incrementAndGet();
            }

            public void onAnimationEnd(Animation animation) {
                if (atomicInteger.decrementAndGet() == 0) {
                    Iterator it = BubbleContainer.this.mWrapperList.iterator();
                    while (it.hasNext()) {
                        ((BubbleAnimateWrapper) it.next()).enableFloating(true);
                    }
                    BubbleContainer.this.mIsLayoutAnimRunning.set(false);
                }
                animation.setAnimationListener((Animation.AnimationListener) null);
            }
        });
        setLayoutAnimation(layoutAnimationController);
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (i < 0 || i > this.mWrapperList.size()) {
            int size = this.mWrapperList.size();
            this.mWrapperList.add(size, new BubbleAnimateWrapper(view, size));
            view.setId(size);
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int unused = BubbleContainer.this.clickedBubbleId = view.getId();
                    return false;
                }
            });
        }
        super.addView(view, i, layoutParams);
    }

    public void removeView(View view) {
        int i = 0;
        while (true) {
            if (i >= this.mWrapperList.size()) {
                break;
            } else if (this.mWrapperList.get(i).getCurrentView() == view) {
                this.mWrapperList.remove(i);
                break;
            } else {
                i++;
            }
        }
        super.removeView(view);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int i3 = 0;
        if (this.mIsPositionDirty) {
            calculateBubbleInfo();
            this.mIsPositionDirty = false;
        }
        if (!this.mIsReattached && this.mScreenState != sScreenLock && !this.mHasLayouted && this.mWrapperList.size() >= this.mTotal) {
            int size = this.mBubblePositions.size() + 0;
            int childCount = getChildCount();
            if (size > childCount) {
                size = childCount;
            }
            int size2 = this.mBubblePositions.size();
            int i4 = 0;
            while (i3 < size2 && i4 < childCount) {
                BubblePosition bubblePosition = this.mBubblePositions.get(i3);
                View childAt = getChildAt(i4);
                this.mWrapperList.get(i4).setBubblePosition(bubblePosition);
                childAt.measure(View.MeasureSpec.makeMeasureSpec((int) bubblePosition.width, 1073741824), View.MeasureSpec.makeMeasureSpec((int) bubblePosition.height, 1073741824));
                i3++;
                i4++;
            }
            int size3 = this.mTailNails.size();
            int i5 = size;
            while (i5 < childCount && i4 < childCount) {
                BubblePosition bubblePosition2 = this.mTailNails.get((i5 - size) % size3);
                View childAt2 = getChildAt(i4);
                this.mWrapperList.get(i4).setBubblePosition(bubblePosition2);
                childAt2.measure(View.MeasureSpec.makeMeasureSpec((int) bubblePosition2.width, 1073741824), View.MeasureSpec.makeMeasureSpec((int) bubblePosition2.height, 1073741824));
                i5++;
                i4++;
            }
        }
    }

    public void setHeadNails(float[][] fArr) {
        if (fArr != null) {
            this.mHeadNails.clear();
            for (float[] fArr2 : fArr) {
                if (fArr2.length == 4) {
                    BubblePosition bubblePosition = new BubblePosition(fArr2);
                    bubblePosition.isNail = true;
                    this.mHeadNails.add(bubblePosition);
                }
            }
            this.mIsPositionDirty = true;
        }
    }

    public void setTailNails(float[][] fArr) {
        if (fArr != null) {
            this.mTailNails.clear();
            for (float[] fArr2 : fArr) {
                if (fArr2.length == 4) {
                    BubblePosition bubblePosition = new BubblePosition(fArr2);
                    bubblePosition.isNail = true;
                    this.mTailNails.add(bubblePosition);
                }
            }
            this.mIsPositionDirty = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mScreenState != sScreenLock && !this.mIsReattached && !this.mHasLayouted && this.mWrapperList.size() >= this.mTotal) {
            int size = this.mBubblePositions.size() + 0;
            int childCount = getChildCount();
            if (size > childCount) {
                size = childCount;
            }
            this.mHeadNailViews.clear();
            this.mPositionCache.clear();
            this.mAppearedAnims.clear();
            int size2 = this.mBubblePositions.size();
            int i5 = 0;
            for (int i6 = 0; i6 < size2 && i5 < childCount; i6++) {
                BubblePosition bubblePosition = this.mBubblePositions.get(i6);
                View childAt = getChildAt(i5);
                BubbleAnimateWrapper bubbleAnimateWrapper = this.mWrapperList.get(i5);
                bubbleAnimateWrapper.setBubblePosition(bubblePosition);
                childAt.layout((int) bubblePosition.x, (int) bubblePosition.y, (int) (bubblePosition.x + bubblePosition.width), (int) (bubblePosition.y + bubblePosition.height));
                this.mPositionCache.add(bubbleAnimateWrapper);
                i5++;
                this.mAppearedAnims.add(Integer.valueOf(bubbleAnimateWrapper.getViewIndex()));
            }
            this.mTailNailViews.clear();
            int size3 = this.mTailNails.size();
            int i7 = size;
            while (i7 < childCount && i5 < childCount) {
                BubblePosition bubblePosition2 = this.mTailNails.get((i7 - size) % size3);
                getChildAt(i5).layout((int) bubblePosition2.x, (int) bubblePosition2.y, (int) (bubblePosition2.x + bubblePosition2.width), (int) (bubblePosition2.y + bubblePosition2.height));
                BubbleAnimateWrapper bubbleAnimateWrapper2 = this.mWrapperList.get(i5);
                bubbleAnimateWrapper2.setBubblePosition(bubblePosition2);
                this.mTailNailViews.add(bubbleAnimateWrapper2);
                bubbleAnimateWrapper2.enableFloating(true);
                i7++;
                i5++;
            }
            if (this.mWrapperList.size() == this.mTotal) {
                this.mHasLayouted = true;
                init();
                startLayoutAnimation();
            }
        }
    }

    public void setRows(int i) {
        this.mRowCount = i;
        this.mIsPositionDirty = true;
    }

    private void calculateNailInfo() {
        if (this.mRowCount > 0 && !this.mBubblePositions.isEmpty()) {
            for (int i = 0; i < this.mHeadNails.size(); i++) {
                BubblePosition bubblePosition = this.mHeadNails.get(i);
                bubblePosition.row = i % this.mRowCount;
                bubblePosition.column = -1 - (i / this.mRowCount);
                if (i > 0) {
                    BubblePosition bubblePosition2 = this.mHeadNails.get(i);
                    if (bubblePosition2.row == bubblePosition.row && bubblePosition2.row + 1 == bubblePosition.row) {
                        bubblePosition2.setBottomSibling(bubblePosition);
                        bubblePosition.setTopSibling(bubblePosition2);
                    }
                }
            }
            for (int i2 = 0; i2 < this.mTailNails.size(); i2++) {
                BubblePosition bubblePosition3 = this.mTailNails.get(i2);
                bubblePosition3.row = i2 % this.mRowCount;
                bubblePosition3.column = this.mColumnCount + (i2 / this.mRowCount);
                if (i2 > 0) {
                    BubblePosition bubblePosition4 = this.mTailNails.get(i2);
                    if (bubblePosition4.row == bubblePosition3.row && bubblePosition4.row + 1 == bubblePosition3.row) {
                        bubblePosition4.setBottomSibling(bubblePosition3);
                        bubblePosition3.setTopSibling(bubblePosition4);
                    }
                }
            }
            int size = this.mHeadNails.size() >= this.mRowCount ? this.mRowCount : this.mHeadNails.size();
            for (int i3 = 0; i3 < size; i3++) {
                BubblePosition bubblePosition5 = this.mBubblePositions.get(i3);
                BubblePosition bubblePosition6 = this.mHeadNails.get(i3);
                bubblePosition5.setLeftSibling(bubblePosition6);
                bubblePosition6.setRightSibling(bubblePosition5);
            }
            int size2 = this.mTailNails.size() >= this.mRowCount ? this.mRowCount : this.mTailNails.size();
            int size3 = this.mBubblePositions.size();
            for (int i4 = 0; i4 < size2; i4++) {
                BubblePosition bubblePosition7 = this.mBubblePositions.get((size3 - size2) + i4);
                BubblePosition bubblePosition8 = this.mTailNails.get(i4);
                bubblePosition7.setRightSibling(bubblePosition8);
                bubblePosition8.setLeftSibling(bubblePosition7);
            }
        }
    }

    private void calculateBubbleInfo() {
        if (this.mRowCount > 0 && !this.mBubblePositions.isEmpty()) {
            double size = (double) this.mBubblePositions.size();
            Double.isNaN(size);
            double d = (double) this.mRowCount;
            Double.isNaN(d);
            this.mColumnCount = (int) Math.ceil((size * 1.0d) / d);
            Iterator<BubblePosition> it = this.mBubblePositions.iterator();
            int i = 0;
            int i2 = 0;
            while (it.hasNext()) {
                BubblePosition next = it.next();
                next.row = i2 % this.mRowCount;
                next.column = i2 / this.mRowCount;
                i2++;
            }
            int size2 = this.mBubblePositions.size();
            while (i < size2) {
                BubblePosition bubblePosition = this.mBubblePositions.get(i);
                i++;
                for (int i3 = i; i3 < size2; i3++) {
                    BubblePosition bubblePosition2 = this.mBubblePositions.get(i3);
                    if (bubblePosition.row == bubblePosition2.row && bubblePosition.column + 1 == bubblePosition2.column) {
                        bubblePosition.setRightSibling(bubblePosition2);
                        bubblePosition2.setLeftSibling(bubblePosition);
                    } else if (bubblePosition.column == bubblePosition2.column && bubblePosition.row + 1 == bubblePosition2.row) {
                        bubblePosition.setBottomSibling(bubblePosition2);
                        bubblePosition2.setTopSibling(bubblePosition);
                    }
                }
            }
            calculateNailInfo();
        }
    }

    public void setPositions(float[][] fArr) {
        if (fArr != null && this.mBubblePositions.isEmpty()) {
            this.mBubblePositions.clear();
            for (float[] fArr2 : fArr) {
                if (fArr2.length == 4) {
                    this.mBubblePositions.add(new BubblePosition(fArr2));
                    if (fArr2[2] > BubblePosition.sMaxWidth) {
                        BubblePosition.sMaxWidth = fArr2[2];
                    }
                    if (fArr2[3] > BubblePosition.sMaxHeight) {
                        BubblePosition.sMaxHeight = fArr2[3];
                    }
                }
            }
            this.mIsPositionDirty = true;
            if (!this.mIsAnimationShow) {
                startLayoutAnimation();
                this.mIsAnimationShow = true;
            }
        }
    }

    private boolean isAnimating() {
        for (Map.Entry<BubbleEventCenter.AnimationType, HashSet<BubbleAnimateWrapper>> value : this.mAnimationRecorder.entrySet()) {
            if (((HashSet) value.getValue()).size() > 0) {
                return true;
            }
        }
        return false;
    }

    public void swipeByFronted(int i) {
        swipe(i, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0194, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void swipe(int r9, boolean r10) {
        /*
            r8 = this;
            monitor-enter(r8)
            boolean r0 = r8.mIsBubbleReplacing     // Catch:{ all -> 0x0195 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r8)
            return
        L_0x0007:
            java.util.concurrent.atomic.AtomicBoolean r0 = r8.mIsLayoutAnimRunning     // Catch:{ all -> 0x0195 }
            boolean r0 = r0.get()     // Catch:{ all -> 0x0195 }
            if (r0 == 0) goto L_0x0011
            monitor-exit(r8)
            return
        L_0x0011:
            com.alibaba.aliweex.bubble.BubbleContainer$BubbleMode r0 = com.alibaba.aliweex.bubble.BubbleContainer.BubbleMode.Scroll     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubbleContainer$BubbleMode r1 = r8.mBubbleMode     // Catch:{ all -> 0x0195 }
            boolean r0 = r0.equals(r1)     // Catch:{ all -> 0x0195 }
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x003c
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r0 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x0195 }
            r3 = 0
        L_0x0024:
            boolean r4 = r0.hasNext()     // Catch:{ all -> 0x0195 }
            if (r4 == 0) goto L_0x0038
            java.lang.Object r4 = r0.next()     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubbleAnimateWrapper r4 = (com.alibaba.aliweex.bubble.BubbleAnimateWrapper) r4     // Catch:{ all -> 0x0195 }
            boolean r4 = r4.inBounceMode()     // Catch:{ all -> 0x0195 }
            if (r4 == 0) goto L_0x0024
            r3 = 1
            goto L_0x0024
        L_0x0038:
            if (r3 == 0) goto L_0x003c
            monitor-exit(r8)
            return
        L_0x003c:
            r0 = 256(0x100, float:3.59E-43)
            if (r9 != r0) goto L_0x00f1
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r0 = r8.mTailNailViews     // Catch:{ all -> 0x0195 }
            int r0 = r0.size()     // Catch:{ all -> 0x0195 }
            int r3 = r8.mRowCount     // Catch:{ all -> 0x0195 }
            if (r0 < r3) goto L_0x00db
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r0 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            int r0 = r0.size()     // Catch:{ all -> 0x0195 }
            java.util.ArrayList<com.alibaba.aliweex.bubble.BubblePosition> r3 = r8.mBubblePositions     // Catch:{ all -> 0x0195 }
            int r3 = r3.size()     // Catch:{ all -> 0x0195 }
            int r4 = r8.mRowCount     // Catch:{ all -> 0x0195 }
            int r3 = r3 - r4
            if (r0 > r3) goto L_0x005d
            goto L_0x00db
        L_0x005d:
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r0 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x0195 }
        L_0x0063:
            boolean r3 = r0.hasNext()     // Catch:{ all -> 0x0195 }
            if (r3 == 0) goto L_0x0087
            java.lang.Object r3 = r0.next()     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubbleAnimateWrapper r3 = (com.alibaba.aliweex.bubble.BubbleAnimateWrapper) r3     // Catch:{ all -> 0x0195 }
            r3.move(r9, r2, r1, r10)     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubblePosition r4 = r3.getPosition()     // Catch:{ all -> 0x0195 }
            if (r4 == 0) goto L_0x0063
            int r4 = r4.column     // Catch:{ all -> 0x0195 }
            if (r4 >= 0) goto L_0x0063
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mHeadNailViews     // Catch:{ all -> 0x0195 }
            addInOrder(r4, r3)     // Catch:{ all -> 0x0195 }
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            r4.remove(r3)     // Catch:{ all -> 0x0195 }
            goto L_0x0063
        L_0x0087:
            r0 = 0
            r3 = 0
        L_0x0089:
            int r4 = r8.mRowCount     // Catch:{ all -> 0x0195 }
            if (r0 >= r4) goto L_0x0193
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mTailNailViews     // Catch:{ all -> 0x0195 }
            int r4 = r4.size()     // Catch:{ all -> 0x0195 }
            if (r4 <= 0) goto L_0x0193
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mTailNailViews     // Catch:{ all -> 0x0195 }
            int r4 = r4.size()     // Catch:{ all -> 0x0195 }
            if (r0 > r4) goto L_0x0193
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mTailNailViews     // Catch:{ all -> 0x0195 }
            int r4 = r4.size()     // Catch:{ all -> 0x0195 }
            r5 = 0
        L_0x00a4:
            if (r5 >= r4) goto L_0x00d8
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r6 = r8.mTailNailViews     // Catch:{ all -> 0x0195 }
            java.lang.Object r6 = r6.get(r5)     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubbleAnimateWrapper r6 = (com.alibaba.aliweex.bubble.BubbleAnimateWrapper) r6     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubblePosition r7 = r6.getPosition()     // Catch:{ all -> 0x0195 }
            int r7 = r7.row     // Catch:{ all -> 0x0195 }
            if (r7 != r3) goto L_0x00d5
            r6.move(r9, r2, r2, r10)     // Catch:{ all -> 0x0195 }
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mTailNailViews     // Catch:{ all -> 0x0195 }
            r4.remove(r6)     // Catch:{ all -> 0x0195 }
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            addInOrder(r4, r6)     // Catch:{ all -> 0x0195 }
            java.util.Set<java.lang.Integer> r4 = r8.mAppearedAnims     // Catch:{ all -> 0x0195 }
            int r5 = r6.getViewIndex()     // Catch:{ all -> 0x0195 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0195 }
            r4.add(r5)     // Catch:{ all -> 0x0195 }
            int r3 = r3 + 1
            int r0 = r0 + 1
            goto L_0x0089
        L_0x00d5:
            int r5 = r5 + 1
            goto L_0x00a4
        L_0x00d8:
            int r0 = r0 + 1
            goto L_0x0089
        L_0x00db:
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r10 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            java.util.Iterator r10 = r10.iterator()     // Catch:{ all -> 0x0195 }
        L_0x00e1:
            boolean r0 = r10.hasNext()     // Catch:{ all -> 0x0195 }
            if (r0 == 0) goto L_0x0193
            java.lang.Object r0 = r10.next()     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubbleAnimateWrapper r0 = (com.alibaba.aliweex.bubble.BubbleAnimateWrapper) r0     // Catch:{ all -> 0x0195 }
            r0.edgeBounce(r9)     // Catch:{ all -> 0x0195 }
            goto L_0x00e1
        L_0x00f1:
            r0 = 512(0x200, float:7.175E-43)
            if (r9 != r0) goto L_0x0193
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r0 = r8.mHeadNailViews     // Catch:{ all -> 0x0195 }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x0195 }
            if (r0 == 0) goto L_0x0113
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r10 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            java.util.Iterator r10 = r10.iterator()     // Catch:{ all -> 0x0195 }
        L_0x0103:
            boolean r0 = r10.hasNext()     // Catch:{ all -> 0x0195 }
            if (r0 == 0) goto L_0x0193
            java.lang.Object r0 = r10.next()     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubbleAnimateWrapper r0 = (com.alibaba.aliweex.bubble.BubbleAnimateWrapper) r0     // Catch:{ all -> 0x0195 }
            r0.edgeBounce(r9)     // Catch:{ all -> 0x0195 }
            goto L_0x0103
        L_0x0113:
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r0 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x0195 }
        L_0x0119:
            boolean r3 = r0.hasNext()     // Catch:{ all -> 0x0195 }
            if (r3 == 0) goto L_0x013f
            java.lang.Object r3 = r0.next()     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubbleAnimateWrapper r3 = (com.alibaba.aliweex.bubble.BubbleAnimateWrapper) r3     // Catch:{ all -> 0x0195 }
            r3.move(r9, r2, r1, r10)     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubblePosition r4 = r3.getPosition()     // Catch:{ all -> 0x0195 }
            if (r4 == 0) goto L_0x0119
            int r4 = r4.column     // Catch:{ all -> 0x0195 }
            int r5 = r8.mColumnCount     // Catch:{ all -> 0x0195 }
            if (r4 < r5) goto L_0x0119
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mTailNailViews     // Catch:{ all -> 0x0195 }
            addInOrder(r4, r3)     // Catch:{ all -> 0x0195 }
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            r4.remove(r3)     // Catch:{ all -> 0x0195 }
            goto L_0x0119
        L_0x013f:
            r0 = 0
            r3 = 0
        L_0x0141:
            int r4 = r8.mRowCount     // Catch:{ all -> 0x0195 }
            if (r0 >= r4) goto L_0x0193
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mHeadNailViews     // Catch:{ all -> 0x0195 }
            int r4 = r4.size()     // Catch:{ all -> 0x0195 }
            if (r4 <= 0) goto L_0x0193
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mHeadNailViews     // Catch:{ all -> 0x0195 }
            int r4 = r4.size()     // Catch:{ all -> 0x0195 }
            if (r0 > r4) goto L_0x0193
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mHeadNailViews     // Catch:{ all -> 0x0195 }
            int r4 = r4.size()     // Catch:{ all -> 0x0195 }
            r5 = 0
        L_0x015c:
            if (r5 >= r4) goto L_0x0190
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r6 = r8.mHeadNailViews     // Catch:{ all -> 0x0195 }
            java.lang.Object r6 = r6.get(r5)     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubbleAnimateWrapper r6 = (com.alibaba.aliweex.bubble.BubbleAnimateWrapper) r6     // Catch:{ all -> 0x0195 }
            com.alibaba.aliweex.bubble.BubblePosition r7 = r6.getPosition()     // Catch:{ all -> 0x0195 }
            int r7 = r7.row     // Catch:{ all -> 0x0195 }
            if (r7 != r3) goto L_0x018d
            r6.move(r9, r2, r2, r10)     // Catch:{ all -> 0x0195 }
            java.util.Set<java.lang.Integer> r4 = r8.mAppearedAnims     // Catch:{ all -> 0x0195 }
            int r5 = r6.getViewIndex()     // Catch:{ all -> 0x0195 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0195 }
            r4.add(r5)     // Catch:{ all -> 0x0195 }
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mHeadNailViews     // Catch:{ all -> 0x0195 }
            r4.remove(r6)     // Catch:{ all -> 0x0195 }
            java.util.concurrent.CopyOnWriteArrayList<com.alibaba.aliweex.bubble.BubbleAnimateWrapper> r4 = r8.mPositionCache     // Catch:{ all -> 0x0195 }
            addInOrder(r4, r6)     // Catch:{ all -> 0x0195 }
            int r3 = r3 + 1
            int r0 = r0 + 1
            goto L_0x0141
        L_0x018d:
            int r5 = r5 + 1
            goto L_0x015c
        L_0x0190:
            int r0 = r0 + 1
            goto L_0x0141
        L_0x0193:
            monitor-exit(r8)
            return
        L_0x0195:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.bubble.BubbleContainer.swipe(int, boolean):void");
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        switch (motionEvent.getAction()) {
            case 0:
                this.touch_down_x = (int) motionEvent.getRawX();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case 1:
            case 3:
                this.moveTimes = 1;
                break;
            case 2:
                if (this.mBubbleMode == BubbleMode.Scroll) {
                    int rawX2 = ((int) motionEvent.getRawX()) - this.touch_down_x;
                    if (((float) Math.abs(rawX2)) > ((float) this.moveTimes) * (BubblePosition.sMaxWidth / 2.0f)) {
                        this.moveTimes++;
                        swipe(rawX2 < 0 ? 256 : 512, false);
                    }
                }
                if (Math.abs(rawX - this.lastX) + 0 >= Math.abs(rawY - this.lastY) + 0) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                this.lastX = rawX;
                this.lastY = rawY;
                break;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAnimationRecorder.put(BubbleEventCenter.AnimationType.EdgeBounceLeft, new HashSet());
        this.mAnimationRecorder.put(BubbleEventCenter.AnimationType.EdgeBounceRight, new HashSet());
        this.mAnimationRecorder.put(BubbleEventCenter.AnimationType.MoveLeft, new HashSet());
        this.mAnimationRecorder.put(BubbleEventCenter.AnimationType.MoveRight, new HashSet());
        this.mAnimationRecorder.put(BubbleEventCenter.AnimationType.ReplaceScale, new HashSet());
        BubbleEventCenter.getEventCenter().addBubbleAnimListener(this);
        if (this.mIsDetached) {
            this.mIsReattached = true;
        }
        this.mIsDetached = false;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        getContext().registerReceiver(this.mScreenReceiver, intentFilter);
        if (this.mIsReattached) {
            Iterator<BubbleAnimateWrapper> it = this.mWrapperList.iterator();
            while (it.hasNext()) {
                it.next().enableFloating(true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BubbleEventCenter.getEventCenter().removeBubbleAnimListener(this);
        this.mAnimationRecorder.clear();
        this.mIsDetached = true;
        this.mIsBubbleReplacing = false;
        getContext().unregisterReceiver(this.mScreenReceiver);
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        if (this.clickedBubbleId < 0) {
            return true;
        }
        Iterator<IBubbleClickListener> it = this.mBubbleClickListeners.iterator();
        while (it.hasNext()) {
            it.next().onClick(this.clickedBubbleId);
        }
        this.clickedBubbleId = -1;
        return true;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.mBubbleMode == BubbleMode.Scroll) {
            return false;
        }
        try {
            float x = motionEvent2.getX() - motionEvent.getX();
            if (Math.abs(x) <= Math.abs(motionEvent2.getY() - motionEvent.getY()) || Math.abs(x) <= 50.0f || Math.abs(f) <= 50.0f) {
                return false;
            }
            if (x > 0.0f) {
                swipe(512, false);
            } else {
                swipe(256, false);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONArray inViewBubbleList() {
        int size = this.mPositionCache.size();
        JSONArray jSONArray = new JSONArray(this.mPositionCache.size());
        for (int i = 0; i < size; i++) {
            jSONArray.add(Integer.valueOf(this.mPositionCache.get(i).getViewIndex()));
        }
        return jSONArray;
    }

    public JSONArray outViewBubbleList() {
        JSONArray jSONArray = new JSONArray();
        Iterator<BubbleAnimateWrapper> it = this.mWrapperList.iterator();
        while (it.hasNext()) {
            BubbleAnimateWrapper next = it.next();
            if (!this.mPositionCache.contains(next)) {
                jSONArray.add(Integer.valueOf(next.getViewIndex()));
            }
        }
        return jSONArray;
    }

    public void resetBubbles() {
        if (this.mWrapperList != null && this.mWrapperList.size() != 0) {
            int size = this.mBubblePositions.size() + 0;
            int childCount = getChildCount();
            if (size > childCount) {
                size = childCount;
            }
            int size2 = this.mBubblePositions.size();
            int i = 0;
            int i2 = 0;
            while (i < size2 && i2 < childCount) {
                this.mWrapperList.get(i2).setBubblePosition(this.mBubblePositions.get(i));
                i++;
                i2++;
            }
            int size3 = this.mTailNails.size();
            int i3 = size;
            while (i3 < childCount && i2 < childCount) {
                this.mWrapperList.get(i2).setBubblePosition(this.mTailNails.get((i3 - size) % size3));
                i3++;
                i2++;
            }
            for (int i4 = 0; i4 < this.mWrapperList.size(); i4++) {
                this.mWrapperList.get(i4).resetSpringAnimation(2000.0f);
            }
            this.mIsPositionDirty = true;
            this.mHasLayouted = false;
            requestLayout();
        }
    }

    public void replaceBubble(int i, int i2) {
        if (!isAnimating() && !this.mIsDetached && i2 <= this.mBubblePositions.size() && i2 >= 0 && i >= 0 && i <= this.mWrapperList.size()) {
            BubbleAnimateWrapper bubbleAnimateWrapper = null;
            Iterator<BubbleAnimateWrapper> it = this.mWrapperList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BubbleAnimateWrapper next = it.next();
                if (next.getViewIndex() == i) {
                    bubbleAnimateWrapper = next;
                    break;
                }
            }
            BubblePosition bubblePosition = this.mBubblePositions.get(i2);
            if (bubbleAnimateWrapper != null && bubblePosition != null && !bubblePosition.equals(bubbleAnimateWrapper.getPosition()) && !this.mAppearedAnims.contains(Integer.valueOf(i)) && this.mHeadNailViews.indexOf(bubbleAnimateWrapper) == -1) {
                this.mIsBubbleReplacing = true;
                if (this.mTailNailViews.indexOf(bubbleAnimateWrapper) != -1) {
                    this.mTailNailViews.remove(bubbleAnimateWrapper);
                }
                Iterator<BubbleAnimateWrapper> it2 = this.mPositionCache.iterator();
                while (it2.hasNext()) {
                    BubbleAnimateWrapper next2 = it2.next();
                    BubblePosition position = next2.getPosition();
                    if (position != null && !position.isNail) {
                        if (bubbleAnimateWrapper == next2 || position.row != bubblePosition.row || position.column < bubblePosition.column) {
                            next2.gravityMove(bubblePosition);
                        } else {
                            next2.move(512, false, false, false);
                            BubblePosition position2 = next2.getPosition();
                            if (position2 != null && position2.column >= this.mColumnCount) {
                                addInOrder(this.mTailNailViews, next2);
                                this.mPositionCache.remove(next2);
                            }
                        }
                    }
                }
                if (!this.mPositionCache.contains(bubbleAnimateWrapper)) {
                    addInOrder(this.mPositionCache, bubbleAnimateWrapper);
                }
                bubbleAnimateWrapper.scaleBounce(bubblePosition);
                this.mAppearedAnims.add(Integer.valueOf(bubbleAnimateWrapper.getViewIndex()));
            }
        }
    }

    public void onStart(BubbleEventCenter.AnimationType animationType, BubbleAnimateWrapper bubbleAnimateWrapper) {
        HashSet hashSet = this.mAnimationRecorder.get(animationType);
        if (hashSet == null) {
            hashSet = new HashSet();
            this.mAnimationRecorder.put(animationType, hashSet);
        }
        if (hashSet.size() == 0) {
            switch (animationType) {
                case MoveLeft:
                case MoveRight:
                case ScrollLeft:
                case ScrollRight:
                case EdgeBounceLeft:
                case EdgeBounceRight:
                    Iterator<IAnimationListener> it = this.mAnimationListeners.iterator();
                    while (it.hasNext()) {
                        it.next().onAnimationStart(animationType);
                    }
                    break;
            }
        }
        hashSet.add(bubbleAnimateWrapper);
    }

    public void onEnd(BubbleEventCenter.AnimationType animationType, BubbleAnimateWrapper bubbleAnimateWrapper) {
        Set set = this.mAnimationRecorder.get(animationType);
        if (set != null) {
            set.remove(bubbleAnimateWrapper);
            if (set.size() == 0) {
                switch (animationType) {
                    case MoveLeft:
                    case MoveRight:
                    case ScrollLeft:
                    case ScrollRight:
                    case EdgeBounceLeft:
                    case EdgeBounceRight:
                        Iterator<IAnimationListener> it = this.mAnimationListeners.iterator();
                        while (it.hasNext()) {
                            it.next().onAnimationEnd(animationType);
                        }
                        return;
                    case ReplaceScale:
                        this.mIsBubbleReplacing = false;
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void addAnimationCallback(IAnimationListener iAnimationListener) {
        if (!this.mAnimationListeners.contains(iAnimationListener)) {
            this.mAnimationListeners.add(iAnimationListener);
        }
    }

    public void addBubbleClickCallback(IBubbleClickListener iBubbleClickListener) {
        if (!this.mBubbleClickListeners.contains(iBubbleClickListener)) {
            this.mBubbleClickListeners.add(iBubbleClickListener);
        }
    }

    private static int addInOrder(List<BubbleAnimateWrapper> list, BubbleAnimateWrapper bubbleAnimateWrapper) {
        if (list.contains(bubbleAnimateWrapper)) {
            return -1;
        }
        int size = list.size();
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= size) {
                break;
            }
            BubbleAnimateWrapper bubbleAnimateWrapper2 = list.get(i2);
            if (bubbleAnimateWrapper.getPosition().column == bubbleAnimateWrapper2.getPosition().column && bubbleAnimateWrapper.getPosition().row > bubbleAnimateWrapper2.getPosition().row) {
                i = i2 + 1;
                break;
            }
            i2++;
        }
        list.add(i, bubbleAnimateWrapper);
        return i;
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private ScreenBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                int unused = BubbleContainer.this.mScreenState = BubbleContainer.sScreenOn;
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                int unused2 = BubbleContainer.this.mScreenState = BubbleContainer.sScreenLock;
            } else if ("android.intent.action.USER_PRESENT".equals(action)) {
                int unused3 = BubbleContainer.this.mScreenState = BubbleContainer.sScreenOn;
            }
        }
    }
}
