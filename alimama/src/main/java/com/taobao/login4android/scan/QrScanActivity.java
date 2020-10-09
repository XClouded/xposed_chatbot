package com.taobao.login4android.scan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.common.api.LoginApprearanceExtensions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.taobao.login4android.sdk.R;

public class QrScanActivity extends BaseActivity {
    public static final String FRAGMENT_LABEL = "aliuser_qrcode_confirm";
    private Fragment mFragment;
    protected FragmentManager mFragmentManager;
    public String mScene;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        this.mFragmentManager = getSupportFragmentManager();
        initParam();
        super.onCreate(bundle);
    }

    private void initParam() {
        if (getIntent() != null) {
            try {
                this.mScene = getIntent().getStringExtra(LoginConstant.SCAN_SCENE);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.user_scan_activity;
    }

    public void initViews() {
        super.initViews();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.aliuser_scan_login_title);
        }
        openFragmentById(getIntent());
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initParam();
        openFragmentById(intent);
    }

    private void openFragmentById(Intent intent) {
        LoginApprearanceExtensions loginApprearanceExtensions = AliUserLogin.mAppreanceExtentions;
        if (!TextUtils.isEmpty(this.mScene)) {
            openAlibabaConfirm(intent, loginApprearanceExtensions);
        } else {
            openConfirm(intent, loginApprearanceExtensions);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0036  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void openAlibabaConfirm(android.content.Intent r3, com.ali.user.mobile.common.api.LoginApprearanceExtensions r4) {
        /*
            r2 = this;
            com.taobao.login4android.scan.QrScanAlibabaFragment r0 = new com.taobao.login4android.scan.QrScanAlibabaFragment
            r0.<init>()
            if (r4 == 0) goto L_0x001c
            java.lang.Class r1 = r4.getFullyCustomizedScanAlibabaFragment()
            if (r1 == 0) goto L_0x001c
            java.lang.Class r4 = r4.getFullyCustomizedScanAlibabaFragment()     // Catch:{ Exception -> 0x0018 }
            java.lang.Object r4 = r4.newInstance()     // Catch:{ Exception -> 0x0018 }
            androidx.fragment.app.Fragment r4 = (androidx.fragment.app.Fragment) r4     // Catch:{ Exception -> 0x0018 }
            goto L_0x001d
        L_0x0018:
            r4 = move-exception
            r4.printStackTrace()
        L_0x001c:
            r4 = r0
        L_0x001d:
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            java.lang.String r1 = "aliuser_qrcode_confirm"
            androidx.fragment.app.Fragment r0 = r0.findFragmentByTag(r1)
            if (r0 == 0) goto L_0x0034
            androidx.fragment.app.FragmentManager r1 = r2.mFragmentManager
            androidx.fragment.app.FragmentTransaction r1 = r1.beginTransaction()
            androidx.fragment.app.FragmentTransaction r0 = r1.remove(r0)
            r0.commitAllowingStateLoss()
        L_0x0034:
            if (r3 == 0) goto L_0x003d
            android.os.Bundle r3 = r3.getExtras()
            r4.setArguments(r3)
        L_0x003d:
            r2.mFragment = r4
            androidx.fragment.app.FragmentManager r3 = r2.mFragmentManager
            androidx.fragment.app.FragmentTransaction r3 = r3.beginTransaction()
            int r0 = com.taobao.login4android.sdk.R.id.loginContainer
            java.lang.String r1 = "aliuser_qrcode_confirm"
            androidx.fragment.app.FragmentTransaction r3 = r3.add(r0, r4, r1)
            r3.commitAllowingStateLoss()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.scan.QrScanActivity.openAlibabaConfirm(android.content.Intent, com.ali.user.mobile.common.api.LoginApprearanceExtensions):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0036  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void openConfirm(android.content.Intent r3, com.ali.user.mobile.common.api.LoginApprearanceExtensions r4) {
        /*
            r2 = this;
            com.taobao.login4android.scan.QrScanFragment r0 = new com.taobao.login4android.scan.QrScanFragment
            r0.<init>()
            if (r4 == 0) goto L_0x001c
            java.lang.Class r1 = r4.getFullyCustomiedScanFragment()
            if (r1 == 0) goto L_0x001c
            java.lang.Class r4 = r4.getFullyCustomiedScanFragment()     // Catch:{ Exception -> 0x0018 }
            java.lang.Object r4 = r4.newInstance()     // Catch:{ Exception -> 0x0018 }
            androidx.fragment.app.Fragment r4 = (androidx.fragment.app.Fragment) r4     // Catch:{ Exception -> 0x0018 }
            goto L_0x001d
        L_0x0018:
            r4 = move-exception
            r4.printStackTrace()
        L_0x001c:
            r4 = r0
        L_0x001d:
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            java.lang.String r1 = "aliuser_qrcode_confirm"
            androidx.fragment.app.Fragment r0 = r0.findFragmentByTag(r1)
            if (r0 == 0) goto L_0x0034
            androidx.fragment.app.FragmentManager r1 = r2.mFragmentManager
            androidx.fragment.app.FragmentTransaction r1 = r1.beginTransaction()
            androidx.fragment.app.FragmentTransaction r0 = r1.remove(r0)
            r0.commitAllowingStateLoss()
        L_0x0034:
            if (r3 == 0) goto L_0x003d
            android.os.Bundle r3 = r3.getExtras()
            r4.setArguments(r3)
        L_0x003d:
            r2.mFragment = r4
            androidx.fragment.app.FragmentManager r3 = r2.mFragmentManager
            androidx.fragment.app.FragmentTransaction r3 = r3.beginTransaction()
            int r0 = com.taobao.login4android.sdk.R.id.loginContainer
            java.lang.String r1 = "aliuser_qrcode_confirm"
            androidx.fragment.app.FragmentTransaction r3 = r3.add(r0, r4, r1)
            r3.commitAllowingStateLoss()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.scan.QrScanActivity.openConfirm(android.content.Intent, com.ali.user.mobile.common.api.LoginApprearanceExtensions):void");
    }

    public void onBackPressed() {
        if (this.mFragment == null || !this.mFragment.isVisible() || !(this.mFragment instanceof QrScanFragment)) {
            super.onBackPressed();
        } else {
            ((QrScanFragment) this.mFragment).handleBack();
        }
    }
}
