package android.taobao.windvane.packageapp.zipapp;

import android.os.Build;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.monitor.AppInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.data.AppResConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;

import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.weex.el.parse.Operators;
import com.uc.crashsdk.export.CrashApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class ZipAppManager {
    private static String TAG = "ZCache_2";
    private static ZipAppManager zipAppManager;
    private boolean isInit = false;
    private ZipAppFileManager zipAppFile;

    public static synchronized ZipAppManager getInstance() {
        ZipAppManager zipAppManager2;
        synchronized (ZipAppManager.class) {
            if (zipAppManager == null) {
                zipAppManager = new ZipAppManager();
            }
            zipAppManager2 = zipAppManager;
        }
        return zipAppManager2;
    }

    public synchronized boolean init() {
        if (this.isInit) {
            return true;
        }
        TaoLog.d(TAG, "init: zipapp init start .");
        this.zipAppFile = ZipAppFileManager.getInstance();
        boolean createZipAppInitDir = this.zipAppFile.createZipAppInitDir();
        String str = TAG;
        TaoLog.i(str, "init: zipapp init finished .isSuccess=" + createZipAppInitDir);
        this.isInit = createZipAppInitDir;
        return this.isInit;
    }

    public int install(ZipAppInfo zipAppInfo, String str, boolean z, boolean z2) {
        String str2 = TAG;
        TaoLog.i(str2, "zcache speed = [" + z2 + Operators.ARRAY_END_STR);
        if (z2) {
            return installNew(zipAppInfo, str, z);
        }
        return install(zipAppInfo, str, z);
    }

    private int install(ZipAppInfo zipAppInfo, String str, boolean z) {
        if (zipAppInfo == null || TextUtils.isEmpty(str)) {
            TaoLog.w(TAG, "install: check fail :appInfo is null or destFile is null");
            AppInfoMonitor.error(zipAppInfo, ZipAppResultCode.ERR_PARAM, "ErrorMsg = ERR_PARAM");
            return ZipAppResultCode.ERR_PARAM;
        }
        String unZipToTmp = this.zipAppFile.unZipToTmp(zipAppInfo, str);
        if (TaoLog.getLogStatus()) {
            String str2 = TAG;
            TaoLog.i(str2, "install: unZipToTmp :[" + zipAppInfo.name + ":" + unZipToTmp + Operators.ARRAY_END_STR);
        }
        if (zipAppInfo.isPreViewApp) {
            WVEventService.getInstance().onEvent(6005, unZipToTmp);
        }
        if ("success".equals(unZipToTmp)) {
            return copyUpdateDel(zipAppInfo, z, false);
        }
        int i = ZipAppResultCode.ERR_FILE_UNZIP;
        AppInfoMonitor.error(zipAppInfo, i, "ErrorMsg = ERR_FILE_UNZIP : " + unZipToTmp);
        return ZipAppResultCode.ERR_FILE_UNZIP;
    }

    private int installNew(ZipAppInfo zipAppInfo, String str, boolean z) {
        if (zipAppInfo == null || TextUtils.isEmpty(str)) {
            TaoLog.w(TAG, "install: check fail :appInfo is null or destFile is null");
            AppInfoMonitor.error(zipAppInfo, ZipAppResultCode.ERR_PARAM, "ErrorMsg = ERR_PARAM");
            return ZipAppResultCode.ERR_PARAM;
        }
        File file = new File(str);
        try {
            CrashApi.getInstance().addHeaderInfo("wv_zip_url", str);
            CrashApi instance = CrashApi.getInstance();
            instance.addHeaderInfo("device_identifier", Build.BRAND + DinamicConstant.DINAMIC_PREFIX_AT + Build.VERSION.RELEASE);
            CrashApi.getInstance().addHeaderInfo("config_version", WVCommonConfig.commonConfig.v);
            CrashApi instance2 = CrashApi.getInstance();
            instance2.addHeaderInfo("zip_degrade_config", WVCommonConfig.commonConfig.zipDegradeMode + " / " + WVCommonConfig.commonConfig.zipDegradeList);
        } catch (Throwable unused) {
        }
        if (zipAppInfo.isPreViewApp) {
            WVEventService.getInstance().onEvent(6005, "");
        }
        if (unZipByZIS(zipAppInfo, str, ZipAppFileManager.getInstance().getNewRootDir(zipAppInfo), z) == ZipAppResultCode.SECCUSS) {
            String str2 = TAG;
            TaoLog.i(str2, "unZip :[" + zipAppInfo.name + ":" + "success" + Operators.ARRAY_END_STR);
            if (file.exists()) {
                file.delete();
                String str3 = TAG;
                TaoLog.d(str3, "Delete temp file:" + str);
            }
            return copyUpdateDel(zipAppInfo, z, true);
        }
        int i = ZipAppResultCode.ERR_FILE_UNZIP;
        AppInfoMonitor.error(zipAppInfo, i, "ErrorMsg = ERR_FILE_UNZIP : " + "");
        return ZipAppResultCode.ERR_FILE_UNZIP;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:113:?, code lost:
        android.taobao.windvane.util.TaoLog.e(TAG, "empty md5, appName=[" + r1.name + com.taobao.weex.el.parse.Operators.ARRAY_END_STR);
        r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_CHECK_CONFIG_APPRES;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x0297, code lost:
        if (r5 == null) goto L_0x029f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x029d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x029f, code lost:
        r12.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x02a2, code lost:
        if (r6 == null) goto L_0x02a7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x02a4, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x02a7, code lost:
        if (r7 == null) goto L_0x02c7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x02a9, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x02ad, code lost:
        android.taobao.windvane.util.TaoLog.e(TAG, "close Stream Exception:" + r0.getMessage());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x02c7, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x0303, code lost:
        if (r4 != r10.mResfileMap.size()) goto L_0x0307;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x0305, code lost:
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x0307, code lost:
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x030a, code lost:
        if (r1.isPreViewApp == false) goto L_0x0330;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x030c, code lost:
        r8 = 0;
        r1.isPreViewApp = false;
        android.taobao.windvane.service.WVEventService.getInstance().onEvent(6006, java.lang.Boolean.valueOf(r0), java.lang.Long.valueOf(r1.s), r1.name);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x0330, code lost:
        r8 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0331, code lost:
        if (r0 != false) goto L_0x0391;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x0333, code lost:
        android.taobao.windvane.util.TaoLog.e(TAG, "error res file, require=[" + r10.mResfileMap.size() + "], real=[" + r4 + com.taobao.weex.el.parse.Operators.ARRAY_END_STR);
        r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_CHECK_CONFIG_APPRES;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x035e, code lost:
        if (r5 == null) goto L_0x0366;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0364, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x0366, code lost:
        if (r13 == null) goto L_0x036b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x0368, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x036b, code lost:
        if (r6 == null) goto L_0x0370;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x036d, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:153:0x0370, code lost:
        if (r7 == null) goto L_0x0390;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x0372, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x0376, code lost:
        android.taobao.windvane.util.TaoLog.e(TAG, "close Stream Exception:" + r0.getMessage());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:156:0x0390, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:?, code lost:
        android.taobao.windvane.util.TaoLog.i(TAG, "unzip all file success");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x0398, code lost:
        if (r1 == null) goto L_0x03cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:161:0x03a0, code lost:
        if (android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE != r17.getAppType()) goto L_0x03cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x03a2, code lost:
        if (r20 != false) goto L_0x03c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x03a4, code lost:
        r0 = android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig().getZcacheResConfig().get(r1.name);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x03b4, code lost:
        if (r0 == null) goto L_0x03c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x03ba, code lost:
        if (r8 >= r0.size()) goto L_0x03c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x03bc, code lost:
        r11.add(r0.get(r8));
        r8 = r8 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x03c6, code lost:
        android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig().addZcacheResConfig(r1.name, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x03cf, code lost:
        r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.SECCUSS;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x03d1, code lost:
        if (r5 == null) goto L_0x03d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x03d7, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x03d9, code lost:
        if (r13 == null) goto L_0x03de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x03db, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:0x03de, code lost:
        if (r6 == null) goto L_0x03e3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:178:0x03e0, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x03e3, code lost:
        if (r7 == null) goto L_0x0403;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:0x03e5, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:0x03e9, code lost:
        android.taobao.windvane.util.TaoLog.e(TAG, "close Stream Exception:" + r0.getMessage());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x0403, code lost:
        return r1;
     */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x042c A[SYNTHETIC, Splitter:B:193:0x042c] */
    /* JADX WARNING: Removed duplicated region for block: B:198:0x0434 A[Catch:{ IOException -> 0x0430 }] */
    /* JADX WARNING: Removed duplicated region for block: B:200:0x0439 A[Catch:{ IOException -> 0x0430 }] */
    /* JADX WARNING: Removed duplicated region for block: B:202:0x043e A[Catch:{ IOException -> 0x0430 }] */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x0461 A[SYNTHETIC, Splitter:B:207:0x0461] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:212:0x0469 A[Catch:{ IOException -> 0x0465 }] */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x046e A[Catch:{ IOException -> 0x0465 }] */
    /* JADX WARNING: Removed duplicated region for block: B:216:0x0473 A[Catch:{ IOException -> 0x0465 }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int unZipByZIS(android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r17, java.lang.String r18, java.lang.String r19, boolean r20) {
        /*
            r16 = this;
            r1 = r17
            r0 = r18
            r2 = r19
            if (r0 == 0) goto L_0x0492
            if (r2 != 0) goto L_0x000c
            goto L_0x0492
        L_0x000c:
            java.lang.String r4 = "/"
            boolean r4 = r2.endsWith(r4)
            if (r4 != 0) goto L_0x0025
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r2)
            java.lang.String r2 = "/"
            r4.append(r2)
            java.lang.String r2 = r4.toString()
        L_0x0025:
            r4 = 0
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0040 }
            r5.<init>(r0)     // Catch:{ IOException -> 0x0040 }
            java.util.zip.ZipInputStream r6 = new java.util.zip.ZipInputStream     // Catch:{ IOException -> 0x003d }
            r6.<init>(r5)     // Catch:{ IOException -> 0x003d }
            java.util.zip.ZipInputStream r7 = new java.util.zip.ZipInputStream     // Catch:{ IOException -> 0x003b }
            java.io.FileInputStream r8 = new java.io.FileInputStream     // Catch:{ IOException -> 0x003b }
            r8.<init>(r0)     // Catch:{ IOException -> 0x003b }
            r7.<init>(r8)     // Catch:{ IOException -> 0x003b }
            goto L_0x0047
        L_0x003b:
            r0 = move-exception
            goto L_0x0043
        L_0x003d:
            r0 = move-exception
            r6 = r4
            goto L_0x0043
        L_0x0040:
            r0 = move-exception
            r5 = r4
            r6 = r5
        L_0x0043:
            r0.printStackTrace()
            r7 = r4
        L_0x0047:
            if (r5 != 0) goto L_0x004c
            int r0 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_FILE_READ
            return r0
        L_0x004c:
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            r8 = 1024(0x400, float:1.435E-42)
            byte[] r9 = new byte[r8]     // Catch:{ Exception -> 0x040f }
            java.lang.String r10 = ""
        L_0x0057:
            java.util.zip.ZipEntry r11 = r6.getNextEntry()     // Catch:{ Exception -> 0x040f }
            r12 = 0
            if (r11 == 0) goto L_0x009d
            java.lang.String r13 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.APP_RES_NAME     // Catch:{ Exception -> 0x040f }
            java.lang.String r11 = r11.getName()     // Catch:{ Exception -> 0x040f }
            boolean r11 = r13.equals(r11)     // Catch:{ Exception -> 0x040f }
            if (r11 == 0) goto L_0x0057
        L_0x006a:
            int r10 = r6.read(r9, r12, r8)     // Catch:{ Exception -> 0x040f }
            if (r10 <= 0) goto L_0x0074
            r0.write(r9, r12, r10)     // Catch:{ Exception -> 0x040f }
            goto L_0x006a
        L_0x0074:
            java.lang.String r10 = r0.toString()     // Catch:{ Exception -> 0x040f }
            java.lang.String r11 = TAG     // Catch:{ Exception -> 0x040f }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x040f }
            r13.<init>()     // Catch:{ Exception -> 0x040f }
            java.lang.String r14 = "["
            r13.append(r14)     // Catch:{ Exception -> 0x040f }
            java.lang.String r14 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.APP_RES_NAME     // Catch:{ Exception -> 0x040f }
            r13.append(r14)     // Catch:{ Exception -> 0x040f }
            java.lang.String r14 = "] is "
            r13.append(r14)     // Catch:{ Exception -> 0x040f }
            r13.append(r10)     // Catch:{ Exception -> 0x040f }
            java.lang.String r14 = ""
            r13.append(r14)     // Catch:{ Exception -> 0x040f }
            java.lang.String r13 = r13.toString()     // Catch:{ Exception -> 0x040f }
            android.taobao.windvane.util.TaoLog.i(r11, r13)     // Catch:{ Exception -> 0x040f }
        L_0x009d:
            r0.close()     // Catch:{ Exception -> 0x040f }
            boolean r0 = android.text.TextUtils.isEmpty(r10)     // Catch:{ Exception -> 0x040f }
            if (r0 == 0) goto L_0x00d6
            int r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_DOWN_CONFIG_APPRES     // Catch:{ Exception -> 0x040f }
            if (r5 == 0) goto L_0x00b0
            r5.close()     // Catch:{ IOException -> 0x00ae }
            goto L_0x00b0
        L_0x00ae:
            r0 = move-exception
            goto L_0x00bb
        L_0x00b0:
            if (r6 == 0) goto L_0x00b5
            r6.close()     // Catch:{ IOException -> 0x00ae }
        L_0x00b5:
            if (r7 == 0) goto L_0x00d5
            r7.close()     // Catch:{ IOException -> 0x00ae }
            goto L_0x00d5
        L_0x00bb:
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "close Stream Exception:"
            r3.append(r4)
            java.lang.String r0 = r0.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r0)
        L_0x00d5:
            return r1
        L_0x00d6:
            r0 = 1
            android.taobao.windvane.packageapp.zipapp.data.AppResConfig r10 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.parseAppResConfig(r10, r0)     // Catch:{ Exception -> 0x040f }
            if (r10 != 0) goto L_0x0114
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x040f }
            java.lang.String r1 = "validZipPackage fail. AppResInfo valid fail."
            android.taobao.windvane.util.TaoLog.e(r0, r1)     // Catch:{ Exception -> 0x040f }
            int r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_APPRES_CONFIG_PARSE     // Catch:{ Exception -> 0x040f }
            if (r5 == 0) goto L_0x00ee
            r5.close()     // Catch:{ IOException -> 0x00ec }
            goto L_0x00ee
        L_0x00ec:
            r0 = move-exception
            goto L_0x00f9
        L_0x00ee:
            if (r6 == 0) goto L_0x00f3
            r6.close()     // Catch:{ IOException -> 0x00ec }
        L_0x00f3:
            if (r7 == 0) goto L_0x0113
            r7.close()     // Catch:{ IOException -> 0x00ec }
            goto L_0x0113
        L_0x00f9:
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "close Stream Exception:"
            r3.append(r4)
            java.lang.String r0 = r0.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r0)
        L_0x0113:
            return r1
        L_0x0114:
            java.util.ArrayList r11 = new java.util.ArrayList     // Catch:{ Exception -> 0x040f }
            r11.<init>()     // Catch:{ Exception -> 0x040f }
            r13 = r4
            r4 = 0
        L_0x011b:
            java.util.zip.ZipEntry r14 = r7.getNextEntry()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r14 == 0) goto L_0x02fd
            java.lang.String r15 = r14.getName()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r0 = "../"
            boolean r0 = r15.contains(r0)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r0 == 0) goto L_0x012f
            r0 = 1
            goto L_0x011b
        L_0x012f:
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r0.<init>()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r15 = r14.getName()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r20 != 0) goto L_0x0144
            java.lang.String r8 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.APP_RES_NAME     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            boolean r8 = r8.equals(r15)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r8 == 0) goto L_0x0144
            java.lang.String r15 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.APP_RES_INC_NAME     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
        L_0x0144:
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r12.<init>()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r12.append(r2)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r12.append(r15)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r8.<init>(r12)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            boolean r12 = r14.isDirectory()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r12 == 0) goto L_0x0162
            r8.mkdirs()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            goto L_0x0173
        L_0x0162:
            java.io.File r12 = r8.getParentFile()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            boolean r12 = r12.exists()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r12 != 0) goto L_0x0173
            java.io.File r12 = r8.getParentFile()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r12.mkdirs()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
        L_0x0173:
            java.io.FileOutputStream r12 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r12.<init>(r8)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r8 = 0
            r13 = 1024(0x400, float:1.435E-42)
        L_0x017b:
            int r15 = r7.read(r9, r8, r13)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            if (r15 <= 0) goto L_0x0186
            r0.write(r9, r8, r15)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r8 = 0
            goto L_0x017b
        L_0x0186:
            java.util.Hashtable<java.lang.String, android.taobao.windvane.packageapp.zipapp.data.AppResConfig$FileInfo> r8 = r10.mResfileMap     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r15 = r14.getName()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            boolean r8 = r8.containsKey(r15)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            if (r8 == 0) goto L_0x02c8
            java.util.Hashtable<java.lang.String, android.taobao.windvane.packageapp.zipapp.data.AppResConfig$FileInfo> r8 = r10.mResfileMap     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r15 = r14.getName()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.Object r8 = r8.get(r15)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            android.taobao.windvane.packageapp.zipapp.data.AppResConfig$FileInfo r8 = (android.taobao.windvane.packageapp.zipapp.data.AppResConfig.FileInfo) r8     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r8 = r8.v     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r15 = android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r13 = r17.getAppType()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            if (r15 == r13) goto L_0x01af
            java.lang.String r13 = r14.getName()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r11.add(r13)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
        L_0x01af:
            boolean r13 = android.text.TextUtils.isEmpty(r8)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            if (r13 != 0) goto L_0x0278
            byte[] r13 = r0.toByteArray()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r13 = android.taobao.windvane.util.DigestUtils.md5ToHex((byte[]) r13)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            boolean r13 = r8.equals(r13)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            if (r13 == 0) goto L_0x01e6
            int r4 = r4 + 1
            java.lang.String r8 = TAG     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r13.<init>()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r15 = "check res["
            r13.append(r15)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r15 = r14.getName()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r13.append(r15)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r15 = "] success"
            r13.append(r15)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r13 = r13.toString()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            android.taobao.windvane.util.TaoLog.i(r8, r13)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            goto L_0x02c8
        L_0x01e6:
            java.lang.String r2 = TAG     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r3.<init>()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r4 = "error check res["
            r3.append(r4)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r4 = r14.getName()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r3.append(r4)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r4 = "];\n md5required hash:["
            r3.append(r4)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r3.append(r8)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r4 = "];\n real hash:["
            r3.append(r4)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            byte[] r0 = r0.toByteArray()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r0 = android.taobao.windvane.util.DigestUtils.md5ToHex((byte[]) r0)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r3.append(r0)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r0 = "]"
            r3.append(r0)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r0 = r3.toString()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            android.taobao.windvane.util.TaoLog.e(r2, r0)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            int r0 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_CHECK_ZIP     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r2.<init>()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r3 = r1.v     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r4 = r1.installedVersion     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            boolean r3 = r3.equals(r4)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r2.append(r3)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r3 = ":"
            r2.append(r3)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            long r3 = r1.s     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r2.append(r3)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r3 = "ErrorMsg = ERR_CHECK_ZIP"
            r2.append(r3)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            android.taobao.windvane.packageapp.monitor.AppInfoMonitor.error(r1, r0, r2)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            int r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_CHECK_ZIP     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            if (r5 == 0) goto L_0x024f
            r5.close()     // Catch:{ IOException -> 0x024d }
            goto L_0x024f
        L_0x024d:
            r0 = move-exception
            goto L_0x025d
        L_0x024f:
            r12.close()     // Catch:{ IOException -> 0x024d }
            if (r6 == 0) goto L_0x0257
            r6.close()     // Catch:{ IOException -> 0x024d }
        L_0x0257:
            if (r7 == 0) goto L_0x0277
            r7.close()     // Catch:{ IOException -> 0x024d }
            goto L_0x0277
        L_0x025d:
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "close Stream Exception:"
            r3.append(r4)
            java.lang.String r0 = r0.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r0)
        L_0x0277:
            return r1
        L_0x0278:
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r2.<init>()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r3 = "empty md5, appName=["
            r2.append(r3)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r1 = r1.name     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r2.append(r1)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r1 = "]"
            r2.append(r1)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r1 = r2.toString()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            android.taobao.windvane.util.TaoLog.e(r0, r1)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            int r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_CHECK_CONFIG_APPRES     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            if (r5 == 0) goto L_0x029f
            r5.close()     // Catch:{ IOException -> 0x029d }
            goto L_0x029f
        L_0x029d:
            r0 = move-exception
            goto L_0x02ad
        L_0x029f:
            r12.close()     // Catch:{ IOException -> 0x029d }
            if (r6 == 0) goto L_0x02a7
            r6.close()     // Catch:{ IOException -> 0x029d }
        L_0x02a7:
            if (r7 == 0) goto L_0x02c7
            r7.close()     // Catch:{ IOException -> 0x029d }
            goto L_0x02c7
        L_0x02ad:
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "close Stream Exception:"
            r3.append(r4)
            java.lang.String r0 = r0.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r0)
        L_0x02c7:
            return r1
        L_0x02c8:
            byte[] r0 = r0.toByteArray()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r12.write(r0)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r8.<init>()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r13 = "unzip file["
            r8.append(r13)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r13 = r14.getName()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r8.append(r13)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r13 = "] success"
            r8.append(r13)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            android.taobao.windvane.util.TaoLog.i(r0, r8)     // Catch:{ Exception -> 0x02f9, all -> 0x02f5 }
            r13 = r12
            r0 = 1
            r8 = 1024(0x400, float:1.435E-42)
            r12 = 0
            goto L_0x011b
        L_0x02f5:
            r0 = move-exception
            r1 = r0
            goto L_0x045f
        L_0x02f9:
            r0 = move-exception
            r4 = r12
            goto L_0x0410
        L_0x02fd:
            java.util.Hashtable<java.lang.String, android.taobao.windvane.packageapp.zipapp.data.AppResConfig$FileInfo> r0 = r10.mResfileMap     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            int r0 = r0.size()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r4 != r0) goto L_0x0307
            r0 = 1
            goto L_0x0308
        L_0x0307:
            r0 = 0
        L_0x0308:
            boolean r2 = r1.isPreViewApp     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r2 == 0) goto L_0x0330
            r8 = 0
            r1.isPreViewApp = r8     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            android.taobao.windvane.service.WVEventService r2 = android.taobao.windvane.service.WVEventService.getInstance()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r9 = 6006(0x1776, float:8.416E-42)
            r12 = 3
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.Boolean r14 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r12[r8] = r14     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            long r14 = r1.s     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.Long r14 = java.lang.Long.valueOf(r14)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r15 = 1
            r12[r15] = r14     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r14 = 2
            java.lang.String r15 = r1.name     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r12[r14] = r15     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r2.onEvent(r9, r12)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            goto L_0x0331
        L_0x0330:
            r8 = 0
        L_0x0331:
            if (r0 != 0) goto L_0x0391
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r1.<init>()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r2 = "error res file, require=["
            r1.append(r2)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.util.Hashtable<java.lang.String, android.taobao.windvane.packageapp.zipapp.data.AppResConfig$FileInfo> r2 = r10.mResfileMap     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            int r2 = r2.size()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r1.append(r2)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r2 = "], real=["
            r1.append(r2)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r1.append(r4)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r2 = "]"
            r1.append(r2)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            android.taobao.windvane.util.TaoLog.e(r0, r1)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            int r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_CHECK_CONFIG_APPRES     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r5 == 0) goto L_0x0366
            r5.close()     // Catch:{ IOException -> 0x0364 }
            goto L_0x0366
        L_0x0364:
            r0 = move-exception
            goto L_0x0376
        L_0x0366:
            if (r13 == 0) goto L_0x036b
            r13.close()     // Catch:{ IOException -> 0x0364 }
        L_0x036b:
            if (r6 == 0) goto L_0x0370
            r6.close()     // Catch:{ IOException -> 0x0364 }
        L_0x0370:
            if (r7 == 0) goto L_0x0390
            r7.close()     // Catch:{ IOException -> 0x0364 }
            goto L_0x0390
        L_0x0376:
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "close Stream Exception:"
            r3.append(r4)
            java.lang.String r0 = r0.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r0)
        L_0x0390:
            return r1
        L_0x0391:
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r2 = "unzip all file success"
            android.taobao.windvane.util.TaoLog.i(r0, r2)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r1 == 0) goto L_0x03cf
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r0 = android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r2 = r17.getAppType()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r0 != r2) goto L_0x03cf
            if (r20 != 0) goto L_0x03c6
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r0 = android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.util.Hashtable r0 = r0.getZcacheResConfig()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r2 = r1.name     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.Object r0 = r0.get(r2)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
        L_0x03b4:
            if (r0 == 0) goto L_0x03c6
            int r2 = r0.size()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r8 >= r2) goto L_0x03c6
            java.lang.Object r2 = r0.get(r8)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r11.add(r2)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            int r8 = r8 + 1
            goto L_0x03b4
        L_0x03c6:
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r0 = android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig()     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            java.lang.String r1 = r1.name     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            r0.addZcacheResConfig(r1, r11)     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
        L_0x03cf:
            int r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.SECCUSS     // Catch:{ Exception -> 0x0408, all -> 0x0404 }
            if (r5 == 0) goto L_0x03d9
            r5.close()     // Catch:{ IOException -> 0x03d7 }
            goto L_0x03d9
        L_0x03d7:
            r0 = move-exception
            goto L_0x03e9
        L_0x03d9:
            if (r13 == 0) goto L_0x03de
            r13.close()     // Catch:{ IOException -> 0x03d7 }
        L_0x03de:
            if (r6 == 0) goto L_0x03e3
            r6.close()     // Catch:{ IOException -> 0x03d7 }
        L_0x03e3:
            if (r7 == 0) goto L_0x0403
            r7.close()     // Catch:{ IOException -> 0x03d7 }
            goto L_0x0403
        L_0x03e9:
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "close Stream Exception:"
            r3.append(r4)
            java.lang.String r0 = r0.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r0)
        L_0x0403:
            return r1
        L_0x0404:
            r0 = move-exception
            r1 = r0
            r12 = r13
            goto L_0x045f
        L_0x0408:
            r0 = move-exception
            r4 = r13
            goto L_0x0410
        L_0x040b:
            r0 = move-exception
            r1 = r0
            r12 = r4
            goto L_0x045f
        L_0x040f:
            r0 = move-exception
        L_0x0410:
            java.lang.String r1 = TAG     // Catch:{ all -> 0x040b }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x040b }
            r2.<init>()     // Catch:{ all -> 0x040b }
            java.lang.String r3 = "unzip: IOException:"
            r2.append(r3)     // Catch:{ all -> 0x040b }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x040b }
            r2.append(r0)     // Catch:{ all -> 0x040b }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x040b }
            android.taobao.windvane.util.TaoLog.e(r1, r0)     // Catch:{ all -> 0x040b }
            if (r5 == 0) goto L_0x0432
            r5.close()     // Catch:{ IOException -> 0x0430 }
            goto L_0x0432
        L_0x0430:
            r0 = move-exception
            goto L_0x0442
        L_0x0432:
            if (r4 == 0) goto L_0x0437
            r4.close()     // Catch:{ IOException -> 0x0430 }
        L_0x0437:
            if (r6 == 0) goto L_0x043c
            r6.close()     // Catch:{ IOException -> 0x0430 }
        L_0x043c:
            if (r7 == 0) goto L_0x045c
            r7.close()     // Catch:{ IOException -> 0x0430 }
            goto L_0x045c
        L_0x0442:
            java.lang.String r1 = TAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "close Stream Exception:"
            r2.append(r3)
            java.lang.String r0 = r0.getMessage()
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            android.taobao.windvane.util.TaoLog.e(r1, r0)
        L_0x045c:
            int r0 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_FILE_UNZIP
            return r0
        L_0x045f:
            if (r5 == 0) goto L_0x0467
            r5.close()     // Catch:{ IOException -> 0x0465 }
            goto L_0x0467
        L_0x0465:
            r0 = move-exception
            goto L_0x0477
        L_0x0467:
            if (r12 == 0) goto L_0x046c
            r12.close()     // Catch:{ IOException -> 0x0465 }
        L_0x046c:
            if (r6 == 0) goto L_0x0471
            r6.close()     // Catch:{ IOException -> 0x0465 }
        L_0x0471:
            if (r7 == 0) goto L_0x0491
            r7.close()     // Catch:{ IOException -> 0x0465 }
            goto L_0x0491
        L_0x0477:
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "close Stream Exception:"
            r3.append(r4)
            java.lang.String r0 = r0.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r0)
        L_0x0491:
            throw r1
        L_0x0492:
            int r0 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_FILE_READ
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.zipapp.ZipAppManager.unZipByZIS(android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo, java.lang.String, java.lang.String, boolean):int");
    }

    public int copyUpdateDel(ZipAppInfo zipAppInfo, boolean z, boolean z2) {
        String str = z ? "install" : "upgrade";
        try {
            String str2 = TAG;
            TaoLog.i(str2, str + " app:[" + zipAppInfo.name + Operators.ARRAY_END_STR);
            if (!z2) {
                boolean validInstallZipPackage = validInstallZipPackage(zipAppInfo, z, true);
                if (zipAppInfo.isPreViewApp) {
                    zipAppInfo.isPreViewApp = false;
                    WVEventService.getInstance().onEvent(6006, Boolean.valueOf(validInstallZipPackage), Long.valueOf(zipAppInfo.s), zipAppInfo.name);
                }
                if (TaoLog.getLogStatus()) {
                    String str3 = TAG;
                    TaoLog.d(str3, str + ": validZipPackage :[" + zipAppInfo.name + ":" + validInstallZipPackage + Operators.ARRAY_END_STR);
                }
                if (!validInstallZipPackage) {
                    int i = ZipAppResultCode.ERR_CHECK_ZIP;
                    AppInfoMonitor.error(zipAppInfo, i, zipAppInfo.v.equals(zipAppInfo.installedVersion) + ":" + zipAppInfo.s + "ErrorMsg = ERR_CHECK_ZIP");
                    return ZipAppResultCode.ERR_CHECK_ZIP;
                }
            }
            if (!parseUrlMappingInfo(zipAppInfo, !z2, z)) {
                AppInfoMonitor.error(zipAppInfo, ZipAppResultCode.ERR_FILE_READ, "ErrorMsg = ERR_FILE_READ_MAPPINGINFO");
                return ZipAppResultCode.ERR_FILE_READ;
            }
            if (!z2) {
                dealAppResFileName(zipAppInfo, z);
            }
            if (!z2) {
                boolean copyZipApp = this.zipAppFile.copyZipApp(zipAppInfo);
                String str4 = TAG;
                TaoLog.i(str4, str + ": copyZipApp :[" + zipAppInfo.name + ":" + copyZipApp + Operators.ARRAY_END_STR);
                if (!copyZipApp) {
                    AppInfoMonitor.error(zipAppInfo, ZipAppResultCode.ERR_FILE_COPY, "ErrorMsg = ERR_FILE_COPY");
                    return ZipAppResultCode.ERR_FILE_COPY;
                }
            }
            zipAppInfo.status = ZipAppConstants.ZIP_NEWEST;
            boolean updateGlobalConfig = ConfigManager.updateGlobalConfig(zipAppInfo, (String) null, false);
            String str5 = TAG;
            TaoLog.i(str5, str + ": UpdateGlobalConfig :[" + zipAppInfo.name + ":" + updateGlobalConfig + Operators.ARRAY_END_STR);
            if (!updateGlobalConfig) {
                AppInfoMonitor.error(zipAppInfo, ZipAppResultCode.ERR_FILE_SAVE, "ErrorMsg = ERR_FILE_SAVE");
                return ZipAppResultCode.ERR_FILE_SAVE;
            }
            boolean deleteHisZipApp = this.zipAppFile.deleteHisZipApp(zipAppInfo);
            String str6 = TAG;
            TaoLog.i(str6, str + ": deleteHisZipApp :" + deleteHisZipApp);
            return ZipAppResultCode.SECCUSS;
        } catch (Exception e) {
            int i2 = ZipAppResultCode.ERR_SYSTEM;
            AppInfoMonitor.error(zipAppInfo, i2, "ErrorMsg = ERR_SYSTEM : " + e.getMessage());
            String str7 = TAG;
            TaoLog.e(str7, "copyUpdateDel Exception:" + e.getMessage());
            return ZipAppResultCode.ERR_SYSTEM;
        }
    }

    private void dealAppResFileName(ZipAppInfo zipAppInfo, boolean z) {
        if (!z) {
            boolean renameTo = new File(ZipAppFileManager.getInstance().getZipResAbsolutePath(zipAppInfo, ZipAppConstants.APP_RES_NAME, true)).renameTo(new File(ZipAppFileManager.getInstance().getZipResAbsolutePath(zipAppInfo, ZipAppConstants.APP_RES_INC_NAME, true)));
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(zipAppInfo.name);
            sb.append(" : appResFile changeName : ");
            sb.append(renameTo ? "sussess!" : "failed!");
            TaoLog.d(str, sb.toString());
        }
    }

    public int unInstall(ZipAppInfo zipAppInfo) {
        try {
            if (!this.zipAppFile.deleteZipApp(zipAppInfo, false)) {
                if (TaoLog.getLogStatus()) {
                    String str = TAG;
                    TaoLog.w(str, "unInstall: deleteZipApp :fail [" + zipAppInfo.name + Operators.ARRAY_END_STR);
                }
                return ZipAppResultCode.ERR_FILE_DEL;
            }
            boolean updateGlobalConfig = ConfigManager.updateGlobalConfig(zipAppInfo, (String) null, true);
            if (!updateGlobalConfig) {
                if (TaoLog.getLogStatus()) {
                    String str2 = TAG;
                    TaoLog.w(str2, "unInstall: updateGlobalConfig :fail [" + zipAppInfo.name + updateGlobalConfig + Operators.ARRAY_END_STR);
                }
                return ZipAppResultCode.ERR_FILE_SAVE;
            }
            ConfigManager.getLocGlobalConfig().removeZcacheRes(zipAppInfo.name);
            return ZipAppResultCode.SECCUSS;
        } catch (Exception e) {
            String str3 = TAG;
            TaoLog.e(str3, "unInstall Exception:" + e.getMessage());
            return ZipAppResultCode.ERR_SYSTEM;
        }
    }

    public static boolean parseUrlMappingInfo(ZipAppInfo zipAppInfo, boolean z, boolean z2) {
        String str;
        int i;
        if (zipAppInfo == null) {
            return false;
        }
        if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
            TaoLog.d(TAG, "zcache not need parse appinfo.wvc");
            return true;
        }
        if (z) {
            str = ZipAppFileManager.getInstance().readZipAppRes(zipAppInfo, ZipAppConstants.APP_INFO_NAME, true);
        } else {
            str = ZipAppFileManager.getInstance().readFile(ZipAppFileManager.getInstance().getNewZipResAbsolutePath(zipAppInfo, ZipAppConstants.APP_INFO_NAME, false));
        }
        if (TextUtils.isEmpty(str)) {
            if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP) {
                zipAppInfo.mappingUrl = "//h5." + GlobalConfig.env.getValue() + ".taobao.com/app/" + zipAppInfo.name + "/";
            }
            if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE2) {
                return false;
            }
            TaoLog.i(TAG, "parseUrlMappingInfo fail. appinfo.wvc is empty.");
            return true;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.optString("appMonitor");
            String optString = jSONObject.optString("mappingUrl");
            if (!TextUtils.isEmpty(optString)) {
                zipAppInfo.mappingUrl = optString;
                TaoLog.i(TAG, zipAppInfo.name + " : mappingUrl : " + optString);
            } else {
                TaoLog.w(TAG, zipAppInfo.name + " mappingUrl is empty!");
            }
            if (zipAppInfo.folders == null) {
                zipAppInfo.folders = new ArrayList<>();
                TaoLog.e(TAG + "-Folders", "create empty folders: " + zipAppInfo.name);
            }
            if (z2) {
                TaoLog.e(TAG + "-Folders", "Override update, folders should be clear");
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("removedFolders");
            if (optJSONArray != null) {
                for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                    String obj = optJSONArray.get(i2).toString();
                    if (zipAppInfo.folders.contains(obj)) {
                        zipAppInfo.folders.remove(obj);
                        TaoLog.d(TAG + "-Folders", zipAppInfo.name + " : remvoe folder : " + obj);
                    }
                }
            }
            JSONArray optJSONArray2 = jSONObject.optJSONArray("addFolders");
            if (optJSONArray2 != null) {
                for (int i3 = 0; i3 < optJSONArray2.length(); i3++) {
                    String obj2 = optJSONArray2.get(i3).toString();
                    if (!zipAppInfo.folders.contains(obj2)) {
                        zipAppInfo.folders.add(obj2);
                    }
                    TaoLog.d(TAG + "-Folders", zipAppInfo.name + " : add folder : " + obj2);
                }
            }
            JSONArray optJSONArray3 = jSONObject.optJSONArray("removedRes");
            if (optJSONArray3 != null && optString != null) {
                int i4 = 0;
                while (true) {
                    if (i4 < optJSONArray3.length()) {
                        String obj3 = optJSONArray3.get(i4).toString();
                        if (str != null) {
                            String zipResAbsolutePath = ZipAppFileManager.getInstance().getZipResAbsolutePath(zipAppInfo, obj3, false);
                            if (TextUtils.isEmpty(zipResAbsolutePath)) {
                                break;
                            }
                            File file = new File(zipResAbsolutePath);
                            if (file.exists()) {
                                boolean deleteFile = FileAccesser.deleteFile(file);
                                String str2 = TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append(zipAppInfo.name);
                                sb.append(" : delete res:");
                                sb.append(zipResAbsolutePath);
                                sb.append(" : ");
                                sb.append(deleteFile ? "sussess!" : "failed!");
                                TaoLog.i(str2, sb.toString());
                            }
                        }
                        i4++;
                    }
                }
                return true;
            }
            try {
                File file2 = new File(ZipAppFileManager.getInstance().getZipRootDir(zipAppInfo, false));
                if (file2.exists() && file2.isDirectory()) {
                    String[] list = file2.list(new FilenameFilter() {
                        public boolean accept(File file, String str) {
                            return file.isDirectory();
                        }
                    });
                    if (list == null) {
                        i = 0;
                    } else {
                        i = list.length;
                    }
                    TaoLog.e(TAG + "-Folders", zipAppInfo.name + " local existed " + i + " dirs.");
                    if (!(list == null || i == zipAppInfo.folders.size())) {
                        TaoLog.e(TAG + "-Folders", "ZCache: folders index does not match the local files, indexed [" + zipAppInfo.folders.size() + "], local existed [" + i + Operators.ARRAY_END_STR);
                        zipAppInfo.localFolders.clear();
                        zipAppInfo.localFolders.addAll(Arrays.asList(list));
                        if (WVMonitorService.getWvMonitorInterface() != null) {
                            WVMonitorService.getPackageMonitorInterface().commitFail("WrongFolderIndex", -1, zipAppInfo.name + " / " + zipAppInfo.v + " [" + zipAppInfo.folders.size() + "," + i + Operators.ARRAY_END_STR, zipAppInfo.getZipUrl());
                        }
                    }
                }
            } catch (Throwable th) {
                TaoLog.w(TAG + "-Folders", "Check folders", th, new Object[0]);
            }
        } catch (Exception unused) {
            if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE2) {
                return false;
            }
        }
        return true;
    }

    public static boolean validInstallZipPackage(ZipAppInfo zipAppInfo, boolean z, boolean z2) {
        try {
            String readZipAppRes = ZipAppFileManager.getInstance().readZipAppRes(zipAppInfo, ZipAppConstants.APP_RES_NAME, z2);
            if (TextUtils.isEmpty(readZipAppRes)) {
                TaoLog.w(TAG, "validZipPackage fail. appres is empty.");
                return false;
            }
            AppResConfig parseAppResConfig = ZipAppUtils.parseAppResConfig(readZipAppRes, true);
            if (parseAppResConfig == null) {
                TaoLog.w(TAG, "validZipPackage fail. AppResInfo valid fail.");
                return false;
            }
            ArrayList arrayList = new ArrayList();
            for (Map.Entry next : parseAppResConfig.mResfileMap.entrySet()) {
                String str = ((AppResConfig.FileInfo) next.getValue()).v;
                String str2 = (String) next.getKey();
                if (!(zipAppInfo == null || ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == zipAppInfo.getAppType())) {
                    arrayList.add(str2);
                }
                if (!TextUtils.isEmpty(str)) {
                    byte[] readZipAppResByte = ZipAppFileManager.getInstance().readZipAppResByte(zipAppInfo, str2, z2);
                    if (readZipAppResByte != null) {
                        if (readZipAppResByte.length >= 1) {
                            if (str.equals(DigestUtils.md5ToHex(readZipAppResByte))) {
                            }
                        }
                    }
                    if (z) {
                        return false;
                    }
                }
                if (TaoLog.getLogStatus()) {
                    String str3 = TAG;
                    TaoLog.d(str3, str2 + "[invalid]" + str);
                }
                return false;
            }
            if (zipAppInfo != null && ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE == zipAppInfo.getAppType()) {
                if (!z) {
                    ArrayList arrayList2 = ConfigManager.getLocGlobalConfig().getZcacheResConfig().get(zipAppInfo.name);
                    int i = 0;
                    while (arrayList2 != null && i < arrayList2.size()) {
                        arrayList.add(arrayList2.get(i));
                        i++;
                    }
                }
                ConfigManager.updateZcacheurlMap(zipAppInfo.name, arrayList);
            }
            return true;
        } catch (Exception e) {
            String str4 = TAG;
            TaoLog.e(str4, "validZipPackage fail. parse config fail: " + e.getMessage());
            return false;
        }
    }

    public int validRunningZipPackage(String str) {
        try {
            String readFile = ZipAppFileManager.getInstance().readFile(str);
            if (TextUtils.isEmpty(readFile)) {
                if (TaoLog.getLogStatus()) {
                    String str2 = TAG;
                    TaoLog.d(str2, "validZipPackage fail. appres is empty.patch=" + str);
                }
                return ZipAppResultCode.ERR_NOTFOUND_APPRES;
            }
            AppResConfig parseAppResConfig = ZipAppUtils.parseAppResConfig(readFile, true);
            if (parseAppResConfig == null) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "validZipPackage fail. AppResInfo valid fail.");
                }
                return ZipAppResultCode.ERR_VERIFY_APPRES;
            }
            for (Map.Entry<String, AppResConfig.FileInfo> value : parseAppResConfig.mResfileMap.entrySet()) {
                AppResConfig.FileInfo fileInfo = (AppResConfig.FileInfo) value.getValue();
                WVZipSecurityManager.getInstance().put(WVUrlUtil.force2HttpUrl(fileInfo.url), fileInfo.v, fileInfo.headers);
            }
            return ZipAppResultCode.SECCUSS;
        } catch (Exception unused) {
            return ZipAppResultCode.ERR_VERIFY_APPRES;
        }
    }

    public static HashSet<String> getUrlsByAppName(String str) {
        AppResConfig parseAppResConfig;
        HashSet<String> hashSet = new HashSet<>();
        if (TextUtils.isEmpty(str)) {
            return hashSet;
        }
        try {
            ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(str);
            if (appInfo == null) {
                return hashSet;
            }
            String zipResAbsolutePath = ZipAppFileManager.getInstance().getZipResAbsolutePath(appInfo, ZipAppConstants.APP_RES_NAME, false);
            if (TextUtils.isEmpty(zipResAbsolutePath)) {
                return hashSet;
            }
            String readFile = ZipAppFileManager.getInstance().readFile(zipResAbsolutePath);
            if (TextUtils.isEmpty(readFile) || (parseAppResConfig = ZipAppUtils.parseAppResConfig(readFile, true)) == null) {
                return hashSet;
            }
            for (Map.Entry<String, AppResConfig.FileInfo> value : parseAppResConfig.mResfileMap.entrySet()) {
                String str2 = ((AppResConfig.FileInfo) value.getValue()).url;
                if (!TextUtils.isEmpty(str2)) {
                    if (!str2.endsWith(".wvc")) {
                        hashSet.add(str2.replace("http://", "https://"));
                    }
                }
            }
            return hashSet;
        } catch (Exception unused) {
        }
    }
}
