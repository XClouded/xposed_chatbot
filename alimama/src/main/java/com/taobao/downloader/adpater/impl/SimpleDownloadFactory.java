package com.taobao.downloader.adpater.impl;

import com.taobao.downloader.adpater.DownloadFactory;
import com.taobao.downloader.download.IDownloader;
import com.taobao.downloader.download.impl.DMDownloader;
import com.taobao.downloader.download.impl2.DefaultDownloader2;
import com.taobao.downloader.request.Param;

public class SimpleDownloadFactory implements DownloadFactory {
    public IDownloader getDownloader(Param param) {
        if (1 == param.downloadStrategy) {
            return new DMDownloader();
        }
        return new DefaultDownloader2();
    }
}
