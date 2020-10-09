package com.alimamaunion.support.debugimpl;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    private static final String LOGTAG = "SimpleUncaught";
    private static final String dirName = "disk";
    private static File mDiskCacheDir;

    public static void initDiskCacheDir(Application application) {
        mDiskCacheDir = getDiskCacheDir(application, dirName);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0053 A[SYNTHETIC, Splitter:B:23:0x0053] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005d A[SYNTHETIC, Splitter:B:29:0x005d] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0067 A[SYNTHETIC, Splitter:B:35:0x0067] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0072 A[SYNTHETIC, Splitter:B:40:0x0072] */
    /* JADX WARNING: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:32:0x0062=Splitter:B:32:0x0062, B:26:0x0058=Splitter:B:26:0x0058, B:20:0x004e=Splitter:B:20:0x004e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void savaFileToSD(java.lang.String r3, java.lang.String r4) {
        /*
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            java.io.File r2 = mDiskCacheDir     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            r1.<init>(r2, r3)     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            boolean r3 = r1.exists()     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            if (r3 != 0) goto L_0x001d
            java.io.File r3 = new java.io.File     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            java.lang.String r2 = r1.getParent()     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            r3.<init>(r2)     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            r3.mkdirs()     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            r1.createNewFile()     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
        L_0x001d:
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            r3.<init>(r1)     // Catch:{ FileNotFoundException -> 0x0061, IOException -> 0x0057, Exception -> 0x004d }
            java.io.BufferedOutputStream r0 = new java.io.BufferedOutputStream     // Catch:{ FileNotFoundException -> 0x0047, IOException -> 0x0043, Exception -> 0x003f, all -> 0x003b }
            r0.<init>(r3)     // Catch:{ FileNotFoundException -> 0x0047, IOException -> 0x0043, Exception -> 0x003f, all -> 0x003b }
            byte[] r4 = r4.getBytes()     // Catch:{ FileNotFoundException -> 0x0047, IOException -> 0x0043, Exception -> 0x003f, all -> 0x003b }
            r0.write(r4)     // Catch:{ FileNotFoundException -> 0x0047, IOException -> 0x0043, Exception -> 0x003f, all -> 0x003b }
            r0.flush()     // Catch:{ FileNotFoundException -> 0x0047, IOException -> 0x0043, Exception -> 0x003f, all -> 0x003b }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0047, IOException -> 0x0043, Exception -> 0x003f, all -> 0x003b }
            r3.close()     // Catch:{ FileNotFoundException -> 0x0047, IOException -> 0x0043, Exception -> 0x003f, all -> 0x003b }
            r3.close()     // Catch:{ IOException -> 0x006b }
            goto L_0x006f
        L_0x003b:
            r4 = move-exception
            r0 = r3
            r3 = r4
            goto L_0x0070
        L_0x003f:
            r4 = move-exception
            r0 = r3
            r3 = r4
            goto L_0x004e
        L_0x0043:
            r4 = move-exception
            r0 = r3
            r3 = r4
            goto L_0x0058
        L_0x0047:
            r4 = move-exception
            r0 = r3
            r3 = r4
            goto L_0x0062
        L_0x004b:
            r3 = move-exception
            goto L_0x0070
        L_0x004d:
            r3 = move-exception
        L_0x004e:
            r3.printStackTrace()     // Catch:{ all -> 0x004b }
            if (r0 == 0) goto L_0x006f
            r0.close()     // Catch:{ IOException -> 0x006b }
            goto L_0x006f
        L_0x0057:
            r3 = move-exception
        L_0x0058:
            r3.printStackTrace()     // Catch:{ all -> 0x004b }
            if (r0 == 0) goto L_0x006f
            r0.close()     // Catch:{ IOException -> 0x006b }
            goto L_0x006f
        L_0x0061:
            r3 = move-exception
        L_0x0062:
            r3.printStackTrace()     // Catch:{ all -> 0x004b }
            if (r0 == 0) goto L_0x006f
            r0.close()     // Catch:{ IOException -> 0x006b }
            goto L_0x006f
        L_0x006b:
            r3 = move-exception
            r3.printStackTrace()
        L_0x006f:
            return
        L_0x0070:
            if (r0 == 0) goto L_0x007a
            r0.close()     // Catch:{ IOException -> 0x0076 }
            goto L_0x007a
        L_0x0076:
            r4 = move-exception
            r4.printStackTrace()
        L_0x007a:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimamaunion.support.debugimpl.FileUtils.savaFileToSD(java.lang.String, java.lang.String):void");
    }

    public static String readFromSD(String str) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if (Environment.getExternalStorageState().equals("mounted")) {
            FileInputStream fileInputStream = new FileInputStream(mDiskCacheDir + "/" + str);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                sb.append(new String(bArr, 0, read));
            }
            fileInputStream.close();
        }
        return sb.toString();
    }

    private static File getDiskCacheDir(Context context, String str) {
        String str2;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = getExternalCacheDir(context);
            if (externalCacheDir != null) {
                str2 = externalCacheDir.getPath();
            } else {
                str2 = context.getCacheDir().getPath();
            }
        } else {
            str2 = context.getCacheDir().getPath();
        }
        return new File(str2 + File.separator + str);
    }

    @TargetApi(8)
    private static File getExternalCacheDir(Context context) {
        if (hasFroyo()) {
            return context.getExternalCacheDir();
        }
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
    }

    private static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= 8;
    }
}
