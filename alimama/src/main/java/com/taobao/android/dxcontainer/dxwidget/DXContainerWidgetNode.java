package com.taobao.android.dxcontainer.dxwidget;

import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dxcontainer.DXContainerEngine;

public class DXContainerWidgetNode extends DXWidgetNode {
    protected DXContainerEngine containerEngine;

    public DXContainerEngine getContainerEngine() {
        return this.containerEngine;
    }

    public void setContainerEngine(DXContainerEngine dXContainerEngine) {
        this.containerEngine = dXContainerEngine;
    }
}
