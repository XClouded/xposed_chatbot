package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.ValueCallback;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.cyclone.UCLoader;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.uc.startup.a;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import com.uc.webview.export.internal.utility.d;
import com.uc.webview.export.internal.utility.e;
import com.uc.webview.export.internal.utility.g;
import com.uc.webview.export.internal.utility.h;
import com.uc.webview.export.internal.utility.i;
import com.uc.webview.export.internal.utility.k;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: U4Source */
public final class ak {
    private static final AtomicBoolean a = new AtomicBoolean(false);
    private static final AtomicBoolean b = new AtomicBoolean(false);
    private static final AtomicBoolean c = new AtomicBoolean(false);
    private static final AtomicBoolean d = new AtomicBoolean(false);
    private static final AtomicBoolean e = new AtomicBoolean(false);
    private static final AtomicBoolean f = new AtomicBoolean(false);
    private static final AtomicBoolean g = new AtomicBoolean(false);
    private static final AtomicBoolean h = new AtomicBoolean(false);
    private static final AtomicBoolean i = new AtomicBoolean(false);
    private static final AtomicBoolean j = new AtomicBoolean(false);
    private static final AtomicBoolean k = new AtomicBoolean(false);
    private static final AtomicBoolean l = new AtomicBoolean(false);
    private static String m = null;

    private ak() {
    }

    private static void a() {
        i.a((Runnable) new al());
        ae.a();
    }

    /* access modifiers changed from: private */
    public static void c(Context context) {
        Log.e("SetupPreprocess", "commonInitPreprocess " + context);
        b.a(516);
        d(context);
        b();
        a(context, (String) null);
        a();
        o.a().b();
        b.a(517);
    }

    public static void a(Context context) {
        Log.e("SetupPreprocess", "asyncInitPreprocess " + context);
        if (b.compareAndSet(false, true)) {
            i.a((Runnable) new am(context));
        }
    }

