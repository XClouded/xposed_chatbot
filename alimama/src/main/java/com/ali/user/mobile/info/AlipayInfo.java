package com.ali.user.mobile.info;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.callback.DataCallback;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.alipay.apmobilesecuritysdk.face.APSecuritySdk;
import java.util.HashMap;

public class AlipayInfo {
    private static AlipayInfo INSTANCE = null;
    private static final String TAG = "login.AlipayInfo";
    private APSecuritySdk alipaySecuritySdk;
    /* access modifiers changed from: private */
    public String mApdid;
    /* access modifiers changed from: private */
    public String mApdidToken;

    private int getEnvModeConfig(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    private AlipayInfo() {
    }

    public static synchronized AlipayInfo getInstance() {
        AlipayInfo alipayInfo;
        synchronized (AlipayInfo.class) {
            if (INSTANCE == null) {
                INSTANCE = new AlipayInfo();
            }
            alipayInfo = INSTANCE;
        }
        return alipayInfo;
    }

    public void init() {
        try {
            this.alipaySecuritySdk = APSecuritySdk.getInstance(DataProviderFactory.getApplicationContext());
            initApdid();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void initApdid() {
        if (TextUtils.isEmpty(this.mApdid)) {
            generateAlipayTokens((DataCallback<String>) null);
        }
    }

    public String getApdid() {
        if (TextUtils.isEmpty(this.mApdid) && this.alipaySecuritySdk != null) {
            generateAlipayTokens((DataCallback<String>) null);
        }
        return this.mApdid;
    }

    public String getApdidToken() {
        if (TextUtils.isEmpty(this.mApdidToken) && this.alipaySecuritySdk != null) {
            generateAlipayTokens((DataCallback<String>) null);
            UserTrackAdapter.sendUT("Event_InitApdidToken");
        }
        TLogAdapter.d(TAG, "mApdidToken=" + this.mApdidToken);
        return this.mApdidToken;
    }

    public void getApdidToken(DataCallback<String> dataCallback) {
        if (!TextUtils.isEmpty(this.mApdidToken) || this.alipaySecuritySdk == null) {
            dataCallback.result(this.mApdidToken);
            return;
        }
        generateAlipayTokens(dataCallback);
        UserTrackAdapter.sendUT("Event_InitApdidToken");
    }

    private void generateAlipayTokens(final DataCallback<String> dataCallback) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("tid", DataProviderFactory.getDataProvider().getTID());
            hashMap.put("utdid", AppInfo.getInstance().getUtdid());
            int envModeConfig = getEnvModeConfig(DataProviderFactory.getDataProvider().getEnvType());
            if (this.alipaySecuritySdk != null) {
                this.alipaySecuritySdk.initToken(envModeConfig, hashMap, new APSecuritySdk.InitResultListener() {
                    public void onResult(APSecuritySdk.TokenResult tokenResult) {
                        if (tokenResult != null) {
                            String unused = AlipayInfo.this.mApdid = tokenResult.apdid;
                            String unused2 = AlipayInfo.this.mApdidToken = tokenResult.apdidToken;
                        } else {
                            TLogAdapter.e(AlipayInfo.TAG, "generateAlipayTokens Failed!");
                        }
                        if (dataCallback != null) {
                            dataCallback.result(AlipayInfo.this.mApdidToken);
                        }
                    }
                });
                TLogAdapter.d(TAG, "init mApdid=" + this.mApdid);
                return;
            }
            TLogAdapter.e(TAG, "generateAlipayTokens Failed: alipaySecuritySdk null");
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
