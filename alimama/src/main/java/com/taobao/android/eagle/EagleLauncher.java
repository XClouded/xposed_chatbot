package com.taobao.android.eagle;

import com.taobao.weex.WXEaglePluginManager;

public class EagleLauncher {
    public static final String soName = "WeexEagle";

    public static void initPlugin() {
        WXEaglePluginManager.getInstance().register(new EagleVuePlugin());
    }
}
