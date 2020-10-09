package com.uc.sandboxExport;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.uc.sandboxExport.IChildProcessSetup;
import com.uc.sandboxExport.helper.a;
import dalvik.system.DexFile;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import kotlin.UByte;

@Api
/* compiled from: U4Source */
public class SandboxedProcessService extends Service {
    static final /* synthetic */ boolean a = (!SandboxedProcessService.class.desiredAssertionStatus());
    private final String b = "ProcessService.sandbox";
    /* access modifiers changed from: private */
    public String c = "ProcessService.sandbox";
    private boolean d;
    private boolean e = false;
    private Constructor<?> f;
    private Method g;
    private Method h;
    private Method i;
    /* access modifiers changed from: private */
    public IBinder j;
    private Object k;
    private Intent l;
    private final IChildProcessSetup.Stub m = new a(this);
    private String n;
    private int o;

    /* access modifiers changed from: protected */
    public void init(ParcelFileDescriptor parcelFileDescriptor, Parcelable[] parcelableArr, ParcelFileDescriptor parcelFileDescriptor2) {
        DexFileClassLoader dexFileClassLoader;
        Class<?> cls;
        DexFile dexFile;
        DexFileClassLoader dexFileClassLoader2;
        Class<?> cls2;
        Object invoke;
        Method method;
        ParcelFileDescriptor parcelFileDescriptor3 = parcelFileDescriptor2;
        if (!this.d) {
            this.e = a.a();
            if (a || parcelFileDescriptor3 != null) {
                Log.i(this.c, "initCrashSdkIfNeeded " + parcelFileDescriptor3);
                Class<?> cls3 = null;
                if (parcelFileDescriptor3 != null) {
                    try {
                        cls2 = Class.forName("com.uc.crashsdk.export.CrashApi");
                        if (!(cls2 == null || (invoke = cls2.getDeclaredMethod("getInstance", new Class[0]).invoke((Object) null, new Object[0])) == null)) {
                            method = cls2.getDeclaredMethod("setHostFd", new Class[]{ParcelFileDescriptor.class});
                            method.invoke(invoke, new Object[]{parcelFileDescriptor3});
                        }
                    } catch (Exception unused) {
                        method = cls2.getDeclaredMethod("setIsolatedHostFd", new Class[]{ParcelFileDescriptor.class});
                    } catch (Throwable th) {
                        Log.e(this.c, "initCrashSdkIfNeeded: init crashsdk failed.", th);
                    }
                }
                String stringExtra = this.l.getStringExtra("dex.path");
                String stringExtra2 = this.l.getStringExtra("odex.path");
                String stringExtra3 = this.l.getStringExtra("lib.path");
                String stringExtra4 = this.l.getStringExtra("source.dir");
                String stringExtra5 = this.l.getStringExtra("source.dir.prior");
                a(Switches.ENABLE_RENDERER_DEBUG_LOG);
                if (stringExtra == null || stringExtra.length() == 0) {
                    dexFileClassLoader = null;
                } else {
                    try {
                        dexFile = (!this.e || stringExtra5 == null || !new File(stringExtra5).exists()) ? null : new DexFile(stringExtra5);
                    } catch (Throwable unused2) {
                        dexFile = null;
                    }
                    try {
                        dexFileClassLoader2 = new DexFileClassLoader(stringExtra, stringExtra2, stringExtra3, getClass().getClassLoader(), parcelFileDescriptor, dexFile == null ? stringExtra4 : stringExtra5, dexFile);
                    } catch (Throwable th2) {
                        a("new DexFileClassLoader failed.", th2);
                        dexFileClassLoader2 = null;
                    }
                    if (parcelFileDescriptor != null && this.e) {
                        this.l.putExtra("isolated", true);
                    }
                    dexFileClassLoader = dexFileClassLoader2;
                }
                String stringExtra6 = this.l.getStringExtra("org.chromium.base.process_launcher.info.core.version");
                String stringExtra7 = this.l.getStringExtra("org.chromium.base.process_launcher.info.sdk.version");
                String a2 = a((ClassLoader) dexFileClassLoader, "webviewSdkVersion");
                String a3 = a((ClassLoader) dexFileClassLoader, "coreVersion");
                Log.e(this.c, "main process version: " + stringExtra7 + AVFSCacheConstants.COMMA_SEP + stringExtra6);
                Log.e(this.c, "this process version: " + a2 + AVFSCacheConstants.COMMA_SEP + a3);
                if (dexFileClassLoader != null) {
                    try {
                        cls = Class.forName("org.chromium.content.app.SandboxedProcessService0", false, dexFileClassLoader);
                    } catch (Throwable th3) {
                        if (dexFileClassLoader == null) {
                            a("Class.forName(org.chromium.content.app.SandboxedProcessService0) failed.", th3);
                        } else {
                            a("Class.forName(org.chromium.content.app.SandboxedProcessService0, " + dexFileClassLoader + ") failed.", th3);
                        }
                    }
                } else {
                    cls = Class.forName("org.chromium.content.app.SandboxedProcessService0");
                }
                cls3 = cls;
                try {
                    this.f = cls3.getDeclaredConstructor(new Class[0]);
                    this.f.setAccessible(true);
                } catch (Throwable th4) {
                    a("initServiceClassIfNeeded: getDeclaredConstructor failed.", th4);
                }
                try {
                    this.g = cls3.getMethod("onDestroy", new Class[0]);
                    this.g.setAccessible(true);
                } catch (Throwable th5) {
                    a("initServiceClassIfNeeded: getMethod onDestroy failed.", th5);
                }
                try {
                    this.i = cls3.getDeclaredMethod("initializeEngine", new Class[]{Class.forName("[Landroid.os.ParcelFileDescriptor;")});
                    this.i.setAccessible(true);
                } catch (Throwable th6) {
                    a("initServiceClassIfNeeded: getDeclaredMethod mInitializeMethod failed.", th6);
                }
                try {
                    this.h = cls3.getDeclaredMethod("onBind", new Class[]{Class.forName("android.content.Intent")});
                    this.h.setAccessible(true);
                } catch (Throwable th7) {
                    a("initServiceClassIfNeeded: getDeclaredMethod onBind failed.", th7);
                }
                if (this.f != null) {
                    try {
                        this.k = this.f.newInstance(new Object[0]);
                    } catch (Exception e2) {
                        a("initServiceClassIfNeeded: new SandboxedProcessService failed.", (Throwable) e2);
                    }
                }
                try {
                    Log.e(this.c, "attachSandboxedProcessService begin");
                    Class<?> cls4 = Class.forName("android.app.Service");
                    Field declaredField = cls4.getDeclaredField("mThread");
                    declaredField.setAccessible(true);
                    Field declaredField2 = cls4.getDeclaredField("mClassName");
                    declaredField2.setAccessible(true);
                    Field declaredField3 = cls4.getDeclaredField("mToken");
                    declaredField3.setAccessible(true);
                    Field declaredField4 = cls4.getDeclaredField("mApplication");
                    declaredField4.setAccessible(true);
                    Field declaredField5 = cls4.getDeclaredField("mActivityManager");
                    declaredField5.setAccessible(true);
                    Field declaredField6 = Class.forName("android.content.ContextWrapper").getDeclaredField("mBase");
                    declaredField6.setAccessible(true);
                    cls4.getDeclaredMethod("attach", new Class[]{Context.class, Class.forName("android.app.ActivityThread"), String.class, IBinder.class, Application.class, Object.class}).invoke(this.k, new Object[]{declaredField6.get(this), declaredField.get(this), declaredField2.get(this), declaredField3.get(this), declaredField4.get(this), declaredField5.get(this)});
                    Log.e(this.c, "attachSandboxedProcessService finish");
                    Log.w(this.c, "attachSandboxedProcessService: attach service success.");
                } catch (Exception e3) {
                    a("attachSandboxedProcessService: attach service failed.", (Throwable) e3);
                }
                if (!(this.k == null || this.h == null)) {
                    try {
                        this.j = (IBinder) this.h.invoke(this.k, new Object[]{this.l});
                    } catch (Exception e4) {
                        a("doInitService: invoke onBind failed.", (Throwable) e4);
                    }
                }
                if (!(this.k == null || this.i == null)) {
                    try {
                        this.i.invoke(this.k, new Object[]{parcelableArr});
                    } catch (Exception e5) {
                        a("doInitService: invoke initialize failed.", (Throwable) e5);
                    }
                }
                Log.e(this.c, "doInit: init success.");
                this.d = true;
                return;
            }
            throw new AssertionError();
        }
    }

