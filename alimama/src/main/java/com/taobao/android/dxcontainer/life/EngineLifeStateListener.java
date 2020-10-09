package com.taobao.android.dxcontainer.life;

import android.view.View;
import android.view.ViewGroup;
import com.taobao.android.dxcontainer.DXContainerModel;

public interface EngineLifeStateListener {
    void afterBindViewHolder(View view, int i, DXContainerModel dXContainerModel, String str);

    void afterCreateViewHolder(ViewGroup viewGroup, int i, String str, String str2, Object obj);

    void beforeBindViewHolder(View view, int i, DXContainerModel dXContainerModel, String str);

    void beforeCreateViewHolder(ViewGroup viewGroup, int i, String str, String str2, Object obj);

    void onViewRecycled(View view, DXContainerModel dXContainerModel, String str, String str2, Object obj);
}
