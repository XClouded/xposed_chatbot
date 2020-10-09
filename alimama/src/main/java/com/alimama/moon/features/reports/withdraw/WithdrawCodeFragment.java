package com.alimama.moon.features.reports.withdraw;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alimama.moon.R;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.features.reports.withdraw.network.SendMobileCodeRequest;
import com.alimama.moon.features.reports.withdraw.network.WithdrawRequest;
import com.alimama.moon.features.reports.withdraw.network.WithdrawResponse;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.utils.PhoneInfo;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimama.union.app.views.AlertMsgDialog;
import com.alimama.union.app.views.LoadingDialog;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WithdrawCodeFragment extends BaseFragment {
    private static final int CODE_COUNT = 6;
    private static final String KEY_ARG_ALIPAY_ACCOUNT = "KEY_ARG_ALIPAY_ACCOUNT";
    private static final String KEY_ARG_EMAIL = "KEY_ARG_EMAIL";
    private static final String KEY_ARG_MOBILE = "KEY_ARG_MOBILE";
    private static final String KEY_ARG_WITHDRAW_VAL = "KEY_ARG_WITHDRAW_VAL";
    private static final String RETCODE_CODE_EXPIRED = "FAIL_BIZ_WITH_DRAW_CODE_EXPIRED";
    private static final String RETCODE_CODE_NOT_RIGHT = "FAIL_BIZ_WITH_DRAW_CODE_NOT_RIGHT";
    private static final String RETCODE_NOT_ENOUGH_BALANCE = "FAIL_BIZ_WITH_DRAW_BALANCE_NOT_ENOUGH";
    public static final String TAG = "WithdrawCodeFragment";
    private static final int WAIT_COUNT = 90;
    /* access modifiers changed from: private */
    public String email;
    private Logger logger = LoggerFactory.getLogger((Class<?>) WithdrawCodeFragment.class);
    /* access modifiers changed from: private */
    public WithdrawActivity mActivity;
    /* access modifiers changed from: private */
    public EditText mEditText;
    private TextView mEmailTextView;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public LoadingDialog mLoadingDialog;
    private TextView mPhoneTextView;
    /* access modifiers changed from: private */
    public Button mSendCodeBtn;
    /* access modifiers changed from: private */
    public Timer mTimer;
    /* access modifiers changed from: private */
    public Button mWithdrawBtn;
    /* access modifiers changed from: private */
    public int timerCount = 90;
    private double withdrawVale = 0.0d;

    static /* synthetic */ int access$810(WithdrawCodeFragment withdrawCodeFragment) {
        int i = withdrawCodeFragment.timerCount;
        withdrawCodeFragment.timerCount = i - 1;
        return i;
    }

    public static WithdrawCodeFragment newInstance(double d, @NonNull String str, @NonNull String str2, @NonNull String str3) {
        WithdrawCodeFragment withdrawCodeFragment = new WithdrawCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(KEY_ARG_WITHDRAW_VAL, d);
        bundle.putString(KEY_ARG_MOBILE, str);
        bundle.putString(KEY_ARG_EMAIL, str2);
        bundle.putString(KEY_ARG_ALIPAY_ACCOUNT, str3);
        withdrawCodeFragment.setArguments(bundle);
        return withdrawCodeFragment;
    }

    public View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.layout_withdraw_code, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mActivity = (WithdrawActivity) getActivity();
        this.mHandler = new Handler();
        this.withdrawVale = getArguments().getDouble(KEY_ARG_WITHDRAW_VAL);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }

    private void initView(View view) {
        this.mEditText = (EditText) view.findViewById(R.id.code_input_et);
        this.mEmailTextView = (TextView) view.findViewById(R.id.tv_email);
        this.email = getArguments().getString(KEY_ARG_EMAIL);
        SpannableString spannableString = new SpannableString(getString(R.string.code_input_hint_str));
        spannableString.setSpan(new AbsoluteSizeSpan(14, true), 0, spannableString.length(), 33);
        this.mEditText.setHint(new SpannedString(spannableString));
        this.mEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String obj = WithdrawCodeFragment.this.mEditText.getText().toString();
                WithdrawCodeFragment.this.mWithdrawBtn.setEnabled(obj != null && obj.length() == 6);
            }
        });
        this.mPhoneTextView = (TextView) view.findViewById(R.id.phone_tv);
        this.mPhoneTextView.setText(StringUtil.maskPrivacyInfo(getArguments().getString(KEY_ARG_MOBILE)));
        this.mWithdrawBtn = (Button) view.findViewById(R.id.withdraw_code_btn);
        this.mWithdrawBtn.setEnabled(false);
        this.mWithdrawBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TBS.Page.ctrlClicked(CT.Button, "WithDrawBtn2");
                BusinessMonitorLogger.WithdrawCash.submit(WithdrawCodeFragment.TAG, "click the submit btn");
                LoadingDialog unused = WithdrawCodeFragment.this.mLoadingDialog = new LoadingDialog(view.getContext(), R.style.common_dialog_style);
                WithdrawCodeFragment.this.mLoadingDialog.show();
                String obj = WithdrawCodeFragment.this.mEditText.getText().toString();
                if (!StringUtil.isEmpty(obj) && obj.length() == 6) {
                    WithdrawCodeFragment.this.doWthdraw();
                }
            }
        });
        this.mSendCodeBtn = (Button) view.findViewById(R.id.code_send_btn);
        this.mSendCodeBtn.setText(R.string.code_send_str);
        this.mSendCodeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TBS.Page.ctrlClicked(CT.Button, "SendMobileCodeBtn");
                BusinessMonitorLogger.WithdrawCash.getAuthenticationCode(WithdrawCodeFragment.TAG);
                WithdrawCodeFragment.this.sendMobileCode();
            }
        });
        if (!TextUtils.isEmpty(this.email)) {
            this.mEmailTextView.setVisibility(0);
            this.mEmailTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    BusinessMonitorLogger.WithdrawCash.receiveAuthenticationCodeFailed(WithdrawCodeFragment.TAG);
                    new AlertMsgDialog(view.getContext()).title(R.string.withdraw_email_tips_title).content(WithdrawCodeFragment.this.getContext().getString(R.string.withdraw_email_tips_content, new Object[]{StringUtil.maskPrivacyInfo(WithdrawCodeFragment.this.email)})).positiveButtonText(R.string.confirm_okay).show();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void doWthdraw() {
        if (((SettingManager) BeanContext.get(SettingManager.class)).getCurUserPref() == null) {
            this.mActivity.goToLoginActivity();
            return;
        }
        this.mWithdrawBtn.setEnabled(false);
        new WithdrawRequest(this.withdrawVale, this.mEditText.getText().toString()).sendRequest(new RxMtopRequest.RxMtopResult<WithdrawResponse>() {
            /* JADX WARNING: Code restructure failed: missing block: B:25:0x00ba, code lost:
                if (r0.equals(com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.RETCODE_CODE_EXPIRED) != false) goto L_0x00be;
             */
            /* JADX WARNING: Removed duplicated region for block: B:28:0x00c1  */
            /* JADX WARNING: Removed duplicated region for block: B:29:0x00cd  */
            /* JADX WARNING: Removed duplicated region for block: B:30:0x00da  */
            /* JADX WARNING: Removed duplicated region for block: B:31:0x00e7  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void result(com.alimama.union.app.rxnetwork.RxMtopResponse<com.alimama.moon.features.reports.withdraw.network.WithdrawResponse> r8) {
                /*
                    r7 = this;
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r0 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    com.alimama.union.app.views.LoadingDialog r0 = r0.mLoadingDialog
                    r0.dismiss()
                    boolean r0 = r8.isReqSuccess
                    r1 = 1
                    if (r0 == 0) goto L_0x0067
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r0 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    android.widget.Button r0 = r0.mWithdrawBtn
                    r0.setEnabled(r1)
                    T r0 = r8.result
                    if (r0 == 0) goto L_0x00f3
                    T r8 = r8.result
                    com.alimama.moon.features.reports.withdraw.network.WithdrawResponse r8 = (com.alimama.moon.features.reports.withdraw.network.WithdrawResponse) r8
                    java.lang.Double r8 = r8.getAmount()
                    if (r8 != 0) goto L_0x0040
                    java.lang.String r8 = "WithdrawCodeFragment"
                    java.lang.String r0 = "submitFailed, amount==null"
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r1 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    r2 = 2131296812(0x7f09022c, float:1.8211551E38)
                    java.lang.String r1 = r1.getString(r2)
                    com.alimama.union.app.logger.BusinessMonitorLogger.WithdrawCash.submitFailed(r8, r0, r1)
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r8 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    com.alimama.moon.features.reports.withdraw.WithdrawActivity r8 = r8.mActivity
                    com.alimama.moon.utils.ToastUtil.toast((android.content.Context) r8, (int) r2)
                    goto L_0x00f3
                L_0x0040:
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r0 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    com.alimama.moon.features.reports.withdraw.WithdrawActivity r1 = r0.mActivity
                    double r2 = r8.doubleValue()
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r8 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    android.os.Bundle r8 = r8.getArguments()
                    java.lang.String r0 = "KEY_ARG_WITHDRAW_VAL"
                    double r4 = r8.getDouble(r0)
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r8 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    android.os.Bundle r8 = r8.getArguments()
                    java.lang.String r0 = "KEY_ARG_ALIPAY_ACCOUNT"
                    java.lang.String r6 = r8.getString(r0)
                    r1.goToWithdrawResultPage(r2, r4, r6)
                    goto L_0x00f3
                L_0x0067:
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r0 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    android.widget.Button r0 = r0.mWithdrawBtn
                    r0.setEnabled(r1)
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r0 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    com.alimama.moon.features.reports.withdraw.WithdrawActivity r0 = r0.mActivity
                    boolean r0 = com.alimama.moon.utils.PhoneInfo.isNetworkAvailable(r0)
                    if (r0 != 0) goto L_0x0089
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r8 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    com.alimama.moon.features.reports.withdraw.WithdrawActivity r8 = r8.mActivity
                    r0 = 2131296751(0x7f0901ef, float:1.8211428E38)
                    com.alimama.moon.utils.ToastUtil.toast((android.content.Context) r8, (int) r0)
                    return
                L_0x0089:
                    java.lang.String r0 = r8.retCode
                    r2 = -1
                    int r3 = r0.hashCode()
                    r4 = -766982878(0xffffffffd248c522, float:-2.155752E11)
                    if (r3 == r4) goto L_0x00b4
                    r1 = -338769843(0xffffffffebcec84d, float:-4.9996923E26)
                    if (r3 == r1) goto L_0x00aa
                    r1 = 494628110(0x1d7b6d0e, float:3.3275953E-21)
                    if (r3 == r1) goto L_0x00a0
                    goto L_0x00bd
                L_0x00a0:
                    java.lang.String r1 = "FAIL_BIZ_WITH_DRAW_BALANCE_NOT_ENOUGH"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto L_0x00bd
                    r1 = 2
                    goto L_0x00be
                L_0x00aa:
                    java.lang.String r1 = "FAIL_BIZ_WITH_DRAW_CODE_NOT_RIGHT"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto L_0x00bd
                    r1 = 0
                    goto L_0x00be
                L_0x00b4:
                    java.lang.String r3 = "FAIL_BIZ_WITH_DRAW_CODE_EXPIRED"
                    boolean r0 = r0.equals(r3)
                    if (r0 == 0) goto L_0x00bd
                    goto L_0x00be
                L_0x00bd:
                    r1 = -1
                L_0x00be:
                    switch(r1) {
                        case 0: goto L_0x00e7;
                        case 1: goto L_0x00da;
                        case 2: goto L_0x00cd;
                        default: goto L_0x00c1;
                    }
                L_0x00c1:
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r0 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    com.alimama.moon.features.reports.withdraw.WithdrawActivity r0 = r0.mActivity
                    java.lang.String r8 = r8.retMsg
                    com.alimama.moon.utils.ToastUtil.toast((android.content.Context) r0, (java.lang.CharSequence) r8)
                    goto L_0x00f3
                L_0x00cd:
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r8 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    com.alimama.moon.features.reports.withdraw.WithdrawActivity r8 = r8.mActivity
                    r0 = 2131296573(0x7f09013d, float:1.8211066E38)
                    com.alimama.moon.utils.ToastUtil.toast((android.content.Context) r8, (int) r0)
                    goto L_0x00f3
                L_0x00da:
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r8 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    com.alimama.moon.features.reports.withdraw.WithdrawActivity r8 = r8.mActivity
                    r0 = 2131296575(0x7f09013f, float:1.821107E38)
                    com.alimama.moon.utils.ToastUtil.toast((android.content.Context) r8, (int) r0)
                    goto L_0x00f3
                L_0x00e7:
                    com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment r8 = com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.this
                    com.alimama.moon.features.reports.withdraw.WithdrawActivity r8 = r8.mActivity
                    r0 = 2131296576(0x7f090140, float:1.8211073E38)
                    com.alimama.moon.utils.ToastUtil.toast((android.content.Context) r8, (int) r0)
                L_0x00f3:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.features.reports.withdraw.WithdrawCodeFragment.AnonymousClass5.result(com.alimama.union.app.rxnetwork.RxMtopResponse):void");
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendMobileCode() {
        if (((SettingManager) BeanContext.get(SettingManager.class)).getCurUserPref() == null) {
            this.mActivity.goToLoginActivity();
            return;
        }
        new SendMobileCodeRequest().sendRequest(new RxMtopRequest.RxMtopResult<Void>() {
            public void result(RxMtopResponse<Void> rxMtopResponse) {
                if (rxMtopResponse.isReqSuccess) {
                    WithdrawCodeFragment.this.mSendCodeBtn.setEnabled(false);
                    int unused = WithdrawCodeFragment.this.timerCount = 90;
                    WithdrawCodeFragment.this.mSendCodeBtn.setText(WithdrawCodeFragment.this.setSendBtnTextStyle(WithdrawCodeFragment.this.timerCount));
                    AnonymousClass1 r2 = new TimerTask() {
                        public void run() {
                            WithdrawCodeFragment.this.mHandler.post(new Runnable() {
                                public void run() {
                                    if (WithdrawCodeFragment.this.isAdded()) {
                                        WithdrawCodeFragment.access$810(WithdrawCodeFragment.this);
                                        if (WithdrawCodeFragment.this.timerCount > 0) {
                                            WithdrawCodeFragment.this.mSendCodeBtn.setText(WithdrawCodeFragment.this.setSendBtnTextStyle(WithdrawCodeFragment.this.timerCount));
                                            return;
                                        }
                                        WithdrawCodeFragment.this.mSendCodeBtn.setText(R.string.code_send_str);
                                        WithdrawCodeFragment.this.mSendCodeBtn.setEnabled(true);
                                        int unused = WithdrawCodeFragment.this.timerCount = 90;
                                        WithdrawCodeFragment.this.mTimer.cancel();
                                    }
                                }
                            });
                        }
                    };
                    Timer unused2 = WithdrawCodeFragment.this.mTimer = new Timer(true);
                    WithdrawCodeFragment.this.mTimer.schedule(r2, 1000, 1000);
                } else if (!PhoneInfo.isNetworkAvailable(WithdrawCodeFragment.this.mActivity)) {
                    ToastUtil.toast((Context) WithdrawCodeFragment.this.mActivity, (int) R.string.no_net);
                } else {
                    ToastUtil.toast((Context) WithdrawCodeFragment.this.mActivity, (CharSequence) rxMtopResponse.retMsg);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public SpannableString setSendBtnTextStyle(int i) {
        String format = String.format(getString(R.string.code_count_str), new Object[]{Integer.valueOf(i)});
        SpannableString spannableString = new SpannableString(format);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_text_color)), String.valueOf(i).length() + 1, format.length(), 33);
        return spannableString;
    }
}
