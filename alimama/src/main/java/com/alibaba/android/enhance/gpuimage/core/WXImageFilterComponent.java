package com.alibaba.android.enhance.gpuimage.core;

import android.content.Context;
import android.opengl.GLSurfaceView;
import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;

public class WXImageFilterComponent extends WXComponent<GLSurfaceView> {
    public WXImageFilterComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXImageFilterComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, int i, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, i, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public GLSurfaceView initComponentHostView(@NonNull Context context) {
        return new GLImageFilterView(context);
    }

    public void onActivityStop() {
        super.onActivityStop();
        if (getHostView() != null) {
            ((GLSurfaceView) getHostView()).onPause();
        }
    }

    public void onActivityStart() {
        super.onActivityStart();
        if (getHostView() != null) {
            ((GLSurfaceView) getHostView()).onResume();
        }
    }
}
