package com.uc.crashsdk.export;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.webkit.ValueCallback;
import com.taobao.weex.el.parse.Operators;
import com.uc.crashsdk.CrashLogFilesUploader;
import com.uc.crashsdk.JNIBridge;
import com.uc.crashsdk.a.a;
import com.uc.crashsdk.a.g;
import com.uc.crashsdk.b;
import com.uc.crashsdk.d;
import com.uc.crashsdk.e;
import com.uc.crashsdk.h;
import com.uc.crashsdk.i;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
public class CrashApi {
    private static CrashApi a = null;
    private static boolean d = true;
    private static boolean e = false;
    private boolean b = false;
    private boolean c = false;

    public static synchronized CrashApi createInstance(Context context, CustomInfo customInfo, VersionInfo versionInfo, ICrashClient iCrashClient, String str, boolean z, boolean z2, boolean z3) {
        CrashApi crashApi;
        synchronized (CrashApi.class) {
            if (a == null) {
                a = new CrashApi(context, customInfo, versionInfo, iCrashClient, str, z, z2, z3);
            }
            crashApi = a;
        }
        return crashApi;
    }

    public static CrashApi createInstance(Context context, CustomInfo customInfo, VersionInfo versionInfo, ICrashClient iCrashClient, String str) {
        return createInstance(context, customInfo, versionInfo, iCrashClient, str, true, true, true);
    }

    public static CrashApi createInstance(Context context, CustomInfo customInfo, VersionInfo versionInfo, String str) {
        return createInstance(context, customInfo, versionInfo, (ICrashClient) null, str);
    }

    public static CrashApi createInstance(Context context, String str, Bundle bundle) {
        return createInstance(context, str, bundle, (ValueCallback<Bundle>) null, (ValueCallback<Bundle>) null);
    }

    public static CrashApi createInstance(Context context, String str, Bundle bundle, ValueCallback<Bundle> valueCallback, ValueCallback<Bundle> valueCallback2) {
        if (a != null) {
            return a;
        }
        if (bundle != null) {
            d = bundle.getBoolean("useApplicationContext", true);
            Context a2 = a(context);
            b(a2);
            CrashApi createInstance = createInstance(a2, h.a((CustomInfo) null, bundle), h.a(bundle), (ICrashClient) null, str, bundle.getBoolean("enableJavaLog", true), bundle.getBoolean("enableNativeLog", true), bundle.getBoolean("enableUnexpLog", b.A()));
            if (valueCallback != null) {
                d.d(valueCallback);
            }
            if (valueCallback2 != null) {
                d.a(valueCallback2);
            }
            String string = bundle.getString("soPathName");
            if (g.b(string) && e.e(string)) {
                createInstance.crashSoLoaded();
            }
            return createInstance;
        }
        throw new NullPointerException();
    }

    public static CrashApi createInstanceEx(Context context, String str, boolean z) {
        return createInstanceEx(context, str, z, (Bundle) null);
    }

    public static CrashApi createInstanceEx(Context context, String str, boolean z, Bundle bundle) {
        return createInstanceEx(context, str, z, bundle, (ICrashClient) null);
    }

