package com.taobao.android;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import java.util.HashMap;
import java.util.Map;

public class AliMonitorBalancedPool implements AliMonitorIPool {
    private static AliMonitorBalancedPool instance = new AliMonitorBalancedPool();
    private Map<Class<? extends AliMonitorReusable>, AliMonitorReuseItemPool<? extends AliMonitorReusable>> reuseItemPools = new HashMap();

    public static AliMonitorBalancedPool getInstance() {
        return instance;
    }

    private AliMonitorBalancedPool() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0014  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T extends com.taobao.android.AliMonitorReusable> T poll(java.lang.Class<T> r2, java.lang.Object... r3) {
        /*
            r1 = this;
            com.taobao.android.AliMonitorReuseItemPool r0 = r1.getPool(r2)
            com.taobao.android.AliMonitorReusable r0 = r0.poll()
            if (r0 != 0) goto L_0x0011
            java.lang.Object r2 = r2.newInstance()     // Catch:{ Exception -> 0x0011 }
            com.taobao.android.AliMonitorReusable r2 = (com.taobao.android.AliMonitorReusable) r2     // Catch:{ Exception -> 0x0011 }
            goto L_0x0012
        L_0x0011:
            r2 = r0
        L_0x0012:
            if (r2 == 0) goto L_0x0017
            r2.fill(r3)
        L_0x0017:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.AliMonitorBalancedPool.poll(java.lang.Class, java.lang.Object[]):com.taobao.android.AliMonitorReusable");
    }

    public <T extends AliMonitorReusable> void offer(T t) {
        if (t != null) {
            getPool(t.getClass()).offer(t);
        }
    }

    private synchronized <T extends AliMonitorReusable> AliMonitorReuseItemPool<T> getPool(Class<T> cls) {
        AliMonitorReuseItemPool<T> aliMonitorReuseItemPool;
        aliMonitorReuseItemPool = this.reuseItemPools.get(cls);
        if (aliMonitorReuseItemPool == null) {
            aliMonitorReuseItemPool = new AliMonitorReuseItemPool<>();
            this.reuseItemPools.put(cls, aliMonitorReuseItemPool);
        }
        return aliMonitorReuseItemPool;
    }

    protected static long getMaxMemAllocatedSize(Context context) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        return Math.min(maxMemory, activityManager != null ? (long) (activityManager.getMemoryClass() * 1048576) : 0) < 67108864 ? PlaybackStateCompat.ACTION_PREPARE_FROM_URI : PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
    }
}
