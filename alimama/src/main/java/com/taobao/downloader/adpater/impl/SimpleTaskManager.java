package com.taobao.downloader.adpater.impl;

import com.taobao.downloader.adpater.TaskManager;
import com.taobao.downloader.download.IDownloader;
import com.taobao.downloader.request.ModifyParam;
import com.taobao.downloader.request.task.SingleTask;
import com.taobao.downloader.request.task.TaskParam;
import com.taobao.downloader.util.ThreadUtil;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleTaskManager implements TaskManager {
    /* access modifiers changed from: private */
    public ConcurrentHashMap<Integer, IDownloader> downloaderMap = new ConcurrentHashMap<>();

    public void addTask(final List<SingleTask> list, final TaskParam taskParam) {
        final IDownloader downloader = new SimpleDownloadFactory().getDownloader(taskParam.userParam);
        this.downloaderMap.put(Integer.valueOf(taskParam.taskId), downloader);
        ThreadUtil.execute(new Runnable() {
            public void run() {
                for (SingleTask download : list) {
                    downloader.download(download, taskParam.listener);
                }
                SimpleTaskManager.this.downloaderMap.remove(Integer.valueOf(taskParam.taskId));
            }
        }, false);
    }

    public void modifyTask(int i, int i2) {
        IDownloader iDownloader = this.downloaderMap.get(Integer.valueOf(i));
        if (iDownloader == null) {
            return;
        }
        if (1 == i2) {
            iDownloader.pause();
        } else if (2 == i2) {
            iDownloader.cancel();
        }
    }

    public void modifyTask(int i, ModifyParam modifyParam) {
        modifyTask(i, modifyParam.status.intValue());
    }
}
