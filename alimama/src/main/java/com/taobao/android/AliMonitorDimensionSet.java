package com.taobao.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AliMonitorDimensionSet implements Parcelable {
    public static final Parcelable.Creator<AliMonitorDimensionSet> CREATOR = new Parcelable.Creator<AliMonitorDimensionSet>() {
        public AliMonitorDimensionSet createFromParcel(Parcel parcel) {
            return AliMonitorDimensionSet.readFromParcel(parcel);
        }

        public AliMonitorDimensionSet[] newArray(int i) {
            return new AliMonitorDimensionSet[i];
        }
    };
    private static final int INIT_SIZE = 3;
    private static final String TAG = "DimensionSet";
    private List<AliMonitorDimension> dimensions = new ArrayList(3);

    public int describeContents() {
        return 0;
    }

    public static AliMonitorDimensionSet create() {
        return new AliMonitorDimensionSet();
    }

    public static AliMonitorDimensionSet create(Collection<String> collection) {
        AliMonitorDimensionSet aliMonitorDimensionSet = new AliMonitorDimensionSet();
        if (collection != null) {
            for (String aliMonitorDimension : collection) {
                aliMonitorDimensionSet.addDimension(new AliMonitorDimension(aliMonitorDimension));
            }
        }
        return aliMonitorDimensionSet;
    }

    public static AliMonitorDimensionSet create(String[] strArr) {
        AliMonitorDimensionSet aliMonitorDimensionSet = new AliMonitorDimensionSet();
        if (strArr != null) {
            for (String aliMonitorDimension : strArr) {
                aliMonitorDimensionSet.addDimension(new AliMonitorDimension(aliMonitorDimension));
            }
        }
        return aliMonitorDimensionSet;
    }

    private AliMonitorDimensionSet() {
    }

    public boolean valid(AliMonitorDimensionValueSet aliMonitorDimensionValueSet) {
        if (this.dimensions == null) {
            return true;
        }
        if (aliMonitorDimensionValueSet == null) {
            return false;
        }
        for (AliMonitorDimension name : this.dimensions) {
            if (!aliMonitorDimensionValueSet.containValue(name.getName())) {
                return false;
            }
        }
        return true;
    }

    public AliMonitorDimensionSet addDimension(AliMonitorDimension aliMonitorDimension) {
        if (this.dimensions.contains(aliMonitorDimension)) {
            return this;
        }
        this.dimensions.add(aliMonitorDimension);
        return this;
    }

    public AliMonitorDimensionSet addDimension(String str) {
        return addDimension(new AliMonitorDimension(str));
    }

    public AliMonitorDimensionSet addDimension(String str, String str2) {
        return addDimension(new AliMonitorDimension(str, str2));
    }

    public AliMonitorDimension getDimension(String str) {
        for (AliMonitorDimension next : this.dimensions) {
            if (next.getName().equals(str)) {
                return next;
            }
        }
        return null;
    }

    public void setConstantValue(AliMonitorDimensionValueSet aliMonitorDimensionValueSet) {
        if (this.dimensions != null && aliMonitorDimensionValueSet != null) {
            for (AliMonitorDimension next : this.dimensions) {
                if (next.getConstantValue() != null && aliMonitorDimensionValueSet.getValue(next.getName()) == null) {
                    aliMonitorDimensionValueSet.setValue(next.getName(), next.getConstantValue());
                }
            }
        }
    }

    public List<AliMonitorDimension> getDimensions() {
        return this.dimensions;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.dimensions != null) {
            try {
                Object[] array = this.dimensions.toArray();
                AliMonitorDimension[] aliMonitorDimensionArr = null;
                if (array != null) {
                    aliMonitorDimensionArr = new AliMonitorDimension[array.length];
                    for (int i2 = 0; i2 < array.length; i2++) {
                        aliMonitorDimensionArr[i2] = (AliMonitorDimension) array[i2];
                    }
                }
                parcel.writeParcelableArray(aliMonitorDimensionArr, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static AliMonitorDimensionSet readFromParcel(Parcel parcel) {
        AliMonitorDimensionSet create = create();
        try {
            Parcelable[] readParcelableArray = parcel.readParcelableArray(AliMonitorDimensionSet.class.getClassLoader());
            if (readParcelableArray != null) {
                if (create.dimensions == null) {
                    create.dimensions = new ArrayList();
                }
                for (int i = 0; i < readParcelableArray.length; i++) {
                    if (readParcelableArray[i] == null || !(readParcelableArray[i] instanceof AliMonitorDimension)) {
                        Log.d(TAG, "parcelables[i]:" + readParcelableArray[i]);
                    } else {
                        create.dimensions.add((AliMonitorDimension) readParcelableArray[i]);
                    }
                }
            }
        } catch (Throwable th) {
            Log.w(TAG, "[readFromParcel]", th);
        }
        return create;
    }
}
