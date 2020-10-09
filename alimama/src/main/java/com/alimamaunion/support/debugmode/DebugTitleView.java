package com.alimamaunion.support.debugmode;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;

public class DebugTitleView extends RelativeLayout implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private int mInitScrollX;
    protected LinearLayout mItemContainer;
    protected int mSelColor;
    protected View mTopView;
    protected int mUnSelColor;
    protected ViewPager mViewPager;

    /* access modifiers changed from: protected */
    public void initView(Context context, AttributeSet attributeSet) {
    }

    public void notifySelected(ViewPager viewPager, int i) {
    }

    public void onClick(View view) {
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onPageSelected(int i) {
    }

    public void renderTitle(String[] strArr) {
    }

    public void setViewPager(ViewPager viewPager) {
    }

    public DebugTitleView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DebugTitleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSelColor = -850877;
        this.mUnSelColor = -16777216;
        initView(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public TextView renderTitleView(int i, String str, ViewGroup viewGroup) {
        return new TextView(getContext());
    }
}
