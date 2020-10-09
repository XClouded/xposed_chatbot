package com.alimamaunion.support.debugmode;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class DebugViewpagerAdapter extends FragmentStatePagerAdapter {
    private List<List<DebugItemData>> mItemDataList = new ArrayList();

    public int getItemPosition(Object obj) {
        return -2;
    }

    public DebugViewpagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void setItemDataList(List<List<DebugItemData>> list) {
        this.mItemDataList.addAll(list);
    }

    @Nullable
    public Fragment getItem(int i) {
        if (i >= this.mItemDataList.size()) {
            return null;
        }
        return DebugFragment.newInstance(this.mItemDataList.get(i));
    }

    public int getCount() {
        return this.mItemDataList.size();
    }
}
