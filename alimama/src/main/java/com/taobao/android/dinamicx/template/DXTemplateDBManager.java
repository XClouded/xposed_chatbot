package com.taobao.android.dinamicx.template;

import android.content.Context;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.db.DXDataBaseHelper;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DXTemplateDBManager {
    private static final String DEFAULT_BIZ_TYPE = "DinamicX_db";
    private volatile DXDataBaseHelper dXDataBaseHelper;

    private DXTemplateDBManager() {
    }

    public static DXTemplateDBManager getInstance() {
        return DXTemplateDBManagerHolder.instance;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void init(android.content.Context r2, java.lang.String r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 == 0) goto L_0x001a
            boolean r0 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x0017 }
            if (r0 == 0) goto L_0x000a
            goto L_0x001a
        L_0x000a:
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper r0 = r1.dXDataBaseHelper     // Catch:{ all -> 0x0017 }
            if (r0 != 0) goto L_0x0015
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper r0 = new com.taobao.android.dinamicx.template.db.DXDataBaseHelper     // Catch:{ all -> 0x0017 }
            r0.<init>(r2, r3)     // Catch:{ all -> 0x0017 }
            r1.dXDataBaseHelper = r0     // Catch:{ all -> 0x0017 }
        L_0x0015:
            monitor-exit(r1)
            return
        L_0x0017:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        L_0x001a:
            monitor-exit(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.template.DXTemplateDBManager.init(android.content.Context, java.lang.String):void");
    }

    private boolean reinitialization() {
        if (this.dXDataBaseHelper == null) {
            init((Context) null, (String) null);
        }
        if (this.dXDataBaseHelper != null) {
            return true;
        }
        trackError(DXMonitorConstant.DX_MONITOR_DB_OPEN, DXError.DX_DB_NULL, "dXDataBaseHelper == null");
        return false;
    }

    public void dropTable() {
        if (reinitialization()) {
            this.dXDataBaseHelper.dropTable();
        }
    }

    public int getDbSize() {
        if (reinitialization()) {
            return this.dXDataBaseHelper.getDbSize();
        }
        return 0;
    }

    public void insertTemplateItem(String str, DXTemplateItem dXTemplateItem) {
        long nanoTime = System.nanoTime();
        if (reinitialization()) {
            this.dXDataBaseHelper.store(str, dXTemplateItem);
        }
        trackerPerform(DXMonitorConstant.DX_MONITOR_DB_STORE, str, dXTemplateItem, System.nanoTime() - nanoTime);
    }

    public void insertTemplateItems(String str, List<DXTemplateItem> list) {
        long nanoTime = System.nanoTime();
        if (reinitialization()) {
            this.dXDataBaseHelper.store(str, list);
        }
        trackerPerform(DXMonitorConstant.DX_MONITOR_DB_STORE, str, (DXTemplateItem) null, System.nanoTime() - nanoTime);
    }

    public void closeDatabase() {
        if (reinitialization()) {
            this.dXDataBaseHelper.closeDatabase();
        }
    }

    public void release() {
        this.dXDataBaseHelper = null;
    }

    public void deleteTemplateItem(String str, DXTemplateItem dXTemplateItem) {
        long nanoTime = System.nanoTime();
        if (reinitialization()) {
            this.dXDataBaseHelper.delete(str, dXTemplateItem);
        }
        trackerPerform(DXMonitorConstant.DX_MONITOR_DB_DELETE, str, dXTemplateItem, System.nanoTime() - nanoTime);
    }

    public void deleteAll() {
        long nanoTime = System.nanoTime();
        if (reinitialization()) {
            this.dXDataBaseHelper.deleteAll();
        }
        trackerPerform(DXMonitorConstant.DX_MONITOR_DB_DELETE_ALL, DEFAULT_BIZ_TYPE, (DXTemplateItem) null, System.nanoTime() - nanoTime);
    }

    public LinkedList<DXTemplateItem> queryTemplates(String str, DXTemplateItem dXTemplateItem) {
        long nanoTime = System.nanoTime();
        if (reinitialization()) {
            return this.dXDataBaseHelper.query(str, dXTemplateItem);
        }
        trackerPerform(DXMonitorConstant.DX_MONITOR_DB_QUERY, str, dXTemplateItem, System.nanoTime() - nanoTime);
        return null;
    }

    private static class DXTemplateDBManagerHolder {
        /* access modifiers changed from: private */
        public static final DXTemplateDBManager instance = new DXTemplateDBManager();

        private DXTemplateDBManagerHolder() {
        }
    }

    private void trackerPerform(String str, String str2, DXTemplateItem dXTemplateItem, long j) {
        DXAppMonitor.trackerPerform(2, str2, DXMonitorConstant.DX_MONITOR_DB, str, dXTemplateItem, (Map<String, String>) null, (double) j, true);
    }

    private void trackError(String str, int i, String str2) {
        DXError dXError = new DXError(DEFAULT_BIZ_TYPE);
        DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_DB, str, i);
        dXErrorInfo.reason = str2;
        dXError.dxErrorInfoList = new ArrayList();
        dXError.dxErrorInfoList.add(dXErrorInfo);
        DXAppMonitor.trackerError(dXError);
    }
}
