package com.taobao.weex.analyzer.view.overlay;

import android.content.Context;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.IPermissionHandler;

public abstract class PermissionOverlayView extends DragSupportOverlayView implements IPermissionHandler {
    protected Config mConfig;

    public PermissionOverlayView(Context context) {
        super(context);
        this.mConfig = null;
    }

    public PermissionOverlayView(Context context, boolean z) {
        super(context, z);
        this.mConfig = null;
    }

    public PermissionOverlayView(Context context, boolean z, Config config) {
        super(context, z);
        this.mConfig = config;
    }

    public void show() {
        if (this.mConfig == null || isPermissionGranted(this.mConfig)) {
            super.show();
        }
    }
}
