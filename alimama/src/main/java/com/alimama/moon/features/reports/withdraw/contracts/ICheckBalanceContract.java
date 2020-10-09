package com.alimama.moon.features.reports.withdraw.contracts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.alimama.moon.features.reports.withdraw.network.CheckBalanceResponse;
import com.alimama.union.app.rxnetwork.RxMtopResponse;

public interface ICheckBalanceContract {
    public static final String ERROR_ALIPAY_MISSING_PHONE_NUM = "FAIL_BIZ_CHECK_BALANCE_ALIPAY_NOT_BIND_MOBILE";
    public static final String ERROR_PHONE_NUM_NOT_SAME = "FAIL_BIZ_CHECK_BALANCE_NOT_ALIPAY_BIND_MOBILE";

    public interface IPresenter {
        boolean checkWithdrawValue(double d, double d2);

        void onCheckBalanceResponse(double d, @NonNull RxMtopResponse<CheckBalanceResponse> rxMtopResponse);
    }

    public interface IView {
        void displayToast(@StringRes int i);

        void displayToast(@NonNull String str);

        void goToChangeNumberPage(double d, @NonNull String str, @NonNull String str2, @Nullable String str3, @Nullable String str4);

        void goToErrorPage(@StringRes int i, @StringRes int i2);

        void goToWithdrawCodePage(@NonNull Double d, @NonNull String str, @NonNull String str2, @Nullable String str3);

        void setWithdrawButtonEnabled(boolean z);
    }
}
