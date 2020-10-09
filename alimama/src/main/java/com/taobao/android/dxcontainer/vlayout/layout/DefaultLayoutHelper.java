package com.taobao.android.dxcontainer.vlayout.layout;

import com.taobao.android.dxcontainer.vlayout.LayoutHelper;

public class DefaultLayoutHelper extends LinearLayoutHelper {
    public boolean isOutOfRange(int i) {
        return false;
    }

    public static LayoutHelper newHelper(int i) {
        DefaultLayoutHelper defaultLayoutHelper = new DefaultLayoutHelper();
        defaultLayoutHelper.setItemCount(i);
        return defaultLayoutHelper;
    }
}
