package com.alibaba.ut.abtest.internal.debug;

import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup;
import java.util.Map;

public interface DebugService {
    Long getWhitelistGroupIdByLayerId(long j);

    boolean isWhitelistExperimentGroup(ExperimentGroup experimentGroup);

    void reportLog(int i, String str, String str2, String str3, String str4);

    void setLogMaxReportSize(int i);

    void setWhitelist(Map<Long, Long> map);

    void startRealTimeDebug(Debug debug);
}
