package com.alimama.union.app.share.flutter.network;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.vessel.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class ShareInfoResponse2 implements Parcelable {
    public static final Parcelable.Creator<ShareInfoResponse2> CREATOR = new Parcelable.Creator<ShareInfoResponse2>() {
        public ShareInfoResponse2 createFromParcel(Parcel parcel) {
            return new ShareInfoResponse2(parcel);
        }

        public ShareInfoResponse2[] newArray(int i) {
            return new ShareInfoResponse2[i];
        }
    };
    private String mClickUrl;
    private String mCommissionRate;
    private String mCommissionTotal;
    private Double mCouponAmount;
    private List<String> mImages = new ArrayList();
    private Double mPrice;
    private Double mPriceAfterCoupon;
    private String mQrCodeUrl;
    private String mSharedText;
    private String mTaoCodeToken;
    private String mTitile;
    private String status;

    public int describeContents() {
        return 0;
    }

    public static ShareInfoResponse2 fromJson(@NonNull SafeJSONObject safeJSONObject) {
        ShareInfoResponse2 shareInfoResponse2 = new ShareInfoResponse2();
        shareInfoResponse2.status = safeJSONObject.optString("status");
        shareInfoResponse2.mCommissionRate = safeJSONObject.optString("preCommissionRate");
        shareInfoResponse2.mCommissionTotal = safeJSONObject.optString("preCommissionAmt");
        shareInfoResponse2.mPrice = doubleValueOf(safeJSONObject.optString("discountPrice"));
        shareInfoResponse2.mPriceAfterCoupon = doubleValueOf(safeJSONObject.optString("discountCouponPrice"));
        shareInfoResponse2.mCouponAmount = doubleValueOf(safeJSONObject.optString("amount"));
        shareInfoResponse2.mSharedText = safeJSONObject.optString("shareChangeMaterialText");
        shareInfoResponse2.mTitile = safeJSONObject.optString("title");
        shareInfoResponse2.mClickUrl = safeJSONObject.optString("clickUrl");
        shareInfoResponse2.mQrCodeUrl = safeJSONObject.optString("qrcodeUrl");
        shareInfoResponse2.mTaoCodeToken = safeJSONObject.optString("shareTaoToken");
        SafeJSONArray optJSONArray = safeJSONObject.optJSONArray("sharePicts");
        for (int i = 0; i < optJSONArray.length(); i++) {
            String optString = optJSONArray.optString(i);
            if (!TextUtils.isEmpty(optString)) {
                if (!optString.startsWith("http")) {
                    optString = Utils.HTTPS_SCHEMA + optString;
                }
                shareInfoResponse2.mImages.add(optString);
            }
        }
        return shareInfoResponse2;
    }

    public String getShareUrl() {
        return this.mClickUrl;
    }

    public String getCommissionRate() {
        return this.mCommissionRate;
    }

    public String getCommissionTotal() {
        return this.mCommissionTotal;
    }

    public String getCommissionInfo(@NonNull Context context) {
        return context.getString(R.string.share_commission_info, new Object[]{this.mCommissionRate, this.mCommissionTotal});
    }

    public String getSharedText() {
        return this.mSharedText;
    }

    public String getTitle() {
        return this.mTitile;
    }

    public Double getCouponAmount() {
        return this.mCouponAmount;
    }

    public Double getPrice() {
        return this.mPrice;
    }

    public Double getPriceAfterCoupon() {
        return this.mPriceAfterCoupon;
    }

    public String getTaoCode() {
        return this.mTaoCodeToken;
    }

    public String getQrCodeUrl() {
        return this.mQrCodeUrl;
    }

    public String getStatus() {
        return this.status;
    }

    @NonNull
    public List<String> getImages() {
        return this.mImages;
    }

    private static Double doubleValueOf(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return Double.valueOf(Double.NaN);
        }
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException unused) {
            return Double.valueOf(Double.NaN);
        }
    }

    public ShareInfoResponse2() {
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mCommissionRate);
        parcel.writeString(this.mCommissionTotal);
        parcel.writeValue(this.mPrice);
        parcel.writeValue(this.mPriceAfterCoupon);
        parcel.writeValue(this.mCouponAmount);
        parcel.writeString(this.mSharedText);
        parcel.writeString(this.mClickUrl);
        parcel.writeString(this.mTitile);
        parcel.writeString(this.mQrCodeUrl);
        parcel.writeString(this.mTaoCodeToken);
        parcel.writeStringList(this.mImages);
    }

    protected ShareInfoResponse2(Parcel parcel) {
        this.mCommissionRate = parcel.readString();
        this.mCommissionTotal = parcel.readString();
        this.mPrice = (Double) parcel.readValue(Double.class.getClassLoader());
        this.mPriceAfterCoupon = (Double) parcel.readValue(Double.class.getClassLoader());
        this.mCouponAmount = (Double) parcel.readValue(Double.class.getClassLoader());
        this.mSharedText = parcel.readString();
        this.mClickUrl = parcel.readString();
        this.mTitile = parcel.readString();
        this.mQrCodeUrl = parcel.readString();
        this.mTaoCodeToken = parcel.readString();
        this.mImages = parcel.createStringArrayList();
    }
}
