package com.uc.webview.export.cyclone;

import android.os.AsyncTask;
import android.os.Process;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

@Constant
/* compiled from: U4Source */
public class UCLogger {
    private static final Class[] PARAMS_WITHOUT_THROWABLE = {String.class, String.class};
    private static final Class[] PARAMS_WITH_THROWABLE = {String.class, String.class, Throwable.class};
    private static boolean bAllowAllLevel = false;
    private static boolean bAllowAllTag = false;
    private static String sAllowLevels = "[all]";
    private static String sAllowTags = "[all]";
    /* access modifiers changed from: private */
    public static AsyncTask<Object, Object, Object> sAsyncTask = null;
    private static ArrayList<Pair<Pair<String, String>, UCLogger>> sCachedLoggers = null;
    /* access modifiers changed from: private */
    public static ValueCallback<Object[]> sCallbackChannel = null;
    private static boolean sEnabled = false;
    /* access modifiers changed from: private */
    public static final ConcurrentLinkedQueue<Object[]> sLogItems = new ConcurrentLinkedQueue<>();
    private static Class<?> sLogcatChannel;
    private String mLevel;
    private String mTag;

    private UCLogger(String str, String str2) {
        this.mLevel = str;
        this.mTag = UCCyclone.logExtraTag + str2;
    }

    private UCLogger(String str) {
        this.mLevel = str;
        this.mTag = UCCyclone.logExtraTag;
    }

