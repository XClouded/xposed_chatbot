package com.uc.webview.export.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.accs.common.Constants;
import com.taobao.android.dxcontainer.DXContainerErrorConstant;
import com.uc.webview.export.Build;
import com.uc.webview.export.WebResourceResponse;
import com.uc.webview.export.WebView;
import com.uc.webview.export.annotations.Reflection;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.extension.InitCallback;
import com.uc.webview.export.extension.NotAvailableUCListener;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.extension.UCExtension;
import com.uc.webview.export.internal.android.CookieManagerAndroid;
import com.uc.webview.export.internal.android.WebViewAndroid;
import com.uc.webview.export.internal.android.f;
import com.uc.webview.export.internal.android.q;
import com.uc.webview.export.internal.android.u;
import com.uc.webview.export.internal.interfaces.CommonDef;
import com.uc.webview.export.internal.interfaces.ICookieManager;
import com.uc.webview.export.internal.interfaces.IGeolocationPermissions;
import com.uc.webview.export.internal.interfaces.IGlobalSettings;
import com.uc.webview.export.internal.interfaces.IMimeTypeMap;
import com.uc.webview.export.internal.interfaces.IPreloadManager;
import com.uc.webview.export.internal.interfaces.IServiceWorkerController;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.interfaces.IWebStorage;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;
import com.uc.webview.export.internal.setup.UCMRunningInfo;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.UCSetupTask;
import com.uc.webview.export.internal.setup.ay;
import com.uc.webview.export.internal.setup.br;
import com.uc.webview.export.internal.setup.l;
import com.uc.webview.export.internal.uc.CoreFactory;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import com.uc.webview.export.internal.utility.d;
import com.uc.webview.export.internal.utility.i;
import com.uc.webview.export.internal.utility.k;
import com.uc.webview.export.utility.SetupTask;
import com.uc.webview.export.utility.download.UpdateTask;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/* compiled from: U4Source */
public final class SDKFactory {
    private static AbstractWebViewFactory A = new a((byte) 0);
    private static final Object B = new Object();
    private static boolean C = false;
    private static boolean D = false;
    public static NotAvailableUCListener a = null;
    public static ValueCallback<Pair<String, HashMap<String, String>>> b = null;
    public static ClassLoader c = SDKFactory.class.getClassLoader();
    public static UCMobileWebKit d = null;
    public static Context e = null;
    public static boolean f = false;
    public static String g = null;
    @Reflection
    public static final int getCoreType = 10020;
    @Reflection
    public static final int getGlobalSettings = 10022;
    public static int h = 0;
    @Reflection
    public static final int handlePerformanceTests = 10030;
    public static InitCallback i = null;
    @Reflection
    public static final int isInited = 10010;
    public static boolean j = false;
    public static boolean k = false;
    public static String l = null;
    public static ValueCallback<String> m = null;
    public static ValueCallback<String> n = null;
    public static ValueCallback<UCSetupException> o = null;
    public static long p = 0;
    public static Map<String, Integer> q = new HashMap();
    public static String r = null;
    public static SetupTask s = null;
    @Reflection
    public static final int setBrowserFlag = 10009;
    @Reflection
    public static final int setCoreType = 10021;
    @Reflection
    public static final int setPreloadManager = 10059;
    @Reflection
    public static final int setWebViewFactory = 10008;
    static boolean t = false;
    static boolean u = false;
    static boolean v = false;
    static boolean w = false;
    /* access modifiers changed from: private */
    public static int x = 0;
    private static IGlobalSettings y = null;
    private static IPreloadManager z = null;

    @Reflection
    public static Object invoke(int i2, Object... objArr) {
        if (i2 == 10030 || i2 == 10059) {
            return null;
        }
        switch (i2) {
            case 10008:
            case 10009:
                return null;
            case isInited /*10010*/:
                return Boolean.valueOf(b());
            default:
                switch (i2) {
                    case getCoreType /*10020*/:
                        return Integer.valueOf(d());
                    case setCoreType /*10021*/:
                        f(objArr[0].intValue());
                        return null;
                    case getGlobalSettings /*10022*/:
                        return e();
                    default:
                        return null;
                }
        }
    }

