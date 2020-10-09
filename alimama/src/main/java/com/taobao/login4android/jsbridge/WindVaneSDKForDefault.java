package com.taobao.login4android.jsbridge;

import android.content.Context;
import android.taobao.windvane.WindVaneSDK;
import android.taobao.windvane.config.WVAppParams;
import android.taobao.windvane.debug.WVDebug;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jsbridge.api.WVAPI;
import android.taobao.windvane.monitor.WVMonitor;
import android.taobao.windvane.packageapp.WVPackageAppConfig;
import android.taobao.windvane.packageapp.WVPackageAppManager;
import android.taobao.windvane.packageapp.WVPackageAppService;

public class WindVaneSDKForDefault {
    public static void init(Context context, WVAppParams wVAppParams) {
        try {
            WindVaneSDK.init(context, wVAppParams);
            WVPackageAppService.registerWvPackageAppConfig(new WVPackageAppConfig());
            WVPackageAppManager.getInstance().init(context, true);
            WVJsBridge.getInstance().init();
            WVAPI.setup();
            WVDebug.init();
            WVMonitor.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
