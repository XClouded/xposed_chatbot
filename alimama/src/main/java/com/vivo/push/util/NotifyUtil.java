package com.vivo.push.util;

import android.content.Context;

public class NotifyUtil {
    private static BaseNotifyDataAdapter sNotifyData = null;
    private static String sNotifyDataAdapter = "com.vivo.push.util.NotifyDataAdapter";
    private static BaseNotifyLayoutAdapter sNotifyLayout = null;
    private static String sNotifyLayoutAdapter = "com.vivo.push.util.NotifyLayoutAdapter";

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0011  */
    /* JADX WARNING: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Object getObjectByReflect(java.lang.String r1, java.lang.Object r2) {
        /*
            r0 = 0
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch:{ Exception -> 0x0006 }
            goto L_0x0007
        L_0x0006:
            r1 = r0
        L_0x0007:
            if (r1 == 0) goto L_0x000e
            java.lang.Object r1 = r1.newInstance()     // Catch:{ Exception -> 0x000e }
            goto L_0x000f
        L_0x000e:
            r1 = r0
        L_0x000f:
            if (r1 != 0) goto L_0x0012
            r1 = r2
        L_0x0012:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.util.NotifyUtil.getObjectByReflect(java.lang.String, java.lang.Object):java.lang.Object");
    }

    private static synchronized void initAdapter(Context context) {
        synchronized (NotifyUtil.class) {
            if (sNotifyData == null) {
                BaseNotifyDataAdapter baseNotifyDataAdapter = (BaseNotifyDataAdapter) getObjectByReflect(sNotifyDataAdapter, new i());
                sNotifyData = baseNotifyDataAdapter;
                baseNotifyDataAdapter.init(context);
            }
            if (sNotifyLayout == null) {
                BaseNotifyLayoutAdapter baseNotifyLayoutAdapter = (BaseNotifyLayoutAdapter) getObjectByReflect(sNotifyLayoutAdapter, new j());
                sNotifyLayout = baseNotifyLayoutAdapter;
                baseNotifyLayoutAdapter.init(context);
            }
        }
    }

    public static BaseNotifyDataAdapter getNotifyDataAdapter(Context context) {
        initAdapter(context);
        return sNotifyData;
    }

    public static BaseNotifyLayoutAdapter getNotifyLayoutAdapter(Context context) {
        initAdapter(context);
        return sNotifyLayout;
    }
}
