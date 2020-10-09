package com.alimama.unwdinamicxcontainer.event;

import com.taobao.android.dinamicx.DXAbsEventHandler;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;

public class DXUNWCommonClickEventHandler extends DXAbsEventHandler {
    public static final long DX_EVENT_UNWCOMMONCLICK = 2391353566828719191L;

    public void prepareBindEventWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        super.prepareBindEventWithArgs(objArr, dXRuntimeContext);
    }

    public void handleEvent(DXEvent dXEvent, Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        DXEventManager.getInstance().dispatchEvent(objArr);
    }
}
