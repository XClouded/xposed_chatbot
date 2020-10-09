package com.huawei.hms.support.api.client;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import com.huawei.hms.core.aidl.a.a;
import java.util.Arrays;

public final class Status implements Parcelable {
    public static final Parcelable.Creator<Status> CREATOR = new a();
    public static final Status CoreException = new Status(500);
    public static final Status FAILURE = new Status(1);
    public static final Status MessageNotFound = new Status(404);
    public static final Status SUCCESS = new Status(0);
    @a
    private final PendingIntent a;
    @a
    private int b;
    @a
    private String c;

    public int describeContents() {
        return 0;
    }

    public Status(int i) {
        this(i, (String) null);
    }

    public Status(int i, String str) {
        this(i, str, (PendingIntent) null);
    }

    public Status(int i, String str, PendingIntent pendingIntent) {
        this.b = i;
        this.c = str;
        this.a = pendingIntent;
    }

    private static boolean a(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public boolean hasResolution() {
        return this.a != null;
    }

    public void startResolutionForResult(Activity activity, int i) throws IntentSender.SendIntentException {
        if (hasResolution()) {
            activity.startIntentSenderForResult(this.a.getIntentSender(), i, (Intent) null, 0, 0, 0);
        }
    }

    public int getStatusCode() {
        return this.b;
    }

    public String getStatusMessage() {
        return this.c;
    }

    public PendingIntent getResolution() {
        return this.a;
    }

    public boolean isSuccess() {
        return this.b <= 0;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.b), this.c, this.a});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Status)) {
            return false;
        }
        Status status = (Status) obj;
        if (this.b != status.b || !a(this.c, status.c) || !a(this.a, status.a)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "{statusCode: " + this.b + ", statusMessage: " + this.c + ", pendingIntent: " + this.a + ", }";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.b);
        parcel.writeString(this.c);
        PendingIntent.writePendingIntentOrNullToParcel(this.a, parcel);
    }
}
