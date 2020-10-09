package com.alibaba.motu.tbrest;

import android.app.Application;
import com.alibaba.motu.tbrest.rest.RestReqSend;
import com.taobao.application.common.ApmManager;
import com.taobao.application.common.IApmEventListener;
import java.io.Serializable;
import java.util.HashMap;

public class ApmRestLauncher implements Serializable {
    private static volatile boolean init = false;

    public void init(Application application, HashMap<String, Object> hashMap) {
        if (!init) {
            init = true;
            ApmManager.addApmEventListener(new ApmListener());
        }
    }

    private static class ApmListener implements IApmEventListener {
        private ApmListener() {
        }

        public void onEvent(int i) {
            if (i == 1) {
                RestReqSend.sendAllCacheData();
            }
        }
    }
}
