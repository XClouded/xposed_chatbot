package com.alibaba.ut.abtest.bucketing.decision;

import com.alibaba.ut.abtest.UTABDataListener;
import com.alibaba.ut.abtest.VariationSet;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroupPO;
import java.util.List;
import java.util.Map;

public interface DecisionService {
    void addDataListener(String str, String str2, UTABDataListener uTABDataListener);

    void clearExperimentsCache();

    String getExperimentDataSignature();

    long getExperimentDataVersion();

    Long getExperimentId(long j);

    long getLastRequestTimestamp();

    VariationSet getVariations(String str, String str2, Map<String, Object> map, boolean z, Object obj);

    void initialize();

    void removeDataListener(String str, String str2, UTABDataListener uTABDataListener);

    void saveBetaExperiments(List<ExperimentGroupPO> list);

    void saveExperiments(List<ExperimentGroupPO> list, long j, String str);

    void syncExperiments(boolean z);
}
