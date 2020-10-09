package android.taobao.windvane.packageapp;

import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.file.FileManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.util.TaoLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ZipAppFileManager {
    private static ZipAppFileManager zipAppFileManager;
    private String TAG = "PackageApp-ZipAppFileManager";
    private ZipDegradeDecider mDecider;

    public interface ZipDegradeDecider {
        boolean needDegrade();
    }

    public static ZipAppFileManager getInstance() {
        if (zipAppFileManager == null) {
            synchronized (ZipAppFileManager.class) {
                if (zipAppFileManager == null) {
                    zipAppFileManager = new ZipAppFileManager();
                }
            }
        }
        return zipAppFileManager;
    }

    public boolean createZipAppInitDir() {
        if (GlobalConfig.context == null) {
            return false;
        }
        File createFolder = FileManager.createFolder(GlobalConfig.context, ZipAppConstants.ZIPAPP_ROOT_APPS_DIR);
        String str = this.TAG;
        TaoLog.d(str, "createDir: dir[" + createFolder.getAbsolutePath() + "]:" + createFolder.exists());
        if (!createFolder.exists()) {
            return false;
        }
        File createFolder2 = FileManager.createFolder(GlobalConfig.context, ZipAppConstants.ZIPAPP_ROOT_TMP_DIR);
        String str2 = this.TAG;
        TaoLog.d(str2, "createDir: dir[" + createFolder2.getAbsolutePath() + "]:" + createFolder2.exists());
        return createFolder2.exists();
    }

    public String readZcacheConfig(boolean z) {
        return readFile(getZcacheConfigPath(z));
    }

    public String readGlobalConfig(boolean z) {
        return readFile(getGlobalConfigPath(z));
    }

    public synchronized boolean saveGlobalConfig(byte[] bArr, boolean z) {
        return saveFile(getGlobalConfigPath(z), bArr);
    }

    public boolean saveZcacheConfig(byte[] bArr, boolean z) {
        return saveFile(getZcacheConfigPath(z), bArr);
    }

    public String readZipAppRes(ZipAppInfo zipAppInfo, String str, boolean z) {
        return readFile(getZipResAbsolutePath(zipAppInfo, str, z));
    }

    public byte[] readZipAppResByte(ZipAppInfo zipAppInfo, String str, boolean z) {
        return FileAccesser.read(getZipResAbsolutePath(zipAppInfo, str, z));
    }

    public boolean saveZipAppRes(ZipAppInfo zipAppInfo, String str, byte[] bArr, boolean z) {
        return saveFile(getZipResAbsolutePath(zipAppInfo, str, z), bArr);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x004d A[Catch:{ Exception -> 0x0067 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0077  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0079  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String unZipToTmp(android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r6, java.lang.String r7) {
        /*
            r5 = this;
            r0 = 1
            java.lang.String r1 = r5.getZipRootDir(r6, r0)
            android.taobao.windvane.file.FileAccesser.deleteFile((java.lang.String) r1)
            java.lang.String r1 = ""
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0067 }
            r3.<init>(r7)     // Catch:{ Exception -> 0x0067 }
            android.taobao.windvane.packageapp.ZipAppFileManager$ZipDegradeDecider r4 = r5.mDecider     // Catch:{ Exception -> 0x0067 }
            if (r4 == 0) goto L_0x001b
            android.taobao.windvane.packageapp.ZipAppFileManager$ZipDegradeDecider r4 = r5.mDecider     // Catch:{ Exception -> 0x0067 }
            boolean r4 = r4.needDegrade()     // Catch:{ Exception -> 0x0067 }
            goto L_0x001c
        L_0x001b:
            r4 = 0
        L_0x001c:
            if (r4 != 0) goto L_0x0038
            android.taobao.windvane.config.WVCommonConfigData r4 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x0067 }
            boolean r4 = r4.needZipDegrade     // Catch:{ Exception -> 0x0067 }
            if (r4 == 0) goto L_0x0025
            goto L_0x0038
        L_0x0025:
            r3.setReadOnly()     // Catch:{ Exception -> 0x0067 }
            java.lang.String r4 = r5.getZipRootDir(r6, r0)     // Catch:{ Exception -> 0x0067 }
            java.lang.String r4 = android.taobao.windvane.file.FileManager.unZipByFilePath(r7, r4)     // Catch:{ Exception -> 0x0067 }
            r3.setWritable(r0)     // Catch:{ Exception -> 0x0035 }
            r1 = r4
            goto L_0x0047
        L_0x0035:
            r7 = move-exception
            r1 = r4
            goto L_0x0068
        L_0x0038:
            java.lang.String r0 = r5.getZipRootDir(r6, r0)     // Catch:{ Exception -> 0x0067 }
            boolean r0 = android.taobao.windvane.file.FileManager.unzip((java.lang.String) r7, (java.lang.String) r0)     // Catch:{ Exception -> 0x0067 }
            if (r0 == 0) goto L_0x0045
            java.lang.String r0 = "success"
            goto L_0x0046
        L_0x0045:
            r0 = r1
        L_0x0046:
            r1 = r0
        L_0x0047:
            boolean r0 = r3.exists()     // Catch:{ Exception -> 0x0067 }
            if (r0 == 0) goto L_0x008b
            r3.delete()     // Catch:{ Exception -> 0x0067 }
            java.lang.String r0 = r5.TAG     // Catch:{ Exception -> 0x0067 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0067 }
            r3.<init>()     // Catch:{ Exception -> 0x0067 }
            java.lang.String r4 = "Delete temp file:"
            r3.append(r4)     // Catch:{ Exception -> 0x0067 }
            r3.append(r7)     // Catch:{ Exception -> 0x0067 }
            java.lang.String r7 = r3.toString()     // Catch:{ Exception -> 0x0067 }
            android.taobao.windvane.util.TaoLog.d(r0, r7)     // Catch:{ Exception -> 0x0067 }
            goto L_0x008b
        L_0x0067:
            r7 = move-exception
        L_0x0068:
            java.lang.String r0 = r5.TAG
            java.lang.String r3 = "unZipToTemp"
            java.lang.Object[] r2 = new java.lang.Object[r2]
            android.taobao.windvane.util.TaoLog.w(r0, r3, r7, r2)
            android.taobao.windvane.config.WVCommonConfigData r0 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            boolean r0 = r0.needZipDegrade
            if (r0 == 0) goto L_0x0079
            r0 = -1
            goto L_0x007a
        L_0x0079:
            r0 = -2
        L_0x007a:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r2 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            java.lang.String r3 = "UnzipError"
            java.lang.String r7 = r7.getMessage()
            java.lang.String r6 = r6.getZipUrl()
            r2.commitFail(r3, r0, r7, r6)
        L_0x008b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.ZipAppFileManager.unZipToTmp(android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo, java.lang.String):java.lang.String");
    }

    public boolean deleteHisZipApp(ZipAppInfo zipAppInfo) {
        return deleteDir(getFileAbsolutePath(zipAppInfo.name, false, ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == zipAppInfo.getAppType()), zipAppInfo.v);
    }

    public boolean deleteZipApp(ZipAppInfo zipAppInfo, boolean z) {
        File file = new File(getFileAbsolutePath(zipAppInfo.name, z, ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == zipAppInfo.getAppType()));
        if (file.exists()) {
            return FileAccesser.deleteFile(file);
        }
        return true;
    }

    public InputStream getPreloadInputStream(String str) {
        try {
            return GlobalConfig.context.getResources().getAssets().open(str);
        } catch (FileNotFoundException unused) {
            TaoLog.i(this.TAG, "preload package not exists");
            return null;
        } catch (Exception unused2) {
            return null;
        }
    }

    public boolean copyZipApp(ZipAppInfo zipAppInfo) {
        boolean z = true;
        String zipRootDir = getZipRootDir(zipAppInfo, true);
        String str = zipAppInfo.name + "/" + zipAppInfo.v;
        if (ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP != zipAppInfo.getAppType()) {
            z = false;
        }
        return FileManager.copyDir(zipRootDir, getFileAbsolutePath(str, false, z));
    }

    public boolean clearTmpDir(String str, boolean z) {
        return FileAccesser.deleteFile(new File(getFileAbsolutePath(str, true, true)), z);
    }

    public boolean clearAppsDir() {
        return FileAccesser.deleteFile(new File(getFileAbsolutePath((String) null, false, true)), false);
    }

    public boolean clearZCacheDir() {
        return FileAccesser.deleteFile(new File(getFileAbsolutePath((String) null, false, false)), false);
    }

    public String getRootPath() {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + ZipAppConstants.ZIPAPP_ROOT_DIR;
    }

    public String getRootPathTmp() {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + ZipAppConstants.ZIPAPP_ROOT_TMP_DIR;
    }

    public String getDownLoadPath() {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + ZipAppConstants.ZIPAPP_DOWNLOAD__DIR;
    }

    public String getRootPathApps() {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + ZipAppConstants.ZIPAPP_ROOT_APPS_DIR;
    }

    public String getGlobalConfigPath(boolean z) {
        return getFileAbsolutePath(ZipAppConstants.H5_APPS_NAME, z, true);
    }

    public String getZcacheConfigPath(boolean z) {
        return getFileAbsolutePath(ZipAppConstants.H5_ZCACHE_MAP, z, false);
    }

    public String getZipRootDir(ZipAppInfo zipAppInfo, boolean z) {
        return getFileAbsolutePath(zipAppInfo.genMidPath(z), z, ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == zipAppInfo.getAppType());
    }

    public String getNewRootDir(ZipAppInfo zipAppInfo) {
        boolean z = true;
        String genMidPath = zipAppInfo.genMidPath(true);
        if (ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP != zipAppInfo.getAppType()) {
            z = false;
        }
        return getFileAbsolutePath(genMidPath, false, z);
    }

    public String getZipResAbsolutePath(ZipAppInfo zipAppInfo, String str, boolean z) {
        return getFileAbsolutePath(zipAppInfo.genMidPath(z) + File.separator + str, z, ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == zipAppInfo.getAppType());
    }

    public String getNewZipResAbsolutePath(ZipAppInfo zipAppInfo, String str, boolean z) {
        StringBuilder sb = new StringBuilder();
        boolean z2 = true;
        sb.append(zipAppInfo.genMidPath(true));
        sb.append(File.separator);
        sb.append(str);
        String sb2 = sb.toString();
        if (ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP != zipAppInfo.getAppType()) {
            z2 = false;
        }
        return getFileAbsolutePath(sb2, z, z2);
    }

    public String readFile(String str) {
        try {
            if (!FileAccesser.exists(str)) {
                String str2 = this.TAG;
                TaoLog.i(str2, "file[" + str + "] not found");
                return null;
            }
            byte[] read = FileAccesser.read(str);
            if (read != null) {
                if (read.length >= 1) {
                    return new String(read, ZipAppConstants.DEFAULT_ENCODING);
                }
            }
            String str3 = this.TAG;
            TaoLog.w(str3, "readConfig:[" + str + "] data is null");
            return null;
        } catch (Exception e) {
            String str4 = this.TAG;
            TaoLog.e(str4, "readFile:[" + str + "] exception:" + e.getMessage());
            return null;
        }
    }

    private boolean saveFile(String str, byte[] bArr) {
        try {
            return FileAccesser.write(str, ByteBuffer.wrap(bArr));
        } catch (Exception e) {
            String str2 = this.TAG;
            TaoLog.e(str2, "write file:[" + str + "]  exception:" + e.getMessage());
            return false;
        }
    }

    private String getFileAbsolutePath(ZipAppInfo zipAppInfo, boolean z) {
        String str;
        if (GlobalConfig.context == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(GlobalConfig.context.getFilesDir().getAbsolutePath());
        sb.append(File.separator);
        sb.append(z ? ZipAppConstants.ZIPAPP_ROOT_TMP_DIR : ZipAppConstants.ZIPAPP_ROOT_APPS_DIR);
        if (zipAppInfo.genMidPath(z) == null) {
            str = "";
        } else {
            str = File.separator + zipAppInfo.genMidPath(z);
        }
        sb.append(str);
        return sb.toString();
    }

    private String getFileAbsolutePath(String str, boolean z) {
        String str2;
        if (GlobalConfig.context == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(GlobalConfig.context.getFilesDir().getAbsolutePath());
        sb.append(File.separator);
        sb.append(z ? ZipAppConstants.ZIPAPP_ROOT_TMP_DIR : ZipAppConstants.ZIPAPP_ROOT_APPS_DIR);
        if (str == null) {
            str2 = "";
        } else {
            str2 = File.separator + str;
        }
        sb.append(str2);
        return sb.toString();
    }

    private String getFileAbsolutePath(String str, boolean z, boolean z2) {
        String str2;
        if (GlobalConfig.context == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append(GlobalConfig.context.getFilesDir().getAbsolutePath());
        sb.append(File.separator);
        sb.append(z ? ZipAppConstants.ZIPAPP_ROOT_TMP_DIR : z2 ? ZipAppConstants.ZIPAPP_ROOT_APPS_DIR : ZipAppConstants.ZIPAPP_ROOT_ZCACHE_DIR);
        if (str == null) {
            str2 = "";
        } else {
            str2 = File.separator + str;
        }
        sb.append(str2);
        return sb.toString();
    }

    private boolean deleteDir(String str, String str2) {
        try {
            File file = new File(str);
            if (file.exists()) {
                if (file.isDirectory()) {
                    for (File file2 : file.listFiles()) {
                        if (!file2.isDirectory()) {
                            file2.delete();
                        } else if (!str2.equals(file2.getName())) {
                            FileAccesser.deleteFile(file2);
                        }
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setZipDegradeDecider(ZipDegradeDecider zipDegradeDecider) {
        this.mDecider = zipDegradeDecider;
    }
}
