package com.alibaba.android.update.state;

import android.text.TextUtils;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.update.UpdatePreference;
import com.alibaba.android.update.UpdateService;
import com.alibaba.android.update.UpdateUtils;

public class UndownloadState extends State {
    public static final int DOWNLOAD_NORMAL = 0;
    public static final int DOWNLOAD_PATCH = 2;
    public static final int DOWNLOAD_SILENT = 1;
    private static final String TAG = "UndownloadState";

    public String toString() {
        return TAG;
    }

    public void download(String str, String str2, boolean z, String str3, String str4, String str5, boolean z2, boolean z3) {
        int i;
        super.download(str, str2, z, str3, str4, str5, z2, z3);
        resetCache();
        this.mEditor.putString(UpdatePreference.KEY_DOWNLOAD_URL, str);
        this.mEditor.putString(UpdatePreference.KEY_DOWNLOAD_URL_PATCH, str);
        this.mEditor.putString(UpdatePreference.KEY_APK_MD5, str5);
        this.mEditor.putBoolean(UpdatePreference.KEY_APK_MD5_IS_SWITCH_ON, z2);
        if (!z || TextUtils.isEmpty(str2)) {
            i = 0;
        } else {
            str = str2;
            i = 2;
        }
        if (z3) {
            i = 1;
        }
        try {
            long download = download(str, str3, str4, i);
            this.mEditor.putLong(UpdatePreference.KEY_DOWNLOAD_ID, download);
            if (download != -1) {
                this.mEditor.putBoolean(UpdatePreference.KEY_DOWNLOADING, true);
            }
            this.mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void fail() {
        this.mProcessor.setState(this.mProcessor.getUndownloadState());
        resetCache();
    }

    private long download(String str, String str2, String str3, int i) {
        ILogger iLogger = this.logger;
        iLogger.logd(TAG, "update->UndownloadState: download, url: " + str + ", title: " + str2 + ", filename: " + str3 + ", download type: " + i);
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        switch (i) {
            case 1:
                return UpdateUtils.systemDownloadWithUrl(this.mProcessor.getContext(), str, UpdateService.TITLE_SILENT, str3, 2, 2);
            case 2:
                return UpdateUtils.systemDownloadWithUrl(this.mProcessor.getContext(), str, str2, str3, -1, 2);
            default:
                long systemDownloadWithUrl = UpdateUtils.systemDownloadWithUrl(this.mProcessor.getContext(), str, str2, str3, -1, 1);
                if (systemDownloadWithUrl == -1) {
                    UpdateUtils.gotoUrl(this.mProcessor.getContext(), str);
                }
                return systemDownloadWithUrl;
        }
    }

    private void resetCache() {
        this.logger.logd(TAG, "update->UndownloadState: resetCache");
        UpdateUtils.reset(this.mProcessor.getContext());
    }
}
