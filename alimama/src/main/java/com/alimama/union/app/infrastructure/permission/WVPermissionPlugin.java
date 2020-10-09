package com.alimama.union.app.infrastructure.permission;

import android.app.Activity;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import com.alimama.moon.App;
import javax.inject.Inject;
import javax.inject.Named;

public class WVPermissionPlugin extends WVApiPlugin {
    @Inject
    @Named("READ_CONTACTS")
    Permission permission;

    public WVPermissionPlugin() {
        App.getAppComponent().inject(this);
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (((str.hashCode() == -372024179 && str.equals("openSettings")) ? (char) 0 : 65535) != 0) {
            return false;
        }
        openPermissionSetting(wVCallBackContext);
        return true;
    }

    private void openPermissionSetting(WVCallBackContext wVCallBackContext) {
        this.permission.openPermissionSetting((Activity) wVCallBackContext.getWebview().getContext());
    }
}
