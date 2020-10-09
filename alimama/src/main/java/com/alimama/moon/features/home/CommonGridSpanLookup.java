package com.alimama.moon.features.home;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimamaunion.common.listpage.CommonItemInfo;

public class CommonGridSpanLookup extends GridLayoutManager.SpanSizeLookup {
    private RecyclerView.Adapter mAdapter;

    public CommonGridSpanLookup(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
    }

    public int getSpanSize(int i) {
        CommonItemInfo findItemInfo = CommonItemInfo.findItemInfo(this.mAdapter.getItemViewType(i));
        if (findItemInfo != null) {
            return findItemInfo.spanSize;
        }
        return 20;
    }
}
