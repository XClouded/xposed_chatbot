package com.taobao.android.dxcontainer;

import com.taobao.android.dinamicx.DXUserContext;
import java.lang.ref.WeakReference;

public class DXContainerUserContext extends DXUserContext {
    public WeakReference<DXContainerModel> dxcModelWeakReference;
    public WeakReference<DXContainerEngine> engineWeakReference;
}
