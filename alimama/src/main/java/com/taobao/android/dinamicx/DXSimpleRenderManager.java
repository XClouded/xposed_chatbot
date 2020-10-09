package com.taobao.android.dinamicx;

import com.taobao.android.dinamicx.render.diff.DXSimplePipelineDiff;

public class DXSimpleRenderManager extends DXRenderManager {
    public DXSimpleRenderManager() {
        this.absDiff = new DXSimplePipelineDiff();
    }
}
