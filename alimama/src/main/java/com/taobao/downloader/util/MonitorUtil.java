package com.taobao.downloader.util;

import com.taobao.downloader.Configuration;
import com.taobao.downloader.adpater.Monitor;
import com.uc.webview.export.extension.UCCore;
import java.net.URL;

public class MonitorUtil {

    public static class DownloadStat {
        public String biz;
        public long connectTime;
        public double downloadSpeed;
        public long downloadTime;
        public String error_code;
        public String error_msg;
        public boolean range;
        public boolean retry;
        public long size;
        public long startTime = System.currentTimeMillis();
        public boolean success;
        public long traffic;
        public URL url;
    }

    public static void monitorSuccess(String str, String str2) {
        try {
            if (Configuration.monitor != null && (Configuration.monitor instanceof Monitor)) {
                Configuration.monitor.monitorSuccess("download-sdk", str, str2);
            }
        } catch (Throwable unused) {
        }
    }

    public static void monitorFail(String str, String str2, String str3, String str4) {
        if (Configuration.monitor != null) {
            Configuration.monitor.monitorFail("download-sdk", str, str2, str3, str4);
        }
    }

    public static void statDownload(DownloadStat downloadStat, String str) {
        if (Configuration.monitor != null) {
            try {
                Configuration.monitor.stat(downloadStat, str);
            } catch (Throwable th) {
                LogUtil.error(UCCore.EVENT_STAT, "on exception", th);
            }
        }
    }
}
