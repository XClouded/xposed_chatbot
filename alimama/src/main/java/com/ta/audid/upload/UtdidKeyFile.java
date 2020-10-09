package com.ta.audid.upload;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import com.alibaba.analytics.core.device.Constants;
import com.ta.audid.Variables;
import com.ta.audid.permission.PermissionUtils;
import com.ta.audid.utils.FileUtils;
import com.ta.audid.utils.UtUtils;
import com.ta.audid.utils.UtdidLogger;
import com.ta.utdid2.android.utils.AESUtils;
import com.taobao.alivfsadapter.MonitorCacheEvent;
import com.taobao.android.dinamic.DinamicConstant;
import java.io.File;
import java.util.HashMap;

public class UtdidKeyFile {
    private static final String AUDID_FILE_DIR = (Constants.PERSISTENT_CONFIG_DIR + File.separator + "Global");
    private static final String AUDID_FILE_NAME = "cec06585501c9775";
    private static final String AUDID_NOT_UPLOAD = "3c9b584e65e6c983";
    private static final String SDCARD_DEVICE_MODLE_FILE_NAME = "322a309482c4dae6";
    private static final String UTDID_FILE_APPS = "c3de653fbca500f9";
    private static final String UTDID_FILE_APPUTDID = "4635b664f789000d";
    private static final String UTDID_FILE_DIR = ".7934039a7252be16";
    private static final String UTDID_FILE_LOCK = "9983c160aa044115";
    private static final String UTDID_FILE_OLDMODE = "719893c6fa359335";
    private static final String UTDID_FILE_UPLOADLOCK = "a325712a39bd320a";
    private static final String UTDID_FILE_UTDID = "7934039a7252be16";

    private static String getAudidFilePath() {
        if (!PermissionUtils.checkStoragePermissionGranted(Variables.getInstance().getContext())) {
            return null;
        }
        return getUtdidSdcardRootFileDir() + File.separator + AUDID_FILE_NAME;
    }

