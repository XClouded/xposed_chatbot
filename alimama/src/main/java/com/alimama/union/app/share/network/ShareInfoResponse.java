package com.alimama.union.app.share.network;

import android.content.Context;
import android.net.Uri;
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

public class ShareInfoResponse implements Parcelable {
    public static final Parcelable.Creator<ShareInfoResponse> CREATOR = new Parcelable.Creator<ShareInfoResponse>() {
        public ShareInfoResponse createFromParcel(Parcel parcel) {
            return new ShareInfoResponse(parcel);
        }

        public ShareInfoResponse[] newArray(int i) {
            return new ShareInfoResponse[i];
        }
    };
    private static final String HTTPS_SCHEMA = "https";
    private static final String PARAM_KEY_SPM = "spm";
    private String mClickUrl;
    private String mCommissionRate;
    private String mCommissionTotal;
    private Double mCouponAmount;
    private List<String> mImages = new ArrayList();
    private Double mPrice;
    private Double mPriceAfterCoupon;
    private String mShareUrl;
    private String mSharedText;
    private String mSpmVal;
    private String mTitle;

    public int describeContents() {
        return 0;
    }

    public static ShareInfoResponse fromJson(@NonNull SafeJSONObject safeJSONObject, @Nullable String str) {
        ShareInfoResponse shareInfoResponse = new ShareInfoResponse();
        shareInfoResponse.mCommissionRate = safeJSONObject.optString("preCommissionRate");
        shareInfoResponse.mCommissionTotal = safeJSONObject.optString("preCommissionAmt");
        shareInfoResponse.mPrice = doubleValueOf(safeJSONObject.optString("discountPrice"));
        shareInfoResponse.mPriceAfterCoupon = doubleValueOf(safeJSONObject.optString("discountCouponPrice"));
        shareInfoResponse.mCouponAmount = doubleValueOf(safeJSONObject.optString("amount"));
        shareInfoResponse.mTitle = safeJSONObject.optString("title");
        shareInfoResponse.mSharedText = safeJSONObject.optString("shareText");
        shareInfoResponse.mClickUrl = safeJSONObject.optString("clickUrl");
        shareInfoResponse.mSpmVal = str;
        SafeJSONArray optJSONArray = safeJSONObject.optJSONArray("sharePicts");
        for (int i = 0; i < optJSONArray.length(); i++) {
            String optString = optJSONArray.optString(i);
            if (!TextUtils.isEmpty(optString)) {
                if (!optString.startsWith("http")) {
                    optString = Utils.HTTPS_SCHEMA + optString;
                }
                shareInfoResponse.mImages.add(optString);
            }
        }
        return shareInfoResponse;
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
        return this.mTitle;
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

    public String getShareUrl() {
        if (TextUtils.isEmpty(this.mClickUrl)) {
            return this.mClickUrl;
        }
        if (!TextUtils.isEmpty(this.mShareUrl)) {
            return this.mShareUrl;
        }
        Uri parse = Uri.parse(this.mClickUrl);
        String host = parse.getHost();
        String scheme = parse.getScheme();
        Uri.Builder buildUpon = parse.buildUpon();
        if (TextUtils.isEmpty(scheme)) {
            buildUpon.scheme("https");
        }
        if (!TextUtils.isEmpty(host) && host.contains("uland")) {
            buildUpon.appendQueryParameter("src", "tblm_lmapp");
        }
        if (!TextUtils.isEmpty(this.mSpmVal)) {
            buildUpon.appendQueryParameter("spm", this.mSpmVal);
        }
        this.mShareUrl = buildUpon.build().toString();
        return this.mShareUrl;
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

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mCommissionRate);
        parcel.writeString(this.mCommissionTotal);
        parcel.writeString(this.mTitle);
        parcel.writeValue(this.mPrice);
        parcel.writeValue(this.mPriceAfterCoupon);
        parcel.writeValue(this.mCouponAmount);
        parcel.writeString(this.mSharedText);
        parcel.writeString(this.mClickUrl);
        parcel.writeString(this.mShareUrl);
        parcel.writeString(this.mSpmVal);
        parcel.writeStringList(this.mImages);
    }

    public ShareInfoResponse() {
    }

    protected ShareInfoResponse(Parcel parcel) {
        this.mCommissionRate = parcel.readString();
        this.mCommissionTotal = parcel.readString();
        this.mTitle = parcel.readString();
        this.mPrice = (Double) parcel.readValue(Double.class.getClassLoader());
        this.mPriceAfterCoupon = (Double) parcel.readValue(Double.class.getClassLoader());
        this.mCouponAmount = (Double) parcel.readValue(Double.class.getClassLoader());
        this.mSharedText = parcel.readString();
        this.mClickUrl = parcel.readString();
        this.mShareUrl = parcel.readString();
        this.mSpmVal = parcel.readString();
        this.mImages = parcel.createStringArrayList();
    }
}
