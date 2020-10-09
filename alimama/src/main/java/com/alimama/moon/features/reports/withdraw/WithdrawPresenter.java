package com.alimama.moon.features.reports.withdraw;

import android.content.Context;
import androidx.annotation.NonNull;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alimama.moon.features.reports.withdraw.contracts.IWithdrawContract;
import com.alimama.moon.features.reports.withdraw.network.GetBalanceResponse;
import com.alimama.union.app.rxnetwork.RxMtopResponse;

class WithdrawPresenter implements IWithdrawContract.IPresenter {
    private static final String APP_MONITOR_MODULE = "Page_Account";
    private static final String APP_MONITOR_POINT = "display_can_withdraw_money";
    @NonNull
    private final IWithdrawContract.IView mView;

    WithdrawPresenter(@NonNull IWithdrawContract.IView iView) {
        this.mView = iView;
    }

    public void onGetBalanceResponse(@NonNull RxMtopResponse<GetBalanceResponse> rxMtopResponse, @NonNull Context context) {
        if (rxMtopResponse.isReqSuccess) {
            GetBalanceResponse getBalanceResponse = (GetBalanceResponse) rxMtopResponse.result;
            if (getBalanceResponse == null || getBalanceResponse.getBalance() == null) {
                AppMonitor.Alarm.commitFail(APP_MONITOR_MODULE, APP_MONITOR_POINT, rxMtopResponse.retCode, "empty balance");
                this.mView.displayRetryError();
                return;
            }
            AppMonitor.Alarm.commitSuccess(APP_MONITOR_MODULE, APP_MONITOR_POINT);
            this.mView.goToWithdrawAccountPage(getBalanceResponse);
            return;
        }
        AppMonitor.Alarm.commitFail(APP_MONITOR_MODULE, APP_MONITOR_POINT, rxMtopResponse.retCode, "");
        onGetBalanceError(rxMtopResponse, context);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onGetBalanceError(@androidx.annotation.NonNull com.alimama.union.app.rxnetwork.RxMtopResponse<com.alimama.moon.features.reports.withdraw.network.GetBalanceResponse> r8, @androidx.annotation.NonNull android.content.Context r9) {
        /*
            r7 = this;
            java.lang.String r0 = r8.retCode
            int r1 = r0.hashCode()
            r2 = 0
            r3 = 1
            switch(r1) {
                case -1635113663: goto L_0x003e;
                case -1542387391: goto L_0x0034;
                case -934697366: goto L_0x002a;
                case -685022026: goto L_0x0020;
                case 1986346354: goto L_0x0016;
                case 1994720996: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x0048
        L_0x000c:
            java.lang.String r1 = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ALIPAY_NOTEQ"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0048
            r0 = 3
            goto L_0x0049
        L_0x0016:
            java.lang.String r1 = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ALIPAY_EMPTY"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0048
            r0 = 4
            goto L_0x0049
        L_0x0020:
            java.lang.String r1 = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ACCOUNT_ALIPAY"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0048
            r0 = 5
            goto L_0x0049
        L_0x002a:
            java.lang.String r1 = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ACCOUNT_V"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0048
            r0 = 0
            goto L_0x0049
        L_0x0034:
            java.lang.String r1 = "FAIL_BIZ_WITH_DRAW_LIMIT_STATUS_ACCOUNT_EMPTY"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0048
            r0 = 1
            goto L_0x0049
        L_0x003e:
            java.lang.String r1 = "FAIL_BIZ_SEND_MOBILE_CODE_CHENG_LING_RECORD_NOT_EXIST"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0048
            r0 = 2
            goto L_0x0049
        L_0x0048:
            r0 = -1
        L_0x0049:
            switch(r0) {
                case 0: goto L_0x00d7;
                case 1: goto L_0x00d7;
                case 2: goto L_0x00cb;
                case 3: goto L_0x00bf;
                case 4: goto L_0x005e;
                case 5: goto L_0x0052;
                default: goto L_0x004c;
            }
        L_0x004c:
            com.alimama.moon.features.reports.withdraw.contracts.IWithdrawContract$IView r8 = r7.mView
            r8.displayRetryError()
            return
        L_0x0052:
            com.alimama.moon.features.reports.withdraw.contracts.IWithdrawContract$IView r8 = r7.mView
            r9 = 2131296564(0x7f090134, float:1.8211048E38)
            r0 = 2131296550(0x7f090126, float:1.821102E38)
            r8.displayWithdrawError((int) r9, (int) r0)
            return
        L_0x005e:
            T r8 = r8.result
            com.alimama.moon.features.reports.withdraw.network.GetBalanceResponse r8 = (com.alimama.moon.features.reports.withdraw.network.GetBalanceResponse) r8
            r0 = 2131296569(0x7f090139, float:1.8211058E38)
            if (r8 == 0) goto L_0x00b6
            java.lang.String r8 = r8.getAlipayAccount()
            java.lang.String r8 = com.alimama.moon.utils.StringUtil.maskPrivacyInfo(r8)
            boolean r1 = android.text.TextUtils.isEmpty(r8)
            if (r1 != 0) goto L_0x00b6
            r1 = 2131296556(0x7f09012c, float:1.8211032E38)
            java.lang.Object[] r4 = new java.lang.Object[r3]
            r4[r2] = r8
            java.lang.String r1 = r9.getString(r1, r4)
            int r2 = r1.indexOf(r8)
            android.text.SpannableString r4 = new android.text.SpannableString
            r4.<init>(r1)
            android.text.style.StyleSpan r1 = new android.text.style.StyleSpan
            r1.<init>(r3)
            android.text.style.ForegroundColorSpan r3 = new android.text.style.ForegroundColorSpan
            r5 = 2131624298(0x7f0e016a, float:1.8875772E38)
            int r5 = androidx.core.content.ContextCompat.getColor(r9, r5)
            r3.<init>(r5)
            int r5 = r8.length()
            int r5 = r5 + r2
            r6 = 33
            r4.setSpan(r3, r2, r5, r6)
            int r8 = r8.length()
            int r8 = r8 + r2
            r4.setSpan(r1, r2, r8, r6)
            com.alimama.moon.features.reports.withdraw.contracts.IWithdrawContract$IView r8 = r7.mView
            java.lang.String r9 = r9.getString(r0)
            r8.displayWithdrawError((java.lang.CharSequence) r9, (java.lang.CharSequence) r4)
            return
        L_0x00b6:
            com.alimama.moon.features.reports.withdraw.contracts.IWithdrawContract$IView r8 = r7.mView
            r9 = 2131296555(0x7f09012b, float:1.821103E38)
            r8.displayWithdrawError((int) r0, (int) r9)
            return
        L_0x00bf:
            com.alimama.moon.features.reports.withdraw.contracts.IWithdrawContract$IView r8 = r7.mView
            r9 = 2131296566(0x7f090136, float:1.8211052E38)
            r0 = 2131296552(0x7f090128, float:1.8211024E38)
            r8.displayWithdrawError((int) r9, (int) r0)
            return
        L_0x00cb:
            com.alimama.moon.features.reports.withdraw.contracts.IWithdrawContract$IView r8 = r7.mView
            r9 = 2131296568(0x7f090138, float:1.8211056E38)
            r0 = 2131296554(0x7f09012a, float:1.8211028E38)
            r8.displayWithdrawError((int) r9, (int) r0)
            return
        L_0x00d7:
            com.alimama.moon.features.reports.withdraw.contracts.IWithdrawContract$IView r8 = r7.mView
            r9 = 2131296567(0x7f090137, float:1.8211054E38)
            r0 = 2131296553(0x7f090129, float:1.8211026E38)
            r8.displayWithdrawError((int) r9, (int) r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.features.reports.withdraw.WithdrawPresenter.onGetBalanceError(com.alimama.union.app.rxnetwork.RxMtopResponse, android.content.Context):void");
    }
}
