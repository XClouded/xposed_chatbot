package com.taobao.android.dxcontainer.loadmore;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IDXContainerLoadMoreController {
    public static final int STATE_FAILED = 3;
    public static final int STATE_LOADING = 1;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_NO_MORE = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    int getState();

    void setState(int i);
}
