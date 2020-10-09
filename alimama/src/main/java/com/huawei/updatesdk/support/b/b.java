package com.huawei.updatesdk.support.b;

import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import com.huawei.updatesdk.sdk.a.c.a.a.a;
import com.huawei.updatesdk.support.b.a;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.utils.tools.TimeCalculator;
import java.io.File;
import java.lang.reflect.Method;

public class b {
    private static String[] a;

    private static long a(String str) {
        long blockSize;
        long availableBlocks;
        try {
            StatFs statFs = new StatFs(str);
            if (Build.VERSION.SDK_INT >= 18) {
                blockSize = statFs.getBlockSizeLong();
                availableBlocks = statFs.getAvailableBlocksLong();
            } else {
                blockSize = (long) statFs.getBlockSize();
                availableBlocks = (long) statFs.getAvailableBlocks();
            }
            return blockSize * availableBlocks;
        } catch (IllegalArgumentException e) {
            a.a("StorageManage", "path error", e);
            return 0;
        }
    }

    public static a a() {
        Context b = com.huawei.updatesdk.sdk.service.a.a.a().b();
        a b2 = (Build.VERSION.SDK_INT >= 24 || com.huawei.updatesdk.sdk.a.d.b.a.g() == 0) ? b(b) : a(b);
        if (TextUtils.isEmpty(b2.a())) {
            return null;
        }
        String str = b2.a() + File.separator;
        b2.a(str);
        File file = new File(str);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        if (Build.VERSION.SDK_INT < 24 && !file.setExecutable(true, false)) {
            a.d("StorageManage", "can not set Executeable to AppCache");
        }
        return b2;
    }

    private static a a(Context context) {
        String d = d(context);
        File file = new File(d + File.separator + context.getPackageName() + File.separator + AVFSCacheConstants.AVFS_FIlE_PATH_NAME);
        if (!file.exists() && !file.mkdirs()) {
            a.d("StorageManage", "failed to create file.");
        }
        a aVar = new a();
        String absolutePath = file.getAbsolutePath();
        aVar.b(a(absolutePath));
        aVar.a(b(absolutePath));
        aVar.a(absolutePath);
        aVar.a(a.C0018a.INNER_SDCARD);
        return aVar;
    }

