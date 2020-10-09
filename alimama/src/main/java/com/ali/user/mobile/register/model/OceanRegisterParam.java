package com.ali.user.mobile.register.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OceanRegisterParam implements Parcelable {
    public static final Parcelable.Creator<OceanRegisterParam> CREATOR = new Parcelable.Creator<OceanRegisterParam>() {
        public OceanRegisterParam createFromParcel(Parcel parcel) {
            return new OceanRegisterParam(parcel);
        }

        public OceanRegisterParam[] newArray(int i) {
            return new OceanRegisterParam[i];
        }
    };
    public String checkCode;
    public String companyName;
    public String countryCode;
    public String email;
    public String firstName;
    public String lastName;
    public String locale;
    public String mobileAreaCode;
    public String mobileNum;
    public String ncSessionId;
    public String ncSignature;
    public String ncToken;
    public String password;
    public String sessionId;
    public String thirdType;
    public String thirdUserId;
    public String voice;

    public int describeContents() {
        return 0;
    }

    public OceanRegisterParam toSendOverSeaSMS() {
        OceanRegisterParam oceanRegisterParam = new OceanRegisterParam();
        oceanRegisterParam.sessionId = this.sessionId;
        oceanRegisterParam.checkCode = this.checkCode;
        oceanRegisterParam.mobileAreaCode = this.mobileAreaCode;
        oceanRegisterParam.mobileNum = this.mobileNum;
        oceanRegisterParam.countryCode = this.countryCode;
        return oceanRegisterParam;
    }

    public OceanRegisterParam toInfo() {
        OceanRegisterParam oceanRegisterParam = new OceanRegisterParam();
        oceanRegisterParam.locale = this.locale;
        oceanRegisterParam.checkCode = this.checkCode;
        oceanRegisterParam.countryCode = this.countryCode;
        oceanRegisterParam.mobileAreaCode = this.mobileAreaCode;
        oceanRegisterParam.mobileNum = this.mobileNum;
        oceanRegisterParam.email = this.email;
        oceanRegisterParam.password = this.password;
        oceanRegisterParam.firstName = this.firstName;
        oceanRegisterParam.lastName = this.lastName;
        oceanRegisterParam.companyName = this.companyName;
        oceanRegisterParam.thirdUserId = this.thirdUserId;
        oceanRegisterParam.thirdType = this.thirdType;
        oceanRegisterParam.sessionId = this.sessionId;
        oceanRegisterParam.ncSessionId = this.ncSessionId;
        oceanRegisterParam.ncToken = this.ncToken;
        oceanRegisterParam.ncSignature = this.ncSignature;
        return oceanRegisterParam;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.checkCode);
        parcel.writeString(this.countryCode);
        parcel.writeString(this.mobileAreaCode);
        parcel.writeString(this.mobileNum);
        parcel.writeString(this.email);
        parcel.writeString(this.password);
        parcel.writeString(this.firstName);
        parcel.writeString(this.lastName);
        parcel.writeString(this.companyName);
        parcel.writeString(this.thirdUserId);
        parcel.writeString(this.thirdType);
        parcel.writeString(this.sessionId);
        parcel.writeString(this.ncSessionId);
        parcel.writeString(this.ncToken);
        parcel.writeString(this.ncSignature);
        parcel.writeString(this.voice);
    }

    public OceanRegisterParam() {
    }

    protected OceanRegisterParam(Parcel parcel) {
        this.checkCode = parcel.readString();
        this.countryCode = parcel.readString();
        this.mobileAreaCode = parcel.readString();
        this.mobileNum = parcel.readString();
        this.email = parcel.readString();
        this.password = parcel.readString();
        this.firstName = parcel.readString();
        this.lastName = parcel.readString();
        this.companyName = parcel.readString();
        this.thirdUserId = parcel.readString();
        this.thirdType = parcel.readString();
        this.sessionId = parcel.readString();
        this.ncSessionId = parcel.readString();
        this.ncToken = parcel.readString();
        this.ncSignature = parcel.readString();
        this.voice = parcel.readString();
    }
}
