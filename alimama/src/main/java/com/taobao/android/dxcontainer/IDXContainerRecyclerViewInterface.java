package com.taobao.android.dxcontainer;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

public interface IDXContainerRecyclerViewInterface {
    RecyclerView newRecyclerView(Context context, DXContainerRecyclerViewOption dXContainerRecyclerViewOption);

    boolean setRecyclerViewAttr(RecyclerView recyclerView, DXContainerRecyclerViewOption dXContainerRecyclerViewOption);
}
