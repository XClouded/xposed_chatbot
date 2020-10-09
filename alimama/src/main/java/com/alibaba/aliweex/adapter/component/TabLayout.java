package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;

public class TabLayout extends FrameLayout {
    private OnTabSelectedListener mOnTabSelectedListener;
    private LinearLayout mRootView;
    private Tab mSelectedTab;
    private View.OnClickListener mTabClickListener;
    private WXTabbar mTabbar;
    private final ArrayList<Tab> mTabs = new ArrayList<>();

    public interface OnTabSelectedListener {
        void onTabReselected(Tab tab);

        void onTabSelected(Tab tab);

        void onTabUnselected(Tab tab);
    }

    public TabLayout(Context context, WXTabbar wXTabbar) {
        super(context);
        this.mTabbar = wXTabbar;
        init(context);
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public TabLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setBackgroundColor(0);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(80);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, (int) this.mTabbar.getLayoutHeight());
        layoutParams.gravity = 48;
        addView(linearLayout, layoutParams);
        this.mRootView = linearLayout;
    }

    public void updateSize() {
        if (this.mRootView != null && this.mTabs != null) {
            ViewGroup.LayoutParams layoutParams = this.mRootView.getLayoutParams();
            layoutParams.height = (int) this.mTabbar.getLayoutHeight();
            this.mRootView.setLayoutParams(layoutParams);
            for (int i = 0; i < this.mTabs.size(); i++) {
                View customView = this.mTabs.get(i).getCustomView();
                if (customView != null) {
                    ViewGroup.LayoutParams layoutParams2 = customView.getLayoutParams();
                    layoutParams2.height = (int) this.mTabbar.getLayoutHeight();
                    customView.setLayoutParams(layoutParams2);
                }
            }
        }
    }

    @Nullable
    public Tab getTabAt(int i) {
        return this.mTabs.get(i);
    }

    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        boolean z = layoutParams instanceof FrameLayout.LayoutParams;
        super.setLayoutParams(layoutParams);
    }

    public LinearLayout getRootView() {
        return this.mRootView;
    }

    /* access modifiers changed from: private */
    public void updateTab(int i) {
        View customView;
        ViewParent parent;
        Tab tabAt = getTabAt(i);
        if (tabAt != null && (customView = tabAt.getCustomView()) != null && (parent = customView.getParent()) != this) {
            if (parent != null) {
                ((ViewGroup) parent).removeView(customView);
            }
            addView(customView);
        }
    }

    private void addTabView(Tab tab, boolean z) {
        View customView = tab.getCustomView();
        if (this.mTabClickListener == null) {
            this.mTabClickListener = new View.OnClickListener() {
                public void onClick(View view) {
                    ((Tab) view.getTag()).select();
                }
            };
        }
        if (customView != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, (int) this.mTabbar.getLayoutHeight());
            layoutParams.weight = 1.0f;
            customView.setTag(tab);
            customView.setLayoutParams(layoutParams);
            customView.setOnClickListener(this.mTabClickListener);
            this.mRootView.addView(customView);
            if (z) {
                customView.setSelected(true);
            }
        }
    }

    public void addTab(@NonNull Tab tab, boolean z) {
        if (tab.mParent == this) {
            addTabView(tab, z);
            int size = this.mTabs.size();
            tab.setPosition(this.mTabs.size());
            this.mTabs.add(size, tab);
            int size2 = this.mTabs.size();
            while (true) {
                size++;
                if (size >= size2) {
                    break;
                }
                this.mTabs.get(size).setPosition(size);
            }
            if (z) {
                tab.select();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    @NonNull
    public Tab newTab() {
        return new Tab(this);
    }

    public void removeAllTabs() {
        Iterator<Tab> it = this.mTabs.iterator();
        while (it.hasNext()) {
            it.next().setPosition(-1);
            it.remove();
        }
        this.mSelectedTab = null;
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mOnTabSelectedListener = onTabSelectedListener;
    }

    /* access modifiers changed from: package-private */
    public void selectTab(Tab tab) {
        if (this.mSelectedTab != tab) {
            if (!(this.mSelectedTab == null || this.mOnTabSelectedListener == null)) {
                this.mOnTabSelectedListener.onTabUnselected(this.mSelectedTab);
            }
            this.mSelectedTab = tab;
            if (this.mSelectedTab != null && this.mOnTabSelectedListener != null) {
                this.mOnTabSelectedListener.onTabSelected(this.mSelectedTab);
            }
        } else if (this.mSelectedTab != null && this.mOnTabSelectedListener != null) {
            this.mOnTabSelectedListener.onTabReselected(this.mSelectedTab);
        }
    }

    public int getSelectedTabPosition() {
        if (this.mSelectedTab != null) {
            return this.mSelectedTab.getPosition();
        }
        return -1;
    }

    public static final class Tab {
        public static final int INVALID_POSITION = -1;
        private View mCustomView;
        /* access modifiers changed from: private */
        public final TabLayout mParent;
        private int mPosition = -1;

        Tab(TabLayout tabLayout) {
            this.mParent = tabLayout;
        }

        @Nullable
        public View getCustomView() {
            return this.mCustomView;
        }

        @NonNull
        public Tab setCustomView(@Nullable View view) {
            this.mCustomView = view;
            if (this.mPosition >= 0) {
                this.mParent.updateTab(this.mPosition);
            }
            return this;
        }

        public int getPosition() {
            return this.mPosition;
        }

        /* access modifiers changed from: package-private */
        public void setPosition(int i) {
            this.mPosition = i;
        }

        public void select() {
            this.mCustomView.setSelected(true);
            this.mParent.selectTab(this);
        }

        public boolean isSelected() {
            return this.mParent.getSelectedTabPosition() == this.mPosition;
        }
    }
}
