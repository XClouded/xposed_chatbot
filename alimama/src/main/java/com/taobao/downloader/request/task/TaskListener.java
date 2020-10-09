package com.taobao.downloader.request.task;

import com.taobao.downloader.download.IListener;
import com.taobao.downloader.request.DownloadListener;
import com.taobao.downloader.request.Param;

public interface TaskListener extends IListener {
    void onDownloadStateChange(String str, boolean z);

    void onNetworkLimit(int i, Param param, DownloadListener.NetworkLimitCallback networkLimitCallback);
}
