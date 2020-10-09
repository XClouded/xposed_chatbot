package com.taobao.downloader.adpater;

import com.taobao.downloader.request.DownloadRequest;

public interface CloundConfigAdapter {
    String getConfig(String str);

    DownloadRequest make(String str);
}
