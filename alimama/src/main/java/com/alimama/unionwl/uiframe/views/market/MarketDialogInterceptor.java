package com.alimama.unionwl.uiframe.views.market;

import android.view.View;

public interface MarketDialogInterceptor {
    void onDismissIntercept();

    void onImageClickIntercept(View view);

    void onShowIntercept();
}
