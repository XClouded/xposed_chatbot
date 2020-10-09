package com.ali.telescope.internal.plugins.memleak;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Keep;

@Keep
public class LeakResult implements Parcelable {
    public static final Parcelable.Creator<LeakResult> CREATOR = new Parcelable.Creator<LeakResult>() {
        public LeakResult createFromParcel(Parcel parcel) {
            return new LeakResult(parcel);
        }

        public LeakResult[] newArray(int i) {
            return new LeakResult[i];
        }
    };
    public boolean hasLeak;
    public LeakItem[] leakList;

    public int describeContents() {
        return 0;
    }

    public LeakResult() {
    }

    protected LeakResult(Parcel parcel) {
        this.leakList = (LeakItem[]) parcel.createTypedArray(LeakItem.CREATOR);
        this.hasLeak = parcel.readByte() != 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedArray(this.leakList, i);
        parcel.writeByte(this.hasLeak ? (byte) 1 : 0);
    }

    /* access modifiers changed from: package-private */
    public LeakResult resolve() {
        if (this.hasLeak && this.leakList != null) {
            for (LeakItem leakItem : this.leakList) {
                if (leakItem.keyBytes != null) {
                    leakItem.key = new String(leakItem.keyBytes);
                }
                if (leakItem.leakTrace == null || leakItem.leakTrace.length <= 0) {
                    leakItem.leakClass = "";
                } else {
                    leakItem.leakClass = leakItem.leakTrace[leakItem.leakTrace.length - 1];
                }
            }
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.hasLeak) {
            sb.append("memory has leak\n");
            if (this.leakList != null) {
                for (LeakItem leakItem : this.leakList) {
                    sb.append("leak item :\n");
                    if (leakItem.leakTrace != null) {
                        for (String str : leakItem.leakTrace) {
                            sb.append(str + "\n");
                        }
                    }
                    if (leakItem.keyBytes != null) {
                        sb.append("key: " + new String(leakItem.keyBytes) + "\n");
                    }
                    sb.append("leak class: " + leakItem.leakClass);
                }
            }
        } else {
            sb.append("no memory leak found");
        }
        return sb.toString();
    }

    @Keep
    public static class LeakItem implements Parcelable {
        public static final Parcelable.Creator<LeakItem> CREATOR = new Parcelable.Creator<LeakItem>() {
            public LeakItem createFromParcel(Parcel parcel) {
                return new LeakItem(parcel);
            }

            public LeakItem[] newArray(int i) {
                return new LeakItem[i];
            }
        };
        public String key;
        /* access modifiers changed from: private */
        public byte[] keyBytes;
        public String leakClass;
        public String[] leakTrace;

        public int describeContents() {
            return 0;
        }

        public LeakItem() {
        }

        protected LeakItem(Parcel parcel) {
            this.leakTrace = parcel.createStringArray();
            this.key = parcel.readString();
            this.keyBytes = parcel.createByteArray();
            this.leakClass = parcel.readString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeStringArray(this.leakTrace);
            parcel.writeString(this.key);
            parcel.writeByteArray(this.keyBytes);
            parcel.writeString(this.leakClass);
        }
    }
}
