package com.alibaba.ut.abtest.internal.bucketing;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.ut.abtest.Variation;
import com.alibaba.ut.abtest.internal.util.LogUtils;

public class DefaultVariation implements Variation {
    public static final Parcelable.Creator<DefaultVariation> CREATOR = new Parcelable.Creator<DefaultVariation>() {
        public DefaultVariation createFromParcel(Parcel parcel) {
            return new DefaultVariation(parcel.readString(), parcel.readString());
        }

        public DefaultVariation[] newArray(int i) {
            return new DefaultVariation[i];
        }
    };
    private static final String TAG = "DefaultVariation";
    private final String name;
    private final String value;

    public int describeContents() {
        return 0;
    }

    public DefaultVariation(String str, String str2) {
        this.name = str;
        this.value = str2;
    }

    public String getName() {
        return this.name;
    }

    public int getValueAsInt(int i) {
        if (this.value == null || this.value.length() == 0) {
            return i;
        }
        try {
            return Integer.parseInt(this.value);
        } catch (Exception e) {
            LogUtils.logWAndReport(TAG, "变量 '" + this.name + "' 不能转换成 int 类型，变量值：" + this.value, e);
            return i;
        }
    }

    public long getValueAsLong(long j) {
        if (this.value == null || this.value.length() == 0) {
            return j;
        }
        try {
            return Long.parseLong(this.value);
        } catch (Exception e) {
            LogUtils.logWAndReport(TAG, "变量 '" + this.name + "' 不能转换成 long 类型，变量值：" + this.value, e);
            return j;
        }
    }

    public short getValueAsShort(short s) {
        if (this.value == null || this.value.length() == 0) {
            return s;
        }
        try {
            return Short.parseShort(this.value);
        } catch (Exception e) {
            LogUtils.logWAndReport(TAG, "变量 '" + this.name + "' 不能转换成 short 类型，变量值：" + this.value, e);
            return s;
        }
    }

    public double getValueAsDouble(double d) {
        if (this.value == null || this.value.length() == 0) {
            return d;
        }
        try {
            return Double.parseDouble(this.value);
        } catch (Exception e) {
            LogUtils.logWAndReport(TAG, "变量 '" + this.name + "' 不能转换成 double 类型，变量值：" + this.value, e);
            return d;
        }
    }

    public float getValueAsFloat(float f) {
        if (this.value == null || this.value.length() == 0) {
            return f;
        }
        try {
            return Float.parseFloat(this.value);
        } catch (Exception e) {
            LogUtils.logWAndReport(TAG, "变量 '" + this.name + "' 不能转换成 float 类型，变量值：" + this.value, e);
            return f;
        }
    }

    public String getValueAsString(String str) {
        return (this.value == null || this.value.length() == 0) ? str : this.value;
    }

    public boolean getValueAsBoolean(boolean z) {
        if (this.value == null || this.value.length() == 0) {
            return z;
        }
        try {
            return Boolean.parseBoolean(this.value);
        } catch (Exception e) {
            LogUtils.logWAndReport(TAG, "变量 '" + this.name + "' 不能转换成 boolean 类型，变量值：" + this.value, e);
            return z;
        }
    }

    public Object getValue(Object obj) {
        return this.value != null ? this.value : obj;
    }

    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeString(this.name);
            parcel.writeString(this.value);
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
    }
}
