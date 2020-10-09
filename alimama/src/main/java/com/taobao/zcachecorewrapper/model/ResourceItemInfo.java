package com.taobao.zcachecorewrapper.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ResourceItemInfo implements Parcelable {
    public static final Parcelable.Creator<ResourceItemInfo> CREATOR = new Parcelable.Creator<ResourceItemInfo>() {
        public ResourceItemInfo createFromParcel(Parcel parcel) {
            return new ResourceItemInfo(parcel);
        }

        public ResourceItemInfo[] newArray(int i) {
            return new ResourceItemInfo[i];
        }
    };
    public String appName;
    public int errCode;
    public String errMsg;
    public String filePath;
    public boolean isAppInstalled;
    public boolean isFirstVisit;
    public double matchTime;
    public String md5;
    public double readAppResTime;
    public long seq;
    public String trigger;
    public String url;

    public int describeContents() {
        return 0;
    }

    public ResourceItemInfo() {
    }

    protected ResourceItemInfo(Parcel parcel) {
        this.url = parcel.readString();
        this.appName = parcel.readString();
        this.seq = parcel.readLong();
        this.matchTime = parcel.readDouble();
        this.readAppResTime = parcel.readDouble();
        this.errCode = parcel.readInt();
        this.errMsg = parcel.readString();
        this.md5 = parcel.readString();
        this.filePath = parcel.readString();
        boolean z = false;
        this.isFirstVisit = parcel.readByte() != 0;
        this.isAppInstalled = parcel.readByte() != 0 ? true : z;
        this.trigger = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeString(this.appName);
        parcel.writeLong(this.seq);
        parcel.writeDouble(this.matchTime);
        parcel.writeDouble(this.readAppResTime);
        parcel.writeInt(this.errCode);
        parcel.writeString(this.errMsg);
        parcel.writeString(this.md5);
        parcel.writeString(this.filePath);
        parcel.writeByte(this.isFirstVisit ? (byte) 1 : 0);
        parcel.writeByte(this.isAppInstalled ? (byte) 1 : 0);
        parcel.writeString(this.trigger);
    }
}
