package com.ali.telescope.internal.plugins.threadio;

import android.os.Build;
import com.ali.telescope.internal.plugins.SoLoader;
import java.lang.reflect.Method;

public class SqliteConnectionMethodHook {
    private static boolean isHooked;

    private static native void hookNativeExecute(Method method);

    private static native void hookNativeExecuteForBlobFileDescriptor(Method method);

    private static native void hookNativeExecuteForChangedRowCount(Method method);

    private static native void hookNativeExecuteForCursorWindow(Method method);

    private static native void hookNativeExecuteForLastInsertedRowId(Method method);

    private static native void hookNativeExecuteForLong(Method method);

    private static native void hookNativeExecuteForString(Method method);

    private static native void init(int i, boolean z, int i2, Class cls, Method method);

    public static boolean isSupport() {
        return Build.VERSION.SDK_INT <= 25 && Build.VERSION.SDK_INT >= 21;
    }

    public static void hook(int i, Class cls, Method method) {
        try {
            if (!isHooked && isSupport()) {
                SoLoader.loadHookSo();
                init(Build.VERSION.SDK_INT, isArt(), i, cls, method);
                Class<?> cls2 = Class.forName("android.database.sqlite.SQLiteConnection");
                hookNativeExecute(cls2.getDeclaredMethod("nativeExecute", new Class[]{Long.TYPE, Long.TYPE}));
                hookNativeExecuteForLong(cls2.getDeclaredMethod("nativeExecuteForLong", new Class[]{Long.TYPE, Long.TYPE}));
                hookNativeExecuteForString(cls2.getDeclaredMethod("nativeExecuteForString", new Class[]{Long.TYPE, Long.TYPE}));
                hookNativeExecuteForBlobFileDescriptor(cls2.getDeclaredMethod("nativeExecuteForBlobFileDescriptor", new Class[]{Long.TYPE, Long.TYPE}));
                hookNativeExecuteForChangedRowCount(cls2.getDeclaredMethod("nativeExecuteForChangedRowCount", new Class[]{Long.TYPE, Long.TYPE}));
                hookNativeExecuteForLastInsertedRowId(cls2.getDeclaredMethod("nativeExecuteForLastInsertedRowId", new Class[]{Long.TYPE, Long.TYPE}));
                hookNativeExecuteForCursorWindow(cls2.getDeclaredMethod("nativeExecuteForCursorWindow", new Class[]{Long.TYPE, Long.TYPE, Long.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE}));
            }
        } catch (Throwable th) {
            isHooked = true;
            throw th;
        }
        isHooked = true;
    }

    private static boolean isArt() {
        String property = System.getProperty("java.vm.version");
        return property != null && property.startsWith("2");
    }
}
