package com.taobao.uikit.feature.features;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.ScrollCallback;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ParallaxScrollFeature extends AbsFeature<ScrollView> implements ScrollCallback {
    /* access modifiers changed from: private */
    public static boolean isAPI11 = (Build.VERSION.SDK_INT >= 11);
    private float mInnerParallaxFactor = 1.8f;
    private float mParallaxFactor = 1.8f;
    private int mParallaxNum = 1;
    private ArrayList<ParallaxView> mParallaxViews = new ArrayList<>();

    public void afterComputeScroll() {
    }

    public void beforeComputeScroll() {
    }

    public void beforeOnScrollChanged(int i, int i2, int i3, int i4) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        if (attributeSet != null && (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ParallaxScrollFeature, i, 0)) != null) {
            this.mParallaxFactor = obtainStyledAttributes.getFloat(R.styleable.ParallaxScrollFeature_uik_parallaxFactor, this.mParallaxFactor);
            this.mInnerParallaxFactor = obtainStyledAttributes.getFloat(R.styleable.ParallaxScrollFeature_uik_innerParallaxFactor, this.mInnerParallaxFactor);
            this.mParallaxNum = obtainStyledAttributes.getInt(R.styleable.ParallaxScrollFeature_uik_parallaxNum, this.mParallaxNum);
            obtainStyledAttributes.recycle();
        }
    }

    public void afterOnScrollChanged(int i, int i2, int i3, int i4) {
        float f = this.mParallaxFactor;
        int size = this.mParallaxViews.size();
        for (int i5 = 0; i5 < size; i5++) {
            this.mParallaxViews.get(i5).setOffset(((float) i2) / f);
            f *= this.mInnerParallaxFactor;
        }
    }

    public void setHost(ScrollView scrollView) {
        super.setHost(scrollView);
        ((ScrollView) getHost()).setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            public void onChildViewAdded(View view, View view2) {
                ParallaxScrollFeature.this.makeViewsParallax();
            }

            public void onChildViewRemoved(View view, View view2) {
                ParallaxScrollFeature.this.makeViewsParallax();
            }
        });
    }

    /* access modifiers changed from: private */
    public void makeViewsParallax() {
        this.mParallaxViews.clear();
        if (((ScrollView) getHost()).getChildCount() > 0) {
            if (((ScrollView) getHost()).getChildAt(0) instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) ((ScrollView) getHost()).getChildAt(0);
                int min = Math.min(this.mParallaxNum, viewGroup.getChildCount());
                for (int i = 0; i < min; i++) {
                    this.mParallaxViews.add(new ParallaxView(viewGroup.getChildAt(i)));
                }
            }
        }
    }

    public void setParallaxNum(int i) {
        this.mParallaxNum = i;
        makeViewsParallax();
    }

    public void setParallaxFactor(float f) {
        this.mParallaxFactor = f;
    }

    public void setInnerParallaxFactor(float f) {
        this.mInnerParallaxFactor = f;
    }

    class ParallaxView {
        protected int lastOffset = 0;
        protected WeakReference<View> view;

        public ParallaxView(View view2) {
            this.view = new WeakReference<>(view2);
        }

        @SuppressLint({"NewApi"})
        public void setOffset(float f) {
            View view2 = (View) this.view.get();
            if (view2 == null) {
                return;
            }
            if (ParallaxScrollFeature.isAPI11) {
                view2.setTranslationY(f);
            } else {
                translatePreICS(view2, f);
            }
        }

        /* access modifiers changed from: protected */
        public void translatePreICS(View view2, float f) {
            int i = (int) f;
            view2.offsetTopAndBottom(i - this.lastOffset);
            this.lastOffset = i;
        }
    }
}
