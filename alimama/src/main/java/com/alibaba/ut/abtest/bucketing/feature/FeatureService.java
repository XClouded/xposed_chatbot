package com.alibaba.ut.abtest.bucketing.feature;

public interface FeatureService {
    boolean isFeature(FeatureType featureType, String str);
}
