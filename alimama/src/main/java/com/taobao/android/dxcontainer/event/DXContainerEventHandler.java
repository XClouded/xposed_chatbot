package com.taobao.android.dxcontainer.event;

import com.taobao.android.dinamicx.DXAbsEventHandler;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dxcontainer.DXContainerEngine;

public class DXContainerEventHandler extends DXAbsEventHandler {
    private DXContainerEngine engine;
    private DXContainerEventCallback eventCallback;

    public DXContainerEngine getContainerEngine() {
        return this.engine;
    }

    public void setContainerEngine(DXContainerEngine dXContainerEngine) {
        this.engine = dXContainerEngine;
    }

    public void setEventCallback(DXContainerEventCallback dXContainerEventCallback) {
        this.eventCallback = dXContainerEventCallback;
    }

    public void prepareBindEventWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        super.prepareBindEventWithArgs(objArr, dXRuntimeContext);
        if (this.eventCallback != null) {
            this.eventCallback.prepareBindEventWithArgs(objArr, dXRuntimeContext);
        }
    }

    public void handleEvent(DXEvent dXEvent, Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (this.eventCallback != null) {
            this.eventCallback.handleEvent(dXEvent, objArr, dXRuntimeContext);
        }
    }
}
