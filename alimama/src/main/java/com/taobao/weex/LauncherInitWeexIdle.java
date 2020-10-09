package com.taobao.weex;

import android.app.Application;
import com.taobao.weex.utils.cache.RegisterCache;
import java.util.HashMap;

public class LauncherInitWeexIdle {
    public void init(Application application, HashMap<String, Object> hashMap) {
        RegisterCache.getInstance().idle(false);
    }
}