    public static CrashApi createInstanceEx(Context context, String str, boolean z, Bundle bundle, ICrashClient iCrashClient) {
        if (a != null) {
            return a;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        d = bundle.getBoolean("useApplicationContext", true);
        Context a2 = a(context);
        b(a2);
        CustomInfo customInfo = new CustomInfo(str);
        customInfo.mEnableStatReport = true;
        customInfo.mZipLog = true;
        customInfo.mPrintStackInfos = z;
        CustomInfo a3 = h.a(customInfo, bundle);
        VersionInfo a4 = h.a(bundle);
        boolean z2 = bundle.getBoolean("enableJavaLog", true);
        boolean z3 = bundle.getBoolean("enableNativeLog", true);
        boolean z4 = bundle.getBoolean("enableUnexpLog", b.A());
        CrashApi createInstance = createInstance(a2, a3, a4, iCrashClient, e.d(a3.mIsInternational), z2, z3, z4);
        if (z3 || z4) {
            if (e.f("libcrashsdk.so")) {
                createInstance.crashSoLoaded();
            } else {
                a.c("crashsdk", "load libcrashsdk.so failed!");
            }
        }
        int i = bundle.getInt("uploadLogDelaySeconds", 15);
        if (i >= 0 && b.A()) {
            e.b(i);
        }
        return createInstance;
    }

    public boolean registerCallback(int i, ValueCallback<Bundle> valueCallback) {
        if (valueCallback != null) {
            switch (i) {
                case 1:
                    return d.a(valueCallback);
                case 2:
                    return d.c(valueCallback);
                case 3:
                    return d.d(valueCallback);
                case 4:
                    return d.b(valueCallback);
                default:
                    throw new IllegalArgumentException("Unknown event type: " + i);
            }
        } else {
            throw new NullPointerException();
        }
    }

    public int updateCustomInfo(CustomInfo customInfo) {
        if (customInfo != null) {
            return h.b(customInfo);
        }
        throw new NullPointerException();
    }

    public int updateCustomInfo(Bundle bundle) {
        if (bundle != null) {
            return updateCustomInfo(h.a((CustomInfo) null, bundle));
        }
        throw new NullPointerException();
    }

    public static CrashApi getInstance() {
        return a;
    }

    public void crashSoLoaded() {
        if (!a("crashSoLoaded")) {
            b.f = true;
            b();
            synchronized (b.e) {
                if (b.g) {
                    if (b.f) {
                        if (!b.c) {
                            if (!b.d) {
                                c();
                                h.e();
                            }
                            e.r();
                            b.c = true;
                        }
                    }
                }
            }
            com.uc.crashsdk.a.m();
            e.j();
        }
    }

    public void updateVersionInfo(VersionInfo versionInfo) {
        if (versionInfo != null) {
            h.a(versionInfo);
            return;
        }
        throw new NullPointerException();
    }

    public void updateVersionInfo(Bundle bundle) {
        if (bundle != null) {
            updateVersionInfo(h.a(bundle));
            return;
        }
        throw new NullPointerException();
    }

    public void disableLog(int i) {
        synchronized (b.e) {
            b.b(i);
            if (LogType.isForJava(i) && b.a) {
                e.p();
                b.a = false;
            }
            if (LogType.isForNative(i)) {
                if (b.b) {
                    JNIBridge.nativeUninstallBreakpad();
                    b.b = false;
                } else {
                    this.b = false;
                }
            }
            if (LogType.isForUnexp(i)) {
                if (!b.c) {
                    b.g = false;
                } else if (e.t()) {
                    b.c = false;
                }
            }
        }
    }

    public boolean addStatInfo(String str, String str2) {
        if (a("addStatInfo")) {
            return false;
        }
        if (g.a(str)) {
            throw new NullPointerException();
        } else if (str.length() <= 24) {
            if (str2 != null && str2.length() > 512) {
                str2 = str2.substring(0, 512);
            }
            return com.uc.crashsdk.a.h.a(str, str2);
        } else {
            throw new IllegalArgumentException("key is too long!");
        }
    }

    public void setCrashLogUploadUrl(String str) {
        if (!a("setCrashLogUploadUrl")) {
            e.a(str, false);
        }
    }

    public String getCrashLogUploadUrl() {
        if (a("getCrashLogUploadUrl")) {
            return null;
        }
        return e.i();
    }

    public int getLastExitType() {
        if (a("getLastExitType")) {
            return 1;
        }
        return b.B();
    }

    public int reportCrashStats(boolean z) {
        if (a("reportCrashStats")) {
            return 0;
        }
        return e.b(z, true);
    }

    public int resetCrashStats(boolean z) {
        if (a("resetCrashStats")) {
            return 0;
        }
        return e.e(z);
    }

    public void onExit() {
        b.t();
    }

    public void setNewInstall() {
        if (!a("setNewInstall")) {
            b.s();
        }
    }

    public void setForeground(boolean z) {
        b.a(z);
    }

    public void uploadCrashLogs() {
        if (!a("uploadCrashLogs")) {
            e.a(false, true);
        }
    }

    public int registerThread(int i, String str) {
        return com.uc.crashsdk.a.a(i, str);
    }

    public boolean generateCustomLog(CustomLogInfo customLogInfo) {
        StringBuilder sb;
        if (customLogInfo == null) {
            throw new NullPointerException();
        } else if (customLogInfo.mData == null || customLogInfo.mLogType == null) {
            throw new NullPointerException("mData or mLogType is null!");
        } else if (customLogInfo.mLogType.contains("_") || customLogInfo.mLogType.contains(Operators.SPACE_STR)) {
            throw new IllegalArgumentException("mLogType can not contain char '_' and ' '");
        } else {
            String str = null;
            if (customLogInfo.mDumpTids == null || customLogInfo.mDumpTids.size() <= 0) {
                sb = null;
            } else {
                sb = new StringBuilder();
                Iterator<Integer> it = customLogInfo.mDumpTids.iterator();
                while (it.hasNext()) {
                    sb.append(it.next().intValue());
                    sb.append(Operators.SPACE_STR);
                }
            }
            StringBuffer stringBuffer = customLogInfo.mData;
            String str2 = customLogInfo.mLogType;
            boolean z = customLogInfo.mAddHeader;
            boolean z2 = customLogInfo.mAddFooter;
            boolean z3 = customLogInfo.mAddLogcat;
            boolean z4 = customLogInfo.mAddThreadsDump;
            boolean z5 = customLogInfo.mUploadNow;
            ArrayList<String> arrayList = customLogInfo.mDumpFiles;
            ArrayList<String> arrayList2 = customLogInfo.mCallbacks;
            ArrayList<String> arrayList3 = customLogInfo.mCachedInfos;
            if (sb != null) {
                str = sb.toString();
            }
            return e.a(stringBuffer, str2, z, z2, z3, z4, z5, arrayList, arrayList2, arrayList3, str);
        }
    }

    public boolean generateCustomLog(StringBuffer stringBuffer, String str, Bundle bundle) {
        CustomLogInfo customLogInfo = new CustomLogInfo(stringBuffer, str);
        if (bundle != null) {
            customLogInfo.mAddHeader = bundle.getBoolean("mAddHeader", customLogInfo.mAddHeader);
            customLogInfo.mAddFooter = bundle.getBoolean("mAddFooter", customLogInfo.mAddFooter);
            customLogInfo.mAddLogcat = bundle.getBoolean("mAddLogcat", customLogInfo.mAddLogcat);
            customLogInfo.mUploadNow = bundle.getBoolean("mUploadNow", customLogInfo.mUploadNow);
            customLogInfo.mAddThreadsDump = bundle.getBoolean("mAddThreadsDump", customLogInfo.mAddThreadsDump);
            customLogInfo.mDumpFiles = bundle.getStringArrayList("mDumpFiles");
            customLogInfo.mCallbacks = bundle.getStringArrayList("mCallbacks");
            customLogInfo.mCachedInfos = bundle.getStringArrayList("mCachedInfos");
            customLogInfo.mDumpTids = bundle.getIntegerArrayList("mDumpTids");
        }
        return generateCustomLog(customLogInfo);
    }

    public void addHeaderInfo(String str, String str2) {
        if (str != null) {
            com.uc.crashsdk.a.a(str, str2);
            return;
        }
        throw new NullPointerException();
    }

    public int createCachedInfo(String str, int i, int i2) {
        if (str == null) {
            throw new NullPointerException();
        } else if (i <= 0) {
            throw new IllegalArgumentException("capacity must > 0!");
        } else if ((i2 & 273) == 0) {
            return 0;
        } else {
            return com.uc.crashsdk.a.a(str, i, i2);
        }
    }

    public int addCachedInfo(String str, String str2) {
        if (str != null && str2 != null) {
            return com.uc.crashsdk.a.b(str, str2);
        }
        throw new NullPointerException();
    }

    public int registerInfoCallback(String str, int i) {
        if (str == null) {
            throw new NullPointerException();
        } else if ((i & 273) == 0) {
            return 0;
        } else {
            return com.uc.crashsdk.a.a(str, i, (Callable<String>) null, 0, 0);
        }
    }

    public int registerInfoCallback(String str, int i, Callable<String> callable) {
        if (str == null || callable == null) {
            throw new NullPointerException();
        } else if ((i & 273) == 0) {
            return 0;
        } else {
            return com.uc.crashsdk.a.a(str, i, callable, 0, 0);
        }
    }

    public int addDumpFile(DumpFileInfo dumpFileInfo) {
        if (dumpFileInfo == null) {
            throw new NullPointerException();
        } else if (dumpFileInfo.mCategory == null || dumpFileInfo.mFileTobeDump == null) {
            throw new NullPointerException();
        } else if ((dumpFileInfo.mLogType & 273) == 0) {
            return 0;
        } else {
            return com.uc.crashsdk.a.a(dumpFileInfo.mCategory, dumpFileInfo.mFileTobeDump, dumpFileInfo.mIsEncrypted, dumpFileInfo.mWriteCategory, dumpFileInfo.mLogType, dumpFileInfo.mDeleteAfterDump);
        }
    }

    public int addDumpFile(String str, String str2, int i, Bundle bundle) {
        DumpFileInfo dumpFileInfo = new DumpFileInfo(str, str2, i);
        if (bundle != null) {
            dumpFileInfo.mIsEncrypted = bundle.getBoolean("mIsEncrypted", dumpFileInfo.mIsEncrypted);
            dumpFileInfo.mWriteCategory = bundle.getBoolean("mWriteCategory", dumpFileInfo.mWriteCategory);
            dumpFileInfo.mDeleteAfterDump = bundle.getBoolean("mDeleteAfterDump", dumpFileInfo.mDeleteAfterDump);
        }
        return addDumpFile(dumpFileInfo);
    }

    public Throwable getUncaughtException() {
        return e.q();
    }

    public boolean updateUnexpInfo() {
        if (a("updateUnexpInfo")) {
            return false;
        }
        return com.uc.crashsdk.a.a(true);
    }

    public ParcelFileDescriptor getHostFd() {
        return e.w();
    }

    public boolean setHostFd(ParcelFileDescriptor parcelFileDescriptor) {
        return e.a(parcelFileDescriptor);
    }

    public ParcelFileDescriptor getIsolatedHostFd() {
        return e.w();
    }

    public boolean setIsolatedHostFd(ParcelFileDescriptor parcelFileDescriptor) {
        return e.a(parcelFileDescriptor);
    }

    public boolean generateTraces(String str, long j) {
        if (a("generateTraces")) {
            return false;
        }
        if (b.d) {
            return JNIBridge.nativeGenerateTraces(str, j);
        }
        a.c("crashsdk", "Crash so is not loaded!");
        return false;
    }

    private CrashApi(Context context, CustomInfo customInfo, VersionInfo versionInfo, ICrashClient iCrashClient, String str, boolean z, boolean z2, boolean z3) {
        Context a2 = a(context);
        b(a2);
        this.b = z2;
        b.g = z3;
        if (b.D()) {
            b(a2);
            a(a2, customInfo, versionInfo, iCrashClient);
            if (z) {
                a();
            }
            if (this.b && e.f("libcrashsdk.so")) {
                b.f = true;
                b();
            }
        } else if (customInfo == null || versionInfo == null) {
            a.c("crashsdk", "VersionInfo and CustomInfo can not be null!");
            throw new NullPointerException();
        } else {
            h.a(customInfo);
            try {
                e.a(str, true);
                a(a2, customInfo, versionInfo, iCrashClient);
            } catch (Throwable th) {
                a(th);
            }
            if (z) {
                try {
                    a();
                } catch (Throwable th2) {
                    a(th2);
                }
            }
            try {
                i.a();
                com.uc.crashsdk.a.h.a();
                com.uc.crashsdk.a.d.a();
                g.i();
            } catch (Throwable th3) {
                g.a(th3);
            }
            try {
                if (!b.a(a2)) {
                    a.c("crashsdk", "registerLifecycleCallbacks failed!");
                }
            } catch (Throwable th4) {
                g.a(th4);
            }
            try {
                com.uc.crashsdk.a.m();
                e.u();
            } catch (Throwable th5) {
                g.a(th5);
            }
            e.v();
            try {
                if (h.s() && b.A() && !this.c) {
                    CrashLogFilesUploader.a(a2);
                    this.c = true;
                }
            } catch (Throwable th6) {
                g.b(th6);
            }
        }
    }

    private static void a() {
        if (b.a) {
            a.c("Has enabled java log!");
            return;
        }
        e.o();
        e.l();
        b.a = true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0030, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void b() {
        /*
            r3 = this;
            java.lang.Object r0 = com.uc.crashsdk.b.e
            monitor-enter(r0)
            boolean r1 = r3.b     // Catch:{ all -> 0x0031 }
            if (r1 == 0) goto L_0x002f
            boolean r1 = com.uc.crashsdk.b.f     // Catch:{ all -> 0x0031 }
            if (r1 != 0) goto L_0x000c
            goto L_0x002f
        L_0x000c:
            boolean r1 = com.uc.crashsdk.b.b     // Catch:{ all -> 0x0031 }
            if (r1 == 0) goto L_0x0017
            java.lang.String r1 = "Has enabled native log!"
            com.uc.crashsdk.a.a.c(r1)     // Catch:{ all -> 0x0031 }
            monitor-exit(r0)     // Catch:{ all -> 0x0031 }
            return
        L_0x0017:
            c()     // Catch:{ all -> 0x0031 }
            boolean r1 = com.uc.crashsdk.b.D()     // Catch:{ all -> 0x0031 }
            r2 = 0
            com.uc.crashsdk.JNIBridge.nativeInstallBreakpad(r1, r2)     // Catch:{ all -> 0x0031 }
            r1 = 1
            com.uc.crashsdk.b.b = r1     // Catch:{ all -> 0x0031 }
            java.lang.String r1 = android.os.Build.FINGERPRINT     // Catch:{ all -> 0x0031 }
            com.uc.crashsdk.JNIBridge.nativeBreakpadInited(r1)     // Catch:{ all -> 0x0031 }
            com.uc.crashsdk.h.e()     // Catch:{ all -> 0x0031 }
            monitor-exit(r0)     // Catch:{ all -> 0x0031 }
            return
        L_0x002f:
            monitor-exit(r0)     // Catch:{ all -> 0x0031 }
            return
        L_0x0031:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0031 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.export.CrashApi.b():void");
    }

    private static void a(Context context, CustomInfo customInfo, VersionInfo versionInfo, ICrashClient iCrashClient) {
        d.a(iCrashClient);
        h.a(customInfo, versionInfo);
        if (!b.D()) {
            e.m();
            e.a(context);
        }
    }

    private static Context a(Context context) {
        if (context == null) {
            a.c("crashsdk", "context can not be null!");
            throw new NullPointerException();
        } else if (!d || (context instanceof Application) || ((context = context.getApplicationContext()) != null && (context instanceof Application))) {
            return context;
        } else {
            a.c("crashsdk", "Can not get Application context from given context!");
            throw new IllegalArgumentException("Can not get Application context from given context!");
        }
    }

    private static void b(Context context) {
        try {
            if (!e) {
                g.a(context);
                com.uc.crashsdk.a.a = context.getPackageName();
                e = true;
            }
        } catch (Throwable th) {
            a(th);
        }
    }

    private static void c() {
        if (!b.d) {
            h.c();
            JNIBridge.nativeInitNative();
            h.d();
            b.d = true;
        }
    }

    private static void a(Throwable th) {
        new e().a(Thread.currentThread(), th, true);
    }

    private static boolean a(String str) {
        if (!b.D()) {
            return false;
        }
        a.c("crashsdk", "Can not call '" + str + "' in isolated process!");
        return true;
    }
}
