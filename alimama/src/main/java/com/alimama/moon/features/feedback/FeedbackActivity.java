package com.alimama.moon.features.feedback;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;
import com.alimama.moon.BuildConfig;
import com.alimama.moon.R;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.utils.ActivityUtil;
import com.alimama.moon.utils.AliLog;
import com.alimama.moon.utils.PhoneInfo;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;

public class FeedbackActivity extends BaseActivity {
    private static final String TAG = "FeedbackActivity";
    private EditText mEditText;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_feedback);
        ActivityUtil.setupToolbar(this, (Toolbar) findViewById(R.id.toolbar), true);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FeedbackActivity.this.clickSubmitBtn();
            }
        });
        this.mEditText = (EditText) findViewById(R.id.feedback_et);
    }

    /* access modifiers changed from: package-private */
    public void clickSubmitBtn() {
        String obj = this.mEditText.getText().toString();
        if (TextUtils.isEmpty(obj)) {
            ToastUtil.toast(getApplicationContext(), (CharSequence) getString(R.string.feedback_error));
            return;
        }
        if (!PhoneInfo.isNetworkAvailable(getApplicationContext())) {
            ToastUtil.toast(getApplicationContext(), (CharSequence) getString(R.string.feedback_no_net));
        }
        sendFeedback(obj);
    }

    private void sendFeedback(String str) {
        new FeedbackRequest(str, getAppInfo()).sendRequest(new RxMtopRequest.RxMtopResult<Void>() {
            public void result(RxMtopResponse<Void> rxMtopResponse) {
                if (rxMtopResponse.isReqSuccess) {
                    ToastUtil.toast(FeedbackActivity.this.getApplicationContext(), (CharSequence) FeedbackActivity.this.getString(R.string.feedback_send_sucess));
                    if (!FeedbackActivity.this.isFinishing()) {
                        FeedbackActivity.this.finish();
                        return;
                    }
                    return;
                }
                AliLog.LogE(FeedbackActivity.TAG, "feedback error: " + rxMtopResponse.retMsg);
                ToastUtil.toast(FeedbackActivity.this.getApplicationContext(), (CharSequence) FeedbackActivity.this.getString(R.string.feedback_send_failed));
            }
        });
    }

    private String getAppInfo() {
        String str = ((("" + BuildConfig.VERSION_NAME) + ":" + Build.VERSION.RELEASE) + ":" + Build.MODEL) + ":杭州市";
        if (PhoneInfo.isMobileConnected(this)) {
            str = str + ":2g/3g";
        } else if (PhoneInfo.isWifiConnected(this)) {
            str = str + ":wifi";
        }
        String str2 = str + ":" + getWindowManager().getDefaultDisplay().getWidth() + "*" + getWindowManager().getDefaultDisplay().getHeight();
        AliLog.LogE(TAG, "feedback appInfo: " + str2);
        return str2;
    }
}
