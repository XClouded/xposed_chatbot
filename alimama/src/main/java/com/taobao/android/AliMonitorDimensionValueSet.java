package com.taobao.android;

import android.os.Parcel;
import android.os.Parcelable;
import com.taobao.weex.BuildConfig;
import java.util.LinkedHashMap;
import java.util.Map;

public class AliMonitorDimensionValueSet implements AliMonitorReusable, Parcelable {
    public static final Parcelable.Creator<AliMonitorDimensionValueSet> CREATOR = new Parcelable.Creator<AliMonitorDimensionValueSet>() {
        public AliMonitorDimensionValueSet createFromParcel(Parcel parcel) {
            return AliMonitorDimensionValueSet.readFromParcel(parcel);
        }

        public AliMonitorDimensionValueSet[] newArray(int i) {
            return new AliMonitorDimensionValueSet[i];
        }
    };
    protected Map<String, String> map;

    public int describeContents() {
        return 0;
    }

    public static AliMonitorDimensionValueSet create() {
        return (AliMonitorDimensionValueSet) AliMonitorBalancedPool.getInstance().poll(AliMonitorDimensionValueSet.class, new Object[0]);
    }

    @Deprecated
    public static AliMonitorDimensionValueSet create(int i) {
        return (AliMonitorDimensionValueSet) AliMonitorBalancedPool.getInstance().poll(AliMonitorDimensionValueSet.class, new Object[0]);
    }

    @Deprecated
    public AliMonitorDimensionValueSet() {
        if (this.map == null) {
            this.map = new LinkedHashMap();
        }
    }

    public static AliMonitorDimensionValueSet fromStringMap(Map<String, String> map2) {
        AliMonitorDimensionValueSet aliMonitorDimensionValueSet = (AliMonitorDimensionValueSet) AliMonitorBalancedPool.getInstance().poll(AliMonitorDimensionValueSet.class, new Object[0]);
        for (Map.Entry next : map2.entrySet()) {
            aliMonitorDimensionValueSet.map.put(next.getKey(), next.getValue() != null ? (String) next.getValue() : BuildConfig.buildJavascriptFrameworkVersion);
        }
        return aliMonitorDimensionValueSet;
    }

    public AliMonitorDimensionValueSet setValue(String str, String str2) {
        Map<String, String> map2 = this.map;
        if (str2 == null) {
            str2 = BuildConfig.buildJavascriptFrameworkVersion;
        }
        map2.put(str, str2);
        return this;
    }

    public AliMonitorDimensionValueSet addValues(AliMonitorDimensionValueSet aliMonitorDimensionValueSet) {
        Map<String, String> map2;
        if (!(aliMonitorDimensionValueSet == null || (map2 = aliMonitorDimensionValueSet.getMap()) == null)) {
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
        AliMonitorDimensionValueSet aliMonitorDimensionValueSet = (AliMonitorDimensionValueSet) obj;
        if (this.map == null) {
            if (aliMonitorDimensionValueSet.map != null) {
                return false;
            }
        } else if (!this.map.equals(aliMonitorDimensionValueSet.map)) {
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

    static AliMonitorDimensionValueSet readFromParcel(Parcel parcel) {
        AliMonitorDimensionValueSet aliMonitorDimensionValueSet;
        try {
            aliMonitorDimensionValueSet = create();
            try {
                aliMonitorDimensionValueSet.map = parcel.readHashMap(AliMonitorDimensionValueSet.class.getClassLoader());
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            aliMonitorDimensionValueSet = null;
            th.printStackTrace();
            return aliMonitorDimensionValueSet;
        }
        return aliMonitorDimensionValueSet;
    }
}
