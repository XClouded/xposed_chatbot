package com.taobao.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AliMonitorMeasure implements Parcelable {
    public static final Parcelable.Creator<AliMonitorMeasure> CREATOR = new Parcelable.Creator<AliMonitorMeasure>() {
        public AliMonitorMeasure createFromParcel(Parcel parcel) {
            return AliMonitorMeasure.readFromParcel(parcel);
        }

        public AliMonitorMeasure[] newArray(int i) {
            return new AliMonitorMeasure[i];
        }
    };
    private static final String TAG = "AliMonitorMeasure";
    private static List<Double> nullList = new ArrayList(1);
    private List<Double> bounds;
    protected Double constantValue;
    public String name;

    public int describeContents() {
        return 0;
    }

    AliMonitorMeasure() {
        this.constantValue = Double.valueOf(0.0d);
    }

    static {
        nullList.add((Object) null);
    }

    public AliMonitorMeasure(String str) {
        this(str, Double.valueOf(0.0d));
    }

    public AliMonitorMeasure(String str, Double d, List<Double> list) {
        double d2 = 0.0d;
        this.constantValue = Double.valueOf(0.0d);
        if (list != null) {
            if (list.removeAll(nullList)) {
                Log.w(TAG, "bounds entity must not be null");
            }
            Collections.sort(list);
            this.bounds = list;
        }
        this.name = str;
        this.constantValue = Double.valueOf(d != null ? d.doubleValue() : d2);
    }

    public AliMonitorMeasure(String str, Double d) {
        this(str, d, (Double) null, (Double) null);
    }

    public AliMonitorMeasure(String str, Double d, Double d2, Double d3) {
        this(str, d, (List<Double>) null);
        if (d2 != null || d3 != null) {
            setRange(d2, d3);
        }
    }

    public void setRange(Double d, Double d2) {
        if (d == null || d2 == null) {
            Log.w(TAG, "min or max must not be null");
            return;
        }
        this.bounds = new ArrayList(2);
        this.bounds.add(d);
        this.bounds.add(d2);
    }

    public Double getMin() {
        if (this.bounds == null || this.bounds.size() < 1) {
            return null;
        }
        return this.bounds.get(0);
    }

    public Double getMax() {
        if (this.bounds == null || this.bounds.size() < 2) {
            return null;
        }
        return this.bounds.get(this.bounds.size() - 1);
    }

    public List<Double> getBounds() {
        return this.bounds;
    }

    public String getName() {
        return this.name;
    }

    public Double getConstantValue() {
        return this.constantValue;
    }

    public void setConstantValue(Double d) {
        this.constantValue = d;
    }

    public boolean valid(AliMonitorMeasureValue aliMonitorMeasureValue) {
        Double valueOf = Double.valueOf(aliMonitorMeasureValue.getValue());
        return valueOf != null && (getMin() == null || valueOf.doubleValue() >= getMin().doubleValue()) && (getMax() == null || valueOf.doubleValue() < getMax().doubleValue());
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
        AliMonitorMeasure aliMonitorMeasure = (AliMonitorMeasure) obj;
        if (this.name == null) {
            if (aliMonitorMeasure.name != null) {
                return false;
            }
        } else if (!this.name.equals(aliMonitorMeasure.name)) {
            return false;
        }
        return true;
    }

    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeList(this.bounds);
            parcel.writeString(this.name);
            parcel.writeInt(this.constantValue == null ? 0 : 1);
            if (this.constantValue != null) {
                parcel.writeDouble(this.constantValue.doubleValue());
            }
        } catch (Throwable th) {
            Log.e(TAG, "writeToParcel", th);
        }
    }

    static AliMonitorMeasure readFromParcel(Parcel parcel) {
        try {
            return new AliMonitorMeasure(parcel.readString(), !(parcel.readInt() == 0) ? Double.valueOf(parcel.readDouble()) : null, parcel.readArrayList(AliMonitorMeasure.class.getClassLoader()));
        } catch (Throwable th) {
            th.printStackTrace();
            Log.e(TAG, "readFromParcel", th);
            return null;
        }
    }
}
