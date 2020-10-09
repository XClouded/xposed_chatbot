package com.alibaba.android.update;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyFactory;
import com.alibaba.android.update.NetworkInfo;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.taobao.bspatch.BSPatch;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.List;

public class UpdateUtils {
    private static String DEFAULT_DOWNLOAD_DIRECTORY = Environment.DIRECTORY_DOWNLOADS;
    private static final String DEFAULT_DOWNLOAD_NAME = "default_update";
    private static final long DOWNLOAD_MIN_SIZE = 104857600;
    public static final String SUFFIX_APK = "apk";
    public static final String SUFFIX_PATCH = "patch";
    private static final String TAG = "UpdateUtils";

    static boolean isSilentAvailable(Context context) {
        ILogger iLogger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
        if (context == null) {
            iLogger.logd(TAG, "update->isSilentAvailable context = null");
            return false;
        } else if (!UpdatePreference.getInstance(context).getSharedPreferences().getBoolean(UpdatePreference.KEY_SWITCH_SILIENT_ON, false)) {
            iLogger.logd(TAG, "update->isSilentAvailable isSwitchOn false");
            return false;
        } else {
            NetworkInfo.NetWorkType currentNetType = NetworkInfo.getCurrentNetType(context);
            if (currentNetType != null) {
                iLogger.logd(TAG, "update->isSilentAvailable netWorkType = " + currentNetType.value());
            }
            if (NetworkInfo.NetWorkType.NETWORK_TYPE_WIFI != currentNetType) {
                iLogger.logd(TAG, "update->isSilentAvailable network type not wifi, " + currentNetType);
                return false;
            } else if (getDownloadAvailableSize() <= 104857600) {
                iLogger.logd(TAG, "update->isSilentAvailable : no enough storage size");
                return false;
            } else {
                iLogger.logd(TAG, "update->isSilentAvailable true");
                return true;
            }
        }
    }

