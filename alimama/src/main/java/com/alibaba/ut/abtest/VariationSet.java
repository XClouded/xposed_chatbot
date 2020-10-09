package com.alibaba.ut.abtest;

import android.os.Parcelable;
import androidx.annotation.Keep;
import java.util.Iterator;

@Keep
public interface VariationSet extends Parcelable {
    boolean contains(String str);

    @Deprecated
    long getExperimentBucketId();

    @Deprecated
    long getExperimentId();

    @Deprecated
    long getExperimentReleaseId();

    Variation getVariation(String str);

    Iterator<Variation> iterator();

    int size();
}
