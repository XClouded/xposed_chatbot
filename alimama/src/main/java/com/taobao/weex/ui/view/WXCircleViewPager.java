package com.taobao.weex.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import androidx.viewpager.widget.ViewPager;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.Field;

@SuppressLint({"HandlerLeak"})
public class WXCircleViewPager extends ViewPager implements WXGestureObservable {
    private final int SCROLL_TO_NEXT = 1;
    /* access modifiers changed from: private */
    public long intervalTime = TBToast.Duration.MEDIUM;
    private boolean isAutoScroll;
    private Handler mAutoScrollHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                WXLogUtils.d("[CircleViewPager] trigger auto play action");
                WXCircleViewPager.this.showNextItem();
                sendEmptyMessageDelayed(1, WXCircleViewPager.this.intervalTime);
                return;
            }
            super.handleMessage(message);
        }
    };
    private WXSmoothScroller mScroller;
    /* access modifiers changed from: private */
    public int mState = 0;
    /* access modifiers changed from: private */
    public boolean needLoop = true;
    private boolean scrollable = true;
    private WXGesture wxGesture;

    @SuppressLint({"NewApi"})
    public WXCircleViewPager(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOverScrollMode(2);
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrollStateChanged(int i) {
                int unused = WXCircleViewPager.this.mState = i;
                WXCirclePageAdapter circlePageAdapter = WXCircleViewPager.this.getCirclePageAdapter();
                int access$301 = WXCircleViewPager.super.getCurrentItem();
                if (WXCircleViewPager.this.needLoop && i == 0 && circlePageAdapter.getCount() > 1) {
                    if (access$301 == circlePageAdapter.getCount() - 1) {
                        WXCircleViewPager.this.superSetCurrentItem(1, false);
                    } else if (access$301 == 0) {
                        WXCircleViewPager.this.superSetCurrentItem(circlePageAdapter.getCount() - 2, false);
                    }
                }
            }
        });
        postInitViewPager();
    }

    private void postInitViewPager() {
        if (!isInEditMode()) {
            try {
                Field declaredField = ViewPager.class.getDeclaredField("mScroller");
                declaredField.setAccessible(true);
                Field declaredField2 = ViewPager.class.getDeclaredField("sInterpolator");
                declaredField2.setAccessible(true);
                this.mScroller = new WXSmoothScroller(getContext(), (Interpolator) declaredField2.get((Object) null));
                declaredField.set(this, this.mScroller);
            } catch (Exception e) {
                WXLogUtils.e("[CircleViewPager] postInitViewPager: ", (Throwable) e);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public WXCircleViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public int getCurrentItem() {
        return getRealCurrentItem();
    }

    public int superGetCurrentItem() {
        return super.getCurrentItem();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        try {
            return this.scrollable && super.onInterceptTouchEvent(motionEvent);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (ArrayIndexOutOfBoundsException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.scrollable) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void scrollTo(int i, int i2) {
        if (this.scrollable || this.mState != 1) {
            super.scrollTo(i, i2);
        }
    }

    public void startAutoScroll() {
        this.isAutoScroll = true;
        this.mAutoScrollHandler.removeCallbacksAndMessages((Object) null);
        this.mAutoScrollHandler.sendEmptyMessageDelayed(1, this.intervalTime);
    }

    public void pauseAutoScroll() {
        this.mAutoScrollHandler.removeCallbacksAndMessages((Object) null);
    }

    public void stopAutoScroll() {
        this.isAutoScroll = false;
        this.mAutoScrollHandler.removeCallbacksAndMessages((Object) null);
    }

    public boolean isAutoScroll() {
        return this.isAutoScroll;
    }

    public void setCurrentItem(int i) {
        setRealCurrentItem(i);
    }

    public WXCirclePageAdapter getCirclePageAdapter() {
        return (WXCirclePageAdapter) getAdapter();
    }

    public void setCirclePageAdapter(WXCirclePageAdapter wXCirclePageAdapter) {
        setAdapter(wXCirclePageAdapter);
    }

    public long getIntervalTime() {
        return this.intervalTime;
    }

    public void setIntervalTime(long j) {
        this.intervalTime = j;
    }

    public void setCircle(boolean z) {
        this.needLoop = z;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
            case 2:
                this.mAutoScrollHandler.removeCallbacksAndMessages((Object) null);
                break;
            case 1:
            case 3:
                if (isAutoScroll()) {
                    this.mAutoScrollHandler.sendEmptyMessageDelayed(1, this.intervalTime);
                    break;
                }
                break;
        }
        try {
            boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
            return this.wxGesture != null ? dispatchTouchEvent | this.wxGesture.onTouch(this, motionEvent) : dispatchTouchEvent;
        } catch (Exception unused) {
            return false;
        }
    }

    public void destory() {
        this.mAutoScrollHandler.removeCallbacksAndMessages((Object) null);
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public int getRealCurrentItem() {
        return ((WXCirclePageAdapter) getAdapter()).getRealPosition(super.getCurrentItem());
    }

    private void setRealCurrentItem(int i) {
        superSetCurrentItem(((WXCirclePageAdapter) getAdapter()).getFirst() + i, false);
    }

    /* access modifiers changed from: private */
    public void superSetCurrentItem(int i, boolean z) {
        try {
            super.setCurrentItem(i, z);
        } catch (IllegalStateException e) {
            WXLogUtils.e(e.toString());
            if (getAdapter() != null) {
                getAdapter().notifyDataSetChanged();
                super.setCurrentItem(i, z);
            }
        }
    }

    public int getRealCount() {
        return ((WXCirclePageAdapter) getAdapter()).getRealCount();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        try {
            super.onMeasure(i, i2);
        } catch (IllegalStateException e) {
            WXLogUtils.e(e.toString());
            if (getAdapter() != null) {
                getAdapter().notifyDataSetChanged();
                super.onMeasure(i, i2);
            }
        }
    }

    public boolean isScrollable() {
        return this.scrollable;
    }

    public void setScrollable(boolean z) {
        this.scrollable = z;
    }

    /* access modifiers changed from: private */
    public void showNextItem() {
        if (getCirclePageAdapter() == null || !getCirclePageAdapter().isRTL) {
            if (!this.needLoop && superGetCurrentItem() == getRealCount() - 1) {
                return;
            }
            if (getRealCount() == 2 && superGetCurrentItem() == 1) {
                superSetCurrentItem(0, true);
            } else {
                superSetCurrentItem(superGetCurrentItem() + 1, true);
            }
        } else if (!this.needLoop && superGetCurrentItem() == 0) {
        } else {
            if (getRealCount() == 2 && superGetCurrentItem() == 0) {
                superSetCurrentItem(1, true);
            } else {
                superSetCurrentItem(superGetCurrentItem() - 1, true);
            }
        }
    }
}
