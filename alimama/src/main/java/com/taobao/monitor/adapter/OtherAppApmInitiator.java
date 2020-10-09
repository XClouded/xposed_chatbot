package com.taobao.monitor.adapter;

import alimama.com.unwetaologger.base.UNWLogger;
import android.app.Application;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.orange.OConstant;
import java.util.HashMap;

public class OtherAppApmInitiator extends AbsAPMInitiator {
    /* access modifiers changed from: protected */
    public void initPage() {
    }

    public /* bridge */ /* synthetic */ void init(Application application, HashMap hashMap) {
        super.init(application, hashMap);
    }

    /* access modifiers changed from: package-private */
    public void yourFuncation(Application application) {
        HashMap hashMap = new HashMap();
        hashMap.put("deviceId", "xxxxx");
        hashMap.put(OConstant.LAUNCH_ONLINEAPPKEY, "xxxxx");
        hashMap.put("appVersion", "x.x.x");
        hashMap.put(Constants.KEY_APP_BUILD, "x.x.x");
        hashMap.put(UNWLogger.LOG_VALUE_TYPE_PROCESS, "com.xxx.xxx");
        hashMap.put("ttid", "xxxxx");
        hashMap.put("channel", "xxxxx");
        hashMap.put("appPatch", "xxxxx");
        new OtherAppApmInitiator().init(application, hashMap);
    }
}
