package com.ali.telescope.internal.plugins.mainthreadblock;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.MessageQueue;
import com.ali.telescope.internal.plugins.SoLoader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BlockMonitor {
    private static boolean isInit;

    static native void nativeInit(int i, boolean z, long j, Object obj, int i2, int i3, int i4);

    static native void sampleRegister(Object obj, Method method, Method method2, Method method3);

    public static void init(Context context, int i, int i2, int i3, Object obj, Method method, Method method2, Method method3) {
        try {
            if (!isInit) {
                SoLoader.loadHookSo();
                MessageQueue queue = Looper.getMainLooper().getQueue();
                Field declaredField = MessageQueue.class.getDeclaredField("mPtr");
                declaredField.setAccessible(true);
                nativeInit(Build.VERSION.SDK_INT, isArt(), ((Long) declaredField.get(queue)).longValue(), MessageQueue.class.getDeclaredMethod("nativePollOnce", new Class[]{Long.TYPE, Integer.TYPE}), i, i2, i3);
                sampleRegister(obj, method, method2, method3);
                isInit = true;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static boolean isArt() {
        String property = System.getProperty("java.vm.version");
        return property != null && property.startsWith("2");
    }
}
