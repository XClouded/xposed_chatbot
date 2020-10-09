package com.alibaba.ut.abtest.track;

import com.alibaba.ut.abtest.bucketing.decision.DebugTrack;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentActivateGroup;
import java.util.Map;

public interface TrackService {
    void addActivateExperimentGroup(ExperimentActivateGroup experimentActivateGroup, Object obj);

    boolean addActivateServerExperimentGroup(String str, Object obj);

    String getAppActivateTrackId();

    String getPageActivateTrackId(String str);

    int[] getSubscribeUTEventIds();

    TrackId getTrackId(String str, int i, String str2, String str3, String str4, Map<String, String> map, String str5);

    String getTrackUtParam(TrackId trackId, int i, Map<String, String> map);

    void removeActivateExperiment(String str);

    void traceActivate(ExperimentActivateGroup experimentActivateGroup, DebugTrack debugTrack);

    void traceActivateNotSend();
}
