package com.alibaba.mtl.appmonitor.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.Reusable;
import com.taobao.weex.BuildConfig;
import java.util.LinkedHashMap;
import java.util.Map;

public class DimensionValueSet implements Reusable, Parcelable {
    public static final Parcelable.Creator<DimensionValueSet> CREATOR = new Parcelable.Creator<DimensionValueSet>() {
        public DimensionValueSet createFromParcel(Parcel parcel) {
            return DimensionValueSet.readFromParcel(parcel);
        }

        public DimensionValueSet[] newArray(int i) {
            return new DimensionValueSet[i];
        }
    };
    protected Map<String, String> map;

    public int describeContents() {
        return 0;
    }

    public static DimensionValueSet create() {
        return (DimensionValueSet) BalancedPool.getInstance().poll(DimensionValueSet.class, new Object[0]);
    }

    @Deprecated
    public static DimensionValueSet create(int i) {
        return (DimensionValueSet) BalancedPool.getInstance().poll(DimensionValueSet.class, new Object[0]);
    }

    @Deprecated
    public DimensionValueSet() {
        if (this.map == null) {
            this.map = new LinkedHashMap();
        }
    }

    public static DimensionValueSet fromStringMap(Map<String, String> map2) {
        DimensionValueSet dimensionValueSet = (DimensionValueSet) BalancedPool.getInstance().poll(DimensionValueSet.class, new Object[0]);
        for (Map.Entry next : map2.entrySet()) {
            dimensionValueSet.map.put(next.getKey(), next.getValue() != null ? (String) next.getValue() : BuildConfig.buildJavascriptFrameworkVersion);
        }
        return dimensionValueSet;
    }

    public DimensionValueSet setValue(String str, String str2) {
        Map<String, String> map2 = this.map;
        if (str2 == null) {
            str2 = BuildConfig.buildJavascriptFrameworkVersion;
        }
        map2.put(str, str2);
        return this;
    }

    public DimensionValueSet addValues(DimensionValueSet dimensionValueSet) {
        Map<String, String> map2;
        if (!(dimensionValueSet == null || (map2 = dimensionValueSet.getMap()) == null)) {
            for (Map.Entry next : map2.entrySet()) {
                this.map.put(next.getKey(), next.getValue() != null ? (String) next.getValue() : BuildConfig.buildJavascriptFrameworkVersion);
            }
        }
        return this;
    }

    public boolean containValue(String str) {
        return this.map.containsKey(str);
    }

    public String getValue(String str) {
        return this.map.get(str);
    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public void setMap(Map<String, String> map2) {
        for (Map.Entry next : map2.entrySet()) {
            this.map.put(next.getKey(), next.getValue() != null ? (String) next.getValue() : BuildConfig.buildJavascriptFrameworkVersion);
        }
    }

    public int hashCode() {
        return 31 + (this.map == null ? 0 : this.map.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DimensionValueSet dimensionValueSet = (DimensionValueSet) obj;
        if (this.map == null) {
            if (dimensionValueSet.map != null) {
                return false;
            }
        } else if (!this.map.equals(dimensionValueSet.map)) {
            return false;
        }
        return true;
    }

    public void clean() {
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

    static DimensionValueSet readFromParcel(Parcel parcel) {
        DimensionValueSet dimensionValueSet;
        try {
            dimensionValueSet = create();
            try {
                dimensionValueSet.map = parcel.readHashMap(DimensionValueSet.class.getClassLoader());
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            dimensionValueSet = null;
            th.printStackTrace();
            return dimensionValueSet;
        }
        return dimensionValueSet;
    }
}
