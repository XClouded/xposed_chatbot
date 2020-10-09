package com.alimama.moon.features.reports.withdraw.contracts;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.alimama.moon.features.reports.withdraw.network.GetBalanceResponse;
import com.alimama.union.app.rxnetwork.RxMtopResponse;

public interface IWithdrawContract {
    public static final String ERROR_ALIPAY_NOT_EXIST_FOR_ALIMAMA = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ACCOUNT_ALIPAY";
    public static final String ERROR_ALIPAY_NOT_EXIST_FOR_TAOBAO = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ALIPAY_EMPTY";
    public static final String ERROR_ALIPAY_NOT_SAME = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ALIPAY_NOTEQ";
    public static final String ERROR_EMPTY_ACCOUNT = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ACCOUNT_EMPTY";
    public static final String ERROR_NOT_ACTIVATED = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ACCOUNT_V";
    public static final String ERROR_NOT_VERIFIED = "FAIL_BIZ_SEND_MOBILE_CODE_CHENG_LING_RECORD_NOT_EXIST";

    public interface IPresenter {
        void onGetBalanceResponse(@NonNull RxMtopResponse<GetBalanceResponse> rxMtopResponse, @NonNull Context context);
    }

    public interface IView {
        void displayRetryError();

        void displayWithdrawError(@StringRes int i, @StringRes int i2);

        void displayWithdrawError(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2);

        void goToWithdrawAccountPage(@NonNull GetBalanceResponse getBalanceResponse);
    }
}
