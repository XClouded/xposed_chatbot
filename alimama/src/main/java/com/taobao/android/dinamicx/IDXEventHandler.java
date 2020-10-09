package com.taobao.android.dinamicx;

import com.taobao.android.dinamicx.expression.event.DXEvent;

public interface IDXEventHandler {
    void handleEvent(DXEvent dXEvent, Object[] objArr, DXRuntimeContext dXRuntimeContext);

    void prepareBindEventWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext);
}
