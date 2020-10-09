package com.ut.mini.extend;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import com.alibaba.analytics.utils.Logger;
import com.ut.mini.core.WVUserTrack;
import com.ut.mini.module.process.AbsMultiProcessAdapter;
import com.ut.mini.module.process.MultiProcessManager;

public class WindvaneExtend {
    public static void registerWindvane(boolean z) {
        if (!UTExtendSwitch.bWindvaneExtend) {
            Logger.w("UTAnalytics", "user disable WVTBUserTrack ");
        } else if (z) {
            Logger.w("UTAnalytics", "Has registered WVTBUserTrack plugin,not need to register again! ");
        } else {
            Class cls = null;
            try {
                AbsMultiProcessAdapter multiProcessAdapter = MultiProcessManager.getMultiProcessAdapter();
                if (multiProcessAdapter != null) {
                    if (multiProcessAdapter.isUiSubProcess()) {
                        cls = multiProcessAdapter.getSubProcessWVApiPluginClass();
                    } else {
                        cls = WVUserTrack.class;
                    }
                }
                if (cls == null) {
                    cls = WVUserTrack.class;
                }
                WVPluginManager.registerPlugin("WVTBUserTrack", (Class<? extends WVApiPlugin>) cls, true);
                Logger.d("UTAnalytics", "register WVTBUserTrack Success");
            } catch (Throwable th) {
                Logger.e("UTAnalytics", "Exception", th.toString());
            }
        }
    }
}
