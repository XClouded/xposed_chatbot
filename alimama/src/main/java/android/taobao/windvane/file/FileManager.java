package android.taobao.windvane.file;

import android.app.Application;
import android.content.Context;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileManager {
    static final int BUFFER = 1024;
    private static final String TAG = "FileManager";
    public static final String UNZIP_SUCCESS = "success";

    public static File createFolder(Context context, String str) {
        String absolutePath = context.getFilesDir().getAbsolutePath();
        if (!TextUtils.isEmpty(str)) {
            absolutePath = absolutePath + File.separator + str;
        }
        File file = new File(absolutePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static boolean unzip(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        try {
            return unzip((InputStream) new FileInputStream(str), str2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean unzip(byte[] bArr, String str) {
        return unzip((InputStream) new ByteArrayInputStream(bArr), str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:54:0x00f0 A[SYNTHETIC, Splitter:B:54:0x00f0] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00f8 A[Catch:{ IOException -> 0x00f4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00fd A[Catch:{ IOException -> 0x00f4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x011f A[SYNTHETIC, Splitter:B:66:0x011f] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0127 A[Catch:{ IOException -> 0x0123 }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x012c A[Catch:{ IOException -> 0x0123 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean unzip(java.io.InputStream r10, java.lang.String r11) {
        /*
            r0 = 0
            if (r10 == 0) goto L_0x014b
            if (r11 != 0) goto L_0x0007
            goto L_0x014b
        L_0x0007:
            java.lang.String r1 = "/"
            boolean r1 = r11.endsWith(r1)
            if (r1 != 0) goto L_0x0020
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r11)
            java.lang.String r11 = "/"
            r1.append(r11)
            java.lang.String r11 = r1.toString()
        L_0x0020:
            r1 = 0
            java.util.zip.ZipInputStream r2 = new java.util.zip.ZipInputStream     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            r2.<init>(r10)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r3]     // Catch:{ Exception -> 0x00cd }
            java.lang.StringBuffer r5 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x00cd }
            r6 = 200(0xc8, float:2.8E-43)
            r5.<init>(r6)     // Catch:{ Exception -> 0x00cd }
        L_0x0031:
            java.util.zip.ZipEntry r6 = r2.getNextEntry()     // Catch:{ Exception -> 0x00cd }
            if (r6 == 0) goto L_0x00a0
            java.lang.String r7 = r6.getName()     // Catch:{ Exception -> 0x00cd }
            r5.append(r7)     // Catch:{ Exception -> 0x00cd }
            java.lang.String r7 = r5.toString()     // Catch:{ Exception -> 0x00cd }
            java.lang.String r8 = "../"
            boolean r7 = r7.contains(r8)     // Catch:{ Exception -> 0x00cd }
            if (r7 == 0) goto L_0x004b
            goto L_0x0031
        L_0x004b:
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x00cd }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cd }
            r8.<init>()     // Catch:{ Exception -> 0x00cd }
            r8.append(r11)     // Catch:{ Exception -> 0x00cd }
            java.lang.String r9 = r5.toString()     // Catch:{ Exception -> 0x00cd }
            r8.append(r9)     // Catch:{ Exception -> 0x00cd }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x00cd }
            r7.<init>(r8)     // Catch:{ Exception -> 0x00cd }
            int r8 = r5.length()     // Catch:{ Exception -> 0x00cd }
            r5.delete(r0, r8)     // Catch:{ Exception -> 0x00cd }
            boolean r6 = r6.isDirectory()     // Catch:{ Exception -> 0x00cd }
            if (r6 == 0) goto L_0x0074
            r7.mkdirs()     // Catch:{ Exception -> 0x00cd }
            goto L_0x0031
        L_0x0074:
            java.io.File r6 = r7.getParentFile()     // Catch:{ Exception -> 0x00cd }
            boolean r6 = r6.exists()     // Catch:{ Exception -> 0x00cd }
            if (r6 != 0) goto L_0x0085
            java.io.File r6 = r7.getParentFile()     // Catch:{ Exception -> 0x00cd }
            r6.mkdirs()     // Catch:{ Exception -> 0x00cd }
        L_0x0085:
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00cd }
            r6.<init>(r7)     // Catch:{ Exception -> 0x00cd }
        L_0x008a:
            int r1 = r2.read(r4, r0, r3)     // Catch:{ Exception -> 0x009d, all -> 0x0099 }
            if (r1 <= 0) goto L_0x0094
            r6.write(r4, r0, r1)     // Catch:{ Exception -> 0x009d, all -> 0x0099 }
            goto L_0x008a
        L_0x0094:
            r6.close()     // Catch:{ Exception -> 0x009d, all -> 0x0099 }
            r1 = r6
            goto L_0x0031
        L_0x0099:
            r11 = move-exception
            r1 = r6
            goto L_0x011d
        L_0x009d:
            r11 = move-exception
            r1 = r6
            goto L_0x00d4
        L_0x00a0:
            r11 = 1
            if (r1 == 0) goto L_0x00a9
            r1.close()     // Catch:{ IOException -> 0x00a7 }
            goto L_0x00a9
        L_0x00a7:
            r10 = move-exception
            goto L_0x00b2
        L_0x00a9:
            r2.close()     // Catch:{ IOException -> 0x00a7 }
            if (r10 == 0) goto L_0x00cc
            r10.close()     // Catch:{ IOException -> 0x00a7 }
            goto L_0x00cc
        L_0x00b2:
            java.lang.String r0 = "FileManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "close Stream Exception:"
            r1.append(r2)
            java.lang.String r10 = r10.getMessage()
            r1.append(r10)
            java.lang.String r10 = r1.toString()
            android.taobao.windvane.util.TaoLog.e(r0, r10)
        L_0x00cc:
            return r11
        L_0x00cd:
            r11 = move-exception
            goto L_0x00d4
        L_0x00cf:
            r11 = move-exception
            r2 = r1
            goto L_0x011d
        L_0x00d2:
            r11 = move-exception
            r2 = r1
        L_0x00d4:
            java.lang.String r3 = "FileManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x011c }
            r4.<init>()     // Catch:{ all -> 0x011c }
            java.lang.String r5 = "unzip: IOException:"
            r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.String r11 = r11.getMessage()     // Catch:{ all -> 0x011c }
            r4.append(r11)     // Catch:{ all -> 0x011c }
            java.lang.String r11 = r4.toString()     // Catch:{ all -> 0x011c }
            android.taobao.windvane.util.TaoLog.e(r3, r11)     // Catch:{ all -> 0x011c }
            if (r1 == 0) goto L_0x00f6
            r1.close()     // Catch:{ IOException -> 0x00f4 }
            goto L_0x00f6
        L_0x00f4:
            r10 = move-exception
            goto L_0x0101
        L_0x00f6:
            if (r2 == 0) goto L_0x00fb
            r2.close()     // Catch:{ IOException -> 0x00f4 }
        L_0x00fb:
            if (r10 == 0) goto L_0x011b
            r10.close()     // Catch:{ IOException -> 0x00f4 }
            goto L_0x011b
        L_0x0101:
            java.lang.String r11 = "FileManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "close Stream Exception:"
            r1.append(r2)
            java.lang.String r10 = r10.getMessage()
            r1.append(r10)
            java.lang.String r10 = r1.toString()
            android.taobao.windvane.util.TaoLog.e(r11, r10)
        L_0x011b:
            return r0
        L_0x011c:
            r11 = move-exception
        L_0x011d:
            if (r1 == 0) goto L_0x0125
            r1.close()     // Catch:{ IOException -> 0x0123 }
            goto L_0x0125
        L_0x0123:
            r10 = move-exception
            goto L_0x0130
        L_0x0125:
            if (r2 == 0) goto L_0x012a
            r2.close()     // Catch:{ IOException -> 0x0123 }
        L_0x012a:
            if (r10 == 0) goto L_0x014a
            r10.close()     // Catch:{ IOException -> 0x0123 }
            goto L_0x014a
        L_0x0130:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "close Stream Exception:"
            r0.append(r1)
            java.lang.String r10 = r10.getMessage()
            r0.append(r10)
            java.lang.String r10 = r0.toString()
            java.lang.String r0 = "FileManager"
            android.taobao.windvane.util.TaoLog.e(r0, r10)
        L_0x014a:
            throw r11
        L_0x014b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileManager.unzip(java.io.InputStream, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0050 A[SYNTHETIC, Splitter:B:27:0x0050] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0071 A[SYNTHETIC, Splitter:B:34:0x0071] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0077 A[SYNTHETIC, Splitter:B:37:0x0077] */
    /* JADX WARNING: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyFile(java.io.InputStream r5, java.io.File r6) {
        /*
            if (r5 == 0) goto L_0x0080
            if (r6 != 0) goto L_0x0006
            goto L_0x0080
        L_0x0006:
            r0 = 0
            r6.createNewFile()     // Catch:{ FileNotFoundException -> 0x0054, IOException -> 0x0033 }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0054, IOException -> 0x0033 }
            r1.<init>(r6)     // Catch:{ FileNotFoundException -> 0x0054, IOException -> 0x0033 }
            r6 = 2048(0x800, float:2.87E-42)
            byte[] r0 = new byte[r6]     // Catch:{ FileNotFoundException -> 0x002e, IOException -> 0x002b, all -> 0x0028 }
        L_0x0013:
            r2 = 0
            int r3 = r5.read(r0, r2, r6)     // Catch:{ FileNotFoundException -> 0x002e, IOException -> 0x002b, all -> 0x0028 }
            r4 = -1
            if (r3 == r4) goto L_0x001f
            r1.write(r0, r2, r3)     // Catch:{ FileNotFoundException -> 0x002e, IOException -> 0x002b, all -> 0x0028 }
            goto L_0x0013
        L_0x001f:
            r1.close()     // Catch:{ IOException -> 0x0023 }
            goto L_0x0074
        L_0x0023:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0074
        L_0x0028:
            r5 = move-exception
            r0 = r1
            goto L_0x0075
        L_0x002b:
            r5 = move-exception
            r0 = r1
            goto L_0x0034
        L_0x002e:
            r5 = move-exception
            r0 = r1
            goto L_0x0055
        L_0x0031:
            r5 = move-exception
            goto L_0x0075
        L_0x0033:
            r5 = move-exception
        L_0x0034:
            java.lang.String r6 = "FileManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0031 }
            r1.<init>()     // Catch:{ all -> 0x0031 }
            java.lang.String r2 = "copyFile: write IOException:"
            r1.append(r2)     // Catch:{ all -> 0x0031 }
            java.lang.String r5 = r5.getMessage()     // Catch:{ all -> 0x0031 }
            r1.append(r5)     // Catch:{ all -> 0x0031 }
            java.lang.String r5 = r1.toString()     // Catch:{ all -> 0x0031 }
            android.taobao.windvane.util.TaoLog.e(r6, r5)     // Catch:{ all -> 0x0031 }
            if (r0 == 0) goto L_0x0074
            r0.close()     // Catch:{ IOException -> 0x0023 }
            goto L_0x0074
        L_0x0054:
            r5 = move-exception
        L_0x0055:
            java.lang.String r6 = "FileManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0031 }
            r1.<init>()     // Catch:{ all -> 0x0031 }
            java.lang.String r2 = "copyFile: dest FileNotFoundException:"
            r1.append(r2)     // Catch:{ all -> 0x0031 }
            java.lang.String r5 = r5.getMessage()     // Catch:{ all -> 0x0031 }
            r1.append(r5)     // Catch:{ all -> 0x0031 }
            java.lang.String r5 = r1.toString()     // Catch:{ all -> 0x0031 }
            android.taobao.windvane.util.TaoLog.e(r6, r5)     // Catch:{ all -> 0x0031 }
            if (r0 == 0) goto L_0x0074
            r0.close()     // Catch:{ IOException -> 0x0023 }
        L_0x0074:
            return
        L_0x0075:
            if (r0 == 0) goto L_0x007f
            r0.close()     // Catch:{ IOException -> 0x007b }
            goto L_0x007f
        L_0x007b:
            r6 = move-exception
            r6.printStackTrace()
        L_0x007f:
            throw r5
        L_0x0080:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileManager.copyFile(java.io.InputStream, java.io.File):void");
    }

    public static String createBaseDir(Application application, String str, String str2, boolean z) {
        StringBuilder sb;
        String str3 = null;
        if (z) {
            File externalCacheDir = application.getExternalCacheDir();
            if (externalCacheDir != null) {
                sb = new StringBuilder();
                sb.append(externalCacheDir.getAbsolutePath());
                sb.append(File.separator);
            } else {
                File externalCacheDir2 = CommonUtils.getExternalCacheDir(application);
                if (externalCacheDir2 != null) {
                    sb = new StringBuilder();
                    sb.append(externalCacheDir2.getAbsolutePath());
                    sb.append(File.separator);
                } else {
                    sb = null;
                }
            }
            if (!TextUtils.isEmpty(str) && sb != null) {
                sb.append(str);
                sb.append(File.separator);
                sb.append(str2);
                str3 = sb.toString();
            }
        }
        if (str3 == null) {
            str3 = createInnerCacheStorage(application, str, str2);
        }
        TaoLog.d(TAG, "createBaseDir path:" + str3);
        return str3;
    }

    public static String createInnerCacheStorage(Application application, String str, String str2) {
        if (application.getFilesDir() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(application.getCacheDir().getAbsolutePath());
        if (!TextUtils.isEmpty(str)) {
            sb.append(File.separator);
            sb.append(str);
        }
        sb.append(File.separator);
        sb.append(str2);
        String sb2 = sb.toString();
        TaoLog.d(TAG, "createInnerCacheStorage path:" + sb2);
        return sb2;
    }

    public static String createInnerfileStorage(Application application, String str, String str2) {
        if (application.getFilesDir() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(application.getFilesDir().getAbsolutePath());
        if (!TextUtils.isEmpty(str)) {
            sb.append(File.separator);
            sb.append(str);
        }
        sb.append(File.separator);
        sb.append(str2);
        String sb2 = sb.toString();
        TaoLog.d(TAG, "createInnerfileStorage path:" + sb2);
        return sb2;
    }

    public static boolean copy(String str, String str2) {
        return copy(new File(str), new File(str2));
    }

    public static boolean copy(File file, File file2) {
        return copy(file, file2, (byte[]) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x005b A[Catch:{ Exception -> 0x004a, all -> 0x0044 }, LOOP:0: B:24:0x0054->B:26:0x005b, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0086 A[SYNTHETIC, Splitter:B:49:0x0086] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0090 A[SYNTHETIC, Splitter:B:54:0x0090] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x009c A[SYNTHETIC, Splitter:B:61:0x009c] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00a6 A[SYNTHETIC, Splitter:B:66:0x00a6] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x005f A[EDGE_INSN: B:71:0x005f->B:27:0x005f ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copy(java.io.File r4, java.io.File r5, byte[] r6) {
        /*
            r0 = 0
            r1 = 0
            boolean r2 = r4.exists()     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            if (r2 != 0) goto L_0x0029
            boolean r5 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            if (r5 == 0) goto L_0x0028
            java.lang.String r5 = "FileManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            r6.<init>()     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            java.lang.String r2 = "src file not exist, "
            r6.append(r2)     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            java.io.File r4 = r4.getAbsoluteFile()     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            r6.append(r4)     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            java.lang.String r4 = r6.toString()     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            android.taobao.windvane.util.TaoLog.w(r5, r4)     // Catch:{ Exception -> 0x007f, all -> 0x007c }
        L_0x0028:
            return r1
        L_0x0029:
            boolean r2 = r5.exists()     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            if (r2 != 0) goto L_0x0032
            r5.createNewFile()     // Catch:{ Exception -> 0x007f, all -> 0x007c }
        L_0x0032:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            r2.<init>(r4)     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0078, all -> 0x0074 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0078, all -> 0x0074 }
            if (r6 == 0) goto L_0x0050
            int r5 = r6.length     // Catch:{ Exception -> 0x004a, all -> 0x0044 }
            r0 = 10
            if (r5 >= r0) goto L_0x0054
            goto L_0x0050
        L_0x0044:
            r5 = move-exception
            r0 = r2
            r3 = r5
            r5 = r4
            r4 = r3
            goto L_0x009a
        L_0x004a:
            r5 = move-exception
            r0 = r2
            r3 = r5
            r5 = r4
            r4 = r3
            goto L_0x0081
        L_0x0050:
            r5 = 2048(0x800, float:2.87E-42)
            byte[] r6 = new byte[r5]     // Catch:{ Exception -> 0x004a, all -> 0x0044 }
        L_0x0054:
            int r5 = r2.read(r6)     // Catch:{ Exception -> 0x004a, all -> 0x0044 }
            r0 = -1
            if (r5 == r0) goto L_0x005f
            r4.write(r6, r1, r5)     // Catch:{ Exception -> 0x004a, all -> 0x0044 }
            goto L_0x0054
        L_0x005f:
            r4.flush()     // Catch:{ Exception -> 0x004a, all -> 0x0044 }
            r5 = 1
            r2.close()     // Catch:{ IOException -> 0x0067 }
            goto L_0x006b
        L_0x0067:
            r6 = move-exception
            r6.printStackTrace()
        L_0x006b:
            r4.close()     // Catch:{ IOException -> 0x006f }
            goto L_0x0073
        L_0x006f:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0073:
            return r5
        L_0x0074:
            r4 = move-exception
            r5 = r0
            r0 = r2
            goto L_0x009a
        L_0x0078:
            r4 = move-exception
            r5 = r0
            r0 = r2
            goto L_0x0081
        L_0x007c:
            r4 = move-exception
            r5 = r0
            goto L_0x009a
        L_0x007f:
            r4 = move-exception
            r5 = r0
        L_0x0081:
            r4.printStackTrace()     // Catch:{ all -> 0x0099 }
            if (r0 == 0) goto L_0x008e
            r0.close()     // Catch:{ IOException -> 0x008a }
            goto L_0x008e
        L_0x008a:
            r4 = move-exception
            r4.printStackTrace()
        L_0x008e:
            if (r5 == 0) goto L_0x0098
            r5.close()     // Catch:{ IOException -> 0x0094 }
            goto L_0x0098
        L_0x0094:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0098:
            return r1
        L_0x0099:
            r4 = move-exception
        L_0x009a:
            if (r0 == 0) goto L_0x00a4
            r0.close()     // Catch:{ IOException -> 0x00a0 }
            goto L_0x00a4
        L_0x00a0:
            r6 = move-exception
            r6.printStackTrace()
        L_0x00a4:
            if (r5 == 0) goto L_0x00ae
            r5.close()     // Catch:{ IOException -> 0x00aa }
            goto L_0x00ae
        L_0x00aa:
            r5 = move-exception
            r5.printStackTrace()
        L_0x00ae:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileManager.copy(java.io.File, java.io.File, byte[]):boolean");
    }

    public static boolean copyDir(String str, String str2) {
        String formatUrl = formatUrl(str);
        String formatUrl2 = formatUrl(str2);
        new File(formatUrl2).mkdirs();
        File[] listFiles = new File(formatUrl).listFiles();
        byte[] bArr = new byte[2048];
        if (listFiles == null) {
            return false;
        }
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isFile()) {
                if (!copy(listFiles[i], new File(new File(formatUrl2).getAbsolutePath() + File.separator + listFiles[i].getName()), bArr)) {
                    return false;
                }
            }
            if (listFiles[i].isDirectory()) {
                if (!copyDir(formatUrl + File.separator + listFiles[i].getName(), formatUrl2 + File.separator + listFiles[i].getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static String formatUrl(String str) {
        String replaceAll = str.replaceAll("//", "/");
        return replaceAll.endsWith("/") ? replaceAll.substring(0, replaceAll.length() - 1) : replaceAll;
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x025a A[SYNTHETIC, Splitter:B:100:0x025a] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0132 A[SYNTHETIC, Splitter:B:57:0x0132] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0153 A[SYNTHETIC, Splitter:B:64:0x0153] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x020c A[SYNTHETIC, Splitter:B:91:0x020c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String unZipByFilePath(java.lang.String r11, java.lang.String r12) {
        /*
            long r0 = java.lang.System.currentTimeMillis()
            com.uc.crashsdk.export.CrashApi r2 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r3 = "wv_zip_url"
            r2.addHeaderInfo(r3, r11)     // Catch:{ Throwable -> 0x0060 }
            com.uc.crashsdk.export.CrashApi r2 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r3 = "device_identifier"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0060 }
            r4.<init>()     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r5 = android.os.Build.BRAND     // Catch:{ Throwable -> 0x0060 }
            r4.append(r5)     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r5 = "@"
            r4.append(r5)     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r5 = android.os.Build.VERSION.RELEASE     // Catch:{ Throwable -> 0x0060 }
            r4.append(r5)     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x0060 }
            r2.addHeaderInfo(r3, r4)     // Catch:{ Throwable -> 0x0060 }
            com.uc.crashsdk.export.CrashApi r2 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r3 = "config_version"
            android.taobao.windvane.config.WVCommonConfigData r4 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r4 = r4.v     // Catch:{ Throwable -> 0x0060 }
            r2.addHeaderInfo(r3, r4)     // Catch:{ Throwable -> 0x0060 }
            com.uc.crashsdk.export.CrashApi r2 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r3 = "zip_degrade_config"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0060 }
            r4.<init>()     // Catch:{ Throwable -> 0x0060 }
            android.taobao.windvane.config.WVCommonConfigData r5 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Throwable -> 0x0060 }
            int r5 = r5.zipDegradeMode     // Catch:{ Throwable -> 0x0060 }
            r4.append(r5)     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r5 = " / "
            r4.append(r5)     // Catch:{ Throwable -> 0x0060 }
            android.taobao.windvane.config.WVCommonConfigData r5 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r5 = r5.zipDegradeList     // Catch:{ Throwable -> 0x0060 }
            r4.append(r5)     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x0060 }
            r2.addHeaderInfo(r3, r4)     // Catch:{ Throwable -> 0x0060 }
        L_0x0060:
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x01eb }
            r3.<init>(r12)     // Catch:{ Exception -> 0x01eb }
            boolean r4 = r3.exists()     // Catch:{ Exception -> 0x01eb }
            if (r4 != 0) goto L_0x006f
            r3.mkdirs()     // Catch:{ Exception -> 0x01eb }
        L_0x006f:
            java.util.zip.ZipFile r3 = new java.util.zip.ZipFile     // Catch:{ Exception -> 0x01eb }
            r3.<init>(r11)     // Catch:{ Exception -> 0x01eb }
            java.util.Enumeration r11 = r3.entries()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r4 = r2
            r5 = r4
        L_0x007a:
            boolean r6 = r11.hasMoreElements()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            if (r6 == 0) goto L_0x0173
            java.lang.Object r6 = r11.nextElement()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.util.zip.ZipEntry r6 = (java.util.zip.ZipEntry) r6     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r7 = 1024(0x400, float:1.435E-42)
            byte[] r8 = new byte[r7]     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r8 = r6.getName()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            if (r8 == 0) goto L_0x007a
            java.lang.String r9 = "../"
            boolean r9 = r8.contains(r9)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            if (r9 == 0) goto L_0x0099
            goto L_0x007a
        L_0x0099:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r9.<init>()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r9.append(r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r10 = "/"
            r9.append(r10)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r9.append(r8)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r8 = r9.toString()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.io.File r9 = new java.io.File     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r9.<init>(r8)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            boolean r10 = r6.isDirectory()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            if (r10 == 0) goto L_0x00bc
            r9.mkdirs()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            goto L_0x00cc
        L_0x00bc:
            boolean r10 = r9.exists()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            if (r10 != 0) goto L_0x00cc
            java.io.File r10 = r9.getParentFile()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r10.mkdirs()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r9.createNewFile()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
        L_0x00cc:
            java.io.InputStream r6 = r3.getInputStream(r6)     // Catch:{ all -> 0x012e }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ all -> 0x012c }
            r5.<init>(r8)     // Catch:{ all -> 0x012c }
            byte[] r4 = new byte[r7]     // Catch:{ all -> 0x0129 }
        L_0x00d7:
            int r7 = r6.read(r4)     // Catch:{ all -> 0x0129 }
            r8 = -1
            if (r7 == r8) goto L_0x00e3
            r8 = 0
            r5.write(r4, r8, r7)     // Catch:{ all -> 0x0129 }
            goto L_0x00d7
        L_0x00e3:
            r5.close()     // Catch:{ IOException -> 0x00e8 }
            r4 = r2
            goto L_0x0104
        L_0x00e8:
            r4 = move-exception
            java.lang.String r7 = "FileManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r8.<init>()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r9 = "stream failed to close : "
            r8.append(r9)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r4 = r4.getMessage()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r8.append(r4)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r4 = r8.toString()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            android.taobao.windvane.util.TaoLog.e(r7, r4)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r4 = r5
        L_0x0104:
            if (r6 == 0) goto L_0x0126
            r6.close()     // Catch:{ IOException -> 0x010b }
            r6 = r2
            goto L_0x0126
        L_0x010b:
            r5 = move-exception
            java.lang.String r7 = "FileManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r8.<init>()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r9 = "stream failed to close : "
            r8.append(r9)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r5 = r5.getMessage()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r8.append(r5)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r5 = r8.toString()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            android.taobao.windvane.util.TaoLog.e(r7, r5)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
        L_0x0126:
            r5 = r6
            goto L_0x007a
        L_0x0129:
            r11 = move-exception
            r4 = r5
            goto L_0x0130
        L_0x012c:
            r11 = move-exception
            goto L_0x0130
        L_0x012e:
            r11 = move-exception
            r6 = r5
        L_0x0130:
            if (r4 == 0) goto L_0x0151
            r4.close()     // Catch:{ IOException -> 0x0136 }
            goto L_0x0151
        L_0x0136:
            r12 = move-exception
            java.lang.String r0 = "FileManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r1.<init>()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r2 = "stream failed to close : "
            r1.append(r2)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r12 = r12.getMessage()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r1.append(r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r12 = r1.toString()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            android.taobao.windvane.util.TaoLog.e(r0, r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
        L_0x0151:
            if (r6 == 0) goto L_0x0172
            r6.close()     // Catch:{ IOException -> 0x0157 }
            goto L_0x0172
        L_0x0157:
            r12 = move-exception
            java.lang.String r0 = "FileManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r1.<init>()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r2 = "stream failed to close : "
            r1.append(r2)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r12 = r12.getMessage()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            r1.append(r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            java.lang.String r12 = r1.toString()     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
            android.taobao.windvane.util.TaoLog.e(r0, r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
        L_0x0172:
            throw r11     // Catch:{ Exception -> 0x01e5, all -> 0x01e2 }
        L_0x0173:
            r3.close()     // Catch:{ IOException -> 0x0177 }
            goto L_0x0192
        L_0x0177:
            r11 = move-exception
            java.lang.String r12 = "FileManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "zipfile failed to close : "
            r2.append(r3)
            java.lang.String r11 = r11.getMessage()
            r2.append(r11)
            java.lang.String r11 = r2.toString()
            android.taobao.windvane.util.TaoLog.e(r12, r11)
        L_0x0192:
            com.uc.crashsdk.export.CrashApi r11 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x01be }
            java.lang.String r12 = "wv_zip_url"
            java.lang.String r2 = ""
            r11.addHeaderInfo(r12, r2)     // Catch:{ Throwable -> 0x01be }
            com.uc.crashsdk.export.CrashApi r11 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x01be }
            java.lang.String r12 = "device_identifier"
            java.lang.String r2 = ""
            r11.addHeaderInfo(r12, r2)     // Catch:{ Throwable -> 0x01be }
            com.uc.crashsdk.export.CrashApi r11 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x01be }
            java.lang.String r12 = "config_version"
            java.lang.String r2 = ""
            r11.addHeaderInfo(r12, r2)     // Catch:{ Throwable -> 0x01be }
            com.uc.crashsdk.export.CrashApi r11 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x01be }
            java.lang.String r12 = "zip_degrade_config"
            java.lang.String r2 = ""
            r11.addHeaderInfo(r12, r2)     // Catch:{ Throwable -> 0x01be }
        L_0x01be:
            boolean r11 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r11 == 0) goto L_0x01df
            java.lang.String r11 = "FileManager"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r2 = "unZipByFilePath use time(ms) : "
            r12.append(r2)
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r0
            r12.append(r2)
            java.lang.String r12 = r12.toString()
            android.taobao.windvane.util.TaoLog.d(r11, r12)
        L_0x01df:
            java.lang.String r11 = "success"
            return r11
        L_0x01e2:
            r11 = move-exception
            goto L_0x0258
        L_0x01e5:
            r11 = move-exception
            r2 = r3
            goto L_0x01ec
        L_0x01e8:
            r11 = move-exception
            r3 = r2
            goto L_0x0258
        L_0x01eb:
            r11 = move-exception
        L_0x01ec:
            java.lang.String r12 = "FileManager"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x01e8 }
            r0.<init>()     // Catch:{ all -> 0x01e8 }
            java.lang.String r1 = "unZipByFilePath failed : "
            r0.append(r1)     // Catch:{ all -> 0x01e8 }
            java.lang.String r1 = r11.getMessage()     // Catch:{ all -> 0x01e8 }
            r0.append(r1)     // Catch:{ all -> 0x01e8 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x01e8 }
            android.taobao.windvane.util.TaoLog.e(r12, r0)     // Catch:{ all -> 0x01e8 }
            java.lang.String r11 = r11.getMessage()     // Catch:{ all -> 0x01e8 }
            if (r2 == 0) goto L_0x0257
            r2.close()     // Catch:{ IOException -> 0x0210 }
            goto L_0x022b
        L_0x0210:
            r12 = move-exception
            java.lang.String r0 = "FileManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "zipfile failed to close : "
            r1.append(r2)
            java.lang.String r12 = r12.getMessage()
            r1.append(r12)
            java.lang.String r12 = r1.toString()
            android.taobao.windvane.util.TaoLog.e(r0, r12)
        L_0x022b:
            com.uc.crashsdk.export.CrashApi r12 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x0257 }
            java.lang.String r0 = "wv_zip_url"
            java.lang.String r1 = ""
            r12.addHeaderInfo(r0, r1)     // Catch:{ Throwable -> 0x0257 }
            com.uc.crashsdk.export.CrashApi r12 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x0257 }
            java.lang.String r0 = "device_identifier"
            java.lang.String r1 = ""
            r12.addHeaderInfo(r0, r1)     // Catch:{ Throwable -> 0x0257 }
            com.uc.crashsdk.export.CrashApi r12 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x0257 }
            java.lang.String r0 = "config_version"
            java.lang.String r1 = ""
            r12.addHeaderInfo(r0, r1)     // Catch:{ Throwable -> 0x0257 }
            com.uc.crashsdk.export.CrashApi r12 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x0257 }
            java.lang.String r0 = "zip_degrade_config"
            java.lang.String r1 = ""
            r12.addHeaderInfo(r0, r1)     // Catch:{ Throwable -> 0x0257 }
        L_0x0257:
            return r11
        L_0x0258:
            if (r3 == 0) goto L_0x02a5
            r3.close()     // Catch:{ IOException -> 0x025e }
            goto L_0x0279
        L_0x025e:
            r12 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "zipfile failed to close : "
            r0.append(r1)
            java.lang.String r12 = r12.getMessage()
            r0.append(r12)
            java.lang.String r12 = r0.toString()
            java.lang.String r0 = "FileManager"
            android.taobao.windvane.util.TaoLog.e(r0, r12)
        L_0x0279:
            com.uc.crashsdk.export.CrashApi r12 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x02a5 }
            java.lang.String r0 = "wv_zip_url"
            java.lang.String r1 = ""
            r12.addHeaderInfo(r0, r1)     // Catch:{ Throwable -> 0x02a5 }
            com.uc.crashsdk.export.CrashApi r12 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x02a5 }
            java.lang.String r0 = "device_identifier"
            java.lang.String r1 = ""
            r12.addHeaderInfo(r0, r1)     // Catch:{ Throwable -> 0x02a5 }
            com.uc.crashsdk.export.CrashApi r12 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x02a5 }
            java.lang.String r0 = "config_version"
            java.lang.String r1 = ""
            r12.addHeaderInfo(r0, r1)     // Catch:{ Throwable -> 0x02a5 }
            com.uc.crashsdk.export.CrashApi r12 = com.uc.crashsdk.export.CrashApi.getInstance()     // Catch:{ Throwable -> 0x02a5 }
            java.lang.String r0 = "zip_degrade_config"
            java.lang.String r1 = ""
            r12.addHeaderInfo(r0, r1)     // Catch:{ Throwable -> 0x02a5 }
        L_0x02a5:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileManager.unZipByFilePath(java.lang.String, java.lang.String):java.lang.String");
    }
}
