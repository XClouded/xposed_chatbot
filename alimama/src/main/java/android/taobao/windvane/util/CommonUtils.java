package android.taobao.windvane.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileFilter;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import mtopsdk.common.util.SymbolExpUtil;

public class CommonUtils {
    private static final String TAG = "CommonUtils";
    private static String currentProcessName = "";
    private static Boolean isMainProcess;

    public static int compareVer(String str, String str2) {
        if (str == null) {
            str = "0";
        }
        if (str2 == null) {
            str2 = "0";
        }
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int length = split.length;
        int length2 = split2.length;
        int i = length > length2 ? length : length2;
        for (int i2 = 0; i2 < i; i2++) {
            if (i2 >= length || i2 >= length2) {
                int i3 = length > length2 ? toInt(split[i2]) : toInt(split2[i2]) * -1;
                if (i3 != 0) {
                    return i3;
                }
            } else {
                int i4 = toInt(split[i2]);
                int i5 = toInt(split2[i2]);
                if (i4 != i5) {
                    return i4 - i5;
                }
            }
        }
        return 0;
    }

    public static int toInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    public static String parseCharset(String str) {
        int indexOf;
        if (TextUtils.isEmpty(str) || (indexOf = str.indexOf(WVConstants.CHARSET)) == -1 || str.indexOf(SymbolExpUtil.SYMBOL_EQUAL, indexOf) == -1) {
            return "";
        }
        String substring = str.substring(str.indexOf(SymbolExpUtil.SYMBOL_EQUAL, indexOf) + 1);
        int indexOf2 = substring.indexOf(";");
        if (indexOf2 != -1) {
            substring = substring.substring(0, indexOf2).trim();
        }
        return substring.trim();
    }

    public static String parseMimeType(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int indexOf = str.indexOf(";");
        if (indexOf == -1) {
            return str.trim();
        }
        return str.substring(0, indexOf).trim();
    }

