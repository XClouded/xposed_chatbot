package com.taobao.aipc.core.wrapper;

import android.os.Parcel;

public class BaseWrapper {
    private boolean mIsName;
    private String mName;

    /* access modifiers changed from: protected */
    public void setName(boolean z, String str) {
        this.mIsName = z;
        this.mName = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mIsName ? 1 : 0);
        parcel.writeString(this.mName);
    }

    public void readFromParcel(Parcel parcel) {
        boolean z = true;
        if (parcel.readInt() != 1) {
            z = false;
        }
        this.mIsName = z;
        this.mName = parcel.readString();
    }

    public boolean isName() {
        return this.mIsName;
    }

    public String getName() {
        return this.mName;
    }
}
