package com.taobao.phenix.compat.effects.internal;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.taobao.tcommon.log.FLog;

public class NdkCore {
    private static final String LIBRARY_NAME = "EffectsCore";
    private static boolean sIsSoInstalled = true;

    private static native int nativeBlurBitmap(Bitmap bitmap, int i, int i2, int i3);

    static {
        try {
            System.loadLibrary(LIBRARY_NAME);
            FLog.i("Effects4Phenix", "system load lib%s.so success", LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            FLog.e("Effects4Phenix", "system load lib%s.so error=%s", LIBRARY_NAME, e);
        }
    }

    public static Bitmap blurBitmap(@NonNull Bitmap bitmap, int i) {
        if (!sIsSoInstalled || bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        try {
            if (nativeBlurBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), i) == 0) {
                return bitmap;
            }
            return null;
        } catch (UnsatisfiedLinkError e) {
            FLog.i("Effects4Phenix", "native blur bitmap error=%s", e);
        }
    }
}
