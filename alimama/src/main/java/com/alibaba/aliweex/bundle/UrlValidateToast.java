package com.alibaba.aliweex.bundle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;

public class UrlValidateToast {
    /* access modifiers changed from: private */
    public boolean cancelNoti = true;
    private Context mContext;
    private View mView;
    /* access modifiers changed from: private */
    public TextView mWarningView = null;

    public UrlValidateToast(Context context, View view) {
        this.mContext = context;
        this.mView = view;
    }

    @TargetApi(21)
    private void setNotiView(Drawable drawable, String str, int i) {
        this.mWarningView = new TextView(this.mContext);
        this.mWarningView.setTextColor(Color.parseColor("#666666"));
        this.mWarningView.setBackgroundColor(Color.parseColor("#ffe7b3"));
        this.mWarningView.setText(str);
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
        animatorSet.play(ofFloat2).after(TBToast.Duration.MEDIUM);
        ofFloat2.addListener(new Animator.AnimatorListener() {
            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                if (UrlValidateToast.this.mWarningView != null) {
                    ViewParent parent = UrlValidateToast.this.mWarningView.getParent();
                    if (parent != null && (parent instanceof ViewGroup)) {
                        ((ViewGroup) parent).removeView(UrlValidateToast.this.mWarningView);
                    }
                    TextView unused = UrlValidateToast.this.mWarningView = null;
                }
            }

            public void onAnimationCancel(Animator animator) {
                if (UrlValidateToast.this.mWarningView != null) {
                    ViewParent parent = UrlValidateToast.this.mWarningView.getParent();
                    if (parent != null && (parent instanceof ViewGroup)) {
                        ((ViewGroup) parent).removeView(UrlValidateToast.this.mWarningView);
                    }
                    TextView unused = UrlValidateToast.this.mWarningView = null;
                }
            }
        });
        animatorSet.start();
        this.mWarningView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (UrlValidateToast.this.cancelNoti) {
                    animatorSet.cancel();
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(ofFloat2);
                    animatorSet.start();
                    boolean unused = UrlValidateToast.this.cancelNoti = false;
                }
            }
        });
    }
}
