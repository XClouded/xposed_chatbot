package com.taobao.uikit.feature.callback;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerAdapterCallback {
    RecyclerView.Adapter wrapAdapter(RecyclerView.Adapter adapter);
}
