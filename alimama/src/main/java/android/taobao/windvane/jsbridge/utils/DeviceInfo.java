package android.taobao.windvane.jsbridge.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.el.parse.Operators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class DeviceInfo {
    private static final FileFilter CPU_FILTER = new FileFilter() {
        public boolean accept(File file) {
            String name = file.getName();
            if (!name.startsWith(Config.TYPE_CPU)) {
                return false;
            }
            for (int i = 3; i < name.length(); i++) {
                if (name.charAt(i) < '0' || name.charAt(i) > '9') {
                    return false;
                }
            }
            return true;
        }
    };
    public static final int DEVICEINFO_UNKNOWN = -1;

    public static int getNumberOfCPUCores() {
        if (Build.VERSION.SDK_INT <= 10) {
            return 1;
        }
        try {
            return new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (NullPointerException | SecurityException unused) {
            return -1;
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0062 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getCPUMaxFreqKHz() {
        /*
            r0 = 0
            r1 = -1
            r2 = 0
            r3 = -1
        L_0x0004:
            int r4 = getNumberOfCPUCores()     // Catch:{ IOException -> 0x008e }
            if (r2 >= r4) goto L_0x006e
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x008e }
            r4.<init>()     // Catch:{ IOException -> 0x008e }
            java.lang.String r5 = "/sys/devices/system/cpu/cpu"
            r4.append(r5)     // Catch:{ IOException -> 0x008e }
            r4.append(r2)     // Catch:{ IOException -> 0x008e }
            java.lang.String r5 = "/cpufreq/cpuinfo_max_freq"
            r4.append(r5)     // Catch:{ IOException -> 0x008e }
            java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x008e }
            java.io.File r5 = new java.io.File     // Catch:{ IOException -> 0x008e }
            r5.<init>(r4)     // Catch:{ IOException -> 0x008e }
            boolean r4 = r5.exists()     // Catch:{ IOException -> 0x008e }
            if (r4 == 0) goto L_0x006b
            r4 = 128(0x80, float:1.794E-43)
            byte[] r4 = new byte[r4]     // Catch:{ IOException -> 0x008e }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ IOException -> 0x008e }
            r6.<init>(r5)     // Catch:{ IOException -> 0x008e }
            r6.read(r4)     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            r5 = 0
        L_0x0038:
            byte r7 = r4[r5]     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            r8 = 48
            if (r7 < r8) goto L_0x004a
            byte r7 = r4[r5]     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            r8 = 57
            if (r7 > r8) goto L_0x004a
            int r7 = r4.length     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            if (r5 >= r7) goto L_0x004a
            int r5 = r5 + 1
            goto L_0x0038
        L_0x004a:
            java.lang.String r7 = new java.lang.String     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            r7.<init>(r4, r0, r5)     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            int r4 = java.lang.Integer.parseInt(r7)     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            int r5 = r4.intValue()     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            if (r5 <= r3) goto L_0x0062
            int r4 = r4.intValue()     // Catch:{ NumberFormatException -> 0x0062, all -> 0x0066 }
            r3 = r4
        L_0x0062:
            r6.close()     // Catch:{ IOException -> 0x008e }
            goto L_0x006b
        L_0x0066:
            r0 = move-exception
            r6.close()     // Catch:{ IOException -> 0x008e }
            throw r0     // Catch:{ IOException -> 0x008e }
        L_0x006b:
            int r2 = r2 + 1
            goto L_0x0004
        L_0x006e:
            if (r3 != r1) goto L_0x008d
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ IOException -> 0x008e }
            java.lang.String r2 = "/proc/cpuinfo"
            r0.<init>(r2)     // Catch:{ IOException -> 0x008e }
            java.lang.String r2 = "cpu MHz"
            int r2 = parseFileForValue(r2, r0)     // Catch:{ all -> 0x0088 }
            int r2 = r2 * 1000
            if (r2 <= r3) goto L_0x0082
            goto L_0x0083
        L_0x0082:
            r2 = r3
        L_0x0083:
            r0.close()     // Catch:{ IOException -> 0x008e }
            r1 = r2
            goto L_0x008e
        L_0x0088:
            r2 = move-exception
            r0.close()     // Catch:{ IOException -> 0x008e }
            throw r2     // Catch:{ IOException -> 0x008e }
        L_0x008d:
            r1 = r3
        L_0x008e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.utils.DeviceInfo.getCPUMaxFreqKHz():int");
    }

    @TargetApi(16)
    public static long getTotalMemory(Context context) {
        FileInputStream fileInputStream;
        if (Build.VERSION.SDK_INT >= 16) {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
            return memoryInfo.totalMem;
        }
        long j = -1;
        try {
            fileInputStream = new FileInputStream("/proc/meminfo");
            j = ((long) parseFileForValue("MemTotal", fileInputStream)) * 1024;
            fileInputStream.close();
        } catch (IOException unused) {
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
        return j;
    }

    private static int parseFileForValue(String str, FileInputStream fileInputStream) {
        byte[] bArr = new byte[1024];
        try {
            int read = fileInputStream.read(bArr);
            int i = 0;
            while (i < read) {
                if (bArr[i] == 10 || i == 0) {
                    if (bArr[i] == 10) {
                        i++;
                    }
                    int i2 = i;
                    while (true) {
                        if (i2 >= read) {
                            continue;
                            break;
                        }
                        int i3 = i2 - i;
                        if (bArr[i2] != str.charAt(i3)) {
                            break;
                        } else if (i3 == str.length() - 1) {
                            return extractValue(bArr, i2);
                        } else {
                            i2++;
                        }
                    }
                }
                i++;
            }
            return -1;
        } catch (IOException | NumberFormatException unused) {
            return -1;
        }
    }

    private static int extractValue(byte[] bArr, int i) {
        while (i < bArr.length && bArr[i] != 10) {
            if (bArr[i] < 48 || bArr[i] > 57) {
                i++;
            } else {
                int i2 = i + 1;
                while (i2 < bArr.length && bArr[i2] >= 48 && bArr[i2] <= 57) {
                    i2++;
                }
                return Integer.parseInt(new String(bArr, 0, i, i2 - i));
            }
        }
        return -1;
    }

    public static float getProcessCpuRate() {
        ArrayList<Long> cpuTime = getCpuTime();
        if (cpuTime == null || cpuTime.size() < 2) {
            return 0.0f;
        }
        float longValue = (float) cpuTime.get(0).longValue();
        float longValue2 = (float) cpuTime.get(1).longValue();
        try {
            Thread.sleep(360);
        } catch (Exception unused) {
        }
        ArrayList<Long> cpuTime2 = getCpuTime();
        if (cpuTime2 == null || cpuTime2.size() < 2) {
            return 0.0f;
        }
        float longValue3 = (float) cpuTime2.get(0).longValue();
        return ((longValue3 - ((float) cpuTime2.get(1).longValue())) - (longValue - longValue2)) / (longValue3 - longValue);
    }

    private static ArrayList<Long> getCpuTime() {
        ArrayList<Long> arrayList = new ArrayList<>();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("/proc/stat", UploadQueueMgr.MSGTYPE_REALTIME);
            while (true) {
                String readLine = randomAccessFile.readLine();
                if (readLine == null || !readLine.startsWith(Config.TYPE_CPU)) {
                    randomAccessFile.close();
                } else {
                    String[] split = readLine.split("\\s+");
                    long parseLong = Long.parseLong(split[4]);
                    arrayList.add(Long.valueOf(Long.parseLong(split[1]) + Long.parseLong(split[2]) + Long.parseLong(split[3]) + Long.parseLong(split[4]) + Long.parseLong(split[6]) + Long.parseLong(split[5]) + Long.parseLong(split[7])));
                    arrayList.add(Long.valueOf(parseLong));
                }
            }
            randomAccessFile.close();
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public long getTotalMemory() {
        String str = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return Long.parseLong(str.split(Operators.SPACE_STR)[0].trim());
                } else if (readLine.contains("MemTotal")) {
                    str = readLine.split(":")[1].trim();
                }
            }
        } catch (IOException unused) {
            return 0;
        }
    }

    public static long getFreeMemorySize(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }
}
