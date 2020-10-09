package com.taobao.tao.log.godeye.memorydump.dump;

public class MemoryDump {

    public interface MemoryDumpCallBack {
        void dumpError();

        void dumpSuccess(String str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        com.taobao.tao.log.godeye.memorydump.dump.MemoryFileZip.deleteFile(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
        if (r2 != null) goto L_0x001a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001a, code lost:
        r2.dumpError();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001e, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0015 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void dumpHprof(java.lang.String r1, com.taobao.tao.log.godeye.memorydump.dump.MemoryDump.MemoryDumpCallBack r2) {
        /*
            java.lang.Class<com.taobao.tao.log.godeye.memorydump.dump.MemoryDump> r0 = com.taobao.tao.log.godeye.memorydump.dump.MemoryDump.class
            monitor-enter(r0)
            if (r1 != 0) goto L_0x0007
            monitor-exit(r0)
            return
        L_0x0007:
            com.taobao.tao.log.godeye.memorydump.dump.MemoryFileZip.deleteFile(r1)     // Catch:{ Throwable -> 0x0015 }
            android.os.Debug.dumpHprofData(r1)     // Catch:{ Throwable -> 0x0015 }
            if (r2 == 0) goto L_0x001d
            r2.dumpSuccess(r1)     // Catch:{ Throwable -> 0x0015 }
            goto L_0x001d
        L_0x0013:
            r1 = move-exception
            goto L_0x001f
        L_0x0015:
            com.taobao.tao.log.godeye.memorydump.dump.MemoryFileZip.deleteFile(r1)     // Catch:{ all -> 0x0013 }
            if (r2 == 0) goto L_0x001d
            r2.dumpError()     // Catch:{ all -> 0x0013 }
        L_0x001d:
            monitor-exit(r0)
            return
        L_0x001f:
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.log.godeye.memorydump.dump.MemoryDump.dumpHprof(java.lang.String, com.taobao.tao.log.godeye.memorydump.dump.MemoryDump$MemoryDumpCallBack):void");
    }
}