    private static void a(Class cls, ClassLoader classLoader) {
        try {
            Class.forName(cls.getName(), true, classLoader);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static void a(Context context, String str) {
        int i2;
        Log.e("SetupPreprocess", "preloadIo " + context + AVFSCacheConstants.COMMA_SEP + str);
        if (context != null) {
            int i3 = 0;
            if (e.compareAndSet(false, true)) {
                b.a(522);
                try {
                    d.a().a("gk_preload_io", true);
                    h.b(context);
                    SDKFactory.c(context);
                    IWaStat.WaStat.stat("pre_head");
                    if (k.g()) {
                        String[] strArr = new String[2];
                        if (!k.a(str)) {
                            strArr[0] = ah.a(UCCore.getExtractDirPath(context, new File(str, "libkernelu4_7z_uc.so").getAbsolutePath()));
                            i3 = 1;
                        }
                        if (h.a() != null) {
                            i2 = i3 + 1;
                            strArr[i3] = ah.a(h.a().soDirPath);
                        } else {
                            i2 = i3;
                        }
                        if (i2 > 0) {
                            ah.a(context, strArr);
                        }
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                } finally {
                    b.a(523);
                }
            }
        }
    }

    private static void d(Context context) {
        if (a.compareAndSet(false, true)) {
            Log.e("SetupPreprocess", "preloadStart " + context);
            if (!com.uc.webview.export.internal.utility.Log.enableUCLogger() && context != null && k.compareAndSet(false, true)) {
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "uclogconfig.apk");
                    UCElapseTime uCElapseTime = new UCElapseTime();
                    boolean exists = file.exists();
                    b.a(227, uCElapseTime.getMilis());
                    if (exists && file.isFile() && file.length() <= TBToast.Duration.MEDIUM && g.a(file.getAbsolutePath(), context, context, "com.UCMobile", (ValueCallback<Object[]>) null, (e.a) null)) {
                        String str = (String) ReflectionUtil.invoke(new UCLoader(file.getAbsolutePath(), context.getCacheDir().getAbsolutePath(), (String) null, UCSetupTask.class.getClassLoader()).loadClass("com.uc.webview.uclogconfig.UCSDKLogConfig"), "getEnabledDate", (Class[]) null, (Object[]) null);
                        String format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                        if (str != null && str.length() >= 8 && format != null && format.length() >= 8 && str.startsWith(format)) {
                            UCCore.setPrintLog(true);
                        }
                    }
                } catch (Throwable th) {
                    Log.e("SetupPreprocess", "setupPrintLogByConfigFile ", th);
                }
            }
            a.a();
        }
    }

    private static boolean a(ClassLoader classLoader) {
        if (l.get()) {
            return true;
        }
        if (classLoader != null) {
            try {
                Class.forName(br.CORE_FACTORY_IMPL_CLASS, true, classLoader);
            } catch (ClassNotFoundException e2) {
                Log.e("SetupPreprocess", "shouldPreLaunchCoreSetupTask failed. ", e2);
                return false;
            }
        } else {
            Class.forName(br.CORE_FACTORY_IMPL_CLASS);
        }
        l.set(true);
        return true;
    }

    private static void a(int i2, Object[] objArr) {
        new StringBuilder("SetupController.preLaunchCoreSetupTask_").append(String.valueOf(i2));
        a.a(i2, objArr);
        new StringBuilder("SetupController.preLaunchCoreSetupTask_").append(String.valueOf(i2));
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:49:0x0223=Splitter:B:49:0x0223, B:97:0x036a=Splitter:B:97:0x036a} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object a(int r7, android.content.Context r8, java.lang.Object[] r9) {
        /*
            r0 = 0
            d(r8)     // Catch:{ Throwable -> 0x0379 }
            r1 = 2
            r2 = 1
            r3 = 0
            switch(r7) {
                case 0: goto L_0x02e5;
                case 1: goto L_0x02dc;
                case 2: goto L_0x02cd;
                case 3: goto L_0x0297;
                case 4: goto L_0x0237;
                case 5: goto L_0x017b;
                case 6: goto L_0x0128;
                case 7: goto L_0x010a;
                case 8: goto L_0x009e;
                case 9: goto L_0x0043;
                case 10: goto L_0x000c;
                default: goto L_0x000a;
            }     // Catch:{ Throwable -> 0x0379 }
        L_0x000a:
            goto L_0x037d
        L_0x000c:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "initPreprocess "
            r7.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "SetupPreprocess"
            android.util.Log.e(r9, r7)     // Catch:{ Throwable -> 0x0379 }
            java.util.concurrent.atomic.AtomicBoolean r7 = b     // Catch:{ Throwable -> 0x0379 }
            boolean r7 = r7.compareAndSet(r3, r2)     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x037d
            r7 = 518(0x206, float:7.26E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r7)     // Catch:{ Throwable -> 0x0379 }
            c(r8)     // Catch:{ Throwable -> 0x0379 }
            com.uc.webview.export.internal.utility.d r7 = com.uc.webview.export.internal.utility.d.a()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = "gk_init_pre"
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r2)     // Catch:{ Throwable -> 0x0379 }
            r7.a(r8, r9)     // Catch:{ Throwable -> 0x0379 }
            r7 = 519(0x207, float:7.27E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r7)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x0043:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "preInitIcu "
            r7.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = "SetupPreprocess"
            android.util.Log.e(r8, r7)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = "SetupPreprocess"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "preInitIcu sDecompressRootDir:"
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = m     // Catch:{ Throwable -> 0x0379 }
            r8.append(r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0379 }
            com.uc.webview.export.internal.utility.Log.i(r7, r8)     // Catch:{ Throwable -> 0x0379 }
            java.util.concurrent.atomic.AtomicBoolean r7 = j     // Catch:{ Throwable -> 0x0379 }
            boolean r7 = r7.compareAndSet(r3, r2)     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x0087
            boolean r7 = a((java.lang.ClassLoader) r0)     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x0087
            java.lang.String r7 = m     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x0087
            com.uc.webview.export.internal.uc.startup.a.a()     // Catch:{ Throwable -> 0x0379 }
            r7 = 9007(0x232f, float:1.2621E-41)
            a((int) r7, (java.lang.Object[]) r0)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x0087:
            java.lang.String r7 = "SetupPreprocess"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "preInitIcu failed, sDecompressRootDir:"
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = m     // Catch:{ Throwable -> 0x0379 }
            r8.append(r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0379 }
            com.uc.webview.export.internal.utility.Log.e(r7, r8)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x009e:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "preInitPak "
            r7.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = "SetupPreprocess"
            android.util.Log.e(r8, r7)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = "SetupPreprocess"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "preInitPak sDecompressRootDir:"
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = m     // Catch:{ Throwable -> 0x0379 }
            r8.append(r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0379 }
            com.uc.webview.export.internal.utility.Log.i(r7, r8)     // Catch:{ Throwable -> 0x0379 }
            java.util.concurrent.atomic.AtomicBoolean r7 = i     // Catch:{ Throwable -> 0x0379 }
            boolean r7 = r7.compareAndSet(r3, r2)     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x00f3
            boolean r7 = a((java.lang.ClassLoader) r0)     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x00f3
            java.lang.String r7 = m     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x00f3
            com.uc.webview.export.internal.uc.startup.a.a()     // Catch:{ Throwable -> 0x0379 }
            java.io.File r7 = new java.io.File     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = m     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "assets"
            r7.<init>(r8, r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = r7.getAbsolutePath()     // Catch:{ Throwable -> 0x0379 }
            r8 = 9008(0x2330, float:1.2623E-41)
            java.lang.Object[] r9 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0379 }
            r9[r3] = r7     // Catch:{ Throwable -> 0x0379 }
            a((int) r8, (java.lang.Object[]) r9)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x00f3:
            java.lang.String r7 = "SetupPreprocess"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "preInitPak failed, sDecompressRootDir:"
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = m     // Catch:{ Throwable -> 0x0379 }
            r8.append(r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0379 }
            com.uc.webview.export.internal.utility.Log.e(r7, r8)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x010a:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "preStartCoreEngine "
            r7.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "SetupPreprocess"
            android.util.Log.e(r9, r7)     // Catch:{ Throwable -> 0x0379 }
            r7 = 9009(0x2331, float:1.2624E-41)
            java.lang.Object[] r9 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0379 }
            r9[r3] = r8     // Catch:{ Throwable -> 0x0379 }
            a((int) r7, (java.lang.Object[]) r9)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x0128:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "prePartialInitWebView "
            r7.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "SetupPreprocess"
            android.util.Log.e(r9, r7)     // Catch:{ Throwable -> 0x0379 }
            java.util.concurrent.atomic.AtomicBoolean r7 = h     // Catch:{ Throwable -> 0x0379 }
            boolean r7 = r7.compareAndSet(r3, r2)     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x0172
            boolean r7 = a((java.lang.ClassLoader) r0)     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x0172
            com.uc.webview.export.internal.uc.startup.a.a()     // Catch:{ Throwable -> 0x0379 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "preInitWebviewProvider "
            r7.<init>(r9)     // Catch:{ Throwable -> 0x0379 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = "SetupPreprocess"
            android.util.Log.e(r9, r7)     // Catch:{ Throwable -> 0x0379 }
            r7 = 9005(0x232d, float:1.2619E-41)
            java.lang.Object[] r9 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0379 }
            r9[r3] = r8     // Catch:{ Throwable -> 0x0379 }
            a((int) r7, (java.lang.Object[]) r9)     // Catch:{ Throwable -> 0x0379 }
            com.uc.webview.export.internal.setup.j.a((android.content.Context) r8)     // Catch:{ Throwable -> 0x0379 }
            r7 = 9006(0x232e, float:1.262E-41)
            a((int) r7, (java.lang.Object[]) r0)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x0172:
            java.lang.String r7 = "SetupPreprocess"
            java.lang.String r8 = "prePartialInitWebView failed"
            com.uc.webview.export.internal.utility.Log.e(r7, r8)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x017b:
            r7 = r9[r3]     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x0379 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r4 = "preloadSo "
            r9.<init>(r4)     // Catch:{ Throwable -> 0x0379 }
            r9.append(r8)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r4 = ", decompressRootDir"
            r9.append(r4)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r4 = "SetupPreprocess"
            android.util.Log.e(r4, r9)     // Catch:{ Throwable -> 0x0379 }
            com.uc.webview.export.internal.uc.startup.a.a()     // Catch:{ Throwable -> 0x0379 }
            java.lang.ClassLoader r9 = com.uc.webview.export.internal.setup.af.e()     // Catch:{ Throwable -> 0x0379 }
            if (r8 == 0) goto L_0x037d
            boolean r4 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)     // Catch:{ Throwable -> 0x0379 }
            if (r4 == 0) goto L_0x01a8
            goto L_0x037d
        L_0x01a8:
            java.util.concurrent.atomic.AtomicBoolean r4 = f     // Catch:{ Throwable -> 0x0379 }
            boolean r4 = r4.getAndSet(r2)     // Catch:{ Throwable -> 0x0379 }
            if (r4 != 0) goto L_0x037d
            boolean r9 = a((java.lang.ClassLoader) r9)     // Catch:{ Throwable -> 0x0379 }
            if (r9 == 0) goto L_0x037d
            r9 = 524(0x20c, float:7.34E-43)
            r4 = 525(0x20d, float:7.36E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r9)     // Catch:{ Throwable -> 0x022a }
            java.io.File r9 = new java.io.File     // Catch:{ Throwable -> 0x022a }
            java.lang.String r5 = "lib"
            r9.<init>(r7, r5)     // Catch:{ Throwable -> 0x022a }
            java.io.File r9 = com.uc.webview.export.internal.utility.k.c((java.io.File) r9)     // Catch:{ Throwable -> 0x022a }
            java.lang.String r9 = r9.getCanonicalPath()     // Catch:{ Throwable -> 0x022a }
            java.io.File r5 = new java.io.File     // Catch:{ Throwable -> 0x022a }
            java.lang.String r6 = "libwebviewuc.so"
            r5.<init>(r9, r6)     // Catch:{ Throwable -> 0x022a }
            boolean r5 = r5.exists()     // Catch:{ Throwable -> 0x022a }
            if (r5 == 0) goto L_0x0210
            r5 = 9004(0x232c, float:1.2617E-41)
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x022a }
            r1[r3] = r8     // Catch:{ Throwable -> 0x022a }
            r1[r2] = r9     // Catch:{ Throwable -> 0x022a }
            java.lang.Object r8 = com.uc.webview.export.internal.uc.startup.a.a(r5, r1)     // Catch:{ Throwable -> 0x022a }
            java.lang.Boolean r8 = (java.lang.Boolean) r8     // Catch:{ Throwable -> 0x022a }
            boolean r8 = r8.booleanValue()     // Catch:{ Throwable -> 0x022a }
            if (r8 == 0) goto L_0x01fc
            m = r7     // Catch:{ Throwable -> 0x022a }
            com.uc.webview.export.internal.utility.d r7 = com.uc.webview.export.internal.utility.d.a()     // Catch:{ Throwable -> 0x022a }
            java.lang.String r9 = "gk_preload_so"
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r2)     // Catch:{ Throwable -> 0x022a }
            r7.a(r9, r1)     // Catch:{ Throwable -> 0x022a }
        L_0x01fc:
            java.lang.String r7 = "SetupPreprocess"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x022a }
            java.lang.String r1 = "preloadSoInternal  libwebviewuc res:"
            r9.<init>(r1)     // Catch:{ Throwable -> 0x022a }
            r9.append(r8)     // Catch:{ Throwable -> 0x022a }
            java.lang.String r8 = r9.toString()     // Catch:{ Throwable -> 0x022a }
            com.uc.webview.export.internal.utility.Log.i(r7, r8)     // Catch:{ Throwable -> 0x022a }
            goto L_0x0223
        L_0x0210:
            java.lang.String r7 = "SetupPreprocess"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x022a }
            java.lang.String r1 = "preloadSoInternal failed libwebviewuc not exist. libDir:"
            r8.<init>(r1)     // Catch:{ Throwable -> 0x022a }
            r8.append(r9)     // Catch:{ Throwable -> 0x022a }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x022a }
            com.uc.webview.export.internal.utility.Log.e(r7, r8)     // Catch:{ Throwable -> 0x022a }
        L_0x0223:
            com.uc.webview.export.internal.uc.startup.b.a(r4)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x0228:
            r7 = move-exception
            goto L_0x0233
        L_0x022a:
            r7 = move-exception
            java.lang.String r8 = "SetupPreprocess"
            java.lang.String r9 = "preloadSoInternal failed. "
            com.uc.webview.export.internal.utility.Log.e(r8, r9, r7)     // Catch:{ all -> 0x0228 }
            goto L_0x0223
        L_0x0233:
            com.uc.webview.export.internal.uc.startup.b.a(r4)     // Catch:{ Throwable -> 0x0379 }
            throw r7     // Catch:{ Throwable -> 0x0379 }
        L_0x0237:
            r7 = r9[r3]     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x0379 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r1 = "preloadJar "
            r9.<init>(r1)     // Catch:{ Throwable -> 0x0379 }
            r9.append(r8)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r1 = ", decompressRootDir"
            r9.append(r1)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r1 = "SetupPreprocess"
            android.util.Log.e(r1, r9)     // Catch:{ Throwable -> 0x0379 }
            r9 = 530(0x212, float:7.43E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r9)     // Catch:{ Throwable -> 0x0379 }
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r2 = "core.jar"
            r1.<init>(r7, r2)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = r1.getAbsolutePath()     // Catch:{ Throwable -> 0x0379 }
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0379 }
            r1.<init>(r7)     // Catch:{ Throwable -> 0x0379 }
            boolean r1 = r1.exists()     // Catch:{ Throwable -> 0x0379 }
            if (r1 == 0) goto L_0x0292
            java.lang.String r1 = "odexs"
            java.io.File r1 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r8, (java.lang.String) r1)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = com.uc.webview.export.internal.utility.k.b((android.content.Context) r8, (java.lang.String) r7)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r8 = com.uc.webview.export.internal.utility.k.e((java.lang.String) r8)     // Catch:{ Throwable -> 0x0379 }
            java.io.File r8 = com.uc.webview.export.internal.utility.k.b((java.io.File) r1, (java.lang.String) r8)     // Catch:{ Throwable -> 0x0379 }
            boolean r1 = r8.exists()     // Catch:{ Throwable -> 0x0379 }
            if (r1 == 0) goto L_0x0292
            java.lang.String r8 = r8.getAbsolutePath()     // Catch:{ Throwable -> 0x0379 }
            java.lang.ClassLoader r7 = com.uc.webview.export.internal.setup.af.a(r7, r8, r0)     // Catch:{ Throwable -> 0x0379 }
            com.uc.webview.export.internal.setup.af.a((java.lang.ClassLoader) r7)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x0293
        L_0x0292:
            r7 = r0
        L_0x0293:
            com.uc.webview.export.internal.uc.startup.b.a(r9)     // Catch:{ Throwable -> 0x0379 }
            return r7
        L_0x0297:
            java.lang.String r7 = "preloadCoreClass"
            java.lang.String r8 = "SetupPreprocess"
            android.util.Log.e(r8, r7)     // Catch:{ Throwable -> 0x0379 }
            java.util.concurrent.atomic.AtomicBoolean r7 = d     // Catch:{ Throwable -> 0x0379 }
            boolean r7 = r7.compareAndSet(r3, r2)     // Catch:{ Throwable -> 0x0379 }
            if (r7 == 0) goto L_0x037d
            boolean r7 = com.uc.webview.export.internal.utility.k.g()     // Catch:{ Throwable -> 0x02c6 }
            if (r7 == 0) goto L_0x02b7
            java.lang.Class<com.uc.webview.export.internal.setup.ak> r7 = com.uc.webview.export.internal.setup.ak.class
            java.lang.ClassLoader r7 = r7.getClassLoader()     // Catch:{ Throwable -> 0x02c6 }
            com.uc.webview.export.internal.uc.CoreClassPreLoader.a(r7)     // Catch:{ Throwable -> 0x02c6 }
            goto L_0x037d
        L_0x02b7:
            boolean r7 = com.uc.webview.export.internal.uc.startup.a.b()     // Catch:{ Throwable -> 0x02c6 }
            if (r7 == 0) goto L_0x037d
            r7 = 9001(0x2329, float:1.2613E-41)
            com.uc.webview.export.internal.uc.startup.a.a(r7, r0)     // Catch:{ Throwable -> 0x02c6 }
            goto L_0x037d
        L_0x02c4:
            r7 = move-exception
            goto L_0x02cc
        L_0x02c6:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ all -> 0x02c4 }
            goto L_0x037d
        L_0x02cc:
            throw r7     // Catch:{ Throwable -> 0x0379 }
        L_0x02cd:
            b()     // Catch:{ Throwable -> 0x0379 }
            a()     // Catch:{ Throwable -> 0x0379 }
            com.uc.webview.export.internal.setup.o r7 = com.uc.webview.export.internal.setup.o.a()     // Catch:{ Throwable -> 0x0379 }
            r7.b()     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x02dc:
            r7 = r9[r3]     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x0379 }
            a((android.content.Context) r8, (java.lang.String) r7)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x02e5:
            r7 = r9[r3]     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x0379 }
            r4 = r9[r2]     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x0379 }
            r1 = r9[r1]     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x0379 }
            r5 = 3
            r9 = r9[r5]     // Catch:{ Throwable -> 0x0379 }
            java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch:{ Throwable -> 0x0379 }
            boolean r9 = r9.booleanValue()     // Catch:{ Throwable -> 0x0379 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r6 = "preDecompress "
            r5.<init>(r6)     // Catch:{ Throwable -> 0x0379 }
            r5.append(r8)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r6 = ", "
            r5.append(r6)     // Catch:{ Throwable -> 0x0379 }
            r5.append(r7)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r6 = ", "
            r5.append(r6)     // Catch:{ Throwable -> 0x0379 }
            r5.append(r4)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r6 = ", "
            r5.append(r6)     // Catch:{ Throwable -> 0x0379 }
            r5.append(r1)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r6 = ", "
            r5.append(r6)     // Catch:{ Throwable -> 0x0379 }
            r5.append(r9)     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r9 = r5.toString()     // Catch:{ Throwable -> 0x0379 }
            java.lang.String r5 = "SetupPreprocess"
            android.util.Log.e(r5, r9)     // Catch:{ Throwable -> 0x0379 }
            if (r8 == 0) goto L_0x037d
            boolean r9 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)     // Catch:{ Throwable -> 0x0379 }
            if (r9 != 0) goto L_0x037d
            boolean r9 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r1)     // Catch:{ Throwable -> 0x0379 }
            if (r9 == 0) goto L_0x033c
            goto L_0x037d
        L_0x033c:
            java.util.concurrent.atomic.AtomicBoolean r9 = g     // Catch:{ Throwable -> 0x0379 }
            boolean r9 = r9.compareAndSet(r3, r2)     // Catch:{ Throwable -> 0x0379 }
            if (r9 == 0) goto L_0x037d
            r9 = 528(0x210, float:7.4E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r9)     // Catch:{ Throwable -> 0x0379 }
            r9 = 529(0x211, float:7.41E-43)
            java.io.File r2 = new java.io.File     // Catch:{ Throwable -> 0x0370 }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x0370 }
            r2.mkdirs()     // Catch:{ Throwable -> 0x0370 }
            boolean r7 = com.uc.webview.export.extension.UCCore.extractWebCoreLibraryIfNeeded(r8, r7, r4, r1, r3)     // Catch:{ Throwable -> 0x0370 }
            java.lang.String r8 = "SetupPreprocess"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0370 }
            java.lang.String r2 = "preDecompress extract: "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0370 }
            r1.append(r7)     // Catch:{ Throwable -> 0x0370 }
            java.lang.String r7 = r1.toString()     // Catch:{ Throwable -> 0x0370 }
            com.uc.webview.export.internal.utility.Log.d(r8, r7)     // Catch:{ Throwable -> 0x0370 }
        L_0x036a:
            com.uc.webview.export.internal.uc.startup.b.a(r9)     // Catch:{ Throwable -> 0x0379 }
            goto L_0x037d
        L_0x036e:
            r7 = move-exception
            goto L_0x0375
        L_0x0370:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ all -> 0x036e }
            goto L_0x036a
        L_0x0375:
            com.uc.webview.export.internal.uc.startup.b.a(r9)     // Catch:{ Throwable -> 0x0379 }
            throw r7     // Catch:{ Throwable -> 0x0379 }
        L_0x0379:
            r7 = move-exception
            r7.printStackTrace()
        L_0x037d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.ak.a(int, android.content.Context, java.lang.Object[]):java.lang.Object");
    }

    private static void b() {
        Log.e("SetupPreprocess", "preloadSdkClass ");
        if (c.compareAndSet(false, true)) {
            b.a(520);
            try {
                ClassLoader classLoader = ak.class.getClassLoader();
                a(o.class, classLoader);
                a(SDKFactory.class, classLoader);
                a(UCCyclone.class, classLoader);
                a(br.class, classLoader);
                a(com.uc.webview.export.internal.uc.wa.a.class, classLoader);
                a(IWaStat.class, classLoader);
                a(k.class, classLoader);
                d.a().a("gk_preload_cl", true);
            } catch (Throwable th) {
                th.printStackTrace();
            } finally {
                b.a(521);
            }
        }
    }
}
