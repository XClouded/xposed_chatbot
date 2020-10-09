package com.taobao.weex.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.view.BaseFrameLayout;
import com.taobao.weex.ui.view.WXCircleIndicator;
import com.taobao.weex.ui.view.WXCirclePageAdapter;
import com.taobao.weex.ui.view.WXCircleViewPager;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component(lazyload = false)
public class WXSlider extends WXVContainer<FrameLayout> {
    public static final String INDEX = "index";
    public static final String INFINITE = "infinite";
    /* access modifiers changed from: private */
    public int initIndex;
    /* access modifiers changed from: private */
    public Runnable initRunnable;
    private boolean isInfinite;
    private boolean keepIndex;
    protected WXCirclePageAdapter mAdapter;
    protected WXIndicator mIndicator;
    protected ViewPager.OnPageChangeListener mPageChangeListener;
    protected boolean mShowIndicators;
    WXCircleViewPager mViewPager;
    /* access modifiers changed from: private */
    public float offsetXAccuracy;
    Map<String, Object> params;

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXSlider(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @Deprecated
    public WXSlider(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXSlider(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.isInfinite = true;
        this.params = new HashMap();
        this.offsetXAccuracy = 0.1f;
        this.initIndex = -1;
        this.keepIndex = false;
        this.mShowIndicators = true;
        this.mPageChangeListener = new SliderPageChangeListener();
    }

    /* access modifiers changed from: protected */
    public BaseFrameLayout initComponentHostView(@NonNull Context context) {
        BaseFrameLayout baseFrameLayout = new BaseFrameLayout(context);
        if (getAttrs() != null) {
            this.isInfinite = WXUtils.getBoolean(getAttrs().get(INFINITE), true).booleanValue();
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        this.mViewPager = new WXCircleViewPager(context);
        this.mViewPager.setCircle(this.isInfinite);
        this.mViewPager.setLayoutParams(layoutParams);
        this.mAdapter = new WXCirclePageAdapter(this.isInfinite);
        this.mViewPager.setAdapter(this.mAdapter);
        baseFrameLayout.addView(this.mViewPager);
        this.mViewPager.addOnPageChangeListener(this.mPageChangeListener);
        registerActivityStateListener();
        return baseFrameLayout;
    }

    public ViewGroup.LayoutParams getChildLayoutParams(WXComponent wXComponent, View view, int i, int i2, int i3, int i4, int i5, int i6) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new FrameLayout.LayoutParams(i, i2);
        } else {
            layoutParams.width = i;
            layoutParams.height = i2;
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            if (wXComponent instanceof WXIndicator) {
                setMarginsSupportRTL((ViewGroup.MarginLayoutParams) layoutParams, i3, i5, i4, i6);
            } else {
                setMarginsSupportRTL((ViewGroup.MarginLayoutParams) layoutParams, 0, 0, 0, 0);
            }
        }
        return layoutParams;
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if ("scroll".equals(str) && this.mViewPager != null) {
            this.mViewPager.addOnPageChangeListener(new SliderOnScrollListener(this));
        }
    }

    public boolean containsGesture(WXGestureType wXGestureType) {
        return super.containsGesture(wXGestureType);
    }

    public ViewGroup getRealView() {
        return this.mViewPager;
    }

    public void addSubView(View view, int i) {
        if (view != null && this.mAdapter != null && !(view instanceof WXCircleIndicator)) {
            this.mAdapter.addPageView(view);
            hackTwoItemsInfiniteScroll();
            if (this.initIndex != -1 && this.mAdapter.getRealCount() > this.initIndex) {
                if (this.initRunnable == null) {
                    this.initRunnable = new Runnable() {
                        public void run() {
                            int unused = WXSlider.this.initIndex = WXSlider.this.getInitIndex();
                            WXSlider.this.mViewPager.setCurrentItem(WXSlider.this.getRealIndex(WXSlider.this.initIndex));
                            int unused2 = WXSlider.this.initIndex = -1;
                            Runnable unused3 = WXSlider.this.initRunnable = null;
                        }
                    };
                }
                this.mViewPager.removeCallbacks(this.initRunnable);
                this.mViewPager.postDelayed(this.initRunnable, 50);
            } else if (!this.keepIndex) {
                this.mViewPager.setCurrentItem(getRealIndex(0));
            }
            if (this.mIndicator != null) {
                ((WXCircleIndicator) this.mIndicator.getHostView()).forceLayout();
                ((WXCircleIndicator) this.mIndicator.getHostView()).requestLayout();
            }
        }
    }

    public void setLayout(WXComponent wXComponent) {
        if (this.mAdapter != null) {
            this.mAdapter.setLayoutDirectionRTL(isLayoutRTL());
        }
        super.setLayout(wXComponent);
    }

    public void remove(WXComponent wXComponent, boolean z) {
        if (wXComponent != null && wXComponent.getHostView() != null && this.mAdapter != null) {
            this.mAdapter.removePageView(wXComponent.getHostView());
            hackTwoItemsInfiniteScroll();
            super.remove(wXComponent, z);
        }
    }

    public void destroy() {
        super.destroy();
        if (this.mViewPager != null) {
            this.mViewPager.stopAutoScroll();
            this.mViewPager.removeAllViews();
            this.mViewPager.destory();
        }
    }

    public void onActivityResume() {
        super.onActivityResume();
        if (this.mViewPager != null && this.mViewPager.isAutoScroll()) {
            this.mViewPager.startAutoScroll();
        }
    }

    public void onActivityStop() {
        super.onActivityStop();
        if (this.mViewPager != null) {
            this.mViewPager.pauseAutoScroll();
        }
    }

    public void addIndicator(WXIndicator wXIndicator) {
        FrameLayout frameLayout = (FrameLayout) getHostView();
        if (frameLayout != null) {
            this.mIndicator = wXIndicator;
            this.mIndicator.setShowIndicators(this.mShowIndicators);
            WXCircleIndicator wXCircleIndicator = (WXCircleIndicator) wXIndicator.getHostView();
            if (wXCircleIndicator != null) {
                wXCircleIndicator.setCircleViewPager(this.mViewPager);
                frameLayout.addView(wXCircleIndicator);
            }
        }
    }

    /* access modifiers changed from: private */
    public int getInitIndex() {
        int intValue = WXUtils.getInteger(getAttrs().get("index"), Integer.valueOf(this.initIndex)).intValue();
        if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            return 0;
        }
        return intValue >= this.mAdapter.getRealCount() ? intValue % this.mAdapter.getRealCount() : intValue;
    }

