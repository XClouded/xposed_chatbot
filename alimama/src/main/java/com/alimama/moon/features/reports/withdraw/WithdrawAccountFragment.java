package com.alimama.moon.features.reports.withdraw;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.alimama.moon.R;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.features.reports.ErrorInfoFragment;
import com.alimama.moon.features.reports.withdraw.contracts.ICheckBalanceContract;
import com.alimama.moon.features.reports.withdraw.network.CheckBalanceRequest;
import com.alimama.moon.features.reports.withdraw.network.CheckBalanceResponse;
import com.alimama.moon.features.reports.withdraw.network.GetBalanceResponse;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.utils.ActivityUtil;
import com.alimama.moon.utils.AliLog;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import org.apache.commons.cli.HelpFormatter;

public class WithdrawAccountFragment extends BaseFragment implements RxMtopRequest.RxMtopResult<CheckBalanceResponse>, ICheckBalanceContract.IView {
    private static final String ARGS_KEY_BALANCE = "ARGS_KEY_BALANCE";
    public static final String TAG = "WithdrawAccountFragment";
    private WithdrawActivity mActivity;
    private TextView mAlipayAccountTextView;
    /* access modifiers changed from: private */
    public Double mBalance = null;
    private TextView mBalanceTextView;
    private CheckBalanceRequest mCheckBalanceRequest;
    /* access modifiers changed from: private */
    public EditText mEditText;
    /* access modifiers changed from: private */
    public ICheckBalanceContract.IPresenter mPresenter;
    /* access modifiers changed from: private */
    public Button mWithdrawBtn;
    @Nullable
    private Double mWithdrawNum = null;

