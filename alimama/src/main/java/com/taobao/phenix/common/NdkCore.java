package com.taobao.phenix.common;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

public class NdkCore {
    private static String sCpuType;
    private static boolean sLoadedSuccess = true;

    public static native boolean nativeCpuSupportNEON();

    public static native String nativeGetCpuAbi(String str);

    public static native void nativePinBitmap(Bitmap bitmap) throws RuntimeException;

    public static native void nativePinBitmapWithAddr(Bitmap bitmap, long[] jArr) throws Exception;

    public static native void nativeUnpinBitmap(Bitmap bitmap) throws RuntimeException;

    static {
        try {
            System.loadLibrary("phxcore");
        } catch (UnsatisfiedLinkError e) {
            UnitedLog.e("JNI", "loadLibrary phxcore error=%s", e);
        }
    }

    public static boolean isLoadedSuccess() {
        return sLoadedSuccess;
    }

    public static boolean isCpuSupportNEON() {
        try {
            return nativeCpuSupportNEON();
        } catch (UnsatisfiedLinkError e) {
            UnitedLog.e("JNI", "NdkCore.nativeCpuSupportNEON error=%s", e);
            return false;
        }
    }

    public static String getCpuType() {
        if (sCpuType == null) {
            try {
                if (isLoadedSuccess()) {
                    sCpuType = nativeGetCpuAbi(Build.CPU_ABI);
                }
            } catch (UnsatisfiedLinkError e) {
                UnitedLog.e("JNI", "NdkCore.nativeGetCpuAbi error=%s", e);
            }
            if (TextUtils.isEmpty(sCpuType)) {
                sCpuType = Build.CPU_ABI;
            }
        }
        return sCpuType;
    }
}
