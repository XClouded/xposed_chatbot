package com.aurelhubert.ahbottomnavigation.notification;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AHNotification implements Parcelable {
    public static final Parcelable.Creator<AHNotification> CREATOR = new Parcelable.Creator<AHNotification>() {
        public AHNotification createFromParcel(Parcel parcel) {
            return new AHNotification(parcel);
        }

        public AHNotification[] newArray(int i) {
            return new AHNotification[i];
        }
    };
    /* access modifiers changed from: private */
    @ColorInt
    public int backgroundColor;
    /* access modifiers changed from: private */
    @Nullable
    public String text;
    /* access modifiers changed from: private */
    @ColorInt
    public int textColor;

    public int describeContents() {
        return 0;
    }

    public AHNotification() {
    }

    private AHNotification(Parcel parcel) {
        this.text = parcel.readString();
        this.textColor = parcel.readInt();
        this.backgroundColor = parcel.readInt();
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(this.text);
    }

    public String getText() {
        return this.text;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public static AHNotification justText(String str) {
        return new Builder().setText(str).build();
    }

    public static List<AHNotification> generateEmptyList(int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < i; i2++) {
            arrayList.add(new AHNotification());
        }
        return arrayList;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.text);
        parcel.writeInt(this.textColor);
        parcel.writeInt(this.backgroundColor);
    }

    public static class Builder {
        @ColorInt
        private int backgroundColor;
        @Nullable
        private String text;
        @ColorInt
        private int textColor;

        public Builder setText(String str) {
            this.text = str;
            return this;
        }

        public Builder setTextColor(@ColorInt int i) {
            this.textColor = i;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int i) {
            this.backgroundColor = i;
            return this;
        }

        public AHNotification build() {
            AHNotification aHNotification = new AHNotification();
            String unused = aHNotification.text = this.text;
            int unused2 = aHNotification.textColor = this.textColor;
            int unused3 = aHNotification.backgroundColor = this.backgroundColor;
            return aHNotification;
        }
    }
}
