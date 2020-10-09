package com.vivo.push.cache.impl;

import android.content.Context;
import android.text.TextUtils;
import com.vivo.push.PushClientConstants;
import com.vivo.push.cache.ISubscribeAppAliasManager;
import com.vivo.push.model.SubscribeAppInfo;
import java.util.Iterator;

public class SubscribeAppAliasManagerImpl extends a implements ISubscribeAppAliasManager {
    /* access modifiers changed from: protected */
    public String generateStrByType() {
        return PushClientConstants.PUSH_APP_ALIAS;
    }

    public SubscribeAppAliasManagerImpl(Context context) {
        super(context);
    }

    public boolean setAlias(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int size = this.mAppDatas.size();
        SubscribeAppInfo subscribeAppInfo = getSubscribeAppInfo();
        if (size == 1 && subscribeAppInfo != null && str.equals(subscribeAppInfo.getName()) && subscribeAppInfo.getTargetStatus() == 1) {
            return false;
        }
        clearData();
        addData(new SubscribeAppInfo(str, 1, 2));
        return true;
    }

    public boolean delAlias(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int size = this.mAppDatas.size();
        SubscribeAppInfo subscribeAppInfo = getSubscribeAppInfo();
        if (size == 1 && subscribeAppInfo != null && str.equals(subscribeAppInfo.getName()) && subscribeAppInfo.getTargetStatus() == 2) {
            return false;
        }
        clearData();
        addData(new SubscribeAppInfo(str, 2, 1));
        return true;
    }

    public void setAliasSuccess(String str) {
        synchronized (sAppLock) {
            boolean z = false;
            if (!TextUtils.isEmpty(str)) {
                Iterator it = this.mAppDatas.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SubscribeAppInfo subscribeAppInfo = (SubscribeAppInfo) it.next();
                    if (subscribeAppInfo.getName().equals(str) && subscribeAppInfo.getActualStatus() != 1) {
                        subscribeAppInfo.setActualStatus(1);
                        z = true;
                        break;
                    }
                }
            }
            if (z) {
                updateDataToSP(this.mAppDatas);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void delAliasSuccess(java.lang.String r7) {
        /*
            r6 = this;
            java.lang.Object r0 = sAppLock
            monitor-enter(r0)
            r1 = 0
            boolean r2 = android.text.TextUtils.isEmpty(r7)     // Catch:{ all -> 0x0050 }
            if (r2 != 0) goto L_0x0031
            java.util.Set r2 = r6.mAppDatas     // Catch:{ all -> 0x0050 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x0050 }
        L_0x0010:
            boolean r3 = r2.hasNext()     // Catch:{ all -> 0x0050 }
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()     // Catch:{ all -> 0x0050 }
            com.vivo.push.model.SubscribeAppInfo r3 = (com.vivo.push.model.SubscribeAppInfo) r3     // Catch:{ all -> 0x0050 }
            java.lang.String r4 = r3.getName()     // Catch:{ all -> 0x0050 }
            boolean r4 = r4.equals(r7)     // Catch:{ all -> 0x0050 }
            if (r4 == 0) goto L_0x0010
            int r4 = r3.getActualStatus()     // Catch:{ all -> 0x0050 }
            r5 = 2
            if (r4 == r5) goto L_0x0010
            r3.setActualStatus(r5)     // Catch:{ all -> 0x0050 }
            r1 = 1
        L_0x0031:
            if (r1 == 0) goto L_0x004e
            com.vivo.push.model.SubscribeAppInfo r7 = r6.getSubscribeAppInfo()     // Catch:{ all -> 0x0050 }
            if (r7 != 0) goto L_0x003b
            monitor-exit(r0)     // Catch:{ all -> 0x0050 }
            return
        L_0x003b:
            int r1 = r7.getActualStatus()     // Catch:{ all -> 0x0050 }
            int r7 = r7.getTargetStatus()     // Catch:{ all -> 0x0050 }
            if (r1 != r7) goto L_0x0049
            r6.clearData()     // Catch:{ all -> 0x0050 }
            goto L_0x004e
        L_0x0049:
            java.util.Set r7 = r6.mAppDatas     // Catch:{ all -> 0x0050 }
            r6.updateDataToSP(r7)     // Catch:{ all -> 0x0050 }
        L_0x004e:
            monitor-exit(r0)     // Catch:{ all -> 0x0050 }
            return
        L_0x0050:
            r7 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0050 }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.cache.impl.SubscribeAppAliasManagerImpl.delAliasSuccess(java.lang.String):void");
    }

    public SubscribeAppInfo getSubscribeAppInfo() {
        synchronized (sAppLock) {
            Iterator it = this.mAppDatas.iterator();
            if (!it.hasNext()) {
                return null;
            }
            SubscribeAppInfo subscribeAppInfo = (SubscribeAppInfo) it.next();
            return subscribeAppInfo;
        }
    }

    public SubscribeAppInfo getRetrySubscribeAppInfo() {
        SubscribeAppInfo subscribeAppInfo = getSubscribeAppInfo();
        if (subscribeAppInfo == null || subscribeAppInfo.getTargetStatus() == subscribeAppInfo.getActualStatus()) {
            return null;
        }
        return subscribeAppInfo;
    }
}
