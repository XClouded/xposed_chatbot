package com.alimama.moon.features.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.HomeTabCateItem;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends FragmentPagerAdapter {
    private static final ArrayList DEFAULT = new ArrayList() {
        {
            add(new HomeTabCateItem("发现", "discovery", ""));
        }
    };
    private Context mContext;
    private List<HomeTabCateItem> mHomeTabList = new ArrayList();

    public HomeAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.mContext = context;
    }

    public void setHomeTabList(List<HomeTabCateItem> list) {
        if (list == null || list.isEmpty()) {
            list = DEFAULT;
        }
        this.mHomeTabList.clear();
        this.mHomeTabList.addAll(list);
        notifyDataSetChanged();
    }

    public Fragment getItem(int i) {
        String str = "";
        String str2 = "";
        String str3 = "";
        String str4 = "";
        if (i < this.mHomeTabList.size()) {
            str = this.mHomeTabList.get(i).getType();
            str2 = this.mHomeTabList.get(i).getFloorId();
            str3 = this.mHomeTabList.get(i).getQieId();
            str4 = this.mHomeTabList.get(i).getSpm();
        }
        return HomeCateFragment.newInstance(i, str, str2, str3, str4);
    }

    public int getCount() {
        if (this.mHomeTabList.isEmpty()) {
            return 1;
        }
        return this.mHomeTabList.size();
    }

    public View getTabView(int i) {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.fragment_home_tab_view, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.tab_title_tv)).setText(this.mHomeTabList.get(i).getTitle());
        inflate.setTag(this.mHomeTabList.get(i).getTitle());
        return inflate;
    }
}
