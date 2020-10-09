package com.ut.mini.crashhandler;

import android.content.Context;
import java.lang.Thread;

public class UTMiniCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "UTCrashHandler";
    private static volatile boolean mCrashing = false;
    private static UTMiniCrashHandler s_instance = new UTMiniCrashHandler();
    private Context mContext = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler = null;
    private boolean mIsTurnOff = true;
    private IUTCrashCaughtListner mListener = null;

    private UTMiniCrashHandler() {
    }

    public static UTMiniCrashHandler getInstance() {
        return s_instance;
    }

    public boolean isTurnOff() {
        return this.mIsTurnOff;
    }

    public void turnOff() {
        if (this.mDefaultHandler != null) {
            Thread.setDefaultUncaughtExceptionHandler(this.mDefaultHandler);
            this.mDefaultHandler = null;
        }
        this.mIsTurnOff = true;
    }

    public void turnOn(Context context) {
        _initialize();
    }

    public void setCrashCaughtListener(IUTCrashCaughtListner iUTCrashCaughtListner) {
        this.mListener = iUTCrashCaughtListner;
    }

    private void _initialize() {
        if (this.mIsTurnOff) {
            this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
            this.mIsTurnOff = false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a2, code lost:
        if (r11.mDefaultHandler != null) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00b5, code lost:
        android.os.Process.killProcess(android.os.Process.myPid());
        java.lang.System.exit(10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void uncaughtException(java.lang.Thread r12, java.lang.Throwable r13) {
        /*
            r11 = this;
            r0 = 10
            boolean r1 = mCrashing     // Catch:{ Throwable -> 0x00a7 }
            if (r1 == 0) goto L_0x001b
            java.lang.Thread$UncaughtExceptionHandler r1 = r11.mDefaultHandler
            if (r1 == 0) goto L_0x0010
            java.lang.Thread$UncaughtExceptionHandler r0 = r11.mDefaultHandler
            r0.uncaughtException(r12, r13)
            goto L_0x001a
        L_0x0010:
            int r12 = android.os.Process.myPid()
            android.os.Process.killProcess(r12)
            java.lang.System.exit(r0)
        L_0x001a:
            return
        L_0x001b:
            r1 = 1
            mCrashing = r1     // Catch:{ Throwable -> 0x00a7 }
            r2 = 0
            if (r13 == 0) goto L_0x002b
            java.lang.String r3 = "Caught Exception By UTCrashHandler.Please see log as follows!"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00a7 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r3, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x00a7 }
            r13.printStackTrace()     // Catch:{ Throwable -> 0x00a7 }
        L_0x002b:
            com.ut.mini.crashhandler.UTExceptionParser$UTExceptionItem r3 = com.ut.mini.crashhandler.UTExceptionParser.parse(r13)     // Catch:{ Throwable -> 0x00a7 }
            if (r3 == 0) goto L_0x00a0
            java.lang.String r4 = r3.mCrashDetail     // Catch:{ Throwable -> 0x00a7 }
            if (r4 == 0) goto L_0x00a0
            java.lang.String r4 = r3.getExpName()     // Catch:{ Throwable -> 0x00a7 }
            if (r4 == 0) goto L_0x00a0
            java.lang.String r4 = r3.getMd5()     // Catch:{ Throwable -> 0x00a7 }
            if (r4 == 0) goto L_0x00a0
            r4 = 0
            com.ut.mini.crashhandler.IUTCrashCaughtListner r5 = r11.mListener     // Catch:{ Throwable -> 0x00a7 }
            if (r5 == 0) goto L_0x0052
            com.ut.mini.crashhandler.IUTCrashCaughtListner r5 = r11.mListener     // Catch:{ Throwable -> 0x004e }
            java.util.Map r5 = r5.onCrashCaught(r12, r13)     // Catch:{ Throwable -> 0x004e }
            r4 = r5
            goto L_0x0052
        L_0x004e:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ Throwable -> 0x00a7 }
        L_0x0052:
            if (r4 != 0) goto L_0x0059
            java.util.HashMap r4 = new java.util.HashMap     // Catch:{ Throwable -> 0x00a7 }
            r4.<init>()     // Catch:{ Throwable -> 0x00a7 }
        L_0x0059:
            r9 = r4
            java.lang.String r4 = "StackTrace"
            java.lang.String r5 = r3.getCrashDetail()     // Catch:{ Throwable -> 0x00a7 }
            r9.put(r4, r5)     // Catch:{ Throwable -> 0x00a7 }
            com.ut.mini.internal.UTOriginalCustomHitBuilder r10 = new com.ut.mini.internal.UTOriginalCustomHitBuilder     // Catch:{ Throwable -> 0x00a7 }
            java.lang.String r4 = "UT"
            r5 = 1
            java.lang.String r6 = r3.getMd5()     // Catch:{ Throwable -> 0x00a7 }
            java.lang.String r7 = r3.getExpName()     // Catch:{ Throwable -> 0x00a7 }
            r8 = 0
            r3 = r10
            r3.<init>(r4, r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x00a7 }
            java.lang.String r3 = "_priority"
            java.lang.String r4 = "5"
            r10.setProperty(r3, r4)     // Catch:{ Throwable -> 0x00a7 }
            java.lang.String r3 = "_sls"
            java.lang.String r4 = "yes"
            r10.setProperty(r3, r4)     // Catch:{ Throwable -> 0x00a7 }
            com.ut.mini.UTAnalytics r3 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ Throwable -> 0x00a7 }
            com.ut.mini.UTTracker r3 = r3.getDefaultTracker()     // Catch:{ Throwable -> 0x00a7 }
            if (r3 == 0) goto L_0x0095
            java.util.Map r1 = r10.build()     // Catch:{ Throwable -> 0x00a7 }
            r3.send(r1)     // Catch:{ Throwable -> 0x00a7 }
            goto L_0x00a0
        L_0x0095:
            java.lang.String r3 = "Record crash stacktrace error"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00a7 }
            java.lang.String r4 = "Fatal Error,must call setRequestAuthentication method first."
            r1[r2] = r4     // Catch:{ Throwable -> 0x00a7 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r3, (java.lang.Object[]) r1)     // Catch:{ Throwable -> 0x00a7 }
        L_0x00a0:
            java.lang.Thread$UncaughtExceptionHandler r1 = r11.mDefaultHandler
            if (r1 == 0) goto L_0x00b5
            goto L_0x00af
        L_0x00a5:
            r1 = move-exception
            goto L_0x00c0
        L_0x00a7:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x00a5 }
            java.lang.Thread$UncaughtExceptionHandler r1 = r11.mDefaultHandler
            if (r1 == 0) goto L_0x00b5
        L_0x00af:
            java.lang.Thread$UncaughtExceptionHandler r0 = r11.mDefaultHandler
            r0.uncaughtException(r12, r13)
            goto L_0x00bf
        L_0x00b5:
            int r12 = android.os.Process.myPid()
            android.os.Process.killProcess(r12)
            java.lang.System.exit(r0)
        L_0x00bf:
            return
        L_0x00c0:
            java.lang.Thread$UncaughtExceptionHandler r2 = r11.mDefaultHandler
            if (r2 == 0) goto L_0x00ca
            java.lang.Thread$UncaughtExceptionHandler r0 = r11.mDefaultHandler
            r0.uncaughtException(r12, r13)
            goto L_0x00d4
        L_0x00ca:
            int r12 = android.os.Process.myPid()
            android.os.Process.killProcess(r12)
            java.lang.System.exit(r0)
        L_0x00d4:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.crashhandler.UTMiniCrashHandler.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
    }
}
