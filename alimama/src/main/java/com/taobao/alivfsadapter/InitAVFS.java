package com.taobao.alivfsadapter;

import android.app.Application;
import java.io.Serializable;
import java.util.HashMap;

public class InitAVFS implements Serializable {
    public void init(Application application, HashMap<String, Object> hashMap) {
        AVFSAdapterManager.getInstance().ensureInitialized(application);
    }
}
