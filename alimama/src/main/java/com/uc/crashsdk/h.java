package com.uc.crashsdk;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import com.uc.crashsdk.a.g;
import com.uc.crashsdk.export.CustomInfo;
import com.uc.crashsdk.export.VersionInfo;
import java.io.File;
import java.lang.reflect.Field;

/* compiled from: ProGuard */
public class h {
    static final /* synthetic */ boolean a = (!h.class.desiredAssertionStatus());
    private static CustomInfo b = null;
    private static VersionInfo c = null;
    private static final Object d = new Object();
    private static String e = null;
    private static String f = null;
    private static String g = null;
    private static String h = null;
    private static final Object i = new Object();

    public static void a(CustomInfo customInfo, VersionInfo versionInfo) {
        CustomInfo customInfo2 = new CustomInfo(customInfo);
        b = customInfo2;
        c(customInfo2);
        c = new VersionInfo(versionInfo);
        if (!b.D()) {
            try {
                b();
            } catch (Throwable th) {
                g.a(th);
            }
        }
    }

    private static void c(CustomInfo customInfo) {
        if (customInfo.mZippedLogExtension == null) {
            customInfo.mZippedLogExtension = "";
        }
        if (customInfo.mOmitJavaCrash) {
            customInfo.mCallJavaDefaultHandler = false;
        }
        if (customInfo.mOmitNativeCrash) {
            customInfo.mCallNativeDefaultHandler = false;
        }
    }

    public static void a(CustomInfo customInfo) {
        if (!a && customInfo.mTagFilesFolderName == null) {
            throw new AssertionError();
        } else if (!a && customInfo.mCrashLogsFolderName == null) {
            throw new AssertionError();
        } else if (customInfo.mTagFilesFolderName.equals(customInfo.mCrashLogsFolderName)) {
            throw new IllegalArgumentException("mTagFilesFolderName and mCrashLogsFolderName can not be set to the same!");
        }
    }

    static boolean a() {
        return b != null;
    }

    public static void a(VersionInfo versionInfo) {
        synchronized (d) {
            c = new VersionInfo(versionInfo);
            e.b();
            if (b.d) {
                JNIBridge.nativeSetVersionInfo(P(), Q(), R(), "190401175529");
                JNIBridge.nativeUpdateCrashLogNames();
            }
        }
    }

