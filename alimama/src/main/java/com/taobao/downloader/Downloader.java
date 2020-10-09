package com.taobao.downloader;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.downloader.adpater.Monitor;
import com.taobao.downloader.adpater.impl.SimpleDownloadFactory;
import com.taobao.downloader.adpater.impl.SimpleFileCacheManager;
import com.taobao.downloader.adpater.impl.SimpleTaskManager;
import com.taobao.downloader.request.DownloadListener;
import com.taobao.downloader.request.DownloadRequest;
import com.taobao.downloader.request.Item;
import com.taobao.downloader.request.ModifyParam;
import com.taobao.downloader.request.task.SingleTask;
import com.taobao.downloader.request.task.TaskParam;
import com.taobao.downloader.util.FileUtil;
import com.taobao.downloader.util.IdGenerator;
import com.taobao.downloader.util.LogUtil;
import com.taobao.downloader.util.MonitorUtil;
import com.taobao.downloader.wrapper.ListenerWrapper;
import java.util.ArrayList;

public class Downloader {
    protected static Downloader downloader;

    public static Downloader getInstance() {
        if (downloader == null) {
            downloader = new Downloader();
            if (Configuration.downloadFactory == null) {
                Configuration.downloadFactory = new SimpleDownloadFactory();
            }
            if (Configuration.taskManager == null) {
                Configuration.taskManager = new SimpleTaskManager();
            }
            if (Configuration.fileCacheManager == null) {
                Configuration.fileCacheManager = new SimpleFileCacheManager();
            }
        }
        return downloader;
    }

    public static void init(Context context) {
        Configuration.sContext = context;
    }

    public int download(DownloadRequest downloadRequest, DownloadListener downloadListener) {
        LogUtil.debug("api", " invoke download api  {}", downloadRequest);
        if (!(downloadRequest == null || !TextUtils.isEmpty(downloadRequest.downloadParam.fileStorePath) || Configuration.fileCacheManager == null)) {
            downloadRequest.downloadParam.fileStorePath = Configuration.fileCacheManager.getTmpCache();
        }
        if (downloadRequest == null || !downloadRequest.validate()) {
            if (downloadListener != null) {
                downloadListener.onFinish(false);
            }
            MonitorUtil.monitorFail(Monitor.POINT_ADD, "paramerror", (String) null, (String) null);
            return -100;
        }
        if (Configuration.bizPriManager != null) {
            downloadRequest.downloadParam.priority = Configuration.bizPriManager.getPriBy(downloadRequest.downloadParam);
        }
        TaskParam taskParam = new TaskParam();
        taskParam.taskId = IdGenerator.nextId();
        taskParam.userParam = downloadRequest.downloadParam;
        taskParam.inputItems = downloadRequest.downloadList;
        taskParam.listener = new ListenerWrapper(downloadRequest, downloadListener);
        ArrayList arrayList = new ArrayList();
        for (Item item : downloadRequest.downloadList) {
            SingleTask singleTask = new SingleTask();
            singleTask.item = item;
            singleTask.param = downloadRequest.downloadParam;
            singleTask.storeDir = downloadRequest.downloadParam.fileStorePath;
            arrayList.add(singleTask);
        }
        Configuration.taskManager.addTask(arrayList, taskParam);
        return taskParam.taskId;
    }

    public void cancel(int i) {
        Configuration.taskManager.modifyTask(i, 2);
    }

    public void suspend(int i) {
        Configuration.taskManager.modifyTask(i, 1);
    }

    public void resume(int i) {
        Configuration.taskManager.modifyTask(i, 0);
    }

    public void modify(int i, ModifyParam modifyParam) {
        Configuration.taskManager.modifyTask(i, modifyParam);
    }

    public int fetch(String str, String str2, DownloadListener downloadListener) {
        DownloadRequest downloadRequest = Configuration.cloundConfigAdapter == null ? new DownloadRequest(str) : Configuration.cloundConfigAdapter.make(str);
        if (!TextUtils.isEmpty(str2)) {
            downloadRequest.downloadParam.bizId = str2;
        }
        return download(downloadRequest, downloadListener);
    }

    public String getLocalFile(String str, Item item) {
        return FileUtil.getLocalFile(str, item);
    }
}
