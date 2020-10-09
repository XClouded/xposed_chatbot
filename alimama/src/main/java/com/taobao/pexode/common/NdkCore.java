package com.taobao.pexode.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import com.taobao.pexode.Pexode;
import com.taobao.tcommon.log.FLog;
import java.io.FileDescriptor;

public class NdkCore {
    private static final int LIBRARY_JNI_VERSION = 2;
    private static final String LIBRARY_NAME = "pexcore";
    private static String[] sCpuAbiList;
    private static boolean sIsSoInstalled;

    private static native boolean nativeCpuSupportNEON();

    private static native String nativeGetCpuAbi(String str);

    private static native String nativeGetCpuAbiList();

    private static native boolean nativeIsSeekable(FileDescriptor fileDescriptor);

    private static native int nativeLoadedVersionTest();

    public static native void nativePinBitmap(Bitmap bitmap) throws RuntimeException;

    public static native void nativePinBitmapWithAddr(Bitmap bitmap, long[] jArr) throws Exception;

    public static native void nativeUnpinBitmap(Bitmap bitmap) throws RuntimeException;

    static {
        try {
            System.loadLibrary(LIBRARY_NAME);
            sIsSoInstalled = nativeLoadedVersionTest() == 2;
            FLog.i(Pexode.TAG, "system load lib%s.so success", LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            FLog.e(Pexode.TAG, "system load lib%s.so error=%s", LIBRARY_NAME, e);
        }
    }

    public static void prepare(Context context) {
        if (!sIsSoInstalled) {
            sIsSoInstalled = SoInstallMgrSdk.loadBackup(LIBRARY_NAME, 2) && nativeLoadedVersionTest() == 2;
            FLog.i(Pexode.TAG, "retry load lib%s.so result=%b", LIBRARY_NAME, Boolean.valueOf(sIsSoInstalled));
        }
    }

    public static boolean isSoInstalled() {
        return sIsSoInstalled;
    }

    public static boolean isCpuSupportNEON() {
        try {
            return nativeCpuSupportNEON();
        } catch (UnsatisfiedLinkError e) {
            FLog.e(Pexode.TAG, "NdkCore.isCpuSupportNEON error=%s", e);
            return false;
        }
    }

    public static boolean isCpuAbiSupported(String str) {
        if (sCpuAbiList == null) {
            try {
                if (Build.VERSION.SDK_INT >= 21) {
                    String nativeGetCpuAbiList = nativeGetCpuAbiList();
                    if (!TextUtils.isEmpty(nativeGetCpuAbiList)) {
                        sCpuAbiList = nativeGetCpuAbiList.split(",");
                    }
                } else {
                    sCpuAbiList = new String[]{nativeGetCpuAbi(Build.CPU_ABI)};
                }
            } catch (UnsatisfiedLinkError e) {
                FLog.e(Pexode.TAG, "NdkCore.isCpuAbiSupported error=%s", e);
            }
            if (sCpuAbiList == null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    sCpuAbiList = Build.SUPPORTED_ABIS;
                } else {
                    sCpuAbiList = new String[]{Build.CPU_ABI};
                }
            }
        }
        for (String equals : sCpuAbiList) {
            if (equals.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFdSeekable(FileDescriptor fileDescriptor) {
        try {
            return nativeIsSeekable(fileDescriptor);
        } catch (UnsatisfiedLinkError e) {
            FLog.e(Pexode.TAG, "NdkCore.isFdSeekable error=%s", e);
            return false;
        }
    }
}
