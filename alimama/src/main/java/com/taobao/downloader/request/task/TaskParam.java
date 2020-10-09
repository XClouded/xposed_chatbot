package com.taobao.downloader.request.task;

import com.taobao.downloader.request.Item;
import com.taobao.downloader.request.Param;
import java.util.List;

public class TaskParam {
    public List<Item> inputItems;
    public TaskListener listener;
    public int status;
    public int taskId;
    public Param userParam;

    public String toString() {
        return "TaskParams{downloadParams=" + this.userParam + ", taskId=" + this.taskId + ", status=" + this.status + '}';
    }
}