    static void b() {
        b.v();
        b.u();
        if (b.mBackupLogs) {
            File file = new File(U());
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    private static void a(String str) {
        String a2 = e.a(str);
        JNIBridge.nativeSyncInfo("mLogTypeSuffix", a2, 0, 0);
        if (!e.x() && !b.D()) {
            e.b(a2);
        }
    }

    public static void c() {
        JNIBridge.nativeSetFolderNames(g.b(), b.mTagFilesFolderName, b.mCrashLogsFolderName, U());
        JNIBridge.nativeSetProcessNames(e.g(), b.a());
        JNIBridge.nativeSetVersionInfo(P(), Q(), R(), "190401175529");
        JNIBridge.nativeSetMobileInfo(Build.MODEL, Build.VERSION.RELEASE, e.n());
        JNIBridge.nativeSetLogStrategy(b.mCallNativeDefaultHandler, b.mDumpUserSolibBuildId, b.mReservedNativeMemoryBytes);
        V();
    }

    private static void V() {
        JNIBridge.nativeSetCrashLogFileNames(b.mNativeCrashLogFileName, b.mUnexpCrashLogFileName, b.mAppId);
    }

    public static void d() {
        W();
        X();
        Y();
        a(b.mLogTypeSuffix);
    }

    private static void W() {
        JNIBridge.nativeSetCrashCustoms(L(), b.mBackupLogs, b.mCrashRestartInterval, b.mMaxCrashLogFilesCount, b.mMaxNativeLogcatLineCount, b.mMaxUnexpLogcatLineCount, b.mOverrideLibcMalloc, K(), b.mIsUsedByUCM, Build.VERSION.SDK_INT, b.mOmitNativeCrash);
    }

    private static void X() {
        JNIBridge.nativeUpdateSignals(b.mDisableSignals, b.mDisableBackgroundSignals, 0);
    }

    private static void Y() {
        JNIBridge.nativeSetZip(b.mZipLog, b.mZippedLogExtension, b.mLogMaxBytesLimit);
    }

    public static void e() {
        ac();
        if (e.h()) {
            JNIBridge.nativeSetCrashLogFilesUploaded();
        }
        JNIBridge.nativeReserveFileHandle(b.mReservedNativeFileHandleCount, b.mFdDumpMinLimit);
        JNIBridge.nativeSetForeground(b.y());
        JNIBridge.nativeSetProcessType(b.A());
        a.d();
        a.f();
        a.h();
        a.j();
        JNIBridge.nativeSetPackageInfo(a.a, "", "");
        Z();
        JNIBridge.nativeSyncInfo("aver", a.a(), 0, 0);
        ab();
        b.C();
        g.j();
    }

    private static void Z() {
        if (b.d) {
            JNIBridge.nativeSyncInfo("thdump", (String) null, (long) b.mThreadsDumpMinLimit, 0);
        }
    }

    public static String f() {
        return b.mAppId;
    }

    static boolean g() {
        if (!g.b(b.mJavaCrashLogFileName) && !g.b(b.mNativeCrashLogFileName) && !g.b(b.mUnexpCrashLogFileName)) {
            return false;
        }
        return true;
    }

    static String h() {
        return b.mJavaCrashLogFileName;
    }

    static int i() {
        return b.mCrashRestartInterval;
    }

    static boolean j() {
        return b.mCallJavaDefaultHandler;
    }

    static boolean k() {
        return b.mDumpHprofDataForJavaOOM;
    }

    static boolean l() {
        return b.mRenameFileToDefaultName;
    }

    static int m() {
        return b.mMaxCrashLogFilesCount;
    }

    static int n() {
        return b.mMaxCustomLogFilesCount;
    }

    static int o() {
        return b.mMaxJavaLogcatLineCount;
    }

    static int p() {
        return b.mUnexpDelayMillSeconds;
    }

    static int q() {
        return b.mUnexpSubTypes;
    }

    static boolean r() {
        return b.mBackupLogs;
    }

    public static boolean s() {
        return b.mUploadUcebuCrashLog;
    }

    static boolean t() {
        return b.mSyncUploadSetupCrashLogs;
    }

    static boolean u() {
        return b.mOmitJavaCrash;
    }

    static boolean v() {
        return b.mAutoDeleteOldVersionStats;
    }

    static boolean w() {
        return b.mZipLog;
    }

    static String x() {
        return b.mZippedLogExtension;
    }

    static int y() {
        return b.mLogMaxBytesLimit;
    }

    static int z() {
        return b.mLogMaxUploadBytesLimit;
    }

    static long A() {
        return b.mMaxUploadBytesPerDay;
    }

    static int B() {
        return b.mMaxUploadCrashLogCountPerDay;
    }

    static int C() {
        return b.mMaxUploadCustomLogCountPerDay;
    }

    static int D() {
        return b.mMaxCustomLogCountPerTypePerDay;
    }

    static int E() {
        return b.mUnexpInfoUpdateInterval;
    }

    static int F() {
        return b.mReservedJavaFileHandleCount;
    }

    static int G() {
        return b.mFdDumpMinLimit;
    }

    static int H() {
        return b.mThreadsDumpMinLimit;
    }

    static boolean I() {
        return b.mAutoDetectLifeCycle;
    }

    static boolean J() {
        return b.mMonitorBattery;
    }

    public static boolean K() {
        return b == null || b.mDebug;
    }

    static boolean L() {
        return b == null || b.mPrintStackInfos;
    }

    static String M() {
        return b.mLogTypeSuffix;
    }

    public static boolean N() {
        return b.mEnableStatReport;
    }

    public static boolean O() {
        return b.mIsInternational;
    }

    public static String P() {
        if (g.a(c.mVersion)) {
            return a.a();
        }
        return c.mVersion;
    }

    public static String Q() {
        if (g.a(c.mSubVersion)) {
            return "release";
        }
        return c.mSubVersion;
    }

    public static String R() {
        if (g.a(c.mBuildId)) {
            return aa();
        }
        return c.mBuildId;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x004c A[SYNTHETIC, Splitter:B:21:0x004c] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0055 A[SYNTHETIC, Splitter:B:28:0x0055] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String aa() {
        /*
            java.lang.String r0 = e
            if (r0 == 0) goto L_0x0007
            java.lang.String r0 = e
            return r0
        L_0x0007:
            r0 = 0
            java.util.zip.ZipFile r1 = new java.util.zip.ZipFile     // Catch:{ Throwable -> 0x003f, all -> 0x003a }
            java.lang.String r2 = com.uc.crashsdk.a.g.c()     // Catch:{ Throwable -> 0x003f, all -> 0x003a }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x003f, all -> 0x003a }
            java.lang.String r0 = "classes.dex"
            java.util.zip.ZipEntry r0 = r1.getEntry(r0)     // Catch:{ Throwable -> 0x0038 }
            long r2 = r0.getCrc()     // Catch:{ Throwable -> 0x0038 }
            java.lang.String r0 = java.lang.Long.toHexString(r2)     // Catch:{ Throwable -> 0x0038 }
            e = r0     // Catch:{ Throwable -> 0x0038 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0038 }
            java.lang.String r2 = "version unique build id: "
            r0.<init>(r2)     // Catch:{ Throwable -> 0x0038 }
            java.lang.String r2 = e     // Catch:{ Throwable -> 0x0038 }
            r0.append(r2)     // Catch:{ Throwable -> 0x0038 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0038 }
            com.uc.crashsdk.a.a.b(r0)     // Catch:{ Throwable -> 0x0038 }
            r1.close()     // Catch:{ Throwable -> 0x004f }
            goto L_0x004f
        L_0x0038:
            r0 = move-exception
            goto L_0x0043
        L_0x003a:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x0053
        L_0x003f:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x0043:
            java.lang.String r2 = ""
            e = r2     // Catch:{ all -> 0x0052 }
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)     // Catch:{ all -> 0x0052 }
            if (r1 == 0) goto L_0x004f
            r1.close()     // Catch:{ Throwable -> 0x004f }
        L_0x004f:
            java.lang.String r0 = e
            return r0
        L_0x0052:
            r0 = move-exception
        L_0x0053:
            if (r1 == 0) goto L_0x0058
            r1.close()     // Catch:{ Throwable -> 0x0058 }
        L_0x0058:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.h.aa():java.lang.String");
    }

    public static String S() {
        if (f == null) {
            f = g.b() + File.separatorChar + b.mTagFilesFolderName + File.separatorChar;
        }
        return f;
    }

    static String T() {
        if (g == null) {
            g = g.b() + File.separatorChar + b.mCrashLogsFolderName + File.separatorChar;
        }
        return g;
    }

    static String U() {
        if (h == null) {
            if (!g.a(b.mLogsBackupPathName)) {
                String trim = b.mLogsBackupPathName.trim();
                if (!trim.endsWith(File.separator)) {
                    trim = trim + File.separator;
                }
                h = trim;
            } else {
                String str = "/sdcard";
                if (!b.D()) {
                    try {
                        str = Environment.getExternalStorageDirectory().getPath();
                    } catch (Throwable th) {
                        g.a(th);
                    }
                }
                h = str + File.separatorChar + b.mCrashLogsFolderName + File.separatorChar;
            }
        }
        return h;
    }

    public static CustomInfo a(CustomInfo customInfo, Bundle bundle) {
        if (customInfo == null) {
            if (b == null) {
                customInfo = new CustomInfo();
            } else {
                customInfo = new CustomInfo(b);
            }
        }
        Field[] fields = customInfo.getClass().getFields();
        for (String str : bundle.keySet()) {
            for (Field field : fields) {
                if (field.getName().equals(str)) {
                    Object obj = bundle.get(str);
                    try {
                        field.set(customInfo, obj);
                    } catch (Exception e2) {
                        g.a((Throwable) e2);
                        StringBuilder sb = new StringBuilder("Field ");
                        sb.append(str);
                        sb.append(" must be a ");
                        sb.append(field.getType().getName());
                        sb.append(", but give a ");
                        sb.append(obj != null ? obj.getClass().getName() : "(null)");
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
            }
        }
        return customInfo;
    }

    public static VersionInfo a(Bundle bundle) {
        VersionInfo versionInfo;
        if (c == null) {
            versionInfo = new VersionInfo();
        } else {
            versionInfo = new VersionInfo(c);
        }
        String string = bundle.getString("mVersion");
        if (!g.a(string)) {
            versionInfo.mVersion = string;
        }
        String string2 = bundle.getString("mSubVersion");
        if (!g.a(string2)) {
            versionInfo.mSubVersion = string2;
        }
        String string3 = bundle.getString("mBuildId");
        if (!g.a(string3)) {
            versionInfo.mBuildId = string3;
        }
        String string4 = bundle.getString("crver");
        if (!g.a(string4)) {
            a.b = string4;
            ab();
        }
        return versionInfo;
    }

    private static void ab() {
        if (b.d) {
            JNIBridge.nativeSyncInfo("crver", a.b, 0, 1);
        }
    }

    private static boolean a(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str == null || str2 == null) {
            return false;
        }
        return str.equals(str2);
    }

    public static int b(CustomInfo customInfo) {
        int i2;
        boolean z;
        int i3;
        boolean z2;
        boolean z3;
        synchronized (i) {
            i2 = 0;
            if (customInfo != null) {
                c(customInfo);
                if (b == null) {
                    b = new CustomInfo();
                }
                CustomInfo customInfo2 = b;
                if (!a(customInfo.mAppId, customInfo2.mAppId)) {
                    customInfo2.mAppId = customInfo.mAppId;
                    i3 = 1;
                    z = true;
                } else {
                    i3 = 0;
                    z = false;
                }
                if (!a(customInfo.mJavaCrashLogFileName, customInfo2.mJavaCrashLogFileName)) {
                    customInfo2.mJavaCrashLogFileName = customInfo.mJavaCrashLogFileName;
                    i3++;
                }
                if (!a(customInfo.mNativeCrashLogFileName, customInfo2.mNativeCrashLogFileName)) {
                    customInfo2.mNativeCrashLogFileName = customInfo.mNativeCrashLogFileName;
                    i3++;
                    z = true;
                }
                if (!a(customInfo.mUnexpCrashLogFileName, customInfo2.mUnexpCrashLogFileName)) {
                    customInfo2.mUnexpCrashLogFileName = customInfo.mUnexpCrashLogFileName;
                    i3++;
                    z = true;
                }
                if (z) {
                    e.b();
                    if (b.d) {
                        V();
                        JNIBridge.nativeUpdateCrashLogNames();
                    }
                }
                if (customInfo2.mPrintStackInfos != customInfo.mPrintStackInfos) {
                    customInfo2.mPrintStackInfos = customInfo.mPrintStackInfos;
                    i3++;
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (customInfo2.mDebug != customInfo.mDebug) {
                    customInfo2.mDebug = customInfo.mDebug;
                    i3++;
                    z2 = true;
                }
                if (customInfo2.mBackupLogs != customInfo.mBackupLogs) {
                    customInfo2.mBackupLogs = customInfo.mBackupLogs;
                    i3++;
                    z2 = true;
                }
                if (customInfo2.mOmitNativeCrash != customInfo.mOmitNativeCrash) {
                    customInfo2.mOmitNativeCrash = customInfo.mOmitNativeCrash;
                    i3++;
                    z2 = true;
                }
                if (customInfo2.mCrashRestartInterval != customInfo.mCrashRestartInterval) {
                    customInfo2.mCrashRestartInterval = customInfo.mCrashRestartInterval;
                    if (customInfo2.mCrashRestartInterval >= 0) {
                        i.a();
                    }
                    i3++;
                    z2 = true;
                }
                if (customInfo2.mMaxCrashLogFilesCount != customInfo.mMaxCrashLogFilesCount) {
                    customInfo2.mMaxCrashLogFilesCount = customInfo.mMaxCrashLogFilesCount;
                    i3++;
                    z2 = true;
                }
                if (customInfo2.mMaxNativeLogcatLineCount != customInfo.mMaxNativeLogcatLineCount) {
                    customInfo2.mMaxNativeLogcatLineCount = customInfo.mMaxNativeLogcatLineCount;
                    i3++;
                    z2 = true;
                }
                if (customInfo2.mMaxJavaLogcatLineCount != customInfo.mMaxJavaLogcatLineCount) {
                    customInfo2.mMaxJavaLogcatLineCount = customInfo.mMaxJavaLogcatLineCount;
                    i3++;
                }
                if (customInfo2.mMaxUnexpLogcatLineCount != customInfo.mMaxUnexpLogcatLineCount) {
                    customInfo2.mMaxUnexpLogcatLineCount = customInfo.mMaxUnexpLogcatLineCount;
                    i3++;
                    z2 = true;
                }
                if (customInfo2.mIsUsedByUCM != customInfo.mIsUsedByUCM) {
                    customInfo2.mIsUsedByUCM = customInfo.mIsUsedByUCM;
                    i3++;
                    z2 = true;
                }
                if (z2 && b.d) {
                    W();
                }
                if (customInfo2.mZipLog != customInfo.mZipLog) {
                    customInfo2.mZipLog = customInfo.mZipLog;
                    i3++;
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (!a(customInfo.mZippedLogExtension, customInfo2.mZippedLogExtension)) {
                    customInfo2.mZippedLogExtension = customInfo.mZippedLogExtension;
                    i3++;
                    z3 = true;
                }
                if (customInfo2.mLogMaxBytesLimit != customInfo.mLogMaxBytesLimit) {
                    customInfo2.mLogMaxBytesLimit = customInfo.mLogMaxBytesLimit;
                    i3++;
                    z3 = true;
                }
                if (z3 && b.d) {
                    Y();
                }
                if (customInfo2.mSyncUploadSetupCrashLogs != customInfo.mSyncUploadSetupCrashLogs) {
                    customInfo2.mSyncUploadSetupCrashLogs = customInfo.mSyncUploadSetupCrashLogs;
                    i3++;
                }
                if (customInfo2.mMaxCustomLogFilesCount != customInfo.mMaxCustomLogFilesCount) {
                    customInfo2.mMaxCustomLogFilesCount = customInfo.mMaxCustomLogFilesCount;
                    i3++;
                }
                if (customInfo2.mOmitJavaCrash != customInfo.mOmitJavaCrash) {
                    customInfo2.mOmitJavaCrash = customInfo.mOmitJavaCrash;
                    i3++;
                }
                if (customInfo2.mLogMaxUploadBytesLimit != customInfo.mLogMaxUploadBytesLimit) {
                    customInfo2.mLogMaxUploadBytesLimit = customInfo.mLogMaxUploadBytesLimit;
                    i3++;
                }
                if (customInfo2.mMaxUploadBytesPerDay != customInfo.mMaxUploadBytesPerDay) {
                    customInfo2.mMaxUploadBytesPerDay = customInfo.mMaxUploadBytesPerDay;
                    i3++;
                }
                if (customInfo2.mMaxUploadCrashLogCountPerDay != customInfo.mMaxUploadCrashLogCountPerDay) {
                    customInfo2.mMaxUploadCrashLogCountPerDay = customInfo.mMaxUploadCrashLogCountPerDay;
                    i3++;
                }
                if (customInfo2.mMaxUploadCustomLogCountPerDay != customInfo.mMaxUploadCustomLogCountPerDay) {
                    customInfo2.mMaxUploadCustomLogCountPerDay = customInfo.mMaxUploadCustomLogCountPerDay;
                    i3++;
                }
                if (customInfo2.mMaxCustomLogCountPerTypePerDay != customInfo.mMaxCustomLogCountPerTypePerDay) {
                    customInfo2.mMaxCustomLogCountPerTypePerDay = customInfo.mMaxCustomLogCountPerTypePerDay;
                    i3++;
                }
                if (customInfo2.mCallJavaDefaultHandler != customInfo.mCallJavaDefaultHandler) {
                    customInfo2.mCallJavaDefaultHandler = customInfo.mCallJavaDefaultHandler;
                    i3++;
                }
                if (!(customInfo2.mCallNativeDefaultHandler == customInfo.mCallNativeDefaultHandler && customInfo2.mDumpUserSolibBuildId == customInfo.mDumpUserSolibBuildId)) {
                    if (customInfo2.mCallNativeDefaultHandler != customInfo.mCallNativeDefaultHandler) {
                        i3++;
                    }
                    if (customInfo2.mDumpUserSolibBuildId != customInfo.mDumpUserSolibBuildId) {
                        i3++;
                    }
                    customInfo2.mCallNativeDefaultHandler = customInfo.mCallNativeDefaultHandler;
                    customInfo2.mDumpUserSolibBuildId = customInfo.mDumpUserSolibBuildId;
                    if (b.d) {
                        JNIBridge.nativeSetLogStrategy(b.mCallNativeDefaultHandler, b.mDumpUserSolibBuildId, b.mReservedNativeMemoryBytes);
                    }
                    i3++;
                }
                if (customInfo2.mDumpHprofDataForJavaOOM != customInfo.mDumpHprofDataForJavaOOM) {
                    customInfo2.mDumpHprofDataForJavaOOM = customInfo.mDumpHprofDataForJavaOOM;
                    i3++;
                }
                if (customInfo2.mRenameFileToDefaultName != customInfo.mRenameFileToDefaultName) {
                    customInfo2.mRenameFileToDefaultName = customInfo.mRenameFileToDefaultName;
                    i3++;
                }
                if (customInfo2.mAutoDeleteOldVersionStats != customInfo.mAutoDeleteOldVersionStats) {
                    customInfo2.mAutoDeleteOldVersionStats = customInfo.mAutoDeleteOldVersionStats;
                    i3++;
                }
                if (customInfo2.mFdDumpMinLimit != customInfo.mFdDumpMinLimit) {
                    customInfo2.mFdDumpMinLimit = customInfo.mFdDumpMinLimit;
                    if (b.d) {
                        JNIBridge.nativeReserveFileHandle(0, b.mFdDumpMinLimit);
                    }
                    i3++;
                }
                if (customInfo2.mThreadsDumpMinLimit != customInfo.mThreadsDumpMinLimit) {
                    customInfo2.mThreadsDumpMinLimit = customInfo.mThreadsDumpMinLimit;
                    Z();
                }
                if (customInfo2.mUnexpInfoUpdateInterval != customInfo.mUnexpInfoUpdateInterval) {
                    if (customInfo2.mUnexpInfoUpdateInterval <= 0 && customInfo.mUnexpInfoUpdateInterval > 0) {
                        a.a(false);
                    }
                    customInfo2.mUnexpInfoUpdateInterval = customInfo.mUnexpInfoUpdateInterval;
                    i3++;
                }
                if (!a(customInfo.mLogTypeSuffix, customInfo2.mLogTypeSuffix)) {
                    customInfo2.mLogTypeSuffix = customInfo.mLogTypeSuffix;
                    if (b.d) {
                        a(customInfo2.mLogTypeSuffix);
                    }
                    i3++;
                }
                if (customInfo2.mDisableBackgroundSignals != customInfo.mDisableBackgroundSignals) {
                    customInfo2.mDisableBackgroundSignals = customInfo.mDisableBackgroundSignals;
                    if (b.d) {
                        X();
                    }
                    i3++;
                }
                if (customInfo2.mEnableStatReport != customInfo.mEnableStatReport) {
                    customInfo2.mEnableStatReport = customInfo.mEnableStatReport;
                    if (customInfo2.mEnableStatReport) {
                        e.v();
                    }
                    i3++;
                }
                if (customInfo2.mIsInternational != customInfo.mIsInternational) {
                    customInfo2.mIsInternational = customInfo.mIsInternational;
                    ac();
                    i3++;
                }
                if (customInfo2.mAutoDetectLifeCycle != customInfo.mAutoDetectLifeCycle) {
                    customInfo2.mAutoDetectLifeCycle = customInfo.mAutoDetectLifeCycle;
                    if (customInfo2.mAutoDetectLifeCycle) {
                        b.z();
                    }
                    i3++;
                }
                if (customInfo2.mMonitorBattery != customInfo.mMonitorBattery) {
                    customInfo2.mMonitorBattery = customInfo.mMonitorBattery;
                    e.c(b.y());
                    i3++;
                }
                if (customInfo2.mUnexpSubTypes != customInfo.mUnexpSubTypes) {
                    customInfo2.mUnexpSubTypes = customInfo.mUnexpSubTypes;
                    i3++;
                }
                i2 = i3;
            }
        }
        return i2;
    }

    private static void ac() {
        if (b.d) {
            long j = 1;
            if (b.mIsInternational) {
                j = 2;
            }
            JNIBridge.nativeSyncInfo("inter", (String) null, j, 0);
        }
    }
}
