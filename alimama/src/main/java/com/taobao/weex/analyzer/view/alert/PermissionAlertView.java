package com.taobao.weex.analyzer.view.alert;

import android.content.Context;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.IPermissionHandler;

public abstract class PermissionAlertView extends AbstractAlertView implements IPermissionHandler {
    private Config mConfig;

    public PermissionAlertView(Context context, Config config) {
        super(context);
        this.mConfig = config;
    }

    public void show() {
        if (this.mConfig == null || isPermissionGranted(this.mConfig)) {
            super.show();
        }
    }
}
