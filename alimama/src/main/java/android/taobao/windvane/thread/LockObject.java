package android.taobao.windvane.thread;

public class LockObject {
    private boolean needwait = true;
    public int result = 0;

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:1:0x0001 */
    /* JADX WARNING: Removed duplicated region for block: B:1:0x0001 A[LOOP:0: B:1:0x0001->B:11:0x0001, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void lwait() {
        /*
            r1 = this;
            monitor-enter(r1)
        L_0x0001:
            boolean r0 = r1.needwait     // Catch:{ all -> 0x000b }
            if (r0 == 0) goto L_0x0009
            r1.wait()     // Catch:{ InterruptedException -> 0x0001 }
            goto L_0x0001
        L_0x0009:
            monitor-exit(r1)
            return
        L_0x000b:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.thread.LockObject.lwait():void");
    }

    public synchronized void lnotify() {
        if (this.needwait) {
            this.needwait = false;
            notify();
        }
    }
}
