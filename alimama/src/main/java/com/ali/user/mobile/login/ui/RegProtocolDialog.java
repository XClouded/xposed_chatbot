package com.ali.user.mobile.login.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.register.ProtocolModel;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.utils.ProtocolHelper;
import com.ali.user.mobile.utils.ScreenUtil;
import com.taobao.orange.OrangeConfig;

public class RegProtocolDialog extends DialogFragment {
    protected TextView alipayProtocolTV;
    private boolean isShowInset = false;
    private Activity mAttachedActivity;
    /* access modifiers changed from: private */
    public View.OnClickListener mNagetiveListener;
    private String mNegativeText;
    /* access modifiers changed from: private */
    public View.OnClickListener mPositiveListener;
    private String mPositiveText;
    private String mRegTip;
    private String mTitleText;

    /* access modifiers changed from: protected */
    public ProtocolModel getExtraProtocals() {
        return null;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, R.style.DialogTheme);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mAttachedActivity = activity;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            Window window = dialog.getWindow();
            double d = (double) displayMetrics.widthPixels;
            Double.isNaN(d);
            window.setLayout((int) (d * 0.84d), -2);
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_protocol;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().requestWindowFeature(1);
        View inflate = layoutInflater.inflate(getLayoutContent(), viewGroup);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.aliuser_protocol_inset);
        if (this.isShowInset) {
            imageView.setVisibility(0);
        } else {
            imageView.setVisibility(8);
        }
        TextView textView = (TextView) inflate.findViewById(R.id.aliuser_reg_tip);
        if (TextUtils.isEmpty(this.mRegTip)) {
            textView.setVisibility(8);
        } else {
            if (this.mRegTip.length() > 10) {
                textView.setTextSize(2, 18.0f);
            } else {
                textView.setTextSize(2, 24.0f);
            }
            textView.setText(this.mRegTip);
        }
        ((TextView) inflate.findViewById(R.id.aliuser_protocal_tip)).setMovementMethod(ScrollingMovementMethod.getInstance());
        ((TextView) inflate.findViewById(R.id.aliuser_tb_protocal)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NavigatorManager.getInstance().navToWebViewPage(RegProtocolDialog.this.getActivity(), ProtocolHelper.getProtocol(), (LoginParam) null, (LoginReturnData) null);
            }
        });
        this.alipayProtocolTV = (TextView) inflate.findViewById(R.id.aliuser_alipay_protocal);
        this.alipayProtocolTV.setText(OrangeConfig.getInstance().getConfig("login4android", "alipay_protocol_text", getString(R.string.aliuser_alipay_protocal)));
        this.alipayProtocolTV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RegProtocolDialog.this.getAlipayClick();
            }
        });
        ((TextView) inflate.findViewById(R.id.aliuser_policy_protocal)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NavigatorManager.getInstance().navToWebViewPage(RegProtocolDialog.this.getActivity(), ProtocolHelper.getPolicyProtocol(), (LoginParam) null, (LoginReturnData) null);
            }
        });
        try {
            ((TextView) inflate.findViewById(R.id.aliuser_law_protocal)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    NavigatorManager.getInstance().navToWebViewPage(RegProtocolDialog.this.getActivity(), ProtocolHelper.getLawProtocol(), (LoginParam) null, (LoginReturnData) null);
                }
            });
            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.aliuser_protocal_ll);
            if (!(linearLayout == null || getExtraProtocals() == null)) {
                ProtocolModel extraProtocals = getExtraProtocals();
                if (extraProtocals.protocolItems != null && extraProtocals.protocolItems.size() > 0) {
                    for (String next : extraProtocals.protocolItems.keySet()) {
                        final String str = extraProtocals.protocolItems.get(next);
                        if (!TextUtils.isEmpty(next) && !TextUtils.isEmpty(str)) {
                            TextView textView2 = new TextView(this.mAttachedActivity);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                            textView2.setTextColor(getResources().getColor(R.color.aliuser_color_orange));
                            textView2.setTextSize(2, 14.0f);
                            textView2.setPadding(0, 0, 0, 0);
                            layoutParams.bottomMargin = (int) (ScreenUtil.getDeivceDensity(this.mAttachedActivity) * 8.0f);
                            textView2.setText(next);
                            textView2.setClickable(true);
                            textView2.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    NavigatorManager.getInstance().navToWebViewPage(RegProtocolDialog.this.getActivity(), str, (LoginParam) null, (LoginReturnData) null);
                                }
                            });
                            linearLayout.addView(textView2, layoutParams);
                        }
                    }
                }
            }
        } catch (Throwable unused) {
        }
        LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.aliuser_protocol_button_layout);
        if (DataProviderFactory.getDataProvider().isTaobaoApp()) {
            linearLayout2.setBackgroundResource(R.drawable.aliuser_btn_shadow);
        }
        Button button = (Button) inflate.findViewById(R.id.aliuser_protocol_agree);
        if (button != null) {
            if (!TextUtils.isEmpty(this.mPositiveText)) {
                button.setText(this.mPositiveText);
            }
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    RegProtocolDialog.this.dismissAllowingStateLoss();
                    if (RegProtocolDialog.this.mPositiveListener != null) {
                        RegProtocolDialog.this.mPositiveListener.onClick(view);
                    }
                }
            });
        }
        Button button2 = (Button) inflate.findViewById(R.id.aliuser_protocol_disagree);
        if (button2 != null) {
            if (!TextUtils.isEmpty(this.mNegativeText)) {
                button2.setText(this.mNegativeText);
                button2.setVisibility(0);
            } else {
                button2.setVisibility(8);
            }
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    RegProtocolDialog.this.dismissAllowingStateLoss();
                    if (RegProtocolDialog.this.mNagetiveListener != null) {
                        RegProtocolDialog.this.mNagetiveListener.onClick(view);
                    }
                }
            });
        }
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.aliuser_protocol_cancel);
        if (imageView2 != null) {
            imageView2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    RegProtocolDialog.this.dismissAllowingStateLoss();
                    if (RegProtocolDialog.this.mNagetiveListener != null) {
                        RegProtocolDialog.this.mNagetiveListener.onClick(view);
                    }
                }
            });
        }
        setCancelable(true);
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void getAlipayClick() {
        NavigatorManager.getInstance().navToWebViewPage(getActivity(), OrangeConfig.getInstance().getConfig("login4android", "alipay_protocol", getString(R.string.aliuser_alipay_protocal_url)), (LoginParam) null, (LoginReturnData) null);
    }

    public void setRegTip(String str) {
        this.mRegTip = str;
    }

    public void setTitle(String str) {
        this.mTitleText = str;
    }

    public void setPositive(String str, View.OnClickListener onClickListener) {
        this.mPositiveText = str;
        this.mPositiveListener = onClickListener;
    }

    public void setNagetive(View.OnClickListener onClickListener) {
        this.mNagetiveListener = onClickListener;
    }

    public String getNegativeText() {
        return this.mNegativeText;
    }

    public void setNegativeText(String str) {
        this.mNegativeText = str;
    }

    public String getTitleText() {
        return this.mTitleText;
    }

    public void setTitleText(String str) {
        this.mTitleText = str;
    }

    public void setShowInset(boolean z) {
        this.isShowInset = z;
    }

    public void show(FragmentManager fragmentManager, String str) {
        try {
            super.show(fragmentManager, str);
        } catch (IllegalStateException unused) {
        }
    }
}
