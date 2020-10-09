package com.alimamaunion.common.listpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alimamaunion.common.listpage.CommonBaseItem;

public interface CommonBaseViewHolder<T extends CommonBaseItem> {
    View createView(LayoutInflater layoutInflater, ViewGroup viewGroup);

    void onBindViewHolder(int i, T t);
}
