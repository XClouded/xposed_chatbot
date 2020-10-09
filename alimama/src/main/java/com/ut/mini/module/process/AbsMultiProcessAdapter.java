package com.ut.mini.module.process;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import com.ut.mini.UTTracker;

public abstract class AbsMultiProcessAdapter {
    public abstract Class<? extends UTTracker> getSubProcessUTTrackerClass();

    public abstract Class<? extends WVApiPlugin> getSubProcessWVApiPluginClass();

    public abstract void initProxy();

    public abstract boolean isUiSubProcess();

    public abstract void registerActivityLifecycleCallbacks();
}
