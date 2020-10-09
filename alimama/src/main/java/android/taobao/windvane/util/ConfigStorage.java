package android.taobao.windvane.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.file.FileManager;
import android.text.TextUtils;

import java.io.File;
import java.nio.ByteBuffer;

public class ConfigStorage {
    public static final long DEFAULT_MAX_AGE = 21600000;
    public static final long DEFAULT_SMALL_MAX_AGE = 1800000;
    public static final String KEY_DATA = "wv-data";
    public static final String KEY_TIME = "wv-time";
    public static final String ROOT_WINDVANE_CONFIG_DIR = "windvane/config";
    /* access modifiers changed from: private */
    public static String TAG = "ConfigStorage";

    public static synchronized boolean initDirs() {
        synchronized (ConfigStorage.class) {
            if (GlobalConfig.context == null) {
                return false;
            }
            File createFolder = FileManager.createFolder(GlobalConfig.context, ROOT_WINDVANE_CONFIG_DIR);
            String str = TAG;
            TaoLog.d(str, "createDir: dir[" + createFolder.getAbsolutePath() + "]:" + createFolder.exists());
            boolean exists = createFolder.exists();
            return exists;
        }
    }

    /* access modifiers changed from: private */
    public static String getFileAbsolutePath(String str) {
        String str2;
        if (GlobalConfig.context == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append(GlobalConfig.context.getFilesDir().getAbsolutePath());
        sb.append(File.separator);
        sb.append(ROOT_WINDVANE_CONFIG_DIR);
        if (str == null) {
            str2 = "";
        } else {
            str2 = File.separator + str;
        }
        sb.append(str2);
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0016, code lost:
        return;
     */
    @android.annotation.TargetApi(11)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void putStringVal(final java.lang.String r2, final java.lang.String r3, final java.lang.String r4) {
        /*
            java.lang.Class<android.taobao.windvane.util.ConfigStorage> r0 = android.taobao.windvane.util.ConfigStorage.class
            monitor-enter(r0)
            if (r2 == 0) goto L_0x0015
            if (r3 != 0) goto L_0x0008
            goto L_0x0015
        L_0x0008:
            android.taobao.windvane.util.ConfigStorage$1 r1 = new android.taobao.windvane.util.ConfigStorage$1     // Catch:{ all -> 0x0012 }
            r1.<init>(r2, r3, r4)     // Catch:{ all -> 0x0012 }
            android.os.AsyncTask.execute(r1)     // Catch:{ all -> 0x0012 }
            monitor-exit(r0)
            return
        L_0x0012:
            r2 = move-exception
            monitor-exit(r0)
            throw r2
        L_0x0015:
            monitor-exit(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.util.ConfigStorage.putStringVal(java.lang.String, java.lang.String, java.lang.String):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0016, code lost:
        return;
     */
    @android.annotation.TargetApi(11)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void putLongVal(final java.lang.String r2, final java.lang.String r3, final long r4) {
        /*
            java.lang.Class<android.taobao.windvane.util.ConfigStorage> r0 = android.taobao.windvane.util.ConfigStorage.class
            monitor-enter(r0)
            if (r2 == 0) goto L_0x0015
            if (r3 != 0) goto L_0x0008
            goto L_0x0015
        L_0x0008:
            android.taobao.windvane.util.ConfigStorage$2 r1 = new android.taobao.windvane.util.ConfigStorage$2     // Catch:{ all -> 0x0012 }
            r1.<init>(r2, r3, r4)     // Catch:{ all -> 0x0012 }
            android.os.AsyncTask.execute(r1)     // Catch:{ all -> 0x0012 }
            monitor-exit(r0)
            return
        L_0x0012:
            r2 = move-exception
            monitor-exit(r0)
            throw r2
        L_0x0015:
            monitor-exit(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.util.ConfigStorage.putLongVal(java.lang.String, java.lang.String, long):void");
    }

    public static String getStringVal(String str, String str2) {
        String str3;
        String configKey = getConfigKey(str, str2);
        try {
            if (new File(getFileAbsolutePath(configKey)).exists()) {
                str3 = new String(FileAccesser.read(new File(getFileAbsolutePath(configKey))));
                try {
                    String str4 = TAG;
                    TaoLog.d(str4, "read " + configKey + " by file : " + str3);
                    return str3;
                } catch (Exception unused) {
                }
            } else {
                SharedPreferences sharedPreference = getSharedPreference();
                if (sharedPreference == null) {
                    return "";
                }
                String string = sharedPreference.getString(configKey, "");
                try {
                    putStringVal(str, str2, string);
                    SharedPreferences.Editor edit = sharedPreference.edit();
                    edit.remove(configKey);
                    edit.commit();
                    String str5 = TAG;
                    TaoLog.i(str5, "read " + configKey + " by sp : " + string);
                    return string;
                } catch (Exception unused2) {
                    str3 = string;
                    String str6 = TAG;
                    TaoLog.e(str6, "can not read file : " + configKey);
                    return str3;
                }
            }
        } catch (Exception unused3) {
            str3 = "";
            String str62 = TAG;
            TaoLog.e(str62, "can not read file : " + configKey);
            return str3;
        }
    }

    public static long getLongVal(String str, String str2) {
        long j;
        String configKey = getConfigKey(str, str2);
        try {
            File file = new File(getFileAbsolutePath(configKey));
            if (file.exists()) {
                byte[] read = FileAccesser.read(file);
                ByteBuffer allocate = ByteBuffer.allocate(8);
                allocate.put(read);
                allocate.flip();
                j = allocate.getLong();
                try {
                    TaoLog.d(TAG, "read " + configKey + " by file : " + j);
                    return j;
                } catch (Exception unused) {
                }
            } else {
                SharedPreferences sharedPreference = getSharedPreference();
                if (sharedPreference == null) {
                    return 0;
                }
                long j2 = sharedPreference.getLong(configKey, 0);
                try {
                    putLongVal(str, str2, j2);
                    SharedPreferences.Editor edit = sharedPreference.edit();
                    edit.remove(configKey);
                    edit.commit();
                    TaoLog.i(TAG, "read " + configKey + " by sp : " + j2);
                    return j2;
                } catch (Exception unused2) {
                    j = j2;
                    TaoLog.e(TAG, "can not read file : " + configKey);
                    return j;
                }
            }
        } catch (Exception unused3) {
            j = 0;
            TaoLog.e(TAG, "can not read file : " + configKey);
            return j;
        }
    }

    public static String getStringVal(String str, String str2, String str3) {
        try {
            String stringVal = getStringVal(str, str2);
            return TextUtils.isEmpty(stringVal) ? str3 : stringVal;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return str3;
        }
    }

    public static long getLongVal(String str, String str2, long j) {
        try {
            Long valueOf = Long.valueOf(getLongVal(str, str2));
            if (valueOf.longValue() == 0) {
                return j;
            }
            return valueOf.longValue();
        } catch (ClassCastException e) {
            e.printStackTrace();
            return j;
        }
    }

    /* access modifiers changed from: private */
    public static String getConfigKey(String str, String str2) {
        return "WindVane_" + str + str2;
    }

    private static SharedPreferences getSharedPreference() {
        if (GlobalConfig.context == null) {
            return null;
        }
        return PreferenceManager.getDefaultSharedPreferences(GlobalConfig.context);
    }
}