    private static long b(String str) {
        long blockSize;
        long blockCount;
        StatFs statFs = new StatFs(str);
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = statFs.getBlockSizeLong();
            blockCount = statFs.getBlockCountLong();
        } else {
            blockSize = (long) statFs.getBlockSize();
            blockCount = (long) statFs.getBlockCount();
        }
        return blockSize * blockCount;
    }

    private static a b(Context context) {
        File filesDir = context.getFilesDir();
        a aVar = new a();
        if (filesDir != null) {
            String absolutePath = filesDir.getAbsolutePath();
            aVar.b(a(absolutePath));
            aVar.a(b(absolutePath));
            aVar.a(absolutePath);
        }
        aVar.a(a.C0018a.SYSTEM_STORAGE);
        return aVar;
    }

    public static String[] b() {
        if (a == null) {
            a = c(com.huawei.updatesdk.sdk.service.a.a.a().b());
        }
        return (String[]) a.clone();
    }

    private static Method c() {
        Method method = null;
        try {
            Method method2 = StorageVolume.class.getMethod("isRemovable", new Class[0]);
            try {
                method2.setAccessible(true);
                return method2;
            } catch (NoSuchMethodException e) {
                NoSuchMethodException noSuchMethodException = e;
                method = method2;
                e = noSuchMethodException;
            }
        } catch (NoSuchMethodException e2) {
            e = e2;
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("StorageManage", "can not find method:isRemovable", e);
            return method;
        }
    }

    private static String[] c(Context context) {
        String d = d(context);
        return new String[]{d + File.separator + TimeCalculator.PLATFORM_ANDROID + File.separator + "data" + File.separator + context.getPackageName() + File.separator + AVFSCacheConstants.AVFS_FIlE_PATH_NAME};
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0077 A[LOOP:0: B:9:0x003c->B:26:0x0077, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0076 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String d(android.content.Context r10) {
        /*
            java.lang.String r0 = "storage"
            java.lang.Object r10 = r10.getSystemService(r0)
            android.os.storage.StorageManager r10 = (android.os.storage.StorageManager) r10
            r0 = 0
            android.os.storage.StorageVolume[] r1 = new android.os.storage.StorageVolume[r0]
            java.lang.Class[] r2 = new java.lang.Class[r0]     // Catch:{ IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0024 }
            java.lang.Class<android.os.storage.StorageManager> r3 = android.os.storage.StorageManager.class
            java.lang.String r4 = "getVolumeList"
            java.lang.reflect.Method r2 = r3.getMethod(r4, r2)     // Catch:{ IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0024 }
            r3 = 1
            r2.setAccessible(r3)     // Catch:{ IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0024 }
            java.lang.Object[] r3 = new java.lang.Object[r0]     // Catch:{ IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0024 }
            java.lang.Object r10 = r2.invoke(r10, r3)     // Catch:{ IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0024 }
            android.os.storage.StorageVolume[] r10 = (android.os.storage.StorageVolume[]) r10     // Catch:{ IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0024 }
            android.os.storage.StorageVolume[] r10 = (android.os.storage.StorageVolume[]) r10     // Catch:{ IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0024 }
            goto L_0x002d
        L_0x0024:
            r10 = move-exception
            java.lang.String r2 = "StorageManage"
            java.lang.String r3 = "can not find method:getVolumeList"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r2, r3, r10)
            r10 = r1
        L_0x002d:
            if (r10 == 0) goto L_0x007a
            int r1 = r10.length
            if (r1 <= 0) goto L_0x007a
            java.lang.reflect.Method r1 = c()
            java.lang.reflect.Method r2 = d()
            int r3 = r10.length
            r4 = 0
        L_0x003c:
            if (r4 >= r3) goto L_0x007a
            r5 = r10[r4]
            if (r1 == 0) goto L_0x0057
            java.lang.Object[] r6 = new java.lang.Object[r0]     // Catch:{ IllegalAccessException | InvocationTargetException -> 0x004f }
            java.lang.Object r6 = r1.invoke(r5, r6)     // Catch:{ IllegalAccessException | InvocationTargetException -> 0x004f }
            java.lang.Boolean r6 = (java.lang.Boolean) r6     // Catch:{ IllegalAccessException | InvocationTargetException -> 0x004f }
            boolean r6 = r6.booleanValue()     // Catch:{ IllegalAccessException | InvocationTargetException -> 0x004f }
            goto L_0x0058
        L_0x004f:
            r6 = move-exception
            java.lang.String r7 = "StorageManage"
            java.lang.String r8 = "can not invoke method:getVolumeList"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r7, r8, r6)
        L_0x0057:
            r6 = 0
        L_0x0058:
            java.lang.String r7 = ""
            java.lang.Object[] r8 = new java.lang.Object[r0]     // Catch:{ IllegalAccessException | InvocationTargetException -> 0x0063 }
            java.lang.Object r5 = r2.invoke(r5, r8)     // Catch:{ IllegalAccessException | InvocationTargetException -> 0x0063 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ IllegalAccessException | InvocationTargetException -> 0x0063 }
            goto L_0x006c
        L_0x0063:
            r5 = move-exception
            java.lang.String r8 = "StorageManage"
            java.lang.String r9 = "can not invoke method:getPath"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r8, r9, r5)
            r5 = r7
        L_0x006c:
            if (r6 != 0) goto L_0x0077
            java.lang.String r6 = "usb"
            boolean r6 = r5.contains(r6)
            if (r6 != 0) goto L_0x0077
            return r5
        L_0x0077:
            int r4 = r4 + 1
            goto L_0x003c
        L_0x007a:
            r10 = 0
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.support.b.b.d(android.content.Context):java.lang.String");
    }

    private static Method d() {
        Method method = null;
        try {
            Method method2 = StorageVolume.class.getMethod("getPath", new Class[0]);
            try {
                method2.setAccessible(true);
                return method2;
            } catch (NoSuchMethodException e) {
                NoSuchMethodException noSuchMethodException = e;
                method = method2;
                e = noSuchMethodException;
            }
        } catch (NoSuchMethodException e2) {
            e = e2;
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("StorageManage", "can not find method:getPath", e);
            return method;
        }
    }
}
