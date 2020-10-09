package com.taobao.monitor.adapter;

import android.app.Application;
import com.taobao.monitor.common.ThreadUtils;
import java.io.Serializable;
import java.util.HashMap;

public class TMAPMAdapterLauncher implements Serializable {
    public void init(final Application application, final HashMap<String, Object> hashMap) {
        new TMAPMInitiator().init(application, hashMap);
        ThreadUtils.start(new Runnable() {
            public void run() {
                new TBAPMAdapterLauncherPart2().init(application, hashMap);
            }
        });
    }
}
