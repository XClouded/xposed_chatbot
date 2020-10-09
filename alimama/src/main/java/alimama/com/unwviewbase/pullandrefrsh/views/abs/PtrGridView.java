package alimama.com.unwviewbase.pullandrefrsh.views.abs;

import alimama.com.unwviewbase.pullandrefrsh.OnLastItemVisibleListener;
import alimama.com.unwviewbase.pullandrefrsh.PullAdapter;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import alimama.com.unwviewbase.pullandrefrsh.views.abs.accessories.AbsListViewDetector;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.GridView;

public class PtrGridView extends GridView implements PullAdapter, AbsListView.OnScrollListener {
    private boolean mLastItemVisible;
    private OnLastItemVisibleListener mOnLastItemVisibleListener;
    private AbsListView.OnScrollListener mOnScrollListener;

    public int getPullDirection() {
        return 0;
    }

    public void onPullAdapterAdded(PullBase pullBase) {
    }

    public void onPullAdapterRemoved(PullBase pullBase) {
    }

    public PtrGridView(Context context) {
        super(context);
        super.setOnScrollListener(this);
    }

    public PtrGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setOnScrollListener(this);
    }

    public PtrGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        super.setOnScrollListener(this);
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 0 && this.mOnLastItemVisibleListener != null && this.mLastItemVisible) {
            this.mOnLastItemVisibleListener.onLastItemVisible();
        }
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScrollStateChanged(absListView, i);
        }
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (this.mOnLastItemVisibleListener != null) {
            this.mLastItemVisible = i3 > 0 && i + i2 >= i3 + -1;
        }
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(absListView, i, i2, i3);
        }
    }

    public final void setOnLastItemVisibleListener(OnLastItemVisibleListener onLastItemVisibleListener) {
        this.mOnLastItemVisibleListener = onLastItemVisibleListener;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public boolean isReadyForPullStart() {
        return AbsListViewDetector.isFirstItemTotallyVisible(this);
    }

    public boolean isReadyForPullEnd() {
        return AbsListViewDetector.isLastItemTotallyVisible(this);
    }
}
