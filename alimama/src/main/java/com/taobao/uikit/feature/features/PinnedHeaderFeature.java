package com.taobao.uikit.feature.features;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.FocusCallback;
import com.taobao.uikit.feature.callback.InterceptTouchEventCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.view.ViewGroupHelper;

public class PinnedHeaderFeature extends AbsFeature<ListView> implements MeasureCallback, InterceptTouchEventCallback, LayoutCallback, CanvasCallback, FocusCallback, AbsListView.OnScrollListener {
    private static final int MAX_ALPHA = 255;
    private static final String TAG = "PinnedHeaderFeature";
    /* access modifiers changed from: private */
    public SectionAdapter mAdapter;
    private View mHeaderView;
    private int mHeaderViewHeight;
    private boolean mHeaderViewVisible = false;
    private int mHeaderViewWidth;
    private int mHeightMeasureSpec;
    private PinnedHeaderAdapterInternal mInternalAdapter;
    private boolean mLastHeaderDown = false;
    private int mWidthMeasureSpec;

    interface PinnedHeaderAdapterInternal {
        public static final int PINNED_HEADER_GONE = 0;
        public static final int PINNED_HEADER_PUSHED_UP = 2;
        public static final int PINNED_HEADER_VISIBLE = 1;

        void configurePinnedHeader(View view, int i, int i2);

        int getPinnedHeaderState(int i);
    }

    public interface SectionAdapter {
        void bindSection(View view, int i);

        int firstSection();

        int getSection(int i);
    }

    public void afterDraw(Canvas canvas) {
    }

    public void afterOnDraw(Canvas canvas) {
    }

    public void afterOnFocusChanged(boolean z, int i, Rect rect) {
    }

    public void beforeDispatchDraw(Canvas canvas) {
    }

    public void beforeDraw(Canvas canvas) {
    }

    public void beforeOnDraw(Canvas canvas) {
    }

    public void beforeOnFocusChanged(boolean z, int i, Rect rect) {
    }

    public void beforeOnLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void beforeOnMeasure(int i, int i2) {
    }

    public void beforeOnWindowFocusChanged(boolean z) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    public void setHost(ListView listView) {
        super.setHost(listView);
        ((ListView) getHost()).setFadingEdgeLength(0);
        ((ListView) getHost()).setOnScrollListener(this);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (this.mHeaderViewVisible) {
            Rect rect = new Rect(0, 0, this.mHeaderViewWidth, this.mHeaderView.getBottom());
            boolean contains = rect.contains((int) motionEvent.getX(), (int) motionEvent.getY());
            if (contains) {
                z = this.mHeaderView.dispatchTouchEvent(motionEvent);
                ((ListView) getHost()).invalidate(rect);
            } else if (this.mLastHeaderDown) {
                this.mHeaderView.dispatchWindowFocusChanged(false);
                this.mHeaderView.setPressed(false);
                ((ListView) getHost()).invalidate(rect);
                this.mLastHeaderDown = false;
            }
            if (motionEvent.getAction() == 0) {
                this.mLastHeaderDown = contains;
            }
        }
        return z;
    }

    public void afterOnMeasure(int i, int i2) {
        if (this.mHeaderView != null) {
            if (getHost() instanceof ViewGroupHelper) {
                ((ViewGroupHelper) getHost()).measureChild(this.mHeaderView, i, i2, 0);
            }
            this.mWidthMeasureSpec = i;
            this.mHeightMeasureSpec = i2;
            this.mHeaderViewWidth = this.mHeaderView.getMeasuredWidth();
            this.mHeaderViewHeight = this.mHeaderView.getMeasuredHeight();
        }
    }

    public void setPinnedHeaderView(View view) {
        this.mHeaderView = view;
        if (this.mHeaderView != null && getHost() != null) {
            ((ListView) getHost()).setFadingEdgeLength(0);
        }
    }

    public void setSectionAdapter(SectionAdapter sectionAdapter) {
        this.mAdapter = sectionAdapter;
        if (this.mAdapter != null) {
            this.mInternalAdapter = new PinnedHeaderAdapterInternal() {
                public int getPinnedHeaderState(int i) {
                    if (i < 0 || i >= ((ListView) PinnedHeaderFeature.this.getHost()).getCount() || PinnedHeaderFeature.this.mAdapter.firstSection() > i) {
                        return 0;
                    }
                    int i2 = i + 1;
                    return (PinnedHeaderFeature.this.mAdapter.getSection(i2) != i2 || PinnedHeaderFeature.this.mAdapter.firstSection() == i2) ? 1 : 2;
                }

                public void configurePinnedHeader(View view, int i, int i2) {
                    PinnedHeaderFeature.this.mAdapter.bindSection(view, PinnedHeaderFeature.this.mAdapter.getSection(i));
                }
            };
        }
    }

    private void measureHeadView() {
        if (this.mHeaderView != null) {
            if (getHost() instanceof ViewGroupHelper) {
                ((ViewGroupHelper) getHost()).measureChild(this.mHeaderView, this.mWidthMeasureSpec, this.mHeightMeasureSpec, 0);
            }
            this.mHeaderViewWidth = this.mHeaderView.getMeasuredWidth();
            this.mHeaderViewHeight = this.mHeaderView.getMeasuredHeight();
        }
    }

    private void configureHeaderView(int i) {
        int i2;
        int headerViewsCount = i + -1 >= 0 ? i - ((ListView) getHost()).getHeaderViewsCount() : 0;
        if (this.mHeaderView != null) {
            int i3 = 255;
            switch (this.mInternalAdapter.getPinnedHeaderState(headerViewsCount)) {
                case 0:
                    this.mHeaderViewVisible = false;
                    return;
                case 1:
                    this.mInternalAdapter.configurePinnedHeader(this.mHeaderView, headerViewsCount, 255);
                    measureHeadView();
                    if (this.mHeaderView.getTop() != 0) {
                        this.mHeaderView.layout(0, 0, this.mHeaderViewWidth, this.mHeaderViewHeight);
                    }
                    this.mHeaderViewVisible = true;
                    return;
                case 2:
                    View childAt = ((ListView) getHost()).getChildAt(0);
                    if (childAt != null) {
                        int bottom = childAt.getBottom();
                        int height = this.mHeaderView.getHeight();
                        if (bottom >= height || height <= 0) {
                            i2 = 0;
                        } else {
                            i2 = bottom - height;
                            i3 = ((height + i2) * 255) / height;
                        }
                        this.mInternalAdapter.configurePinnedHeader(this.mHeaderView, headerViewsCount, i3);
                        measureHeadView();
                        if (this.mHeaderView.getTop() != i2) {
                            this.mHeaderView.layout(0, i2, this.mHeaderViewWidth, this.mHeaderViewHeight + i2);
                        }
                        this.mHeaderViewVisible = true;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void afterOnLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mHeaderView != null) {
            this.mHeaderView.layout(0, 0, this.mHeaderViewWidth, this.mHeaderViewHeight);
            configureHeaderView(((ListView) getHost()).getFirstVisiblePosition());
        }
    }

    public void afterDispatchDraw(Canvas canvas) {
        if (this.mHeaderViewVisible && (getHost() instanceof ViewGroupHelper)) {
            ((ViewGroupHelper) getHost()).drawChild(canvas, this.mHeaderView, ((ListView) getHost()).getDrawingTime(), 0);
        }
    }

    public void afterOnWindowFocusChanged(boolean z) {
        if (!z && this.mHeaderView != null) {
            this.mHeaderView.dispatchWindowFocusChanged(false);
        }
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (this.mHeaderView != null) {
            configureHeaderView(i);
        }
    }
}