    /* access modifiers changed from: private */
    public int getRealIndex(int i) {
        if (this.mAdapter.getRealCount() > 0) {
            if (i >= this.mAdapter.getRealCount()) {
                i = this.mAdapter.getRealCount() - 1;
            }
            if (isLayoutRTL()) {
                i = (this.mAdapter.getRealCount() - 1) - i;
            }
        }
        return i + 0;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r5, java.lang.Object r6) {
        /*
            r4 = this;
            int r0 = r5.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case -1768064947: goto L_0x0050;
                case 66669991: goto L_0x0046;
                case 100346066: goto L_0x003c;
                case 111972721: goto L_0x0032;
                case 570418373: goto L_0x0028;
                case 996926241: goto L_0x001e;
                case 1438608771: goto L_0x0014;
                case 1565939262: goto L_0x000a;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x005a
        L_0x000a:
            java.lang.String r0 = "offsetXAccuracy"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x005a
            r0 = 5
            goto L_0x005b
        L_0x0014:
            java.lang.String r0 = "autoPlay"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x005a
            r0 = 1
            goto L_0x005b
        L_0x001e:
            java.lang.String r0 = "showIndicators"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x005a
            r0 = 2
            goto L_0x005b
        L_0x0028:
            java.lang.String r0 = "interval"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x005a
            r0 = 3
            goto L_0x005b
        L_0x0032:
            java.lang.String r0 = "value"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x005a
            r0 = 0
            goto L_0x005b
        L_0x003c:
            java.lang.String r0 = "index"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x005a
            r0 = 4
            goto L_0x005b
        L_0x0046:
            java.lang.String r0 = "scrollable"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x005a
            r0 = 6
            goto L_0x005b
        L_0x0050:
            java.lang.String r0 = "keepIndex"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x005a
            r0 = 7
            goto L_0x005b
        L_0x005a:
            r0 = -1
        L_0x005b:
            r3 = 0
            switch(r0) {
                case 0: goto L_0x00cf;
                case 1: goto L_0x00c5;
                case 2: goto L_0x00bb;
                case 3: goto L_0x00ad;
                case 4: goto L_0x009f;
                case 5: goto L_0x0083;
                case 6: goto L_0x0073;
                case 7: goto L_0x0064;
                default: goto L_0x005f;
            }
        L_0x005f:
            boolean r5 = super.setProperty(r5, r6)
            return r5
        L_0x0064:
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r5 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r5)
            boolean r5 = r5.booleanValue()
            r4.keepIndex = r5
            return r2
        L_0x0073:
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r2)
            java.lang.Boolean r5 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r5)
            boolean r5 = r5.booleanValue()
            r4.setScrollable(r5)
            return r2
        L_0x0083:
            r5 = 1036831949(0x3dcccccd, float:0.1)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r6, r5)
            float r6 = r5.floatValue()
            r0 = 0
            int r6 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r6 == 0) goto L_0x009e
            float r5 = r5.floatValue()
            r4.setOffsetXAccuracy(r5)
        L_0x009e:
            return r2
        L_0x009f:
            java.lang.Integer r5 = com.taobao.weex.utils.WXUtils.getInteger(r6, r3)
            if (r5 == 0) goto L_0x00ac
            int r5 = r5.intValue()
            r4.setIndex(r5)
        L_0x00ac:
            return r2
        L_0x00ad:
            java.lang.Integer r5 = com.taobao.weex.utils.WXUtils.getInteger(r6, r3)
            if (r5 == 0) goto L_0x00ba
            int r5 = r5.intValue()
            r4.setInterval(r5)
        L_0x00ba:
            return r2
        L_0x00bb:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x00c4
            r4.setShowIndicators(r5)
        L_0x00c4:
            return r2
        L_0x00c5:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x00ce
            r4.setAutoPlay(r5)
        L_0x00ce:
            return r2
        L_0x00cf:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x00d8
            r4.setValue(r5)
        L_0x00d8:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXSlider.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "value")
    @Deprecated
    public void setValue(String str) {
        if (str != null && getHostView() != null) {
            try {
                setIndex(Integer.parseInt(str));
            } catch (NumberFormatException e) {
                WXLogUtils.e("", (Throwable) e);
            }
        }
    }

    @WXComponentProp(name = "autoPlay")
    public void setAutoPlay(String str) {
        if (TextUtils.isEmpty(str) || str.equals("false")) {
            this.mViewPager.stopAutoScroll();
            return;
        }
        this.mViewPager.stopAutoScroll();
        this.mViewPager.startAutoScroll();
    }

    @WXComponentProp(name = "showIndicators")
    public void setShowIndicators(String str) {
        if (TextUtils.isEmpty(str) || str.equals("false")) {
            this.mShowIndicators = false;
        } else {
            this.mShowIndicators = true;
        }
        if (this.mIndicator != null) {
            this.mIndicator.setShowIndicators(this.mShowIndicators);
        }
    }

    @WXComponentProp(name = "interval")
    public void setInterval(int i) {
        if (this.mViewPager != null && i > 0) {
            this.mViewPager.setIntervalTime((long) i);
        }
    }

    @WXComponentProp(name = "index")
    public void setIndex(int i) {
        if (this.mViewPager != null && this.mAdapter != null) {
            if (i >= this.mAdapter.getRealCount() || i < 0) {
                this.initIndex = i;
                return;
            }
            int realIndex = getRealIndex(i);
            this.mViewPager.setCurrentItem(realIndex);
            if (this.mIndicator != null && this.mIndicator.getHostView() != null && ((WXCircleIndicator) this.mIndicator.getHostView()).getRealCurrentItem() != realIndex) {
                WXLogUtils.d("setIndex >>>> correction indicator to " + realIndex);
                ((WXCircleIndicator) this.mIndicator.getHostView()).setRealCurrentItem(realIndex);
                ((WXCircleIndicator) this.mIndicator.getHostView()).invalidate();
                if (this.mPageChangeListener != null && this.mAdapter != null) {
                    this.mPageChangeListener.onPageSelected(this.mAdapter.getFirst() + realIndex);
                }
            }
        }
    }

    @WXComponentProp(name = "scrollable")
    public void setScrollable(boolean z) {
        if (this.mViewPager != null && this.mAdapter != null) {
            this.mViewPager.setScrollable(z);
        }
    }

    @WXComponentProp(name = "offsetXAccuracy")
    public void setOffsetXAccuracy(float f) {
        this.offsetXAccuracy = f;
    }

    protected class SliderPageChangeListener implements ViewPager.OnPageChangeListener {
        private int lastPos = -1;

        public void onPageScrolled(int i, float f, int i2) {
        }

        protected SliderPageChangeListener() {
        }

        public void onPageSelected(int i) {
            if (WXSlider.this.mAdapter.getRealPosition(i) != this.lastPos) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("onPageSelected >>>>" + WXSlider.this.mAdapter.getRealPosition(i) + " lastPos: " + this.lastPos);
                }
                if (WXSlider.this.mAdapter != null && WXSlider.this.mAdapter.getRealCount() != 0) {
                    int realPosition = WXSlider.this.mAdapter.getRealPosition(i);
                    if (WXSlider.this.mChildren != null && realPosition < WXSlider.this.mChildren.size() && WXSlider.this.getEvents().size() != 0) {
                        WXEvent events = WXSlider.this.getEvents();
                        String ref = WXSlider.this.getRef();
                        if (events.contains(Constants.Event.CHANGE) && WXViewUtils.onScreenArea(WXSlider.this.getHostView())) {
                            WXSlider.this.params.put("index", Integer.valueOf(realPosition));
                            HashMap hashMap = new HashMap();
                            HashMap hashMap2 = new HashMap();
                            hashMap2.put("index", Integer.valueOf(realPosition));
                            hashMap.put(TemplateDom.KEY_ATTRS, hashMap2);
                            WXSDKManager.getInstance().fireEvent(WXSlider.this.getInstanceId(), ref, Constants.Event.CHANGE, WXSlider.this.params, hashMap);
                        }
                        WXSlider.this.mViewPager.requestLayout();
                        ((FrameLayout) WXSlider.this.getHostView()).invalidate();
                        this.lastPos = WXSlider.this.mAdapter.getRealPosition(i);
                    }
                }
            }
        }

        public void onPageScrollStateChanged(int i) {
            FrameLayout frameLayout = (FrameLayout) WXSlider.this.getHostView();
            if (frameLayout != null) {
                frameLayout.invalidate();
            }
        }
    }

    protected static class SliderOnScrollListener implements ViewPager.OnPageChangeListener {
        private float lastPositionOffset = 99.0f;
        private int selectedPosition;
        private WXSlider target;

        public SliderOnScrollListener(WXSlider wXSlider) {
            this.target = wXSlider;
            this.selectedPosition = wXSlider.mViewPager.superGetCurrentItem();
        }

        public void onPageScrolled(int i, float f, int i2) {
            if (this.lastPositionOffset == 99.0f) {
                this.lastPositionOffset = f;
            } else if (Math.abs(f - this.lastPositionOffset) >= this.target.offsetXAccuracy) {
                if (i == this.selectedPosition) {
                    HashMap hashMap = new HashMap(1);
                    hashMap.put("offsetXRatio", Float.valueOf(-f));
                    this.target.fireEvent("scroll", hashMap);
                } else if (i < this.selectedPosition) {
                    HashMap hashMap2 = new HashMap(1);
                    hashMap2.put("offsetXRatio", Float.valueOf(1.0f - f));
                    this.target.fireEvent("scroll", hashMap2);
                }
                this.lastPositionOffset = f;
            }
        }

        public void onPageSelected(int i) {
            this.selectedPosition = i;
        }

        public void onPageScrollStateChanged(int i) {
            switch (i) {
                case 0:
                    this.lastPositionOffset = 99.0f;
                    this.target.fireEvent(Constants.Event.SCROLL_END);
                    return;
                case 1:
                    this.target.fireEvent(Constants.Event.SCROLL_START);
                    return;
                default:
                    return;
            }
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void hackTwoItemsInfiniteScroll() {
        if (this.mViewPager != null && this.mAdapter != null && this.isInfinite) {
            if (this.mAdapter.getRealCount() == 2) {
                final GestureDetector gestureDetector = new GestureDetector(getContext(), new FlingGestureListener(this.mViewPager));
                this.mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return gestureDetector.onTouchEvent(motionEvent);
                    }
                });
                return;
            }
            this.mViewPager.setOnTouchListener((View.OnTouchListener) null);
        }
    }

    private static class FlingGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MAX_OFF_PATH = WXViewUtils.dip2px(250.0f);
        private static final int SWIPE_MIN_DISTANCE = WXViewUtils.dip2px(50.0f);
        private static final int SWIPE_THRESHOLD_VELOCITY = WXViewUtils.dip2px(200.0f);
        private WeakReference<WXCircleViewPager> pagerRef;

        FlingGestureListener(WXCircleViewPager wXCircleViewPager) {
            this.pagerRef = new WeakReference<>(wXCircleViewPager);
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            WXCircleViewPager wXCircleViewPager = (WXCircleViewPager) this.pagerRef.get();
            if (wXCircleViewPager == null) {
                return false;
            }
            try {
                if (Math.abs(motionEvent.getY() - motionEvent2.getY()) > ((float) SWIPE_MAX_OFF_PATH)) {
                    return false;
                }
                if (motionEvent.getX() - motionEvent2.getX() <= ((float) SWIPE_MIN_DISTANCE) || Math.abs(f) <= ((float) SWIPE_THRESHOLD_VELOCITY) || wXCircleViewPager.superGetCurrentItem() != 1) {
                    if (motionEvent2.getX() - motionEvent.getX() > ((float) SWIPE_MIN_DISTANCE) && Math.abs(f) > ((float) SWIPE_THRESHOLD_VELOCITY) && wXCircleViewPager.superGetCurrentItem() == 0) {
                        wXCircleViewPager.setCurrentItem(1, false);
                        return true;
                    }
                    return false;
                }
                wXCircleViewPager.setCurrentItem(0, false);
                return true;
            } catch (Exception unused) {
            }
        }
    }
}
