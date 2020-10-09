package com.taobao.android.dinamic.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class DinamicLogThread extends HandlerThread {
    private static HandlerThread handlerThread = null;
    private static boolean isInited = false;
    public static OrderedHandler threadHandler;

    private DinamicLogThread(String str) {
        super(str);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(9:5|6|7|8|9|10|11|12|13) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x001b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0014 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void init(java.lang.String r2) {
        /*
            java.lang.Class<com.taobao.android.dinamic.log.DinamicLogThread> r0 = com.taobao.android.dinamic.log.DinamicLogThread.class
            monitor-enter(r0)
            boolean r1 = isInited     // Catch:{ all -> 0x0027 }
            if (r1 != 0) goto L_0x0025
            com.taobao.android.dinamic.log.DinamicLogThread r1 = new com.taobao.android.dinamic.log.DinamicLogThread     // Catch:{ all -> 0x0027 }
            r1.<init>(r2)     // Catch:{ all -> 0x0027 }
            handlerThread = r1     // Catch:{ all -> 0x0027 }
            r2 = 0
            android.os.HandlerThread r1 = handlerThread     // Catch:{ Throwable -> 0x0014 }
            r1.start()     // Catch:{ Throwable -> 0x0014 }
        L_0x0014:
            android.os.HandlerThread r1 = handlerThread     // Catch:{ Throwable -> 0x001b }
            android.os.Looper r1 = r1.getLooper()     // Catch:{ Throwable -> 0x001b }
            r2 = r1
        L_0x001b:
            com.taobao.android.dinamic.log.DinamicLogThread$OrderedHandler r1 = new com.taobao.android.dinamic.log.DinamicLogThread$OrderedHandler     // Catch:{ all -> 0x0027 }
            r1.<init>(r2)     // Catch:{ all -> 0x0027 }
            threadHandler = r1     // Catch:{ all -> 0x0027 }
            r2 = 1
            isInited = r2     // Catch:{ all -> 0x0027 }
        L_0x0025:
            monitor-exit(r0)
            return
        L_0x0027:
            r2 = move-exception
            monitor-exit(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.log.DinamicLogThread.init(java.lang.String):void");
    }

    public static boolean checkInit() {
        return isInited;
    }

    public static class OrderedHandler extends Handler {
        public OrderedHandler(Looper looper) {
            super(looper);
        }

        public void postTask(Runnable runnable) {
            if (runnable != null) {
                try {
                    Message obtain = Message.obtain();
                    obtain.what = 1;
                    obtain.obj = runnable;
                    sendMessage(obtain);
                } catch (Throwable unused) {
                }
            }
        }

        public void handleMessage(Message message) {
            try {
                if (message.obj != null && (message.obj instanceof Runnable)) {
                    ((Runnable) message.obj).run();
                }
            } catch (Throwable unused) {
            }
            super.handleMessage(message);
        }
    }
}
