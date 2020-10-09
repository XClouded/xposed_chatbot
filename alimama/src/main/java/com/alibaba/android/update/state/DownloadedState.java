package com.alibaba.android.update.state;

import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.update.UpdatePreference;
import com.alibaba.android.update.UpdateUtils;
import java.io.File;

public class DownloadedState extends State {
    private static final String TAG = "DownloadedState";

    public String toString() {
        return TAG;
    }

    public void fail() {
        this.mProcessor.setState(this.mProcessor.getUndownloadState());
    }

    public boolean install(String str, String str2, boolean z) {
        boolean install = UpdateUtils.install(this.mProcessor.getContext(), str, str2, z);
        if (!install) {
            handleInstallException(str);
            fail();
        }
        return install;
    }

    private void handleInstallException(String str) {
        String string = this.mSp.getString(UpdatePreference.KEY_DOWNLOAD_URL, "");
        if (!TextUtils.isEmpty(string)) {
            UpdateUtils.openBrowser(this.mProcessor.getContext(), string);
        }
        deleteDownloadedFile(str);
        resetCache();
    }

    private void resetCache() {
        this.logger.logd(TAG, "update->UndownloadState: resetCache");
        UpdateUtils.reset(this.mProcessor.getContext());
    }

    private boolean deleteDownloadedFile(String str) {
        ILogger iLogger = this.logger;
        iLogger.logd(TAG, "delete  file path: " + str);
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(Uri.parse(str).getPath());
        if (!file.exists()) {
            return false;
        }
        return file.delete();
    }
}
