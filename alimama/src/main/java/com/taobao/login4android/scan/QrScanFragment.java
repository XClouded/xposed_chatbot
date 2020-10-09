package com.taobao.login4android.scan;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.BaseLogonFragment;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.scan.impl.ScanServiceImpl;
import com.ali.user.mobile.scan.model.CommonScanParam;
import com.ali.user.mobile.scan.model.CommonScanResponse;
import com.ali.user.mobile.scan.model.CommonScanResult;
import com.ali.user.mobile.utils.BundleUtil;
import com.ali.user.mobile.utils.LoadImageTask;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.login4android.Login;
import com.taobao.login4android.sdk.R;
import java.util.Locale;

public class QrScanFragment extends BaseLogonFragment implements View.OnClickListener {
    public static final String TAG = "login.qrScanFragment";
    protected FragmentActivity mAttachedActivity;
    protected Button mCancelButton;
    protected Button mConfirmButton;
    protected String mConfirmMsg;
    protected ImageView mHintImageView;
    protected String mScanKey;
    protected TextView mScanSubTitleView;
    protected TextView mScanTitleTextView;
    protected int mSessionExpiredCount = 0;
    protected String mUrl;

    /* access modifiers changed from: protected */
    public boolean handleIntercept(CommonScanResponse commonScanResponse) {
        return false;
    }

    public void onCreate(Bundle bundle) {
        this.isLoginObserver = true;
        super.onCreate(bundle);
        if (!Login.checkSessionValid()) {
            Login.login(true);
        }
    }

    /* access modifiers changed from: protected */
    public void initParams() {
        try {
            Bundle arguments = getArguments();
            if (arguments != null) {
                this.mUrl = (String) arguments.get(LoginConstant.SCAN_KEY);
            }
            if (TextUtils.isEmpty(this.mUrl) && this.mAttachedActivity != null) {
                this.mAttachedActivity.finish();
            }
        } catch (Throwable unused) {
        }
    }

    public void onAttach(Activity activity) {
        TLogAdapter.d("login.qrScanFragment", "onAttach");
        super.onAttach(activity);
        this.mAttachedActivity = (QrScanActivity) activity;
    }

    public void onActivityCreated(Bundle bundle) {
        TLogAdapter.d("login.qrScanFragment", Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_CREATED);
        super.onActivityCreated(bundle);
        drawView();
    }

