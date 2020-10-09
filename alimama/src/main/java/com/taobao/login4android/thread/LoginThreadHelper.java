package com.taobao.login4android.thread;

import android.os.Handler;
import android.os.Looper;
import com.taobao.login4android.log.LoginTLogAdapter;

public class LoginThreadHelper {
    public static final String TAG = "login.LoginThreadHelper";
    private static Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    public static void runOnUIThread(Runnable runnable) {
        try {
            if (!isMainThread()) {
                mMainThreadHandler.post(runnable);
            } else {
                runnable.run();
            }
        } catch (Exception e) {
            LoginTLogAdapter.w(TAG, "runOnUIThread failed", e);
            e.printStackTrace();
        }
    }

    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0025 */
    /* JADX WARNING: Removed duplicated region for block: B:20:? A[Catch:{ Exception -> 0x0069 }, ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0033 A[Catch:{ Exception -> 0x0069 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCurProcessName(android.content.Context r6) {
        /*
            java.lang.String r0 = "com.taobao.tao.TaobaoApplication"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Throwable -> 0x0025 }
            java.lang.String r1 = "getProcessName"
            r2 = 1
            java.lang.Class[] r3 = new java.lang.Class[r2]     // Catch:{ Throwable -> 0x0025 }
            java.lang.Class<android.content.Context> r4 = android.content.Context.class
            r5 = 0
            r3[r5] = r4     // Catch:{ Throwable -> 0x0025 }
            java.lang.reflect.Method r1 = r0.getDeclaredMethod(r1, r3)     // Catch:{ Throwable -> 0x0025 }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0025 }
            r2[r5] = r6     // Catch:{ Throwable -> 0x0025 }
            java.lang.Object r0 = com.taobao.login4android.utils.ReflectionHelper.invokeMethod(r0, r1, r2)     // Catch:{ Throwable -> 0x0025 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x0025 }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0025 }
            if (r1 != 0) goto L_0x0025
            return r0
        L_0x0025:
            int r0 = android.os.Process.myPid()     // Catch:{ Exception -> 0x0069 }
            java.lang.String r1 = "activity"
            java.lang.Object r6 = r6.getSystemService(r1)     // Catch:{ Exception -> 0x0069 }
            android.app.ActivityManager r6 = (android.app.ActivityManager) r6     // Catch:{ Exception -> 0x0069 }
            if (r6 == 0) goto L_0x0066
            java.util.List r6 = r6.getRunningAppProcesses()     // Catch:{ Exception -> 0x0069 }
            java.util.Iterator r6 = r6.iterator()     // Catch:{ Exception -> 0x0069 }
        L_0x003b:
            boolean r1 = r6.hasNext()     // Catch:{ Exception -> 0x0069 }
            if (r1 == 0) goto L_0x0066
            java.lang.Object r1 = r6.next()     // Catch:{ Exception -> 0x0069 }
            android.app.ActivityManager$RunningAppProcessInfo r1 = (android.app.ActivityManager.RunningAppProcessInfo) r1     // Catch:{ Exception -> 0x0069 }
            int r2 = r1.pid     // Catch:{ Exception -> 0x0069 }
            if (r2 != r0) goto L_0x003b
            java.lang.String r6 = "login.LoginThreadHelper"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0069 }
            r0.<init>()     // Catch:{ Exception -> 0x0069 }
            java.lang.String r2 = "login get process = "
            r0.append(r2)     // Catch:{ Exception -> 0x0069 }
            java.lang.String r2 = r1.processName     // Catch:{ Exception -> 0x0069 }
            r0.append(r2)     // Catch:{ Exception -> 0x0069 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0069 }
            android.util.Log.e(r6, r0)     // Catch:{ Exception -> 0x0069 }
            java.lang.String r6 = r1.processName     // Catch:{ Exception -> 0x0069 }
            return r6
        L_0x0066:
            java.lang.String r6 = "ProcessNameNotFound"
            return r6
        L_0x0069:
            r6 = move-exception
            r6.printStackTrace()
            java.lang.String r6 = "GetCurProcessException"
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.thread.LoginThreadHelper.getCurProcessName(android.content.Context):java.lang.String");
    }
}
