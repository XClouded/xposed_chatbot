package com.taobao.uikit.feature.features;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.view.TListView;

public class PageNumberFeature extends AbsFeature<ListView> implements AbsListView.OnScrollListener, MeasureCallback, LayoutCallback, CanvasCallback {
    private static final int PAGETIP_MARGIN_BOTTOM = 20;
    private static final int PAGETIP_MARGIN_RIGHT = 25;
    public static final int SHOW_ALWAYS = 100;
    public static final int SHOW_NONE = 102;
    public static final int SHOW_SCROLLING = 101;
    /* access modifiers changed from: private */
    public int mCurrentPage;
    private int mLastVisibleItemIndex;
    private int mPageSize;
    private PageTip mPageTip;
    private int mScrollState = 0;
    private int mShowType = 101;
    private int mTotalCount;

    public void afterDraw(Canvas canvas) {
    }

    public void afterOnDraw(Canvas canvas) {
    }

    public void beforeDispatchDraw(Canvas canvas) {
    }

    public void beforeDraw(Canvas canvas) {
    }

    public void beforeOnDraw(Canvas canvas) {
    }

    public void beforeOnLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void beforeOnMeasure(int i, int i2) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
    }

    private void init() {
        this.mPageTip = new PageTip(((ListView) getHost()).getContext());
        ((ListView) getHost()).setOnScrollListener(this);
    }

    public void setHost(ListView listView) {
        super.setHost(listView);
        init();
    }

    public int getPageSize() {
        return this.mPageSize;
    }

    public void setShowType(int i) {
        this.mShowType = i;
    }

    public void setPageSize(int i) {
        this.mPageSize = i;
    }

    public int getCurrentPage() {
        return this.mCurrentPage;
    }

    public void setTotalCount(int i) {
        this.mTotalCount = i;
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        this.mScrollState = i;
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (this.mLastVisibleItemIndex != ((ListView) getHost()).getLastVisiblePosition()) {
            this.mLastVisibleItemIndex = ((ListView) getHost()).getLastVisiblePosition();
            this.mPageTip.updatePageIndex(this.mLastVisibleItemIndex - ((ListView) getHost()).getHeaderViewsCount(), this.mPageSize, this.mTotalCount, i);
        }
    }

    public void afterOnMeasure(int i, int i2) {
        ((TListView) getHost()).measureChild(this.mPageTip, i, i2, 0);
    }

    public void afterOnLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mPageTip.layout((((ListView) getHost()).getWidth() - this.mPageTip.getMeasuredWidth()) - this.mPageTip.mMarginRight, (((ListView) getHost()).getHeight() - this.mPageTip.getMeasuredHeight()) - this.mPageTip.mMarginBottom, ((ListView) getHost()).getWidth() - this.mPageTip.mMarginRight, ((ListView) getHost()).getHeight() - this.mPageTip.mMarginBottom);
    }

    private boolean showPageTip() {
        if (this.mShowType != 101) {
            return this.mShowType == 100;
        }
        if (this.mScrollState != 0) {
            return true;
        }
        return false;
    }

    public void afterDispatchDraw(Canvas canvas) {
        if (showPageTip()) {
            ((TListView) getHost()).drawChild(canvas, this.mPageTip, ((ListView) getHost()).getDrawingTime(), 0);
        }
    }

    public class PageTip extends TextView {
        int mMarginBottom;
        int mMarginRight;

        public PageTip(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            init();
        }

        public PageTip(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            init();
        }

        public PageTip(Context context) {
            super(context);
            init();
        }

        private void init() {
            int i = (int) (getContext().getResources().getDisplayMetrics().density * 5.0f);
            setPadding(i, 0, i, 0);
            setTextSize(17.0f);
            setBackgroundColor(-7829368);
            setGravity(17);
            setText("0/0");
            setTextColor(-1);
            setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            new DisplayMetrics();
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            this.mMarginRight = (int) (displayMetrics.density * 25.0f);
            this.mMarginBottom = (int) (displayMetrics.density * 20.0f);
        }

        /* access modifiers changed from: package-private */
        public void updatePageIndex(int i, int i2, int i3, int i4) {
            String str;
            if (i2 != 0) {
                int i5 = i4 == 0 ? 1 : (i / i2) + 1;
                int i6 = ((i3 + i2) - 1) / i2;
                if (i5 > i6) {
                    i5 = i6;
                }
                if (i3 > 0) {
                    str = String.format("%s/%s", new Object[]{Integer.valueOf(i5), Integer.valueOf(i6)});
                } else {
                    str = String.format("%s", new Object[]{Integer.valueOf(i5)});
                }
                int unused = PageNumberFeature.this.mCurrentPage = i5;
                if (!TextUtils.equals(getText(), str)) {
                    setText(str);
                    measure(0, 0);
                    layout(getRight() - getMeasuredWidth(), getBottom() - getMeasuredHeight(), getRight(), getBottom());
                }
            }
        }
    }
}
