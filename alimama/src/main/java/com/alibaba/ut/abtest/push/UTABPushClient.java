package com.alibaba.ut.abtest.push;

public interface UTABPushClient {
    void cancelSyncCrowd();

    void destory();

    void initialize(UTABPushConfiguration uTABPushConfiguration);

    boolean isCrowd(String str);

    void syncExperiments(boolean z);

    void syncWhitelist(boolean z);
}
