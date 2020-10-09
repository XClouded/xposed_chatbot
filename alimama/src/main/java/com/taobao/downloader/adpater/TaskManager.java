package com.taobao.downloader.adpater;

import com.taobao.downloader.request.ModifyParam;
import com.taobao.downloader.request.task.SingleTask;
import com.taobao.downloader.request.task.TaskParam;
import java.util.List;

public interface TaskManager {
    void addTask(List<SingleTask> list, TaskParam taskParam);

    void modifyTask(int i, int i2);

    void modifyTask(int i, ModifyParam modifyParam);
}
