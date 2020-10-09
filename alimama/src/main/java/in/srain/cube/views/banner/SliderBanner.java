package in.srain.cube.views.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.viewpager.widget.ViewPager;
import com.alimama.unionwl.uiframe.R;
import in.srain.cube.views.DotView;
import in.srain.cube.views.banner.AutoPlayer;

public class SliderBanner extends RelativeLayout {
    /* access modifiers changed from: private */
    public AutoPlayer mAutoPlayer;
    /* access modifiers changed from: private */
    public BannerAdapter mBannerAdapter;
    private AutoPlayer.Playable mGalleryPlayable;
    protected int mIdForBackground1;
    protected int mIdForBackground2;
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

    public SliderBanner(Context context) {
        this(context, (AttributeSet) null);
    }

    public SliderBanner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTimeInterval = 2000;
        this.mGalleryPlayable = new AutoPlayer.Playable() {
            public void playTo(int i) {
                SliderBanner.this.mViewPager.setCurrentItem(i, true);
            }

            public void playNext() {
                SliderBanner.this.mViewPager.setCurrentItem(SliderBanner.this.mViewPager.getCurrentItem() + 1, true);
            }

            public void playPrevious() {
                SliderBanner.this.mViewPager.setCurrentItem(SliderBanner.this.mViewPager.getCurrentItem() - 1, true);
            }

            public int getTotal() {
                return SliderBanner.this.mBannerAdapter.getCount();
            }

            public int getCurrent() {
                return SliderBanner.this.mViewPager.getCurrentItem();
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SliderBanner, 0, 0);
        if (obtainStyledAttributes != null) {
            if (obtainStyledAttributes.hasValue(R.styleable.SliderBanner_slider_banner_pager)) {
                this.mIdForViewPager = obtainStyledAttributes.getResourceId(R.styleable.SliderBanner_slider_banner_pager, 0);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.SliderBanner_slider_banner_indicator)) {
                this.mIdForIndicator = obtainStyledAttributes.getResourceId(R.styleable.SliderBanner_slider_banner_indicator, 0);
            }
            this.mTimeInterval = obtainStyledAttributes.getInt(R.styleable.SliderBanner_slider_banner_time_interval, this.mTimeInterval);
            obtainStyledAttributes.recycle();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 3) {
            switch (action) {
                case 0:
                    if (this.mAutoPlayer != null) {
                        this.mAutoPlayer.pause();
                        break;
                    }
                    break;
                case 1:
                    break;
            }
        }
        if (this.mAutoPlayer != null) {
            this.mAutoPlayer.resume();
        }
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
        this.mPagerIndicator = (DotView) findViewById(this.mIdForIndicator);
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int i, float f, int i2) {
                if (SliderBanner.this.mOnPageChangeListener != null) {
                    SliderBanner.this.mOnPageChangeListener.onPageScrolled(i, f, i2);
                }
            }

            public void onPageSelected(int i) {
                if (SliderBanner.this.mPagerIndicator != null) {
                    SliderBanner.this.mPagerIndicator.setSelected(SliderBanner.this.mBannerAdapter.getPositionForIndicator(i));
                }
                SliderBanner.this.mAutoPlayer.skipNext();
                if (SliderBanner.this.mOnPageChangeListener != null) {
                    SliderBanner.this.mOnPageChangeListener.onPageSelected(i);
                }
            }

            public void onPageScrollStateChanged(int i) {
                if (SliderBanner.this.mOnPageChangeListener != null) {
                    SliderBanner.this.mOnPageChangeListener.onPageScrollStateChanged(i);
                }
            }
        });
        this.mAutoPlayer = new AutoPlayer(this.mGalleryPlayable).setPlayRecycleMode(AutoPlayer.PlayRecycleMode.play_back);
        this.mAutoPlayer.setTimeInterval(this.mTimeInterval);
    }

    public void setTimeInterval(int i) {
        this.mAutoPlayer.setTimeInterval(i);
    }

    public void setAdapter(BannerAdapter bannerAdapter) {
        this.mBannerAdapter = bannerAdapter;
        this.mViewPager.setAdapter(bannerAdapter);
    }

    public void beginPlay() {
        this.mAutoPlayer.play();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setDotNum(int i) {
        if (this.mPagerIndicator != null) {
            this.mPagerIndicator.setNum(i);
        }
    }
}
