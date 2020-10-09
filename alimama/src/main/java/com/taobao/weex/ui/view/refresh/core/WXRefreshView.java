package com.taobao.weex.ui.view.refresh.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.ui.view.refresh.circlebar.CircleProgressBar;

public class WXRefreshView extends FrameLayout {
    /* access modifiers changed from: private */
    public CircleProgressBar circleProgressBar;
    /* access modifiers changed from: private */
    public LinearLayout linearLayout;

    public WXRefreshView(Context context) {
        super(context);
        setupViews();
    }

    public WXRefreshView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupViews();
    }

    public WXRefreshView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setupViews();
    }

    private void setupViews() {
        this.linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        this.linearLayout.setOrientation(1);
        this.linearLayout.setGravity(17);
        addView(this.linearLayout, layoutParams);
    }

    public void setContentGravity(int i) {
        if (this.linearLayout != null) {
            this.linearLayout.setGravity(i);
        }
    }

    public void setRefreshView(final View view) {
        if (view != null) {
            post(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    View view = view;
                    if (view.getParent() != null) {
                        ((ViewGroup) view.getParent()).removeView(view);
                    }
                    int i = 0;
                    while (true) {
                        ViewGroup viewGroup = (ViewGroup) view;
                        if (i < viewGroup.getChildCount()) {
                            View childAt = viewGroup.getChildAt(i);
                            if (childAt instanceof CircleProgressBar) {
                                CircleProgressBar unused = WXRefreshView.this.circleProgressBar = (CircleProgressBar) childAt;
                            }
                            i++;
                        } else {
                            WXRefreshView.this.linearLayout.addView(view);
                            return;
                        }
                    }
                }
            }));
        }
    }

    public void setProgressBgColor(int i) {
        if (this.circleProgressBar != null) {
            this.circleProgressBar.setBackgroundColor(i);
        }
    }

    public void setProgressColor(int i) {
        if (this.circleProgressBar != null) {
            this.circleProgressBar.setColorSchemeColors(i);
        }
    }

    /* access modifiers changed from: protected */
    public void startAnimation() {
        if (this.circleProgressBar != null) {
            this.circleProgressBar.start();
        }
    }

    public void setStartEndTrim(float f, float f2) {
        if (this.circleProgressBar != null) {
            this.circleProgressBar.setStartEndTrim(f, f2);
        }
    }

    /* access modifiers changed from: protected */
    public void stopAnimation() {
        if (this.circleProgressBar != null) {
            this.circleProgressBar.stop();
        }
    }

    public void setProgressRotation(float f) {
        if (this.circleProgressBar != null) {
            this.circleProgressBar.setProgressRotation(f);
        }
    }
}
