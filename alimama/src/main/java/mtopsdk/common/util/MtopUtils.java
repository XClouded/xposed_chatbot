package mtopsdk.common.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import com.alimama.unionwl.utils.CommonUtils;
import com.taobao.tao.image.Logger;
import java.io.IOException;
import java.lang.Character;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import mtopsdk.common.util.TBSdkLog;
import org.json.JSONObject;

public final class MtopUtils {
    private static final char[] LOWER_CASE_ENCRYPT_CHARS = {'e', 't', 'a', 'o', 'i', 'n', 's', 'r', 'h', 'l', 'd', 'c', 'u', 'm', 'f', 'p', 'g', 'w', 'y', 'b', 'v', 'k', 'x', 'j', 'q', 'z'};
    public static final int MTOP_BIZID = 4099;
    private static final char[] NUMBER_ENCRYPT_CHARS = {'8', '6', '1', '5', '9', '2', '3', '0', '4', '7'};
    private static final String TAG = "mtopsdk.MtopUtils";
    private static final char[] UPPER_CASE_ENCRYPT_CHARS = {Logger.LEVEL_E, 'T', 'A', 'O', Logger.LEVEL_I, 'N', 'S', 'R', 'H', Logger.LEVEL_L, Logger.LEVEL_D, 'C', 'U', 'M', 'F', 'P', 'G', Logger.LEVEL_W, 'Y', 'B', Logger.LEVEL_V, 'K', 'X', 'J', 'Q', 'Z'};
    public static final List<String> apiWhiteList = Collections.singletonList("mtop.common.gettimestamp$*");
    private static AtomicInteger counter = new AtomicInteger();
    private static volatile Context mContext = null;
    private static volatile String mProcessName = null;
    private static final int mask = Integer.MAX_VALUE;

    public static long convertTimeFormatGMT2Long(String str) {
        Date date;
        if (StringUtils.isBlank(str)) {
            return -1;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            date = simpleDateFormat.parse(str);
        } catch (Exception unused) {
            TBSdkLog.e(TAG, "[convertTimeFormatGMT2Long]parse gmt timeformat error");
            date = null;
        }
        if (date != null) {
            return date.getTime() / 1000;
        }
        return -1;
    }

    public static int createIntSeqNo() {
        return counter.incrementAndGet() & Integer.MAX_VALUE;
    }

