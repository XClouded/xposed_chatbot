package com.taobao.downloader.adpater.impl;

import com.taobao.downloader.Configuration;
import com.taobao.downloader.adpater.FileCacheManager;
import com.taobao.downloader.util.FileUtil;

public class SimpleFileCacheManager implements FileCacheManager {
    public String getTmpCache() {
        return FileUtil.getStorePath(Configuration.sContext, "download-sdk/tmp");
    }
}
