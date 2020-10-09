package com.taobao.securityjni;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Process;
import android.util.Log;
import androidx.annotation.NonNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class SGJpgProcess {
    private static String[] DEFAULT_FILES = {"res/drawable/yw_1222.jpg", "res/drawable/yw_1222_mwua.jpg", "res/drawable/yw_1222_sharetoken.jpg"};
    private static String FINISHED_FILE_NAME = "SGJpgProcessFinished";
    private static String JPG_DIR_PREFIX = "jpgs_";
    private static String JPG_PREFIX = "yw_1222";
    private static String ROOT_FOLDER = "SGLib";
    private static final String TAG = "SG_Compatible";
    private static Method sOpenNonAssetMethod;
    private static boolean sOpenNonAssetMethodFetched;
    private Context ctx;

    public SGJpgProcess(Context context) {
        this.ctx = context;
    }

    private boolean isPathSecurityValid(String str) {
        return !Pattern.compile("\\S*(\\.\\.)+(%)*\\S*").matcher(str).find();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:39|40|46|47|48|49) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:48:0x0080 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:52:0x0082 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean unzipByPrefix(java.lang.String r12, java.lang.String r13, java.lang.String r14, java.lang.String[] r15) {
        /*
            r11 = this;
            r0 = 0
            if (r15 == 0) goto L_0x008f
            int r1 = r15.length     // Catch:{ IOException -> 0x008e }
            if (r1 > 0) goto L_0x0008
            goto L_0x008f
        L_0x0008:
            java.util.zip.ZipFile r1 = new java.util.zip.ZipFile     // Catch:{ IOException -> 0x008e }
            r1.<init>(r12)     // Catch:{ IOException -> 0x008e }
            int r12 = r15.length     // Catch:{ IOException -> 0x008e }
            r2 = 0
        L_0x000f:
            if (r2 >= r12) goto L_0x008c
            r3 = r15[r2]     // Catch:{ IOException -> 0x008e }
            boolean r4 = r11.isPathSecurityValid(r3)     // Catch:{ IOException -> 0x008e }
            if (r4 != 0) goto L_0x001a
            goto L_0x006b
        L_0x001a:
            java.util.zip.ZipEntry r4 = r1.getEntry(r3)     // Catch:{ IOException -> 0x008e }
            if (r4 != 0) goto L_0x0021
            goto L_0x006b
        L_0x0021:
            int r5 = r3.indexOf(r14)     // Catch:{ IOException -> 0x008e }
            if (r5 >= 0) goto L_0x0028
            goto L_0x006b
        L_0x0028:
            java.io.InputStream r6 = r1.getInputStream(r4)     // Catch:{ IOException -> 0x008e }
            long r7 = r4.getSize()     // Catch:{ IOException -> 0x008e }
            int r4 = (int) r7     // Catch:{ IOException -> 0x008e }
            java.lang.String r3 = r3.substring(r5)     // Catch:{ IOException -> 0x008e }
            r5 = 0
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x0081, all -> 0x0075 }
            r7.<init>(r13, r3)     // Catch:{ Exception -> 0x0081, all -> 0x0075 }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0081, all -> 0x0075 }
            r3.<init>(r7)     // Catch:{ Exception -> 0x0081, all -> 0x0075 }
            java.io.BufferedOutputStream r7 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0082, all -> 0x0073 }
            r7.<init>(r3, r4)     // Catch:{ Exception -> 0x0082, all -> 0x0073 }
            byte[] r5 = new byte[r4]     // Catch:{ Exception -> 0x0071, all -> 0x006e }
            r8 = 0
        L_0x0048:
            if (r8 >= r4) goto L_0x0056
            int r9 = r6.read(r5)     // Catch:{ Exception -> 0x0071, all -> 0x006e }
            r10 = -1
            if (r9 == r10) goto L_0x0056
            r7.write(r5, r0, r9)     // Catch:{ Exception -> 0x0071, all -> 0x006e }
            int r8 = r8 + r9
            goto L_0x0048
        L_0x0056:
            if (r8 == r4) goto L_0x0062
            r7.flush()     // Catch:{ Exception -> 0x0061 }
            r7.close()     // Catch:{ Exception -> 0x0061 }
            r3.close()     // Catch:{ Exception -> 0x0061 }
        L_0x0061:
            return r0
        L_0x0062:
            r7.flush()     // Catch:{ Exception -> 0x006b }
            r7.close()     // Catch:{ Exception -> 0x006b }
            r3.close()     // Catch:{ Exception -> 0x006b }
        L_0x006b:
            int r2 = r2 + 1
            goto L_0x000f
        L_0x006e:
            r12 = move-exception
            r5 = r7
            goto L_0x0077
        L_0x0071:
            r5 = r7
            goto L_0x0082
        L_0x0073:
            r12 = move-exception
            goto L_0x0077
        L_0x0075:
            r12 = move-exception
            r3 = r5
        L_0x0077:
            r5.flush()     // Catch:{ Exception -> 0x0080 }
            r5.close()     // Catch:{ Exception -> 0x0080 }
            r3.close()     // Catch:{ Exception -> 0x0080 }
        L_0x0080:
            throw r12     // Catch:{ IOException -> 0x008e }
        L_0x0081:
            r3 = r5
        L_0x0082:
            r5.flush()     // Catch:{ Exception -> 0x008b }
            r5.close()     // Catch:{ Exception -> 0x008b }
            r3.close()     // Catch:{ Exception -> 0x008b }
        L_0x008b:
            return r0
        L_0x008c:
            r12 = 1
            return r12
        L_0x008e:
            return r0
        L_0x008f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.securityjni.SGJpgProcess.unzipByPrefix(java.lang.String, java.lang.String, java.lang.String, java.lang.String[]):boolean");
    }

    private boolean deleteDir(File file) {
        if (file == null) {
            return false;
        }
        if (file.isDirectory()) {
            String[] list = file.list();
            int i = 0;
            while (list != null && i < list.length) {
                if (!deleteDir(new File(file, list[i]))) {
                    return false;
                }
                i++;
            }
        }
        return file.delete();
    }

    private boolean writeEmptyFile(File file) {
        FileOutputStream fileOutputStream = null;
        if (file == null) {
            try {
                fileOutputStream.close();
            } catch (IOException unused) {
            }
            return false;
        }
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(file.getAbsolutePath());
            try {
                fileOutputStream2.close();
                try {
                    fileOutputStream2.close();
                } catch (IOException unused2) {
                }
                return true;
            } catch (IOException e) {
                e = e;
                fileOutputStream = fileOutputStream2;
                try {
                    e.printStackTrace();
                    try {
                        fileOutputStream.close();
                    } catch (IOException unused3) {
                    }
                    return false;
                } catch (Throwable th) {
                    th = th;
                    try {
                        fileOutputStream.close();
                    } catch (IOException unused4) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                fileOutputStream.close();
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            fileOutputStream.close();
            return false;
        }
    }

    private static String getProcessName(Context context) {
        try {
            int myPid = Process.myPid();
            if (context == null) {
                return "";
            }
            for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (next.pid == myPid) {
                    return next.processName != null ? next.processName : "";
                }
            }
            return "";
        } catch (Throwable unused) {
            return "";
        }
    }

    private boolean isMainProcess() {
        try {
            return getProcessName(this.ctx).equals(this.ctx.getPackageName());
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean unzipFinished() {
        return unzipFinished(DEFAULT_FILES);
    }

    public boolean unzipFinished(String[] strArr) {
        try {
            if (this.ctx != null) {
                if (isMainProcess()) {
                    String absolutePath = this.ctx.getDir(ROOT_FOLDER, 0).getAbsolutePath();
                    String str = this.ctx.getApplicationInfo().sourceDir;
                    String str2 = null;
                    if (str != null) {
                        File file = new File(str);
                        if (file.exists()) {
                            str2 = absolutePath + "/app_" + (file.lastModified() / 1000);
                        }
                        File file2 = new File(str2);
                        if (!file2.exists()) {
                            file2.mkdir();
                        }
                        str2 = str2 + "/pre_unzip_jpgs";
                        File file3 = new File(str2);
                        if (!file3.exists()) {
                            file3.mkdir();
                        }
                    }
                    File file4 = new File(str2, FINISHED_FILE_NAME);
                    if (file4.exists()) {
                        return true;
                    }
                    if (unzipByAssetManager(str2, JPG_PREFIX, strArr) || unzipByPrefix(str, str2, JPG_PREFIX, strArr)) {
                        return writeEmptyFile(file4);
                    }
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x007a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean unzipByAssetManager(java.lang.String r12, java.lang.String r13, java.lang.String[] r14) {
        /*
            r11 = this;
            int r0 = r14.length
            r1 = 0
            r2 = 0
        L_0x0003:
            if (r2 >= r0) goto L_0x0093
            r3 = r14[r2]
            boolean r4 = r11.isPathSecurityValid(r3)
            if (r4 != 0) goto L_0x000f
            goto L_0x008e
        L_0x000f:
            int r4 = r3.indexOf(r13)
            if (r4 >= 0) goto L_0x0017
            goto L_0x008e
        L_0x0017:
            r5 = -1
            android.content.Context r6 = r11.ctx     // Catch:{ Exception -> 0x0085 }
            android.content.res.AssetManager r6 = r6.getAssets()     // Catch:{ Exception -> 0x0085 }
            java.io.InputStream r6 = r11.openNonAsset(r6, r3)     // Catch:{ Exception -> 0x0085 }
            if (r6 != 0) goto L_0x0025
            return r1
        L_0x0025:
            java.lang.String r3 = r3.substring(r4)
            r4 = 0
            int r7 = r6.available()     // Catch:{ Exception -> 0x0079, all -> 0x006c }
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x0079, all -> 0x006c }
            r8.<init>(r12, r3)     // Catch:{ Exception -> 0x0079, all -> 0x006c }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0079, all -> 0x006c }
            r3.<init>(r8)     // Catch:{ Exception -> 0x0079, all -> 0x006c }
            java.io.BufferedOutputStream r8 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x007a, all -> 0x006a }
            r8.<init>(r3)     // Catch:{ Exception -> 0x007a, all -> 0x006a }
            byte[] r4 = new byte[r7]     // Catch:{ Exception -> 0x0068, all -> 0x0065 }
            r9 = 0
        L_0x0040:
            if (r9 >= r7) goto L_0x004d
            int r10 = r6.read(r4)     // Catch:{ Exception -> 0x0068, all -> 0x0065 }
            if (r10 == r5) goto L_0x004d
            r8.write(r4, r1, r10)     // Catch:{ Exception -> 0x0068, all -> 0x0065 }
            int r9 = r9 + r10
            goto L_0x0040
        L_0x004d:
            if (r9 == r7) goto L_0x005a
            r8.flush()     // Catch:{ Exception -> 0x0059 }
            r8.close()     // Catch:{ Exception -> 0x0059 }
            r3.close()     // Catch:{ Exception -> 0x0059 }
            return r1
        L_0x0059:
            return r1
        L_0x005a:
            r8.flush()     // Catch:{ Exception -> 0x0064 }
            r8.close()     // Catch:{ Exception -> 0x0064 }
            r3.close()     // Catch:{ Exception -> 0x0064 }
            goto L_0x008e
        L_0x0064:
            return r1
        L_0x0065:
            r12 = move-exception
            r4 = r8
            goto L_0x006e
        L_0x0068:
            r4 = r8
            goto L_0x007a
        L_0x006a:
            r12 = move-exception
            goto L_0x006e
        L_0x006c:
            r12 = move-exception
            r3 = r4
        L_0x006e:
            r4.flush()     // Catch:{ Exception -> 0x0078 }
            r4.close()     // Catch:{ Exception -> 0x0078 }
            r3.close()     // Catch:{ Exception -> 0x0078 }
            throw r12
        L_0x0078:
            return r1
        L_0x0079:
            r3 = r4
        L_0x007a:
            r4.flush()     // Catch:{ Exception -> 0x0084 }
            r4.close()     // Catch:{ Exception -> 0x0084 }
            r3.close()     // Catch:{ Exception -> 0x0084 }
            return r1
        L_0x0084:
            return r1
        L_0x0085:
            java.lang.String r4 = "sharetoken"
            int r3 = r3.indexOf(r4)
            if (r3 == r5) goto L_0x0092
        L_0x008e:
            int r2 = r2 + 1
            goto L_0x0003
        L_0x0092:
            return r1
        L_0x0093:
            r12 = 1
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.securityjni.SGJpgProcess.unzipByAssetManager(java.lang.String, java.lang.String, java.lang.String[]):boolean");
    }

    public InputStream openNonAsset(@NonNull AssetManager assetManager, @NonNull String str) {
        fetchOpenNonAssetMethod();
        if (sOpenNonAssetMethod == null) {
            return null;
        }
        try {
            return (InputStream) sOpenNonAssetMethod.invoke(assetManager, new Object[]{str});
        } catch (IllegalAccessException unused) {
            return null;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private void fetchOpenNonAssetMethod() {
        if (!sOpenNonAssetMethodFetched) {
            try {
                sOpenNonAssetMethod = AssetManager.class.getDeclaredMethod("openNonAsset", new Class[]{String.class});
                sOpenNonAssetMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i(TAG, "Failed to retrieve openNonAsset method", e);
            }
            sOpenNonAssetMethodFetched = true;
        }
    }
}
