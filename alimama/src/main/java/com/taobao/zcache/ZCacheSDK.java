package com.taobao.zcache;

import com.taobao.zcache.global.ZCacheGlobal;
import com.taobao.zcache.log.ZLog;
import com.taobao.zcache.util.CommonUtils;

public class ZCacheSDK {
    public static void initExtra() {
    }

    public static void init(ZCacheParams zCacheParams) {
        if (zCacheParams.context == null) {
            ZLog.i("zcache init failed, context cannot be null");
        } else if (!CommonUtils.isMainProcess(zCacheParams.context)) {
            ZCacheGlobal.instance().setContext(zCacheParams.context);
            ZCacheInitializer.initSubProcess();
        } else {
            ZCacheInitializer.init(zCacheParams.context, zCacheParams.appKey, zCacheParams.appVersion, zCacheParams.env, zCacheParams.useOldPlatform);
            ZCacheManager.instance().setUseNewUnzip(false);
            ZCacheInitializer.initConfig();
            ZCacheInitializer.startUpdateQueue();
        }
    }

    public static void initSub() {
        ZCacheInitializer.initSubProcess();
    }
}
