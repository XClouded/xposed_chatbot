package android.taobao.windvane.packageapp.zipapp;

import android.support.v4.media.session.PlaybackStateCompat;
import android.taobao.windvane.packageapp.monitor.GlobalInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.util.TaoLog;

import com.taobao.weex.el.parse.Operators;

import java.util.Map;

public class ZipAppUpdateManager {
    private static String TAG = "PackageApp-ZipAppUpdateManager";

    public static void startUpdateApps(ZipGlobalConfig zipGlobalConfig) {
        if (zipGlobalConfig == null) {
            try {
                TaoLog.w(TAG, "startUpdateApps: GlobalConfig file parse error or invalid!");
            } catch (Exception e) {
                TaoLog.e(TAG, "startUpdateApps: exception ." + e.getMessage());
                e.printStackTrace();
                GlobalInfoMonitor.error(ZipAppResultCode.ERR_APPS_CONFIG_PARSE, e.getMessage());
            }
        } else {
            ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
            if ("-1".equals(zipGlobalConfig.i) && locGlobalConfig != null && locGlobalConfig.isAvailableData() && zipGlobalConfig != null && zipGlobalConfig.isAvailableData()) {
                for (Map.Entry<String, ZipAppInfo> value : locGlobalConfig.getAppsTable().entrySet()) {
                    ZipAppInfo zipAppInfo = (ZipAppInfo) value.getValue();
                    ZipAppInfo appInfo = zipGlobalConfig.getAppInfo(zipAppInfo.name);
                    if (appInfo == null || appInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) {
                        if (zipAppInfo.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                            zipAppInfo.isOptional = true;
                        } else {
                            zipAppInfo.f |= PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
                        }
                    }
                }
                ConfigManager.saveGlobalConfigToloc(locGlobalConfig);
            }
            locGlobalConfig.online_v = zipGlobalConfig.v;
            updateAppsInfo(zipGlobalConfig, locGlobalConfig);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00d4 A[SYNTHETIC, Splitter:B:42:0x00d4] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00df A[SYNTHETIC, Splitter:B:48:0x00df] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean preloadZipInstall(java.lang.String r9) {
        /*
            long r0 = java.lang.System.currentTimeMillis()
            android.taobao.windvane.packageapp.ZipAppFileManager r2 = android.taobao.windvane.packageapp.ZipAppFileManager.getInstance()
            r3 = 0
            r4 = 0
            r2.clearTmpDir(r3, r4)
            android.taobao.windvane.packageapp.ZipAppFileManager r2 = android.taobao.windvane.packageapp.ZipAppFileManager.getInstance()     // Catch:{ Exception -> 0x00ce }
            java.io.InputStream r9 = r2.getPreloadInputStream(r9)     // Catch:{ Exception -> 0x00ce }
            if (r9 != 0) goto L_0x0030
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r1 = "获取预装包失败或者不存在预装包"
            android.taobao.windvane.util.TaoLog.w(r0, r1)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            if (r9 == 0) goto L_0x0028
            r9.close()     // Catch:{ IOException -> 0x0024 }
            goto L_0x0028
        L_0x0024:
            r9 = move-exception
            r9.printStackTrace()
        L_0x0028:
            return r4
        L_0x0029:
            r0 = move-exception
            goto L_0x00dd
        L_0x002c:
            r0 = move-exception
            r3 = r9
            goto L_0x00cf
        L_0x0030:
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r2 = android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r3.<init>()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            android.taobao.windvane.packageapp.ZipAppFileManager r5 = android.taobao.windvane.packageapp.ZipAppFileManager.getInstance()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r5 = r5.getRootPathTmp()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r3.append(r5)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r5 = java.io.File.separator     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r3.append(r5)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r5 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.APP_PREFIXES_NAME     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r3.append(r5)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            if (r2 != 0) goto L_0x0059
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r2 = new android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r2.<init>()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
        L_0x0059:
            android.taobao.windvane.packageapp.ZipAppFileManager r5 = android.taobao.windvane.packageapp.ZipAppFileManager.getInstance()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r5 = r5.getRootPathTmp()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            boolean r5 = android.taobao.windvane.file.FileManager.unzip((java.io.InputStream) r9, (java.lang.String) r5)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            if (r5 != 0) goto L_0x0079
            java.lang.String r0 = "ZipAppFileManager"
            java.lang.String r1 = "预装解压缩失败"
            android.taobao.windvane.util.TaoLog.w(r0, r1)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            if (r9 == 0) goto L_0x0078
            r9.close()     // Catch:{ IOException -> 0x0074 }
            goto L_0x0078
        L_0x0074:
            r9 = move-exception
            r9.printStackTrace()
        L_0x0078:
            return r4
        L_0x0079:
            android.taobao.windvane.packageapp.ZipAppFileManager r5 = android.taobao.windvane.packageapp.ZipAppFileManager.getInstance()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r6 = 1
            java.lang.String r5 = r5.readGlobalConfig(r6)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r5 = android.taobao.windvane.packageapp.zipapp.utils.ConfigDataUtils.parseGlobalConfig(r5)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            updateFromPreLoadApps(r2, r5)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r7 = "0"
            r5.v = r7     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            android.taobao.windvane.packageapp.ZipAppFileManager r5 = android.taobao.windvane.packageapp.ZipAppFileManager.getInstance()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r3 = r5.readFile(r3)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            android.taobao.windvane.packageapp.WVPackageAppPrefixesConfig r5 = android.taobao.windvane.packageapp.WVPackageAppPrefixesConfig.getInstance()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r5.parseConfig(r3)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            android.taobao.windvane.packageapp.zipapp.ConfigManager.saveGlobalConfigToloc(r2)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r2 = "ZipAppFileManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r3.<init>()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r5 = "Preloaded install: "
            r3.append(r5)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r5 = 0
            long r7 = r7 - r0
            r3.append(r7)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r0 = " ms"
            r3.append(r0)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String r0 = r3.toString()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            android.taobao.windvane.util.TaoLog.w(r2, r0)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            if (r9 == 0) goto L_0x00ca
            r9.close()     // Catch:{ IOException -> 0x00c6 }
            goto L_0x00ca
        L_0x00c6:
            r9 = move-exception
            r9.printStackTrace()
        L_0x00ca:
            return r6
        L_0x00cb:
            r0 = move-exception
            r9 = r3
            goto L_0x00dd
        L_0x00ce:
            r0 = move-exception
        L_0x00cf:
            r0.printStackTrace()     // Catch:{ all -> 0x00cb }
            if (r3 == 0) goto L_0x00dc
            r3.close()     // Catch:{ IOException -> 0x00d8 }
            goto L_0x00dc
        L_0x00d8:
            r9 = move-exception
            r9.printStackTrace()
        L_0x00dc:
            return r4
        L_0x00dd:
            if (r9 == 0) goto L_0x00e7
            r9.close()     // Catch:{ IOException -> 0x00e3 }
            goto L_0x00e7
        L_0x00e3:
            r9 = move-exception
            r9.printStackTrace()
        L_0x00e7:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.zipapp.ZipAppUpdateManager.preloadZipInstall(java.lang.String):boolean");
    }

    private static void updateAppsInfo(ZipGlobalConfig zipGlobalConfig, ZipGlobalConfig zipGlobalConfig2) {
        if (zipGlobalConfig == null || !zipGlobalConfig.isAvailableData()) {
            TaoLog.w(TAG, "updateAppsInfo: onlineConfig is null or appsMap is null");
            return;
        }
        String str = TAG;
        TaoLog.i(str, "updateAppsInfo: 开始更新所有应用信息[count:" + zipGlobalConfig.getAppsTable().size() + Operators.ARRAY_END_STR);
        for (Map.Entry<String, ZipAppInfo> value : zipGlobalConfig.getAppsTable().entrySet()) {
            ZipAppInfo zipAppInfo = (ZipAppInfo) value.getValue();
            zipGlobalConfig2.putAppInfo2Table(zipAppInfo.name, zipAppInfo);
        }
        zipGlobalConfig2.v = zipGlobalConfig.v;
        ConfigManager.saveGlobalConfigToloc(zipGlobalConfig2);
    }

    private static void updateFromPreLoadApps(ZipGlobalConfig zipGlobalConfig, ZipGlobalConfig zipGlobalConfig2) {
        if (zipGlobalConfig2 == null || !zipGlobalConfig2.isAvailableData()) {
            TaoLog.w(TAG, "startUpdateApps:[updateApps]  param error .");
        } else {
            updateFromPreLoad(zipGlobalConfig, zipGlobalConfig2);
        }
    }

    private static void updateFromPreLoad(ZipGlobalConfig zipGlobalConfig, ZipGlobalConfig zipGlobalConfig2) {
        ZipAppInfo zipAppInfo;
        for (Map.Entry next : zipGlobalConfig2.getAppsTable().entrySet()) {
            String str = (String) next.getKey();
            ZipAppInfo zipAppInfo2 = (ZipAppInfo) next.getValue();
            if (str != null && ((zipAppInfo = zipGlobalConfig.getAppsTable().get(str)) == null || zipAppInfo.installedSeq < zipAppInfo2.s)) {
                zipAppInfo2.status = ZipAppConstants.ZIP_NEWEST;
                zipAppInfo2.installedSeq = zipAppInfo2.s;
                zipAppInfo2.installedVersion = zipAppInfo2.v;
                int copyUpdateDel = ZipAppManager.getInstance().copyUpdateDel(zipAppInfo2, true, false);
                if (copyUpdateDel == ZipAppResultCode.SECCUSS) {
                    ConfigManager.updateGlobalConfig(zipAppInfo2, (String) null, false);
                } else {
                    String str2 = TAG;
                    TaoLog.e(str2, Operators.ARRAY_START_STR + zipAppInfo2.name + zipAppInfo2.v + "]:预装出错; errorCode: " + copyUpdateDel);
                }
            }
        }
    }
}