    public static long parseMaxAge(String str) {
        if (TextUtils.isEmpty(str) || str.indexOf("max-age=") == -1) {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        String substring = str.substring(8);
        int i = 0;
        while (i < substring.length() && Character.isDigit(substring.charAt(i))) {
            sb.append(substring.charAt(i));
            i++;
        }
        try {
            return Long.parseLong(sb.toString()) * 1000;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long parseDate(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.parse(str.trim()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isImage(String str) {
        return !TextUtils.isEmpty(str) && str.toLowerCase().startsWith("image");
    }

    public static boolean isHtml(String str) {
        return !TextUtils.isEmpty(str) && str.equalsIgnoreCase("text/html");
    }

    public static String formatDate(long j) {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH).format(new Date(j));
    }

    public static boolean isAppInstalled(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null || packageManager.getPackageInfo(str, 0) == null) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isLowendPhone() {
        int numCores = getNumCores();
        int totalRAM = getTotalRAM();
        TaoLog.d("HomePageNetwork", "processorCore = " + numCores + " ram = " + totalRAM + " MB");
        return numCores == 1 && totalRAM < 800;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.RandomAccessFile] */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0023 A[SYNTHETIC, Splitter:B:17:0x0023] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0055 A[SYNTHETIC, Splitter:B:31:0x0055] */
    /* JADX WARNING: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getTotalRAM() {
        /*
            r0 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x001c, all -> 0x001a }
            java.lang.String r2 = "/proc/meminfo"
            java.lang.String r3 = "r"
            r1.<init>(r2, r3)     // Catch:{ IOException -> 0x001c, all -> 0x001a }
            java.lang.String r2 = r1.readLine()     // Catch:{ IOException -> 0x0018 }
            r1.close()     // Catch:{ IOException -> 0x0012 }
            goto L_0x0016
        L_0x0012:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0016:
            r0 = r2
            goto L_0x002b
        L_0x0018:
            r2 = move-exception
            goto L_0x001e
        L_0x001a:
            r1 = move-exception
            goto L_0x0053
        L_0x001c:
            r2 = move-exception
            r1 = r0
        L_0x001e:
            r2.printStackTrace()     // Catch:{ all -> 0x004f }
            if (r1 == 0) goto L_0x002b
            r1.close()     // Catch:{ IOException -> 0x0027 }
            goto L_0x002b
        L_0x0027:
            r1 = move-exception
            r1.printStackTrace()
        L_0x002b:
            if (r0 == 0) goto L_0x004c
            java.lang.String r1 = "kB"
            java.lang.String r2 = ""
            java.lang.String r0 = r0.replace(r1, r2)
            java.lang.String r1 = "MemTotal:"
            java.lang.String r2 = ""
            java.lang.String r0 = r0.replace(r1, r2)
            java.lang.String r0 = r0.trim()     // Catch:{ NumberFormatException -> 0x0048 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x0048 }
            int r0 = r0 / 1000
            return r0
        L_0x0048:
            r0 = move-exception
            r0.printStackTrace()
        L_0x004c:
            r0 = 1024(0x400, float:1.435E-42)
            return r0
        L_0x004f:
            r0 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x0053:
            if (r0 == 0) goto L_0x005d
            r0.close()     // Catch:{ IOException -> 0x0059 }
            goto L_0x005d
        L_0x0059:
            r0 = move-exception
            r0.printStackTrace()
        L_0x005d:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.util.CommonUtils.getTotalRAM():int");
    }

    public static int getNumCores() {
        try {
            File[] listFiles = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return Pattern.matches("cpu[0-9]", file.getName());
                }
            });
            TaoLog.d("HomePageNetwork", "CPU Count: " + listFiles.length);
            return listFiles.length;
        } catch (Exception e) {
            TaoLog.d("HomePageNetwork", "CPU Count: Failed.");
            e.printStackTrace();
            return 1;
        }
    }

    public static boolean hasSDCardMounted() {
        String externalStorageState = Environment.getExternalStorageState();
        return externalStorageState != null && externalStorageState.equals("mounted");
    }

    public static File getExternalCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= 8) {
            try {
                File externalCacheDir = context.getExternalCacheDir();
                if (externalCacheDir != null) {
                    if (!externalCacheDir.exists()) {
                        externalCacheDir.mkdirs();
                    }
                    return externalCacheDir;
                }
            } catch (Exception unused) {
            }
        }
        File file = new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static boolean canWriteFile(String str, String str2) {
        try {
            File file = new File(str2);
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(file.length());
            randomAccessFile.write(str.getBytes());
            randomAccessFile.close();
            if (!file.exists()) {
                return false;
            }
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            File file2 = new File(str2);
            if (file2.exists()) {
                file2.delete();
            }
            return false;
        }
    }

    public static Bitmap base64ToBitmap(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            byte[] decode = Base64.decode(str.replace(' ', '+'), 0);
            return BitmapFactory.decodeByteArray(decode, 0, decode.length);
        } catch (Exception unused) {
            return null;
        }
    }

    public static boolean isPicture(String str) {
        if (str == null || -1 == str.lastIndexOf(".")) {
            return false;
        }
        String substring = str.substring(str.lastIndexOf(".") + 1, str.length());
        String[] strArr = {"png", "jpeg", "jpg", "webp"};
        for (String equals : strArr) {
            if (equals.equals(substring)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMainProcess(Context context) {
        if (isMainProcess == null) {
            isMainProcess = Boolean.valueOf(context != null && TextUtils.equals(getProcessName(context), context.getPackageName()));
        }
        return isMainProcess.booleanValue();
    }

    public static String getProcessName(Context context) {
        try {
            if (currentProcessName != null && currentProcessName.length() > 0) {
                return currentProcessName;
            }
            if (context == null) {
                return null;
            }
            int myPid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (next.pid == myPid) {
                    currentProcessName = next.processName;
                }
            }
            return currentProcessName;
        } catch (Exception e) {
            TaoLog.e("getProcessName error", e.toString());
        }
    }
}