    /* access modifiers changed from: protected */
    public void drawView() {
        this.mHintImageView.setImageResource(getHitImageResource());
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, CommonScanResponse>() {
            /* access modifiers changed from: protected */
            public CommonScanResponse doInBackground(Object[] objArr) {
                CommonScanParam commonScanParam = new CommonScanParam();
                commonScanParam.appName = DataProviderFactory.getDataProvider().getAppkey();
                commonScanParam.havanaId = Login.getUserId();
                commonScanParam.sid = Login.getSid();
                commonScanParam.currentSite = Login.getLoginSite();
                Bundle serialBundle = BundleUtil.serialBundle(Uri.parse(QrScanFragment.this.mUrl).getQuery());
                if (serialBundle != null) {
                    try {
                        QrScanFragment.this.mScanKey = serialBundle.getString("lgToken");
                        commonScanParam.key = QrScanFragment.this.mScanKey;
                        return ScanServiceImpl.getInstance().commonScan(commonScanParam);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(CommonScanResponse commonScanResponse) {
                if (QrScanFragment.this.mAttachedActivity != null && !QrScanFragment.this.mAttachedActivity.isFinishing()) {
                    if (commonScanResponse == null) {
                        QrScanFragment.this.toast(QrScanFragment.this.mAttachedActivity.getResources().getString(R.string.aliuser_network_error), 0);
                    } else if (commonScanResponse.code == 3000) {
                        if (commonScanResponse.returnValue != null) {
                            String str = ((CommonScanResult) commonScanResponse.returnValue).titleMsg;
                            String str2 = ((CommonScanResult) commonScanResponse.returnValue).subTitleMsg;
                            QrScanFragment.this.mConfirmMsg = ((CommonScanResult) commonScanResponse.returnValue).confirmMsg;
                            if (!TextUtils.isEmpty(str)) {
                                QrScanFragment.this.mScanTitleTextView.setText(str);
                            }
                            if (!TextUtils.isEmpty(str2)) {
                                QrScanFragment.this.mScanSubTitleView.setText(str2);
                            }
                            QrScanFragment.this.mScanSubTitleView.setTextColor(QrScanFragment.this.mAttachedActivity.getResources().getColor(QrScanFragment.this.getSubTitleTextColor(!TextUtils.isEmpty(QrScanFragment.this.mConfirmMsg))));
                            QrScanFragment.this.updateLogo(((CommonScanResult) commonScanResponse.returnValue).logoUrl);
                            return;
                        }
                        QrScanFragment.this.toast(QrScanFragment.this.mAttachedActivity.getResources().getString(R.string.aliuser_network_error), 0);
                    } else if (commonScanResponse.code == 14034) {
                        QrScanFragment.this.handleSessionExpired();
                    } else if (ApiConstants.ResultActionType.H5.equals(commonScanResponse.actionType)) {
                        if (commonScanResponse.returnValue == null) {
                            QrScanFragment.this.toast(QrScanFragment.this.mAttachedActivity.getResources().getString(R.string.aliuser_network_error), 0);
                        } else if (!TextUtils.isEmpty(((CommonScanResult) commonScanResponse.returnValue).h5Url)) {
                            Login.openUrl(QrScanFragment.this.mAttachedActivity, ((CommonScanResult) commonScanResponse.returnValue).h5Url);
                            QrScanFragment.this.mAttachedActivity.finish();
                        } else if (!TextUtils.isEmpty(commonScanResponse.message)) {
                            QrScanFragment.this.alertMessage(commonScanResponse);
                        } else {
                            QrScanFragment.this.toast(QrScanFragment.this.mAttachedActivity.getResources().getString(R.string.aliuser_network_error), 0);
                        }
                    } else if (QrScanFragment.this.handleIntercept(commonScanResponse)) {
                    } else {
                        if (!TextUtils.isEmpty(commonScanResponse.message)) {
                            QrScanFragment.this.alertMessage(commonScanResponse);
                        } else {
                            QrScanFragment.this.toast(QrScanFragment.this.mAttachedActivity.getResources().getString(R.string.aliuser_network_error), 0);
                        }
                    }
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void updateLogo(String str) {
        if (!TextUtils.isEmpty(str)) {
            new LoadImageTask(this.mAttachedActivity.getApplicationContext(), this.mHintImageView, "LogoImages", 800).execute(new String[]{str});
        }
    }

    /* access modifiers changed from: protected */
    public int getHitImageResource() {
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() == Locale.CHINESE || DataProviderFactory.getDataProvider().getCurrentLanguage() == Locale.SIMPLIFIED_CHINESE || DataProviderFactory.getDataProvider().getCurrentLanguage() == Locale.TRADITIONAL_CHINESE) {
            return R.drawable.aliuser_scan_bg;
        }
        return R.drawable.aliuser_scan_bg_en;
    }

    /* access modifiers changed from: protected */
    public int getSubTitleTextColor(boolean z) {
        if (z) {
            return R.color.aliuser_cancel_red;
        }
        return R.color.aliuser_color_ccc;
    }

    /* access modifiers changed from: private */
    public void handleSessionExpired() {
        this.mSessionExpiredCount++;
        if (this.mSessionExpiredCount <= 20) {
            Login.login(true);
        } else {
            alertMessage(this.mAttachedActivity.getResources().getString(R.string.aliuser_login_exception));
        }
    }

    /* access modifiers changed from: private */
    public void alertMessage(CommonScanResponse commonScanResponse) {
        if (commonScanResponse != null && !TextUtils.isEmpty(commonScanResponse.message)) {
            alertMessage(commonScanResponse.message);
        }
    }

    private void alertMessage(String str) {
        if (!TextUtils.isEmpty(str)) {
            alert("", str, this.mAttachedActivity.getResources().getString(R.string.aliuser_confirm), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    QrScanFragment.this.mAttachedActivity.finish();
                }
            }, (String) null, (DialogInterface.OnClickListener) null);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(getLayoutContent(), viewGroup, false);
        initViews(inflate);
        initParams();
        return inflate;
    }

    public void initViews(View view) {
        TLogAdapter.d("login.qrScanFragment", "initViews");
        this.mConfirmButton = (Button) view.findViewById(R.id.aliuser_scan_confirmButton);
        this.mConfirmButton.setOnClickListener(this);
        this.mCancelButton = (Button) view.findViewById(R.id.aliuser_scan_cancelButton);
        this.mCancelButton.setOnClickListener(this);
        this.mHintImageView = (ImageView) view.findViewById(R.id.aliuser_scan_bg_imageview);
        this.mScanTitleTextView = (TextView) view.findViewById(R.id.aliuser_scan_textview);
        this.mScanSubTitleView = (TextView) view.findViewById(R.id.aliuser_scan_subTitleTextView);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.aliuser_account_login);
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.ali_user_scan_fragment;
    }

    /* access modifiers changed from: protected */
    public void doWhenReceiveSuccess() {
        drawView();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.aliuser_scan_confirmButton) {
            if (TextUtils.isEmpty(this.mConfirmMsg)) {
                handleConfirm();
                return;
            }
            alert("", this.mConfirmMsg, this.mAttachedActivity.getResources().getString(R.string.aliuser_confirm), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    QrScanFragment.this.handleConfirm();
                }
            }, this.mAttachedActivity.getResources().getString(R.string.aliuser_cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    QrScanFragment.this.dismissAlertDialog();
                }
            });
        } else if (id == R.id.aliuser_scan_cancelButton) {
            handleBack();
        }
    }

