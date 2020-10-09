package com.alimama.moon.features.home;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.viewpager.widget.ViewPager;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.dinamicx.util.BulletinBoardManager;
import com.alimama.moon.features.home.item.HomeRefreshData;
import com.alimama.moon.features.home.item.HomeTabCateItem;
import com.alimama.moon.features.home.network.HomeTabDataEvent;
import com.alimama.moon.features.home.theme.HomeThemeDataItem;
import com.alimama.moon.features.home.theme.HomeThemeDataManager;
import com.alimama.moon.ui.IBottomNavFragment;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.unionwl.utils.LocalDisplay;
import com.google.android.material.tabs.TabLayout;
import com.ut.mini.UTAnalytics;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomeFragment extends BaseFragment implements IBottomNavFragment {
    private HomeAdapter mAdapter;
    private LinearLayout mContainer;
    private TabLayout mTabLayout;
    private List<HomeTabCateItem> mTabList = new ArrayList();
    private ViewPager mViewPager;

    public String currFragmentTitle() {
        return null;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        this.mTabLayout = (TabLayout) inflate.findViewById(R.id.home_tablayout);
        this.mViewPager = (ViewPager) inflate.findViewById(R.id.home_viewpager);
        this.mContainer = (LinearLayout) inflate.findViewById(R.id.home_container);
        initView();
        return inflate;
    }

    private void initView() {
        this.mTabLayout.setVisibility(8);
        this.mAdapter = new HomeAdapter(getChildFragmentManager(), getContext());
        this.mAdapter.setHomeTabList(this.mTabList);
        this.mViewPager.setAdapter(this.mAdapter);
        this.mTabLayout.setupWithViewPager(this.mViewPager);
        updateTabLayoutSkin();
        setTabCustomStyle();
    }

    @Subscribe
    public void onHomeThemeDataItem(HomeThemeDataItem homeThemeDataItem) {
        updateTabLayoutSkin();
    }

    private void updateTabLayoutSkin() {
        try {
            if (HomeThemeDataManager.getInstance().isSwitchConfigCenterTheme()) {
                HomeThemeDataItem homeThemeDataItem = HomeThemeDataManager.getInstance().themeDataItem;
                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(homeThemeDataItem.tabStartColor), Color.parseColor(homeThemeDataItem.tabEndColor)});
                this.mTabLayout.setBackgroundDrawable(gradientDrawable);
                this.mContainer.setBackgroundDrawable(gradientDrawable);
                return;
            }
            this.mTabLayout.setBackgroundColor(getResources().getColor(R.color.home_tab_bg));
            this.mContainer.setBackgroundColor(getResources().getColor(R.color.home_tab_bg));
        } catch (Exception unused) {
        }
    }

    public void willBeDisplayed() {
        UTHelper.HomePage.showHomePage();
        UnionLensUtil.generatePrepvid();
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity(), UTHelper.HomePage.PAGE_NAME);
    }

    public void willBeHidden() {
        UTHelper.HomePage.hiddenHomePage();
        SpmProcessor.pageDisappear(getActivity(), UTHelper.HomePage.SPM_CNT);
    }

    public void refresh() {
        EventBus.getDefault().post(new HomeRefreshData(true));
    }

    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && App.appStart) {
            UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity(), UTHelper.HomePage.PAGE_NAME);
            UTHelper.HomePage.showHomePage();
            App.appStart = false;
            BulletinBoardManager.getInstance().showBulletinBoard(this);
        }
    }

    public void onPause() {
        super.onPause();
    }

    @Subscribe
    public void onHomeTabDataEvent(HomeTabDataEvent homeTabDataEvent) {
        if (homeTabDataEvent.mHomeTabList == null || homeTabDataEvent.mHomeTabList.isEmpty() || homeTabDataEvent.mHomeTabList.size() <= 1) {
            this.mTabLayout.setVisibility(8);
            UTHelper.HomePage.renderWorshipSingleTab();
            this.mContainer.setPadding(0, LocalDisplay.dp2px(10.0f), 0, 0);
            this.mTabList = homeTabDataEvent.mHomeTabList;
            this.mAdapter.setHomeTabList(this.mTabList);
            return;
        }
        this.mTabLayout.setVisibility(0);
        UTHelper.HomePage.renderNormalMultipleTab();
        this.mContainer.setPadding(0, 0, 0, 0);
        this.mTabList = homeTabDataEvent.mHomeTabList;
        this.mAdapter.setHomeTabList(this.mTabList);
        setTabCustomStyle();
    }

    private void setTabCustomStyle() {
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            TabLayout.Tab tabAt = this.mTabLayout.getTabAt(i);
            if (tabAt != null) {
                tabAt.setCustomView(this.mAdapter.getTabView(i));
                if (tabAt.getCustomView() != null) {
                    final String str = (String) tabAt.getCustomView().getTag();
                    ((View) tabAt.getCustomView().getParent()).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            UTHelper.HomePage.clickTabCate(str);
                        }
                    });
                }
            }
        }
        this.mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    customView.findViewById(R.id.tab_title_tv).setSelected(true);
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    customView.findViewById(R.id.tab_title_tv).setSelected(false);
                }
            }
        });
    }
}
