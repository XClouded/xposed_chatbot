package com.ta.audid.utils;

public class UmidUtils {
    private static String mUmidToken = "";

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0072, code lost:
        return r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String getUmidToken(android.content.Context r10) {
        /*
            java.lang.Class<com.ta.audid.utils.UmidUtils> r0 = com.ta.audid.utils.UmidUtils.class
            monitor-enter(r0)
            java.lang.String r1 = mUmidToken     // Catch:{ all -> 0x0081 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0081 }
            if (r1 != 0) goto L_0x000f
            java.lang.String r10 = mUmidToken     // Catch:{ all -> 0x0081 }
            monitor-exit(r0)
            return r10
        L_0x000f:
            r1 = 1
            r2 = 0
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0073 }
            com.alibaba.wireless.security.open.SecurityGuardManager r10 = com.alibaba.wireless.security.open.SecurityGuardManager.getInstance(r10)     // Catch:{ Throwable -> 0x0073 }
            com.alibaba.wireless.security.open.umid.IUMIDComponent r10 = r10.getUMIDComp()     // Catch:{ Throwable -> 0x0073 }
            int r5 = r10.initUMIDSync(r2)     // Catch:{ Throwable -> 0x0073 }
            r6 = 200(0xc8, float:2.8E-43)
            if (r5 == r6) goto L_0x0045
            java.util.HashMap r6 = new java.util.HashMap     // Catch:{ Throwable -> 0x0073 }
            r6.<init>()     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r7 = "errorCode"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0073 }
            r8.<init>()     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r9 = ""
            r8.append(r9)     // Catch:{ Throwable -> 0x0073 }
            r8.append(r5)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r5 = r8.toString()     // Catch:{ Throwable -> 0x0073 }
            r6.put(r7, r5)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r5 = "umid"
            com.ta.audid.utils.UtUtils.sendUtdidMonitorEvent(r5, r6)     // Catch:{ Throwable -> 0x0073 }
        L_0x0045:
            java.lang.String r5 = ""
            java.lang.Object[] r6 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0073 }
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0073 }
            r9 = 0
            long r7 = r7 - r3
            java.lang.Long r3 = java.lang.Long.valueOf(r7)     // Catch:{ Throwable -> 0x0073 }
            r6[r2] = r3     // Catch:{ Throwable -> 0x0073 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r10 = r10.getSecurityToken(r2)     // Catch:{ Throwable -> 0x0073 }
            boolean r3 = android.text.TextUtils.isEmpty(r10)     // Catch:{ Throwable -> 0x0073 }
            if (r3 != 0) goto L_0x0071
            int r3 = r10.length()     // Catch:{ Throwable -> 0x0073 }
            r4 = 24
            if (r3 != r4) goto L_0x006b
            goto L_0x0071
        L_0x006b:
            mUmidToken = r10     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r10 = mUmidToken     // Catch:{ Throwable -> 0x0073 }
            monitor-exit(r0)
            return r10
        L_0x0071:
            monitor-exit(r0)
            return r10
        L_0x0073:
            r10 = move-exception
            java.lang.String r3 = ""
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0081 }
            r1[r2] = r10     // Catch:{ all -> 0x0081 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r3, (java.lang.Object[]) r1)     // Catch:{ all -> 0x0081 }
            java.lang.String r10 = ""
            monitor-exit(r0)
            return r10
        L_0x0081:
            r10 = move-exception
            monitor-exit(r0)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.utils.UmidUtils.getUmidToken(android.content.Context):java.lang.String");
    }
}
