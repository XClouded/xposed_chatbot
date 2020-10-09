package com.taobao.zcache;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.taobao.zcache.global.ZCacheGlobal;
import com.taobao.zcache.log.ZLog;
import com.taobao.zcache.util.CommonUtils;
import java.util.HashMap;

public class ZCacheInitializer {
    public void init(Application application, HashMap<String, Object> hashMap) {
    }

    public static void init(Context context, String str, String str2, int i, boolean z) {
        ZLog.i("start init zcache 3.0");
        ZCacheGlobal.instance().setAppKey(str);
        ZCacheGlobal.instance().setAppVersion(str2);
        ZCacheGlobal.instance().setContext(context);
        ZCacheGlobal.instance().setEnv(i);
        if (CommonUtils.isMainProcess(context)) {
            ZCacheCoreProxy.instance().setupWithHTTP(str, str2, z);
            ZCacheCoreProxy.instance().setEnv(i);
            ZLog.init(context);
        }
    }

    public static void initConfig() {
        ZLog.i("start update config");
        ZCacheCoreProxy.instance().initConfig();
    }

    public static void startUpdateQueue() {
        ZLog.i("start update queue");
        ZCacheCoreProxy.instance().startUpdateQueue();
    }

    public static void initSubProcess() {
        ZLog.i("init zcache subproces; start service");
        Intent intent = new Intent();
        intent.setClass(ZCacheGlobal.instance().context(), ZCacheServer.class);
        ZCacheGlobal.instance().context().bindService(intent, ZCacheManager.instance().zcacheProxy, 1);
        ZCacheCoreProxy.instance().setupSubProcess();
    }
}
