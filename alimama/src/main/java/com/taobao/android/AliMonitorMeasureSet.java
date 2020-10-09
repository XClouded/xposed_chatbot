package com.taobao.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AliMonitorMeasureSet implements Parcelable {
    public static final Parcelable.Creator<AliMonitorMeasureSet> CREATOR = new Parcelable.Creator<AliMonitorMeasureSet>() {
        public AliMonitorMeasureSet createFromParcel(Parcel parcel) {
            return AliMonitorMeasureSet.readFromParcel(parcel);
        }

        public AliMonitorMeasureSet[] newArray(int i) {
            return new AliMonitorMeasureSet[i];
        }
    };
    private static final int INIT_SIZE = 3;
    private List<AliMonitorMeasure> measures = new ArrayList(3);

    public int describeContents() {
        return 0;
    }

    public static AliMonitorMeasureSet create() {
        return new AliMonitorMeasureSet();
    }

    public static AliMonitorMeasureSet create(Collection<String> collection) {
        AliMonitorMeasureSet aliMonitorMeasureSet = new AliMonitorMeasureSet();
        if (collection != null) {
            for (String addMeasure : collection) {
                aliMonitorMeasureSet.addMeasure(addMeasure);
            }
        }
        return aliMonitorMeasureSet;
    }

    public static AliMonitorMeasureSet create(String[] strArr) {
        AliMonitorMeasureSet aliMonitorMeasureSet = new AliMonitorMeasureSet();
        if (strArr != null) {
            for (String addMeasure : strArr) {
                aliMonitorMeasureSet.addMeasure(addMeasure);
            }
        }
        return aliMonitorMeasureSet;
    }

    private AliMonitorMeasureSet() {
    }

    public boolean valid(AliMonitorMeasureValueSet aliMonitorMeasureValueSet) {
        if (this.measures == null) {
            return true;
        }
        if (aliMonitorMeasureValueSet == null) {
            return false;
        }
        for (int i = 0; i < this.measures.size(); i++) {
            AliMonitorMeasure aliMonitorMeasure = this.measures.get(i);
            if (aliMonitorMeasure != null) {
                String name = aliMonitorMeasure.getName();
                if (!aliMonitorMeasureValueSet.containValue(name) || !aliMonitorMeasure.valid(aliMonitorMeasureValueSet.getValue(name))) {
                    return false;
                }
            }
        }
        return true;
    }

    public AliMonitorMeasureSet addMeasure(AliMonitorMeasure aliMonitorMeasure) {
        if (!this.measures.contains(aliMonitorMeasure)) {
            this.measures.add(aliMonitorMeasure);
        }
        return this;
    }

    public AliMonitorMeasureSet addMeasure(String str) {
        return addMeasure(new AliMonitorMeasure(str));
    }

    public AliMonitorMeasure getMeasure(String str) {
        for (AliMonitorMeasure next : this.measures) {
            if (next.getName().equals(str)) {
                return next;
            }
        }
        return null;
    }

    public List<AliMonitorMeasure> getMeasures() {
        return this.measures;
    }

    public void setConstantValue(AliMonitorMeasureValueSet aliMonitorMeasureValueSet) {
        if (this.measures != null && aliMonitorMeasureValueSet != null) {
            for (AliMonitorMeasure next : this.measures) {
                if (next.getConstantValue() != null && aliMonitorMeasureValueSet.getValue(next.getName()) == null) {
                    aliMonitorMeasureValueSet.setValue(next.getName(), next.getConstantValue().doubleValue());
                }
            }
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.measures != null) {
            try {
                Object[] array = this.measures.toArray();
                AliMonitorMeasure[] aliMonitorMeasureArr = null;
                if (array != null) {
                    aliMonitorMeasureArr = new AliMonitorMeasure[array.length];
                    for (int i2 = 0; i2 < array.length; i2++) {
                        aliMonitorMeasureArr[i2] = (AliMonitorMeasure) array[i2];
                    }
                }
                parcel.writeParcelableArray(aliMonitorMeasureArr, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static AliMonitorMeasureSet readFromParcel(Parcel parcel) {
        AliMonitorMeasureSet create = create();
        try {
            Parcelable[] readParcelableArray = parcel.readParcelableArray(AliMonitorMeasureSet.class.getClassLoader());
            if (readParcelableArray != null) {
                ArrayList arrayList = new ArrayList(readParcelableArray.length);
                for (Parcelable parcelable : readParcelableArray) {
                    arrayList.add((AliMonitorMeasure) parcelable);
                }
                create.measures = arrayList;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return create;
    }

    public void upateMeasures(List<AliMonitorMeasure> list) {
        int size = this.measures.size();
        int size2 = list.size();
        for (int i = 0; i < size; i++) {
            for (int i2 = 0; i2 < size2; i2++) {
                if (TextUtils.equals(this.measures.get(i).name, list.get(i2).name)) {
                    this.measures.get(i).setRange(list.get(i2).getMin(), list.get(i2).getMax());
                }
            }
        }
    }

    public void upateMeasure(AliMonitorMeasure aliMonitorMeasure) {
        int size = this.measures.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(this.measures.get(i).name, aliMonitorMeasure.name)) {
                this.measures.get(i).setRange(aliMonitorMeasure.getMin(), aliMonitorMeasure.getMax());
                this.measures.get(i).setConstantValue(aliMonitorMeasure.getConstantValue());
            }
        }
    }
}
