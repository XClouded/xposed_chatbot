package com.alibaba.ut.abtest.config;

import com.alibaba.ut.abtest.UTABMethod;

public interface ConfigService {
    long getDownloadExperimentDataDelayTime();

    UTABMethod getMethod();

    long getRequestExperimentDataIntervalTime();

    int getSyncCrowdDelayed();

    void initialize();

    boolean isDataTriggerEnabled();

    boolean isNavEnabled();

    boolean isNavIgnored(String str);

    boolean isSdkDowngrade();

    boolean isSdkEnabled();

    boolean isTrack1022ExperimentDisabled(Long l);

    boolean isTrack1022GroupDisabled(Long l);

    boolean isTrackAutoEnabled();

    void setMethod(UTABMethod uTABMethod);

    void setSdkDowngrade(boolean z);
}
