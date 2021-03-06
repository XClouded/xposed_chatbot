package com.vivo.push.cache.impl;

import android.content.Context;
import android.text.TextUtils;
import com.vivo.push.cache.c;
import com.vivo.push.model.SubscribeAppInfo;
import com.vivo.push.util.p;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* compiled from: BaseSubscribeAppManager */
public abstract class a extends c<SubscribeAppInfo> {
    private static final String SPILTE_TAG = "@#";
    private static final String TAG = "SubscribeAppInfoManager";

    public a(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void loadData() {
        super.loadData();
        synchronized (sAppLock) {
            boolean z = false;
            Iterator it = this.mAppDatas.iterator();
            while (it.hasNext()) {
                SubscribeAppInfo subscribeAppInfo = (SubscribeAppInfo) it.next();
                if (subscribeAppInfo.getTargetStatus() == 2 && subscribeAppInfo.getActualStatus() == 2) {
                    it.remove();
                    z = true;
                }
            }
            if (z) {
                updateDataToSP(this.mAppDatas);
            }
        }
    }

    public Set<SubscribeAppInfo> parseAppStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        HashSet hashSet = new HashSet();
        if (!TextUtils.isEmpty(str)) {
            for (String trim : str.trim().split(SPILTE_TAG)) {
                String[] split = trim.trim().trim().split(",");
                if (split.length >= 3) {
                    try {
                        hashSet.add(new SubscribeAppInfo(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
                    } catch (Exception e) {
                        p.d(TAG, "str2Clients E: " + e);
                    }
                }
            }
        }
        return hashSet;
    }

    public void updateSubscribeApp(SubscribeAppInfo subscribeAppInfo) {
        synchronized (sAppLock) {
            boolean z = false;
            if (!TextUtils.isEmpty(subscribeAppInfo.getName())) {
                Iterator it = this.mAppDatas.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SubscribeAppInfo subscribeAppInfo2 = (SubscribeAppInfo) it.next();
                    if (subscribeAppInfo2.getName().equals(subscribeAppInfo.getName())) {
                        subscribeAppInfo2.setTargetStatus(subscribeAppInfo.getTargetStatus());
                        subscribeAppInfo2.setActualStatus(subscribeAppInfo.getActualStatus());
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

    public String toAppStr(Set<SubscribeAppInfo> set) {
        if (set == null || set.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (SubscribeAppInfo next : set) {
            stringBuffer.append(next.getName());
            stringBuffer.append(",");
            stringBuffer.append(next.getTargetStatus());
            stringBuffer.append(",");
            stringBuffer.append(next.getActualStatus());
            stringBuffer.append(SPILTE_TAG);
        }
        return stringBuffer.substring(0, stringBuffer.length() - 2);
    }

    public SubscribeAppInfo getConfigByName(String str) {
        synchronized (sAppLock) {
            for (SubscribeAppInfo subscribeAppInfo : this.mAppDatas) {
                if (!TextUtils.isEmpty(subscribeAppInfo.getName()) && subscribeAppInfo.getName().equals(str)) {
                    return subscribeAppInfo;
                }
            }
            return null;
        }
    }
}
