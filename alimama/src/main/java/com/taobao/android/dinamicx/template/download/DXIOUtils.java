package com.taobao.android.dinamicx.template.download;

import android.content.res.AssetManager;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.log.DXLog;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DXIOUtils {
    private static final int BUFFER_SIZE = 8192;
    private static final int MAX_BUFFER_SIZE = 2147483639;

    public static byte[] readAllBytes(String str) throws IOException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.startsWith(DXTemplateNamePathUtil.ASSET_DIR)) {
            return readFromAssetsPath(str);
        }
        return readFromFilePath(str);
    }

    public static JSONObject readPresetTemplateInfo(String str) {
        byte[] bArr;
        try {
            bArr = readAllBytes(DXTemplateNamePathUtil.ASSET_DIR + str + DXTemplateNamePathUtil.ASSET_PRESET_TEMPLATE_INFOLIST);
        } catch (Throwable unused) {
            if (DinamicXEngine.isDebug()) {
                DXLog.w("DXTemplateInfoManager", str + "未使用内置模板索引文件");
            }
            bArr = null;
        }
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        try {
            return JSON.parseObject(new String(bArr));
        } catch (Throwable th) {
            if (DinamicXEngine.isDebug()) {
                DXLog.e("DXTemplateInfoManager", th, str + "内置模板索引文件格式错误");
            }
            return null;
        }
    }

    private static byte[] readFromAssetsPath(String str) throws IOException {
        return read(DinamicXEngine.getApplicationContext().getAssets().open(str));
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0035 A[SYNTHETIC, Splitter:B:28:0x0035] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x003c A[SYNTHETIC, Splitter:B:32:0x003c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static byte[] read(java.io.InputStream r5) throws java.io.IOException {
        /*
            r0 = 0
            if (r5 != 0) goto L_0x0009
            if (r5 == 0) goto L_0x0008
            r5.close()     // Catch:{ IOException -> 0x0008 }
        L_0x0008:
            return r0
        L_0x0009:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x002f }
            r2 = 2048(0x800, float:2.87E-42)
            r1.<init>(r2)     // Catch:{ all -> 0x002f }
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r0]     // Catch:{ all -> 0x002d }
        L_0x0014:
            int r2 = r5.read(r0)     // Catch:{ all -> 0x002d }
            r3 = -1
            if (r2 == r3) goto L_0x0020
            r3 = 0
            r1.write(r0, r3, r2)     // Catch:{ all -> 0x002d }
            goto L_0x0014
        L_0x0020:
            byte[] r0 = r1.toByteArray()     // Catch:{ all -> 0x002d }
            r1.close()     // Catch:{ IOException -> 0x0027 }
        L_0x0027:
            if (r5 == 0) goto L_0x002c
            r5.close()     // Catch:{ IOException -> 0x002c }
        L_0x002c:
            return r0
        L_0x002d:
            r0 = move-exception
            goto L_0x0033
        L_0x002f:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x0033:
            if (r1 == 0) goto L_0x003a
            r1.close()     // Catch:{ IOException -> 0x0039 }
            goto L_0x003a
        L_0x0039:
        L_0x003a:
            if (r5 == 0) goto L_0x003f
            r5.close()     // Catch:{ IOException -> 0x003f }
        L_0x003f:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.template.download.DXIOUtils.read(java.io.InputStream):byte[]");
    }

    private static byte[] readFromFilePath(String str) throws IOException {
        int read;
        File file = new File(str);
        long length = file.length();
        if (length <= 2147483639) {
            FileInputStream fileInputStream = new FileInputStream(file);
            int i = (int) length;
            byte[] bArr = new byte[i];
            int i2 = 0;
            while (true) {
                int read2 = fileInputStream.read(bArr, i2, i - i2);
                if (read2 > 0) {
                    i2 += read2;
                } else if (read2 < 0 || (read = fileInputStream.read()) < 0) {
                    fileInputStream.close();
                } else {
                    if (i <= MAX_BUFFER_SIZE - i) {
                        i = Math.max(i << 1, 8192);
                    } else if (i != MAX_BUFFER_SIZE) {
                        i = MAX_BUFFER_SIZE;
                    } else {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                    bArr = Arrays.copyOf(bArr, i);
                    bArr[i2] = (byte) read;
                    i2++;
                }
            }
            fileInputStream.close();
            if (i == i2) {
                return bArr;
            }
            return Arrays.copyOf(bArr, i2);
        }
        throw new OutOfMemoryError("Required array size too large");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x003b A[Catch:{ all -> 0x0032 }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0061 A[SYNTHETIC, Splitter:B:25:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0068 A[SYNTHETIC, Splitter:B:29:0x0068] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean writeTemplateToFile(java.lang.String r5, byte[] r6) {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r5)
            java.io.File r5 = new java.io.File
            java.lang.String r1 = r0.getParent()
            r5.<init>(r1)
            boolean r1 = r5.exists()
            if (r1 != 0) goto L_0x0017
            r5.mkdir()
        L_0x0017:
            r5 = 0
            java.io.BufferedOutputStream r1 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0034 }
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0034 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x0034 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0034 }
            r1.write(r6)     // Catch:{ Exception -> 0x002e, all -> 0x002a }
            r5 = 1
            r1.close()     // Catch:{ IOException -> 0x0029 }
        L_0x0029:
            return r5
        L_0x002a:
            r5 = move-exception
            r6 = r5
            r5 = r1
            goto L_0x0066
        L_0x002e:
            r5 = move-exception
            r6 = r5
            r5 = r1
            goto L_0x0035
        L_0x0032:
            r6 = move-exception
            goto L_0x0066
        L_0x0034:
            r6 = move-exception
        L_0x0035:
            boolean r1 = r0.exists()     // Catch:{ all -> 0x0032 }
            if (r1 == 0) goto L_0x003e
            r0.delete()     // Catch:{ all -> 0x0032 }
        L_0x003e:
            com.taobao.android.dinamicx.DXError r0 = new com.taobao.android.dinamicx.DXError     // Catch:{ all -> 0x0032 }
            java.lang.String r1 = "DinamicX_File"
            r0.<init>(r1)     // Catch:{ all -> 0x0032 }
            com.taobao.android.dinamicx.DXError$DXErrorInfo r1 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ all -> 0x0032 }
            java.lang.String r2 = "Template"
            java.lang.String r3 = "Template_Write"
            r4 = 60021(0xea75, float:8.4107E-41)
            r1.<init>(r2, r3, r4)     // Catch:{ all -> 0x0032 }
            java.lang.String r6 = com.taobao.android.dinamicx.exception.DXExceptionUtil.getStackTrace(r6)     // Catch:{ all -> 0x0032 }
            r1.reason = r6     // Catch:{ all -> 0x0032 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r6 = r0.dxErrorInfoList     // Catch:{ all -> 0x0032 }
            r6.add(r1)     // Catch:{ all -> 0x0032 }
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r0)     // Catch:{ all -> 0x0032 }
            if (r5 == 0) goto L_0x0064
            r5.close()     // Catch:{ IOException -> 0x0064 }
        L_0x0064:
            r5 = 0
            return r5
        L_0x0066:
            if (r5 == 0) goto L_0x006b
            r5.close()     // Catch:{ IOException -> 0x006b }
        L_0x006b:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.template.download.DXIOUtils.writeTemplateToFile(java.lang.String, byte[]):boolean");
    }

    public static boolean unzip(DXTemplateItem dXTemplateItem, byte[] bArr, String str, IDXUnzipCallback iDXUnzipCallback, @NonNull DXError dXError) {
        boolean z;
        if (dXTemplateItem == null || bArr == null || iDXUnzipCallback == null || TextUtils.isEmpty(str)) {
            dXError.dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_DOWNLOADER, DXMonitorConstant.DX_MONITOR_DOWNLOADER_DOWNLOAD, DXError.DX_TEMPLATE_UNZIP_REQUIRED_PARAMS_MISSING_ERROR));
            return false;
        }
        HashMap hashMap = new HashMap();
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(bArr)));
            loop0:
            while (true) {
                z = false;
                while (true) {
                    ZipEntry nextEntry = zipInputStream.getNextEntry();
                    if (nextEntry == null) {
                        break loop0;
                    }
                    byte[] bArr2 = new byte[8192];
                    String name = nextEntry.getName();
                    if (!nextEntry.getName().contains("../")) {
                        if (!nextEntry.isDirectory()) {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            String str2 = str + name;
                            File file = new File(new File(str2).getParent());
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            while (true) {
                                int read = zipInputStream.read(bArr2, 0, 8192);
                                if (read == -1) {
                                    break;
                                }
                                byteArrayOutputStream.write(bArr2, 0, read);
                            }
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            if (dXTemplateItem.packageInfo == null) {
                                dXTemplateItem.packageInfo = new DXTemplatePackageInfo();
                            }
                            if (DXTemplateNamePathUtil.DX_MAIN_TEMPLATE_NAME.equalsIgnoreCase(name)) {
                                dXTemplateItem.packageInfo.mainFilePath = str2;
                            } else {
                                if (dXTemplateItem.packageInfo.subFilePathDict == null) {
                                    dXTemplateItem.packageInfo.subFilePathDict = new HashMap();
                                }
                                dXTemplateItem.packageInfo.subFilePathDict.put(name, str2);
                            }
                            hashMap.put(str2, byteArray);
                            byteArrayOutputStream.flush();
                            z = true;
                        }
                    }
                }
            }
            zipInputStream.close();
        } catch (Throwable th) {
            hashMap.clear();
            DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_DOWNLOADER, DXMonitorConstant.DX_MONITOR_DOWNLOADER_DOWNLOAD, DXError.DX_TEMPLATE_UNZIP_ERROR);
            dXErrorInfo.reason = DXExceptionUtil.getStackTrace(th);
            dXError.dxErrorInfoList.add(dXErrorInfo);
            z = false;
        }
        if (z) {
            if (dXTemplateItem.packageInfo == null || TextUtils.isEmpty(dXTemplateItem.packageInfo.mainFilePath)) {
                DXError.DXErrorInfo dXErrorInfo2 = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_DOWNLOADER, DXMonitorConstant.DX_MONITOR_DOWNLOADER_DOWNLOAD, DXError.DX_TEMPLATE_UNZIP_ERROR);
                dXErrorInfo2.reason = "模板zip中缺少main.dx";
                dXError.dxErrorInfoList.add(dXErrorInfo2);
                return false;
            }
            iDXUnzipCallback.onUnzipFinished(dXTemplateItem, hashMap);
        }
        return z;
    }

    public static String[] getAssetsFileNameList(String str) {
        AssetManager assets;
        if (TextUtils.isEmpty(str) || (assets = DinamicXEngine.getApplicationContext().getAssets()) == null) {
            return null;
        }
        try {
            return assets.list(str);
        } catch (IOException unused) {
            return null;
        }
    }
}
