package com.taobao.downloader.request;

public class Param {
    public static final int TYPE_AUTO = 0;
    public static final int TYPE_DM = 1;
    public static final int TYPE_HUC = 2;
    public static final int TYPE_TNET = 3;
    public boolean askIfNetLimit = false;
    public String bizId;
    public int callbackCondition = 1;
    public int callbackType = 1;
    public String description;
    public int downloadStrategy;
    public String fileStorePath;
    public boolean foreground;
    public String from = "";
    public int network = 7;
    public boolean notificationUI;
    public int notificationVisibility;
    public int priority = 10;
    public int retryTimes = 3;
    public String title;
    public boolean useCache = true;

    public String toString() {
        return "DownloadParams{priority=" + this.priority + ", network=" + this.network + ", callbackCondition=" + this.callbackCondition + ", callbackType=" + this.callbackType + ", fileStorePath='" + this.fileStorePath + '\'' + '}';
    }
}
