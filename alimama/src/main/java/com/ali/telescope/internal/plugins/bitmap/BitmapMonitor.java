package com.ali.telescope.internal.plugins.bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Looper;
import androidx.annotation.Keep;
import com.ali.telescope.util.TelescopeLog;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;

@Keep
public class BitmapMonitor {
    private static boolean isInit;
    private static int main_threshold;
    private static int non_main_threshold;
    private static CallBack sCallBack;

    public interface CallBack {
        void callback(int i, Throwable th, boolean z);
    }

    public static native void debug();

    private static native void hookNativeDecodeAsset(Method method);

    private static native void hookNativeDecodeByteArray(Method method);

    private static native void hookNativeDecodeFileDescriptor(Method method);

    private static native void hookNativeDecodeStream(Method method);

    private static native void nativeInit(int i, int i2, boolean z, Class cls, Method method);

    public static boolean isSupport() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static void init(int i, int i2) {
        if (isSupport() && !isInit) {
            main_threshold = i;
            non_main_threshold = i2;
            Class<BitmapMonitor> cls = BitmapMonitor.class;
            try {
                Method declaredMethod = cls.getDeclaredMethod("dumpStack", new Class[]{Integer.TYPE});
                declaredMethod.setAccessible(true);
                nativeInit(Build.VERSION.SDK_INT, Math.min(i, i2), true, BitmapMonitor.class, declaredMethod);
                hookNativeDecodeAsset(BitmapFactory.class.getDeclaredMethod("nativeDecodeAsset", new Class[]{Long.TYPE, Rect.class, BitmapFactory.Options.class}));
                hookNativeDecodeStream(BitmapFactory.class.getDeclaredMethod("nativeDecodeStream", new Class[]{InputStream.class, byte[].class, Rect.class, BitmapFactory.Options.class}));
                hookNativeDecodeFileDescriptor(BitmapFactory.class.getDeclaredMethod("nativeDecodeFileDescriptor", new Class[]{FileDescriptor.class, Rect.class, BitmapFactory.Options.class}));
                hookNativeDecodeByteArray(BitmapFactory.class.getDeclaredMethod("nativeDecodeByteArray", new Class[]{byte[].class, Integer.TYPE, Integer.TYPE, BitmapFactory.Options.class}));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                TelescopeLog.e("", "Bitmap Monitor Failed ==== > " + e);
            }
            isInit = true;
        }
    }

    public static void setCallBack(CallBack callBack) {
        sCallBack = callBack;
    }

    private static void dumpStack(int i) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            if (i > main_threshold) {
                Throwable th = new Throwable();
                if (sCallBack != null) {
                    sCallBack.callback(i, th, true);
                }
            }
        } else if (i > non_main_threshold) {
            Throwable th2 = new Throwable();
            if (sCallBack != null) {
                sCallBack.callback(i, th2, false);
            }
        }
    }
}
