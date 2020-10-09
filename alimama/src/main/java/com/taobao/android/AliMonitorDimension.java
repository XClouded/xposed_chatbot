package com.taobao.android;

import android.os.Parcel;
import android.os.Parcelable;

public class AliMonitorDimension implements Parcelable {
    public static final Parcelable.Creator<AliMonitorDimension> CREATOR = new Parcelable.Creator<AliMonitorDimension>() {
        public AliMonitorDimension createFromParcel(Parcel parcel) {
            return AliMonitorDimension.readFromParcel(parcel);
        }

        public AliMonitorDimension[] newArray(int i) {
            return new AliMonitorDimension[i];
        }
    };
    static final String DEFAULT_NULL_VALUE = "null";
    protected String constantValue;
    protected String name;

    public int describeContents() {
        return 0;
    }

    public AliMonitorDimension() {
        this.constantValue = "null";
    }

    public AliMonitorDimension(String str) {
        this(str, (String) null);
    }

    public AliMonitorDimension(String str, String str2) {
        this.constantValue = "null";
        this.name = str;
        this.constantValue = str2 == null ? "null" : str2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getConstantValue() {
        return this.constantValue;
    }

    public void setConstantValue(String str) {
        this.constantValue = str;
    }

    public int hashCode() {
        return 31 + (this.name == null ? 0 : this.name.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AliMonitorDimension aliMonitorDimension = (AliMonitorDimension) obj;
        if (this.name == null) {
            if (aliMonitorDimension.name != null) {
                return false;
            }
        } else if (!this.name.equals(aliMonitorDimension.name)) {
            return false;
        }
        return true;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.constantValue);
        parcel.writeString(this.name);
    }

    static AliMonitorDimension readFromParcel(Parcel parcel) {
        try {
            return new AliMonitorDimension(parcel.readString(), parcel.readString());
        } catch (Throwable unused) {
            return null;
        }
    }
}
