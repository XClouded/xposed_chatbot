package com.huawei.hms.core.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/* compiled from: DataBuffer */
final class c implements Parcelable.Creator<b> {
    c() {
    }

    /* renamed from: a */
    public b createFromParcel(Parcel parcel) {
        return new b(parcel, (c) null);
    }

    /* renamed from: a */
    public b[] newArray(int i) {
        return new b[i];
    }
}
