package com.alibaba.ut.abtest.multiprocess;

import android.os.Bundle;
import com.alibaba.ut.abtest.VariationSet;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.debug.Debug;
import java.util.Map;

public class UTABMultiProcessClientDefault implements UTABMultiProcessClient {
    private static final String TAG = "UTABMultiProcessClientDefault";

    public void initialize() {
    }

    public void sendMsgToAllSubProcess(int i, Bundle bundle) {
    }

    public VariationSet getVariations(String str, String str2, Map<String, Object> map, boolean z, Object obj) {
        return ABContext.getInstance().getDecisionService().getVariations(str, str2, map, z, obj);
    }

    public boolean addActivateServerExperimentGroup(String str, Object obj) {
        return ABContext.getInstance().getTrackService().addActivateServerExperimentGroup(str, obj);
    }

    public void startRealTimeDebug(Debug debug) {
        ABContext.getInstance().getDebugService().startRealTimeDebug(debug);
    }

    public String getAppActivateTrackId() {
        return ABContext.getInstance().getTrackService().getAppActivateTrackId();
    }

    public void reportLog(String str, String str2, String str3, String str4) {
        ABContext.getInstance().getDebugService().reportLog(0, str, str2, str3, str4);
    }
}
