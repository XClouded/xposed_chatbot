package com.alimama.unwdinamicxcontainer.presenter.dxcengine;

import android.view.View;
import com.alimama.unwdinamicxcontainer.model.dxcengine.UltronageDataInvalidType;
import com.taobao.android.dxcontainer.loadmore.IDXContainerLoadMoreController;

public interface IDXContainerPresenter {
    void clickErrorViewRefreshBtn();

    void initDataError();

    void isEmptyList();

    void loadMore(IDXContainerLoadMoreController iDXContainerLoadMoreController);

    void recyclerViewScrollStateChanged(int i);

    void renderFailed(String str);

    void renderSuccess(View view);

    void ultronageDataInValid(UltronageDataInvalidType ultronageDataInvalidType);
}
