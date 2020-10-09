package com.alibaba.ut.abtest.bucketing.feature;

import com.alibaba.ut.abtest.UTABMethod;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.util.LogUtils;

public class FeatureServiceImpl implements FeatureService {
    private static final String TAG = "FeatureServiceImpl";
    private static final long serialVersionUID = 8550625826350992277L;

    public boolean isFeature(FeatureType featureType, String str) {
        if (featureType != FeatureType.Crowd) {
            return false;
        }
        if (ABContext.getInstance().getCurrentApiMethod() == UTABMethod.Push) {
            return ABContext.getInstance().getPushService().isCrowd(str);
        }
        LogUtils.logE(TAG, "警告：拉模式出现人群判断！人群ID=" + str);
        return false;
    }
}
