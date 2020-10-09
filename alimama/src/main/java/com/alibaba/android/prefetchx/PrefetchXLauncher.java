package com.alibaba.android.prefetchx;

import android.app.Application;
import java.io.Serializable;
import java.util.HashMap;

public class PrefetchXLauncher implements Serializable {
    private static final long serialVersionUID = -8810965795436093671L;

    public void init(Application application, HashMap<String, Object> hashMap) {
        try {
            PrefetchX.getInstance().init(application);
            PrefetchX.getInstance().prepare();
        } catch (Throwable th) {
            PFLog.w("PrefetchX", "error in PrefetchX init.", th);
        }
    }
}
