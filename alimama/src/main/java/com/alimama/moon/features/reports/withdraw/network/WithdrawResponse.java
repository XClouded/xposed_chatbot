package com.alimama.moon.features.reports.withdraw.network;

import android.util.Log;
import androidx.annotation.Nullable;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class WithdrawResponse {
    private static final String TAG = "WithdrawResponse";
    private Double amount;

    public static WithdrawResponse fromJson(SafeJSONObject safeJSONObject) {
        WithdrawResponse withdrawResponse = new WithdrawResponse();
        try {
            withdrawResponse.amount = Double.valueOf(safeJSONObject.optJSONObject("data").optString("amount"));
        } catch (NumberFormatException e) {
            Log.w(TAG, "parsing WithdrawResponse failed", e);
        }
        return withdrawResponse;
    }

    @Nullable
    public Double getAmount() {
        return this.amount;
    }
}
