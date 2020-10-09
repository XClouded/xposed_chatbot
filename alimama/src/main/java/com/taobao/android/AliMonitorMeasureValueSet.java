package com.taobao.android;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AliMonitorMeasureValueSet implements AliMonitorIMerge<AliMonitorMeasureValueSet>, AliMonitorReusable, Parcelable {
    public static final Parcelable.Creator<AliMonitorMeasureValueSet> CREATOR = new Parcelable.Creator<AliMonitorMeasureValueSet>() {
        public AliMonitorMeasureValueSet createFromParcel(Parcel parcel) {
            return AliMonitorMeasureValueSet.readFromParcel(parcel);
        }

        public AliMonitorMeasureValueSet[] newArray(int i) {
            return new AliMonitorMeasureValueSet[i];
        }
    };
    private Map<String, AliMonitorMeasureValue> map = new LinkedHashMap();

    public int describeContents() {
        return 0;
    }

    public static AliMonitorMeasureValueSet create() {
        return (AliMonitorMeasureValueSet) AliMonitorBalancedPool.getInstance().poll(AliMonitorMeasureValueSet.class, new Object[0]);
    }

    @Deprecated
    public static AliMonitorMeasureValueSet create(int i) {
        return (AliMonitorMeasureValueSet) AliMonitorBalancedPool.getInstance().poll(AliMonitorMeasureValueSet.class, new Object[0]);
    }

    public static AliMonitorMeasureValueSet create(Map<String, Double> map2) {
        AliMonitorMeasureValueSet aliMonitorMeasureValueSet = (AliMonitorMeasureValueSet) AliMonitorBalancedPool.getInstance().poll(AliMonitorMeasureValueSet.class, new Object[0]);
        if (map2 != null) {
            for (String next : map2.keySet()) {
                Double d = map2.get(next);
                if (d != null) {
                    aliMonitorMeasureValueSet.map.put(next, AliMonitorBalancedPool.getInstance().poll(AliMonitorMeasureValue.class, d));
                }
            }
        }
        return aliMonitorMeasureValueSet;
    }

    public static AliMonitorMeasureValueSet fromStringMap(Map<String, String> map2) {
        AliMonitorMeasureValueSet aliMonitorMeasureValueSet = (AliMonitorMeasureValueSet) AliMonitorBalancedPool.getInstance().poll(AliMonitorMeasureValueSet.class, new Object[0]);
        if (map2 != null) {
            for (Map.Entry next : map2.entrySet()) {
                Double d = toDouble((String) next.getValue());
                if (d != null) {
                    aliMonitorMeasureValueSet.map.put(next.getKey(), AliMonitorBalancedPool.getInstance().poll(AliMonitorMeasureValue.class, d));
                }
            }
        }
        return aliMonitorMeasureValueSet;
    }

    private static Double toDouble(String str) {
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AliMonitorMeasureValueSet setValue(String str, double d) {
        this.map.put(str, AliMonitorBalancedPool.getInstance().poll(AliMonitorMeasureValue.class, Double.valueOf(d)));
        return this;
    }

    public void setValue(String str, AliMonitorMeasureValue aliMonitorMeasureValue) {
        this.map.put(str, aliMonitorMeasureValue);
    }

    public AliMonitorMeasureValue getValue(String str) {
        return this.map.get(str);
    }

    public Map<String, AliMonitorMeasureValue> getMap() {
        return this.map;
    }

    public void setMap(Map<String, AliMonitorMeasureValue> map2) {
        this.map = map2;
    }

    public boolean containValue(String str) {
        return this.map.containsKey(str);
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public void merge(AliMonitorMeasureValueSet aliMonitorMeasureValueSet) {
        for (String next : this.map.keySet()) {
            this.map.get(next).merge(aliMonitorMeasureValueSet.getValue(next));
        }
    }

    public void clean() {
        for (AliMonitorMeasureValue offer : this.map.values()) {
            AliMonitorBalancedPool.getInstance().offer(offer);
        }
        this.map.clear();
    }

    public void fill(Object... objArr) {
        if (this.map == null) {
            this.map = new LinkedHashMap();
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeMap(this.map);
    }

    static AliMonitorMeasureValueSet readFromParcel(Parcel parcel) {
        try {
            AliMonitorMeasureValueSet create = create();
            try {
                create.map = parcel.readHashMap(AliMonitorDimensionValueSet.class.getClassLoader());
                return create;
            } catch (Throwable unused) {
                return create;
            }
        } catch (Throwable unused2) {
            return null;
        }
    }

    public void setBuckets(List<AliMonitorMeasure> list) {
        if (list != null) {
            for (String next : this.map.keySet()) {
                this.map.get(next).setBuckets(getMeasure(list, next));
            }
        }
    }

    private AliMonitorMeasure getMeasure(List<AliMonitorMeasure> list, String str) {
        for (AliMonitorMeasure next : list) {
            if (str.equalsIgnoreCase(next.getName())) {
                return next;
            }
        }
        return null;
    }
}
