package com.huawei.hms.support.api.client;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;

/* compiled from: Status */
final class a implements Parcelable.Creator {
    a() {
    }

    /* renamed from: a */
    public Status createFromParcel(Parcel parcel) {
        return new Status(parcel.readInt(), parcel.readString(), PendingIntent.readPendingIntentOrNullFromParcel(parcel));
    }

    /* renamed from: a */
    public Status[] newArray(int i) {
        return new Status[i];
    }
}