    public static int createToken(String str, String str2) {
        if (sCachedLoggers == null) {
            synchronized (UCLogger.class) {
                if (sCachedLoggers == null) {
                    sCachedLoggers = new ArrayList<>(20);
                }
            }
        }
        int i = -1;
        try {
            synchronized (sCachedLoggers) {
                if (sCachedLoggers.add(new Pair(new Pair(str, str2), create(str, str2)))) {
                    i = sCachedLoggers.size() - 1;
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return i;
    }

    public static boolean print(int i, String str, Throwable... thArr) {
        UCLogger uCLogger;
        if (i < 0 || !sEnabled || (uCLogger = (UCLogger) sCachedLoggers.get(i).second) == null) {
            return false;
        }
        uCLogger.print(str, thArr);
        return true;
    }

    public static boolean enable(String str, String str2) {
        if (!sEnabled) {
            return false;
        }
        if (sLogcatChannel == null && sCallbackChannel == null) {
            return false;
        }
        if (!bAllowAllLevel && !sAllowLevels.contains(str)) {
            return false;
        }
        if (bAllowAllTag) {
            return true;
        }
        String str3 = sAllowTags;
        StringBuilder sb = new StringBuilder(Operators.ARRAY_START_STR);
        sb.append(str2);
        sb.append(Operators.ARRAY_END_STR);
        return str3.contains(sb.toString());
    }

    public static UCLogger create(String str, String str2) {
        if (enable(str, str2)) {
            return new UCLogger(str, str2);
        }
        return null;
    }

    public static UCLogger create(String str) {
        return new UCLogger(str);
    }

    public void print(String str, Throwable... thArr) {
        print(false, (String) null, str, thArr);
    }

    public void print(String str, String str2, Throwable... thArr) {
        print(false, str, str2, thArr);
    }

    public void print2Cache(String str, Throwable... thArr) {
        print(true, (String) null, str, thArr);
    }

    private void print(boolean z, String str, String str2, Throwable... thArr) {
        String str3;
        if (sEnabled) {
            Throwable th = (thArr == null || thArr.length <= 0 || thArr[0] == null) ? null : thArr[0];
            if (str != null) {
                str3 = UCCyclone.logExtraTag + str;
            } else {
                str3 = this.mTag;
            }
            try {
                if (sLogcatChannel != null) {
                    UCCyclone.invoke(sLogcatChannel, this.mLevel, th != null ? PARAMS_WITH_THROWABLE : PARAMS_WITHOUT_THROWABLE, th != null ? new Object[]{str3, str2, th} : new Object[]{str3, str2});
                }
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
            try {
                if (sCallbackChannel != null) {
                    sLogItems.add(new Object[]{Long.valueOf(System.currentTimeMillis()), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()), this.mLevel, str3, str2, th});
                    if (!z && sAsyncTask == null) {
                        sAsyncTask = new AsyncTask<Object, Object, Object>() {
                            /* access modifiers changed from: protected */
                            public Object doInBackground(Object... objArr) {
                                ValueCallback access$000 = UCLogger.sCallbackChannel;
                                if (access$000 == null) {
                                    try {
                                        UCLogger.sLogItems.clear();
                                    } catch (Throwable th) {
                                        AsyncTask unused = UCLogger.sAsyncTask = null;
                                        throw th;
                                    }
                                } else {
                                    boolean z = true;
                                    while (z) {
                                        Object poll = UCLogger.sLogItems.poll();
                                        while (true) {
                                            Object[] objArr2 = (Object[]) poll;
                                            if (objArr2 == null) {
                                                break;
                                            }
                                            access$000.onReceiveValue(objArr2);
                                            poll = UCLogger.sLogItems.poll();
                                        }
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if (((Object[]) UCLogger.sLogItems.peek()) == null) {
                                            AsyncTask unused2 = UCLogger.sAsyncTask = null;
                                            z = false;
                                        }
                                    }
                                }
                                AsyncTask unused3 = UCLogger.sAsyncTask = null;
                                return null;
                            }
                        }.execute(new Object[0]);
                    }
                }
            } catch (Throwable th3) {
                th3.printStackTrace();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x004d A[Catch:{ Throwable -> 0x0041 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0050 A[Catch:{ Throwable -> 0x0041 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0056 A[Catch:{ Throwable -> 0x0041 }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0059 A[Catch:{ Throwable -> 0x0041 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0075 A[Catch:{ Throwable -> 0x00c0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00be A[DONT_GENERATE] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void setup(java.lang.Object[] r8) {
        /*
            java.lang.Class<com.uc.webview.export.cyclone.UCLogger> r0 = com.uc.webview.export.cyclone.UCLogger.class
            monitor-enter(r0)
            r1 = 0
            r2 = r8[r1]     // Catch:{ all -> 0x00c6 }
            java.lang.Boolean r2 = (java.lang.Boolean) r2     // Catch:{ all -> 0x00c6 }
            r3 = 1
            r4 = r8[r3]     // Catch:{ all -> 0x00c6 }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ all -> 0x00c6 }
            r5 = 2
            r5 = r8[r5]     // Catch:{ all -> 0x00c6 }
            android.webkit.ValueCallback r5 = (android.webkit.ValueCallback) r5     // Catch:{ all -> 0x00c6 }
            r6 = 3
            r6 = r8[r6]     // Catch:{ all -> 0x00c6 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ all -> 0x00c6 }
            r7 = 4
            r8 = r8[r7]     // Catch:{ all -> 0x00c6 }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ all -> 0x00c6 }
            if (r2 == 0) goto L_0x0027
            boolean r2 = r2.booleanValue()     // Catch:{ all -> 0x00c6 }
            if (r2 == 0) goto L_0x0027
            sEnabled = r3     // Catch:{ all -> 0x00c6 }
            goto L_0x0029
        L_0x0027:
            sEnabled = r1     // Catch:{ all -> 0x00c6 }
        L_0x0029:
            r1 = 0
            if (r4 == 0) goto L_0x0043
            boolean r2 = r4.booleanValue()     // Catch:{ Throwable -> 0x0041 }
            if (r2 == 0) goto L_0x0043
            boolean r2 = r4.booleanValue()     // Catch:{ Throwable -> 0x0041 }
            if (r2 == 0) goto L_0x003e
            java.lang.String r1 = "android.util.Log"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch:{ Throwable -> 0x0041 }
        L_0x003e:
            sLogcatChannel = r1     // Catch:{ Throwable -> 0x0041 }
            goto L_0x0049
        L_0x0041:
            r1 = move-exception
            goto L_0x0046
        L_0x0043:
            sLogcatChannel = r1     // Catch:{ Throwable -> 0x0041 }
            goto L_0x0049
        L_0x0046:
            r1.printStackTrace()     // Catch:{ all -> 0x00c6 }
        L_0x0049:
            sCallbackChannel = r5     // Catch:{ all -> 0x00c6 }
            if (r6 == 0) goto L_0x0050
            sAllowLevels = r6     // Catch:{ all -> 0x00c6 }
            goto L_0x0054
        L_0x0050:
            java.lang.String r1 = ""
            sAllowLevels = r1     // Catch:{ all -> 0x00c6 }
        L_0x0054:
            if (r8 == 0) goto L_0x0059
            sAllowTags = r8     // Catch:{ all -> 0x00c6 }
            goto L_0x005d
        L_0x0059:
            java.lang.String r8 = ""
            sAllowTags = r8     // Catch:{ all -> 0x00c6 }
        L_0x005d:
            java.lang.String r8 = sAllowLevels     // Catch:{ all -> 0x00c6 }
            java.lang.String r1 = "[all]"
            boolean r8 = r8.contains(r1)     // Catch:{ all -> 0x00c6 }
            bAllowAllLevel = r8     // Catch:{ all -> 0x00c6 }
            java.lang.String r8 = sAllowTags     // Catch:{ all -> 0x00c6 }
            java.lang.String r1 = "[all]"
            boolean r8 = r8.contains(r1)     // Catch:{ all -> 0x00c6 }
            bAllowAllTag = r8     // Catch:{ all -> 0x00c6 }
            java.util.ArrayList<android.util.Pair<android.util.Pair<java.lang.String, java.lang.String>, com.uc.webview.export.cyclone.UCLogger>> r8 = sCachedLoggers     // Catch:{ Throwable -> 0x00c0 }
            if (r8 == 0) goto L_0x00be
            java.util.ArrayList<android.util.Pair<android.util.Pair<java.lang.String, java.lang.String>, com.uc.webview.export.cyclone.UCLogger>> r8 = sCachedLoggers     // Catch:{ Throwable -> 0x00c0 }
            monitor-enter(r8)     // Catch:{ Throwable -> 0x00c0 }
            java.util.ArrayList<android.util.Pair<android.util.Pair<java.lang.String, java.lang.String>, com.uc.webview.export.cyclone.UCLogger>> r1 = sCachedLoggers     // Catch:{ all -> 0x00bb }
            int r1 = r1.size()     // Catch:{ all -> 0x00bb }
            int r1 = r1 - r3
        L_0x007f:
            if (r1 < 0) goto L_0x00b8
            java.util.ArrayList<android.util.Pair<android.util.Pair<java.lang.String, java.lang.String>, com.uc.webview.export.cyclone.UCLogger>> r2 = sCachedLoggers     // Catch:{ all -> 0x00bb }
            java.lang.Object r2 = r2.get(r1)     // Catch:{ all -> 0x00bb }
            android.util.Pair r2 = (android.util.Pair) r2     // Catch:{ all -> 0x00bb }
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x00bb }
            android.util.Pair r3 = (android.util.Pair) r3     // Catch:{ all -> 0x00bb }
            java.lang.Object r3 = r3.first     // Catch:{ all -> 0x00bb }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x00bb }
            java.lang.Object r4 = r2.first     // Catch:{ all -> 0x00bb }
            android.util.Pair r4 = (android.util.Pair) r4     // Catch:{ all -> 0x00bb }
            java.lang.Object r4 = r4.second     // Catch:{ all -> 0x00bb }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ all -> 0x00bb }
            com.uc.webview.export.cyclone.UCLogger r3 = create(r3, r4)     // Catch:{ all -> 0x00bb }
            java.lang.Object r4 = r2.second     // Catch:{ all -> 0x00bb }
            if (r4 != 0) goto L_0x00a3
            if (r3 != 0) goto L_0x00a9
        L_0x00a3:
            java.lang.Object r4 = r2.second     // Catch:{ all -> 0x00bb }
            if (r4 == 0) goto L_0x00b5
            if (r3 != 0) goto L_0x00b5
        L_0x00a9:
            java.util.ArrayList<android.util.Pair<android.util.Pair<java.lang.String, java.lang.String>, com.uc.webview.export.cyclone.UCLogger>> r4 = sCachedLoggers     // Catch:{ all -> 0x00bb }
            android.util.Pair r5 = new android.util.Pair     // Catch:{ all -> 0x00bb }
            java.lang.Object r2 = r2.first     // Catch:{ all -> 0x00bb }
            r5.<init>(r2, r3)     // Catch:{ all -> 0x00bb }
            r4.set(r1, r5)     // Catch:{ all -> 0x00bb }
        L_0x00b5:
            int r1 = r1 + -1
            goto L_0x007f
        L_0x00b8:
            monitor-exit(r8)     // Catch:{ all -> 0x00bb }
            monitor-exit(r0)
            return
        L_0x00bb:
            r1 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x00bb }
            throw r1     // Catch:{ Throwable -> 0x00c0 }
        L_0x00be:
            monitor-exit(r0)
            return
        L_0x00c0:
            r8 = move-exception
            r8.printStackTrace()     // Catch:{ all -> 0x00c6 }
            monitor-exit(r0)
            return
        L_0x00c6:
            r8 = move-exception
            monitor-exit(r0)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCLogger.setup(java.lang.Object[]):void");
    }
}
