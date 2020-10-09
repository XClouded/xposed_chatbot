package com.alibaba.ut.abtest.internal.debug;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.ut.abtest.internal.util.LogUtils;

public class Debug implements Parcelable {
    public static final Parcelable.Creator<Debug> CREATOR = new Parcelable.Creator<Debug>() {
        public Debug createFromParcel(Parcel parcel) {
            Debug debug = new Debug();
            debug.debugKey = parcel.readString();
            debug.debugSamplingOption = parcel.readString();
            return debug;
        }

        public Debug[] newArray(int i) {
            return new Debug[i];
        }
    };
    private static final String TAG = "Debug";
    @JSONField(name = "debug_key")
    public String debugKey;
    @JSONField(name = "debug_sampling_option")
    public String debugSamplingOption;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeString(this.debugKey);
            parcel.writeString(this.debugSamplingOption);
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
    }
}
