package com.taobao.android.dinamicx.notification;

import androidx.annotation.NonNull;
import com.taobao.android.dinamicx.DXEngineConfig;
import com.taobao.android.dinamicx.notification.DXSignalProduce;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DXNotificationCenter implements DXSignalProduce.SignalReceiver {
    List<DXTemplateItem> failedTemplateItems;
    List<DXTemplateItem> finishedTemplateItems;
    boolean hasRegiserToSignalProduce = false;
    IDXNotificationListener listener;
    int periodCount;
    int periodTime;
    int receiverCount;
    List<DXTemplateUpdateRequest> templateUpdateRequestList;
    Map<String, List<DXTemplateUpdateRequest>> updateRequestMap;

    public DXNotificationCenter(@NonNull DXEngineConfig dXEngineConfig) {
        int i;
        this.periodTime = dXEngineConfig.getPeriodTime();
        if (this.periodTime < DXSignalProduce.PERIOD_TIME) {
            i = DXSignalProduce.PERIOD_TIME;
        } else {
            i = this.periodTime;
        }
        this.periodCount = i / DXSignalProduce.PERIOD_TIME;
        this.finishedTemplateItems = new ArrayList();
        this.failedTemplateItems = new ArrayList();
        this.templateUpdateRequestList = new ArrayList();
    }

    public void registerNotificationListener(IDXNotificationListener iDXNotificationListener) {
        if (iDXNotificationListener != null) {
            this.listener = iDXNotificationListener;
            if (!this.hasRegiserToSignalProduce) {
                DXSignalProduce.getInstance().registerNotificationCenter(this);
                this.hasRegiserToSignalProduce = true;
            }
        }
    }

    public void unRegisterNotificationListener(IDXNotificationListener iDXNotificationListener) {
        if (this.hasRegiserToSignalProduce) {
            if (iDXNotificationListener != null) {
                this.listener = null;
            }
            DXSignalProduce.getInstance().unregisterNotificationCenter(this);
            this.hasRegiserToSignalProduce = false;
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void clear() {
        this.finishedTemplateItems = new ArrayList();
        this.failedTemplateItems = new ArrayList();
        this.templateUpdateRequestList = new ArrayList();
    }

    /* access modifiers changed from: package-private */
    public synchronized void sendNotification() {
        if (needSendNotification()) {
            final DXNotificationResult dXNotificationResult = new DXNotificationResult(this.finishedTemplateItems, this.failedTemplateItems, this.templateUpdateRequestList);
            clear();
            DXRunnableManager.runOnUIThread(new Runnable() {
                public void run() {
                    DXNotificationCenter.this.listener.onNotificationListener(dXNotificationResult);
                }
            });
        }
    }

    private boolean needSendNotification() {
        return this.finishedTemplateItems.size() > 0 || this.failedTemplateItems.size() > 0 || this.templateUpdateRequestList.size() > 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0011, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void postNotification(com.taobao.android.dinamicx.notification.DXTemplateUpdateRequest r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 == 0) goto L_0x0010
            com.taobao.android.dinamicx.template.download.DXTemplateItem r0 = r2.item     // Catch:{ all -> 0x000d }
            if (r0 != 0) goto L_0x0008
            goto L_0x0010
        L_0x0008:
            r1.addRequest(r2)     // Catch:{ all -> 0x000d }
            monitor-exit(r1)
            return
        L_0x000d:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        L_0x0010:
            monitor-exit(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.notification.DXNotificationCenter.postNotification(com.taobao.android.dinamicx.notification.DXTemplateUpdateRequest):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void postNotification(com.taobao.android.dinamicx.template.download.DXDownloadResult r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 != 0) goto L_0x0005
            monitor-exit(r1)
            return
        L_0x0005:
            boolean r0 = r2.isSuccess()     // Catch:{ all -> 0x002c }
            if (r0 == 0) goto L_0x001b
            com.taobao.android.dinamicx.template.download.DXTemplateItem r0 = r2.getItem()     // Catch:{ all -> 0x002c }
            if (r0 == 0) goto L_0x001b
            java.util.List<com.taobao.android.dinamicx.template.download.DXTemplateItem> r0 = r1.finishedTemplateItems     // Catch:{ all -> 0x002c }
            com.taobao.android.dinamicx.template.download.DXTemplateItem r2 = r2.getItem()     // Catch:{ all -> 0x002c }
            r0.add(r2)     // Catch:{ all -> 0x002c }
            goto L_0x002a
        L_0x001b:
            com.taobao.android.dinamicx.template.download.DXTemplateItem r0 = r2.getItem()     // Catch:{ all -> 0x002c }
            if (r0 == 0) goto L_0x002a
            java.util.List<com.taobao.android.dinamicx.template.download.DXTemplateItem> r0 = r1.failedTemplateItems     // Catch:{ all -> 0x002c }
            com.taobao.android.dinamicx.template.download.DXTemplateItem r2 = r2.getItem()     // Catch:{ all -> 0x002c }
            r0.add(r2)     // Catch:{ all -> 0x002c }
        L_0x002a:
            monitor-exit(r1)
            return
        L_0x002c:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.notification.DXNotificationCenter.postNotification(com.taobao.android.dinamicx.template.download.DXDownloadResult):void");
    }

    public synchronized void postNotification(List<DXTemplateItem> list, List<DXTemplateItem> list2) {
        if (list != null) {
            try {
                if (list.size() > 0) {
                    this.finishedTemplateItems.addAll(list);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (list2 != null && list2.size() > 0) {
            this.failedTemplateItems.addAll(list2);
        }
    }

    private void addRequest(DXTemplateUpdateRequest dXTemplateUpdateRequest) {
        if (dXTemplateUpdateRequest != null && dXTemplateUpdateRequest.item != null) {
            this.templateUpdateRequestList.add(dXTemplateUpdateRequest);
        }
    }

    public void onReceiver() {
        if (this.listener == null || this.receiverCount != this.periodCount) {
            this.receiverCount++;
            return;
        }
        sendNotification();
        this.receiverCount = 0;
    }
}
