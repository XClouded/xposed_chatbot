package com.taobao.uikit.feature.features;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.taobao.uikit.feature.callback.TouchEventCallback;

public class BinaryPageFeature extends AbsFeature<ListView> implements AbsListView.OnScrollListener, TouchEventCallback {
    public static final String ACTION_PERCENT = "Home.PagerDivider.ACTION_PERCENT";
    public static final String EXTRA_HALF = "EXTRA_HALF";
    public static final String EXTRA_PERCENT = "EXTRA_PERCENT";
    public static final String EXTRA_TOUCH = "EXTRA_TOUCH";
    private int mAnimationDuration = 600;
    private int mFirstPageBottomOffset;
    private OnPageChangedListener mOnPageChangedListener;
    /* access modifiers changed from: private */
    public int mPagePosition;
    /* access modifiers changed from: private */
    public PageState mPageState = PageState.Wait;
    private int mScrollState;
    private TouchOffset mTouchOffset;
    private Page mTouchPage = Page.NONE;

    public interface OnPageChangedListener {
        void onPageSelected(BinaryPageFeature binaryPageFeature, Page page);
    }

    private void sendLocalBroadcastManager(float f, float f2, boolean z) {
    }

    public void afterDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void afterOnTouchEvent(MotionEvent motionEvent) {
    }

