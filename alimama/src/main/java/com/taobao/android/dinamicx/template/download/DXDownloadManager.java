package com.taobao.android.dinamicx.template.download;

import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXResult;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.notification.DXNotificationCenter;
import com.taobao.android.dinamicx.template.loader.DXFileManager;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.android.dinamicx.thread.DXDownLoadRunnable;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class DXDownloadManager {
    /* access modifiers changed from: private */
    public WeakReference<DXNotificationCenter> centerWeakReference;
    private IDXDownloader dxDownloader;

    interface IDXDownloadCallback {
        void onFailed(DXResult<DXTemplateItem> dXResult);

        void onFinished(DXTemplateItem dXTemplateItem);
    }

    public DXDownloadManager(IDXDownloader iDXDownloader, DXNotificationCenter dXNotificationCenter) {
        if (iDXDownloader == null) {
            this.dxDownloader = new HttpDownloader();
        } else {
            this.dxDownloader = iDXDownloader;
        }
        this.centerWeakReference = new WeakReference<>(dXNotificationCenter);
    }

    public void downloadTemplates(final String str, final List<DXTemplateItem> list, final IDXUnzipCallback iDXUnzipCallback) {
        if (list != null && list.size() > 0) {
            DXRunnableManager.runForDownLoad(new DXDownLoadRunnable(0, new Runnable() {
                public void run() {
                    for (DXTemplateItem access$300 : list) {
                        final long nanoTime = System.nanoTime();
                        final DXDownloadResult dXDownloadResult = new DXDownloadResult();
                        DXDownloadManager.this.downloadItem(str, access$300, iDXUnzipCallback, new IDXDownloadCallback() {
                            public void onFinished(DXTemplateItem dXTemplateItem) {
                                dXDownloadResult.isSuccess = true;
                                dXDownloadResult.item = dXTemplateItem;
                                DXNotificationCenter dXNotificationCenter = (DXNotificationCenter) DXDownloadManager.this.centerWeakReference.get();
                                if (dXNotificationCenter != null) {
                                    dXNotificationCenter.postNotification(dXDownloadResult);
                                }
                                DXDownloadManager.this.trackerDownloadPerform(DXMonitorConstant.DX_MONITOR_DOWNLOADER_DOWNLOAD, str, dXTemplateItem, System.nanoTime() - nanoTime);
                            }

                            public void onFailed(DXResult<DXTemplateItem> dXResult) {
                                dXDownloadResult.isSuccess = false;
                                dXDownloadResult.item = (DXTemplateItem) dXResult.result;
                                DXNotificationCenter dXNotificationCenter = (DXNotificationCenter) DXDownloadManager.this.centerWeakReference.get();
                                if (dXNotificationCenter != null) {
                                    dXNotificationCenter.postNotification(dXDownloadResult);
                                }
                                DXDownloadManager.this.trackerDownloadPerform(DXMonitorConstant.DX_MONITOR_DOWNLOADER_DOWNLOAD, str, dXDownloadResult.item, System.nanoTime() - nanoTime);
                                DXDownloadManager.this.trackerError(dXResult.getDxError());
                            }
                        });
                    }
                }
            }));
        }
    }

    /* access modifiers changed from: private */
    public void downloadItem(String str, DXTemplateItem dXTemplateItem, IDXUnzipCallback iDXUnzipCallback, IDXDownloadCallback iDXDownloadCallback) {
        DXResult dXResult = new DXResult();
        DXError dXError = new DXError(str);
        byte[] download = this.dxDownloader.download(dXTemplateItem.templateUrl);
        if (download == null) {
            DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_DOWNLOADER, DXMonitorConstant.DX_MONITOR_DOWNLOADER_DOWNLOAD, 60000);
            dXResult.result = dXTemplateItem;
            dXError.dxTemplateItem = dXTemplateItem;
            dXError.dxErrorInfoList.add(dXErrorInfo);
            dXResult.setDxError(dXError);
            iDXDownloadCallback.onFailed(dXResult);
            return;
        }
        if (DXIOUtils.unzip(dXTemplateItem, download, DXFileManager.getInstance().getFilePath() + DXTemplateNamePathUtil.DIR + str + DXTemplateNamePathUtil.DIR + dXTemplateItem.name + DXTemplateNamePathUtil.DIR + dXTemplateItem.version + DXTemplateNamePathUtil.DIR, iDXUnzipCallback, dXError)) {
            iDXDownloadCallback.onFinished(dXTemplateItem);
            return;
        }
        dXResult.result = dXTemplateItem;
        dXResult.setDxError(dXError);
        iDXDownloadCallback.onFailed(dXResult);
    }

    /* access modifiers changed from: private */
    public void trackerDownloadPerform(String str, String str2, DXTemplateItem dXTemplateItem, long j) {
        DXAppMonitor.trackerPerform(2, str2, DXMonitorConstant.DX_MONITOR_DOWNLOADER, str, dXTemplateItem, (Map<String, String>) null, (double) j, true);
    }

    /* access modifiers changed from: private */
    public void trackerError(DXError dXError) {
        DXAppMonitor.trackerError(dXError);
    }
}
