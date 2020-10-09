package alimama.com.unwupdate;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;

public class DiskFileUtils {

    public static class CacheDirInfo {
        public boolean isInternal = false;
        public boolean isNotEnough = false;
        public File path;
        public long realSize;
        public long requireSize;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x002f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static alimama.com.unwupdate.DiskFileUtils.CacheDirInfo getDiskCacheDir(android.content.Context r4, java.lang.String r5, long r6, java.lang.String r8) {
        /*
            alimama.com.unwupdate.DiskFileUtils$CacheDirInfo r0 = new alimama.com.unwupdate.DiskFileUtils$CacheDirInfo
            r0.<init>()
            r0.requireSize = r6
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x002c
            java.io.File r1 = new java.io.File
            r1.<init>(r5)
            boolean r2 = r1.exists()
            if (r2 != 0) goto L_0x001e
            boolean r2 = r1.mkdirs()
            if (r2 == 0) goto L_0x002c
        L_0x001e:
            long r2 = getUsableSpace(r1)
            long r6 = java.lang.Math.min(r6, r2)
            r0.realSize = r6
            r0.path = r1
            r1 = 1
            goto L_0x002d
        L_0x002c:
            r1 = 0
        L_0x002d:
            if (r1 != 0) goto L_0x003b
            boolean r0 = android.text.TextUtils.isEmpty(r8)
            if (r0 == 0) goto L_0x0036
            goto L_0x0037
        L_0x0036:
            r5 = r8
        L_0x0037:
            alimama.com.unwupdate.DiskFileUtils$CacheDirInfo r0 = getDiskCacheDir(r4, r5, r6)
        L_0x003b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwupdate.DiskFileUtils.getDiskCacheDir(android.content.Context, java.lang.String, long, java.lang.String):alimama.com.unwupdate.DiskFileUtils$CacheDirInfo");
    }

    public static CacheDirInfo getDiskCacheDir(Context context, String str, long j) {
        Long l;
        File file;
        File file2 = null;
        if (hasSDCardMounted()) {
            file = getExternalCacheDir(context);
            if (!file.exists()) {
                file.mkdirs();
            }
            l = Long.valueOf(getUsableSpace(file));
        } else {
            l = 0L;
            file = null;
        }
        CacheDirInfo cacheDirInfo = new CacheDirInfo();
        cacheDirInfo.requireSize = j;
        boolean z = false;
        if (file == null || l.longValue() < j) {
            file2 = context.getCacheDir();
            long usableSpace = getUsableSpace(file2);
            if (usableSpace < j) {
                if (usableSpace > l.longValue()) {
                    cacheDirInfo.realSize = usableSpace;
                    z = true;
                } else {
                    cacheDirInfo.realSize = l.longValue();
                }
                cacheDirInfo.isNotEnough = true;
            } else {
                cacheDirInfo.realSize = j;
                z = true;
            }
        } else {
            cacheDirInfo.realSize = j;
        }
        cacheDirInfo.isInternal = z;
        if (z) {
            cacheDirInfo.path = new File(file2.getPath() + File.separator + str);
        } else {
            cacheDirInfo.path = new File(file.getPath() + File.separator + str);
        }
        if (!cacheDirInfo.path.exists()) {
            cacheDirInfo.path.mkdirs();
        }
        return cacheDirInfo;
    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        File externalCacheDir;
        if (Version.hasFroyo() && (externalCacheDir = context.getExternalCacheDir()) != null) {
            return externalCacheDir;
        }
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
    }

    @TargetApi(9)
    public static long getUsableSpace(File file) {
        if (file == null) {
            return -1;
        }
        if (Version.hasGingerbread()) {
            return file.getUsableSpace();
        }
        if (!file.exists()) {
            return 0;
        }
        StatFs statFs = new StatFs(file.getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks());
    }

    @TargetApi(9)
    public static long getUsedSpace(File file) {
        if (file == null) {
            return -1;
        }
        if (Version.hasGingerbread()) {
            return file.getTotalSpace() - file.getUsableSpace();
        }
        if (!file.exists()) {
            return -1;
        }
        StatFs statFs = new StatFs(file.getPath());
        return ((long) statFs.getBlockSize()) * ((long) (statFs.getBlockCount() - statFs.getAvailableBlocks()));
    }

    @TargetApi(9)
    public static long getTotalSpace(File file) {
        if (file == null) {
            return -1;
        }
        if (Version.hasGingerbread()) {
            return file.getTotalSpace();
        }
        if (!file.exists()) {
            return 0;
        }
        StatFs statFs = new StatFs(file.getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount());
    }

    public static boolean hasSDCardMounted() {
        String str;
        try {
            str = Environment.getExternalStorageState();
        } catch (NullPointerException unused) {
            str = null;
        }
        return str != null && str.equals("mounted");
    }

    public static String wantFilesPath(Context context, boolean z) {
        File externalFilesDir;
        if (!z || !hasSDCardMounted() || (externalFilesDir = context.getExternalFilesDir("xxx")) == null) {
            return context.getFilesDir().getAbsolutePath();
        }
        return externalFilesDir.getAbsolutePath();
    }

    public static String readAssert(Context context, String str) {
        try {
            if (str.startsWith(File.separator)) {
                str = str.substring(File.separator.length());
            }
            DataInputStream dataInputStream = new DataInputStream(context.getAssets().open(str));
            byte[] bArr = new byte[dataInputStream.available()];
            dataInputStream.readFully(bArr);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(bArr);
            dataInputStream.close();
            return byteArrayOutputStream.toString();
        } catch (Exception unused) {
            return null;
        }
    }
}
