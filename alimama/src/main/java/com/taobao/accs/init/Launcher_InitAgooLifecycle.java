package com.taobao.accs.init;

import android.app.Application;
import com.taobao.accs.utl.ALog;
import java.io.Serializable;
import java.util.HashMap;

public class Launcher_InitAgooLifecycle implements Serializable {
    private static final String TAG = "Launcher_InitAgooLifecycle";

    public void init(Application application, HashMap<String, Object> hashMap) {
        ALog.i(TAG, "init", new Object[0]);
    }
}