    @TargetApi(3)
    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    @TargetApi(4)
    public static boolean isApkDebug(Context context) {
        if (context == null) {
            context = getContext();
        }
        if (context == null) {
            TBSdkLog.e(TAG, "[isApkDebug] context is null!");
            return false;
        }
        try {
            if ((context.getApplicationInfo().flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static String urlDecode(String str, String str2) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        try {
            return URLDecoder.decode(str, str2);
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[urlDecode] URLDecoder decode error. input=" + str + ", charset= " + str2, (Throwable) e);
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0030, code lost:
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002c A[ExcHandler: all (th java.lang.Throwable), Splitter:B:6:0x0012] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x004d A[SYNTHETIC, Splitter:B:26:0x004d] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0053 A[SYNTHETIC, Splitter:B:31:0x0053] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.Serializable readObject(java.io.File r7, java.lang.String r8) {
        /*
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0035 }
            r1.<init>(r7, r8)     // Catch:{ Throwable -> 0x0035 }
            boolean r2 = r1.exists()     // Catch:{ Throwable -> 0x0035 }
            if (r2 != 0) goto L_0x000d
            return r0
        L_0x000d:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0035 }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x0035 }
            java.io.ObjectInputStream r1 = new java.io.ObjectInputStream     // Catch:{ Throwable -> 0x002f, all -> 0x002c }
            java.io.BufferedInputStream r3 = new java.io.BufferedInputStream     // Catch:{ Throwable -> 0x002f, all -> 0x002c }
            r3.<init>(r2)     // Catch:{ Throwable -> 0x002f, all -> 0x002c }
            r1.<init>(r3)     // Catch:{ Throwable -> 0x002f, all -> 0x002c }
            java.lang.Object r3 = r1.readObject()     // Catch:{ Throwable -> 0x002f, all -> 0x002c }
            java.io.Serializable r3 = (java.io.Serializable) r3     // Catch:{ Throwable -> 0x002f, all -> 0x002c }
            r1.close()     // Catch:{ Throwable -> 0x0029, all -> 0x002c }
            r2.close()     // Catch:{ IOException -> 0x0050 }
            goto L_0x0050
        L_0x0029:
            r0 = move-exception
            r1 = r0
            goto L_0x0031
        L_0x002c:
            r7 = move-exception
            r0 = r2
            goto L_0x0051
        L_0x002f:
            r1 = move-exception
            r3 = r0
        L_0x0031:
            r0 = r2
            goto L_0x0037
        L_0x0033:
            r7 = move-exception
            goto L_0x0051
        L_0x0035:
            r1 = move-exception
            r3 = r0
        L_0x0037:
            java.lang.String r2 = "mtopsdk.MtopUtils"
            java.lang.String r4 = "readObject error.fileDir={%s},fileName={%s}"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0033 }
            r6 = 0
            r5[r6] = r7     // Catch:{ all -> 0x0033 }
            r7 = 1
            r5[r7] = r8     // Catch:{ all -> 0x0033 }
            java.lang.String r7 = java.lang.String.format(r4, r5)     // Catch:{ all -> 0x0033 }
            mtopsdk.common.util.TBSdkLog.w((java.lang.String) r2, (java.lang.String) r7, (java.lang.Throwable) r1)     // Catch:{ all -> 0x0033 }
            if (r0 == 0) goto L_0x0050
            r0.close()     // Catch:{ IOException -> 0x0050 }
        L_0x0050:
            return r3
        L_0x0051:
            if (r0 == 0) goto L_0x0056
            r0.close()     // Catch:{ IOException -> 0x0056 }
        L_0x0056:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.common.util.MtopUtils.readObject(java.io.File, java.lang.String):java.io.Serializable");
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0070 A[SYNTHETIC, Splitter:B:25:0x0070] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0077  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0083 A[SYNTHETIC, Splitter:B:33:0x0083] */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean writeObject(java.io.Serializable r9, java.io.File r10, java.lang.String r11) {
        /*
            r0 = 1
            r1 = 0
            r2 = 0
            boolean r3 = r10.exists()     // Catch:{ Throwable -> 0x0057 }
            if (r3 != 0) goto L_0x000c
            r10.mkdirs()     // Catch:{ Throwable -> 0x0057 }
        L_0x000c:
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0057 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0057 }
            r4.<init>()     // Catch:{ Throwable -> 0x0057 }
            r4.append(r11)     // Catch:{ Throwable -> 0x0057 }
            java.util.Random r5 = new java.util.Random     // Catch:{ Throwable -> 0x0057 }
            r5.<init>()     // Catch:{ Throwable -> 0x0057 }
            r6 = 10
            int r5 = r5.nextInt(r6)     // Catch:{ Throwable -> 0x0057 }
            r4.append(r5)     // Catch:{ Throwable -> 0x0057 }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x0057 }
            r3.<init>(r10, r4)     // Catch:{ Throwable -> 0x0057 }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0050 }
            r4.<init>(r3)     // Catch:{ Throwable -> 0x0050 }
            java.io.ObjectOutputStream r2 = new java.io.ObjectOutputStream     // Catch:{ Throwable -> 0x004a, all -> 0x0047 }
            java.io.BufferedOutputStream r5 = new java.io.BufferedOutputStream     // Catch:{ Throwable -> 0x004a, all -> 0x0047 }
            r5.<init>(r4)     // Catch:{ Throwable -> 0x004a, all -> 0x0047 }
            r2.<init>(r5)     // Catch:{ Throwable -> 0x004a, all -> 0x0047 }
            r2.writeObject(r9)     // Catch:{ Throwable -> 0x004a, all -> 0x0047 }
            r2.flush()     // Catch:{ Throwable -> 0x004a, all -> 0x0047 }
            r2.close()     // Catch:{ Throwable -> 0x004a, all -> 0x0047 }
            r4.close()     // Catch:{ IOException -> 0x0075 }
            goto L_0x0075
        L_0x0047:
            r9 = move-exception
            r2 = r4
            goto L_0x0081
        L_0x004a:
            r2 = move-exception
            r8 = r3
            r3 = r2
            r2 = r4
            r4 = r8
            goto L_0x0059
        L_0x0050:
            r4 = move-exception
            r8 = r4
            r4 = r3
            r3 = r8
            goto L_0x0059
        L_0x0055:
            r9 = move-exception
            goto L_0x0081
        L_0x0057:
            r3 = move-exception
            r4 = r2
        L_0x0059:
            java.lang.String r5 = "mtopsdk.MtopUtils"
            java.lang.String r6 = "writeObject error.fileDir={%s},fileName={%s},object={%s}"
            r7 = 3
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0055 }
            r7[r1] = r10     // Catch:{ all -> 0x0055 }
            r7[r0] = r11     // Catch:{ all -> 0x0055 }
            r0 = 2
            r7[r0] = r9     // Catch:{ all -> 0x0055 }
            java.lang.String r9 = java.lang.String.format(r6, r7)     // Catch:{ all -> 0x0055 }
            mtopsdk.common.util.TBSdkLog.w((java.lang.String) r5, (java.lang.String) r9, (java.lang.Throwable) r3)     // Catch:{ all -> 0x0055 }
            if (r2 == 0) goto L_0x0073
            r2.close()     // Catch:{ IOException -> 0x0073 }
        L_0x0073:
            r3 = r4
            r0 = 0
        L_0x0075:
            if (r0 == 0) goto L_0x0080
            java.io.File r9 = new java.io.File
            r9.<init>(r10, r11)
            boolean r0 = r3.renameTo(r9)
        L_0x0080:
            return r0
        L_0x0081:
            if (r2 == 0) goto L_0x0086
            r2.close()     // Catch:{ IOException -> 0x0086 }
        L_0x0086:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.common.util.MtopUtils.writeObject(java.io.Serializable, java.io.File, java.lang.String):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:6|7|(2:8|(1:10)(1:61))|11|12|13|14|15|16|63) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0028 */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x004f A[SYNTHETIC, Splitter:B:33:0x004f] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x006d A[SYNTHETIC, Splitter:B:46:0x006d] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0079 A[SYNTHETIC, Splitter:B:53:0x0079] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0080 A[SYNTHETIC, Splitter:B:57:0x0080] */
    /* JADX WARNING: Removed duplicated region for block: B:64:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:65:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readFile(java.lang.String r8) {
        /*
            r0 = 1
            r1 = 0
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch:{ FileNotFoundException -> 0x005a, Throwable -> 0x003b, all -> 0x0037 }
            r3.<init>(r8)     // Catch:{ FileNotFoundException -> 0x005a, Throwable -> 0x003b, all -> 0x0037 }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x005a, Throwable -> 0x003b, all -> 0x0037 }
            r4.<init>(r3)     // Catch:{ FileNotFoundException -> 0x005a, Throwable -> 0x003b, all -> 0x0037 }
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch:{ FileNotFoundException -> 0x0035, Throwable -> 0x0032, all -> 0x002f }
            r3.<init>()     // Catch:{ FileNotFoundException -> 0x0035, Throwable -> 0x0032, all -> 0x002f }
            r5 = 4096(0x1000, float:5.74E-42)
            byte[] r5 = new byte[r5]     // Catch:{ FileNotFoundException -> 0x005c, Throwable -> 0x002d }
        L_0x0016:
            int r6 = r4.read(r5)     // Catch:{ FileNotFoundException -> 0x005c, Throwable -> 0x002d }
            r7 = -1
            if (r6 == r7) goto L_0x0021
            r3.write(r5, r1, r6)     // Catch:{ FileNotFoundException -> 0x005c, Throwable -> 0x002d }
            goto L_0x0016
        L_0x0021:
            byte[] r5 = r3.toByteArray()     // Catch:{ FileNotFoundException -> 0x005c, Throwable -> 0x002d }
            r3.close()     // Catch:{ IOException -> 0x0028 }
        L_0x0028:
            r4.close()     // Catch:{ IOException -> 0x002b }
        L_0x002b:
            r2 = r5
            goto L_0x0075
        L_0x002d:
            r5 = move-exception
            goto L_0x003e
        L_0x002f:
            r8 = move-exception
            r3 = r2
            goto L_0x0077
        L_0x0032:
            r5 = move-exception
            r3 = r2
            goto L_0x003e
        L_0x0035:
            r3 = r2
            goto L_0x005c
        L_0x0037:
            r8 = move-exception
            r3 = r2
            r4 = r3
            goto L_0x0077
        L_0x003b:
            r5 = move-exception
            r3 = r2
            r4 = r3
        L_0x003e:
            java.lang.String r6 = "mtopsdk.MtopUtils"
            java.lang.String r7 = "readFile error.filePath={%s}"
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0076 }
            r0[r1] = r8     // Catch:{ all -> 0x0076 }
            java.lang.String r8 = java.lang.String.format(r7, r0)     // Catch:{ all -> 0x0076 }
            mtopsdk.common.util.TBSdkLog.w((java.lang.String) r6, (java.lang.String) r8, (java.lang.Throwable) r5)     // Catch:{ all -> 0x0076 }
            if (r3 == 0) goto L_0x0054
            r3.close()     // Catch:{ IOException -> 0x0053 }
            goto L_0x0054
        L_0x0053:
        L_0x0054:
            if (r4 == 0) goto L_0x0075
        L_0x0056:
            r4.close()     // Catch:{ IOException -> 0x0075 }
            goto L_0x0075
        L_0x005a:
            r3 = r2
            r4 = r3
        L_0x005c:
            java.lang.String r5 = "mtopsdk.MtopUtils"
            java.lang.String r6 = "readFile error.filePath={%s} is not found."
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0076 }
            r0[r1] = r8     // Catch:{ all -> 0x0076 }
            java.lang.String r8 = java.lang.String.format(r6, r0)     // Catch:{ all -> 0x0076 }
            mtopsdk.common.util.TBSdkLog.w(r5, r8)     // Catch:{ all -> 0x0076 }
            if (r3 == 0) goto L_0x0072
            r3.close()     // Catch:{ IOException -> 0x0071 }
            goto L_0x0072
        L_0x0071:
        L_0x0072:
            if (r4 == 0) goto L_0x0075
            goto L_0x0056
        L_0x0075:
            return r2
        L_0x0076:
            r8 = move-exception
        L_0x0077:
            if (r3 == 0) goto L_0x007e
            r3.close()     // Catch:{ IOException -> 0x007d }
            goto L_0x007e
        L_0x007d:
        L_0x007e:
            if (r4 == 0) goto L_0x0083
            r4.close()     // Catch:{ IOException -> 0x0083 }
        L_0x0083:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.common.util.MtopUtils.readFile(java.lang.String):byte[]");
    }

    public static String convertUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.startsWith("//")) {
            str = CommonUtils.HTTP_PRE + str;
        }
        try {
            int indexOf = str.indexOf("?");
            if (indexOf != -1) {
                str = str.substring(0, indexOf);
            } else {
                int indexOf2 = str.indexOf("#");
                if (indexOf2 != -1) {
                    str = str.substring(0, indexOf2);
                }
            }
            return str;
        } catch (Exception unused) {
            return "";
        }
    }

    public static Context getContext() {
        if (mContext == null) {
            synchronized (MtopUtils.class) {
                if (mContext == null) {
                    try {
                        Class<?> cls = Class.forName("android.app.ActivityThread");
                        Object invoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(cls, new Object[0]);
                        mContext = (Context) invoke.getClass().getMethod("getApplication", new Class[0]).invoke(invoke, new Object[0]);
                    } catch (Exception unused) {
                        TBSdkLog.e(TAG, "getContext through reflection error.");
                    }
                }
            }
        }
        return mContext;
    }

    @TargetApi(3)
    public static String getCurrentProcessName(Context context) {
        if (context == null) {
            return mProcessName;
        }
        if (mProcessName == null) {
            synchronized (MtopUtils.class) {
                if (mProcessName == null) {
                    try {
                        int myPid = Process.myPid();
                        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
                        if (runningAppProcesses != null && runningAppProcesses.size() > 0) {
                            Iterator<ActivityManager.RunningAppProcessInfo> it = runningAppProcesses.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                ActivityManager.RunningAppProcessInfo next = it.next();
                                if (next.pid == myPid) {
                                    mProcessName = next.processName;
                                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                                        TBSdkLog.i(TAG, "get current processName succeed,processName=" + mProcessName);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        TBSdkLog.e(TAG, "get current processName failed.", (Throwable) e);
                    }
                }
            }
        }
        return mProcessName;
    }

    public static boolean isAppOpenMock(Context context) {
        if (context == null) {
            context = getContext();
        }
        if (context == null) {
            TBSdkLog.e(TAG, "[isAppOpenMock] context is null!");
            return false;
        }
        try {
            byte[] readFile = readFile(context.getFilesDir().getCanonicalPath() + "/mock/openMock.json");
            if (readFile != null) {
                try {
                    return new JSONObject(new String(readFile)).getBoolean("openMock");
                } catch (Exception e) {
                    TBSdkLog.e(TAG, "[isAppOpenMock]parse openMock flag error in isOpenMock.json .", (Throwable) e);
                }
            }
            return false;
        } catch (IOException e2) {
            TBSdkLog.e(TAG, "[isAppOpenMock] parse ExternalFilesDir/mock/openMock.json filePath error.", (Throwable) e2);
            return false;
        }
    }

    public static String caesarEncrypt(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt >= 'A' && charAt <= 'Z') {
                charAt = UPPER_CASE_ENCRYPT_CHARS[charAt - 'A'];
            } else if (charAt >= 'a' && charAt <= 'z') {
                charAt = LOWER_CASE_ENCRYPT_CHARS[charAt - 'a'];
            } else if (charAt >= '0' && charAt <= '9') {
                charAt = NUMBER_ENCRYPT_CHARS[charAt - '0'];
            }
            sb.append(charAt);
        }
        return sb.toString();
    }

    public static final boolean isContainChineseCharacter(String str) {
        if (str == null) {
            return false;
        }
        char[] charArray = str.toCharArray();
        int i = 0;
        while (i < charArray.length) {
            char c = charArray[i];
            try {
                Character.UnicodeBlock of = Character.UnicodeBlock.of(c);
                if (of == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || of == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || of == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || of == Character.UnicodeBlock.GENERAL_PUNCTUATION || of == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || of == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                    return true;
                }
                i++;
            } catch (Throwable unused) {
                if (c >= 19968 && c <= 40959) {
                    return true;
                }
            }
        }
        return false;
    }
}
