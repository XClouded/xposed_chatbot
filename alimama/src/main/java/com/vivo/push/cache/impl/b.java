package com.vivo.push.cache.impl;

import android.content.Context;
import com.vivo.push.PushClientConstants;
import com.vivo.push.cache.ISubscribeAppTagManager;
import com.vivo.push.model.SubscribeAppInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* compiled from: SubscribeAppTagManagerImpl */
public final class b extends a implements ISubscribeAppTagManager {
    /* access modifiers changed from: protected */
    public final String generateStrByType() {
        return PushClientConstants.PUSH_APP_TAGS;
    }

    public b(Context context) {
        super(context);
    }

    public final boolean setTags(Set<String> set) {
        boolean z = false;
        if (set == null) {
            return false;
        }
        synchronized (sAppLock) {
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String next = it.next();
                Iterator it2 = this.mAppDatas.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    SubscribeAppInfo subscribeAppInfo = (SubscribeAppInfo) it2.next();
                    if (subscribeAppInfo.getName().equals(next)) {
                        if (subscribeAppInfo.getTargetStatus() == 1) {
                            it.remove();
                            break;
                        }
                        subscribeAppInfo.setTargetStatus(1);
                        subscribeAppInfo.setActualStatus(2);
                        z = true;
                    }
                }
            }
            HashSet hashSet = new HashSet();
            for (String subscribeAppInfo2 : set) {
                hashSet.add(new SubscribeAppInfo(subscribeAppInfo2, 1, 2));
            }
            if (hashSet.size() > 0) {
                addDatas(hashSet);
                z = true;
            } else if (z) {
                updateDataToSP(this.mAppDatas);
            }
        }
        return z;
    }

    public final boolean delTags(Set<String> set) {
        boolean z = false;
        if (set == null) {
            return false;
        }
        synchronized (sAppLock) {
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String next = it.next();
                Iterator it2 = this.mAppDatas.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    SubscribeAppInfo subscribeAppInfo = (SubscribeAppInfo) it2.next();
                    if (subscribeAppInfo.getName().equals(next)) {
                        if (subscribeAppInfo.getTargetStatus() == 2) {
                            it.remove();
                            break;
                        }
                        subscribeAppInfo.setTargetStatus(2);
                        subscribeAppInfo.setActualStatus(1);
                        z = true;
                    }
                }
            }
            HashSet hashSet = new HashSet();
            for (String subscribeAppInfo2 : set) {
                hashSet.add(new SubscribeAppInfo(subscribeAppInfo2, 2, 1));
            }
            if (hashSet.size() > 0) {
                addDatas(hashSet);
                z = true;
            } else if (z) {
                updateDataToSP(this.mAppDatas);
            }
        }
        return z;
    }

    public final void setTagsSuccess(Set<String> set) {
        synchronized (sAppLock) {
            boolean z = false;
            for (String next : set) {
                for (SubscribeAppInfo subscribeAppInfo : this.mAppDatas) {
                    if (next.equals(subscribeAppInfo.getName()) && subscribeAppInfo.getActualStatus() != 1) {
                        subscribeAppInfo.setActualStatus(1);
                        z = true;
                    }
                }
            }
            if (z) {
                updateDataToSP(this.mAppDatas);
            }
        }
    }

    public final void delTagsSuccess(Set<String> set) {
        synchronized (sAppLock) {
            boolean z = false;
            for (String next : set) {
                Iterator it = this.mAppDatas.iterator();
                while (it.hasNext()) {
                    SubscribeAppInfo subscribeAppInfo = (SubscribeAppInfo) it.next();
                    if (next.equals(subscribeAppInfo.getName()) && subscribeAppInfo.getActualStatus() != 2) {
                        if (subscribeAppInfo.getTargetStatus() == 2) {
                            it.remove();
                        } else {
                            subscribeAppInfo.setActualStatus(2);
                        }
                        z = true;
                    }
                }
            }
            if (z) {
                updateDataToSP(this.mAppDatas);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public ArrayList<String> getSubscribeTags() {
        ArrayList<String> arrayList;
        synchronized (sAppLock) {
            arrayList = new ArrayList<>();
            for (SubscribeAppInfo subscribeAppInfo : this.mAppDatas) {
                if (subscribeAppInfo != null && subscribeAppInfo.getTargetStatus() == 1) {
                    arrayList.add(subscribeAppInfo.getName());
                }
            }
        }
        return arrayList;
    }

    public final ArrayList<String> getRetrySubscribeAppInfo() {
        ArrayList<String> arrayList;
        synchronized (sAppLock) {
            arrayList = new ArrayList<>();
            for (SubscribeAppInfo subscribeAppInfo : this.mAppDatas) {
                if (!(subscribeAppInfo == null || subscribeAppInfo.getTargetStatus() != 1 || subscribeAppInfo.getTargetStatus() == subscribeAppInfo.getActualStatus())) {
                    arrayList.add(subscribeAppInfo.getName());
                }
            }
        }
        return arrayList;
    }

    public final ArrayList<String> getRetryUnsubscribeAppInfo() {
        ArrayList<String> arrayList;
        synchronized (sAppLock) {
            arrayList = new ArrayList<>();
            for (SubscribeAppInfo subscribeAppInfo : this.mAppDatas) {
                if (!(subscribeAppInfo == null || subscribeAppInfo.getTargetStatus() != 2 || subscribeAppInfo.getTargetStatus() == subscribeAppInfo.getActualStatus())) {
                    arrayList.add(subscribeAppInfo.getName());
                }
            }
        }
        return arrayList;
    }
}
