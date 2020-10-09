package com.ali.ha.fulltrace;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import com.ali.ha.fulltrace.dump.DumpManager;
import com.ali.ha.fulltrace.logger.Logger;
import com.ali.ha.fulltrace.upload.UploadManager;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.weex.devtools.debug.WXDebugConstants;
import java.util.HashMap;

public class FulltraceLauncher {
    public static void init(final Application application, HashMap<String, String> hashMap) {
        FTHeader.appVersion = hashMap.get("appVersion");
        FTHeader.appBuild = hashMap.get(Constants.KEY_APP_BUILD);
        FTHeader.appId = hashMap.get("appId");
        FTHeader.appKey = hashMap.get("appKey");
        FTHeader.channel = hashMap.get("channel");
        FTHeader.utdid = hashMap.get("utdid");
        FTHeader.userId = hashMap.get("userId");
        FTHeader.userNick = hashMap.get("userNick");
        FTHeader.ttid = hashMap.get("ttid");
        FTHeader.apmVersion = hashMap.get("apmVersion");
        FTHeader.brand = hashMap.get("brand");
        FTHeader.deviceModel = hashMap.get(WXDebugConstants.ENV_DEVICE_MODEL);
        FTHeader.clientIp = hashMap.get("clientIp");
        FTHeader.os = hashMap.get("os");
        FTHeader.osVersion = hashMap.get(WXDebugConstants.ENV_OS_VERSION);
        FTHeader.processName = hashMap.get("processName");
        if (TextUtils.isEmpty(FTHeader.processName)) {
            Log.e("Fulltrace", "fulltrace invalid because processName is null");
        } else {
            FulltraceGlobal.instance().dumpHandler().post(new Runnable() {
                public void run() {
                    HashMap hashMap = new HashMap();
                    hashMap.put("appVersion", FTHeader.appVersion);
                    hashMap.put(Constants.KEY_APP_BUILD, FTHeader.appBuild);
                    hashMap.put("appId", FTHeader.appId);
                    hashMap.put("appKey", FTHeader.appKey);
                    hashMap.put("channel", FTHeader.channel);
                    hashMap.put("utdid", FTHeader.utdid);
                    hashMap.put("userId", FTHeader.userId);
                    hashMap.put("userNick", FTHeader.userNick);
                    hashMap.put("ttid", FTHeader.ttid);
                    hashMap.put("apmVersion", FTHeader.apmVersion);
                    hashMap.put("session", FTHeader.session);
                    hashMap.put("processName", FTHeader.processName);
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("brand", FTHeader.brand);
                    hashMap2.put(WXDebugConstants.ENV_DEVICE_MODEL, FTHeader.deviceModel);
                    hashMap2.put("clientIp", FTHeader.clientIp);
                    hashMap2.put("os", FTHeader.os);
                    hashMap2.put(WXDebugConstants.ENV_OS_VERSION, FTHeader.osVersion);
                    Logger.setDebug(false);
                    DumpManager.getInstance().initTraceLog(application, hashMap, hashMap2);
                    UploadManager.getInstance().init(application);
                }
            });
        }
    }
}
