package com.alimama.moon.features.reports.withdraw;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alimama.moon.R;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.utils.StringUtil;
import com.alimama.union.app.logger.BusinessMonitorLogger;

public class WithdrawResultFragment extends BaseFragment {
    private static final String ARGS_KEY_ALIPAY_ACCOUNT = "ARGS_KEY_ALIPAY_ACCOUNT";
    private static final String ARGS_KEY_AMOUNT = "ARGS_KEY_AMOUNT";
    private static final String ARGS_KEY_BALANCE = "ARGS_KEY_BALANCE";
    public static final String TAG = "WithdrawResultFragment";

    public static WithdrawResultFragment newInstance(double d, double d2, @NonNull String str) {
        WithdrawResultFragment withdrawResultFragment = new WithdrawResultFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(ARGS_KEY_BALANCE, d2);
        bundle.putString(ARGS_KEY_ALIPAY_ACCOUNT, str);
        bundle.putDouble(ARGS_KEY_AMOUNT, d);
        withdrawResultFragment.setArguments(bundle);
        BusinessMonitorLogger.WithdrawCash.withdrawSuccess(TAG, d, d2, str);
        return withdrawResultFragment;
    }

    public View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.layout_withdraw_result, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    private void initView(View view) {
        String str = getText(R.string.withdraw_success_balance) + StringUtil.twoDecimalStr(Double.valueOf(getArguments().getDouble(ARGS_KEY_BALANCE)));
        String str2 = getText(R.string.withdraw_success_amount) + StringUtil.twoDecimalStr(Double.valueOf(getArguments().getDouble(ARGS_KEY_AMOUNT)));
        String str3 = getText(R.string.withdraw_success_account) + StringUtil.maskPrivacyInfo(getArguments().getString(ARGS_KEY_ALIPAY_ACCOUNT));
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.radio_button_selected_color)), getText(R.string.withdraw_success_balance).length() - 1, str.length(), 33);
        ((TextView) view.findViewById(R.id.tv_balance)).setText(spannableString);
        SpannableString spannableString2 = new SpannableString(str2);
        spannableString2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_black)), getText(R.string.withdraw_success_amount).length() - 1, str2.length(), 33);
        ((TextView) view.findViewById(R.id.tv_amount)).setText(spannableString2);
        SpannableString spannableString3 = new SpannableString(str3);
        spannableString3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_black)), getText(R.string.withdraw_success_account).length(), str3.length(), 33);
        ((TextView) view.findViewById(R.id.tv_account)).setText(spannableString3);
    }
}
