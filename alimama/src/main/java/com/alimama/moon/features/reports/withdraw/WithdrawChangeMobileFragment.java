package com.alimama.moon.features.reports.withdraw;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alimama.moon.R;
import com.alimama.moon.features.reports.withdraw.network.ChangeMobileRequest;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.utils.ActivityUtil;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;

public class WithdrawChangeMobileFragment extends BaseFragment {
    private static final String KEY_ARG_ALIMAMA_MOBILE = "KEY_ARG_ALIMAMA_MOBILE";
    private static final String KEY_ARG_ALIPAY_ACCOUNT = "KEY_ARG_ALIPAY_ACCOUNT";
    private static final String KEY_ARG_ALIPAY_MOBILE = "KEY_ARG_ALIPAY_MOBILE";
    private static final String KEY_ARG_EMAIL = "KEY_ARG_EMAIL";
    private static final String KEY_ARG_WITHDRAW_VAL = "KEY_ARG_WITHDRAW_VAL";
    public static final String TAG = "WithdrawChangeMobileFragment";

    public static WithdrawChangeMobileFragment newInstance(double d, @NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4) {
        WithdrawChangeMobileFragment withdrawChangeMobileFragment = new WithdrawChangeMobileFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(KEY_ARG_WITHDRAW_VAL, d);
        bundle.putString(KEY_ARG_ALIMAMA_MOBILE, str2);
        bundle.putString(KEY_ARG_ALIPAY_MOBILE, str);
        bundle.putString(KEY_ARG_EMAIL, str3);
        bundle.putString(KEY_ARG_ALIPAY_ACCOUNT, str4);
        withdrawChangeMobileFragment.setArguments(bundle);
        return withdrawChangeMobileFragment;
    }

    public View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.layout_withdraw_change_mobile, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.change_mobile_tips);
        TextView textView2 = (TextView) view.findViewById(R.id.change_mobile_phone);
        final Bundle arguments = getArguments();
        String string = arguments.getString(KEY_ARG_ALIMAMA_MOBILE);
        final String string2 = arguments.getString(KEY_ARG_ALIPAY_MOBILE);
        final String string3 = arguments.getString(KEY_ARG_EMAIL);
        final String string4 = arguments.getString(KEY_ARG_ALIPAY_ACCOUNT);
        if (TextUtils.isEmpty(string)) {
            textView.setText(getString(R.string.withdraw_tips_missing_alimama_mobile));
        } else {
            textView.setText(getString(R.string.withdraw_change_mobile_tips, StringUtil.maskPrivacyInfo(string)));
        }
        textView2.setText(StringUtil.maskPrivacyInfo(string2));
        ((Button) view.findViewById(R.id.withdraw_ok_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TBS.Page.ctrlClicked(CT.Button, "WithDrawSuccessBtn");
                new ChangeMobileRequest().sendRequest(new RxMtopRequest.RxMtopResult<Void>() {
                    public void result(RxMtopResponse<Void> rxMtopResponse) {
                        if (rxMtopResponse.isReqSuccess) {
                            if (WithdrawChangeMobileFragment.this.getActivity() != null && !WithdrawChangeMobileFragment.this.getActivity().isFinishing()) {
                                ToastUtil.toast((Context) WithdrawChangeMobileFragment.this.getActivity(), (int) R.string.withdraw_update_mobile_number_success);
                                WithdrawChangeMobileFragment.this.goToWithdrawCodePage(arguments.getDouble(WithdrawChangeMobileFragment.KEY_ARG_WITHDRAW_VAL), string2, string3, string4);
                            }
                        } else if (WithdrawChangeMobileFragment.this.getActivity() != null && !WithdrawChangeMobileFragment.this.getActivity().isFinishing()) {
                            ToastUtil.toast((Context) WithdrawChangeMobileFragment.this.getActivity(), (CharSequence) rxMtopResponse.retMsg);
                        }
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void goToWithdrawCodePage(double d, String str, String str2, String str3) {
        ActivityUtil.displayFragment(getActivity(), R.id.fragment_container, WithdrawCodeFragment.newInstance(d, str, str2, str3), WithdrawCodeFragment.TAG);
    }
}
