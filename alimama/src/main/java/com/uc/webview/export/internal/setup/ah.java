package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.webkit.ValueCallback;
import com.taobao.accs.common.Constants;
import com.uc.webview.export.internal.setup.UCSetupTask;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public class ah<RETURN_TYPE extends UCSetupTask<RETURN_TYPE, CALLBACK_TYPE>, CALLBACK_TYPE extends UCSetupTask<RETURN_TYPE, CALLBACK_TYPE>> {
    /* access modifiers changed from: private */
    public static String d = "ah";
    String a = "";
    public final ValueCallback<CALLBACK_TYPE> b = new ai(this);
    public final ValueCallback<CALLBACK_TYPE> c = new aj(this);
    /* access modifiers changed from: private */
    public WeakReference<UCSetupTask> e;
    /* access modifiers changed from: private */
    public File f;
    /* access modifiers changed from: private */
    public File g;
    /* access modifiers changed from: private */
    public File h;

    public static String a(String str) {
        return "ThickSetupTask_" + str;
    }

    public static void a(Context context, String[] strArr) {
        for (int i = 0; i < 2; i++) {
            ah unused = a.b((UCSetupTask) null, context, strArr[i]);
        }
    }

    /* compiled from: U4Source */
    public static class a {
        private static ConcurrentHashMap<String, ah> a = new ConcurrentHashMap<>();

        public static ah a(UCSetupTask uCSetupTask, Context context, String str) {
            if (k.a(str)) {
                return null;
            }
            synchronized (ah.class) {
                if (a.containsKey(str)) {
                    ah ahVar = a.get(str);
                    ahVar.a(uCSetupTask);
                    return ahVar;
                }
                ah b = b(uCSetupTask, context, str);
                return b;
            }
        }

        /* access modifiers changed from: private */
        public static ah b(UCSetupTask uCSetupTask, Context context, String str) {
            ah ahVar;
            String b = ah.d;
            Log.d(b, "create " + str);
            if (k.a(str)) {
                return null;
            }
            synchronized (ah.class) {
                ahVar = new ah(uCSetupTask, context, str);
                a.put(str, ahVar);
            }
            return ahVar;
        }
    }

    public ah(UCSetupTask uCSetupTask, Context context, String str) {
        if (this.f == null) {
            a(uCSetupTask);
            File b2 = k.b(k.a(context, Constants.KEY_FLAGS), k.e(str));
            String str2 = d;
            Log.d(str2, "<init> flgDirFile.path: " + b2.getAbsolutePath());
            this.f = new File(b2, "b36ce8d879e33bc88f717f74617ea05a");
            this.g = new File(b2, "bd89426940609c9ae14e5ae90827201b");
            this.h = new File(b2, "51bfcd9dd2f1379936c4fbb3558a6e67");
        }
    }

    public final void a(UCSetupTask uCSetupTask) {
        if (uCSetupTask == null) {
            return;
        }
        if (this.e == null || this.e.get() != uCSetupTask) {
            this.e = new WeakReference<>(uCSetupTask);
            String str = d;
            Log.d(str, "UCSetupt.class: " + ((UCSetupTask) this.e.get()).getClass());
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0005 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a() {
        /*
            r1 = this;
            java.io.File r0 = r1.f     // Catch:{ Throwable -> 0x0005 }
            r0.delete()     // Catch:{ Throwable -> 0x0005 }
        L_0x0005:
            java.io.File r0 = r1.h     // Catch:{ Throwable -> 0x000b }
            r0.delete()     // Catch:{ Throwable -> 0x000b }
            return
        L_0x000b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.ah.a():void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:6|7|8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x006c, code lost:
        if (r1 != false) goto L_0x0070;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x003a */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void d(com.uc.webview.export.internal.setup.ah r6) {
        /*
            java.io.File r0 = r6.f
            boolean r0 = r0.exists()
            java.io.File r1 = r6.h
            boolean r1 = r1.exists()
            java.io.File r2 = r6.g
            boolean r2 = r2.exists()
            r3 = 1
            if (r2 == 0) goto L_0x006a
            if (r0 == 0) goto L_0x0070
            if (r1 == 0) goto L_0x0070
            java.io.File r1 = r6.g
            long r1 = r1.lastModified()
            java.io.File r3 = r6.h
            long r3 = r3.lastModified()
            long r1 = java.lang.Math.max(r1, r3)
            long r3 = java.lang.System.currentTimeMillis()
            long r3 = r3 - r1
            r1 = 86400000(0x5265c00, double:4.2687272E-316)
            int r5 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r5 <= 0) goto L_0x0040
            java.io.File r1 = r6.g     // Catch:{ Throwable -> 0x003a }
            r1.delete()     // Catch:{ Throwable -> 0x003a }
        L_0x003a:
            java.io.File r1 = r6.f     // Catch:{ Throwable -> 0x006f }
            r1.delete()     // Catch:{ Throwable -> 0x006f }
            goto L_0x006f
        L_0x0040:
            java.lang.String r0 = "2"
            r6.a = r0
            java.lang.ref.WeakReference<com.uc.webview.export.internal.setup.UCSetupTask> r0 = r6.e
            java.lang.Object r0 = r0.get()
            com.uc.webview.export.internal.setup.UCSetupTask r0 = (com.uc.webview.export.internal.setup.UCSetupTask) r0
            java.lang.String r1 = "disable_multi_unknown_crash"
            java.lang.Object r0 = r0.getOption(r1)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            if (r0 == 0) goto L_0x005c
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x0069
        L_0x005c:
            java.lang.ref.WeakReference<com.uc.webview.export.internal.setup.UCSetupTask> r6 = r6.e
            java.lang.Object r6 = r6.get()
            com.uc.webview.export.internal.setup.UCSetupTask r6 = (com.uc.webview.export.internal.setup.UCSetupTask) r6
            java.lang.String r0 = "crash_repeat"
            r6.callback(r0)
        L_0x0069:
            return
        L_0x006a:
            if (r0 == 0) goto L_0x006f
            if (r1 == 0) goto L_0x006f
            goto L_0x0070
        L_0x006f:
            r3 = 0
        L_0x0070:
            if (r0 == 0) goto L_0x009b
            java.lang.ref.WeakReference<com.uc.webview.export.internal.setup.UCSetupTask> r0 = r6.e
            java.lang.Object r0 = r0.get()
            com.uc.webview.export.internal.setup.UCSetupTask r0 = (com.uc.webview.export.internal.setup.UCSetupTask) r0
            java.lang.String r1 = "VERIFY_POLICY"
            java.lang.Object r0 = r0.getOption(r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            if (r0 == 0) goto L_0x009b
            java.lang.ref.WeakReference<com.uc.webview.export.internal.setup.UCSetupTask> r1 = r6.e
            java.lang.Object r1 = r1.get()
            com.uc.webview.export.internal.setup.UCSetupTask r1 = (com.uc.webview.export.internal.setup.UCSetupTask) r1
            java.lang.String r2 = "VERIFY_POLICY"
            int r0 = r0.intValue()
            r0 = r0 | 16
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r1.setup((java.lang.String) r2, (java.lang.Object) r0)
        L_0x009b:
            if (r3 == 0) goto L_0x00a0
            java.lang.String r0 = "1"
            goto L_0x00a2
        L_0x00a0:
            java.lang.String r0 = "0"
        L_0x00a2:
            r6.a = r0
            java.lang.ref.WeakReference<com.uc.webview.export.internal.setup.UCSetupTask> r6 = r6.e
            java.lang.Object r6 = r6.get()
            com.uc.webview.export.internal.setup.UCSetupTask r6 = (com.uc.webview.export.internal.setup.UCSetupTask) r6
            if (r3 == 0) goto L_0x00b1
            java.lang.String r0 = "crash_seen"
            goto L_0x00b3
        L_0x00b1:
            java.lang.String r0 = "crash_none"
        L_0x00b3:
            r6.callback(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.ah.d(com.uc.webview.export.internal.setup.ah):void");
    }

    static /* synthetic */ void e(ah ahVar) {
        try {
            if (!ahVar.f.exists()) {
                ahVar.f.createNewFile();
            } else if (!ahVar.h.exists()) {
                ahVar.h.createNewFile();
            } else if (!ahVar.g.exists()) {
                ahVar.g.createNewFile();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
