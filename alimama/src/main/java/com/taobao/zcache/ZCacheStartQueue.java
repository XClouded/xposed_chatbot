package com.taobao.zcache;

import android.app.Application;
import com.taobao.zcache.global.ZCacheGlobal;
import com.taobao.zcache.log.ZLog;
import java.io.Serializable;
import java.util.HashMap;

public class ZCacheStartQueue implements Serializable {
    public void init(Application application, HashMap<String, Object> hashMap) {
        ZLog.i(ZCacheGlobal.TAG, "zcache start queue");
        startUpdateQueue();
    }

    public static void startUpdateQueue() {
        ZCacheCoreProxy.instance().startUpdateQueue();
    }
}
