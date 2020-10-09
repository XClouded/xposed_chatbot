package com.alimama.moon.features.home.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alimama.moon.R;
import com.alimamaunion.common.listpage.CommonBaseItem;
import com.alimamaunion.common.listpage.CommonBaseViewHolder;

public class HomeApprenticeAreaTitleViewHolder implements CommonBaseViewHolder<CommonBaseItem> {
    public void onBindViewHolder(int i, CommonBaseItem commonBaseItem) {
    }

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.fragment_home_apprentice_area_title, viewGroup, false);
    }
}
