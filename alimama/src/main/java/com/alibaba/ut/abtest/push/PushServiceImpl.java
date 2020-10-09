package com.alibaba.ut.abtest.push;

import android.util.Log;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.util.Analytics;
import com.alibaba.ut.abtest.internal.util.ClassUtils;
import com.alibaba.ut.abtest.internal.util.LogUtils;

public class PushServiceImpl implements PushService {
    private static final String TAG = "PushServiceImpl";
    private UTABPushClient pushClient;

    public boolean initialize(UTABPushConfiguration uTABPushConfiguration) {
        LogUtils.logD(TAG, "initialize.");
        try {
            synchronized (PushServiceImpl.class) {
                createPushClientIfNotExist();
                if (this.pushClient == null) {
                    return false;
                }
                this.pushClient.initialize(uTABPushConfiguration);
                return true;
            }
        } catch (Exception e) {
            LogUtils.logE(TAG, e.getMessage(), e);
            Analytics.commitFail(Analytics.SERVICE_ALARM, "PushServiceImpl.initialize", e.getMessage(), Log.getStackTraceString(e));
            return false;
        }
    }

    public boolean destory() {
        LogUtils.logD(TAG, "unbindService.");
        synchronized (PushServiceImpl.class) {
            if (this.pushClient != null) {
                this.pushClient.destory();
                this.pushClient = null;
            }
        }
        return true;
    }

    public void syncExperiments(boolean z) {
        LogUtils.logDAndReport(TAG, "检查Orange实验数据更新。forceUpdate=" + z + ",client=" + this.pushClient);
        if (this.pushClient != null) {
            this.pushClient.syncExperiments(z);
        }
    }

    public boolean isCrowd(String str) {
        LogUtils.logD(TAG, "isCrowd. pushClient=" + this.pushClient + ", crowdId=" + str);
        if (this.pushClient != null) {
            return this.pushClient.isCrowd(str);
        }
        return false;
    }

    public void syncWhitelist(boolean z) {
        LogUtils.logDAndReport(TAG, "检查Orange白名单数据更新. forceUpdate=" + z);
        if (this.pushClient != null) {
            this.pushClient.syncWhitelist(z);
        }
    }

    public void cancelSyncCrowd() {
        LogUtils.logD(TAG, "cancelSyncCrowd");
        if (this.pushClient != null) {
            this.pushClient.cancelSyncCrowd();
        }
    }

    private UTABPushClient createPushClientIfNotExist() {
        if (this.pushClient != null) {
            return this.pushClient;
        }
        Class<?> findClassIfExists = ClassUtils.findClassIfExists(ABConstants.BasicConstants.PUSHCLIENT_CLASSNAME, (ClassLoader) null);
        if (findClassIfExists == null) {
            return null;
        }
        try {
            this.pushClient = (UTABPushClient) findClassIfExists.newInstance();
            return this.pushClient;
        } catch (Exception e) {
            LogUtils.logE(TAG, e.getMessage(), e);
            return null;
        }
    }
}
