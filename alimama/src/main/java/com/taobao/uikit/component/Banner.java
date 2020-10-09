package com.taobao.uikit.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.taobao.uikit.R;
import com.taobao.uikit.component.LoopViewPager;
import com.taobao.uikit.utils.HandlerTimer;

public class Banner extends FrameLayout {
    private static final int DEFAULT_CYCLE_INTERVAL_MILLS = 3000;
    private boolean mAutoScroll = false;
    protected IndicatorView mIndicator;
    private float mRatio;
    private int mScrollInterval = 3000;
    private HandlerTimer mTimer;
    protected LoopViewPager mViewPager;

    public Banner(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    public Banner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public Banner(Context context) {
        super(context);
        init(context, (AttributeSet) null, 0);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        initAttr(context, attributeSet, i);
        View.inflate(context, R.layout.uik_banner, this);
        initView();
    }

    private void initAttr(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.Banner, i, 0);
        if (obtainStyledAttributes != null) {
            this.mScrollInterval = obtainStyledAttributes.getInt(R.styleable.Banner_uik_autoScrollInterval, 3000);
            this.mAutoScroll = obtainStyledAttributes.getBoolean(R.styleable.Banner_uik_autoScroll, false);
            this.mRatio = obtainStyledAttributes.getFloat(R.styleable.Banner_uik_ratio, 0.3125f);
            obtainStyledAttributes.recycle();
        }
    }

    private void initView() {
        this.mViewPager = (LoopViewPager) findViewById(R.id.viewpager);
        this.mViewPager.setRatio(this.mRatio);
        this.mIndicator = (IndicatorView) findViewById(R.id.indicator);
        this.mViewPager.setOnPageChangeListener(new LoopViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                Banner.this.mIndicator.setIndex(i);
            }
        });
    }

    private void initTimer() {
        if (this.mTimer != null) {
            this.mTimer.stop();
            this.mTimer = null;
        }
        if (this.mAutoScroll) {
            this.mTimer = new HandlerTimer((long) this.mScrollInterval, new Runnable() {
                public void run() {
                    int count;
                    if (Banner.this.mViewPager != null && Banner.this.mViewPager.getAdapter() != null && (count = Banner.this.mViewPager.getAdapter().getCount()) != 0) {
                        Banner.this.mViewPager.setCurrentItem((Banner.this.mViewPager.getCurrentItem() + 1) % count, true);
                    }
                }
            });
            this.mTimer.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initTimer();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTimer != null) {
            this.mTimer.stop();
            this.mTimer = null;
        }
    }

    public void destory() {
        if (this.mTimer != null) {
            this.mTimer.stop();
            this.mTimer = null;
        }
    }

    public void setAutoScroll(boolean z) {
        this.mAutoScroll = z;
        initTimer();
    }

    public void setScrollInterval(int i) {
        this.mScrollInterval = i;
        initTimer();
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        this.mViewPager.setAdapter(pagerAdapter);
        this.mIndicator.setTotal(pagerAdapter.getCount());
        this.mIndicator.setIndex(0);
    }

    public void setLayout(int i) {
        setLayout(LayoutInflater.from(getContext()).inflate(i, (ViewGroup) null));
    }

    public void setLayout(View view) {
        if (view.findViewById(R.id.viewpager) == null || view.findViewById(R.id.indicator) == null) {
            throw new RuntimeException("banner need two views which's are viewpager and indicator");
        }
        removeAllViews();
        addView(view);
        initView();
    }

    public void setRatio(float f) {
        this.mRatio = f;
        this.mViewPager.setRatio(this.mRatio);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        doTimerEvent(motionEvent);
        return super.dispatchTouchEvent(motionEvent);
    }

    private void doTimerEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            if (this.mTimer != null) {
                this.mTimer.stop();
            }
        } else if ((motionEvent.getAction() == 3 || motionEvent.getAction() == 4 || motionEvent.getAction() == 1) && this.mTimer != null) {
            this.mTimer.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i != 0) {
            if (this.mTimer != null) {
                this.mTimer.stop();
            }
        } else if (this.mTimer != null && isShown()) {
            this.mTimer.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (this.mTimer == null) {
            return;
        }
        if (isShown()) {
            this.mTimer.start();
        } else {
            this.mTimer.stop();
        }
    }
}
