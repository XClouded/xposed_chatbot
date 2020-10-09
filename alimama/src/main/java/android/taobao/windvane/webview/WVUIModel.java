package android.taobao.windvane.webview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.taobao.windvane.view.AbstractNaviBar;
import android.taobao.windvane.view.WebErrorView;
import android.taobao.windvane.view.WebWaitingView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

public class WVUIModel {
    /* access modifiers changed from: private */
    public boolean cancelNoti = true;
    private LinearLayout errorLayout;
    private boolean errorShow = false;
    private View errorView = null;
    private AtomicBoolean isError = new AtomicBoolean(false);
    private View loadingView = null;
    private Context mContext;
    private View mView;
    /* access modifiers changed from: private */
    public TextView mWarningView = null;
    private AbstractNaviBar naviBar = null;
    private boolean showLoading = false;

    public WVUIModel(Context context, View view) {
        this.mContext = context;
        this.mView = view;
        this.errorLayout = new LinearLayout(context);
    }

    public void enableShowLoading() {
        this.showLoading = true;
    }

    public void disableShowLoading() {
        this.showLoading = false;
    }

    public boolean isShowLoading() {
        return this.showLoading;
    }

    public void showLoadingView() {
        if (this.loadingView == null) {
            this.loadingView = new WebWaitingView(this.mContext);
            setLoadingView(this.loadingView);
        }
        this.loadingView.bringToFront();
        if (this.loadingView.getVisibility() != 0) {
            this.loadingView.setVisibility(0);
        }
    }

    public void hideLoadingView() {
        if (this.loadingView != null && this.loadingView.getVisibility() != 8) {
            this.loadingView.setVisibility(8);
        }
    }

