package alimama.com.unwviewbase.pullandrefrsh.views.abs;

import alimama.com.unwviewbase.pullandrefrsh.OnLastItemVisibleListener;
import alimama.com.unwviewbase.pullandrefrsh.PtrBase;
import alimama.com.unwviewbase.pullandrefrsh.PtrInterface;
import alimama.com.unwviewbase.pullandrefrsh.PtrLayout;
import alimama.com.unwviewbase.pullandrefrsh.PtrLoadingDelegate;
import alimama.com.unwviewbase.pullandrefrsh.PtrLoadingHelper;
import alimama.com.unwviewbase.pullandrefrsh.PtrProxy;
import alimama.com.unwviewbase.pullandrefrsh.PullAdapter;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import alimama.com.unwviewbase.pullandrefrsh.views.abs.accessories.AbsListViewDetector;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class PtrListView extends ListView implements PullAdapter, PtrInterface, PtrProxy, AbsListView.OnScrollListener {
    private PtrLoadingDelegate mDelegate;
    private View mEndView;
    private boolean mIsDisable;
    private boolean mLastItemVisible;
    private Drawable mLoadingDrawable;
    private PtrLoadingHelper mLoadingHelper;
    private OnLastItemVisibleListener mOnLastItemVisibleListener;
    private AbsListView.OnScrollListener mOnScrollListener;
    private ColorStateList mTextColor;

    public int getPullDirection() {
        return 0;
    }

    public PtrListView(Context context) {
        super(context);
        super.setOnScrollListener(this);
    }

    public PtrListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setOnScrollListener(this);
    }

    public PtrListView(Context context, AttributeSet attributeSet, int i) {
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

    public final void setEndLoadingTextColor(ColorStateList colorStateList) {
        this.mTextColor = colorStateList;
        if (this.mLoadingHelper != null) {
            this.mLoadingHelper.setLoadingTextColor(colorStateList);
        }
    }

    public final void setEndLoadingDrawable(Drawable drawable) {
        this.mLoadingDrawable = drawable;
        if (this.mLoadingHelper != null) {
            this.mLoadingHelper.setLoadingDrawable(drawable);
        }
    }

    public final void setEndLoadingDelegate(PtrLoadingDelegate ptrLoadingDelegate) {
        this.mDelegate = ptrLoadingDelegate;
        if (this.mLoadingHelper != null) {
            this.mLoadingHelper.setLoadingDelegate(ptrLoadingDelegate);
            onUpdateDirection(getPullDirection());
        }
    }

    public final void disableEndPullFeature(boolean z) {
        if (z) {
            onDisable();
        } else {
            onEnable();
        }
    }

    public boolean isDisableIntrinsicPullFeature() {
        return isDisable();
    }

    public boolean isDisable() {
        return this.mIsDisable;
    }

    public final View getEndView() {
        return this.mEndView;
    }

    public boolean isReadyForPullStart() {
        return AbsListViewDetector.isFirstItemTotallyVisible(this);
    }

    public boolean isReadyForPullEnd() {
        return AbsListViewDetector.isLastItemTotallyVisible(this);
    }

    public void onPullAdapterAdded(PullBase pullBase) {
        if (pullBase instanceof PtrBase) {
            PtrBase ptrBase = (PtrBase) pullBase;
            ptrBase.addEndPtrProxy(this);
            PtrLayout endLayout = ptrBase.getEndLayout();
            if (endLayout != null) {
                endLayout.disableIntrinsicPullFeature(true);
            }
            this.mLoadingHelper = new PtrLoadingHelper(ptrBase.getEndLayout());
            setEndLoadingTextColor(this.mTextColor);
            setEndLoadingDrawable(this.mLoadingDrawable);
            setEndLoadingDelegate(this.mDelegate);
        }
    }

    public void onPullAdapterRemoved(PullBase pullBase) {
        if (pullBase instanceof PtrBase) {
            PtrBase ptrBase = (PtrBase) pullBase;
            ptrBase.removeEndPtrProxy(this);
            removeFooterView(this.mEndView);
            this.mEndView = null;
            this.mLoadingHelper = null;
            ptrBase.getEndLayout().disableIntrinsicPullFeature(false);
        }
    }

    /* renamed from: alimama.com.unwviewbase.pullandrefrsh.views.abs.PtrListView$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode = new int[PullBase.Mode.values().length];

        static {
            try {
                $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode[PullBase.Mode.PULL_FROM_END.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public int getReadyToRefreshingValue(PtrBase ptrBase, PullBase.Mode mode, int i) {
        if (AnonymousClass1.$SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode[mode.ordinal()] != 1 || i == 1 || this.mEndView == null) {
            return -1;
        }
        return this.mEndView.getHeight();
    }

    public int getReleaseTargetValue(PtrBase ptrBase, PullBase.Mode mode, int i) {
        return AnonymousClass1.$SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode[mode.ordinal()] != 1 ? -1 : 0;
    }

    public void onUpdateDirection(int i) {
        if (this.mLoadingHelper != null) {
            if (this.mLoadingHelper != null) {
                this.mLoadingHelper.onUpdateDirection(i);
            }
            if (!isDisable()) {
                removeFooterView(this.mEndView);
                this.mEndView = null;
                if (this.mLoadingHelper != null) {
                    this.mEndView = this.mLoadingHelper.getLoadingView(this);
                    if (this.mEndView != null) {
                        addFooterView(this.mEndView);
                    }
                }
            }
        }
    }

    public void onPull(float f) {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onPull(f);
        }
    }

    public void onRelease(float f) {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onRelease(f);
        }
    }

    public void onRefreshing() {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onRefreshing();
        }
    }

    public void onCompleteUpdate(CharSequence charSequence) {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onCompleteUpdate(charSequence);
        }
    }

    public void onReset() {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onReset();
        }
    }

    public void onFreeze(boolean z, CharSequence charSequence) {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onFreeze(z, charSequence);
        }
    }

    public void onDisable() {
        this.mIsDisable = true;
        removeFooterView(this.mEndView);
        this.mEndView = null;
    }

    public void onEnable() {
        this.mIsDisable = false;
        onUpdateDirection(getPullDirection());
    }
}
