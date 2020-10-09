package com.alimama.unionwl.uiframe.views;

import android.content.Context;
import android.util.AttributeSet;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class EtaoViewPager extends ViewPager {
    private PagerAdapter mAdapter;
    private boolean mHasAttatched = false;

    public EtaoViewPager(Context context) {
        super(context);
    }

    public EtaoViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mHasAttatched = true;
        if (this.mAdapter != null) {
            setAdapter(this.mAdapter);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mHasAttatched = false;
        this.mAdapter = null;
    }

    public void storeAdapter(PagerAdapter pagerAdapter) {
        this.mAdapter = pagerAdapter;
        if (this.mHasAttatched) {
            setAdapter(this.mAdapter);
        }
    }
}
