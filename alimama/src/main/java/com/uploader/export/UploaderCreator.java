package com.uploader.export;

import java.util.concurrent.ConcurrentHashMap;

public class UploaderCreator {
    private static final String TAG = "<aus> UploaderCreator";
    private static final ConcurrentHashMap<Integer, IUploaderManager> managerMap = new ConcurrentHashMap<>();

    public static IUploaderManager get() throws RuntimeException {
        return get(0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0066, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.uploader.export.IUploaderManager get(int r6) {
        /*
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.uploader.export.IUploaderManager> r0 = managerMap
            java.lang.Integer r1 = java.lang.Integer.valueOf(r6)
            java.lang.Object r0 = r0.get(r1)
            com.uploader.export.IUploaderManager r0 = (com.uploader.export.IUploaderManager) r0
            if (r0 == 0) goto L_0x000f
            return r0
        L_0x000f:
            java.lang.Class<com.uploader.export.UploaderCreator> r0 = com.uploader.export.UploaderCreator.class
            monitor-enter(r0)
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.uploader.export.IUploaderManager> r1 = managerMap     // Catch:{ all -> 0x0083 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r6)     // Catch:{ all -> 0x0083 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x0083 }
            com.uploader.export.IUploaderManager r1 = (com.uploader.export.IUploaderManager) r1     // Catch:{ all -> 0x0083 }
            if (r1 == 0) goto L_0x0022
            monitor-exit(r0)     // Catch:{ all -> 0x0083 }
            return r1
        L_0x0022:
            java.lang.String r1 = "com.uploader.implement.UploaderManager"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch:{ Throwable -> 0x0067 }
            r2 = 1
            java.lang.Class[] r3 = new java.lang.Class[r2]     // Catch:{ Throwable -> 0x0067 }
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch:{ Throwable -> 0x0067 }
            r5 = 0
            r3[r5] = r4     // Catch:{ Throwable -> 0x0067 }
            java.lang.reflect.Constructor r1 = r1.getDeclaredConstructor(r3)     // Catch:{ Throwable -> 0x0067 }
            r1.setAccessible(r2)     // Catch:{ Throwable -> 0x0067 }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0067 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0067 }
            r2[r5] = r3     // Catch:{ Throwable -> 0x0067 }
            java.lang.Object r1 = r1.newInstance(r2)     // Catch:{ Throwable -> 0x0067 }
            com.uploader.export.IUploaderManager r1 = (com.uploader.export.IUploaderManager) r1     // Catch:{ Throwable -> 0x0067 }
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, com.uploader.export.IUploaderManager> r2 = managerMap     // Catch:{ all -> 0x0083 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r6)     // Catch:{ all -> 0x0083 }
            r2.put(r3, r1)     // Catch:{ all -> 0x0083 }
            boolean r2 = r1.isInitialized()     // Catch:{ all -> 0x0083 }
            if (r2 != 0) goto L_0x0065
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ all -> 0x0083 }
            com.uploader.export.IUploaderDependency r6 = com.uploader.export.UploaderGlobal.getDependency(r6)     // Catch:{ all -> 0x0083 }
            if (r6 == 0) goto L_0x0065
            android.content.Context r2 = com.uploader.export.UploaderGlobal.retrieveContext()     // Catch:{ all -> 0x0083 }
            r1.initialize(r2, r6)     // Catch:{ all -> 0x0083 }
        L_0x0065:
            monitor-exit(r0)     // Catch:{ all -> 0x0083 }
            return r1
        L_0x0067:
            r6 = move-exception
            java.lang.RuntimeException r1 = new java.lang.RuntimeException     // Catch:{ all -> 0x0083 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0083 }
            r2.<init>()     // Catch:{ all -> 0x0083 }
            java.lang.String r3 = "<aus> UploaderCreator "
            r2.append(r3)     // Catch:{ all -> 0x0083 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0083 }
            r2.append(r6)     // Catch:{ all -> 0x0083 }
            java.lang.String r6 = r2.toString()     // Catch:{ all -> 0x0083 }
            r1.<init>(r6)     // Catch:{ all -> 0x0083 }
            throw r1     // Catch:{ all -> 0x0083 }
        L_0x0083:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0083 }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.export.UploaderCreator.get(int):com.uploader.export.IUploaderManager");
    }
}
