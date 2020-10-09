package com.taobao.android.dxcontainer.life;

import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dxcontainer.DXContainerModel;

public interface EngineModelExchangeListener {
    void onDXCModelCreated(DXContainerModel dXContainerModel);

    void onTemplateCreated(DXTemplateItem dXTemplateItem);
}
