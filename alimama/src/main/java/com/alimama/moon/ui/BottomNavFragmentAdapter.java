package com.alimama.moon.ui;

import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.alimama.moon.config.MoonConfigCenter;
import com.alimama.moon.features.home.HomeFragment;
import com.alimama.moon.features.reports.ReportFragment;
import com.alimama.moon.ui.fragment.DiscoveryFragment;
import com.alimama.union.app.personalCenter.view.NewMineFragment;
import com.alimama.union.app.toolCenter.view.ToolFragment;
import com.alimama.union.app.webContainer.WebFragment;
import java.util.HashMap;
import java.util.Map;

public class BottomNavFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;
    private Map<Integer, String> fragmentTagMap = new HashMap();

    public BottomNavFragmentAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity.getSupportFragmentManager());
        this.fragmentManager = fragmentActivity.getSupportFragmentManager();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        Fragment fragment = (Fragment) super.instantiateItem(viewGroup, i);
        this.fragmentTagMap.put(Integer.valueOf(i), fragment.getTag());
        return fragment;
    }

    public Fragment getItem(int i) {
        if (BottomNavActivity.sIsSwitchMidH5Tab) {
            switch (i) {
                case 0:
                    if (MoonConfigCenter.isHomeWeexSwitchOn()) {
                        return DiscoveryFragment.newInstance();
                    }
                    return HomeFragment.newInstance();
                case 1:
                    return ToolFragment.newInstance();
                case 2:
                    return WebFragment.newInstance();
                case 3:
                    return ReportFragment.newInstance();
                case 4:
                    return NewMineFragment.newInstance();
            }
        } else {
            switch (i) {
                case 0:
                    if (MoonConfigCenter.isHomeWeexSwitchOn()) {
                        return DiscoveryFragment.newInstance();
                    }
                    return HomeFragment.newInstance();
                case 1:
                    return ToolFragment.newInstance();
                case 2:
                    return ReportFragment.newInstance();
                case 3:
                    return NewMineFragment.newInstance();
            }
        }
        return HomeFragment.newInstance();
    }

    public int getCount() {
        return BottomNavActivity.sPageCount;
    }

    public IBottomNavFragment getCurrentFragment(int i) {
        Fragment findFragmentByTag = this.fragmentManager.findFragmentByTag(this.fragmentTagMap.get(Integer.valueOf(i)));
        if (findFragmentByTag != null) {
            return (IBottomNavFragment) findFragmentByTag;
        }
        return null;
    }
}
