package com.alibaba.android.update.state;

import com.alibaba.android.update.UpdatePreference;
import com.alibaba.android.update.UpdateUtils;

public class DownloadingState extends State {
    private static final String TAG = "DownloadingState";

    public boolean isSuspend() {
        return false;
    }

    public void resume() {
    }

    public void suspend() {
    }

    public String toString() {
        return TAG;
    }

    public void fail() {
        this.mProcessor.setState(this.mProcessor.getUndownloadState());
        resetCache();
    }

    public void finish() {
        super.finish();
        this.mEditor.putBoolean(UpdatePreference.KEY_DOWNLOAD_COMPLETE, true);
        this.mEditor.putBoolean(UpdatePreference.KEY_DOWNLOADING, false);
        this.mEditor.commit();
    }

    private void resetCache() {
        this.logger.logd(TAG, "update->UndownloadState: resetCache");
        UpdateUtils.reset(this.mProcessor.getContext());
    }
}
