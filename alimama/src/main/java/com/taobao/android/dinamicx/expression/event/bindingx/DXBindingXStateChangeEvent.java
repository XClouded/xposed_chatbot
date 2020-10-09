package com.taobao.android.dinamicx.expression.event.bindingx;

import com.taobao.android.dinamicx.expression.event.DXEvent;

public class DXBindingXStateChangeEvent extends DXEvent {
    public static final long DXVIEWWIDGETNODE_ONBINDINGXFINISH = -5192979070104500639L;
    public static final long DXVIEWWIDGETNODE_ONBINDINGXSTART = -1026451533627932147L;
    public static final long DXVIEWWIDGETNODE_ONBINDINGXSTOP = 6689515913358780580L;
    String specName;

    public DXBindingXStateChangeEvent(long j, String str) {
        super(j);
        this.specName = str;
    }

    public String getSpecName() {
        return this.specName;
    }

    public void setSpecName(String str) {
        this.specName = str;
    }
}
