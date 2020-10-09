package com.alimama.moon.features.reports.withdraw;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alimama.moon.R;
import com.alimama.moon.features.reports.withdraw.contracts.ICheckBalanceContract;
import com.alimama.moon.features.reports.withdraw.network.CheckBalanceResponse;
import com.alimama.union.app.rxnetwork.RxMtopResponse;

public class CheckBalancePresenter implements ICheckBalanceContract.IPresenter {
    private static final String APP_MONITOR_MODULE = "Page_Account";
    private static final String APP_MONITOR_POINT = "withdraw_btn_click";
    static final double MAX_WITHDRAW_LIMIT = 500000.0d;
    private static final double MIN_WITHDRAW_LIMIT_EXCLUSIVE = 0.1d;
    @NonNull
    private final ICheckBalanceContract.IView mView;

    public CheckBalancePresenter(@NonNull ICheckBalanceContract.IView iView) {
        this.mView = iView;
    }

    public void onCheckBalanceResponse(double d, @NonNull RxMtopResponse<CheckBalanceResponse> rxMtopResponse) {
        if (rxMtopResponse.isReqSuccess) {
            CheckBalanceResponse checkBalanceResponse = (CheckBalanceResponse) rxMtopResponse.result;
            if (checkBalanceResponse == null || TextUtils.isEmpty(checkBalanceResponse.getMobile())) {
                AppMonitor.Alarm.commitFail(APP_MONITOR_MODULE, APP_MONITOR_POINT, rxMtopResponse.retCode, "empty mobile number in success response");
                this.mView.displayToast((int) R.string.server_exception);
                return;
            }
            AppMonitor.Alarm.commitSuccess(APP_MONITOR_MODULE, APP_MONITOR_POINT);
            this.mView.goToWithdrawCodePage(Double.valueOf(d), checkBalanceResponse.getMobile(), checkBalanceResponse.getEmail(), checkBalanceResponse.getAlipayAccount());
        } else {
            onCheckBalanceError(d, rxMtopResponse);
        }
        this.mView.setWithdrawButtonEnabled(true);
    }

    public boolean checkWithdrawValue(double d, double d2) {
        if (d <= MIN_WITHDRAW_LIMIT_EXCLUSIVE) {
            this.mView.displayToast((int) R.string.error_withdraw_below_min_limit);
            return false;
        } else if (d > d2) {
            this.mView.displayToast((int) R.string.error_withdraw_over_balance);
            return false;
        } else {
            double round = (double) Math.round((d2 - d) * 100.0d);
            Double.isNaN(round);
            double d3 = round / 100.0d;
            if (d3 <= MIN_WITHDRAW_LIMIT_EXCLUSIVE && d3 > 0.0d) {
                this.mView.displayToast((int) R.string.error_withdraw_after_balance_below_min_limit);
                return false;
            } else if (d <= MAX_WITHDRAW_LIMIT) {
                return true;
            } else {
                this.mView.displayToast((int) R.string.error_withdraw_above_max_limit);
                return false;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0033  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0056  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onCheckBalanceError(double r8, @androidx.annotation.NonNull com.alimama.union.app.rxnetwork.RxMtopResponse<com.alimama.moon.features.reports.withdraw.network.CheckBalanceResponse> r10) {
        /*
            r7 = this;
            java.lang.String r0 = r10.retCode
            java.lang.String r1 = "Page_Account"
            java.lang.String r2 = "withdraw_btn_click"
            java.lang.String r3 = ""
            com.alibaba.mtl.appmonitor.AppMonitor.Alarm.commitFail(r1, r2, r0, r3)
            int r1 = r0.hashCode()
            r2 = -1683075510(0xffffffff9bae4e4a, float:-2.8836478E-22)
            if (r1 == r2) goto L_0x0025
            r2 = 506409058(0x1e2f3062, float:9.274428E-21)
            if (r1 == r2) goto L_0x001b
            goto L_0x002f
        L_0x001b:
            java.lang.String r1 = "FAIL_BIZ_CHECK_BALANCE_NOT_ALIPAY_BIND_MOBILE"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x002f
            r0 = 0
            goto L_0x0030
        L_0x0025:
            java.lang.String r1 = "FAIL_BIZ_CHECK_BALANCE_ALIPAY_NOT_BIND_MOBILE"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x002f
            r0 = 1
            goto L_0x0030
        L_0x002f:
            r0 = -1
        L_0x0030:
            switch(r0) {
                case 0: goto L_0x0056;
                case 1: goto L_0x004a;
                default: goto L_0x0033;
            }
        L_0x0033:
            com.alimama.moon.App r8 = com.alimama.moon.App.sApplication
            boolean r8 = com.alimama.moon.utils.PhoneInfo.isNetworkAvailable(r8)
            if (r8 != 0) goto L_0x0074
            com.alimama.moon.features.reports.withdraw.contracts.ICheckBalanceContract$IView r8 = r7.mView
            com.alimama.moon.App r9 = com.alimama.moon.App.sApplication
            r10 = 2131296751(0x7f0901ef, float:1.8211428E38)
            java.lang.String r9 = r9.getString(r10)
            r8.displayToast((java.lang.String) r9)
            goto L_0x007b
        L_0x004a:
            com.alimama.moon.features.reports.withdraw.contracts.ICheckBalanceContract$IView r8 = r7.mView
            r9 = 2131296565(0x7f090135, float:1.821105E38)
            r10 = 2131296551(0x7f090127, float:1.8211022E38)
            r8.goToErrorPage(r9, r10)
            goto L_0x007b
        L_0x0056:
            T r10 = r10.result
            com.alimama.moon.features.reports.withdraw.network.CheckBalanceResponse r10 = (com.alimama.moon.features.reports.withdraw.network.CheckBalanceResponse) r10
            if (r10 != 0) goto L_0x005d
            return
        L_0x005d:
            java.lang.String r4 = r10.getMobile()
            java.lang.String r3 = r10.getAlipayMobile()
            java.lang.String r6 = r10.getAlipayAccount()
            com.alimama.moon.features.reports.withdraw.contracts.ICheckBalanceContract$IView r0 = r7.mView
            java.lang.String r5 = r10.getEmail()
            r1 = r8
            r0.goToChangeNumberPage(r1, r3, r4, r5, r6)
            goto L_0x007b
        L_0x0074:
            com.alimama.moon.features.reports.withdraw.contracts.ICheckBalanceContract$IView r8 = r7.mView
            java.lang.String r9 = r10.retMsg
            r8.displayToast((java.lang.String) r9)
        L_0x007b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.features.reports.withdraw.CheckBalancePresenter.onCheckBalanceError(double, com.alimama.union.app.rxnetwork.RxMtopResponse):void");
    }
}
