package com.taobao.android.zcache.dev;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;

public class ZCacheDevManager {
    public static void init() {
        WVPluginManager.registerPlugin(ZCacheDev.PLUGIN_NAME, (Class<? extends WVApiPlugin>) ZCacheDev.class);
    }
}
