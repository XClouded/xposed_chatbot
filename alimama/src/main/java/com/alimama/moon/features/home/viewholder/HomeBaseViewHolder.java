package com.alimama.moon.features.home.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alimama.moon.features.home.item.HomeBaseItem;
import com.alimamaunion.common.listpage.CommonBaseViewHolder;

public interface HomeBaseViewHolder<T extends HomeBaseItem> extends CommonBaseViewHolder<T> {

    /* renamed from: com.alimama.moon.features.home.viewholder.HomeBaseViewHolder$-CC  reason: invalid class name */
    public final /* synthetic */ class CC {
    }

    View createView(LayoutInflater layoutInflater, ViewGroup viewGroup);

    void onBindViewHolder(int i, T t);
}