    private void a(String str, Throwable th) {
        a(true);
        Log.e(this.c, str, th);
        throw new Error(str, th);
    }

    public void onCreate() {
        super.onCreate();
        Log.e(this.c, "SandboxedProcessService.onCreate");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00da, code lost:
        if (r7.c.indexOf("." + r1 + com.taobao.weex.el.parse.Operators.ARRAY_END_STR) == -1) goto L_0x00dc;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.os.IBinder onBind(android.content.Intent r8) {
        /*
            r7 = this;
            r7.l = r8
            java.lang.String r0 = "org.chromium.base.process_launcher.enable.seccomp"
            r1 = 0
            boolean r0 = r8.getBooleanExtra(r0, r1)
            java.lang.String r2 = "org.chromium.base.process_launcher.proc_type"
            java.lang.String r2 = r8.getStringExtra(r2)
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 == 0) goto L_0x0017
            java.lang.String r2 = "Render"
        L_0x0017:
            java.lang.String r3 = "org.chromium.base.process_launcher.browser_proc_name"
            java.lang.String r3 = r8.getStringExtra(r3)
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 == 0) goto L_0x0025
            java.lang.String r3 = "Unknown"
        L_0x0025:
            java.lang.String r4 = "org.chromium.base.process_launcher.browser_proc_pid"
            int r1 = r8.getIntExtra(r4, r1)
            java.lang.String r4 = "ProcessService.sandbox"
            java.lang.String r5 = r7.c
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L_0x00b8
            r7.n = r3
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "["
            r4.<init>(r5)
            r4.append(r2)
            java.lang.String r4 = r4.toString()
            java.lang.String r5 = "GPU"
            boolean r2 = r5.equals(r2)
            if (r2 != 0) goto L_0x008b
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r4)
            java.lang.String r4 = "."
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            boolean r4 = com.uc.sandboxExport.helper.a.a()
            if (r4 == 0) goto L_0x0070
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r2)
            java.lang.String r2 = "I"
            r4 = r0
            goto L_0x0084
        L_0x0070:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r2)
            java.lang.String r2 = "N"
            r4.append(r2)
            if (r0 == 0) goto L_0x0082
            java.lang.String r2 = "S"
            goto L_0x0084
        L_0x0082:
            java.lang.String r2 = "N"
        L_0x0084:
            r4.append(r2)
            java.lang.String r4 = r4.toString()
        L_0x008b:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r4)
            java.lang.String r2 = ".Svc."
            r0.append(r2)
            r0.append(r1)
            java.lang.String r1 = "]"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = r7.c
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r7.c = r0
            goto L_0x010e
        L_0x00b8:
            java.lang.String r0 = r7.c
            int r0 = r0.indexOf(r2)
            r4 = -1
            if (r0 == r4) goto L_0x00dc
            java.lang.String r0 = r7.c
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "."
            r5.<init>(r6)
            r5.append(r1)
            java.lang.String r6 = "]"
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            int r0 = r0.indexOf(r5)
            if (r0 != r4) goto L_0x010e
        L_0x00dc:
            java.lang.String r0 = r7.c
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = ".\n!!! "
            r4.<init>(r5)
            r4.append(r3)
            java.lang.String r5 = "["
            r4.append(r5)
            r4.append(r1)
            java.lang.String r1 = "] is crazy, it want me run as "
            r4.append(r1)
            r4.append(r2)
            java.lang.String r1 = ", I was started by "
            r4.append(r1)
            java.lang.String r1 = r7.n
            r4.append(r1)
            java.lang.String r1 = " !!!\n."
            r4.append(r1)
            java.lang.String r1 = r4.toString()
            android.util.Log.e(r0, r1)
        L_0x010e:
            int r0 = r7.o
            int r0 = r0 + 1
            r7.o = r0
            java.lang.String r0 = r7.c
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "onBind - "
            r1.<init>(r2)
            r1.append(r8)
            java.lang.String r8 = ", "
            r1.append(r8)
            r1.append(r3)
            java.lang.String r8 = ", call count "
            r1.append(r8)
            int r8 = r7.o
            r1.append(r8)
            java.lang.String r8 = r1.toString()
            android.util.Log.e(r0, r8)
            com.uc.sandboxExport.IChildProcessSetup$Stub r8 = r7.m
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.sandboxExport.SandboxedProcessService.onBind(android.content.Intent):android.os.IBinder");
    }

    public void onDestroy() {
        Log.e(this.c, "SandboxedProcessService.onDestroy");
        super.onDestroy();
        if (this.j != null) {
            if (this.g != null) {
                try {
                    this.g.invoke(this.k, new Object[0]);
                } catch (Throwable th) {
                    Log.e(this.c, "onDestroy: onDestroy failed.", th);
                }
            }
            this.k = null;
            this.j = null;
        }
    }

    private String a(ClassLoader classLoader, String str) {
        Class<?> cls;
        if (classLoader == null) {
            try {
                cls = Class.forName("org.chromium.base.utils.MiscUtil");
            } catch (Throwable th) {
                String str2 = this.c;
                Log.e(str2, "org.chromium.base.utils.MiscUtil " + classLoader, th);
                return "";
            }
        } else {
            cls = Class.forName("org.chromium.base.utils.MiscUtil", false, classLoader);
        }
        Method method = cls.getMethod(str, new Class[0]);
        method.setAccessible(true);
        return (String) method.invoke((Object) null, new Object[0]);
    }

    private void a(boolean z) {
        String stringExtra = this.l.getStringExtra("dex.path");
        String stringExtra2 = this.l.getStringExtra("odex.path");
        String stringExtra3 = this.l.getStringExtra("lib.path");
        String stringExtra4 = this.l.getStringExtra("source.dir");
        String stringExtra5 = this.l.getStringExtra("source.dir.prior");
        String str = this.c;
        Log.e(str, "core info:\n        dexPath: " + stringExtra + "\n       odexPath: " + stringExtra2 + "\n        libPath: " + stringExtra3 + "\n      sourceDir: " + stringExtra4 + "\n sourceDirPrior: " + stringExtra5);
        if (z) {
            a(stringExtra, stringExtra2, stringExtra3, stringExtra4, stringExtra5);
        }
    }

    private void a(String... strArr) {
        Log.e(this.c, "file info:");
        for (int i2 = 0; i2 < 5; i2++) {
            String str = strArr[i2];
            if (!TextUtils.isEmpty(str)) {
                try {
                    File file = new File(str);
                    if (!file.exists()) {
                        Log.e(this.c, String.format(" %s not exists!", new Object[]{str}));
                    } else if (file.isDirectory()) {
                        Log.e(this.c, String.format(" %s is directory", new Object[]{str}));
                    } else if (!file.isFile()) {
                        Log.e(this.c, String.format(" %s is not file", new Object[]{str}));
                    } else {
                        Log.e(this.c, String.format(" %s\n   size: %s, last modify type: %s", new Object[]{str, Long.toString(file.length()), a(file.lastModified())}));
                        MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
                        FileInputStream fileInputStream = new FileInputStream(file);
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int read = fileInputStream.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            instance.update(bArr, 0, read);
                        }
                        byte[] digest = instance.digest();
                        String str2 = this.c;
                        Log.e(str2, "    md5: " + a(digest));
                        fileInputStream.close();
                    }
                } catch (Throwable th) {
                    Log.e(this.c, String.format(" %s dump info exception", new Object[]{str}), th);
                }
            }
        }
    }

    private static String a(long j2) {
        try {
            return new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(new Date(j2));
        } catch (Throwable unused) {
            return "";
        }
    }

    private static String a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length);
        for (byte b2 : bArr) {
            stringBuffer.append(Integer.toString((b2 & UByte.MAX_VALUE) + 256, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