    public static void a(Long l2) {
        p |= l2.longValue();
    }

    public static void b(Long l2) {
        p &= l2.longValue() ^ -1;
    }

    public static Boolean c(Long l2) {
        return Boolean.valueOf((p & l2.longValue()) != 0);
    }

    public static Boolean a() {
        return Boolean.valueOf(k);
    }

    public static boolean b() {
        return x != 0;
    }

    public static String c() {
        if (!b()) {
            throw new RuntimeException("UC WebView Sdk not inited.");
        } else if (d() == 2) {
            return "System WebView";
        } else {
            return g;
        }
    }

    public static IWebView a(Context context, AttributeSet attributeSet, WebView webView, boolean z2, int[] iArr) {
        if (e == null) {
            e = context.getApplicationContext();
        }
        if (f) {
            g();
        }
        if (b != null) {
            b.onReceiveValue(new Pair(IWaStat.WV_NEW_BEFORE, (Object) null));
        }
        IWebView createWebView = A.createWebView(context, attributeSet, webView, z2, iArr);
        UCMRunningInfo totalLoadedUCM = UCSetupTask.getTotalLoadedUCM();
        if (totalLoadedUCM != null) {
            try {
                if (totalLoadedUCM.coreType != 2) {
                    if (!D) {
                        D = true;
                        i.a((Runnable) new b(context));
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        if (b != null) {
            b.onReceiveValue(new Pair(IWaStat.WV_NEW_AFTER, (Object) null));
        }
        IWaStat.WaStat.upload();
        return createWebView;
    }

    public static File a(Context context) {
        File a2 = k.a(context, Constants.KEY_FLAGS);
        String str = (String) UCCore.getGlobalOption(UCCore.PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION);
        if (k.a(str)) {
            str = "0";
        }
        return new File(a2, UCCyclone.getSourceHash("flag_new_webview") + "_" + str);
    }

    public static void b(Context context) {
        try {
            File a2 = a(context);
            if (a2.exists()) {
                a2.delete();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static UCExtension a(Context context, IWebView iWebView, int i2) {
        if (e == null) {
            e = context.getApplicationContext();
        }
        g();
        if (i2 == 2) {
            return null;
        }
        return new UCExtension(iWebView);
    }

    public static a a(int i2, Context context) {
        if (e == null) {
            e = context.getApplicationContext();
        }
        g();
        if (i2 == 2) {
            return new u();
        }
        return new com.uc.webview.export.internal.uc.b(context);
    }

    public static IWebStorage a(int i2) {
        g();
        if (i2 == 2) {
            return new q();
        }
        return CoreFactory.d();
    }

    public static ICookieManager b(int i2) {
        g();
        if (i2 == 2) {
            return new CookieManagerAndroid();
        }
        return CoreFactory.getCookieManager();
    }

    public static IServiceWorkerController c(int i2) {
        g();
        if (i2 == 2) {
            return null;
        }
        return CoreFactory.getServiceWorkerController();
    }

    public static IGeolocationPermissions d(int i2) {
        g();
        if (i2 == 2) {
            return new com.uc.webview.export.internal.android.b();
        }
        return CoreFactory.c();
    }

    public static IMimeTypeMap e(int i2) {
        g();
        if (i2 == 2) {
            return new f();
        }
        return CoreFactory.e();
    }

    public static int d() {
        g();
        if (x == 0) {
            return 1;
        }
        return x;
    }

    public static void f(int i2) {
        if (i2 != 0) {
            x = i2;
            f();
            UCLogger create = UCLogger.create(UploadQueueMgr.MSGTYPE_INTERVAL, "SDKFactory");
            if (create != null) {
                create.print("setCoreType: type=" + i2, new Throwable[0]);
            }
        }
    }

    public static IGlobalSettings e() {
        if (y != null) {
            return y;
        }
        if (d() == 2) {
            return null;
        }
        IGlobalSettings b2 = CoreFactory.b();
        y = b2;
        return b2;
    }

    public static void f() {
        synchronized (B) {
            try {
                B.notifyAll();
            } catch (Exception e2) {
                Log.i("tag_test_log", "releaseLock", e2);
            }
        }
    }

    public static void a(Runnable runnable) {
        b.a(runnable);
    }

    public static void c(Context context) {
        if (context != null) {
            if (e == null) {
                e = context.getApplicationContext();
            }
            if (!C) {
                com.uc.webview.export.internal.uc.wa.a.a(context);
                C = true;
            }
        }
    }

    public static void g() {
        if (!w || Looper.myLooper() != Looper.getMainLooper()) {
            if (!(b() && b.a.isEmpty())) {
                if (f && !b() && i != null) {
                    i.notInit();
                }
                if (!f || !Build.IS_INTERNATIONAL_VERSION) {
                    if (!j && s != null) {
                        s.start();
                    }
                    p();
                }
            }
        }
    }

    public static void a(String str) {
        if (d() != 2) {
            CoreFactory.a(str);
        }
    }

    public static Pair<Long, Long> h() {
        UCMobileWebKit uCMobileWebKit;
        if (d() == 2) {
            return null;
        }
        if (d() == 2) {
            uCMobileWebKit = null;
        } else {
            uCMobileWebKit = CoreFactory.getUCMobileWebKit();
        }
        try {
            return new Pair<>(Long.valueOf(uCMobileWebKit.getClass().getField("sTrafficSent").getLong(uCMobileWebKit)), Long.valueOf(uCMobileWebKit.getClass().getField("sTrafficReceived").getLong(uCMobileWebKit)));
        } catch (Throwable th) {
            Log.d("tag_test_log", "getTraffic", th);
            return null;
        }
    }

    public static WebResourceResponse b(String str) {
        if (d() == 2) {
            return null;
        }
        try {
            return CoreFactory.b(str);
        } catch (Throwable unused) {
            throw new RuntimeException("The getResponseByUrl() is not support in this version.");
        }
    }

    public static void i() {
        IGlobalSettings e2;
        boolean booleanValue = com.uc.webview.export.internal.cd.a.c("apollo").booleanValue();
        if (!booleanValue) {
            a(Long.valueOf(PlaybackStateCompat.ACTION_SET_REPEAT_MODE));
        } else {
            b(Long.valueOf(PlaybackStateCompat.ACTION_SET_REPEAT_MODE));
        }
        if (b() && (e2 = e()) != null) {
            if (!booleanValue) {
                Log.i("ucmedia.SDKFactory", "sdk cd forbid apollo");
                e2.setStringValue("sdk_apollo_forbid", "1");
                return;
            }
            e2.setStringValue("sdk_apollo_forbid", "0");
        }
    }

    public static void a(Map<String, Object> map) {
        if (map != null) {
            Object obj = map.get(UCCore.OPTION_UC_PLAYER_ROOT);
            if (obj != null) {
                d.a().a(UCCore.OPTION_UC_PLAYER_ROOT, obj.toString());
            }
            boolean z2 = map.get(UCCore.OPTION_USE_UC_PLAYER);
            d a2 = d.a();
            if (z2 == null) {
                z2 = true;
            }
            a2.a(UCCore.OPTION_USE_UC_PLAYER, z2);
        }
        Log.d("ucmedia.SDKFactory", "sUseUCPlayer:" + d.a().b(UCCore.OPTION_USE_UC_PLAYER) + ",ucPlayerSoDir:" + d.a().a(UCCore.OPTION_UC_PLAYER_ROOT));
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:28:0x0086 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void d(android.content.Context r6) {
        /*
            android.content.Context r0 = e
            if (r0 != 0) goto L_0x000a
            android.content.Context r0 = r6.getApplicationContext()
            e = r0
        L_0x000a:
            com.uc.webview.export.internal.utility.d r0 = com.uc.webview.export.internal.utility.d.a()
            java.lang.String r1 = "ucPlayer"
            boolean r0 = r0.b(r1)
            if (r0 == 0) goto L_0x00b8
            boolean r0 = t
            if (r0 != 0) goto L_0x00b8
            boolean r0 = b()
            if (r0 == 0) goto L_0x00b8
            int r0 = x
            r1 = 2
            if (r0 == r1) goto L_0x00b8
            java.io.File r6 = e((android.content.Context) r6)
            r0 = 0
            if (r6 == 0) goto L_0x00b8
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = r6.getAbsolutePath()
            r1.append(r2)
            java.lang.String r2 = "/"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "ucmedia.SDKFactory"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "ucPlayerDir:"
            r3.<init>(r4)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x009a }
            java.lang.String r3 = "so_dir: "
            r2.<init>(r3)     // Catch:{ Throwable -> 0x009a }
            r2.append(r1)     // Catch:{ Throwable -> 0x009a }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x009a }
            java.lang.String r3 = "crsp_mpplgs"
            com.uc.webview.export.extension.UCSettings.setGlobalStringValue(r3, r2)     // Catch:{ Throwable -> 0x009a }
            java.io.File r2 = new java.io.File     // Catch:{ Throwable -> 0x0086, all -> 0x008a }
            java.lang.String r3 = ".lock"
            r2.<init>(r6, r3)     // Catch:{ Throwable -> 0x0086, all -> 0x008a }
            boolean r6 = r2.exists()     // Catch:{ Throwable -> 0x0086, all -> 0x008a }
            if (r6 != 0) goto L_0x0086
            java.io.FileWriter r6 = new java.io.FileWriter     // Catch:{ Throwable -> 0x0086, all -> 0x008a }
            r6.<init>(r2)     // Catch:{ Throwable -> 0x0086, all -> 0x008a }
            java.lang.String r0 = "2.6.0.167"
            r6.write(r0)     // Catch:{ Throwable -> 0x0085, all -> 0x0080 }
            r0 = r6
            goto L_0x0086
        L_0x0080:
            r0 = move-exception
            r5 = r0
            r0 = r6
            r6 = r5
            goto L_0x008b
        L_0x0085:
            r0 = r6
        L_0x0086:
            com.uc.webview.export.cyclone.UCCyclone.close(r0)     // Catch:{ Throwable -> 0x009a }
            goto L_0x008f
        L_0x008a:
            r6 = move-exception
        L_0x008b:
            com.uc.webview.export.cyclone.UCCyclone.close(r0)     // Catch:{ Throwable -> 0x009a }
            throw r6     // Catch:{ Throwable -> 0x009a }
        L_0x008f:
            l = r1     // Catch:{ Throwable -> 0x009a }
            r6 = 1
            t = r6     // Catch:{ Throwable -> 0x009a }
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            return
        L_0x0098:
            r6 = move-exception
            goto L_0x00b4
        L_0x009a:
            r6 = move-exception
            java.lang.String r1 = "ucmedia.SDKFactory"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0098 }
            java.lang.String r3 = "setupForUCPlayer: "
            r2.<init>(r3)     // Catch:{ all -> 0x0098 }
            java.lang.ClassLoader r3 = c     // Catch:{ all -> 0x0098 }
            r2.append(r3)     // Catch:{ all -> 0x0098 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0098 }
            android.util.Log.e(r1, r2, r6)     // Catch:{ all -> 0x0098 }
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            return
        L_0x00b4:
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            throw r6
        L_0x00b8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.SDKFactory.d(android.content.Context):void");
    }

    public static File e(Context context) {
        if (e == null) {
            e = context.getApplicationContext();
        }
        try {
            File[] fileArr = {null};
            File uCPlayerRoot = UpdateTask.getUCPlayerRoot(context);
            String str = context.getApplicationContext().getApplicationInfo().nativeLibraryDir;
            if (str != null && str.equals(uCPlayerRoot.getAbsolutePath())) {
                uCPlayerRoot = k.a(e, "rep_apollo");
                k.a(new File(str, "libffmpeg.so"), new File(uCPlayerRoot, "libffmpeg.so"), new File(uCPlayerRoot, "libffmpeg.so"));
                k.a(new File(str, "libu3player.so"), new File(uCPlayerRoot, "libu3player.so"), new File(uCPlayerRoot, "libu3player.so"));
                k.a(new File(str, "librotate.so"), new File(uCPlayerRoot, "librotate.so"), new File(uCPlayerRoot, "librotate.so"));
                k.a(new File(str, "libinitHelper.so"), new File(uCPlayerRoot, "libinitHelper.so"), new File(uCPlayerRoot, "libinitHelper.so"));
            }
            a(uCPlayerRoot, fileArr);
            if (fileArr[0] == null) {
                return null;
            }
            return fileArr[0];
        } catch (Throwable th) {
            android.util.Log.e("ucmedia.SDKFactory", "getUCPlayerDir", th);
            return null;
        }
    }

    private static void a(File file, File[] fileArr) {
        if (file.exists() && file.isDirectory()) {
            File file2 = new File(file, "libu3player.so");
            if (file2.exists() && UpdateTask.isFinished(file, "libu3player.so") && (fileArr[0] == null || file2.lastModified() > new File(fileArr[0], "libu3player.so").lastModified())) {
                fileArr[0] = file;
            }
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file3 : listFiles) {
                    if (file3.isDirectory()) {
                        a(file3, fileArr);
                    }
                }
            }
        }
    }

    public static boolean j() {
        if (d() == 2) {
            return true;
        }
        return CoreFactory.f();
    }

    private static void p() {
        int i2;
        int i3;
        if (!u || Looper.myLooper() != Looper.getMainLooper()) {
            Integer num = (Integer) UCCore.getGlobalOption(UCCore.OPTION_WEBVIEW_POLICY);
            if (num == null) {
                i2 = 1;
            } else {
                i2 = num.intValue();
            }
            Integer num2 = (Integer) UCCore.getGlobalOption(UCCore.OPTION_WEBVIEW_POLICY_WAIT_MILLIS);
            if (num2 == null) {
                i3 = 4000;
            } else {
                i3 = num2.intValue();
            }
            UCElapseTime uCElapseTime = new UCElapseTime();
            while (true) {
                int i4 = 3;
                if (!SetupTask.isSetupThread() || b()) {
                    b.a((Runnable) null);
                    if (j) {
                        SetupTask.resumeAll();
                    }
                    if (x != 0) {
                        break;
                    }
                    synchronized (B) {
                        try {
                            B.wait(200);
                        } catch (Exception e2) {
                            Log.i("tag_test_log", "getLock", e2);
                        }
                    }
                    if (uCElapseTime.getMilis() >= ((long) i3) && i2 != 1) {
                        break;
                    }
                } else {
                    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                    StringBuilder sb = new StringBuilder();
                    while (i4 < stackTrace.length && i4 < 8) {
                        sb.append(stackTrace[i4].toString().replace("com.uc.webview.export.", "").replaceAll("\\(.+\\)", ""));
                        sb.append(",");
                        i4++;
                    }
                    throw new UCSetupException(3013, sb.toString());
                }
            }
            b.a((Runnable) null);
            synchronized (SDKFactory.class) {
                if (x == 0) {
                    if (i2 == 2) {
                        x = 2;
                    } else if (i2 == 3) {
                        throw new UCSetupException((int) DXContainerErrorConstant.DX_CONTAINER_ERROR_UPDATE_MODEL_ENGINE_NULL, String.format("Thread [%s] waitting for init is up to [%s] milis.", new Object[]{Thread.currentThread().getName(), String.valueOf(i3)}));
                    }
                }
                Log.d("SDKFactory", String.format(Locale.CHINA, "waitForInit(sWebViewPolicy=%d, sMaxWaitMillis=%d)=%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(x)}));
            }
        }
    }

    public static void k() {
        if (x == 2) {
            ReflectionUtil.invokeNoThrow("android.webkit.WebView", "enableSlowWholeDocumentDraw");
        } else if (x == 3) {
            ReflectionUtil.invokeNoThrow("com.uc.webkit.WebView", "enableSlowWholeDocumentDraw");
        }
    }

    public static String f(Context context) {
        if (x == 2) {
            try {
                return (String) ReflectionUtil.invokeNoThrow("android.webkit.WebSettings", "getDefaultUserAgent", new Class[]{Context.class}, new Object[]{context});
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        } else if (x != 3) {
            return "";
        } else {
            try {
                return (String) ReflectionUtil.invokeNoThrow("com.uc.webkit.WebSettings", "getDefaultUserAgent", new Class[]{Context.class}, new Object[]{context});
            } catch (Exception e3) {
                e3.printStackTrace();
                return null;
            }
        }
    }

    public static IPreloadManager l() {
        if (!(z == null || e == null)) {
            z.setContext(e);
        }
        return z;
    }

    /* compiled from: U4Source */
    static class b extends Handler {
        /* access modifiers changed from: private */
        public static final ConcurrentLinkedQueue<Runnable> a = new ConcurrentLinkedQueue<>();
        /* access modifiers changed from: private */
        public static UCSetupException b = null;
        private static final Runnable c = new c();

        private b(Looper looper) {
            super(looper);
        }

        public final void handleMessage(Message message) {
            if (message.what == 0 && SDKFactory.a != null) {
                SDKFactory.a.onNotAvailableUC(message.arg1);
                SDKFactory.a = null;
            }
        }

        static void a(Runnable runnable) {
            if (runnable != null) {
                a.add(runnable);
                new b(Looper.getMainLooper()).post(c);
            }
            if (SDKFactory.n()) {
                if (b == null) {
                    c.run();
                }
                if (b != null) {
                    throw b;
                }
            }
        }
    }

    /* compiled from: U4Source */
    static class a extends AbstractWebViewFactory {
        private a() {
        }

        /* synthetic */ a(byte b) {
            this();
        }

        public final IWebView createWebView(Context context, AttributeSet attributeSet, WebView webView, boolean z, int[] iArr) {
            int i;
            if (SDKFactory.e == null) {
                SDKFactory.e = context.getApplicationContext();
            }
            SDKFactory.h(context);
            if (CommonDef.sOnCreateWindowType == 1 || z) {
                i = 2;
            } else {
                i = CommonDef.sOnCreateWindowType == 2 ? CoreFactory.getCoreType().intValue() : SDKFactory.x;
            }
            Log.d("SDKFactory", String.format("createWebView(forceUsSystem=%b, sOnCreateWindowType=%d)=%d", new Object[]{Boolean.valueOf(z), Integer.valueOf(CommonDef.sOnCreateWindowType), Integer.valueOf(i)}));
            iArr[0] = i;
            if (i == 2) {
                return new WebViewAndroid(context, attributeSet, webView);
            }
            return CoreFactory.createWebView(context, attributeSet);
        }
    }

    public static String a(br brVar, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("Type:");
        sb.append(str);
        if (brVar.pkgName != null) {
            sb.append("\nPackage Name:");
            sb.append(brVar.pkgName);
        }
        sb.append("\nSo files path:");
        sb.append(brVar.soDirPath);
        sb.append("\nDex files:");
        if (brVar.sdkShellModule != null) {
            sb.append("\n");
            sb.append((String) brVar.sdkShellModule.first);
        }
        if (brVar.coreImplModule != null) {
            sb.append("\n");
            sb.append((String) brVar.coreImplModule.first);
        }
        return sb.toString();
    }

    public static void c(String str) {
        g = str;
    }

    public static void m() {
        IGlobalSettings e2 = e();
        if (e2 != null) {
            e2.setStringValue("apollo_str", "ap_cache3=0&ap_cache=0&ap_cache_preload=0&ap_enable_preload2=0&ap_enable_cache2=0");
        }
    }

    static /* synthetic */ void g(Context context) {
        try {
            a(context).createNewFile();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    static /* synthetic */ boolean n() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    static /* synthetic */ void h(Context context) {
        if (!v || Looper.myLooper() != Looper.getMainLooper()) {
            if (e == null) {
                e = context.getApplicationContext();
            }
            if (!f && !b()) {
                synchronized (SDKFactory.class) {
                    if (!j) {
                        if (s != null) {
                            s.start();
                        } else {
                            ((l) ((l) ((l) new ay().setup("CONTEXT", (Object) context.getApplicationContext())).setup(UCCore.OPTION_HARDWARE_ACCELERATED, (Object) "true")).setup(UCCore.OPTION_VIDEO_HARDWARE_ACCELERATED, (Object) "false")).start();
                        }
                    }
                }
                p();
            }
        }
    }
}
