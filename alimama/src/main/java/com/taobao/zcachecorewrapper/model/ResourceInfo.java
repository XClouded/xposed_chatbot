package com.taobao.zcachecorewrapper.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;

public class ResourceInfo implements Parcelable {
    public static final Parcelable.Creator<ResourceInfo> CREATOR = new Parcelable.Creator<ResourceInfo>() {
        public ResourceInfo createFromParcel(Parcel parcel) {
            return new ResourceInfo(parcel);
        }

        public ResourceInfo[] newArray(int i) {
            return new ResourceInfo[i];
        }
    };
    public String appName;
    public HashMap<String, String> comboHeaders;
    public int errCode;
    public String errMsg;
    public HashMap<String, String> headers;
    public double matchTime;
    public double readAppResTime;
    public ArrayList<ResourceItemInfo> resourceItemInfos;
    public long seq;
    public int type;
    public String url;

    public interface ResourceFindPolocy {
        public static final int ResourceFindAllComboParts = 2;
        public static final int ResourceFindCombo = 1;
        public static final int ResourceFindNotZCacheCombo = 3;
    }

    public int describeContents() {
        return 0;
    }

    protected ResourceInfo(Parcel parcel) {
        this.url = parcel.readString();
        this.type = parcel.readInt();
        this.appName = parcel.readString();
        this.seq = parcel.readLong();
        this.matchTime = parcel.readDouble();
        this.readAppResTime = parcel.readDouble();
        this.errCode = parcel.readInt();
        this.errMsg = parcel.readString();
        this.headers = parcel.readHashMap(HashMap.class.getClassLoader());
        this.comboHeaders = parcel.readHashMap(HashMap.class.getClassLoader());
        this.resourceItemInfos = parcel.readArrayList(ResourceItemInfo.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeInt(this.type);
        parcel.writeString(this.appName);
        parcel.writeLong(this.seq);
        parcel.writeDouble(this.matchTime);
        parcel.writeDouble(this.readAppResTime);
        parcel.writeInt(this.errCode);
        parcel.writeString(this.errMsg);
        parcel.writeMap(this.headers);
        parcel.writeMap(this.comboHeaders);
        parcel.writeList(this.resourceItemInfos);
    }

    public ResourceInfo() {
        this.url = "";
        this.type = 1;
        this.seq = 0;
        this.matchTime = 0.0d;
        this.readAppResTime = 0.0d;
        this.errCode = 0;
        this.errMsg = "";
        this.headers = null;
        this.comboHeaders = null;
        this.resourceItemInfos = null;
    }
}