    public void beforeDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        this.mTouchOffset = new TouchOffset();
    }

    public void setHost(ListView listView) {
        super.setHost(listView);
        init();
    }

    private void init() {
        ((ListView) this.mHost).setOnScrollListener(this);
    }

    public void setPagePosition(int i) {
        this.mPagePosition = i;
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        this.mScrollState = i;
        computeScroll();
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        computeScroll();
    }

    private boolean onTouch(View view, MotionEvent motionEvent) {
        Page page;
        if (view == this.mHost && (motionEvent.getAction() == 0 || motionEvent.getAction() == 2)) {
            this.mPageState = PageState.Wait;
            int lastVisiblePosition = ((ListView) this.mHost).getLastVisiblePosition();
            int firstVisiblePosition = ((ListView) this.mHost).getFirstVisiblePosition();
            if (this.mPagePosition < firstVisiblePosition || this.mPagePosition >= lastVisiblePosition) {
                this.mTouchOffset.reset();
                return false;
            }
            View childAt = ((ListView) this.mHost).getChildAt(this.mPagePosition - firstVisiblePosition);
            if (!this.mTouchOffset.isRecord()) {
                Page page2 = Page.NONE;
                if (((float) childAt.getTop()) / ((float) ((ListView) this.mHost).getHeight()) > (1.0f - ((((float) childAt.getHeight()) + ((float) this.mFirstPageBottomOffset)) / ((float) ((ListView) this.mHost).getHeight()))) / 2.0f) {
                    page = Page.FIRST;
                } else {
                    page = Page.LAST;
                }
                this.mTouchOffset.startRecord(motionEvent.getRawY(), page);
                return false;
            }
            this.mTouchOffset.setEndY(motionEvent.getRawY());
            scrollLikePullToFresh(childAt, true);
        }
        return false;
    }

    private void computeScroll() {
        if (this.mPageState != PageState.Asjusting) {
            int lastVisiblePosition = ((ListView) this.mHost).getLastVisiblePosition();
            int firstVisiblePosition = ((ListView) this.mHost).getFirstVisiblePosition();
            if (this.mScrollState == 1) {
                if (this.mPagePosition > lastVisiblePosition) {
                    this.mTouchPage = Page.FIRST;
                } else if (this.mPagePosition < firstVisiblePosition) {
                    this.mTouchPage = Page.LAST;
                } else {
                    this.mTouchPage = Page.NONE;
                }
                this.mPageState = PageState.Wait;
            } else if (this.mPagePosition >= firstVisiblePosition && this.mPagePosition < lastVisiblePosition && this.mPageState == PageState.Wait) {
                this.mPageState = PageState.Asjusting;
                MotionEvent obtain = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 4, 0.0f, 0.0f, 0);
                ((ListView) this.mHost).dispatchTouchEvent(obtain);
                ((ListView) this.mHost).onTouchEvent(obtain);
                View childAt = ((ListView) this.mHost).getChildAt(this.mPagePosition - firstVisiblePosition);
                if (this.mTouchPage == Page.NONE) {
                    if (!(this.mTouchOffset.getCurrentPage() == this.mTouchOffset.getStartPage() || this.mOnPageChangedListener == null)) {
                        this.mOnPageChangedListener.onPageSelected(this, this.mTouchOffset.getCurrentPage());
                    }
                    if (this.mTouchOffset.getCurrentPage() != Page.FIRST || this.mTouchOffset.getStartPage() != Page.FIRST || this.mTouchOffset.getOffset() <= 0.0f) {
                        scrollLikePullToFresh(childAt, false);
                    } else {
                        return;
                    }
                } else if (this.mTouchPage == Page.FIRST) {
                    scrollToFirstPageBottom(childAt);
                    this.mTouchPage = Page.NONE;
                } else if (this.mTouchPage == Page.LAST) {
                    scrollToLastPageTop(childAt);
                    this.mTouchPage = Page.NONE;
                }
                this.mTouchOffset.reset();
                obtain.recycle();
            }
        }
    }

    private void scrollLikePullToFresh(View view, boolean z) {
        if (this.mTouchOffset.getOffset() != 0.0f) {
            if (this.mTouchOffset.getCurrentPage() == Page.FIRST) {
                if (z) {
                    sendLocalBroadcastManager(1.0f, 0.5f, true);
                    return;
                }
                sendLocalBroadcastManager(1.0f, 0.5f, false);
                scrollToFirstPageBottom(view);
            } else if (this.mTouchOffset.getCurrentPage() != Page.LAST) {
            } else {
                if (z) {
                    sendLocalBroadcastManager(0.1f, 0.5f, true);
                    return;
                }
                scrollToLastPageTop(view);
                sendLocalBroadcastManager(0.1f, 0.5f, false);
            }
        }
    }

    private void scrollToLastPageTop(View view) {
        final int bottom = (int) ((((float) view.getBottom()) * ((float) this.mAnimationDuration)) / ((float) ((ListView) this.mHost).getHeight()));
        ((ListView) this.mHost).post(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= 11) {
                    ((ListView) BinaryPageFeature.this.mHost).smoothScrollToPositionFromTop(BinaryPageFeature.this.mPagePosition + 1, 0, bottom);
                    ((ListView) BinaryPageFeature.this.mHost).postDelayed(new Runnable() {
                        public void run() {
                            if (BinaryPageFeature.this.mPageState == PageState.Asjusting) {
                                PageState unused = BinaryPageFeature.this.mPageState = PageState.Complete;
                            }
                        }
                    }, (long) bottom);
                    return;
                }
                ((ListView) BinaryPageFeature.this.mHost).setSelectionFromTop(BinaryPageFeature.this.mPagePosition + 1, 0);
                if (BinaryPageFeature.this.mPageState == PageState.Asjusting) {
                    PageState unused = BinaryPageFeature.this.mPageState = PageState.Complete;
                }
            }
        });
    }

    private void scrollToFirstPageBottom(View view) {
        float bottom = (float) (view.getBottom() + this.mFirstPageBottomOffset);
        if (bottom != ((float) view.getHeight())) {
            final int abs = (int) ((Math.abs(bottom - ((float) ((ListView) this.mHost).getHeight())) * ((float) this.mAnimationDuration)) / ((float) ((ListView) this.mHost).getHeight()));
            final int height = (((ListView) this.mHost).getHeight() - view.getHeight()) - this.mFirstPageBottomOffset;
            ((ListView) this.mHost).post(new Runnable() {
                public void run() {
                    if (Build.VERSION.SDK_INT >= 11) {
                        ((ListView) BinaryPageFeature.this.mHost).smoothScrollToPositionFromTop(BinaryPageFeature.this.mPagePosition, height, abs);
                        ((ListView) BinaryPageFeature.this.mHost).postDelayed(new Runnable() {
                            public void run() {
                                if (BinaryPageFeature.this.mPageState == PageState.Asjusting) {
                                    PageState unused = BinaryPageFeature.this.mPageState = PageState.Complete;
                                }
                            }
                        }, (long) abs);
                        return;
                    }
                    ((ListView) BinaryPageFeature.this.mHost).setSelectionFromTop(BinaryPageFeature.this.mPagePosition, height);
                    if (BinaryPageFeature.this.mPageState == PageState.Asjusting) {
                        PageState unused = BinaryPageFeature.this.mPageState = PageState.Complete;
                    }
                }
            });
        }
    }

    public void setFirstPageBottomOffset(int i) {
        this.mFirstPageBottomOffset = i;
    }

    public void setAnimationDuration(int i) {
        this.mAnimationDuration = i;
    }

    public void setTouchOffsetLimit(float f) {
        this.mTouchOffset.setOffsetLimit(f);
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.mOnPageChangedListener = onPageChangedListener;
    }

    public enum PageState {
        Wait(0, "待调整"),
        Asjusting(1, "调整中"),
        Complete(2, "系统完成");
        
        private final String desc;
        private final int value;

        private PageState(int i, String str) {
            this.value = i;
            this.desc = str;
        }

        public int getValue() {
            return this.value;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    enum Page {
        NONE(-1, "分界处"),
        FIRST(0, "第一页"),
        LAST(1, "第二页");
        
        private final String desc;
        private final int value;

        private Page(int i, String str) {
            this.value = i;
            this.desc = str;
        }

        public int getValue() {
            return this.value;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    class TouchOffset {
        private float endY;
        private float offsetLimit = 160.0f;
        private boolean record = false;
        private Page startPage = Page.NONE;
        private float startY;

        TouchOffset() {
        }

        public void startRecord(float f, Page page) {
            this.record = true;
            this.startY = f;
            this.startPage = page;
        }

        public void setEndY(float f) {
            this.endY = f;
        }

        public Page getStartPage() {
            return this.startPage;
        }

        public Page getCurrentPage() {
            float offset = getOffset();
            if (Math.abs(offset) <= this.offsetLimit) {
                return this.startPage;
            }
            if (offset > this.offsetLimit) {
                return Page.FIRST;
            }
            return Page.LAST;
        }

        public boolean isUpDown() {
            return getOffset() < 0.0f;
        }

        public boolean isUp() {
            return getOffset() < 0.0f;
        }

        public boolean isDown() {
            return getOffset() >= 0.0f;
        }

        public float getOffset() {
            if (this.record) {
                return this.endY - this.startY;
            }
            return 0.0f;
        }

        public float getOffsetLimit() {
            return this.offsetLimit;
        }

        public void setOffsetLimit(float f) {
            this.offsetLimit = f;
        }

        public boolean isRecord() {
            return this.record;
        }

        public void setRecord(boolean z) {
            this.record = z;
        }

        public void reset() {
            this.record = false;
            this.startPage = Page.NONE;
        }
    }

    public void beforeOnTouchEvent(MotionEvent motionEvent) {
        onTouch(this.mHost, motionEvent);
    }
}
