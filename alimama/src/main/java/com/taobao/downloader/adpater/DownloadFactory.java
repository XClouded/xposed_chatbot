package com.taobao.downloader.adpater;

import com.taobao.downloader.download.IDownloader;
import com.taobao.downloader.request.Param;

public interface DownloadFactory {
    IDownloader getDownloader(Param param);
}