    static long getSDCardAvailaleSize() {
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long getDownloadAvailableSize() {
        try {
            StatFs statFs = new StatFs(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
            long blockSize = (long) statFs.getBlockSize();
            long availableBlocks = (long) statFs.getAvailableBlocks();
            StringBuilder sb = new StringBuilder();
            sb.append("update->可用的block数目：:");
            sb.append(availableBlocks);
            sb.append(",可用大小:");
            long j = availableBlocks * blockSize;
            sb.append(j / 1024);
            sb.append("KB");
            ((ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER)).logd(TAG, sb.toString());
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long systemDownloadWithUrl(Context context, String str, String str2, String str3, int i, int i2) {
        String str4;
        if (context == null || TextUtils.isEmpty(str)) {
            return -1;
        }
        if (isApk(str)) {
            str4 = SUFFIX_APK;
        } else if (!isPatch(str)) {
            return -1;
        } else {
            str4 = SUFFIX_PATCH;
        }
        if (Build.VERSION.SDK_INT < 9) {
            gotoUrl(context, str);
            return -1;
        }
        if (TextUtils.isEmpty(str3)) {
            str3 = DEFAULT_DOWNLOAD_NAME;
        }
        DownloadManager downloadManager = (DownloadManager) context.getSystemService("download");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        request.setTitle(str2);
        if (i >= 0) {
            request.setAllowedNetworkTypes(i);
        }
        if (Build.VERSION.SDK_INT >= 11) {
            request.setNotificationVisibility(i2);
        }
        try {
            ILogger iLogger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
            String downloadDirectory = getDownloadDirectory(context);
            iLogger.logd(TAG, "download directory: " + downloadDirectory);
            request.setDestinationInExternalPublicDir(downloadDirectory, str3 + "." + str4);
            long enqueue = downloadManager.enqueue(request);
            iLogger.logd(TAG, "update->systemDownloadWithUrl downloadId: " + enqueue);
            return enqueue;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getDownloadDirectory(Context context) {
        if (context == null) {
            return DEFAULT_DOWNLOAD_DIRECTORY;
        }
        String string = UpdatePreference.getInstance(context).getSharedPreferences().getString(UpdatePreference.KEY_DOWNLOAD_DIRECTORY_PATH, "");
        if (isDownloadDirectoryValid(string)) {
            return string;
        }
        return DEFAULT_DOWNLOAD_DIRECTORY;
    }

    public static boolean setDownloadDirectory(Context context, String str) {
        ILogger iLogger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        boolean mkdirs = !file.exists() ? file.mkdirs() : true;
        if (mkdirs) {
            iLogger.logd(TAG, "set dowload directory: " + str);
            SharedPreferences.Editor edit = UpdatePreference.getInstance(context).getSharedPreferences().edit();
            edit.putString(UpdatePreference.KEY_DOWNLOAD_DIRECTORY_PATH, str);
            edit.commit();
        }
        return mkdirs;
    }

    private static boolean isDownloadDirectoryValid(String str) {
        ILogger iLogger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
        if (!TextUtils.isEmpty(str) && new File(str).exists()) {
            return true;
        }
        return false;
    }

    static boolean installApk(Context context, String str, String str2, boolean z) {
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        ILogger iLogger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
        String decode = decode(str);
        if (TextUtils.isEmpty(decode)) {
            iLogger.logd(TAG, "installApk->filePath is empty");
            return false;
        }
        String path = Uri.parse(decode).getPath();
        if (TextUtils.isEmpty(path)) {
            iLogger.logd(TAG, "installApk->uri.getPath is empty");
            return false;
        }
        iLogger.logd(TAG, "installApk->path: " + path);
        if (!z || isValid(path, str2)) {
            File file = new File(path);
            if (!file.exists()) {
                iLogger.logd(TAG, "installApk-> file is null or not exist, path: " + path);
                return false;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(268435456);
            context.startActivity(intent);
            return true;
        }
        iLogger.logd(TAG, "apk file is invalid, md5 is unequal");
        return false;
    }

    public static void gotoUrlUserSystemBrowser(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            try {
                Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage("com.android.browser");
                launchIntentForPackage.setAction("android.intent.action.VIEW");
                launchIntentForPackage.addCategory("android.intent.category.DEFAULT");
                launchIntentForPackage.addFlags(268435456);
                launchIntentForPackage.setData(Uri.parse(str));
                context.startActivity(launchIntentForPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void gotoUrl(Context context, String str) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(268435456);
            intent.setData(Uri.parse(str));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppExist(Context context, String str) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            if (installedPackages.get(i).packageName.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSystemBrowserExist(Context context) {
        return isAppExist(context, "com.android.browser");
    }

    public static void openBrowser(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            if (isSystemBrowserExist(context)) {
                gotoUrlUserSystemBrowser(context, str);
            } else {
                gotoUrl(context, str);
            }
        }
    }

    public static boolean isPatch(String str) {
        if (!TextUtils.isEmpty(str) && str.contains(".") && SUFFIX_PATCH.equals(str.substring(str.lastIndexOf(".") + 1, str.length()))) {
            return true;
        }
        return false;
    }

    public static boolean isApk(String str) {
        if (!TextUtils.isEmpty(str) && str.contains(".") && SUFFIX_APK.equals(str.substring(str.lastIndexOf(".") + 1, str.length()))) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0048, code lost:
        r1 = r4.lastIndexOf(".");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getFileNameByString(java.lang.String r4) {
        /*
            com.alibaba.android.common.ServiceProxy r0 = com.alibaba.android.common.ServiceProxyFactory.getProxy()
            java.lang.String r1 = "common_logger"
            java.lang.Object r0 = r0.getService(r1)
            com.alibaba.android.common.ILogger r0 = (com.alibaba.android.common.ILogger) r0
            java.lang.String r1 = "UpdateUtils"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "update->UpdateService: getFileNameByString url: "
            r2.append(r3)
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            r0.logd(r1, r2)
            boolean r1 = android.text.TextUtils.isEmpty(r4)
            r2 = 0
            if (r1 == 0) goto L_0x0040
            java.lang.String r4 = "UpdateUtils"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "update->UpdateUtils: file name: "
            r1.append(r3)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.logd(r4, r1)
            return r2
        L_0x0040:
            java.lang.String r1 = "/"
            boolean r1 = r4.contains(r1)
            if (r1 == 0) goto L_0x005c
            java.lang.String r1 = "."
            int r1 = r4.lastIndexOf(r1)
            java.lang.String r3 = "/"
            int r3 = r4.lastIndexOf(r3)
            if (r3 >= r1) goto L_0x005c
            int r3 = r3 + 1
            java.lang.String r2 = r4.substring(r3, r1)
        L_0x005c:
            java.lang.String r4 = decode(r2)
            java.lang.String r1 = "UpdateUtils"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "update->UpdateUtils: file name: "
            r2.append(r3)
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            r0.logd(r1, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.update.UpdateUtils.getFileNameByString(java.lang.String):java.lang.String");
    }

    public static String getDownloadTitleById(Context context, long j) {
        if (context == null || j < 0) {
            return null;
        }
        try {
            Cursor query = ((DownloadManager) context.getSystemService("download")).query(new DownloadManager.Query().setFilterById(new long[]{j}));
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex("title"));
            query.close();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getInstallApkPathById(Context context, long j) {
        if (context == null || j < 0) {
            return null;
        }
        try {
            Cursor query = ((DownloadManager) context.getSystemService("download")).query(new DownloadManager.Query().setFilterById(new long[]{j}));
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex("local_uri"));
            query.close();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean installPatch(Context context, String str, String str2, String str3, boolean z) {
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        String str4 = str + "." + SUFFIX_APK;
        if (BSPatch.bspatch(getAppInfo(context).sourceDir, str4, Uri.parse(str2).getPath()) != 1) {
            return false;
        }
        return installApk(context, "file://" + str4, str3, z);
    }

    private static ApplicationInfo getAppInfo(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 8192);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void reset(Context context) {
        if (context != null) {
            UpdatePreference.getInstance(context).reset();
        }
    }

    public static boolean isValid(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || !new File(str).exists()) {
            return false;
        }
        if (getMD5(str).equals(str2.toLowerCase())) {
            return true;
        }
        return false;
    }

    public static String getMD5(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            File file = new File(str);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[4096];
            long length = file.length();
            long j = 0;
            while (true) {
                if (j >= length) {
                    break;
                } else if (Thread.interrupted()) {
                    break;
                } else {
                    int read = fileInputStream.read(bArr);
                    j += (long) read;
                    instance.update(bArr, 0, read);
                }
            }
            return bytesToHexString(instance.digest()).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (int i = 0; i < bArr.length; i++) {
            sb.append(Integer.toHexString((bArr[i] >> 4) & 15));
            sb.append(Integer.toHexString(bArr[i] & 15));
        }
        return sb.toString();
    }

    public static String decode(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            return URLDecoder.decode(str, "UTF-8").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static boolean install(Context context, String str, String str2, boolean z) {
        ILogger iLogger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
        iLogger.logd(TAG, "update->UpdateUtils: install, filePath: " + str + ", md5: " + str2 + ", isVerify: " + z);
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String decode = decode(str);
        String path = Uri.parse(decode).getPath();
        String fileNameByString = getFileNameByString(decode);
        if (z && !isValid(path, str2)) {
            return false;
        }
        if (isPatch(decode)) {
            iLogger.logd(TAG, "update->UpdateUtils: patch install");
            return installPatch(context, BSPatch.ROOT + File.separator + fileNameByString, decode, str2, z);
        }
        iLogger.logd(TAG, "update->UpdateUtils: apk install");
        return installApk(context, decode, str2, z);
    }
}
