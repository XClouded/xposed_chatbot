package com.alibaba.ut.abtest.push;

public interface PushService {
    void cancelSyncCrowd();

    boolean destory();

    boolean initialize(UTABPushConfiguration uTABPushConfiguration);

    boolean isCrowd(String str);

    void syncExperiments(boolean z);

    void syncWhitelist(boolean z);
}
