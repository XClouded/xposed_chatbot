package com.taobao.onlinemonitor;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import javax.microedition.khronos.opengles.GL10;

public class HardWareInfo {
    public String mCpuBrand;
    public int mCpuCount = 0;
    public float[] mCpuFreqArray;
    public float mCpuMaxFreq;
    public float mCpuMinFreq;
    public String mCpuName;
    public float mDesty;
    String mFileGPUinfo;
    public String mGpuBrand;
    public long mGpuFreq;
    public String mGpuName;
    public int mHeight;
    boolean mIsGpuFileExist;
    public OnLineMonitor mOnLineMonitor;
    OnlineGLSurfaceView mOnlineGLSurfaceView;
    ViewGroup mViewGroup;
    public int mWidth;

    /* JADX WARNING: Can't wrap try/catch for region: R(26:13|14|(1:16)|17|(1:19)|20|21|22|(3:24|(1:26)|27)|31|(1:33)|34|(1:36)|37|(2:39|(1:41))|42|(1:44)|45|(4:47|48|49|(3:53|(3:56|57|54)|80))|61|62|63|(1:65)(1:66)|67|68|(1:70)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:67:0x0195 */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x019c A[Catch:{ Exception -> 0x01bf }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public HardWareInfo(com.taobao.onlinemonitor.OnLineMonitor r7, android.content.Context r8) {
        /*
            r6 = this;
            r6.<init>()
            r0 = 0
            r6.mCpuCount = r0
            r6.mOnLineMonitor = r7
            android.content.res.Resources r1 = r8.getResources()     // Catch:{ Throwable -> 0x0036 }
            android.util.DisplayMetrics r1 = r1.getDisplayMetrics()     // Catch:{ Throwable -> 0x0036 }
            if (r1 == 0) goto L_0x0036
            float r2 = r1.density     // Catch:{ Throwable -> 0x0036 }
            r6.mDesty = r2     // Catch:{ Throwable -> 0x0036 }
            int r2 = r1.widthPixels     // Catch:{ Throwable -> 0x0036 }
            r6.mWidth = r2     // Catch:{ Throwable -> 0x0036 }
            int r1 = r1.heightPixels     // Catch:{ Throwable -> 0x0036 }
            r6.mHeight = r1     // Catch:{ Throwable -> 0x0036 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r1 = r7.mOnLineStat     // Catch:{ Throwable -> 0x0036 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r1 = r1.deviceInfo     // Catch:{ Throwable -> 0x0036 }
            int r2 = r6.mHeight     // Catch:{ Throwable -> 0x0036 }
            r1.screenHeght = r2     // Catch:{ Throwable -> 0x0036 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r1 = r7.mOnLineStat     // Catch:{ Throwable -> 0x0036 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r1 = r1.deviceInfo     // Catch:{ Throwable -> 0x0036 }
            int r2 = r6.mWidth     // Catch:{ Throwable -> 0x0036 }
            r1.screenWidth = r2     // Catch:{ Throwable -> 0x0036 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r7 = r7.mOnLineStat     // Catch:{ Throwable -> 0x0036 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r7 = r7.deviceInfo     // Catch:{ Throwable -> 0x0036 }
            float r1 = r6.mDesty     // Catch:{ Throwable -> 0x0036 }
            r7.density = r1     // Catch:{ Throwable -> 0x0036 }
        L_0x0036:
            java.io.File r7 = r8.getFilesDir()
            if (r7 == 0) goto L_0x0054
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r7 = r7.getAbsolutePath()
            r8.append(r7)
            java.lang.String r7 = "/cpugpuinfo"
            r8.append(r7)
            java.lang.String r7 = r8.toString()
            r6.mFileGPUinfo = r7
            goto L_0x0058
        L_0x0054:
            java.lang.String r7 = ""
            r6.mFileGPUinfo = r7
        L_0x0058:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = r6.mFileGPUinfo
            r7.append(r8)
            java.lang.String r8 = "retry"
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            java.io.File r8 = new java.io.File
            java.lang.String r1 = r6.mFileGPUinfo
            r8.<init>(r1)
            boolean r1 = r8.exists()
            if (r1 == 0) goto L_0x01c3
            r1 = 1
            r6.mIsGpuFileExist = r1
            java.io.FileReader r2 = new java.io.FileReader     // Catch:{ Exception -> 0x01bf }
            r2.<init>(r8)     // Catch:{ Exception -> 0x01bf }
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x01bf }
            r3.<init>(r2)     // Catch:{ Exception -> 0x01bf }
            java.lang.String r2 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            r6.mCpuBrand = r2     // Catch:{ Exception -> 0x01bf }
            java.lang.String r2 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            r6.mCpuName = r2     // Catch:{ Exception -> 0x01bf }
            java.lang.String r2 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            if (r2 == 0) goto L_0x009d
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Exception -> 0x01bf }
            r6.mCpuCount = r2     // Catch:{ Exception -> 0x01bf }
        L_0x009d:
            java.lang.String r2 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            if (r2 == 0) goto L_0x00a9
            float r2 = java.lang.Float.parseFloat(r2)     // Catch:{ Exception -> 0x01bf }
            r6.mCpuMaxFreq = r2     // Catch:{ Exception -> 0x01bf }
        L_0x00a9:
            java.lang.String r2 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            r6.mGpuName = r2     // Catch:{ Exception -> 0x01bf }
            java.lang.String r2 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            r6.mGpuBrand = r2     // Catch:{ Exception -> 0x01bf }
            java.lang.String r2 = r6.mGpuName     // Catch:{ Throwable -> 0x00d6 }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x00d6 }
            if (r2 == 0) goto L_0x00da
            java.io.File r2 = new java.io.File     // Catch:{ Throwable -> 0x00d6 }
            r2.<init>(r7)     // Catch:{ Throwable -> 0x00d6 }
            boolean r7 = r2.exists()     // Catch:{ Throwable -> 0x00d6 }
            if (r7 != 0) goto L_0x00d2
            java.lang.String r7 = "OnLineMonitor"
            java.lang.String r4 = "删除旧的配置文件"
            android.util.Log.e(r7, r4)     // Catch:{ Throwable -> 0x00d6 }
            r8.delete()     // Catch:{ Throwable -> 0x00d6 }
        L_0x00d2:
            r2.createNewFile()     // Catch:{ Throwable -> 0x00d6 }
            goto L_0x00da
        L_0x00d6:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Exception -> 0x01bf }
        L_0x00da:
            java.lang.String r7 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            if (r7 == 0) goto L_0x00f0
            long r7 = java.lang.Long.parseLong(r7)     // Catch:{ Exception -> 0x01bf }
            r6.mGpuFreq = r7     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor r7 = r6.mOnLineMonitor     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r7 = r7.mOnLineStat     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r7 = r7.deviceInfo     // Catch:{ Exception -> 0x01bf }
            long r4 = r6.mGpuFreq     // Catch:{ Exception -> 0x01bf }
            r7.gpuMaxFreq = r4     // Catch:{ Exception -> 0x01bf }
        L_0x00f0:
            java.lang.String r7 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            if (r7 == 0) goto L_0x00fe
            com.taobao.onlinemonitor.OnLineMonitor r8 = r6.mOnLineMonitor     // Catch:{ Exception -> 0x01bf }
            int r7 = java.lang.Integer.parseInt(r7)     // Catch:{ Exception -> 0x01bf }
            r8.mStatusBarHeight = r7     // Catch:{ Exception -> 0x01bf }
        L_0x00fe:
            java.lang.String r7 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            if (r7 == 0) goto L_0x0115
            float r7 = java.lang.Float.parseFloat(r7)     // Catch:{ Exception -> 0x01bf }
            r6.mCpuMinFreq = r7     // Catch:{ Exception -> 0x01bf }
            float r7 = r6.mCpuMinFreq     // Catch:{ Exception -> 0x01bf }
            r8 = 0
            int r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1))
            if (r7 > 0) goto L_0x0115
            float r7 = r6.mCpuMaxFreq     // Catch:{ Exception -> 0x01bf }
            r6.mCpuMinFreq = r7     // Catch:{ Exception -> 0x01bf }
        L_0x0115:
            java.lang.String r7 = r3.readLine()     // Catch:{ Exception -> 0x01bf }
            float[] r8 = r6.mCpuFreqArray     // Catch:{ Exception -> 0x01bf }
            if (r8 != 0) goto L_0x012f
            int r8 = r6.getCpuCore()     // Catch:{ Exception -> 0x01bf }
            float[] r8 = new float[r8]     // Catch:{ Exception -> 0x01bf }
            r6.mCpuFreqArray = r8     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor r8 = r6.mOnLineMonitor     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r8 = r8.mOnLineStat     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r8 = r8.deviceInfo     // Catch:{ Exception -> 0x01bf }
            float[] r2 = r6.mCpuFreqArray     // Catch:{ Exception -> 0x01bf }
            r8.cpuFreqArray = r2     // Catch:{ Exception -> 0x01bf }
        L_0x012f:
            com.taobao.onlinemonitor.OnLineMonitor r8 = r6.mOnLineMonitor     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r8 = r8.mOnLineStat     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r8 = r8.deviceInfo     // Catch:{ Exception -> 0x01bf }
            float r2 = r6.mCpuMinFreq     // Catch:{ Exception -> 0x01bf }
            r8.cpuMinFreq = r2     // Catch:{ Exception -> 0x01bf }
            if (r7 == 0) goto L_0x015b
            java.lang.String r8 = ","
            java.lang.String[] r7 = r7.split(r8)     // Catch:{ Exception -> 0x0157 }
            if (r7 == 0) goto L_0x015b
            int r8 = r7.length     // Catch:{ Exception -> 0x0157 }
            if (r8 <= 0) goto L_0x015b
            r8 = 0
        L_0x0147:
            int r2 = r7.length     // Catch:{ Exception -> 0x0157 }
            if (r8 >= r2) goto L_0x015b
            float[] r2 = r6.mCpuFreqArray     // Catch:{ Exception -> 0x0157 }
            r4 = r7[r8]     // Catch:{ Exception -> 0x0157 }
            float r4 = java.lang.Float.parseFloat(r4)     // Catch:{ Exception -> 0x0157 }
            r2[r8] = r4     // Catch:{ Exception -> 0x0157 }
            int r8 = r8 + 1
            goto L_0x0147
        L_0x0157:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Exception -> 0x01bf }
        L_0x015b:
            com.taobao.onlinemonitor.OnLineMonitor r7 = r6.mOnLineMonitor     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r7 = r7.mOnLineStat     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r7 = r7.deviceInfo     // Catch:{ Exception -> 0x01bf }
            java.lang.String r8 = r6.mGpuName     // Catch:{ Exception -> 0x01bf }
            r7.gpuModel = r8     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor r7 = r6.mOnLineMonitor     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r7 = r7.mOnLineStat     // Catch:{ Exception -> 0x01bf }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r7 = r7.deviceInfo     // Catch:{ Exception -> 0x01bf }
            java.lang.String r8 = r6.mGpuBrand     // Catch:{ Exception -> 0x01bf }
            r7.gpuBrand = r8     // Catch:{ Exception -> 0x01bf }
            java.lang.String r7 = r3.readLine()     // Catch:{ Throwable -> 0x0195 }
            boolean r8 = android.text.TextUtils.isEmpty(r7)     // Catch:{ Throwable -> 0x0195 }
            if (r8 != 0) goto L_0x0182
            com.taobao.onlinemonitor.OnLineMonitor r8 = r6.mOnLineMonitor     // Catch:{ Throwable -> 0x0195 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r8 = r8.mOnLineStat     // Catch:{ Throwable -> 0x0195 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r8 = r8.deviceInfo     // Catch:{ Throwable -> 0x0195 }
            r8.cpuArch = r7     // Catch:{ Throwable -> 0x0195 }
            goto L_0x0195
        L_0x0182:
            com.taobao.onlinemonitor.OnLineMonitor r7 = r6.mOnLineMonitor     // Catch:{ Throwable -> 0x0195 }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r7 = r7.mOnLineStat     // Catch:{ Throwable -> 0x0195 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r7 = r7.deviceInfo     // Catch:{ Throwable -> 0x0195 }
            java.lang.String r8 = r6.getCpuArch()     // Catch:{ Throwable -> 0x0195 }
            r7.cpuArch = r8     // Catch:{ Throwable -> 0x0195 }
            r6.mIsGpuFileExist = r0     // Catch:{ Throwable -> 0x0195 }
            r6.saveCpuAndGpuInfo()     // Catch:{ Throwable -> 0x0195 }
            r6.mIsGpuFileExist = r1     // Catch:{ Throwable -> 0x0195 }
        L_0x0195:
            r3.close()     // Catch:{ Exception -> 0x01bf }
            boolean r7 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug     // Catch:{ Exception -> 0x01bf }
            if (r7 == 0) goto L_0x01c3
            java.lang.String r7 = "OnLineMonitor"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01bf }
            r8.<init>()     // Catch:{ Exception -> 0x01bf }
            java.lang.String r0 = "GPU Form prop= "
            r8.append(r0)     // Catch:{ Exception -> 0x01bf }
            java.lang.String r0 = r6.mGpuName     // Catch:{ Exception -> 0x01bf }
            r8.append(r0)     // Catch:{ Exception -> 0x01bf }
            java.lang.String r0 = ", Brand="
            r8.append(r0)     // Catch:{ Exception -> 0x01bf }
            java.lang.String r0 = r6.mGpuBrand     // Catch:{ Exception -> 0x01bf }
            r8.append(r0)     // Catch:{ Exception -> 0x01bf }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x01bf }
            android.util.Log.e(r7, r8)     // Catch:{ Exception -> 0x01bf }
            goto L_0x01c3
        L_0x01bf:
            r7 = move-exception
            r7.printStackTrace()
        L_0x01c3:
            java.lang.String r7 = r6.mCpuBrand
            boolean r7 = android.text.TextUtils.isEmpty(r7)
            if (r7 != 0) goto L_0x01d3
            java.lang.String r7 = r6.mCpuName
            boolean r7 = android.text.TextUtils.isEmpty(r7)
            if (r7 == 0) goto L_0x01d6
        L_0x01d3:
            r6.getCpuInfo()
        L_0x01d6:
            com.taobao.onlinemonitor.OnLineMonitor r7 = r6.mOnLineMonitor
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r7 = r7.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r7 = r7.deviceInfo
            java.lang.String r8 = r6.mCpuBrand
            r7.cpuBrand = r8
            com.taobao.onlinemonitor.OnLineMonitor r7 = r6.mOnLineMonitor
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r7 = r7.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r7 = r7.deviceInfo
            java.lang.String r8 = r6.mCpuName
            r7.cpuModel = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.HardWareInfo.<init>(com.taobao.onlinemonitor.OnLineMonitor, android.content.Context):void");
    }

    public void getGpuInfo(Context context) {
        if (!this.mIsGpuFileExist) {
            try {
                if (context instanceof Activity) {
                    this.mViewGroup = (ViewGroup) ((Activity) context).getWindow().getDecorView();
                    if (this.mViewGroup != null) {
                        this.mOnlineGLSurfaceView = new OnlineGLSurfaceView(context);
                        this.mOnlineGLSurfaceView.setAlpha(0.0f);
                        this.mViewGroup.addView(this.mOnlineGLSurfaceView, new ViewGroup.LayoutParams(1, 1));
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00de A[SYNTHETIC, Splitter:B:33:0x00de] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ed A[SYNTHETIC, Splitter:B:39:0x00ed] */
    /* JADX WARNING: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveCpuAndGpuInfo() {
        /*
            r7 = this;
            boolean r0 = r7.mIsGpuFileExist
            if (r0 != 0) goto L_0x00f9
            java.lang.String r0 = r7.mGpuName
            if (r0 != 0) goto L_0x000a
            goto L_0x00f9
        L_0x000a:
            r0 = 0
            r1 = 1
            r7.mIsGpuFileExist = r1
            java.io.File r2 = new java.io.File
            java.lang.String r3 = r7.mFileGPUinfo
            r2.<init>(r3)
            boolean r3 = r2.exists()
            if (r3 == 0) goto L_0x001e
            r2.delete()
        L_0x001e:
            r3 = 0
            java.io.BufferedWriter r4 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x00d4, all -> 0x00d0 }
            java.io.FileWriter r5 = new java.io.FileWriter     // Catch:{ Exception -> 0x00d4, all -> 0x00d0 }
            r5.<init>(r2)     // Catch:{ Exception -> 0x00d4, all -> 0x00d0 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x00d4, all -> 0x00d0 }
            java.lang.String r0 = r7.mCpuBrand     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            java.lang.String r0 = r7.mCpuName     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            int r0 = r7.mCpuCount     // Catch:{ Exception -> 0x00ce }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            float r0 = r7.mCpuMaxFreq     // Catch:{ Exception -> 0x00ce }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            java.lang.String r0 = r7.mGpuName     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            java.lang.String r0 = r7.mGpuBrand     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            long r5 = r7.mGpuFreq     // Catch:{ Exception -> 0x00ce }
            java.lang.String r0 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            com.taobao.onlinemonitor.OnLineMonitor r0 = r7.mOnLineMonitor     // Catch:{ Exception -> 0x00ce }
            int r0 = r0.mStatusBarHeight     // Catch:{ Exception -> 0x00ce }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            float r0 = r7.mCpuMinFreq     // Catch:{ Exception -> 0x00ce }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ce }
            r2 = 50
            r0.<init>(r2)     // Catch:{ Exception -> 0x00ce }
            float[] r2 = r7.mCpuFreqArray     // Catch:{ Exception -> 0x00ce }
            if (r2 == 0) goto L_0x00b2
            float[] r2 = r7.mCpuFreqArray     // Catch:{ Exception -> 0x00ce }
            int r2 = r2.length     // Catch:{ Exception -> 0x00ce }
            if (r2 <= 0) goto L_0x00b2
            r2 = 0
        L_0x0098:
            float[] r5 = r7.mCpuFreqArray     // Catch:{ Exception -> 0x00ce }
            int r5 = r5.length     // Catch:{ Exception -> 0x00ce }
            if (r2 >= r5) goto L_0x00b2
            float[] r5 = r7.mCpuFreqArray     // Catch:{ Exception -> 0x00ce }
            r5 = r5[r2]     // Catch:{ Exception -> 0x00ce }
            r0.append(r5)     // Catch:{ Exception -> 0x00ce }
            float[] r5 = r7.mCpuFreqArray     // Catch:{ Exception -> 0x00ce }
            int r5 = r5.length     // Catch:{ Exception -> 0x00ce }
            int r5 = r5 - r1
            if (r2 >= r5) goto L_0x00af
            r5 = 44
            r0.append(r5)     // Catch:{ Exception -> 0x00ce }
        L_0x00af:
            int r2 = r2 + 1
            goto L_0x0098
        L_0x00b2:
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.newLine()     // Catch:{ Exception -> 0x00ce }
            com.taobao.onlinemonitor.OnLineMonitor r0 = r7.mOnLineMonitor     // Catch:{ Exception -> 0x00ce }
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r0 = r0.mOnLineStat     // Catch:{ Exception -> 0x00ce }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r0 = r0.deviceInfo     // Catch:{ Exception -> 0x00ce }
            java.lang.String r0 = r0.cpuArch     // Catch:{ Exception -> 0x00ce }
            r4.write(r0)     // Catch:{ Exception -> 0x00ce }
            r4.flush()     // Catch:{ IOException -> 0x00e5 }
            r4.close()     // Catch:{ IOException -> 0x00e5 }
            goto L_0x00e9
        L_0x00ce:
            r0 = move-exception
            goto L_0x00d7
        L_0x00d0:
            r1 = move-exception
            r4 = r0
            r0 = r1
            goto L_0x00eb
        L_0x00d4:
            r1 = move-exception
            r4 = r0
            r0 = r1
        L_0x00d7:
            r0.printStackTrace()     // Catch:{ all -> 0x00ea }
            r7.mIsGpuFileExist = r3     // Catch:{ all -> 0x00ea }
            if (r4 == 0) goto L_0x00e9
            r4.flush()     // Catch:{ IOException -> 0x00e5 }
            r4.close()     // Catch:{ IOException -> 0x00e5 }
            goto L_0x00e9
        L_0x00e5:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00e9:
            return
        L_0x00ea:
            r0 = move-exception
        L_0x00eb:
            if (r4 == 0) goto L_0x00f8
            r4.flush()     // Catch:{ IOException -> 0x00f4 }
            r4.close()     // Catch:{ IOException -> 0x00f4 }
            goto L_0x00f8
        L_0x00f4:
            r1 = move-exception
            r1.printStackTrace()
        L_0x00f8:
            throw r0
        L_0x00f9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.HardWareInfo.saveCpuAndGpuInfo():void");
    }

    public String getCpuArch() {
        int i;
        if (!TextUtils.isEmpty(this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuArch)) {
            return this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuArch;
        }
        String str = "UnKnown";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String readLine = bufferedReader.readLine();
            while (true) {
                if (readLine != null) {
                    if (!readLine.contains("AArch") && !readLine.contains("ARM") && !readLine.contains("Intel(R)")) {
                        if (!readLine.contains("model name")) {
                            readLine = bufferedReader.readLine();
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
                            str = readLine.substring(0, i);
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            bufferedReader.close();
            return str;
        } catch (Exception unused) {
            return "UnKnown";
        }
    }

    public float getMaxCpuFreq() {
        if (this.mCpuMaxFreq > 0.0f && this.mCpuFreqArray != null) {
            return this.mCpuMaxFreq;
        }
        if (this.mCpuFreqArray == null) {
            this.mCpuFreqArray = new float[this.mOnLineMonitor.mCpuProcessCount];
            this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuFreqArray = this.mCpuFreqArray;
        }
        int i = 0;
        while (i < this.mOnLineMonitor.mCpuProcessCount) {
            try {
                File file = new File("/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq");
                if (file.exists()) {
                    FileReader fileReader = new FileReader(file);
                    String readLine = new BufferedReader(fileReader).readLine();
                    fileReader.close();
                    if (readLine != null) {
                        float parseLong = ((float) Long.parseLong(readLine)) / 1000000.0f;
                        this.mCpuFreqArray[i] = parseLong;
                        if (this.mCpuMaxFreq < parseLong) {
                            this.mCpuMaxFreq = parseLong;
                        }
                        if (this.mCpuMinFreq == 0.0f) {
                            this.mCpuMinFreq = parseLong;
                        } else if (this.mCpuMinFreq > parseLong) {
                            this.mCpuMinFreq = parseLong;
                        }
                    }
                }
                i++;
            } catch (Exception unused) {
            }
        }
        if (this.mCpuMinFreq == 0.0f) {
            this.mCpuMinFreq = this.mCpuMaxFreq;
        }
        this.mOnLineMonitor.mOnLineStat.deviceInfo.cpuMinFreq = this.mCpuMinFreq;
        if (this.mIsGpuFileExist) {
            this.mIsGpuFileExist = false;
            saveCpuAndGpuInfo();
        }
        return this.mCpuMaxFreq;
    }

    public int getCpuCore() {
        if (this.mCpuCount == 0) {
            this.mCpuCount = Runtime.getRuntime().availableProcessors();
        }
        return this.mCpuCount;
    }

    /* access modifiers changed from: package-private */
    public void getCpuInfo() {
        String str;
        String upperCase = Build.HARDWARE.toUpperCase();
        if (!TextUtils.isEmpty(upperCase)) {
            if (TextUtils.isEmpty(this.mCpuName) || TextUtils.isEmpty(this.mCpuBrand)) {
                if (upperCase.contains("EXYNOS")) {
                    str = upperCase.replace("samsung", "");
                } else {
                    str = null;
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
                        this.mCpuBrand = "三星";
                    } else if (str.contains("MSM") || str.contains("APQ") || str.contains("QSD") || str.contains("SDM")) {
                        this.mCpuBrand = "高通";
                    } else if (str.contains("REDHOOKBAY") || str.contains("MOOREFIELD") || str.contains("MERRIFIELD")) {
                        this.mCpuBrand = "英特尔";
                    } else if (str.contains("MT")) {
                        this.mCpuBrand = "联发科";
                    } else if (str.contains("OMAP")) {
                        this.mCpuBrand = "德州仪器";
                    } else if (str.contains("PXA")) {
                        this.mCpuBrand = "Marvell";
                    } else if (str.contains("HI") || str.contains("K3")) {
                        this.mCpuBrand = "华为海思";
                    } else if (str.contains("SP") || str.contains("SC")) {
                        this.mCpuBrand = "展讯";
                    } else if (str.contains("TEGRA") || str.contains("NVIDIA")) {
                        this.mCpuBrand = "NVIDIA";
                    } else if (str.startsWith("LC")) {
                        this.mCpuBrand = "联芯科技";
                    } else {
                        this.mCpuBrand = upperCase;
                    }
                    this.mCpuName = str;
                    return;
                }
                return;
            }
            String str3 = this.mCpuName;
        }
    }

    public long getMaxGpuFreq() {
        return this.mGpuFreq;
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
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.HardWareInfo.getGpuFreq():long");
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
        this.mOnlineGLSurfaceView = null;
        this.mViewGroup = null;
    }

    class OnlineRenderer implements GLSurfaceView.Renderer {
        public void onDrawFrame(GL10 gl10) {
        }

        public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        }

        OnlineRenderer() {
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x004d */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onSurfaceCreated(javax.microedition.khronos.opengles.GL10 r3, javax.microedition.khronos.egl.EGLConfig r4) {
            /*
                r2 = this;
                com.taobao.onlinemonitor.HardWareInfo r4 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x005b }
                r0 = 7937(0x1f01, float:1.1122E-41)
                java.lang.String r0 = r3.glGetString(r0)     // Catch:{ Throwable -> 0x005b }
                r4.mGpuName = r0     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.HardWareInfo r4 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x005b }
                r0 = 7936(0x1f00, float:1.1121E-41)
                java.lang.String r3 = r3.glGetString(r0)     // Catch:{ Throwable -> 0x005b }
                r4.mGpuBrand = r3     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.HardWareInfo r3 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.OnLineMonitor r3 = r3.mOnLineMonitor     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r3.mOnLineStat     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r3 = r3.deviceInfo     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.HardWareInfo r4 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x005b }
                java.lang.String r4 = r4.mGpuName     // Catch:{ Throwable -> 0x005b }
                r3.gpuModel = r4     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.HardWareInfo r3 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.OnLineMonitor r3 = r3.mOnLineMonitor     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r3.mOnLineStat     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r3 = r3.deviceInfo     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.HardWareInfo r4 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x005b }
                java.lang.String r4 = r4.mGpuBrand     // Catch:{ Throwable -> 0x005b }
                r3.gpuBrand = r4     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.HardWareInfo r3 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x005b }
                r3.destroy()     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.HardWareInfo r3 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x004d }
                com.taobao.onlinemonitor.HardWareInfo r4 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x004d }
                long r0 = r4.getGpuFreq()     // Catch:{ Throwable -> 0x004d }
                r3.mGpuFreq = r0     // Catch:{ Throwable -> 0x004d }
                com.taobao.onlinemonitor.HardWareInfo r3 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x004d }
                com.taobao.onlinemonitor.OnLineMonitor r3 = r3.mOnLineMonitor     // Catch:{ Throwable -> 0x004d }
                com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r3.mOnLineStat     // Catch:{ Throwable -> 0x004d }
                com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r3 = r3.deviceInfo     // Catch:{ Throwable -> 0x004d }
                com.taobao.onlinemonitor.HardWareInfo r4 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x004d }
                long r0 = r4.mGpuFreq     // Catch:{ Throwable -> 0x004d }
                r3.gpuMaxFreq = r0     // Catch:{ Throwable -> 0x004d }
            L_0x004d:
                com.taobao.onlinemonitor.HardWareInfo r3 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.OnLineMonitor r3 = r3.mOnLineMonitor     // Catch:{ Throwable -> 0x005b }
                r4 = 0
                r3.mDevicesScore = r4     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.HardWareInfo r3 = com.taobao.onlinemonitor.HardWareInfo.this     // Catch:{ Throwable -> 0x005b }
                com.taobao.onlinemonitor.OnLineMonitor r3 = r3.mOnLineMonitor     // Catch:{ Throwable -> 0x005b }
                r3.evaluateSystemPerformance()     // Catch:{ Throwable -> 0x005b }
            L_0x005b:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.HardWareInfo.OnlineRenderer.onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig):void");
        }
    }

    class OnlineGLSurfaceView extends GLSurfaceView {
        OnlineRenderer mRenderer;

        public OnlineGLSurfaceView(Context context) {
            super(context);
            setEGLConfigChooser(8, 8, 8, 8, 0, 0);
            this.mRenderer = new OnlineRenderer();
            setRenderer(this.mRenderer);
        }
    }
}
