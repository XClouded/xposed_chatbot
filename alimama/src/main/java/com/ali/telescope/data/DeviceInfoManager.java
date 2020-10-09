package com.ali.telescope.data;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import com.ali.telescope.util.FileUtils;
import com.ali.telescope.util.IOUtils;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.weex.el.parse.Operators;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DeviceInfoManager {
    static final String TAG = "DeviceInfoManager";
    Integer apiLevel = Integer.valueOf(Build.VERSION.SDK_INT);
    String cpuArch = null;
    String cpuBrand = null;
    float[] cpuFreqArray = null;
    Float cpuMaxFreq = Float.valueOf(0.0f);
    Float cpuMinFreq = Float.valueOf(0.0f);
    String cpuModel = null;
    Integer cpuProcessCount = 0;
    Long deviceTotalMemory = 0L;
    Long gpuMaxFreq = 0L;
    Boolean isEmulator = null;
    Boolean isRooted = null;
    volatile Context mApplicationContext;
    volatile Context mContext;
    String mGpuInfoFilePath;
    boolean mIsGpuFileExist;
    TeleGLSurfaceView mTeleGLSurfaceView;
    ViewGroup mViewGroup;
    Integer memoryThreshold = 0;
    public String mobileBrand = Build.BRAND;
    public String mobileModel = Build.MODEL;
    Float screenDensity = Float.valueOf(0.0f);
    Integer screenHeight = 0;
    Integer screenWidth = 0;
    Integer storeTotalSize = null;

    public void initGpuBranchAndModelAsyc(Activity activity) {
    }

    private static final class InstanceHolder {
        public static final DeviceInfoManager INSTANCE = new DeviceInfoManager();

        private InstanceHolder() {
        }
    }

    public static DeviceInfoManager instance() {
        return InstanceHolder.INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mGpuInfoFilePath = FileUtils.getTelescopeFilePath(context, "") + File.separator + "gpu_cpu.info";
        initMemeryInfo();
        initScreenInfo();
        initEmulator();
        initRooted();
        initStorageInfo();
        initGpuAndCpuInfo();
    }

    private void initGpuAndCpuInfo() {
        readCpuAndGpuInfo();
        if (this.cpuArch == null) {
            initCpuArch();
        }
        if (this.cpuModel == null || this.cpuBrand == null) {
            initCpuBranchAndModel();
        }
        if (this.cpuProcessCount.intValue() == 0) {
            initCpuProcessCount();
        }
        if (this.cpuFreqArray == null || this.cpuMaxFreq.floatValue() == 0.0f || this.cpuMinFreq.floatValue() == 0.0f) {
            initCpuFreq();
        }
        if (this.gpuMaxFreq == null) {
            initGpuMaxFreq();
        }
    }

    public boolean getIsRooted() {
        if (this.isRooted == null) {
            initRooted();
        }
        return this.isRooted.booleanValue();
    }

    public boolean isEmulator() {
        if (this.isEmulator == null) {
            initEmulator();
        }
        return this.isEmulator.booleanValue();
    }

    public long getDeviceTotalMemory() {
        if (this.deviceTotalMemory == null || this.deviceTotalMemory.longValue() <= 0) {
            initMemeryInfo();
        }
        return this.deviceTotalMemory.longValue();
    }

    public long getMemoryThreshold() {
        if (this.memoryThreshold == null || this.memoryThreshold.intValue() <= 0) {
            initMemeryInfo();
        }
        return (long) this.memoryThreshold.intValue();
    }

    public String getCpuBrand() {
        if (this.cpuBrand == null) {
            initCpuBranchAndModel();
        }
        return this.cpuBrand;
    }

    public String getCpuModel() {
        if (this.cpuModel == null) {
            initCpuBranchAndModel();
        }
        return this.cpuModel;
    }

    public String getCpuArch() {
        if (this.cpuArch == null) {
            initCpuArch();
        }
        return this.cpuArch;
    }

    public int getCpuProcessCount() {
        if (this.cpuProcessCount == null || this.cpuProcessCount.intValue() <= 0) {
            initCpuProcessCount();
        }
        return this.cpuProcessCount.intValue();
    }

    public float getCpuMaxFreq() {
        if (this.cpuMaxFreq == null || this.cpuMaxFreq.floatValue() <= 0.0f) {
            initCpuFreq();
        }
        return this.cpuMaxFreq.floatValue();
    }

    public float getCpuMinFreq() {
        if (this.cpuMinFreq == null || this.cpuMinFreq.floatValue() <= 0.0f) {
            initCpuFreq();
        }
        return this.cpuMinFreq.floatValue();
    }

    public float[] getCpuFreqArray() {
        if (this.cpuFreqArray == null || this.cpuFreqArray.length == 0) {
            initCpuFreq();
        }
        return this.cpuFreqArray;
    }

    public long getGpuMaxFreq() {
        if (this.gpuMaxFreq == null || this.gpuMaxFreq.longValue() <= 0) {
            initGpuMaxFreq();
        }
        return this.gpuMaxFreq.longValue();
    }

    public int getScreenWidth() {
        if (this.screenWidth == null || this.screenWidth.intValue() <= 0) {
            initScreenInfo();
        }
        return this.screenWidth.intValue();
    }

    public int getScreenHeight() {
        if (this.screenHeight == null || this.screenHeight.intValue() <= 0) {
            initScreenInfo();
        }
        return this.screenHeight.intValue();
    }

    public float getScreenDensity() {
        if (this.screenDensity == null || this.screenDensity.floatValue() <= 0.0f) {
            initScreenInfo();
        }
        return this.screenDensity.floatValue();
    }

    public String getMobileModel() {
        return this.mobileModel;
    }

    public String getMobileBrand() {
        return this.mobileBrand;
    }

    public int getApiLevel() {
        return this.apiLevel.intValue();
    }

    public int getStoreTotalSize() {
        if (this.storeTotalSize == null || this.storeTotalSize.intValue() <= 0) {
            initStorageInfo();
        }
        return this.storeTotalSize.intValue();
    }

    /* access modifiers changed from: package-private */
    public void initRooted() {
        int i = 0;
        this.isRooted = false;
        String[] strArr = {"/system/bin/su", "/system/xbin/su", "/system/sbin/su", "/sbin/su", "/vendor/bin/su"};
        while (i < strArr.length) {
            try {
                if (new File(strArr[i]).exists()) {
                    this.isRooted = true;
                    return;
                }
                i++;
            } catch (Exception unused) {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void initEmulator() {
        try {
            this.isEmulator = Boolean.valueOf("1".equals(Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class}).invoke((Object) null, new Object[]{"ro.kernel.qemu"}).toString()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x002e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initMemeryInfo() {
        /*
            r8 = this;
            android.app.ActivityManager$MemoryInfo r0 = new android.app.ActivityManager$MemoryInfo
            r0.<init>()
            android.content.Context r1 = r8.mContext
            java.lang.String r2 = "activity"
            java.lang.Object r1 = r1.getSystemService(r2)
            android.app.ActivityManager r1 = (android.app.ActivityManager) r1
            r1.getMemoryInfo(r0)
            java.lang.Integer r1 = r8.apiLevel
            int r1 = r1.intValue()
            r2 = 0
            r4 = 16
            if (r1 < r4) goto L_0x0029
            long r4 = r0.totalMem     // Catch:{ Throwable -> 0x0025 }
            r6 = 1024(0x400, double:5.06E-321)
            long r4 = r4 / r6
            long r4 = r4 / r6
            goto L_0x002a
        L_0x0025:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0029:
            r4 = r2
        L_0x002a:
            int r1 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r1 != 0) goto L_0x0033
            int r1 = getTotalMemFromFile()
            long r4 = (long) r1
        L_0x0033:
            r1 = 512(0x200, double:2.53E-321)
            r6 = 256(0x100, double:1.265E-321)
            int r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r3 >= 0) goto L_0x003d
            r1 = r6
            goto L_0x0054
        L_0x003d:
            int r3 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r3 >= 0) goto L_0x0042
            goto L_0x0054
        L_0x0042:
            r1 = 1
        L_0x0043:
            r2 = 20
            if (r1 > r2) goto L_0x0053
            int r2 = r1 * 1024
            long r2 = (long) r2
            int r6 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r6 >= 0) goto L_0x0050
            r1 = r2
            goto L_0x0054
        L_0x0050:
            int r1 = r1 + 1
            goto L_0x0043
        L_0x0053:
            r1 = r4
        L_0x0054:
            java.lang.Long r1 = java.lang.Long.valueOf(r1)
            r8.deviceTotalMemory = r1
            long r0 = r0.threshold     // Catch:{ Throwable -> 0x0068 }
            int r0 = (int) r0     // Catch:{ Throwable -> 0x0068 }
            int r0 = r0 / 1024
            int r0 = r0 / 1024
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Throwable -> 0x0068 }
            r8.memoryThreshold = r0     // Catch:{ Throwable -> 0x0068 }
            goto L_0x0070
        L_0x0068:
            r0 = 64
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r8.memoryThreshold = r0
        L_0x0070:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.data.DeviceInfoManager.initMemeryInfo():void");
    }

    /* access modifiers changed from: package-private */
    public void initCpuBranchAndModel() {
        if (TextUtils.isEmpty(this.cpuModel) || TextUtils.isEmpty(this.cpuBrand)) {
            String str = null;
            String upperCase = Build.HARDWARE.toUpperCase();
            if (!TextUtils.isEmpty(upperCase)) {
                if (upperCase.contains("EXYNOS")) {
                    str = upperCase.replace("samsung", "");
                } else {
                    try {
                        Method declaredMethod = Class.forName("android.os.Build").getDeclaredMethod("getString", new Class[]{String.class});
                        declaredMethod.setAccessible(true);
                        String str2 = (String) declaredMethod.invoke(Build.class, new Object[]{"ro.board.platform"});
                        if (str2 != null) {
                            try {
                                if (str2.equals("mtk")) {
                                    str2 = upperCase;
                                }
                            } catch (Exception unused) {
                            }
                        }
                        str = str2;
                    } catch (Exception unused2) {
                    }
                    if (upperCase != null && upperCase.length() >= 4 && (TextUtils.isEmpty(str) || str.equals("unknown") || str.contains("samsungexynos") || str.contains("mrvl"))) {
                        str = upperCase;
                    }
                }
                if (str != null) {
                    str = str.toUpperCase();
                }
                if (str != null) {
                    if (str.contains("EXYNOS") || str.contains("SMDK") || str.contains("UNIVERSAL")) {
                        this.cpuBrand = "三星";
                    } else if (str.contains("MSM") || str.contains("APQ") || str.contains("QSD")) {
                        this.cpuBrand = "高通";
                    } else if (str.contains("REDHOOKBAY") || str.contains("MOOREFIELD") || str.contains("MERRIFIELD")) {
                        this.cpuBrand = "英特尔";
                    } else if (str.contains("MT")) {
                        this.cpuBrand = "联发科";
                    } else if (str.contains("OMAP")) {
                        this.cpuBrand = "德州仪器";
                    } else if (str.contains("PXA")) {
                        this.cpuBrand = "Marvell";
                    } else if (str.contains("HI") || str.contains("K3")) {
                        this.cpuBrand = "华为海思";
                    } else if (str.contains("SP") || str.contains("SC")) {
                        this.cpuBrand = "展讯";
                    } else if (str.contains("TEGRA") || str.contains("NVIDIA")) {
                        this.cpuBrand = "NVIDIA";
                    } else if (str.startsWith("LC")) {
                        this.cpuBrand = "联芯科技";
                    } else {
                        this.cpuBrand = upperCase;
                    }
                    this.cpuModel = str;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void initCpuArch() {
        Throwable th;
        int i;
        this.cpuArch = "UnKnown";
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader("/proc/cpuinfo"));
            try {
                String readLine = bufferedReader2.readLine();
                while (true) {
                    if (readLine != null) {
                        if (!readLine.contains("AArch") && !readLine.contains("ARM") && !readLine.contains("Intel(R)")) {
                            if (!readLine.contains("model name")) {
                                readLine = bufferedReader2.readLine();
                            }
                        }
                        int indexOf = readLine.indexOf(": ");
                        if (indexOf >= 0) {
                            readLine = readLine.substring(indexOf + 2);
                            if (!readLine.contains("Intel(R)")) {
                                i = readLine.indexOf(32);
                            } else {
                                i = readLine.lastIndexOf(41) + 1;
                            }
                            if (i > 0) {
                                this.cpuArch = readLine.substring(0, i);
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                IOUtils.closeQuietly((Closeable) bufferedReader2);
            } catch (Exception unused) {
                bufferedReader = bufferedReader2;
                try {
                    this.cpuArch = "UnKnown";
                    IOUtils.closeQuietly((Closeable) bufferedReader);
                } catch (Throwable th2) {
                    bufferedReader2 = bufferedReader;
                    th = th2;
                    IOUtils.closeQuietly((Closeable) bufferedReader2);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                IOUtils.closeQuietly((Closeable) bufferedReader2);
                throw th;
            }
        } catch (Exception unused2) {
            this.cpuArch = "UnKnown";
            IOUtils.closeQuietly((Closeable) bufferedReader);
        }
    }

    /* access modifiers changed from: package-private */
    public void initCpuProcessCount() {
        this.cpuProcessCount = Integer.valueOf(Runtime.getRuntime().availableProcessors());
    }

    /* access modifiers changed from: package-private */
    public void initCpuFreq() {
        if (this.cpuFreqArray == null) {
            this.cpuFreqArray = new float[getCpuProcessCount()];
        }
        int i = 0;
        while (i < getCpuProcessCount()) {
            try {
                File file = new File("/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq");
                if (file.exists()) {
                    FileReader fileReader = new FileReader(file);
                    String readLine = new BufferedReader(fileReader).readLine();
                    fileReader.close();
                    if (readLine != null) {
                        float parseLong = ((float) Long.parseLong(readLine)) / 1000000.0f;
                        this.cpuFreqArray[i] = parseLong;
                        if (this.cpuMaxFreq.floatValue() < parseLong) {
                            this.cpuMaxFreq = Float.valueOf(parseLong);
                        }
                        if (this.cpuMinFreq.floatValue() <= 0.0f) {
                            this.cpuMinFreq = Float.valueOf(parseLong);
                        } else if (this.cpuMinFreq.floatValue() > parseLong) {
                            this.cpuMinFreq = Float.valueOf(parseLong);
                        }
                    }
                }
                i++;
            } catch (Exception unused) {
            }
        }
        if (this.cpuMinFreq.floatValue() <= 0.0f) {
            this.cpuMinFreq = this.cpuMaxFreq;
        }
        if (this.mIsGpuFileExist) {
            this.mIsGpuFileExist = false;
            saveCpuAndGpuInfo();
        }
    }

    /* access modifiers changed from: package-private */
    public void initScreenInfo() {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        if (displayMetrics != null) {
            this.screenDensity = Float.valueOf(displayMetrics.density);
            this.screenWidth = Integer.valueOf(displayMetrics.widthPixels);
            this.screenHeight = Integer.valueOf(displayMetrics.heightPixels);
        }
    }

    /* access modifiers changed from: package-private */
    public void initStorageInfo() {
        long j;
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            long j2 = 0;
            if (Build.VERSION.SDK_INT >= 18) {
                j = statFs.getBlockSizeLong();
            } else {
                j = (long) statFs.getBlockSize();
            }
            if (Build.VERSION.SDK_INT >= 18) {
                j2 = statFs.getBlockCountLong();
            } else {
                j = (long) statFs.getBlockCount();
            }
            this.storeTotalSize = Integer.valueOf((int) (((j * j2) / 1024) / 1024));
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void initGpuMaxFreq() {
        this.gpuMaxFreq = Long.valueOf(getGpuFreq());
    }

    static int getTotalMemFromFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"));
            String readLine = bufferedReader.readLine();
            bufferedReader.close();
            return (readLine != null ? Integer.parseInt(readLine.replace("MemTotal:", "").replace("kB", "").replace(Operators.SPACE_STR, "")) : 0) / 1024;
        } catch (Exception unused) {
            return 1024;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0071, code lost:
        r8 = new java.io.File(r2[r3].getAbsolutePath() + "/max_freq");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0091, code lost:
        if (r8.exists() != false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0093, code lost:
        r8 = new java.io.File(r2[r3].getAbsolutePath() + "/max_gpuclk");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00b3, code lost:
        if (r8.exists() == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00b5, code lost:
        r2 = new java.io.FileReader(r8);
        r3 = new java.io.BufferedReader(r2).readLine();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00c3, code lost:
        if (r3 == null) goto L_0x00d4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00c5, code lost:
        r8 = java.lang.Long.parseLong(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00cb, code lost:
        if (r8 <= 0) goto L_0x00d3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r6 = (r8 / 1000) / 1000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00d1, code lost:
        r6 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00d3, code lost:
        r6 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        r2.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long getGpuFreq() {
        /*
            r11 = this;
            r0 = 0
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x00db }
            java.lang.String r3 = "/sys/devices/platform/gpusysfs/gpu_max_clock"
            r2.<init>(r3)     // Catch:{ Exception -> 0x00db }
            boolean r3 = r2.exists()     // Catch:{ Exception -> 0x00db }
            if (r3 != 0) goto L_0x0016
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x00db }
            java.lang.String r3 = "/sys/devices/platform/gpusysfs/max_freq"
            r2.<init>(r3)     // Catch:{ Exception -> 0x00db }
        L_0x0016:
            boolean r3 = r2.exists()     // Catch:{ Exception -> 0x00db }
            r4 = 1000(0x3e8, double:4.94E-321)
            if (r3 == 0) goto L_0x0044
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ Exception -> 0x00db }
            r3.<init>(r2)     // Catch:{ Exception -> 0x00db }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00db }
            r2.<init>(r3)     // Catch:{ Exception -> 0x00db }
            java.lang.String r2 = r2.readLine()     // Catch:{ Exception -> 0x00db }
            if (r2 == 0) goto L_0x003b
            long r6 = java.lang.Long.parseLong(r2)     // Catch:{ Exception -> 0x00db }
            int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r2 <= 0) goto L_0x003c
            long r8 = r6 / r4
            long r8 = r8 / r4
            r6 = r8
            goto L_0x003c
        L_0x003b:
            r6 = r0
        L_0x003c:
            r3.close()     // Catch:{ Exception -> 0x00dc }
            int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r2 <= 0) goto L_0x0045
            return r6
        L_0x0044:
            r6 = r0
        L_0x0045:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x00dc }
            java.lang.String r3 = "/sys/class/devfreq/"
            r2.<init>(r3)     // Catch:{ Exception -> 0x00dc }
            boolean r3 = r2.exists()     // Catch:{ Exception -> 0x00dc }
            if (r3 == 0) goto L_0x00dc
            boolean r3 = r2.isDirectory()     // Catch:{ Exception -> 0x00dc }
            if (r3 == 0) goto L_0x00dc
            java.io.File[] r2 = r2.listFiles()     // Catch:{ Exception -> 0x00dc }
            if (r2 != 0) goto L_0x005f
            return r0
        L_0x005f:
            r3 = 0
        L_0x0060:
            int r8 = r2.length     // Catch:{ Exception -> 0x00dc }
            if (r3 >= r8) goto L_0x00dc
            r8 = r2[r3]     // Catch:{ Exception -> 0x00dc }
            java.lang.String r8 = r8.getName()     // Catch:{ Exception -> 0x00dc }
            java.lang.String r9 = "kgsl"
            boolean r8 = r8.contains(r9)     // Catch:{ Exception -> 0x00dc }
            if (r8 == 0) goto L_0x00d8
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x00dc }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dc }
            r9.<init>()     // Catch:{ Exception -> 0x00dc }
            r10 = r2[r3]     // Catch:{ Exception -> 0x00dc }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ Exception -> 0x00dc }
            r9.append(r10)     // Catch:{ Exception -> 0x00dc }
            java.lang.String r10 = "/max_freq"
            r9.append(r10)     // Catch:{ Exception -> 0x00dc }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x00dc }
            r8.<init>(r9)     // Catch:{ Exception -> 0x00dc }
            boolean r9 = r8.exists()     // Catch:{ Exception -> 0x00dc }
            if (r9 != 0) goto L_0x00af
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x00dc }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dc }
            r9.<init>()     // Catch:{ Exception -> 0x00dc }
            r2 = r2[r3]     // Catch:{ Exception -> 0x00dc }
            java.lang.String r2 = r2.getAbsolutePath()     // Catch:{ Exception -> 0x00dc }
            r9.append(r2)     // Catch:{ Exception -> 0x00dc }
            java.lang.String r2 = "/max_gpuclk"
            r9.append(r2)     // Catch:{ Exception -> 0x00dc }
            java.lang.String r2 = r9.toString()     // Catch:{ Exception -> 0x00dc }
            r8.<init>(r2)     // Catch:{ Exception -> 0x00dc }
        L_0x00af:
            boolean r2 = r8.exists()     // Catch:{ Exception -> 0x00dc }
            if (r2 == 0) goto L_0x00dc
            java.io.FileReader r2 = new java.io.FileReader     // Catch:{ Exception -> 0x00dc }
            r2.<init>(r8)     // Catch:{ Exception -> 0x00dc }
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00dc }
            r3.<init>(r2)     // Catch:{ Exception -> 0x00dc }
            java.lang.String r3 = r3.readLine()     // Catch:{ Exception -> 0x00dc }
            if (r3 == 0) goto L_0x00d4
            long r8 = java.lang.Long.parseLong(r3)     // Catch:{ Exception -> 0x00dc }
            int r3 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r3 <= 0) goto L_0x00d3
            long r6 = r8 / r4
            long r6 = r6 / r4
            goto L_0x00d4
        L_0x00d1:
            r6 = r8
            goto L_0x00dc
        L_0x00d3:
            r6 = r8
        L_0x00d4:
            r2.close()     // Catch:{ Exception -> 0x00dc }
            goto L_0x00dc
        L_0x00d8:
            int r3 = r3 + 1
            goto L_0x0060
        L_0x00db:
            r6 = r0
        L_0x00dc:
            int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r2 != 0) goto L_0x00e6
            java.lang.String r0 = "/sys/devices/"
            long r6 = r11.getKgslFreq(r0)
        L_0x00e6:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.data.DeviceInfoManager.getGpuFreq():long");
    }

    /* access modifiers changed from: package-private */
    public long getKgslFreq(String str) {
        long j;
        long j2 = 0;
        try {
            File file = new File(str);
            if (!file.exists() || !file.isDirectory()) {
                j = 0;
            } else {
                File[] listFiles = file.listFiles();
                if (listFiles == null) {
                    return 0;
                }
                int i = 0;
                j = 0;
                while (i < listFiles.length) {
                    try {
                        File file2 = listFiles[i];
                        if (file2 != null && file2.getName().contains("kgsl") && file2.isDirectory()) {
                            long kgslFreq = getKgslFreq(file2.getAbsolutePath());
                            if (kgslFreq > 0) {
                                return kgslFreq;
                            }
                            j = kgslFreq;
                        }
                        i++;
                    } catch (Exception unused) {
                    }
                }
            }
            File file3 = new File(str + "/max_freq");
            if (!file3.exists()) {
                file3 = new File(str + "/max_gpuclk");
            }
            if (file3.exists()) {
                FileReader fileReader = new FileReader(file3);
                String readLine = new BufferedReader(fileReader).readLine();
                if (readLine != null) {
                    long parseLong = Long.parseLong(readLine);
                    if (parseLong > 0) {
                        try {
                            j = (parseLong / 1000) / 1000;
                        } catch (Exception unused2) {
                            return parseLong;
                        }
                    } else {
                        j2 = parseLong;
                        fileReader.close();
                        return j2;
                    }
                }
                j2 = j;
                fileReader.close();
                return j2;
            }
            return j;
        } catch (Exception unused3) {
            return j2;
        }
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        this.mTeleGLSurfaceView = null;
        this.mViewGroup = null;
    }

    /* access modifiers changed from: package-private */
    public void readCpuAndGpuInfo() {
        BufferedReader bufferedReader;
        Exception e;
        this.mGpuInfoFilePath + "retry";
        File file = new File(this.mGpuInfoFilePath);
        if (file.exists()) {
            this.mIsGpuFileExist = true;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                try {
                    this.cpuBrand = bufferedReader.readLine();
                    this.cpuModel = bufferedReader.readLine();
                    this.cpuArch = bufferedReader.readLine();
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        this.cpuProcessCount = Integer.valueOf(Integer.parseInt(readLine));
                    }
                    String readLine2 = bufferedReader.readLine();
                    if (readLine2 != null) {
                        this.cpuMaxFreq = Float.valueOf(Float.parseFloat(readLine2));
                    }
                    String readLine3 = bufferedReader.readLine();
                    if (readLine3 != null) {
                        this.gpuMaxFreq = Long.valueOf(Long.parseLong(readLine3));
                    }
                    String readLine4 = bufferedReader.readLine();
                    if (readLine4 != null) {
                        this.cpuMinFreq = Float.valueOf(Float.parseFloat(readLine4));
                        if (this.cpuMinFreq.floatValue() <= 0.0f) {
                            this.cpuMinFreq = this.cpuMaxFreq;
                        }
                    }
                    String readLine5 = bufferedReader.readLine();
                    if (this.cpuFreqArray == null) {
                        this.cpuFreqArray = new float[getCpuProcessCount()];
                    }
                    if (readLine5 != null) {
                        try {
                            String[] split = readLine5.split(",");
                            if (split != null && split.length > 0) {
                                for (int i = 0; i < split.length; i++) {
                                    this.cpuFreqArray[i] = Float.parseFloat(split[i]);
                                }
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                }
            } catch (Exception e4) {
                e = e4;
                bufferedReader = null;
                try {
                    e.printStackTrace();
                    IOUtils.closeQuietly((Closeable) bufferedReader);
                } catch (Throwable th) {
                    th = th;
                    IOUtils.closeQuietly((Closeable) bufferedReader);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                bufferedReader = null;
                IOUtils.closeQuietly((Closeable) bufferedReader);
                throw th;
            }
            IOUtils.closeQuietly((Closeable) bufferedReader);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void saveCpuAndGpuInfo() {
        if (!this.mIsGpuFileExist) {
            this.mIsGpuFileExist = true;
            BufferedWriter bufferedWriter = null;
            File file = new File(this.mGpuInfoFilePath);
            if (file.exists()) {
                file.delete();
            }
            try {
                BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(file));
                try {
                    bufferedWriter2.write(this.cpuBrand);
                    bufferedWriter2.newLine();
                    bufferedWriter2.write(this.cpuModel);
                    bufferedWriter2.newLine();
                    bufferedWriter2.write(this.cpuArch);
                    bufferedWriter2.newLine();
                    bufferedWriter2.write(String.valueOf(this.cpuProcessCount));
                    bufferedWriter2.newLine();
                    bufferedWriter2.write(String.valueOf(this.cpuMaxFreq));
                    bufferedWriter2.newLine();
                    bufferedWriter2.write(String.valueOf(this.gpuMaxFreq));
                    bufferedWriter2.newLine();
                    bufferedWriter2.write(String.valueOf(this.cpuMinFreq));
                    bufferedWriter2.newLine();
                    StringBuilder sb = new StringBuilder(50);
                    if (this.cpuFreqArray != null && this.cpuFreqArray.length > 0) {
                        for (int i = 0; i < this.cpuFreqArray.length; i++) {
                            sb.append(this.cpuFreqArray[i]);
                            if (i < this.cpuFreqArray.length - 1) {
                                sb.append(',');
                            }
                        }
                    }
                    bufferedWriter2.write(sb.toString());
                    bufferedWriter2.flush();
                    IOUtils.closeQuietly((Closeable) bufferedWriter2);
                } catch (Exception e) {
                    e = e;
                    bufferedWriter = bufferedWriter2;
                    try {
                        e.printStackTrace();
                        this.mIsGpuFileExist = false;
                        IOUtils.closeQuietly((Closeable) bufferedWriter);
                    } catch (Throwable th) {
                        th = th;
                        bufferedWriter2 = bufferedWriter;
                        IOUtils.closeQuietly((Closeable) bufferedWriter2);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    IOUtils.closeQuietly((Closeable) bufferedWriter2);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                this.mIsGpuFileExist = false;
                IOUtils.closeQuietly((Closeable) bufferedWriter);
            }
        }
    }

    private String getSystemPropertyFromShell(String str) {
        if (str == null || str.trim().equals("")) {
            return "";
        }
        String str2 = "";
        try {
            Process start = new ProcessBuilder(new String[]{"/system/bin/getprop", str}).start();
            BufferedReader bufferedReader = null;
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(start.getInputStream()));
                try {
                    String readLine = bufferedReader2.readLine();
                    if (readLine != null) {
                        str2 = readLine.trim();
                    }
                    start.destroy();
                    IOUtils.closeQuietly((Closeable) bufferedReader2);
                    return str2;
                } catch (Throwable th) {
                    BufferedReader bufferedReader3 = bufferedReader2;
                    th = th;
                    bufferedReader = bufferedReader3;
                    start.destroy();
                    IOUtils.closeQuietly((Closeable) bufferedReader);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                start.destroy();
                IOUtils.closeQuietly((Closeable) bufferedReader);
                throw th;
            }
        } catch (IOException unused) {
            Log.e(TAG, "Couldn't find the property value for '" + str + DXBindingXConstant.SINGLE_QUOTE);
        }
    }

    class TeleGLSurfaceView extends GLSurfaceView {
        TeleRenderer mRenderer;

        public TeleGLSurfaceView(Context context) {
            super(context);
            setEGLConfigChooser(8, 8, 8, 8, 0, 0);
            this.mRenderer = new TeleRenderer();
            setRenderer(this.mRenderer);
        }
    }

    class TeleRenderer implements GLSurfaceView.Renderer {
        public void onDrawFrame(GL10 gl10) {
        }

        public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        }

        TeleRenderer() {
        }

        public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            try {
                DeviceInfoManager.this.destroy();
                DeviceInfoManager.this.saveCpuAndGpuInfo();
            } catch (Throwable unused) {
            }
        }
    }
}
