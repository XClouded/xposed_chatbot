package com.taobao.downloader.download;

import com.taobao.downloader.request.task.SingleTask;

public interface IDownloader {
    void cancel();

    void download(SingleTask singleTask, IListener iListener);

    void pause();
}
