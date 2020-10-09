package com.alimama.unwdinamicxcontainer.presenter.dxengine;

import com.alimama.unwdinamicxcontainer.model.dxengine.DXEngineDataModel;

public interface IDinamicXEngine {
    void onDestroy();

    void render(DXEngineDataModel dXEngineDataModel);

    void render(DXEngineDataModel dXEngineDataModel, IDXEnginePresenter iDXEnginePresenter);
}
