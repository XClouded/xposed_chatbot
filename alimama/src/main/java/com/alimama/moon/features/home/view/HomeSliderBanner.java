package com.alimama.moon.features.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.viewpager.widget.ViewPager;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.UnionAutoPlayer;
import com.alimama.unionwl.utils.LocalDisplay;
import in.srain.cube.views.banner.BannerAdapter;
import in.srain.cube.views.banner.PagerIndicator;

public class HomeSliderBanner extends RelativeLayout {
    public static boolean isOperateHomeSliderBanner = false;
    /* access modifiers changed from: private */
    public UnionAutoPlayer mAutoPlayer;
    /* access modifiers changed from: private */
    public BannerAdapter mBannerAdapter;
    private UnionAutoPlayer.Playable mGalleryPlayable;
    protected int mIdForIndicator;
    protected int mIdForViewPager;
    /* access modifiers changed from: private */
    public ViewPager.OnPageChangeListener mOnPageChangeListener;
    /* access modifiers changed from: private */
    public PagerIndicator mPagerIndicator;
    protected int mTimeInterval;
    /* access modifiers changed from: private */
    public ViewPager mViewPager;
    private View.OnTouchListener mViewPagerOnTouchListener;

    public HomeSliderBanner(Context context) {
        this(context, (AttributeSet) null);
    }

    public HomeSliderBanner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTimeInterval = 2000;
        this.mGalleryPlayable = new UnionAutoPlayer.Playable() {
            public void playTo(int i) {
                HomeSliderBanner.this.mViewPager.setCurrentItem(i, true);
            }

            public void playNext() {
                HomeSliderBanner.this.mViewPager.setCurrentItem(HomeSliderBanner.this.mViewPager.getCurrentItem() + 1, true);
            }

            public void playPrevious() {
                HomeSliderBanner.this.mViewPager.setCurrentItem(HomeSliderBanner.this.mViewPager.getCurrentItem() - 1, true);
            }

            public int getTotal() {
                return HomeSliderBanner.this.mBannerAdapter.getCount();
            }

            public int getCurrent() {
                return HomeSliderBanner.this.mViewPager.getCurrentItem();
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SliderBanner, 0, 0);
        if (obtainStyledAttributes != null) {
            if (obtainStyledAttributes.hasValue(1)) {
                this.mIdForViewPager = obtainStyledAttributes.getResourceId(1, 0);
            }
            if (obtainStyledAttributes.hasValue(0)) {
                this.mIdForIndicator = obtainStyledAttributes.getResourceId(0, 0);
            }
            this.mTimeInterval = obtainStyledAttributes.getInt(2, this.mTimeInterval);
            obtainStyledAttributes.recycle();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                isOperateHomeSliderBanner = true;
                if (this.mAutoPlayer != null) {
                    this.mAutoPlayer.pause();
                    break;
                }
                break;
            case 1:
                break;
            case 2:
                isOperateHomeSliderBanner = true;
                break;
            case 3:
                isOperateHomeSliderBanner = false;
                break;
            default:
                isOperateHomeSliderBanner = false;
                break;
        }
        if (this.mAutoPlayer != null) {
            this.mAutoPlayer.resume();
        }
        isOperateHomeSliderBanner = false;
        if (this.mViewPagerOnTouchListener != null) {
            this.mViewPagerOnTouchListener.onTouch(this, motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void setViewPagerOnTouchListener(View.OnTouchListener onTouchListener) {
        this.mViewPagerOnTouchListener = onTouchListener;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mViewPager = (ViewPager) findViewById(this.mIdForViewPager);
        ViewGroup.LayoutParams layoutParams = this.mViewPager.getLayoutParams();
        double dp2px = (double) (LocalDisplay.SCREEN_WIDTH_PIXELS - LocalDisplay.dp2px(24.0f));
        Double.isNaN(dp2px);
        int i = (int) ((dp2px * 220.0d) / 702.0d);
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-1, i);
        } else {
            layoutParams.height = i;
        }
        this.mViewPager.setLayoutParams(layoutParams);
        this.mPagerIndicator = (PagerIndicator) findViewById(this.mIdForIndicator);
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int i, float f, int i2) {
                if (HomeSliderBanner.this.mOnPageChangeListener != null) {
                    HomeSliderBanner.this.mOnPageChangeListener.onPageScrolled(i, f, i2);
                }
            }

            public void onPageSelected(int i) {
                if (HomeSliderBanner.this.mPagerIndicator != null) {
                    HomeSliderBanner.this.mPagerIndicator.setSelected(HomeSliderBanner.this.mBannerAdapter.getPositionForIndicator(i));
                }
                HomeSliderBanner.this.mAutoPlayer.skipNext();
                if (HomeSliderBanner.this.mOnPageChangeListener != null) {
                    HomeSliderBanner.this.mOnPageChangeListener.onPageSelected(i);
                }
            }

            public void onPageScrollStateChanged(int i) {
                if (HomeSliderBanner.this.mOnPageChangeListener != null) {
                    HomeSliderBanner.this.mOnPageChangeListener.onPageScrollStateChanged(i);
                }
            }
        });
        this.mAutoPlayer = new UnionAutoPlayer(this.mGalleryPlayable).setPlayRecycleMode(UnionAutoPlayer.PlayRecycleMode.repeat_from_start);
        this.mAutoPlayer.setTimeInterval(this.mTimeInterval);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mBannerAdapter != null && this.mBannerAdapter.getCount() > 0) {
            beginPlay();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPlay();
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i != 0) {
            stopPlay();
        } else if (this.mBannerAdapter != null && this.mBannerAdapter.getCount() > 0) {
            beginPlay();
        }
    }

    public void setTimeInterval(int i) {
        this.mAutoPlayer.setTimeInterval(i);
    }

    public void setAdapter(BannerAdapter bannerAdapter) {
        this.mBannerAdapter = bannerAdapter;
        this.mViewPager.setAdapter(bannerAdapter);
    }

    public void beginPlay() {
        this.mViewPager.setCurrentItem(0);
        this.mAutoPlayer.play();
    }

    public void stopPlay() {
        this.mAutoPlayer.stop();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setDotNum(int i) {
        if (this.mPagerIndicator != null) {
            this.mPagerIndicator.setNum(i);
        }
    }

    public ViewPager getViewPager() {
        return this.mViewPager;
    }
}
