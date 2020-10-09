package com.alimama.unwdinamicxcontainer.presenter.dxcengine;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.unwdinamicxcontainer.diywidget.viewcontainer.ISViewContainer;
import com.alimama.unwdinamicxcontainer.model.dxcengine.DXCLoadMoreModel;
import com.alimama.unwdinamicxcontainer.model.dxcengine.GlobalModel;
import com.alimama.unwdinamicxcontainer.presenter.dxcengine.UNWDinamicXContainerEngine;

public interface IDXContainerEngine {
    void appendData(String str);

    void configLoadMoreModel(DXCLoadMoreModel dXCLoadMoreModel);

    void enableFooterView(boolean z);

    GlobalModel fetchGlobalModel();

    RecyclerView fetchRecyclerView();

    View fetchRenderedView();

    ISViewContainer fetchViewContainer();

    void initData(String str, String str2);

    void insertData(String str, int i);

    void refresh();

    void registerEmptyView(View view, ViewGroup.LayoutParams layoutParams);

    void removeData(String str);

    void showEmptyView(String str);

    void showErrorView(View view);

    void showErrorView(View view, ViewGroup.LayoutParams layoutParams);

    void showErrorView(String str);

    void updateDXCLoadMoreState(int i);

    void updateData(String str, UNWDinamicXContainerEngine.OrderType orderType, boolean z, IDXCUpdateDataListener iDXCUpdateDataListener);
}
