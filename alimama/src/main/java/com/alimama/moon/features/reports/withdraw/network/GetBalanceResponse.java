package com.alimama.moon.features.reports.withdraw.network;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.Nullable;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class GetBalanceResponse implements Parcelable {
    public static final Parcelable.Creator<GetBalanceResponse> CREATOR = new Parcelable.Creator<GetBalanceResponse>() {
        public GetBalanceResponse createFromParcel(Parcel parcel) {
            return new GetBalanceResponse(parcel);
        }

        public GetBalanceResponse[] newArray(int i) {
            return new GetBalanceResponse[i];
        }
    };
    private static final String TAG = "GetBalanceResponse";
    @Nullable
    private String alipayAccount;
    @Nullable
    private Double balance;

    public int describeContents() {
        return 0;
    }

    public static GetBalanceResponse fromJson(SafeJSONObject safeJSONObject) {
        SafeJSONObject optJSONObject = safeJSONObject.optJSONObject("data");
        GetBalanceResponse getBalanceResponse = new GetBalanceResponse();
        getBalanceResponse.alipayAccount = optJSONObject.optString("alipay");
        try {
            getBalanceResponse.balance = Double.valueOf(optJSONObject.optString("amount"));
        } catch (NumberFormatException e) {
            Log.w(TAG, "parsing GetBalanceResponse failed", e);
        }
        return getBalanceResponse;
    }

    @Nullable
    public Double getBalance() {
        return this.balance;
    }

    @Nullable
    public String getAlipayAccount() {
        return this.alipayAccount;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.balance);
        parcel.writeString(this.alipayAccount);
    }

    public GetBalanceResponse() {
    }

    protected GetBalanceResponse(Parcel parcel) {
        this.balance = (Double) parcel.readValue(Double.class.getClassLoader());
        this.alipayAccount = parcel.readString();
    }
}
