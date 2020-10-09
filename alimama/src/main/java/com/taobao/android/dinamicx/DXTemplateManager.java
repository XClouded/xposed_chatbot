package com.taobao.android.dinamicx;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.DXTemplateDBManager;
import com.taobao.android.dinamicx.template.DXTemplateDowngradeManager;
import com.taobao.android.dinamicx.template.DXTemplateInfoManager;
import com.taobao.android.dinamicx.template.DXWidgetNodeCacheManager;
import com.taobao.android.dinamicx.template.download.DXDownloadManager;
import com.taobao.android.dinamicx.template.download.DXIOUtils;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.download.IDXUnzipCallback;
import com.taobao.android.dinamicx.template.loader.DXFileManager;
import com.taobao.android.dinamicx.template.loader.DXPackageManager;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.android.dinamicx.thread.DXDownLoadRunnable;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DXTemplateManager extends DXBaseClass {
    private Context context;
    private DXTemplateDowngradeManager downgradeManager = new DXTemplateDowngradeManager(this.config.downgradeType);
    private DXDownloadManager downloader;
    /* access modifiers changed from: private */
    public long engineId = this.config.getEngineId();
    private DXPackageManager loaderManager = new DXPackageManager();

    DXTemplateManager(DXEngineContext dXEngineContext, final Context context2) {
        super(dXEngineContext);
        this.context = context2;
        this.downloader = new DXDownloadManager(DXGlobalCenter.dxDownloader, dXEngineContext.getEngine().dxNotificationCenter);
        DXRunnableManager.runOnWorkThread(new Runnable() {
            public void run() {
                DXTemplateDBManager.getInstance().init(context2, "dinamicx");
            }
        });
        DXFileManager.getInstance().initFilePath(context2);
        loadPresetTemplateInfoList();
    }

    private void loadPresetTemplateInfoList() {
        if (DXTemplateInfoManager.getInstance().needLoadPresetTemplateInfo(this.bizType)) {
            DXTemplateInfoManager.getInstance().putPresetTemplateInfo(this.bizType, DXIOUtils.readPresetTemplateInfo(this.bizType));
        }
    }

    /* access modifiers changed from: package-private */
    public DXWidgetNode getTemplateWT(DXRuntimeContext dXRuntimeContext) {
        if (dXRuntimeContext == null) {
            return null;
        }
        DXRuntimeContext cloneWithWidgetNode = dXRuntimeContext.cloneWithWidgetNode((DXWidgetNode) null);
        cloneWithWidgetNode.dxError = new DXError(this.config.bizType);
        cloneWithWidgetNode.dxError.dxTemplateItem = dXRuntimeContext.dxTemplateItem;
        cloneWithWidgetNode.userContext = null;
        cloneWithWidgetNode.dxUserContext = null;
        DXTemplateItem dXTemplateItem = cloneWithWidgetNode.dxTemplateItem;
        if (!isTemplateExist(dXTemplateItem)) {
            return null;
        }
        if (dXTemplateItem.packageInfo == null || TextUtils.isEmpty(dXTemplateItem.packageInfo.mainFilePath)) {
            dXTemplateItem.packageInfo = DXTemplateInfoManager.getInstance().getPackageInfo(this.bizType, dXTemplateItem);
        }
        if (dXTemplateItem.packageInfo == null) {
            return null;
        }
        DXWidgetNode cache = DXWidgetNodeCacheManager.getInstance().getCache(this.bizType, dXTemplateItem);
        if (cache == null) {
            long nanoTime = System.nanoTime();
            cache = this.loaderManager.load(dXTemplateItem, cloneWithWidgetNode, this.context);
            DXAppMonitor.trackerPerform(3, this.bizType, DXMonitorConstant.DX_MONITOR_TEMPLATE, "Pipeline_Stage_Load_Binary", dXTemplateItem, (Map<String, String>) null, (double) (System.nanoTime() - nanoTime), true);
            if (cache != null) {
                cache.setStatFlag(1);
                DXWidgetNodeCacheManager.getInstance().putCache(this.bizType, dXTemplateItem, cache);
            }
        }
        if (!(cache != null || dXRuntimeContext.getDxError() == null || dXRuntimeContext.getDxError().dxErrorInfoList == null || cloneWithWidgetNode.getDxError() == null || cloneWithWidgetNode.getDxError().dxErrorInfoList == null)) {
            dXRuntimeContext.getDxError().dxErrorInfoList.addAll(cloneWithWidgetNode.getDxError().dxErrorInfoList);
        }
        return cache;
    }

    /* access modifiers changed from: package-private */
    public boolean isTemplateExist(DXTemplateItem dXTemplateItem) {
        long nanoTime = System.nanoTime();
        boolean isTemplateExist = DXTemplateInfoManager.getInstance().isTemplateExist(this.bizType, dXTemplateItem);
        trackerPerform(DXMonitorConstant.DX_MONITOR_TEMPLATE_EXIST, this.bizType, dXTemplateItem, System.nanoTime() - nanoTime);
        return isTemplateExist;
    }

    /* access modifiers changed from: package-private */
    public DXTemplateItem fetchPresetTemplate(DXTemplateItem dXTemplateItem) {
        long nanoTime = System.nanoTime();
        DXTemplateItem fetchPresetTemplate = this.downgradeManager.fetchPresetTemplate(this.bizType, this.engineId, dXTemplateItem);
        trackerPerform(DXMonitorConstant.DX_MONITOR_TEMPLATE_FETCH, this.bizType, dXTemplateItem, System.nanoTime() - nanoTime);
        return fetchPresetTemplate;
    }

    /* access modifiers changed from: package-private */
    public DXTemplateItem fetchTemplate(DXTemplateItem dXTemplateItem) {
        long nanoTime = System.nanoTime();
        DXTemplateItem fetchTemplate = this.downgradeManager.fetchTemplate(this.bizType, this.engineId, dXTemplateItem);
        trackerPerform(DXMonitorConstant.DX_MONITOR_TEMPLATE_FETCH, this.bizType, dXTemplateItem, System.nanoTime() - nanoTime);
        return fetchTemplate;
    }

    /* access modifiers changed from: package-private */
    public void downgradeTemplate(DXTemplateItem dXTemplateItem) {
        this.downgradeManager.startStrategy(this.bizType, this.engineId, dXTemplateItem);
    }

    /* access modifiers changed from: package-private */
    public void downloadTemplates(List<DXTemplateItem> list) {
        this.downloader.downloadTemplates(this.bizType, getTemplateDownloadTaskList(list).downloadTaskTemplates, new IDXUnzipCallback() {
            public void onUnzipFinished(DXTemplateItem dXTemplateItem, Map<String, byte[]> map) {
                if (map != null && map.size() > 0) {
                    int size = map.size();
                    AtomicInteger atomicInteger = new AtomicInteger();
                    for (Map.Entry next : map.entrySet()) {
                        final String str = (String) next.getKey();
                        final byte[] bArr = (byte[]) next.getValue();
                        DXFileManager.getInstance().putFileCache(str, bArr);
                        final AtomicInteger atomicInteger2 = atomicInteger;
                        final int i = size;
                        final DXTemplateItem dXTemplateItem2 = dXTemplateItem;
                        DXRunnableManager.runForDownLoad(new DXDownLoadRunnable(2, new Runnable() {
                            public void run() {
                                if (DXFileManager.getInstance().save(str, bArr) && atomicInteger2.incrementAndGet() == i) {
                                    DXTemplateDBManager.getInstance().insertTemplateItem(DXTemplateManager.this.bizType, dXTemplateItem2);
                                }
                            }
                        }));
                    }
                    DXTemplateInfoManager.getInstance().updateTemplate(DXTemplateManager.this.bizType, DXTemplateManager.this.engineId, dXTemplateItem);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void setUpMaxDowngradeCount(int i) {
        this.downgradeManager.setUpMaxDowngradeCount(i);
    }

    /* access modifiers changed from: package-private */
    public void resetDowngradeCount() {
        this.downgradeManager.resetDowngradeCount(this.engineId);
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        this.downgradeManager.resetDowngradeCount(this.engineId);
        DXTemplateDBManager.getInstance().closeDatabase();
    }

    private DownloadTemplatesTask getTemplateDownloadTaskList(List<DXTemplateItem> list) {
        DownloadTemplatesTask downloadTemplatesTask = new DownloadTemplatesTask();
        if (list == null || list.isEmpty()) {
            return downloadTemplatesTask;
        }
        HashSet<DXTemplateItem> hashSet = new HashSet<>(list);
        if (hashSet.size() > 0) {
            for (DXTemplateItem dXTemplateItem : hashSet) {
                if (DXTemplateNamePathUtil.isValid(dXTemplateItem) && !isTemplateExist(dXTemplateItem)) {
                    downloadTemplatesTask.downloadTaskTemplates.add(dXTemplateItem);
                }
            }
        }
        return downloadTemplatesTask;
    }

    private class DownloadTemplatesTask {
        List<DXTemplateItem> downloadTaskTemplates = new ArrayList();

        DownloadTemplatesTask() {
        }
    }

    private void trackerPerform(String str, String str2, DXTemplateItem dXTemplateItem, long j) {
        DXAppMonitor.trackerPerform(2, str2, DXMonitorConstant.DX_MONITOR_TEMPLATE, str, dXTemplateItem, (Map<String, String>) null, (double) j, true);
    }
}
