package com.ali.user.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryData implements Parcelable {
    public static final Parcelable.Creator<CountryData> CREATOR = new Parcelable.Creator<CountryData>() {
        public CountryData createFromParcel(Parcel parcel) {
            return new CountryData(parcel);
        }

        public CountryData[] newArray(int i) {
            return new CountryData[i];
        }
    };
    public String areaCode;
    public String checkPattern;
    public String countryCode;
    public String countryName;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.areaCode);
        parcel.writeString(this.countryName);
        parcel.writeString(this.countryCode);
        parcel.writeString(this.checkPattern);
    }

    public CountryData() {
    }

    protected CountryData(Parcel parcel) {
        this.areaCode = parcel.readString();
        this.countryName = parcel.readString();
        this.countryCode = parcel.readString();
        this.checkPattern = parcel.readString();
    }
}
