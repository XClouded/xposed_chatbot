package com.alibaba.android.update;

import android.content.Context;
import android.content.SharedPreferences;

public class UpdatePreference {
    public static final String KEY_APK_MD5 = "update_preference_apk_md5";
    public static final String KEY_APK_MD5_IS_SWITCH_ON = "update_preference_apk_md5_is_switch_on";
    public static final String KEY_DOWNLOADED_FILE_PATH = "update_preference_downloaded_file_path";
    public static final String KEY_DOWNLOADING = "update_preference_downloading";
    public static final String KEY_DOWNLOAD_COMPLETE = "update_preference_complete";
    public static final String KEY_DOWNLOAD_DIRECTORY_PATH = "update_preference_download_directory_path";
    public static final String KEY_DOWNLOAD_ID = "update_preference_download_id";
    public static final String KEY_DOWNLOAD_URL = "update_preference_url";
    public static final String KEY_DOWNLOAD_URL_PATCH = "update_preference_url_patch";
    public static final String KEY_NETWORK_CHANGE_TIMESTAMP = "update_preference_change_timestamp";
    public static final String KEY_SWITCH_SILIENT_ON = "update_preference_switch_silent_on";
    private static final String PREFERENCE_NAME = "update_preference";
    private static final String TAG = "UpdatePreference";
    private static UpdatePreference instance;
    private SharedPreferences mPref;

    private UpdatePreference(Context context) {
        if (context != null) {
            this.mPref = context.getSharedPreferences(PREFERENCE_NAME, 0);
        }
    }

    public static UpdatePreference getInstance(Context context) {
        if (instance == null) {
            if (context == null) {
                return null;
            }
            instance = new UpdatePreference(context);
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return this.mPref;
    }

    /* access modifiers changed from: package-private */
    public void reset() {
        SharedPreferences.Editor edit = this.mPref.edit();
        edit.putLong(KEY_DOWNLOAD_ID, -1);
        edit.putBoolean(KEY_DOWNLOADING, false);
        edit.putBoolean(KEY_DOWNLOAD_COMPLETE, false);
        edit.putString(KEY_DOWNLOAD_URL, "");
        edit.putString(KEY_DOWNLOAD_URL_PATCH, "");
        edit.putString(KEY_APK_MD5, "");
        edit.putBoolean(KEY_APK_MD5_IS_SWITCH_ON, false);
        edit.commit();
    }
}
