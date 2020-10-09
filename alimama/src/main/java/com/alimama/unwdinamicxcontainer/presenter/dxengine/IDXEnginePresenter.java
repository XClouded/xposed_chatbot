package com.alimama.unwdinamicxcontainer.presenter.dxengine;

import android.view.View;

public interface IDXEnginePresenter {
    void renderFailed(String str);

    void renderSuccess(View view);
}
