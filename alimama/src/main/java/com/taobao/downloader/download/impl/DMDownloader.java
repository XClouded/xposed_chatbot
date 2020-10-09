package com.taobao.downloader.download.impl;

import android.app.DownloadManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import com.taobao.downloader.Configuration;
import com.taobao.downloader.download.IDownloader;
import com.taobao.downloader.download.IListener;
import com.taobao.downloader.request.task.SingleTask;
import com.taobao.downloader.util.LogUtil;
import java.io.File;

public class DMDownloader implements IDownloader {
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private static DownloadManager downloadManager = ((DownloadManager) Configuration.sContext.getSystemService("download"));
    private ContentObserver downloadChangeObserver;
    private long mDownloadId;
    private IListener mListener;
    private SingleTask mTask;

    public void download(SingleTask singleTask, IListener iListener) {
        this.mListener = iListener;
        this.mTask = singleTask;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(singleTask.item.url));
        int i = (singleTask.param.network & 2) == 2 ? 1 : 0;
        if ((singleTask.param.network & 1) == 1 || (singleTask.param.network & 4) == 4) {
            i |= 2;
        }
        request.setAllowedNetworkTypes(i);
        if ((singleTask.param.network & 4) != 4 && Build.VERSION.SDK_INT > 16) {
            request.setAllowedOverMetered(false);
        }
        if (!TextUtils.isEmpty(singleTask.param.title)) {
            request.setTitle(singleTask.param.title);
            request.setDescription(singleTask.param.description);
        }
        String fileName = singleTask.getFileName();
        request.setDestinationUri(Uri.fromFile(new File(singleTask.storeDir + "/" + fileName)));
        SingleTask singleTask2 = this.mTask;
        singleTask2.storeFilePath = singleTask.storeDir + "/" + fileName;
        if (!singleTask.param.notificationUI) {
            request.setVisibleInDownloadsUi(false);
            if (hasPermission()) {
                request.setNotificationVisibility(2);
            }
        } else {
            request.setNotificationVisibility(this.mTask.param.notificationVisibility);
        }
        this.mDownloadId = downloadManager.enqueue(request);
        this.downloadChangeObserver = new ContentObserver((Handler) null) {
            public void onChange(boolean z) {
                DMDownloader.this.queryDownloadStatus();
            }
        };
        Configuration.sContext.getContentResolver().registerContentObserver(CONTENT_URI, true, this.downloadChangeObserver);
    }

    private boolean hasPermission() {
        return Configuration.sContext.checkCallingOrSelfPermission("android.permission.DOWNLOAD_WITHOUT_NOTIFICATION") == 0;
    }

    public void cancel() {
        downloadManager.remove(new long[]{this.mDownloadId});
        destroy();
    }

    public void pause() {
        destroy();
    }

    /* access modifiers changed from: private */
    public void queryDownloadStatus() {
        if (this.mDownloadId > 0) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(new long[]{this.mDownloadId});
            Cursor query2 = downloadManager.query(query);
            if (query2 != null && query2.moveToFirst()) {
                int i = query2.getInt(query2.getColumnIndex("status"));
                int columnIndex = query2.getColumnIndex("reason");
                int columnIndex2 = query2.getColumnIndex("title");
                int columnIndex3 = query2.getColumnIndex("total_size");
                int columnIndex4 = query2.getColumnIndex("bytes_so_far");
                String string = query2.getString(columnIndex2);
                int i2 = query2.getInt(columnIndex3);
                int i3 = query2.getInt(columnIndex4);
                query2.getInt(columnIndex);
                this.mListener.onProgress((long) i3);
                LogUtil.debug("tag", string + "\n" + "Downloaded " + i3 + " / " + i2, new Object[0]);
                if (i == 4) {
                    LogUtil.debug("tag", "STATUS_PAUSED", new Object[0]);
                } else if (i == 8) {
                    String string2 = query2.getString(query2.getColumnIndex("local_filename"));
                    if (!TextUtils.isEmpty(string2)) {
                        this.mTask.storeFilePath = string2;
                    }
                    this.mTask.success = true;
                    this.mListener.onResult(this.mTask);
                    destroy();
                    return;
                } else if (i != 16) {
                    switch (i) {
                        case 1:
                            break;
                        case 2:
                            break;
                        default:
                            return;
                    }
                } else {
                    LogUtil.debug("tag", "STATUS_FAILED", new Object[0]);
                    return;
                }
                LogUtil.debug("tag", "STATUS_PENDING", new Object[0]);
                LogUtil.debug("tag", "STATUS_RUNNING", new Object[0]);
            }
        }
    }

    private void destroy() {
        if (this.downloadChangeObserver != null) {
            Configuration.sContext.getContentResolver().unregisterContentObserver(this.downloadChangeObserver);
        }
    }
}
