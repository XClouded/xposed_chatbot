package com.ali.user.mobile.info;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.umid.IUMIDComponent;
import com.alibaba.wireless.security.open.umid.IUMIDInitListenerEx;
import com.ut.device.UTDevice;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;

public class AppInfo {
    public static final String INITED_ACTION = "com.ali.user.sdk.biz.inited.action";
    private static AppInfo INSTANCE = null;
    private static final String TAG = "login.AppInfo";
    private String mAppVersion;
    /* access modifiers changed from: private */
    public String mUmidToken;
    private String mUtdid;

    public String getChannel() {
        return null;
    }

    private AppInfo() {
    }

    public void init() {
        init(false);
    }

    public void init(boolean z) {
        int i = 2;
        switch (DataProviderFactory.getDataProvider().getEnvType()) {
            case 0:
            case 1:
            case 4:
                break;
            default:
                i = 0;
                break;
        }
        IUMIDComponent uMIDComp = SecurityGuardManagerWraper.getSecurityGuardManager().getUMIDComp();
        if (uMIDComp != null) {
            try {
                uMIDComp.initUMID(i, new IUMIDInitListenerEx() {
                    public void onUMIDInitFinishedEx(String str, int i) {
                        if (i == 200) {
                            String unused = AppInfo.this.mUmidToken = str;
                        }
                        TLogAdapter.e(AppInfo.TAG, "resultCode=" + i + " onInitFinished umidToken = " + str);
                    }
                });
            } catch (SecException e) {
                UserTrackAdapter.sendUT(e.getErrorCode());
            }
        }
        TLogAdapter.d(TAG, "init mUmidToken=" + this.mUmidToken);
        try {
            this.mUtdid = UTDevice.getUtdid(DataProviderFactory.getApplicationContext());
            TLogAdapter.d(TAG, "init mUtdid=" + this.mUtdid);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static synchronized AppInfo getInstance() {
        AppInfo appInfo;
        synchronized (AppInfo.class) {
            if (INSTANCE == null) {
                INSTANCE = new AppInfo();
            }
            appInfo = INSTANCE;
        }
        return appInfo;
    }

    public String getSdkVersion() {
        String str = "4.5.6.16_fix2";
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("-");
            if (split == null || split.length <= 0) {
                str = "";
            } else {
                str = split[0];
                if (!TextUtils.isEmpty(str)) {
                    String[] split2 = str.split("_");
                    str = (split2 == null || split2.length <= 0) ? "" : split2[0];
                }
            }
        }
        if (TextUtils.isEmpty(str)) {
            str = "4.5.6";
        }
        return "android_" + str;
    }

    public String getUtdid() {
        return this.mUtdid;
    }

    public String getUmidToken() {
        IUMIDComponent uMIDComp;
        if (TextUtils.isEmpty(this.mUmidToken)) {
            int i = 2;
            switch (DataProviderFactory.getDataProvider().getEnvType()) {
                case 0:
                case 1:
                case 4:
                    break;
                default:
                    i = 0;
                    break;
            }
            SecurityGuardManager securityGuardManager = SecurityGuardManagerWraper.getSecurityGuardManager();
            if (!(securityGuardManager == null || (uMIDComp = securityGuardManager.getUMIDComp()) == null)) {
                try {
                    uMIDComp.initUMID(i, new IUMIDInitListenerEx() {
                        public void onUMIDInitFinishedEx(String str, int i) {
                            if (i == 200) {
                                String unused = AppInfo.this.mUmidToken = str;
                            }
                            TLogAdapter.e(AppInfo.TAG, "resultCode=" + i + " onInitFinished umidToken = " + str);
                        }
                    });
                } catch (SecException e) {
                    UserTrackAdapter.sendUT(e.getErrorCode());
                }
            }
            TLogAdapter.d(TAG, "get mUmidToken=" + this.mUmidToken);
        }
        return this.mUmidToken;
    }

    public String getAppVersion() {
        if (this.mAppVersion == null) {
            try {
                PackageInfo packageInfo = DataProviderFactory.getApplicationContext().getPackageManager().getPackageInfo(DataProviderFactory.getApplicationContext().getPackageName(), 0);
                if (packageInfo != null) {
                    this.mAppVersion = packageInfo.versionName;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return this.mAppVersion;
    }

    public String getAndroidAppVersion() {
        return "android_" + getAppVersion();
    }

    public String getLocale() {
        return Locale.getDefault().getCountry();
    }

    public String getApkSignNumber() {
        try {
            return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(DataProviderFactory.getApplicationContext().getPackageManager().getPackageInfo(DataProviderFactory.getApplicationContext().getPackageName(), 64).signatures[0].toByteArray()))).getSerialNumber().toString();
        } catch (Exception unused) {
            return "";
        }
    }
}
