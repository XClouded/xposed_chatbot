package alimama.com.unwviewbase.pullandrefrsh;

import alimama.com.unwviewbase.R;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class PtrLayout extends PullLayout implements PtrProxy {
    private PtrLoadingHelper mHelper;
    private View mInnerParent;
    private boolean mIsDisable;
    private Drawable mLoadingDrawable;
    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;
    private ColorStateList mTextColor;

    public PtrLayout(Context context, PullBase.Mode mode, int i, AttributeSet attributeSet) {
        super(context, mode, i, attributeSet);
    }

    /* access modifiers changed from: protected */
    public final void onInit(Context context, PullBase.Mode mode, int i, AttributeSet attributeSet) {
        super.onInit(context, mode, i, attributeSet);
        this.mPullLabel = "";
        this.mRefreshingLabel = context.getString(R.string.ptr_refreshing_label);
        this.mReleaseLabel = context.getString(R.string.ptr_release_label);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.Refresh);
        if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrPullLabel)) {
            this.mPullLabel = obtainStyledAttributes.getString(R.styleable.Refresh_ptrPullLabel);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrRefreshingLabel)) {
            this.mRefreshingLabel = obtainStyledAttributes.getString(R.styleable.Refresh_ptrRefreshingLabel);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrReleaseLabel)) {
            this.mReleaseLabel = obtainStyledAttributes.getString(R.styleable.Refresh_ptrReleaseLabel);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrLoadingTextColor)) {
            this.mTextColor = obtainStyledAttributes.getColorStateList(R.styleable.Refresh_ptrLoadingTextColor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrLoadingDrawable)) {
            this.mLoadingDrawable = obtainStyledAttributes.getDrawable(R.styleable.Refresh_ptrLoadingDrawable);
        }
        if (AnonymousClass1.$SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode[mode.ordinal()] == 1) {
            if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrEndPullLabel)) {
                this.mPullLabel = obtainStyledAttributes.getString(R.styleable.Refresh_ptrEndPullLabel);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrEndRefreshingLabel)) {
                this.mRefreshingLabel = obtainStyledAttributes.getString(R.styleable.Refresh_ptrEndRefreshingLabel);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrEndReleaseLabel)) {
                this.mReleaseLabel = obtainStyledAttributes.getString(R.styleable.Refresh_ptrEndReleaseLabel);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrEndLoadingTextColor)) {
                this.mTextColor = obtainStyledAttributes.getColorStateList(R.styleable.Refresh_ptrEndLoadingTextColor);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.Refresh_ptrEndLoadingDrawable)) {
                this.mLoadingDrawable = obtainStyledAttributes.getDrawable(R.styleable.Refresh_ptrEndLoadingDrawable);
            }
        }
        obtainStyledAttributes.recycle();
        this.mHelper = new PtrLoadingHelper(this);
        updateScrollDirection(mode, i);
    }

    /* renamed from: alimama.com.unwviewbase.pullandrefrsh.PtrLayout$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode = new int[PullBase.Mode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode[] r0 = alimama.com.unwviewbase.pullandrefrsh.PullBase.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode = r0
                int[] r0 = $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode     // Catch:{ NoSuchFieldError -> 0x0014 }
                alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode r1 = alimama.com.unwviewbase.pullandrefrsh.PullBase.Mode.PULL_FROM_END     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode     // Catch:{ NoSuchFieldError -> 0x001f }
                alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode r1 = alimama.com.unwviewbase.pullandrefrsh.PullBase.Mode.PULL_FROM_START     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwviewbase.pullandrefrsh.PtrLayout.AnonymousClass1.<clinit>():void");
        }
    }

    public void setmPullLabel(CharSequence charSequence) {
        this.mPullLabel = charSequence;
    }

    public void setmRefreshingLabel(CharSequence charSequence) {
        this.mRefreshingLabel = charSequence;
    }

    public void setmLoadingDrawable(Drawable drawable) {
        this.mLoadingDrawable = drawable;
    }

    public void setmReleaseLabel(CharSequence charSequence) {
        this.mReleaseLabel = charSequence;
    }

    public void setmTextColor(ColorStateList colorStateList) {
        this.mTextColor = colorStateList;
    }

    public final void disableIntrinsicPullFeature(boolean z) {
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

    /* access modifiers changed from: package-private */
    public final void updateScrollDirection(PullBase.Mode mode, int i) {
        super.updateScrollDirection(mode, i);
        onUpdateDirection(i);
    }

    public void onUpdateDirection(int i) {
        if (this.mHelper != null) {
            if (this.mHelper != null) {
                this.mHelper.onUpdateDirection(i);
            }
            if (!isDisable()) {
                removeView(this.mInnerParent);
                this.mInnerParent = null;
                if (this.mHelper != null) {
                    this.mInnerParent = this.mHelper.getLoadingView(this);
                    if (this.mInnerParent != null) {
                        setChildGravity(this.mInnerParent);
                        addView(this.mInnerParent);
                    }
                }
            }
        }
    }

    public final void setChildGravity(View view) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null || !(layoutParams instanceof FrameLayout.LayoutParams)) {
                layoutParams = new FrameLayout.LayoutParams(-2, -2, getGravity(getMode(), getScrollDirection()));
            } else {
                ((FrameLayout.LayoutParams) layoutParams).gravity = getGravity(getMode(), getScrollDirection());
            }
            view.setLayoutParams(layoutParams);
        }
    }

    private int getGravity(PullBase.Mode mode, int i) {
        return AnonymousClass1.$SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode[mode.ordinal()] != 1 ? i == 0 ? 81 : 21 : i == 0 ? 49 : 19;
    }

    public final ColorStateList getTextColor() {
        return this.mTextColor;
    }

    public final Drawable getLoadingDrawable() {
        return this.mLoadingDrawable;
    }

    public final CharSequence getPullLabel() {
        return this.mPullLabel;
    }

    public final CharSequence getRefreshingLabel() {
        return this.mRefreshingLabel;
    }

    public final CharSequence getReleaseLabel() {
        return this.mReleaseLabel;
    }

    /* access modifiers changed from: protected */
    public int getContentSize(int i) {
        int contentSize = this.mHelper != null ? this.mHelper.getContentSize(i) : 0;
        if (contentSize != 0) {
            return contentSize;
        }
        if (i == 1) {
            return this.mInnerParent != null ? this.mInnerParent.getWidth() : contentSize;
        }
        if (this.mInnerParent != null) {
            return this.mInnerParent.getHeight();
        }
        return contentSize;
    }

    public void setLoadingTextColor(ColorStateList colorStateList) {
        if (this.mHelper != null) {
            this.mHelper.setLoadingTextColor(colorStateList);
        }
    }

    public void setLoadingTextSize(float f) {
        if (this.mHelper != null) {
            this.mHelper.setTextSize(f);
        }
    }

    public void setLoadingDrawable(Drawable drawable) {
        if (this.mHelper != null) {
            this.mHelper.setLoadingDrawable(drawable);
        }
    }

    public void setLoadingDelegate(PtrLoadingDelegate ptrLoadingDelegate) {
        if (this.mHelper != null) {
            this.mHelper.setLoadingDelegate(ptrLoadingDelegate);
        }
        updateScrollDirection(getMode(), getScrollDirection());
    }

    public void onPull(float f) {
        if (this.mHelper != null && !isDisable()) {
            this.mHelper.onPull(f);
        }
    }

    public void onRelease(float f) {
        if (this.mHelper != null && !isDisable()) {
            this.mHelper.onRelease(f);
        }
    }

    public void onRefreshing() {
        if (this.mHelper != null && !isDisable()) {
            this.mHelper.onRefreshing();
        }
    }

    public void onCompleteUpdate(CharSequence charSequence) {
        if (this.mHelper != null && !isDisable()) {
            this.mHelper.onCompleteUpdate(charSequence);
        }
    }

    public void onReset() {
        if (this.mHelper != null && !isDisable()) {
            this.mHelper.onReset();
        }
    }

    public void onFreeze(boolean z, CharSequence charSequence) {
        if (this.mHelper != null && !isDisable()) {
            this.mHelper.onFreeze(z, charSequence);
        }
    }

    public void onDisable() {
        this.mIsDisable = true;
        removeView(this.mInnerParent);
        this.mInnerParent = null;
    }

    public void onEnable() {
        this.mIsDisable = false;
        updateScrollDirection(getMode(), getScrollDirection());
    }
}
