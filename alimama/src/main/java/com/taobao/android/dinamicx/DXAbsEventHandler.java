package com.taobao.android.dinamicx;

import com.taobao.android.dinamicx.expression.event.DXEvent;

public abstract class DXAbsEventHandler implements IDXEventHandler {
    public abstract void handleEvent(DXEvent dXEvent, Object[] objArr, DXRuntimeContext dXRuntimeContext);

    public void prepareBindEventWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
    }
}