    public static WithdrawAccountFragment newInstance(@NonNull GetBalanceResponse getBalanceResponse) {
        WithdrawAccountFragment withdrawAccountFragment = new WithdrawAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_KEY_BALANCE, getBalanceResponse);
        withdrawAccountFragment.setArguments(bundle);
        return withdrawAccountFragment;
    }

    public View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_withdraw_input_default, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mActivity = (WithdrawActivity) getActivity();
        this.mPresenter = new CheckBalancePresenter(this);
    }

    private void initView(View view) {
        this.mAlipayAccountTextView = (TextView) view.findViewById(R.id.tv_alipay_account);
        this.mEditText = (EditText) view.findViewById(R.id.edt_withdraw_input);
        this.mEditText.setEnabled(false);
        this.mEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                WithdrawAccountFragment.this.mWithdrawBtn.setEnabled(!TextUtils.isEmpty(WithdrawAccountFragment.this.mEditText.getText()));
            }
        });
        AnonymousClass2 r0 = new InputFilter() {
            public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
                if (TextUtils.isEmpty(charSequence)) {
                    return null;
                }
                try {
                    String str = spanned.subSequence(0, i3).toString() + charSequence.subSequence(i, i2) + spanned.subSequence(i4, spanned.length());
                    int indexOf = str.indexOf(".");
                    if (indexOf >= 0 && str.substring(indexOf + 1).length() > 2) {
                        return "";
                    }
                    return null;
                } catch (IndexOutOfBoundsException e) {
                    Log.e(WithdrawAccountFragment.TAG, "InputFilter exception for source: " + charSequence + ", dest: " + spanned, e);
                    return null;
                }
            }
        };
        this.mEditText.setFilters(new InputFilter[]{r0});
        this.mBalanceTextView = (TextView) view.findViewById(R.id.tv_available_balance);
        this.mWithdrawBtn = (Button) view.findViewById(R.id.btn_withdraw);
        this.mWithdrawBtn.setEnabled(false);
        this.mWithdrawBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TBS.Page.ctrlClicked(CT.Button, "WithDrawBtn1");
                BusinessMonitorLogger.WithdrawCash.doWithdraw(WithdrawAccountFragment.TAG, "click the withdraw btn");
                try {
                    double parseDouble = Double.parseDouble(WithdrawAccountFragment.this.mEditText.getText().toString());
                    if (WithdrawAccountFragment.this.mPresenter.checkWithdrawValue(parseDouble, WithdrawAccountFragment.this.mBalance.doubleValue())) {
                        WithdrawAccountFragment.this.mWithdrawBtn.setEnabled(false);
                        WithdrawAccountFragment.this.checkBalance(Double.valueOf(parseDouble));
                    }
                } catch (Exception e) {
                    AliLog.LogE(e.toString());
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        loadAccountData();
    }

    public void onPause() {
        super.onPause();
        updateBalance((Double) null);
    }

    public void goToWithdrawCodePage(@NonNull Double d, @NonNull String str, @NonNull String str2, @Nullable String str3) {
        ActivityUtil.displayFragment(getActivity(), R.id.fragment_container, WithdrawCodeFragment.newInstance(d.doubleValue(), str, str2, str3), WithdrawCodeFragment.TAG);
    }

    public void goToChangeNumberPage(double d, @NonNull String str, @NonNull String str2, @Nullable String str3, @Nullable String str4) {
        ActivityUtil.displayFragment(getActivity(), R.id.fragment_container, WithdrawChangeMobileFragment.newInstance(d, str, str2, str3, str4), WithdrawChangeMobileFragment.TAG);
    }

    public void goToErrorPage(@StringRes int i, @StringRes int i2) {
        ActivityUtil.displayFragment(getActivity(), R.id.fragment_container, ErrorInfoFragment.newInstance(getString(i), getString(i2)), ErrorInfoFragment.TAG);
    }

    public void displayToast(@NonNull String str) {
        ToastUtil.showToast(getContext(), str);
    }

    public void displayToast(int i) {
        ToastUtil.showToast(getContext(), i);
    }

    public void setWithdrawButtonEnabled(boolean z) {
        this.mWithdrawBtn.setEnabled(z);
    }

    public void result(RxMtopResponse<CheckBalanceResponse> rxMtopResponse) {
        if (this.mWithdrawNum == null) {
            Log.e(TAG, "error! error! run run run");
        } else {
            this.mPresenter.onCheckBalanceResponse(this.mWithdrawNum.doubleValue(), rxMtopResponse);
        }
    }

    private void updateBalance(Double d) {
        boolean z = false;
        if (d == null) {
            this.mBalanceTextView.setText(HelpFormatter.DEFAULT_LONG_OPT_PREFIX);
            this.mEditText.setText("");
            this.mWithdrawBtn.setEnabled(false);
            this.mEditText.setEnabled(false);
        } else {
            this.mBalanceTextView.setText(getString(R.string.withdraw_available_balance_with_num, StringUtil.twoDecimalStr(d)));
            String str = "" + StringUtil.twoDecimalStr(Double.valueOf(Math.min(d.doubleValue(), 500000.0d)));
            this.mEditText.setText(str);
            this.mEditText.setSelection(str.length());
            this.mEditText.setEnabled(d.doubleValue() > 0.0d);
            Button button = this.mWithdrawBtn;
            if (d.doubleValue() > 0.0d) {
                z = true;
            }
            button.setEnabled(z);
        }
        this.mBalance = d;
    }

    private void loadAccountData() {
        if (((SettingManager) BeanContext.get(SettingManager.class)).getCurUserPref() == null) {
            this.mActivity.goToLoginActivity();
            return;
        }
        Bundle arguments = getArguments();
        if (arguments == null) {
            updateBalance((Double) null);
            return;
        }
        GetBalanceResponse getBalanceResponse = (GetBalanceResponse) arguments.getParcelable(ARGS_KEY_BALANCE);
        if (getBalanceResponse == null || getBalanceResponse.getBalance() == null) {
            updateBalance((Double) null);
            return;
        }
        updateBalance(getBalanceResponse.getBalance());
        this.mAlipayAccountTextView.setText(StringUtil.maskPrivacyInfo(getBalanceResponse.getAlipayAccount()));
    }

    /* access modifiers changed from: private */
    public void checkBalance(Double d) {
        this.mWithdrawNum = d;
        if (((SettingManager) BeanContext.get(SettingManager.class)).getCurUserPref() == null) {
            this.mActivity.goToLoginActivity();
            return;
        }
        if (this.mCheckBalanceRequest != null) {
            this.mCheckBalanceRequest.setRxMtopResult((RxMtopRequest.RxMtopResult) null);
            this.mCheckBalanceRequest = null;
        }
        this.mCheckBalanceRequest = new CheckBalanceRequest(d.doubleValue());
        this.mCheckBalanceRequest.sendRequest(this);
    }
}
