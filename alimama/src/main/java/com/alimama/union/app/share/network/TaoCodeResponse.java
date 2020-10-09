package com.alimama.union.app.share.network;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class TaoCodeResponse implements Parcelable {
    public static final Parcelable.Creator<TaoCodeResponse> CREATOR = new Parcelable.Creator<TaoCodeResponse>() {
        public TaoCodeResponse createFromParcel(Parcel parcel) {
            return new TaoCodeResponse(parcel);
        }

        public TaoCodeResponse[] newArray(int i) {
            return new TaoCodeResponse[i];
        }
    };
    private String mTaoCode;
    private String mTinyLink;

    public int describeContents() {
        return 0;
    }

    public static TaoCodeResponse fromJson(@NonNull SafeJSONObject safeJSONObject) {
        TaoCodeResponse taoCodeResponse = new TaoCodeResponse();
        taoCodeResponse.mTaoCode = safeJSONObject.optString("password");
        taoCodeResponse.mTinyLink = safeJSONObject.optString("url");
        return taoCodeResponse;
    }

    public String getTinyLink() {
        return this.mTinyLink;
    }

    public String getTaoCode() {
        return this.mTaoCode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mTaoCode);
        parcel.writeString(this.mTinyLink);
    }

    public TaoCodeResponse() {
    }

    protected TaoCodeResponse(Parcel parcel) {
        this.mTaoCode = parcel.readString();
        this.mTinyLink = parcel.readString();
    }
}