    public static String readAudidFile() {
        try {
            String audidFilePath = getAudidFilePath();
            if (TextUtils.isEmpty(audidFilePath)) {
                return null;
            }
            String readFile = FileUtils.readFile(audidFilePath);
            if (TextUtils.isEmpty(readFile) || readFile.length() == 32 || readFile.length() == 36) {
                return readFile;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(DinamicConstant.LENGTH_PREFIX, "" + readFile.length());
            hashMap.put("type", MonitorCacheEvent.OPERATION_READ);
            UtUtils.sendUtdidMonitorEvent("audid", hashMap);
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void writeAudidFile(String str) {
        try {
            UtdidLogger.sd("", "audid:" + str);
            String audidFilePath = getAudidFilePath();
            if (TextUtils.isEmpty(audidFilePath)) {
                return;
            }
            if (TextUtils.isEmpty(str) || str.length() == 32 || str.length() == 36) {
                FileUtils.saveFile(audidFilePath, str);
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(DinamicConstant.LENGTH_PREFIX, "" + str.length());
            hashMap.put("type", MonitorCacheEvent.OPERATION_WRITE);
            UtUtils.sendUtdidMonitorEvent("audid", hashMap);
        } catch (Exception unused) {
        }
    }

    private static String getSdcardUtdidFilePath() {
        if (!PermissionUtils.checkStoragePermissionGranted(Variables.getInstance().getContext())) {
            return null;
        }
        return getUtdidSdcardRootFileDir() + File.separator + UTDID_FILE_UTDID;
    }

    public static String readSdcardUtdidFile() {
        try {
            String sdcardUtdidFilePath = getSdcardUtdidFilePath();
            if (!TextUtils.isEmpty(sdcardUtdidFilePath)) {
                return FileUtils.readFile(sdcardUtdidFilePath);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void writeSdcardUtdidFile(String str) {
        try {
            String sdcardUtdidFilePath = getSdcardUtdidFilePath();
            if (!TextUtils.isEmpty(sdcardUtdidFilePath)) {
                FileUtils.saveFile(sdcardUtdidFilePath, str);
            }
        } catch (Exception unused) {
        }
    }

    private static String getSdcardDeviceModleFilePath() {
        if (!PermissionUtils.checkStoragePermissionGranted(Variables.getInstance().getContext())) {
            return null;
        }
        return getUtdidSdcardRootFileDir() + File.separator + SDCARD_DEVICE_MODLE_FILE_NAME;
    }

    public static String readSdcardDeviceModleFile() {
        try {
            String sdcardDeviceModleFilePath = getSdcardDeviceModleFilePath();
            if (!TextUtils.isEmpty(sdcardDeviceModleFilePath)) {
                return FileUtils.readFile(sdcardDeviceModleFilePath);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void writeSdcardDeviceModleFile(String str) {
        try {
            String sdcardDeviceModleFilePath = getSdcardDeviceModleFilePath();
            if (!TextUtils.isEmpty(sdcardDeviceModleFilePath)) {
                FileUtils.saveFile(sdcardDeviceModleFilePath, str);
            }
        } catch (Exception unused) {
        }
    }

    private static String getAppUtdidFilePath() {
        Context context = Variables.getInstance().getContext();
        String str = getUtdidAppRootFileDir(context) + File.separator + UTDID_FILE_APPUTDID;
        UtdidLogger.sd("", str);
        return str;
    }

    private static String getAppsFilePath() {
        Context context = Variables.getInstance().getContext();
        String str = getUtdidAppRootFileDir(context) + File.separator + UTDID_FILE_APPS;
        UtdidLogger.sd("", str);
        return str;
    }

    public static String getFileLockPath() {
        Context context = Variables.getInstance().getContext();
        return getUtdidAppRootFileDir(context) + File.separator + UTDID_FILE_LOCK;
    }

    public static String getUploadFileLockPath() {
        Context context = Variables.getInstance().getContext();
        return getUtdidAppRootFileDir(context) + File.separator + UTDID_FILE_UPLOADLOCK;
    }

    public static String getOldModeFilePath() {
        Context context = Variables.getInstance().getContext();
        return getUtdidAppRootFileDir(context) + File.separator + UTDID_FILE_OLDMODE;
    }

    public static String readAppUtdidFile() {
        try {
            return FileUtils.readFile(getAppUtdidFilePath());
        } catch (Exception unused) {
            return null;
        }
    }

    public static void writeAppUtdidFile(String str) {
        try {
            UtdidLogger.d();
            FileUtils.saveFile(getAppUtdidFilePath(), str);
        } catch (Throwable unused) {
        }
    }

    public static String readAppsFile() {
        try {
            return AESUtils.decrypt(FileUtils.readFile(getAppsFilePath()));
        } catch (Exception unused) {
            return null;
        }
    }

    public static void writeAppsFile(String str) {
        try {
            UtdidLogger.sd("", str);
            if (TextUtils.isEmpty(str)) {
                new File(getAppsFilePath()).delete();
            } else {
                FileUtils.saveFile(getAppsFilePath(), AESUtils.encrypt(str));
            }
        } catch (Throwable unused) {
        }
    }

    private static String getUtdidSdcardRootFileDir() {
        String str = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + AUDID_FILE_DIR;
        UtdidLogger.sd("", "SdcardRoot dir:" + str);
        FileUtils.isDirExist(str);
        return str;
    }

    private static String getUtdidAppRootFileDir(Context context) {
        String str = context.getFilesDir().getAbsolutePath() + File.separator + UTDID_FILE_DIR;
        UtdidLogger.sd("", "UtdidAppRoot dir:" + str);
        FileUtils.isDirExist(str);
        return str;
    }

    public static void writeUtdidToSettings(Context context, String str) {
        String str2;
        try {
            str2 = Settings.System.getString(context.getContentResolver(), UTDID_FILE_UTDID);
        } catch (Exception unused) {
            str2 = null;
        }
        if (!str.equals(str2)) {
            try {
                Settings.System.putString(context.getContentResolver(), UTDID_FILE_UTDID, str);
            } catch (Exception unused2) {
            }
        }
    }

    public static String getUtdidFromSettings(Context context) {
        try {
            return Settings.System.getString(context.getContentResolver(), UTDID_FILE_UTDID);
        } catch (Exception unused) {
            return null;
        }
    }

    public static boolean enableUpload(Context context) {
        try {
            return !context.getFileStreamPath(AUDID_NOT_UPLOAD).exists();
        } catch (Exception unused) {
            return true;
        }
    }
}
