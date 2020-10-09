package com.taobao.downloader.download;

import com.taobao.downloader.request.task.SingleTask;

public interface IListener {
    void onProgress(long j);

    void onResult(SingleTask singleTask);
}
