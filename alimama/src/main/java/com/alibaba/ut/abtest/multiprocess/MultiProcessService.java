package com.alibaba.ut.abtest.multiprocess;

import android.os.Bundle;
import com.alibaba.ut.abtest.VariationSet;
import com.alibaba.ut.abtest.internal.debug.Debug;
import java.util.Map;

public interface MultiProcessService {
    boolean addActivateServerExperimentGroup(String str, Object obj);

    String getAppActivateTrackId();

    VariationSet getVariations(String str, String str2, Map<String, Object> map, boolean z, Object obj);

    boolean initialize();

    void reportLog(String str, String str2, String str3, String str4);

    void sendMsgToAllSubProcess(int i, Bundle bundle);

    void startRealTimeDebug(Debug debug);
}
