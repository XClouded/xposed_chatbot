package com.alibaba.motu.tbrest.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Process;
import android.os.StatFs;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AppUtils {

    public interface ReaderListener {
        boolean onReadLine(String str);
    }

    public static String getMyProcessNameByAppProcessInfo(Context context) {
        if (context == null) {
            return null;
        }
        int myPid = Process.myPid();
        try {
            for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (next.pid == myPid) {
                    return next.processName;
                }
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getGMT8Time(long j) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return simpleDateFormat.format(new Date(j));
        } catch (Exception e) {
            LogUtil.e("getGMT8Time", e);
            return "";
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LogUtil.e("close.", e);
            }
        }
    }

    public static String getMyProcessNameByCmdline() {
        try {
            return readLine("/proc/self/cmdline").trim();
        } catch (Exception e) {
            LogUtil.e("get my process name by cmd line failure .", e);
            return null;
        }
    }

    public static String getMyStatus() {
        return readFully(new File("/proc/self/status")).trim();
    }

    public static String getMeminfo() {
        return readFully(new File("/proc/meminfo")).trim();
    }

    public static String dumpThread(Thread thread) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(String.format("Thread Name: '%s'\n", new Object[]{thread.getName()}));
            sb.append(String.format("\"%s\" prio=%d tid=%d %s\n", new Object[]{thread.getName(), Integer.valueOf(thread.getPriority()), Long.valueOf(thread.getId()), thread.getState()}));
            StackTraceElement[] stackTrace = thread.getStackTrace();
            int length = stackTrace.length;
            for (int i = 0; i < length; i++) {
                sb.append(String.format("\tat %s\n", new Object[]{stackTrace[i].toString()}));
            }
        } catch (Exception e) {
            LogUtil.e("dumpThread", e);
        }
        return sb.toString();
    }

    public static String dumpMeminfo(Context context) {
        String str;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            Integer num = null;
            if (activityManager != null) {
                activityManager.getMemoryInfo(memoryInfo);
                num = Integer.valueOf((int) (memoryInfo.threshold >> 10));
            }
            StringBuilder sb = new StringBuilder();
            sb.append("JavaTotal:");
            sb.append(Runtime.getRuntime().totalMemory());
            sb.append(" JavaFree:");
            sb.append(Runtime.getRuntime().freeMemory());
            sb.append(" NativeHeap:");
            sb.append(Debug.getNativeHeapSize());
            sb.append(" NativeAllocated:");
            sb.append(Debug.getNativeHeapAllocatedSize());
            sb.append(" NativeFree:");
            sb.append(Debug.getNativeHeapFreeSize());
            sb.append(" threshold:");
            if (num != null) {
                str = num + " KB";
            } else {
                str = "not valid";
            }
            sb.append(str);
            return sb.toString();
        } catch (Exception e) {
            LogUtil.e("dumpMeminfo", e);
            return "";
        }
    }

    private static long[] getSizes(String str) {
        long j;
        long j2;
        long j3;
        long j4;
        long[] jArr = {-1, -1, -1};
        try {
            StatFs statFs = new StatFs(str);
            if (Build.VERSION.SDK_INT < 18) {
                j4 = (long) statFs.getBlockSize();
                j3 = (long) statFs.getBlockCount();
                j2 = (long) statFs.getFreeBlocks();
                j = (long) statFs.getAvailableBlocks();
            } else {
                j4 = statFs.getBlockSizeLong();
                j3 = statFs.getBlockCountLong();
                j2 = statFs.getFreeBlocksLong();
                j = statFs.getAvailableBlocksLong();
            }
            jArr[0] = j3 * j4;
            jArr[1] = j2 * j4;
            jArr[2] = j4 * j;
        } catch (Exception e) {
            LogUtil.e("getSizes", e);
        }
        return jArr;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0067 A[Catch:{ Exception -> 0x019a }] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00b5 A[Catch:{ Exception -> 0x019a }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0103 A[Catch:{ Exception -> 0x019a }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0151 A[Catch:{ Exception -> 0x019a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String dumpStorage(android.content.Context r10) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r1 = 0
            java.lang.String r2 = "mounted"
            java.lang.String r3 = android.os.Environment.getExternalStorageState()     // Catch:{ Exception -> 0x0011 }
            boolean r2 = r2.equals(r3)     // Catch:{ Exception -> 0x0011 }
            goto L_0x0018
        L_0x0011:
            r2 = move-exception
            java.lang.String r3 = "hasSDCard"
            com.alibaba.motu.tbrest.utils.LogUtil.w(r3, r2)
            r2 = 0
        L_0x0018:
            r3 = 1
            android.content.pm.ApplicationInfo r10 = r10.getApplicationInfo()     // Catch:{ Exception -> 0x0026 }
            int r10 = r10.flags     // Catch:{ Exception -> 0x0026 }
            r4 = 262144(0x40000, float:3.67342E-40)
            r10 = r10 & r4
            if (r10 == 0) goto L_0x002c
            r10 = 1
            goto L_0x002d
        L_0x0026:
            r10 = move-exception
            java.lang.String r4 = "installSDCard"
            com.alibaba.motu.tbrest.utils.LogUtil.w(r4, r10)
        L_0x002c:
            r10 = 0
        L_0x002d:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "hasSDCard: "
            r4.append(r5)
            r4.append(r2)
            java.lang.String r2 = "\n"
            r4.append(r2)
            java.lang.String r2 = r4.toString()
            r0.append(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "installSDCard: "
            r2.append(r4)
            r2.append(r10)
            java.lang.String r10 = "\n"
            r2.append(r10)
            java.lang.String r10 = r2.toString()
            r0.append(r10)
            java.io.File r10 = android.os.Environment.getRootDirectory()     // Catch:{ Exception -> 0x019a }
            r2 = 3
            r4 = 2
            if (r10 == 0) goto L_0x00af
            java.lang.String r5 = r10.getAbsolutePath()     // Catch:{ Exception -> 0x019a }
            long[] r5 = getSizes(r5)     // Catch:{ Exception -> 0x019a }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x019a }
            r6.<init>()     // Catch:{ Exception -> 0x019a }
            java.lang.String r7 = "RootDirectory: "
            r6.append(r7)     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ Exception -> 0x019a }
            r6.append(r10)     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = " "
            r6.append(r10)     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = r6.toString()     // Catch:{ Exception -> 0x019a }
            r0.append(r10)     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = "TotalSize: %s FreeSize: %s AvailableSize: %s \n"
            java.lang.Object[] r6 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x019a }
            r7 = r5[r1]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ Exception -> 0x019a }
            r6[r1] = r7     // Catch:{ Exception -> 0x019a }
            r7 = r5[r3]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ Exception -> 0x019a }
            r6[r3] = r7     // Catch:{ Exception -> 0x019a }
            r7 = r5[r4]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r5 = java.lang.Long.valueOf(r7)     // Catch:{ Exception -> 0x019a }
            r6[r4] = r5     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = java.lang.String.format(r10, r6)     // Catch:{ Exception -> 0x019a }
            r0.append(r10)     // Catch:{ Exception -> 0x019a }
        L_0x00af:
            java.io.File r10 = android.os.Environment.getDataDirectory()     // Catch:{ Exception -> 0x019a }
            if (r10 == 0) goto L_0x00fd
            java.lang.String r5 = r10.getAbsolutePath()     // Catch:{ Exception -> 0x019a }
            long[] r5 = getSizes(r5)     // Catch:{ Exception -> 0x019a }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x019a }
            r6.<init>()     // Catch:{ Exception -> 0x019a }
            java.lang.String r7 = "DataDirectory: "
            r6.append(r7)     // Catch:{ Exception -> 0x019a }
            java.lang.String r7 = r10.getAbsolutePath()     // Catch:{ Exception -> 0x019a }
            r6.append(r7)     // Catch:{ Exception -> 0x019a }
            java.lang.String r7 = " "
            r6.append(r7)     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x019a }
            r0.append(r6)     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = "TotalSize: %s FreeSize: %s AvailableSize: %s \n"
            java.lang.Object[] r7 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x019a }
            r8 = r5[r1]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r8 = java.lang.Long.valueOf(r8)     // Catch:{ Exception -> 0x019a }
            r7[r1] = r8     // Catch:{ Exception -> 0x019a }
            r8 = r5[r3]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r8 = java.lang.Long.valueOf(r8)     // Catch:{ Exception -> 0x019a }
            r7[r3] = r8     // Catch:{ Exception -> 0x019a }
            r8 = r5[r4]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r5 = java.lang.Long.valueOf(r8)     // Catch:{ Exception -> 0x019a }
            r7[r4] = r5     // Catch:{ Exception -> 0x019a }
            java.lang.String r5 = java.lang.String.format(r6, r7)     // Catch:{ Exception -> 0x019a }
            r0.append(r5)     // Catch:{ Exception -> 0x019a }
        L_0x00fd:
            java.io.File r5 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x019a }
            if (r10 == 0) goto L_0x014b
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x019a }
            r10.<init>()     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = "ExternalStorageDirectory: "
            r10.append(r6)     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = r5.getAbsolutePath()     // Catch:{ Exception -> 0x019a }
            r10.append(r6)     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = " "
            r10.append(r6)     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x019a }
            r0.append(r10)     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = r5.getAbsolutePath()     // Catch:{ Exception -> 0x019a }
            long[] r10 = getSizes(r10)     // Catch:{ Exception -> 0x019a }
            java.lang.String r5 = "TotalSize: %s FreeSize: %s AvailableSize: %s \n"
            java.lang.Object[] r6 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x019a }
            r7 = r10[r1]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ Exception -> 0x019a }
            r6[r1] = r7     // Catch:{ Exception -> 0x019a }
            r7 = r10[r3]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ Exception -> 0x019a }
            r6[r3] = r7     // Catch:{ Exception -> 0x019a }
            r7 = r10[r4]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r10 = java.lang.Long.valueOf(r7)     // Catch:{ Exception -> 0x019a }
            r6[r4] = r10     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = java.lang.String.format(r5, r6)     // Catch:{ Exception -> 0x019a }
            r0.append(r10)     // Catch:{ Exception -> 0x019a }
        L_0x014b:
            java.io.File r10 = android.os.Environment.getDownloadCacheDirectory()     // Catch:{ Exception -> 0x019a }
            if (r10 == 0) goto L_0x01a0
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x019a }
            r5.<init>()     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = "DownloadCacheDirectory: "
            r5.append(r6)     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = r10.getAbsolutePath()     // Catch:{ Exception -> 0x019a }
            r5.append(r6)     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = " "
            r5.append(r6)     // Catch:{ Exception -> 0x019a }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x019a }
            r0.append(r5)     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ Exception -> 0x019a }
            long[] r10 = getSizes(r10)     // Catch:{ Exception -> 0x019a }
            java.lang.String r5 = "TotalSize: %s FreeSize: %s AvailableSize: %s \n"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x019a }
            r6 = r10[r1]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Exception -> 0x019a }
            r2[r1] = r6     // Catch:{ Exception -> 0x019a }
            r6 = r10[r3]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r1 = java.lang.Long.valueOf(r6)     // Catch:{ Exception -> 0x019a }
            r2[r3] = r1     // Catch:{ Exception -> 0x019a }
            r6 = r10[r4]     // Catch:{ Exception -> 0x019a }
            java.lang.Long r10 = java.lang.Long.valueOf(r6)     // Catch:{ Exception -> 0x019a }
            r2[r4] = r10     // Catch:{ Exception -> 0x019a }
            java.lang.String r10 = java.lang.String.format(r5, r2)     // Catch:{ Exception -> 0x019a }
            r0.append(r10)     // Catch:{ Exception -> 0x019a }
            goto L_0x01a0
        L_0x019a:
            r10 = move-exception
            java.lang.String r1 = "getSizes"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r1, r10)
        L_0x01a0:
            java.lang.String r10 = r0.toString()
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.utils.AppUtils.dumpStorage(android.content.Context):java.lang.String");
    }

    public static Boolean isBackgroundRunning(Context context) {
        try {
            if (Integer.parseInt(readLine("/proc/self/oom_adj").trim()) == 0) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean writeFile(File file, String str) {
        return writeFile(file, str, false);
    }

    public static boolean writeFile(File file, String str, boolean z) {
        FileWriter fileWriter = null;
        try {
            FileWriter fileWriter2 = new FileWriter(file, z);
            try {
                fileWriter2.write(str);
                fileWriter2.flush();
                closeQuietly(fileWriter2);
                return true;
            } catch (IOException e) {
                e = e;
                fileWriter = fileWriter2;
                try {
                    LogUtil.e("writeFile", e);
                    closeQuietly(fileWriter);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(fileWriter);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileWriter = fileWriter2;
                closeQuietly(fileWriter);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            LogUtil.e("writeFile", e);
            closeQuietly(fileWriter);
            return false;
        }
    }

    public static String readLine(String str) {
        return readLine(new File(str));
    }

    public static String readLine(File file) {
        List<String> readLines = readLines(file, 1);
        return !readLines.isEmpty() ? readLines.get(0) : "";
    }

    public static List<String> readLines(File file, int i) {
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            int i2 = 0;
            while (true) {
                try {
                    String readLine = bufferedReader2.readLine();
                    if (readLine == null) {
                        break;
                    }
                    i2++;
                    arrayList.add(readLine);
                    if (i > 0 && i2 >= i) {
                        break;
                    }
                } catch (IOException e) {
                    e = e;
                    bufferedReader = bufferedReader2;
                    try {
                        LogUtil.e("readLine", e);
                        closeQuietly(bufferedReader);
                        return arrayList;
                    } catch (Throwable th) {
                        th = th;
                        closeQuietly(bufferedReader);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = bufferedReader2;
                    closeQuietly(bufferedReader);
                    throw th;
                }
            }
            closeQuietly(bufferedReader2);
        } catch (IOException e2) {
            e = e2;
            LogUtil.e("readLine", e);
            closeQuietly(bufferedReader);
            return arrayList;
        }
        return arrayList;
    }

    public static String readLineAndDel(File file) {
        try {
            String readLine = readLine(file);
            file.delete();
            return readLine;
        } catch (Exception e) {
            LogUtil.e("readLineAndDel", e);
            return null;
        }
    }

    public static void readLine(File file, ReaderListener readerListener) {
        String readLine;
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            do {
                try {
                    readLine = bufferedReader2.readLine();
                    if (readLine == null) {
                        closeQuietly(bufferedReader2);
                        return;
                    }
                } catch (IOException e) {
                    e = e;
                    bufferedReader = bufferedReader2;
                    try {
                        LogUtil.e("readLine", e);
                        closeQuietly(bufferedReader);
                    } catch (Throwable th) {
                        th = th;
                        closeQuietly(bufferedReader);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = bufferedReader2;
                    closeQuietly(bufferedReader);
                    throw th;
                }
            } while (!readerListener.onReadLine(readLine));
            closeQuietly(bufferedReader2);
        } catch (IOException e2) {
            e = e2;
            LogUtil.e("readLine", e);
            closeQuietly(bufferedReader);
        }
    }

    public static String readFully(File file) {
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;
        StringBuilder sb = new StringBuilder();
        InputStreamReader inputStreamReader2 = null;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                inputStreamReader = new InputStreamReader(fileInputStream);
            } catch (Exception e) {
                e = e;
                try {
                    LogUtil.e("readFully.", e);
                    closeQuietly(inputStreamReader2);
                    closeQuietly(fileInputStream);
                    return sb.toString();
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(inputStreamReader2);
                    closeQuietly(fileInputStream);
                    throw th;
                }
            }
            try {
                char[] cArr = new char[4096];
                while (true) {
                    int read = inputStreamReader.read(cArr);
                    if (-1 == read) {
                        break;
                    }
                    sb.append(cArr, 0, read);
                }
                closeQuietly(inputStreamReader);
            } catch (Exception e2) {
                Exception exc = e2;
                inputStreamReader2 = inputStreamReader;
                e = exc;
                LogUtil.e("readFully.", e);
                closeQuietly(inputStreamReader2);
                closeQuietly(fileInputStream);
                return sb.toString();
            } catch (Throwable th2) {
                th = th2;
                inputStreamReader2 = inputStreamReader;
                closeQuietly(inputStreamReader2);
                closeQuietly(fileInputStream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            fileInputStream = null;
            LogUtil.e("readFully.", e);
            closeQuietly(inputStreamReader2);
            closeQuietly(fileInputStream);
            return sb.toString();
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            closeQuietly(inputStreamReader2);
            closeQuietly(fileInputStream);
            throw th;
        }
        closeQuietly(fileInputStream);
        return sb.toString();
    }
}
