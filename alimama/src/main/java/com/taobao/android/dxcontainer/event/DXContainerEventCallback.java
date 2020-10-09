package com.taobao.android.dxcontainer.event;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;

public abstract class DXContainerEventCallback {
    public abstract void handleEvent(DXEvent dXEvent, Object[] objArr, DXRuntimeContext dXRuntimeContext);

    public void prepareBindEventWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
    }
}