    /* access modifiers changed from: protected */
    public void handleConfirm() {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, CommonScanResponse>() {
            /* access modifiers changed from: protected */
            public CommonScanResponse doInBackground(Object[] objArr) {
                CommonScanParam commonScanParam = new CommonScanParam();
                commonScanParam.appName = DataProviderFactory.getDataProvider().getAppkey();
                commonScanParam.havanaId = Login.getUserId();
                commonScanParam.sid = Login.getSid();
                commonScanParam.key = QrScanFragment.this.mScanKey;
                commonScanParam.currentSite = Login.getLoginSite();
                try {
                    return ScanServiceImpl.getInstance().commonConfirm(commonScanParam);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(CommonScanResponse commonScanResponse) {
                if (QrScanFragment.this.mAttachedActivity != null && !QrScanFragment.this.mAttachedActivity.isFinishing()) {
                    if (commonScanResponse == null) {
                        QrScanFragment.this.toast(QrScanFragment.this.mAttachedActivity.getResources().getString(R.string.aliuser_network_error), 0);
                    } else if (commonScanResponse.code == 3000) {
                        QrScanFragment.this.mAttachedActivity.finish();
                    } else if (commonScanResponse.code == 14034) {
                        QrScanFragment.this.handleSessionExpired();
                    } else if (QrScanFragment.this.handleIntercept(commonScanResponse)) {
                    } else {
                        if (!TextUtils.isEmpty(commonScanResponse.message)) {
                            QrScanFragment.this.alertMessage(commonScanResponse);
                        } else {
                            QrScanFragment.this.toast(QrScanFragment.this.mAttachedActivity.getResources().getString(R.string.aliuser_network_error), 0);
                        }
                    }
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void handleBack() {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, CommonScanResponse>() {
            /* access modifiers changed from: protected */
            public CommonScanResponse doInBackground(Object[] objArr) {
                CommonScanParam commonScanParam = new CommonScanParam();
                commonScanParam.appName = DataProviderFactory.getDataProvider().getAppkey();
                commonScanParam.havanaId = Login.getUserId();
                commonScanParam.sid = Login.getSid();
                commonScanParam.key = QrScanFragment.this.mScanKey;
                commonScanParam.currentSite = Login.getLoginSite();
                try {
                    return ScanServiceImpl.getInstance().commonCancel(commonScanParam);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(CommonScanResponse commonScanResponse) {
                QrScanFragment.this.mAttachedActivity.finish();
            }
        }, new Object[0]);
    }

    public boolean onBackPressed() {
        handleBack();
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHintImageView = null;
    }
}