    public void setLoadingView(View view) {
        if (view != null) {
            this.loadingView = view;
            this.loadingView.setVisibility(8);
            ViewParent parent = this.loadingView.getParent();
            if (parent != null && (parent instanceof ViewGroup)) {
                ((ViewGroup) parent).removeView(this.loadingView);
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(14, 1);
            ViewParent parent2 = this.mView.getParent();
            if (parent2 != null) {
                try {
                    ((ViewGroup) parent2).addView(this.loadingView, layoutParams);
                } catch (Exception unused) {
                    ViewParent parent3 = parent2.getParent();
                    if (parent3 != null) {
                        ((ViewGroup) parent3).addView(this.loadingView, layoutParams);
                    }
                }
            }
        }
    }

    public void loadErrorPage() {
        if (this.errorView == null) {
            this.errorView = new WebErrorView(this.mContext);
            setErrorView(this.errorView);
        }
        this.errorLayout.bringToFront();
        if (this.errorLayout.getVisibility() != 0) {
            this.errorLayout.setVisibility(0);
            this.errorShow = true;
        }
    }

    public void hideErrorPage() {
        if (this.errorLayout != null && this.errorLayout.getVisibility() != 8) {
            this.errorLayout.setVisibility(8);
            this.errorShow = false;
        }
    }

    public boolean isErrorShow() {
        return this.errorShow;
    }

    public void setErrorView(View view) {
        if (view != null && this.isError.compareAndSet(false, true)) {
            this.errorView = view;
            this.errorLayout.setVisibility(8);
            ViewParent parent = this.errorView.getParent();
            if (parent != null && (parent instanceof ViewGroup)) {
                ((ViewGroup) parent).removeView(this.errorView);
            }
            ViewGroup.LayoutParams layoutParams = this.mView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            }
            if (layoutParams instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) layoutParams).addRule(14, 1);
            }
            this.errorLayout.addView(this.errorView, layoutParams);
            this.errorLayout.setBackgroundColor(-1);
            this.errorLayout.setAlpha(1.0f);
            ViewParent parent2 = this.mView.getParent();
            if (parent2 != null) {
                try {
                    if (this.errorLayout.getParent() == null) {
                        ((ViewGroup) parent2).addView(this.errorLayout, layoutParams);
                    }
                    this.isError.set(false);
                } catch (Exception unused) {
                    ViewParent parent3 = parent2.getParent();
                    if (parent3 != null && this.errorLayout.getParent() == null) {
                        ((ViewGroup) parent3).addView(this.errorLayout, layoutParams);
                    }
                    this.isError.set(false);
                }
            }
        }
    }

    public View getErrorView() {
        if (this.errorView == null) {
            setErrorView(new WebErrorView(this.mContext));
        }
        return this.errorView;
    }

    public void setNaviBar(AbstractNaviBar abstractNaviBar) {
        if (this.naviBar != null) {
            this.naviBar.setVisibility(8);
            this.naviBar = null;
        }
        if (abstractNaviBar != null) {
            this.naviBar = abstractNaviBar;
        }
    }

    public void resetNaviBar() {
        if (this.naviBar != null) {
            this.naviBar.resetState();
        }
    }

    public void hideNaviBar() {
        if (this.naviBar != null && this.naviBar.getVisibility() != 8) {
            this.naviBar.setVisibility(8);
        }
    }

    public void switchNaviBar(int i) {
        if (this.naviBar != null && i == 1) {
            this.naviBar.startLoading();
        }
    }

    private void setNotiView(Drawable drawable, String str, int i) {
        this.mWarningView = new TextView(this.mContext);
        this.mWarningView.setTextColor(Color.parseColor("#666666"));
        this.mWarningView.setBackgroundColor(Color.parseColor("#ffe7b3"));
        this.mWarningView.setText(str);
        this.mWarningView.setTextAlignment(4);
        this.mWarningView.setGravity(16);
        ViewParent parent = this.mWarningView.getParent();
        if (parent != null && (parent instanceof ViewGroup)) {
            ((ViewGroup) parent).removeView(this.mWarningView);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            this.mWarningView.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
            int i2 = i / 10;
            this.mWarningView.setCompoundDrawablePadding(i2);
            this.mWarningView.setPadding(i2, 0, 0, 0);
        }
        try {
            this.mWarningView.setElevation(2.0f);
        } catch (Throwable unused) {
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, i);
        ViewParent parent2 = this.mView.getParent();
        if (parent2 != null) {
            try {
                ((ViewGroup) parent2).addView(this.mWarningView, layoutParams);
            } catch (Exception unused2) {
                ViewParent parent3 = parent2.getParent();
                if (parent3 != null) {
                    ((ViewGroup) parent3).addView(this.mWarningView, layoutParams);
                }
            }
        }
    }

    public void showNotiView(Drawable drawable, String str, int i) {
        if (this.mWarningView == null || (str != null && !str.equals(this.mWarningView.getText()))) {
            setNotiView(drawable, str, i);
        }
        this.mWarningView.bringToFront();
        this.mWarningView.setTranslationY(0.0f);
        float f = (float) (-i);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mWarningView, "translationY", new float[]{f, 0.0f});
        ofFloat.setInterpolator(new DecelerateInterpolator());
        ofFloat.setDuration(1000);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mWarningView, "translationY", new float[]{0.0f, f});
        ofFloat2.setDuration(1000);
        ofFloat2.setInterpolator(new DecelerateInterpolator());
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofFloat);
        animatorSet.play(ofFloat2).after(6000);
        ofFloat2.addListener(new Animator.AnimatorListener() {
            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                if (WVUIModel.this.mWarningView != null) {
                    ViewParent parent = WVUIModel.this.mWarningView.getParent();
                    if (parent != null && (parent instanceof ViewGroup)) {
                        ((ViewGroup) parent).removeView(WVUIModel.this.mWarningView);
                    }
                    TextView unused = WVUIModel.this.mWarningView = null;
                }
            }

            public void onAnimationCancel(Animator animator) {
                if (WVUIModel.this.mWarningView != null) {
                    ViewParent parent = WVUIModel.this.mWarningView.getParent();
                    if (parent != null && (parent instanceof ViewGroup)) {
                        ((ViewGroup) parent).removeView(WVUIModel.this.mWarningView);
                    }
                    TextView unused = WVUIModel.this.mWarningView = null;
                }
            }
        });
        animatorSet.start();
        this.mWarningView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (WVUIModel.this.cancelNoti) {
                    animatorSet.cancel();
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(ofFloat2);
                    animatorSet.start();
                    boolean unused = WVUIModel.this.cancelNoti = false;
                }
            }
        });
    }
}
