package com.ali.alihadeviceevaluator.old;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.ali.alihadeviceevaluator.AliHAHardware;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import javax.microedition.khronos.opengles.GL10;

public class HardWareInfo {
    public String mCpuArch;
    public String mCpuBrand;
    public int mCpuCount = 0;
    public float[] mCpuFreqArray;
    public float mCpuMaxFreq;
    public float mCpuMinFreq;
    public String mCpuName;
    public float mDesty = AliHAHardware.getInstance().getDisplayInfo().mDensity;
    String mFileGPUinfo;
    public String mGpuBrand;
    public long mGpuFreq;
    public String mGpuName;
    public int mHeight = AliHAHardware.getInstance().getDisplayInfo().mHeightPixels;
    boolean mLocalFileExist;
    OnlineGLSurfaceView mOnlineGLSurfaceView;
    ViewGroup mViewGroup;
    public int mWidth = AliHAHardware.getInstance().getDisplayInfo().mWidthPixels;
    /* access modifiers changed from: private */
    public OldScoreMaker oldScoreMaker = new OldScoreMaker();

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00d6, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x017c, code lost:
        r0 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x017c A[ExcHandler: all (th java.lang.Throwable), Splitter:B:9:0x006e] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0191 A[SYNTHETIC, Splitter:B:62:0x0191] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x019c A[SYNTHETIC, Splitter:B:67:0x019c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public HardWareInfo(android.content.Context r5) {
        /*
            r4 = this;
            r4.<init>()
            r0 = 0
            r4.mCpuCount = r0
            com.ali.alihadeviceevaluator.old.OldScoreMaker r1 = new com.ali.alihadeviceevaluator.old.OldScoreMaker
            r1.<init>()
            r4.oldScoreMaker = r1
            com.ali.alihadeviceevaluator.AliHAHardware r1 = com.ali.alihadeviceevaluator.AliHAHardware.getInstance()
            com.ali.alihadeviceevaluator.AliHAHardware$DisplayInfo r1 = r1.getDisplayInfo()
            float r1 = r1.mDensity
            r4.mDesty = r1
            com.ali.alihadeviceevaluator.AliHAHardware r1 = com.ali.alihadeviceevaluator.AliHAHardware.getInstance()
            com.ali.alihadeviceevaluator.AliHAHardware$DisplayInfo r1 = r1.getDisplayInfo()
            int r1 = r1.mWidthPixels
            r4.mWidth = r1
            com.ali.alihadeviceevaluator.AliHAHardware r1 = com.ali.alihadeviceevaluator.AliHAHardware.getInstance()
            com.ali.alihadeviceevaluator.AliHAHardware$DisplayInfo r1 = r1.getDisplayInfo()
            int r1 = r1.mHeightPixels
            r4.mHeight = r1
            java.io.File r5 = r5.getFilesDir()
            if (r5 == 0) goto L_0x004f
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r5 = r5.getAbsolutePath()
            r1.append(r5)
            java.lang.String r5 = "/deviceInfo"
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r4.mFileGPUinfo = r5
            goto L_0x0053
        L_0x004f:
            java.lang.String r5 = ""
            r4.mFileGPUinfo = r5
        L_0x0053:
            java.io.File r5 = new java.io.File
            java.lang.String r1 = r4.mFileGPUinfo
            r5.<init>(r1)
            boolean r1 = r5.exists()
            if (r1 == 0) goto L_0x01a5
            r1 = 1
            r4.mLocalFileExist = r1
            r1 = 0
            java.io.FileReader r2 = new java.io.FileReader     // Catch:{ Exception -> 0x0184 }
            r2.<init>(r5)     // Catch:{ Exception -> 0x0184 }
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0184 }
            r5.<init>(r2)     // Catch:{ Exception -> 0x0184 }
            java.lang.String r1 = r5.readLine()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mCpuBrand = r1     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r1 = r5.readLine()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mCpuName = r1     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r1 = r5.readLine()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            if (r1 == 0) goto L_0x0086
            int r1 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mCpuCount = r1     // Catch:{ Exception -> 0x017e, all -> 0x017c }
        L_0x0086:
            java.lang.String r1 = r5.readLine()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            if (r1 == 0) goto L_0x0092
            float r1 = java.lang.Float.parseFloat(r1)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mCpuMaxFreq = r1     // Catch:{ Exception -> 0x017e, all -> 0x017c }
        L_0x0092:
            java.lang.String r1 = r5.readLine()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            if (r1 == 0) goto L_0x00a9
            float r1 = java.lang.Float.parseFloat(r1)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mCpuMinFreq = r1     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            float r1 = r4.mCpuMinFreq     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r2 = 0
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 > 0) goto L_0x00a9
            float r1 = r4.mCpuMaxFreq     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mCpuMinFreq = r1     // Catch:{ Exception -> 0x017e, all -> 0x017c }
        L_0x00a9:
            java.lang.String r1 = r5.readLine()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            float[] r2 = r4.mCpuFreqArray     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            if (r2 != 0) goto L_0x00b9
            int r2 = r4.getCpuCore()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            float[] r2 = new float[r2]     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mCpuFreqArray = r2     // Catch:{ Exception -> 0x017e, all -> 0x017c }
        L_0x00b9:
            if (r1 == 0) goto L_0x00da
            java.lang.String r2 = ","
            java.lang.String[] r1 = r1.split(r2)     // Catch:{ Exception -> 0x00d6, all -> 0x017c }
            if (r1 == 0) goto L_0x00da
            int r2 = r1.length     // Catch:{ Exception -> 0x00d6, all -> 0x017c }
            if (r2 <= 0) goto L_0x00da
        L_0x00c6:
            int r2 = r1.length     // Catch:{ Exception -> 0x00d6, all -> 0x017c }
            if (r0 >= r2) goto L_0x00da
            float[] r2 = r4.mCpuFreqArray     // Catch:{ Exception -> 0x00d6, all -> 0x017c }
            r3 = r1[r0]     // Catch:{ Exception -> 0x00d6, all -> 0x017c }
            float r3 = java.lang.Float.parseFloat(r3)     // Catch:{ Exception -> 0x00d6, all -> 0x017c }
            r2[r0] = r3     // Catch:{ Exception -> 0x00d6, all -> 0x017c }
            int r0 = r0 + 1
            goto L_0x00c6
        L_0x00d6:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
        L_0x00da:
            java.lang.String r0 = r5.readLine()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mGpuName = r0     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r0 = r5.readLine()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mGpuBrand = r0     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r0 = r5.readLine()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            if (r0 == 0) goto L_0x00f2
            long r0 = java.lang.Long.parseLong(r0)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r4.mGpuFreq = r0     // Catch:{ Exception -> 0x017e, all -> 0x017c }
        L_0x00f2:
            java.lang.String r0 = r5.readLine()     // Catch:{ Throwable -> 0x0105 }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0105 }
            if (r1 != 0) goto L_0x00ff
            r4.mCpuArch = r0     // Catch:{ Throwable -> 0x0105 }
            goto L_0x0105
        L_0x00ff:
            java.lang.String r0 = r4.getCpuArch()     // Catch:{ Throwable -> 0x0105 }
            r4.mCpuArch = r0     // Catch:{ Throwable -> 0x0105 }
        L_0x0105:
            java.lang.String r0 = "DeviceEvaluator"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.<init>()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = "load local file succ: cpuBrand="
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = r4.mCpuBrand     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = ", cpuName="
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = r4.mCpuName     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = ",cpuCount="
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            int r2 = r4.mCpuCount     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = ",maxFreq="
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            float r2 = r4.mCpuMaxFreq     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = ",minFreq="
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            float r2 = r4.mCpuMinFreq     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = ",freqCount="
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            float[] r2 = r4.mCpuFreqArray     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            int r2 = r2.length     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = ",GpuName"
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = r4.mGpuName     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = ",GpuBrand="
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = r4.mGpuBrand     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = ",GpuFreq="
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            long r2 = r4.mGpuFreq     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = ",CpuArch="
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r2 = r4.mCpuArch     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r1.append(r2)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            android.util.Log.d(r0, r1)     // Catch:{ Exception -> 0x017e, all -> 0x017c }
            r5.close()     // Catch:{ IOException -> 0x0195 }
            goto L_0x01a5
        L_0x017c:
            r0 = move-exception
            goto L_0x019a
        L_0x017e:
            r0 = move-exception
            r1 = r5
            goto L_0x0185
        L_0x0181:
            r0 = move-exception
            r5 = r1
            goto L_0x019a
        L_0x0184:
            r0 = move-exception
        L_0x0185:
            java.lang.String r5 = "DeviceEvaluator"
            java.lang.String r2 = "load local file failed!!"
            android.util.Log.e(r5, r2)     // Catch:{ all -> 0x0181 }
            r0.printStackTrace()     // Catch:{ all -> 0x0181 }
            if (r1 == 0) goto L_0x01a5
            r1.close()     // Catch:{ IOException -> 0x0195 }
            goto L_0x01a5
        L_0x0195:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x01a5
        L_0x019a:
            if (r5 == 0) goto L_0x01a4
            r5.close()     // Catch:{ IOException -> 0x01a0 }
            goto L_0x01a4
        L_0x01a0:
            r5 = move-exception
            r5.printStackTrace()
        L_0x01a4:
            throw r0
        L_0x01a5:
            r4.getCpuCore()
            r4.getCpuInfo()
            r4.getMaxCpuFreq()
            r4.getCpuArch()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.old.HardWareInfo.<init>(android.content.Context):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d5 A[SYNTHETIC, Splitter:B:36:0x00d5] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00e3 A[SYNTHETIC, Splitter:B:41:0x00e3] */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveCpuAndGpuInfo() {
        /*
            r6 = this;
            boolean r0 = r6.mLocalFileExist
            if (r0 != 0) goto L_0x00ef
            java.lang.String r0 = r6.mGpuName
            if (r0 == 0) goto L_0x00ef
            boolean r0 = com.ali.alihadeviceevaluator.util.ProcessUtils.isInMainProcess()
            if (r0 == 0) goto L_0x00ef
            r0 = 1
            r6.mLocalFileExist = r0
            java.io.File r1 = new java.io.File
            java.lang.String r2 = r6.mFileGPUinfo
            r1.<init>(r2)
            boolean r2 = r1.exists()
            if (r2 == 0) goto L_0x0021
            r1.delete()
        L_0x0021:
            r2 = 0
            r3 = 0
            java.io.BufferedWriter r4 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x00cd }
            java.io.FileWriter r5 = new java.io.FileWriter     // Catch:{ Exception -> 0x00cd }
            r5.<init>(r1)     // Catch:{ Exception -> 0x00cd }
            r4.<init>(r5)     // Catch:{ Exception -> 0x00cd }
            java.lang.String r1 = r6.mCpuBrand     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r1)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.String r1 = r6.mCpuName     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r1)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            int r1 = r6.mCpuCount     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r1)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            float r1 = r6.mCpuMaxFreq     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r1)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            float r1 = r6.mCpuMinFreq     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r1)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r2 = 50
            r1.<init>(r2)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            float[] r2 = r6.mCpuFreqArray     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            if (r2 == 0) goto L_0x008c
            float[] r2 = r6.mCpuFreqArray     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            int r2 = r2.length     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            if (r2 <= 0) goto L_0x008c
            r2 = 0
        L_0x0072:
            float[] r5 = r6.mCpuFreqArray     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            int r5 = r5.length     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            if (r2 >= r5) goto L_0x008c
            float[] r5 = r6.mCpuFreqArray     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r5 = r5[r2]     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r1.append(r5)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            float[] r5 = r6.mCpuFreqArray     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            int r5 = r5.length     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            int r5 = r5 - r0
            if (r2 >= r5) goto L_0x0089
            r5 = 44
            r1.append(r5)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
        L_0x0089:
            int r2 = r2 + 1
            goto L_0x0072
        L_0x008c:
            java.lang.String r0 = r1.toString()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r0)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.String r0 = r6.mGpuName     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r0)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.String r0 = r6.mGpuBrand     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r0)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            long r0 = r6.mGpuFreq     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r0)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.String r0 = r6.mCpuArch     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.write(r0)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.newLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r4.flush()     // Catch:{ IOException -> 0x00dc }
            r4.close()     // Catch:{ IOException -> 0x00dc }
            goto L_0x00ef
        L_0x00c5:
            r0 = move-exception
            goto L_0x00e1
        L_0x00c7:
            r0 = move-exception
            r2 = r4
            goto L_0x00ce
        L_0x00ca:
            r0 = move-exception
            r4 = r2
            goto L_0x00e1
        L_0x00cd:
            r0 = move-exception
        L_0x00ce:
            r0.printStackTrace()     // Catch:{ all -> 0x00ca }
            r6.mLocalFileExist = r3     // Catch:{ all -> 0x00ca }
            if (r2 == 0) goto L_0x00ef
            r2.flush()     // Catch:{ IOException -> 0x00dc }
            r2.close()     // Catch:{ IOException -> 0x00dc }
            goto L_0x00ef
        L_0x00dc:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00ef
        L_0x00e1:
            if (r4 == 0) goto L_0x00ee
            r4.flush()     // Catch:{ IOException -> 0x00ea }
            r4.close()     // Catch:{ IOException -> 0x00ea }
            goto L_0x00ee
        L_0x00ea:
            r1 = move-exception
            r1.printStackTrace()
        L_0x00ee:
            throw r0
        L_0x00ef:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.old.HardWareInfo.saveCpuAndGpuInfo():void");
    }

    public void getGpuInfo(Context context) {
        if (!this.mLocalFileExist) {
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

    public String getCpuArch() {
        int i;
        if (!TextUtils.isEmpty(this.mCpuArch)) {
            return this.mCpuArch;
        }
        String str = "UnKnown";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String readLine = bufferedReader.readLine();
            while (true) {
                if (readLine == null) {
                    break;
                } else if (readLine.contains("AArch") || readLine.contains("ARM") || readLine.contains("Intel(R)") || readLine.contains("model name")) {
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
                    } else {
                        continue;
                    }
                } else {
                    readLine = bufferedReader.readLine();
                }
            }
            bufferedReader.close();
        } catch (Exception unused) {
            str = "UnKnown";
        }
        this.mCpuArch = str;
        return str;
    }

    public float getMaxCpuFreq() {
        if (this.mCpuMaxFreq > 0.0f && this.mCpuFreqArray != null) {
            return this.mCpuMaxFreq;
        }
        if (this.mCpuFreqArray == null) {
            this.mCpuFreqArray = new float[getCpuCore()];
        }
        int i = 0;
        while (i < getCpuCore()) {
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
        if (this.mLocalFileExist) {
            this.mLocalFileExist = false;
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
        if (TextUtils.isEmpty(upperCase)) {
            return;
        }
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
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.old.HardWareInfo.getGpuFreq():long");
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

    public int getScore() {
        if (this.oldScoreMaker.mScore == 0) {
            this.oldScoreMaker.evaluateDeviceScore(this);
        }
        return this.oldScoreMaker.mScore;
    }

    public int getMemScore() {
        return this.oldScoreMaker.mMemScore;
    }

    public int getEglScore() {
        return this.oldScoreMaker.mEglScore;
    }

    public int getCpuScore() {
        return this.oldScoreMaker.mCpuScore;
    }

    public int getGpuScore() {
        return this.oldScoreMaker.mGpuScore;
    }

    class OnlineGLSurfaceView extends GLSurfaceView {
        OnlineRenderer mRenderer;

        public OnlineGLSurfaceView(Context context) {
            super(context);
            setEGLConfigChooser(8, 8, 8, 8, 0, 0);
            HardWareInfo.this.getClass();
            this.mRenderer = new OnlineRenderer();
            setRenderer(this.mRenderer);
        }
    }

    class OnlineRenderer implements GLSurfaceView.Renderer {
        public void onDrawFrame(GL10 gl10) {
        }

        public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        }

        OnlineRenderer() {
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0023 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onSurfaceCreated(javax.microedition.khronos.opengles.GL10 r3, javax.microedition.khronos.egl.EGLConfig r4) {
            /*
                r2 = this;
                com.ali.alihadeviceevaluator.old.HardWareInfo r4 = com.ali.alihadeviceevaluator.old.HardWareInfo.this     // Catch:{ Throwable -> 0x002e }
                r0 = 7937(0x1f01, float:1.1122E-41)
                java.lang.String r0 = r3.glGetString(r0)     // Catch:{ Throwable -> 0x002e }
                r4.mGpuName = r0     // Catch:{ Throwable -> 0x002e }
                com.ali.alihadeviceevaluator.old.HardWareInfo r4 = com.ali.alihadeviceevaluator.old.HardWareInfo.this     // Catch:{ Throwable -> 0x002e }
                r0 = 7936(0x1f00, float:1.1121E-41)
                java.lang.String r3 = r3.glGetString(r0)     // Catch:{ Throwable -> 0x002e }
                r4.mGpuBrand = r3     // Catch:{ Throwable -> 0x002e }
                com.ali.alihadeviceevaluator.old.HardWareInfo r3 = com.ali.alihadeviceevaluator.old.HardWareInfo.this     // Catch:{ Throwable -> 0x002e }
                r3.destroy()     // Catch:{ Throwable -> 0x002e }
                com.ali.alihadeviceevaluator.old.HardWareInfo r3 = com.ali.alihadeviceevaluator.old.HardWareInfo.this     // Catch:{ Throwable -> 0x0023 }
                com.ali.alihadeviceevaluator.old.HardWareInfo r4 = com.ali.alihadeviceevaluator.old.HardWareInfo.this     // Catch:{ Throwable -> 0x0023 }
                long r0 = r4.getGpuFreq()     // Catch:{ Throwable -> 0x0023 }
                r3.mGpuFreq = r0     // Catch:{ Throwable -> 0x0023 }
            L_0x0023:
                com.ali.alihadeviceevaluator.old.HardWareInfo r3 = com.ali.alihadeviceevaluator.old.HardWareInfo.this     // Catch:{ Throwable -> 0x002e }
                com.ali.alihadeviceevaluator.old.OldScoreMaker r3 = r3.oldScoreMaker     // Catch:{ Throwable -> 0x002e }
                com.ali.alihadeviceevaluator.old.HardWareInfo r4 = com.ali.alihadeviceevaluator.old.HardWareInfo.this     // Catch:{ Throwable -> 0x002e }
                r3.evaluateDeviceScore(r4)     // Catch:{ Throwable -> 0x002e }
            L_0x002e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.old.HardWareInfo.OnlineRenderer.onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig):void");
        }
    }
}
