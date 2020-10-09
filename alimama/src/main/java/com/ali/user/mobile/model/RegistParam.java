package com.ali.user.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RegistParam implements Parcelable {
    public static final Parcelable.Creator<RegistParam> CREATOR = new Parcelable.Creator<RegistParam>() {
        public RegistParam[] newArray(int i) {
            return new RegistParam[i];
        }

        public RegistParam createFromParcel(Parcel parcel) {
            return new RegistParam(parcel);
        }
    };
    public String regFrom;
    public String registAccount;
    public int registSite;
    public String token;

    public int describeContents() {
        return 0;
    }

    public RegistParam() {
    }

    public RegistParam(Parcel parcel) {
        this.registAccount = parcel.readString();
        this.token = parcel.readString();
        this.registSite = parcel.readInt();
        this.regFrom = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.registAccount);
        parcel.writeString(this.token);
        parcel.writeInt(this.registSite);
        parcel.writeString(this.regFrom);
    }
}
