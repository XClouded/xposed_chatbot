package com.taobao.login4android.scan;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.BaseLogonFragment;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.scan.impl.ScanServiceImpl;
import com.ali.user.mobile.scan.model.ScanParam;
import com.ali.user.mobile.scan.model.ScanResponse;
import com.ali.user.mobile.utils.BundleUtil;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.login4android.Login;
import com.taobao.login4android.sdk.R;
import java.util.Locale;

public class QrScanAlibabaFragment extends BaseLogonFragment implements View.OnClickListener {
    public static final String TAG = "login.qrScanFragment";
    protected FragmentActivity mAttachedActivity;
    protected Button mCancelButton;
    protected Button mConfirmButton;
    protected ImageView mHintImageView;
    protected TextView mHintTextView;
    protected String mScanKey;
    protected int mSessionExpiredCount = 0;
    protected String mUrl;

    public void onCreate(Bundle bundle) {
        this.isLoginObserver = true;
        initParams();
        super.onCreate(bundle);
        if (!Login.checkSessionValid()) {
            this.mSessionExpiredCount++;
            Login.login(true);
        }
    }

    /* access modifiers changed from: protected */
    public void initParams() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mUrl = (String) arguments.get(LoginConstant.SCAN_KEY);
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
        if (DataProviderFactory.getDataProvider().getSupportedSites() == null || !Login.checkSessionValid() || Login.getLoginSite() == 4) {
            if (DataProviderFactory.getDataProvider().getCurrentLanguage() == Locale.CHINESE || DataProviderFactory.getDataProvider().getCurrentLanguage() == Locale.SIMPLIFIED_CHINESE || DataProviderFactory.getDataProvider().getCurrentLanguage() == Locale.TRADITIONAL_CHINESE) {
                this.mHintImageView.setImageResource(R.drawable.aliuser_scan_bg);
            } else {
                this.mHintImageView.setImageResource(R.drawable.aliuser_scan_bg_en);
            }
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, ScanResponse>() {
                /* access modifiers changed from: protected */
                public ScanResponse doInBackground(Object[] objArr) {
                    ScanParam scanParam = new ScanParam();
                    scanParam.appName = DataProviderFactory.getDataProvider().getAppkey();
                    scanParam.havanaId = Login.getUserId();
                    scanParam.currentSite = Login.getLoginSite();
                    Bundle serialBundle = BundleUtil.serialBundle(Uri.parse(QrScanAlibabaFragment.this.mUrl).getQuery());
                    if (serialBundle != null) {
                        try {
                            QrScanAlibabaFragment.this.mScanKey = serialBundle.getString("codeKey");
                            scanParam.key = QrScanAlibabaFragment.this.mScanKey;
                            return ScanServiceImpl.getInstance().scan(scanParam);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(ScanResponse scanResponse) {
                    if (scanResponse == null) {
                        QrScanAlibabaFragment.this.toast(QrScanAlibabaFragment.this.getResources().getString(R.string.aliuser_network_error), 0);
                    } else if (!scanResponse.bizSuccess) {
                        if (!TextUtils.isEmpty(scanResponse.errorMessage)) {
                            QrScanAlibabaFragment.this.alertMessage(scanResponse.errorMessage);
                        } else {
                            QrScanAlibabaFragment.this.toast(QrScanAlibabaFragment.this.getResources().getString(R.string.aliuser_network_error), 0);
                        }
                    }
                }
            }, new Object[0]);
            return;
        }
        alert("", getResources().getString(R.string.aliuser_error_scan_site), getResources().getString(R.string.aliuser_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                QrScanAlibabaFragment.this.mAttachedActivity.finish();
            }
        }, "", (DialogInterface.OnClickListener) null);
    }

    /* access modifiers changed from: private */
    public void alertMessage(String str) {
        if (!TextUtils.isEmpty(str)) {
            alert("", str, getResources().getString(R.string.aliuser_confirm), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    QrScanAlibabaFragment.this.mAttachedActivity.finish();
                }
            }, (String) null, (DialogInterface.OnClickListener) null);
        }
    }

    public void initViews(View view) {
        TLogAdapter.d("login.qrScanFragment", "initViews");
        this.mConfirmButton = (Button) view.findViewById(R.id.aliuser_scan_confirmButton);
        this.mConfirmButton.setOnClickListener(this);
        this.mCancelButton = (Button) view.findViewById(R.id.aliuser_scan_cancelButton);
        this.mCancelButton.setOnClickListener(this);
        this.mHintTextView = (TextView) view.findViewById(R.id.aliuser_scan_textview);
        this.mHintImageView = (ImageView) view.findViewById(R.id.aliuser_scan_bg_imageview);
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
            handleConfirm();
        } else if (id == R.id.aliuser_scan_cancelButton) {
            handleBack();
        }
    }

    /* access modifiers changed from: protected */
    public void handleConfirm() {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, ScanResponse>() {
            /* access modifiers changed from: protected */
            public ScanResponse doInBackground(Object[] objArr) {
                ScanParam scanParam = new ScanParam();
                scanParam.appName = DataProviderFactory.getDataProvider().getAppkey();
                scanParam.havanaId = Login.getUserId();
                scanParam.currentSite = Login.getLoginSite();
                scanParam.key = QrScanAlibabaFragment.this.mScanKey;
                try {
                    return ScanServiceImpl.getInstance().confirm(scanParam);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(ScanResponse scanResponse) {
                if (scanResponse == null) {
                    QrScanAlibabaFragment.this.toast(QrScanAlibabaFragment.this.getResources().getString(R.string.aliuser_network_error), 0);
                } else if (scanResponse.bizSuccess) {
                    QrScanAlibabaFragment.this.mAttachedActivity.finish();
                } else if (!TextUtils.isEmpty(scanResponse.errorMessage)) {
                    QrScanAlibabaFragment.this.alertMessage(scanResponse.errorMessage);
                } else {
                    QrScanAlibabaFragment.this.toast(QrScanAlibabaFragment.this.getResources().getString(R.string.aliuser_network_error), 0);
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void handleBack() {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, ScanResponse>() {
            /* access modifiers changed from: protected */
            public ScanResponse doInBackground(Object[] objArr) {
                ScanParam scanParam = new ScanParam();
                scanParam.appName = DataProviderFactory.getDataProvider().getAppkey();
                scanParam.havanaId = Login.getUserId();
                scanParam.key = QrScanAlibabaFragment.this.mScanKey;
                scanParam.currentSite = Login.getLoginSite();
                try {
                    return ScanServiceImpl.getInstance().cancel(scanParam);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(ScanResponse scanResponse) {
                QrScanAlibabaFragment.this.mAttachedActivity.finish();
            }
        }, new Object[0]);
    }

    public boolean onBackPressed() {
        handleBack();
        return true;
    }
}
