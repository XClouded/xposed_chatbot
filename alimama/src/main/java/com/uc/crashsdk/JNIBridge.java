package com.uc.crashsdk;

import android.os.Looper;
import android.os.Process;
import com.uc.crashsdk.a.a;
import com.uc.crashsdk.a.g;
import com.uc.crashsdk.export.LogType;
import java.io.File;
import java.util.Locale;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
public class JNIBridge {
    public static native boolean nativeAddCachedInfo(String str, String str2);

    public static native int nativeAddCallbackInfo(String str, boolean z, boolean z2, long j, int i);

    public static native int nativeAddDumpFile(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5);

    public static native void nativeAddHeaderInfo(String str, String str2);

    public static native void nativeBreakpadInited(String str);

    public static native boolean nativeChangeState(String str, String str2, boolean z);

    public static native void nativeClientCloseConnection(long j);

    public static native long nativeClientCreateConnection(String str, String str2, String str3, int i);

    public static native int nativeClientWriteData(long j, String str);

    public static native void nativeCloseFile(int i);

    public static native void nativeCrash(int i, int i2);

    public static native boolean nativeCreateCachedInfo(String str, int i);

    public static native String nativeDumpThreads(String str, long j);

    public static native boolean nativeGenerateTraces(String str, long j);

    public static native int nativeGenerateUnexpLog(long j, int i);

    public static native String nativeGetCallbackInfo(String str, long j, int i, boolean z);

    public static native String nativeGetNativeBuildseq();

    public static native int nativeGetOrSetHostFd(int i, int i2);

    public static native void nativeInitNative();

    public static native void nativeInstallBreakpad(int i, int i2);

    public static native boolean nativeIsCrashing();

    public static native boolean nativeLockFile(int i, boolean z);

    public static native int nativeLog(int i, String str, String str2);

    public static native int nativeOpenFile(String str);

    public static native void nativePrepareUnexpInfos(boolean z);

    public static native void nativeRegisterCurrentThread(String str);

    public static native void nativeReserveFileHandle(int i, int i2);

    public static native void nativeSetCrashCustoms(boolean z, boolean z2, int i, int i2, int i3, int i4, boolean z3, boolean z4, boolean z5, int i5, boolean z6);

    public static native void nativeSetCrashLogFileNames(String str, String str2, String str3);

    public static native void nativeSetCrashLogFilesUploaded();

    public static native void nativeSetFolderNames(String str, String str2, String str3, String str4);

    public static native void nativeSetForeground(boolean z);

    public static native void nativeSetLogStrategy(boolean z, boolean z2, long j);

    public static native void nativeSetMobileInfo(String str, String str2, String str3);

    public static native void nativeSetPackageInfo(String str, String str2, String str3);

    public static native void nativeSetProcessNames(String str, String str2);

    public static native void nativeSetProcessType(boolean z);

    public static native void nativeSetVersionInfo(String str, String str2, String str3, String str4);

    public static native void nativeSetZip(boolean z, String str, int i);

    public static native boolean nativeSyncInfo(String str, String str2, long j, long j2);

    public static native boolean nativeSyncStatus(String str, String str2, int i);

    public static native void nativeUninstallBreakpad();

    public static native void nativeUpdateCrashLogNames();

    public static native void nativeUpdateSignals(int i, int i2, int i3);

    public static native void nativeUpdateUnexpInfo(int i);

    private static long getMaxHeapSize() {
        return Runtime.getRuntime().maxMemory();
    }

    private static String getLogFileNamePart1() {
        return e.c();
    }

    private static String getProcessList(String str, String str2) {
        return e.a(str, str2);
    }

    private static String getJavaMemory() {
        return e.f();
    }

    protected static String getCallbackInfo(String str, boolean z) {
        return a.a(str, z);
    }

    private static String getJavaStackTrace(Thread thread, int i) {
        if (i != 0 && i == Process.myPid()) {
            thread = Looper.getMainLooper().getThread();
        }
        if (thread != null) {
            return e.a(thread.getStackTrace(), "getJavaStackTrace").toString();
        }
        return null;
    }

    private static void onCrashLogGenerated(String str, String str2, String str3, boolean z) {
        d.a(str, str2, str3);
        boolean equals = e.g().equals(str2);
        boolean equals2 = LogType.NATIVE_TYPE.equals(str3);
        if (equals && equals2) {
            e.b(true);
        } else if (z) {
            e.a(false, false);
        }
    }

    private static void onCrashRestarting() {
        d.a(false);
        i.b();
    }

    private static void addHeaderInfo(String str, String str2) {
        a.a(str, str2);
    }

    private static int registerCurrentThread(String str, int i) {
        return a.a(i, str);
    }

    private static int registerInfoCallback(String str, int i, long j, int i2) {
        return a.a(str, i, (Callable<String>) null, j, i2);
    }

    private static int addDumpFile(String str, String str2, boolean z, boolean z2, int i, boolean z3) {
        return a.a(str, str2, z, z2, i, z3);
    }

    private static int createCachedInfo(String str, int i, int i2) {
        return a.a(str, i, i2);
    }

    private static int addCachedInfo(String str, String str2) {
        return a.b(str, str2);
    }

    private static boolean generateCustomLog(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, String str3, String str4, String str5, String str6) {
        StringBuffer stringBuffer = new StringBuffer();
        String str7 = str;
        stringBuffer.append(str);
        return e.a(stringBuffer, str2, z, z2, z3, z4, z5, a.c(str3), a.c(str4), a.c(str5), str6);
    }

    private static void onKillProcess(String str, int i, int i2) {
        String str2 = "onKillProcess. SIG: " + i2;
        if (e.a()) {
            a.a("crashsdk", str2);
        } else {
            a.b(str2);
        }
        StringBuilder g = e.g("onKillProcess");
        g.insert(0, String.format(Locale.US, "State in disk: '%s'\n", new Object[]{b.n()}));
        g.insert(0, String.format(Locale.US, "SIG: %d, fg: %s, exiting: %s, main process: %s, time: %s\n", new Object[]{Integer.valueOf(i2), Boolean.valueOf(b.y()), Boolean.valueOf(b.r()), Boolean.valueOf(b.A()), e.k()}));
        g.insert(0, String.format(Locale.US, "Kill PID: %d (%s) by pid: %d (%s) tid: %d (%s)\n", new Object[]{Integer.valueOf(i), e.a(i), Integer.valueOf(Process.myPid()), e.a(Process.myPid()), Integer.valueOf(Process.myTid()), Thread.currentThread().getName()}));
        String sb = g.toString();
        if (e.a()) {
            a.a("crashsdk", sb);
        } else {
            a.b(sb);
        }
        if (!b.D()) {
            g.a(new File(str), sb.getBytes());
        }
    }

    private static String getDatasForClientJavaLog(int i, String str) {
        boolean equals = "$all$".equals(str);
        if (i == 1) {
            if (equals) {
                return a.g();
            }
            return a.a(str);
        } else if (i == 2) {
            if (equals) {
                return a.i();
            }
            return a.a(str, true);
        } else if (i == 3) {
            if (equals) {
                return a.k();
            }
            return a.b(str);
        } else if (i == 4) {
            return e.d(str) ? "1" : "0";
        } else {
            return null;
        }
    }

    private static int onPreClientCustomLog(String str, String str2, boolean z) {
        h.b();
        e.a(false);
        if (e.a(str, str2, z)) {
            return 0;
        }
        e.b(str, str2, z);
        return 1;
    }
}
