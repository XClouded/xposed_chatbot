package com.taobao.tao.log.godeye;

import android.app.Application;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.android.tlog.protocol.model.GodeyeInfo;
import com.taobao.tao.log.godeye.core.GodEyeAppListener;
import com.taobao.tao.log.godeye.core.GodEyeReponse;
import com.taobao.tao.log.godeye.core.control.Godeye;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class GodeyeInitializer {
    public GodeyeConfig config;
    AtomicBoolean enabling;

    private GodeyeInitializer() {
        this.enabling = new AtomicBoolean(false);
        this.config = null;
    }

    private static class CreateInstance {
        /* access modifiers changed from: private */
        public static GodeyeInitializer instance = new GodeyeInitializer();

        private CreateInstance() {
        }
    }

    public static synchronized GodeyeInitializer getInstance() {
        GodeyeInitializer access$100;
        synchronized (GodeyeInitializer.class) {
            access$100 = CreateInstance.instance;
        }
        return access$100;
    }

    public void init(Application application, GodeyeConfig godeyeConfig) {
        if (this.enabling.compareAndSet(false, true)) {
            if (godeyeConfig == null) {
                godeyeConfig = new GodeyeConfig();
            }
            this.config = godeyeConfig;
            String str = this.config.appVersion;
            String str2 = this.config.packageTag;
            String str3 = this.config.appId;
            Godeye.sharedInstance().utdid = this.config.utdid;
            Godeye.sharedInstance().initialize(application, str3, str);
            Godeye.sharedInstance().setBuildId(str2);
        }
    }

    public void registGodEyeReponse(String str, GodEyeReponse godEyeReponse) {
        if (str != null && godEyeReponse != null) {
            Godeye.sharedInstance().godEyeReponses.put(str, godEyeReponse);
        }
    }

    public boolean handleRemoteCommand(GodeyeInfo godeyeInfo) {
        return Godeye.sharedInstance().handleRemoteCommand(godeyeInfo);
    }

    public void registGodEyeAppListener(GodEyeAppListener godEyeAppListener) {
        if (godEyeAppListener != null) {
            Godeye.sharedInstance().godEyeAppListener = godEyeAppListener;
        }
    }

    public void onAccurateBootFinished(HashMap<String, String> hashMap) {
        Godeye.sharedInstance().defaultGodeyeJointPointCenter().invokeCustomEventJointPointHandlersIfExist(Constants.AndroidJointPointKey.EVENT_KEY_APP_STARTED);
    }
}
