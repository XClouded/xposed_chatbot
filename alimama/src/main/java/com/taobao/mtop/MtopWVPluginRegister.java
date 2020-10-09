package com.taobao.mtop;

import com.taobao.mtop.statplugin.MtopStatPlugin;
import com.taobao.mtop.wvplugin.MtopWVPlugin;

public class MtopWVPluginRegister {
    public static void register() {
        MtopWVPlugin.register();
        MtopStatPlugin.register();
    }
}
