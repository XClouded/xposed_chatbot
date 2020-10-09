package com.facebook.soloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import dalvik.system.BaseDexClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public class SoLoader {
    static final boolean DEBUG = false;
    public static final int SOLOADER_ALLOW_ASYNC_INIT = 2;
    public static final int SOLOADER_ENABLE_EXOPACKAGE = 1;
    public static final int SOLOADER_LOOK_IN_ZIP = 4;
    private static String SO_STORE_NAME_MAIN = "lib-main";
    static final boolean SYSTRACE_LIBRARY_LOADING;
    static final String TAG = "SoLoader";
    private static int sFlags;
    private static final Set<String> sLoadedLibraries = new HashSet();
    private static final Map<String, Object> sLoadingLibraries = new HashMap();
    @Nullable
    static SoFileLoader sSoFileLoader;
    @Nullable
    private static SoSource[] sSoSources;
    @Nullable
    private static SystemLoadLibraryWrapper sSystemLoadLibraryWrapper = null;

    static {
        boolean z = false;
        try {
            if (Build.VERSION.SDK_INT >= 18) {
                z = true;
            }
        } catch (NoClassDefFoundError | UnsatisfiedLinkError unused) {
        }
        SYSTRACE_LIBRARY_LOADING = z;
    }

    public static void init(Context context, int i) throws IOException {
        init(context, i, (SoFileLoader) null);
    }

    public static void init(Context context, int i, @Nullable SoFileLoader soFileLoader) throws IOException {
        StrictMode.ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        try {
            initImpl(context, i, soFileLoader);
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
        }
    }

    public static void init(Context context, boolean z) {
        try {
            init(context, z ? 1 : 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static synchronized void initImpl(Context context, int i, @Nullable SoFileLoader soFileLoader) throws IOException {
        synchronized (SoLoader.class) {
            if (sSoSources == null) {
                Log.d(TAG, "init start");
                sFlags = i;
                initSoLoader(soFileLoader);
                ArrayList arrayList = new ArrayList();
                String str = System.getenv("LD_LIBRARY_PATH");
                if (str == null) {
                    str = "/vendor/lib:/system/lib";
                }
                String[] split = str.split(":");
                for (String file : split) {
                    arrayList.add(new DirectorySoSource(new File(file), 2));
                }
                if (context != null) {
                    int i2 = 1;
                    if ((i & 1) != 0) {
                        arrayList.add(0, new ExoSoSource(context, SO_STORE_NAME_MAIN));
                    } else {
                        ApplicationInfo applicationInfo = context.getApplicationInfo();
                        if ((applicationInfo.flags & 1) != 0 && (applicationInfo.flags & 128) == 0) {
                            i2 = 0;
                        } else {
                            arrayList.add(0, new DirectorySoSource(new File(applicationInfo.nativeLibraryDir), Build.VERSION.SDK_INT <= 17 ? 1 : 0));
                        }
                        arrayList.add(0, new ApkSoSource(context, SO_STORE_NAME_MAIN, i2));
                    }
                }
                SoSource[] soSourceArr = (SoSource[]) arrayList.toArray(new SoSource[arrayList.size()]);
                int makePrepareFlags = makePrepareFlags();
                int length = soSourceArr.length;
                while (true) {
                    int i3 = length - 1;
                    if (length <= 0) {
                        break;
                    }
                    Log.d(TAG, "Preparing SO source: " + soSourceArr[i3]);
                    soSourceArr[i3].prepare(makePrepareFlags);
                    length = i3;
                }
                sSoSources = soSourceArr;
                Log.d(TAG, "init finish: " + sSoSources.length + " SO sources prepared");
            }
        }
    }

    private static int makePrepareFlags() {
        return (sFlags & 2) != 0 ? 1 : 0;
    }

    private static synchronized void initSoLoader(@Nullable SoFileLoader soFileLoader) {
        synchronized (SoLoader.class) {
            if (soFileLoader != null) {
                sSoFileLoader = soFileLoader;
                return;
            }
            final Runtime runtime = Runtime.getRuntime();
            final Method nativeLoadRuntimeMethod = getNativeLoadRuntimeMethod();
            final boolean z = nativeLoadRuntimeMethod != null;
            final String classLoaderLdLoadLibrary = z ? Api14Utils.getClassLoaderLdLoadLibrary() : null;
            final String makeNonZipPath = makeNonZipPath(classLoaderLdLoadLibrary);
            sSoFileLoader = new SoFileLoader() {
                public void load(String str, int i) {
                    if (z) {
                        String str2 = (i & 4) == 4 ? classLoaderLdLoadLibrary : makeNonZipPath;
                        try {
                            synchronized (runtime) {
                                String str3 = (String) nativeLoadRuntimeMethod.invoke(runtime, new Object[]{str, SoLoader.class.getClassLoader(), str2});
                                if (str3 != null) {
                                    Log.e(SoLoader.TAG, "Error when loading lib: " + str3);
                                    throw new UnsatisfiedLinkError(str3);
                                }
                            }
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            String str4 = "Error: Cannot load " + str;
                            Log.e(SoLoader.TAG, str4);
                            throw new RuntimeException(str4, e);
                        }
                    } else {
                        System.load(str);
                    }
                }
            };
        }
    }

    @Nullable
    private static Method getNativeLoadRuntimeMethod() {
        if (Build.VERSION.SDK_INT < 23) {
            return null;
        }
        try {
            Method declaredMethod = Runtime.class.getDeclaredMethod("nativeLoad", new Class[]{String.class, ClassLoader.class, String.class});
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (NoSuchMethodException | SecurityException e) {
            Log.w(TAG, "Cannot get nativeLoad method", e);
            return null;
        }
    }

    public static void setInTestMode() {
        setSoSources(new SoSource[]{new NoopSoSource()});
    }

    public static void deinitForTest() {
        setSoSources((SoSource[]) null);
    }

    public static synchronized void setSoSources(SoSource[] soSourceArr) {
        synchronized (SoLoader.class) {
            sSoSources = soSourceArr;
        }
    }

    public static void setSoFileLoader(SoFileLoader soFileLoader) {
        sSoFileLoader = soFileLoader;
    }

    public static synchronized void resetStatus() {
        synchronized (SoLoader.class) {
            sLoadedLibraries.clear();
            sLoadingLibraries.clear();
            sSoFileLoader = null;
            sSoSources = null;
        }
    }

    public static void setSystemLoadLibraryWrapper(SystemLoadLibraryWrapper systemLoadLibraryWrapper) {
        sSystemLoadLibraryWrapper = systemLoadLibraryWrapper;
    }

    public static final class WrongAbiError extends UnsatisfiedLinkError {
        WrongAbiError(Throwable th) {
            super("APK was built for a different platform");
            initCause(th);
        }
    }

    public static void loadLibrary(String str) {
        loadLibrary(str, 0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0027, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0029, code lost:
        r0 = com.facebook.soloader.MergedSoMapping.mapLibName(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002d, code lost:
        if (r0 == null) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0031, code lost:
        r1 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0032, code lost:
        loadLibraryBySoName(java.lang.System.mapLibraryName(r1), r3, r0, r4, (android.os.StrictMode.ThreadPolicy) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void loadLibrary(java.lang.String r3, int r4) throws java.lang.UnsatisfiedLinkError {
        /*
            java.lang.Class<com.facebook.soloader.SoLoader> r0 = com.facebook.soloader.SoLoader.class
            monitor-enter(r0)
            com.facebook.soloader.SoSource[] r1 = sSoSources     // Catch:{ all -> 0x003b }
            if (r1 != 0) goto L_0x0028
            java.lang.String r1 = "http://www.android.com/"
            java.lang.String r2 = "java.vendor.url"
            java.lang.String r2 = java.lang.System.getProperty(r2)     // Catch:{ all -> 0x003b }
            boolean r1 = r1.equals(r2)     // Catch:{ all -> 0x003b }
            if (r1 == 0) goto L_0x0019
            assertInitialized()     // Catch:{ all -> 0x003b }
            goto L_0x0028
        L_0x0019:
            com.facebook.soloader.SystemLoadLibraryWrapper r4 = sSystemLoadLibraryWrapper     // Catch:{ all -> 0x003b }
            if (r4 == 0) goto L_0x0023
            com.facebook.soloader.SystemLoadLibraryWrapper r4 = sSystemLoadLibraryWrapper     // Catch:{ all -> 0x003b }
            r4.loadLibrary(r3)     // Catch:{ all -> 0x003b }
            goto L_0x0026
        L_0x0023:
            java.lang.System.loadLibrary(r3)     // Catch:{ all -> 0x003b }
        L_0x0026:
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            return
        L_0x0028:
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            java.lang.String r0 = com.facebook.soloader.MergedSoMapping.mapLibName(r3)
            if (r0 == 0) goto L_0x0031
            r1 = r0
            goto L_0x0032
        L_0x0031:
            r1 = r3
        L_0x0032:
            java.lang.String r1 = java.lang.System.mapLibraryName(r1)
            r2 = 0
            loadLibraryBySoName(r1, r3, r0, r4, r2)
            return
        L_0x003b:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.SoLoader.loadLibrary(java.lang.String, int):void");
    }

    static void loadLibraryBySoName(String str, int i, StrictMode.ThreadPolicy threadPolicy) {
        loadLibraryBySoName(str, (String) null, (String) null, i, threadPolicy);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002e, code lost:
        if (r1 != false) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0030, code lost:
        r0 = com.facebook.soloader.SoLoader.class;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        monitor-enter(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0039, code lost:
        if (sLoadedLibraries.contains(r5) == false) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003b, code lost:
        if (r7 != null) goto L_0x0040;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003d, code lost:
        monitor-exit(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x003f, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0040, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        monitor-exit(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0042, code lost:
        if (r1 != false) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        android.util.Log.d(TAG, "About to load: " + r5);
        doLoadLibraryBySoName(r5, r8, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x005d, code lost:
        r8 = com.facebook.soloader.SoLoader.class;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        android.util.Log.d(TAG, "Loaded: " + r5);
        sLoadedLibraries.add(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x007b, code lost:
        monitor-exit(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0080, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0081, code lost:
        r6 = r5.getMessage();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0085, code lost:
        if (r6 == null) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0094, code lost:
        throw new com.facebook.soloader.SoLoader.WrongAbiError(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0095, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0096, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x009c, code lost:
        throw new java.lang.RuntimeException(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00a0, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00a2, code lost:
        if (r7 == null) goto L_0x00f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00a6, code lost:
        if (SYSTRACE_LIBRARY_LOADING == false) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00a8, code lost:
        com.facebook.soloader.Api18TraceUtils.beginTraceSection("MergedSoMapping.invokeJniOnload[" + r6 + com.taobao.weex.el.parse.Operators.ARRAY_END_STR);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        android.util.Log.d(TAG, "About to merge: " + r6 + " / " + r5);
        com.facebook.soloader.MergedSoMapping.invokeJniOnload(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00e4, code lost:
        if (SYSTRACE_LIBRARY_LOADING == false) goto L_0x00f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00e6, code lost:
        com.facebook.soloader.Api18TraceUtils.endSection();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x00ea, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x00ed, code lost:
        if (SYSTRACE_LIBRARY_LOADING != false) goto L_0x00ef;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x00ef, code lost:
        com.facebook.soloader.Api18TraceUtils.endSection();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x00f2, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x00f3, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x00f4, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x00f6, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void loadLibraryBySoName(java.lang.String r5, java.lang.String r6, java.lang.String r7, int r8, android.os.StrictMode.ThreadPolicy r9) {
        /*
            java.lang.Class<com.facebook.soloader.SoLoader> r0 = com.facebook.soloader.SoLoader.class
            monitor-enter(r0)
            java.util.Set<java.lang.String> r1 = sLoadedLibraries     // Catch:{ all -> 0x00f7 }
            boolean r1 = r1.contains(r5)     // Catch:{ all -> 0x00f7 }
            r2 = 1
            if (r1 == 0) goto L_0x0012
            if (r7 != 0) goto L_0x0010
            monitor-exit(r0)     // Catch:{ all -> 0x00f7 }
            return
        L_0x0010:
            r1 = 1
            goto L_0x0013
        L_0x0012:
            r1 = 0
        L_0x0013:
            java.util.Map<java.lang.String, java.lang.Object> r3 = sLoadingLibraries     // Catch:{ all -> 0x00f7 }
            boolean r3 = r3.containsKey(r5)     // Catch:{ all -> 0x00f7 }
            if (r3 == 0) goto L_0x0022
            java.util.Map<java.lang.String, java.lang.Object> r3 = sLoadingLibraries     // Catch:{ all -> 0x00f7 }
            java.lang.Object r3 = r3.get(r5)     // Catch:{ all -> 0x00f7 }
            goto L_0x002c
        L_0x0022:
            java.lang.Object r3 = new java.lang.Object     // Catch:{ all -> 0x00f7 }
            r3.<init>()     // Catch:{ all -> 0x00f7 }
            java.util.Map<java.lang.String, java.lang.Object> r4 = sLoadingLibraries     // Catch:{ all -> 0x00f7 }
            r4.put(r5, r3)     // Catch:{ all -> 0x00f7 }
        L_0x002c:
            monitor-exit(r0)     // Catch:{ all -> 0x00f7 }
            monitor-enter(r3)
            if (r1 != 0) goto L_0x00a2
            java.lang.Class<com.facebook.soloader.SoLoader> r0 = com.facebook.soloader.SoLoader.class
            monitor-enter(r0)     // Catch:{ all -> 0x00a0 }
            java.util.Set<java.lang.String> r4 = sLoadedLibraries     // Catch:{ all -> 0x009d }
            boolean r4 = r4.contains(r5)     // Catch:{ all -> 0x009d }
            if (r4 == 0) goto L_0x0041
            if (r7 != 0) goto L_0x0040
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            monitor-exit(r3)     // Catch:{ all -> 0x00a0 }
            return
        L_0x0040:
            r1 = 1
        L_0x0041:
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            if (r1 != 0) goto L_0x00a2
            java.lang.String r0 = "SoLoader"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0096, UnsatisfiedLinkError -> 0x0080 }
            r1.<init>()     // Catch:{ IOException -> 0x0096, UnsatisfiedLinkError -> 0x0080 }
            java.lang.String r2 = "About to load: "
            r1.append(r2)     // Catch:{ IOException -> 0x0096, UnsatisfiedLinkError -> 0x0080 }
            r1.append(r5)     // Catch:{ IOException -> 0x0096, UnsatisfiedLinkError -> 0x0080 }
            java.lang.String r1 = r1.toString()     // Catch:{ IOException -> 0x0096, UnsatisfiedLinkError -> 0x0080 }
            android.util.Log.d(r0, r1)     // Catch:{ IOException -> 0x0096, UnsatisfiedLinkError -> 0x0080 }
            doLoadLibraryBySoName(r5, r8, r9)     // Catch:{ IOException -> 0x0096, UnsatisfiedLinkError -> 0x0080 }
            java.lang.Class<com.facebook.soloader.SoLoader> r8 = com.facebook.soloader.SoLoader.class
            monitor-enter(r8)     // Catch:{ all -> 0x00a0 }
            java.lang.String r9 = "SoLoader"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x007d }
            r0.<init>()     // Catch:{ all -> 0x007d }
            java.lang.String r1 = "Loaded: "
            r0.append(r1)     // Catch:{ all -> 0x007d }
            r0.append(r5)     // Catch:{ all -> 0x007d }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x007d }
            android.util.Log.d(r9, r0)     // Catch:{ all -> 0x007d }
            java.util.Set<java.lang.String> r9 = sLoadedLibraries     // Catch:{ all -> 0x007d }
            r9.add(r5)     // Catch:{ all -> 0x007d }
            monitor-exit(r8)     // Catch:{ all -> 0x007d }
            goto L_0x00a2
        L_0x007d:
            r5 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x007d }
            throw r5     // Catch:{ all -> 0x00a0 }
        L_0x0080:
            r5 = move-exception
            java.lang.String r6 = r5.getMessage()     // Catch:{ all -> 0x00a0 }
            if (r6 == 0) goto L_0x0095
            java.lang.String r7 = "unexpected e_machine:"
            boolean r6 = r6.contains(r7)     // Catch:{ all -> 0x00a0 }
            if (r6 == 0) goto L_0x0095
            com.facebook.soloader.SoLoader$WrongAbiError r6 = new com.facebook.soloader.SoLoader$WrongAbiError     // Catch:{ all -> 0x00a0 }
            r6.<init>(r5)     // Catch:{ all -> 0x00a0 }
            throw r6     // Catch:{ all -> 0x00a0 }
        L_0x0095:
            throw r5     // Catch:{ all -> 0x00a0 }
        L_0x0096:
            r5 = move-exception
            java.lang.RuntimeException r6 = new java.lang.RuntimeException     // Catch:{ all -> 0x00a0 }
            r6.<init>(r5)     // Catch:{ all -> 0x00a0 }
            throw r6     // Catch:{ all -> 0x00a0 }
        L_0x009d:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            throw r5     // Catch:{ all -> 0x00a0 }
        L_0x00a0:
            r5 = move-exception
            goto L_0x00f5
        L_0x00a2:
            if (r7 == 0) goto L_0x00f3
            boolean r7 = SYSTRACE_LIBRARY_LOADING     // Catch:{ all -> 0x00a0 }
            if (r7 == 0) goto L_0x00c1
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a0 }
            r7.<init>()     // Catch:{ all -> 0x00a0 }
            java.lang.String r8 = "MergedSoMapping.invokeJniOnload["
            r7.append(r8)     // Catch:{ all -> 0x00a0 }
            r7.append(r6)     // Catch:{ all -> 0x00a0 }
            java.lang.String r8 = "]"
            r7.append(r8)     // Catch:{ all -> 0x00a0 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x00a0 }
            com.facebook.soloader.Api18TraceUtils.beginTraceSection(r7)     // Catch:{ all -> 0x00a0 }
        L_0x00c1:
            java.lang.String r7 = "SoLoader"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r8.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r9 = "About to merge: "
            r8.append(r9)     // Catch:{ all -> 0x00ea }
            r8.append(r6)     // Catch:{ all -> 0x00ea }
            java.lang.String r9 = " / "
            r8.append(r9)     // Catch:{ all -> 0x00ea }
            r8.append(r5)     // Catch:{ all -> 0x00ea }
            java.lang.String r5 = r8.toString()     // Catch:{ all -> 0x00ea }
            android.util.Log.d(r7, r5)     // Catch:{ all -> 0x00ea }
            com.facebook.soloader.MergedSoMapping.invokeJniOnload(r6)     // Catch:{ all -> 0x00ea }
            boolean r5 = SYSTRACE_LIBRARY_LOADING     // Catch:{ all -> 0x00a0 }
            if (r5 == 0) goto L_0x00f3
            com.facebook.soloader.Api18TraceUtils.endSection()     // Catch:{ all -> 0x00a0 }
            goto L_0x00f3
        L_0x00ea:
            r5 = move-exception
            boolean r6 = SYSTRACE_LIBRARY_LOADING     // Catch:{ all -> 0x00a0 }
            if (r6 == 0) goto L_0x00f2
            com.facebook.soloader.Api18TraceUtils.endSection()     // Catch:{ all -> 0x00a0 }
        L_0x00f2:
            throw r5     // Catch:{ all -> 0x00a0 }
        L_0x00f3:
            monitor-exit(r3)     // Catch:{ all -> 0x00a0 }
            return
        L_0x00f5:
            monitor-exit(r3)     // Catch:{ all -> 0x00a0 }
            throw r5
        L_0x00f7:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00f7 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.SoLoader.loadLibraryBySoName(java.lang.String, java.lang.String, java.lang.String, int, android.os.StrictMode$ThreadPolicy):void");
    }

    public static File unpackLibraryAndDependencies(String str) throws UnsatisfiedLinkError {
        assertInitialized();
        try {
            return unpackLibraryBySoName(System.mapLibraryName(str));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00da  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void doLoadLibraryBySoName(java.lang.String r8, int r9, android.os.StrictMode.ThreadPolicy r10) throws java.io.IOException {
        /*
            java.lang.Class<com.facebook.soloader.SoLoader> r0 = com.facebook.soloader.SoLoader.class
            monitor-enter(r0)
            com.facebook.soloader.SoSource[] r1 = sSoSources     // Catch:{ all -> 0x0149 }
            if (r1 == 0) goto L_0x0117
            com.facebook.soloader.SoSource[] r1 = sSoSources     // Catch:{ all -> 0x0149 }
            int r1 = r1.length     // Catch:{ all -> 0x0149 }
            com.facebook.soloader.SoSource[] r1 = new com.facebook.soloader.SoSource[r1]     // Catch:{ all -> 0x0149 }
            com.facebook.soloader.SoSource[] r2 = sSoSources     // Catch:{ all -> 0x0149 }
            com.facebook.soloader.SoSource[] r3 = sSoSources     // Catch:{ all -> 0x0149 }
            int r3 = r3.length     // Catch:{ all -> 0x0149 }
            r4 = 0
            java.lang.System.arraycopy(r2, r4, r1, r4, r3)     // Catch:{ all -> 0x0149 }
            monitor-exit(r0)     // Catch:{ all -> 0x0149 }
            if (r10 != 0) goto L_0x001e
            android.os.StrictMode$ThreadPolicy r10 = android.os.StrictMode.allowThreadDiskReads()
            r0 = 1
            goto L_0x001f
        L_0x001e:
            r0 = 0
        L_0x001f:
            boolean r2 = SYSTRACE_LIBRARY_LOADING
            if (r2 == 0) goto L_0x003c
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "SoLoader.loadLibrary["
            r2.append(r3)
            r2.append(r8)
            java.lang.String r3 = "]"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.facebook.soloader.Api18TraceUtils.beginTraceSection(r2)
        L_0x003c:
            r2 = 0
        L_0x003d:
            if (r4 != 0) goto L_0x00db
            int r3 = r1.length     // Catch:{ all -> 0x009d }
            if (r2 >= r3) goto L_0x00db
            r3 = r1[r2]     // Catch:{ all -> 0x009d }
            int r3 = r3.loadLibrary(r8, r9, r10)     // Catch:{ all -> 0x009d }
            if (r3 != 0) goto L_0x0075
            java.lang.String r4 = "SoLoader"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0073 }
            r5.<init>()     // Catch:{ all -> 0x0073 }
            java.lang.String r6 = "Result "
            r5.append(r6)     // Catch:{ all -> 0x0073 }
            r5.append(r3)     // Catch:{ all -> 0x0073 }
            java.lang.String r6 = " for "
            r5.append(r6)     // Catch:{ all -> 0x0073 }
            r5.append(r8)     // Catch:{ all -> 0x0073 }
            java.lang.String r6 = " in source "
            r5.append(r6)     // Catch:{ all -> 0x0073 }
            r6 = r1[r2]     // Catch:{ all -> 0x0073 }
            r5.append(r6)     // Catch:{ all -> 0x0073 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0073 }
            android.util.Log.d(r4, r5)     // Catch:{ all -> 0x0073 }
            goto L_0x0075
        L_0x0073:
            r9 = move-exception
            goto L_0x009f
        L_0x0075:
            r4 = r1[r2]     // Catch:{ all -> 0x0073 }
            boolean r4 = r4 instanceof com.facebook.soloader.ExtractFromZipSoSource     // Catch:{ all -> 0x0073 }
            if (r4 == 0) goto L_0x0099
            r4 = r1[r2]     // Catch:{ all -> 0x0073 }
            com.facebook.soloader.ExtractFromZipSoSource r4 = (com.facebook.soloader.ExtractFromZipSoSource) r4     // Catch:{ all -> 0x0073 }
            java.lang.String r5 = "SoLoader"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0073 }
            r6.<init>()     // Catch:{ all -> 0x0073 }
            java.lang.String r7 = "Extraction logs: "
            r6.append(r7)     // Catch:{ all -> 0x0073 }
            java.lang.String r4 = r4.getExtractLogs(r8)     // Catch:{ all -> 0x0073 }
            r6.append(r4)     // Catch:{ all -> 0x0073 }
            java.lang.String r4 = r6.toString()     // Catch:{ all -> 0x0073 }
            android.util.Log.d(r5, r4)     // Catch:{ all -> 0x0073 }
        L_0x0099:
            int r2 = r2 + 1
            r4 = r3
            goto L_0x003d
        L_0x009d:
            r9 = move-exception
            r3 = r4
        L_0x009f:
            boolean r1 = SYSTRACE_LIBRARY_LOADING
            if (r1 == 0) goto L_0x00a6
            com.facebook.soloader.Api18TraceUtils.endSection()
        L_0x00a6:
            if (r0 == 0) goto L_0x00ab
            android.os.StrictMode.setThreadPolicy(r10)
        L_0x00ab:
            if (r3 != 0) goto L_0x00da
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "Could not load: "
            r9.append(r10)
            r9.append(r8)
            java.lang.String r9 = r9.toString()
            java.lang.String r10 = "SoLoader"
            android.util.Log.e(r10, r9)
            java.lang.UnsatisfiedLinkError r9 = new java.lang.UnsatisfiedLinkError
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r0 = "couldn't find DSO to load: "
            r10.append(r0)
            r10.append(r8)
            java.lang.String r8 = r10.toString()
            r9.<init>(r8)
            throw r9
        L_0x00da:
            throw r9
        L_0x00db:
            boolean r9 = SYSTRACE_LIBRARY_LOADING
            if (r9 == 0) goto L_0x00e2
            com.facebook.soloader.Api18TraceUtils.endSection()
        L_0x00e2:
            if (r0 == 0) goto L_0x00e7
            android.os.StrictMode.setThreadPolicy(r10)
        L_0x00e7:
            if (r4 == 0) goto L_0x00ea
            return
        L_0x00ea:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "Could not load: "
            r9.append(r10)
            r9.append(r8)
            java.lang.String r9 = r9.toString()
            java.lang.String r10 = "SoLoader"
            android.util.Log.e(r10, r9)
            java.lang.UnsatisfiedLinkError r9 = new java.lang.UnsatisfiedLinkError
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r0 = "couldn't find DSO to load: "
            r10.append(r0)
            r10.append(r8)
            java.lang.String r8 = r10.toString()
            r9.<init>(r8)
            throw r9
        L_0x0117:
            java.lang.String r9 = "SoLoader"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0149 }
            r10.<init>()     // Catch:{ all -> 0x0149 }
            java.lang.String r1 = "Could not load: "
            r10.append(r1)     // Catch:{ all -> 0x0149 }
            r10.append(r8)     // Catch:{ all -> 0x0149 }
            java.lang.String r1 = " because no SO source exists"
            r10.append(r1)     // Catch:{ all -> 0x0149 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0149 }
            android.util.Log.e(r9, r10)     // Catch:{ all -> 0x0149 }
            java.lang.UnsatisfiedLinkError r9 = new java.lang.UnsatisfiedLinkError     // Catch:{ all -> 0x0149 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0149 }
            r10.<init>()     // Catch:{ all -> 0x0149 }
            java.lang.String r1 = "couldn't find DSO to load: "
            r10.append(r1)     // Catch:{ all -> 0x0149 }
            r10.append(r8)     // Catch:{ all -> 0x0149 }
            java.lang.String r8 = r10.toString()     // Catch:{ all -> 0x0149 }
            r9.<init>(r8)     // Catch:{ all -> 0x0149 }
            throw r9     // Catch:{ all -> 0x0149 }
        L_0x0149:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0149 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.SoLoader.doLoadLibraryBySoName(java.lang.String, int, android.os.StrictMode$ThreadPolicy):void");
    }

    @Nullable
    public static String makeNonZipPath(String str) {
        if (str == null) {
            return null;
        }
        String[] split = str.split(":");
        ArrayList arrayList = new ArrayList(split.length);
        for (String str2 : split) {
            if (!str2.contains(Operators.AND_NOT)) {
                arrayList.add(str2);
            }
        }
        return TextUtils.join(":", arrayList);
    }

    public static Set<String> getLoadedLibrariesNames() {
        return sLoadedLibraries;
    }

    static File unpackLibraryBySoName(String str) throws IOException {
        for (SoSource unpackLibrary : sSoSources) {
            File unpackLibrary2 = unpackLibrary.unpackLibrary(str);
            if (unpackLibrary2 != null) {
                return unpackLibrary2;
            }
        }
        throw new FileNotFoundException(str);
    }

    private static void assertInitialized() {
        if (sSoSources == null) {
            throw new RuntimeException("SoLoader.init() not yet called");
        }
    }

    public static synchronized void prependSoSource(SoSource soSource) throws IOException {
        synchronized (SoLoader.class) {
            Log.d(TAG, "Prepending to SO sources: " + soSource);
            assertInitialized();
            soSource.prepare(makePrepareFlags());
            SoSource[] soSourceArr = new SoSource[(sSoSources.length + 1)];
            soSourceArr[0] = soSource;
            System.arraycopy(sSoSources, 0, soSourceArr, 1, sSoSources.length);
            sSoSources = soSourceArr;
            Log.d(TAG, "Prepended to SO sources: " + soSource);
        }
    }

    public static synchronized String makeLdLibraryPath() {
        String join;
        synchronized (SoLoader.class) {
            assertInitialized();
            Log.d(TAG, "makeLdLibraryPath");
            ArrayList arrayList = new ArrayList();
            SoSource[] soSourceArr = sSoSources;
            for (SoSource addToLdLibraryPath : soSourceArr) {
                addToLdLibraryPath.addToLdLibraryPath(arrayList);
            }
            join = TextUtils.join(":", arrayList);
            Log.d(TAG, "makeLdLibraryPath final path: " + join);
        }
        return join;
    }

    public static boolean areSoSourcesAbisSupported() {
        SoSource[] soSourceArr = sSoSources;
        if (soSourceArr == null) {
            return false;
        }
        String[] supportedAbis = SysUtil.getSupportedAbis();
        for (SoSource soSourceAbis : soSourceArr) {
            String[] soSourceAbis2 = soSourceAbis.getSoSourceAbis();
            for (int i = 0; i < soSourceAbis2.length; i++) {
                boolean z = false;
                for (int i2 = 0; i2 < supportedAbis.length && !z; i2++) {
                    z = soSourceAbis2[i].equals(supportedAbis[i2]);
                }
                if (!z) {
                    return false;
                }
            }
        }
        return true;
    }

    @DoNotOptimize
    @TargetApi(14)
    private static class Api14Utils {
        private Api14Utils() {
        }

        public static String getClassLoaderLdLoadLibrary() {
            ClassLoader classLoader = SoLoader.class.getClassLoader();
            if (classLoader instanceof BaseDexClassLoader) {
                try {
                    return (String) BaseDexClassLoader.class.getMethod("getLdLibraryPath", new Class[0]).invoke((BaseDexClassLoader) classLoader, new Object[0]);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot call getLdLibraryPath", e);
                }
            } else {
                throw new IllegalStateException("ClassLoader " + classLoader.getClass().getName() + " should be of type BaseDexClassLoader");
            }
        }
    }
}
