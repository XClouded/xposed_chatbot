package com.taobao.downloader.request;

public interface DownloadListener {

    public interface NetworkLimitCallback {
        void hasChangeParams(boolean z);
    }

    void onDownloadError(String str, int i, String str2);

    void onDownloadFinish(String str, String str2);

    void onDownloadProgress(int i);

    void onDownloadStateChange(String str, boolean z);

    void onFinish(boolean z);

    void onNetworkLimit(int i, Param param, NetworkLimitCallback networkLimitCallback);
}
