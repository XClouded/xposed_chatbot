package com.taobao.android;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.UUID;

public class AliMonitorTransaction implements Parcelable {
    public static Parcelable.Creator<AliMonitorTransaction> CREATOR = new Parcelable.Creator<AliMonitorTransaction>() {
        public AliMonitorTransaction createFromParcel(Parcel parcel) {
            return AliMonitorTransaction.readFromParcel(parcel);
        }

        public AliMonitorTransaction[] newArray(int i) {
            return new AliMonitorTransaction[i];
        }
    };
    public AliMonitorDimensionValueSet dimensionValues;
    public Integer eventId;
    private Object lock;
    public String module;
    public String monitorPoint;
    public String transactionId;

    public int describeContents() {
        return 0;
    }

    public AliMonitorTransaction(Integer num, String str, String str2, AliMonitorDimensionValueSet aliMonitorDimensionValueSet) {
        this.eventId = num;
        this.module = str;
        this.monitorPoint = str2;
        this.transactionId = UUID.randomUUID().toString();
        this.dimensionValues = aliMonitorDimensionValueSet;
        this.lock = new Object();
    }

    public AliMonitorTransaction() {
    }

    public void begin(String str) {
        if (AliMonitorServiceFetcher.getMonitorService() != null) {
            AliMonitorServiceFetcher.getMonitorService().transaction_begin(this, str);
        }
    }

    public void end(String str) {
        if (AliMonitorServiceFetcher.getMonitorService() != null) {
            AliMonitorServiceFetcher.getMonitorService().transaction_end(this, str);
        }
    }

    public void addDimensionValues(AliMonitorDimensionValueSet aliMonitorDimensionValueSet) {
        synchronized (this.lock) {
            if (this.dimensionValues == null) {
                this.dimensionValues = aliMonitorDimensionValueSet;
            } else {
                this.dimensionValues.addValues(aliMonitorDimensionValueSet);
            }
        }
    }

    public void addDimensionValues(String str, String str2) {
        synchronized (this.lock) {
            if (this.dimensionValues == null) {
                this.dimensionValues = (AliMonitorDimensionValueSet) AliMonitorBalancedPool.getInstance().poll(AliMonitorDimensionValueSet.class, new Object[0]);
            }
            this.dimensionValues.setValue(str, str2);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.dimensionValues, i);
        parcel.writeInt(this.eventId.intValue());
        parcel.writeString(this.module);
        parcel.writeString(this.monitorPoint);
        parcel.writeString(this.transactionId);
    }

    static AliMonitorTransaction readFromParcel(Parcel parcel) {
        AliMonitorTransaction aliMonitorTransaction = new AliMonitorTransaction();
        try {
            aliMonitorTransaction.dimensionValues = (AliMonitorDimensionValueSet) parcel.readParcelable(AliMonitorTransaction.class.getClassLoader());
            aliMonitorTransaction.eventId = Integer.valueOf(parcel.readInt());
            aliMonitorTransaction.module = parcel.readString();
            aliMonitorTransaction.monitorPoint = parcel.readString();
            aliMonitorTransaction.transactionId = parcel.readString();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return aliMonitorTransaction;
    }
}
