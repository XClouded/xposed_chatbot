package com.alibaba.ut.abtest.internal.bucketing;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.ut.abtest.Variation;
import com.alibaba.ut.abtest.VariationSet;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentActivateGroup;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultVariationSet implements VariationSet {
    public static final Parcelable.Creator<DefaultVariationSet> CREATOR = new Parcelable.Creator<DefaultVariationSet>() {
        public DefaultVariationSet createFromParcel(Parcel parcel) {
            try {
                return new DefaultVariationSet(parcel);
            } catch (Throwable th) {
                LogUtils.logW(DefaultVariationSet.TAG, th.getMessage(), th);
                return null;
            }
        }

        public DefaultVariationSet[] newArray(int i) {
            return new DefaultVariationSet[i];
        }
    };
    private static final String TAG = "DefaultVariationSet";
    private long experimentGroupId;
    private long experimentId;
    private long experimentReleaseId;
    private Map<String, Variation> variations;

    public int describeContents() {
        return 0;
    }

    protected DefaultVariationSet(Parcel parcel) {
        this.experimentId = parcel.readLong();
        this.experimentReleaseId = parcel.readLong();
        this.experimentGroupId = parcel.readLong();
        this.variations = parcel.readHashMap(DefaultVariationSet.class.getClassLoader());
    }

    public DefaultVariationSet(ExperimentActivateGroup experimentActivateGroup) {
        HashMap hashMap = new HashMap();
        if (experimentActivateGroup != null) {
            if (experimentActivateGroup.getGroups() != null && !experimentActivateGroup.getGroups().isEmpty()) {
                this.experimentId = experimentActivateGroup.getGroups().get(0).getExperimentId();
                this.experimentReleaseId = experimentActivateGroup.getGroups().get(0).getReleaseId();
                this.experimentGroupId = experimentActivateGroup.getGroups().get(0).getId();
            }
            if (experimentActivateGroup.getVariations() != null) {
                for (Map.Entry next : experimentActivateGroup.getVariations().entrySet()) {
                    hashMap.put(next.getKey(), new DefaultVariation((String) next.getKey(), (String) next.getValue()));
                }
            }
        }
        this.variations = Collections.unmodifiableMap(hashMap);
    }

    @Deprecated
    public long getExperimentBucketId() {
        return this.experimentGroupId;
    }

    public long getExperimentId() {
        return this.experimentId;
    }

    public long getExperimentReleaseId() {
        return this.experimentReleaseId;
    }

    public Variation getVariation(String str) {
        return this.variations.get(str);
    }

    public boolean contains(String str) {
        return this.variations.containsKey(str);
    }

    public Iterator<Variation> iterator() {
        return this.variations.values().iterator();
    }

    public int size() {
        return this.variations.size();
    }

    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeLong(this.experimentId);
            parcel.writeLong(this.experimentReleaseId);
            parcel.writeLong(this.experimentGroupId);
            parcel.writeMap(this.variations);
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
    }
}
