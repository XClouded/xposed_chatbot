package com.taobao.android.dxcontainer;

import android.view.ViewGroup;

public interface IDXContainerWrapper {
    void setCurrentChild(ViewGroup viewGroup);

    void setRoot(ViewGroup viewGroup);

    void setTopHeight(int i);
}
