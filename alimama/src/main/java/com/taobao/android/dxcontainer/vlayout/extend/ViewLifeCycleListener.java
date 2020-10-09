package com.taobao.android.dxcontainer.vlayout.extend;

import android.view.View;

public interface ViewLifeCycleListener {
    void onAppeared(View view);

    void onAppearing(View view);

    void onDisappeared(View view);

    void onDisappearing(View view);
}
