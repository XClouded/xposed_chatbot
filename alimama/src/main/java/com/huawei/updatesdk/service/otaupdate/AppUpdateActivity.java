package com.huawei.updatesdk.service.otaupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.taobao.windvane.config.WVConfigManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.ut.abtest.pipeline.StatusCode;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.deamon.download.DownloadService;
import com.huawei.updatesdk.service.deamon.download.d;
import com.huawei.updatesdk.support.c.a;
import com.huawei.updatesdk.support.f.a;
import com.huawei.updatesdk.support.pm.g;
import java.io.Serializable;

public class AppUpdateActivity extends Activity implements a, com.huawei.updatesdk.support.d.b {
    private AlertDialog a;
    /* access modifiers changed from: private */
    public com.huawei.updatesdk.support.f.a b;
    /* access modifiers changed from: private */
    public com.huawei.updatesdk.support.f.a c;
    /* access modifiers changed from: private */
    public ProgressBar d;
    /* access modifiers changed from: private */
    public TextView e;
    private ImageView f;
    private RelativeLayout g;
    private boolean h = false;
    private boolean i = false;
    /* access modifiers changed from: private */
    public ApkUpgradeInfo j = null;
    /* access modifiers changed from: private */
    public boolean k = false;
    private boolean l = false;
    /* access modifiers changed from: private */
    public int m = -99;
    private int n = -99;
    /* access modifiers changed from: private */
    public int o = -99;
    private Intent p = null;
    private c q;

    private static class a implements DialogInterface.OnDismissListener {
        private a() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            Intent intent = new Intent();
            intent.putExtra(UpdateKey.DIALOG_STATUS, 10001);
            b.a().b(intent);
        }
    }

    private static class b implements DialogInterface.OnShowListener {
        private b() {
        }

        public void onShow(DialogInterface dialogInterface) {
            Intent intent = new Intent();
            intent.putExtra(UpdateKey.DIALOG_STATUS, StatusCode.UTDID_IS_BLANK);
            b.a().b(intent);
        }
    }

    private class c extends com.huawei.updatesdk.sdk.service.b.a {
        private c() {
        }

        public void a(Context context, com.huawei.updatesdk.sdk.service.b.b bVar) {
            if (bVar.d()) {
                if (AppUpdateActivity.this.b != null) {
                    AppUpdateActivity.this.b.c();
                }
                AppUpdateActivity.this.f();
                String b = bVar.b();
                String dataString = bVar.c().getDataString();
                if (dataString != null && dataString.length() >= 9) {
                    String substring = dataString.substring(8);
                    if ("android.intent.action.PACKAGE_ADDED".equals(b) && "com.huawei.appmarket".equals(substring)) {
                        b.a().a(AppUpdateActivity.this.a(6, 0, -1));
                        g.a.a(substring, 1);
                        AppUpdateActivity.this.a(AppUpdateActivity.this.j.getPackage_(), AppUpdateActivity.this.j.getDetailId_());
                        if (AppUpdateActivity.this.k) {
                            AppUpdateActivity.this.e(AppUpdateActivity.this.j);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public Intent a(int i2, int i3, int i4) {
        Intent intent = new Intent();
        intent.putExtra(UpdateKey.MARKET_DLD_STATUS, i4);
        intent.putExtra(UpdateKey.MARKET_INSTALL_STATE, i2);
        intent.putExtra(UpdateKey.MARKET_INSTALL_TYPE, i3);
        return intent;
    }

    private synchronized void a() {
        if (d.b() != null) {
            d.b().d();
        }
        com.huawei.updatesdk.support.e.a.a(this, this.q);
        com.huawei.updatesdk.support.d.c.b().b(this);
    }

    private void a(Context context) {
        if (!DownloadService.a()) {
            d.a();
        }
        d.b().f();
        com.huawei.updatesdk.support.d.c.b().a(this);
    }

    private void a(View view) {
        try {
            ScrollView scrollView = (ScrollView) view.findViewById(com.huawei.updatesdk.support.e.d.a(this, "scroll_layout"));
            if (Build.VERSION.SDK_INT >= 22) {
                TypedValue typedValue = new TypedValue();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getTheme().resolveAttribute(16843987, typedValue, true);
                ((WindowManager) getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
                int complexToDimensionPixelSize = TypedValue.complexToDimensionPixelSize(typedValue.data, displayMetrics);
                scrollView.setPadding(complexToDimensionPixelSize, 0, complexToDimensionPixelSize, 0);
            }
        } catch (Exception e2) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("AppUpdateActivity", e2.toString());
        }
    }

    private void a(ApkUpgradeInfo apkUpgradeInfo, TextView textView) {
        if (textView != null) {
            if (apkUpgradeInfo.getDiffSize_() > 0) {
                String a2 = com.huawei.updatesdk.support.b.d.a(this, (long) apkUpgradeInfo.getSize_());
                SpannableString spannableString = new SpannableString(a2);
                spannableString.setSpan(new StrikethroughSpan(), 0, a2.length(), 33);
                spannableString.setSpan(new TextAppearanceSpan("HwChinese-medium", 0, (int) textView.getTextSize(), (ColorStateList) null, (ColorStateList) null), 0, spannableString.length(), 33);
                textView.setText(spannableString);
                return;
            }
            textView.setVisibility(8);
        }
    }

    private void a(String str) {
        Intent intent = new Intent("com.huawei.appmarket.intent.action.ThirdUpdateAction");
        intent.setPackage("com.huawei.appmarket");
        intent.putExtra("APP_PACKAGENAME", str);
        intent.putExtra("APP_MUST_UPDATE_BTN", this.l);
        try {
            this.i = false;
            startActivityForResult(intent, 1002);
        } catch (ActivityNotFoundException e2) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("AppUpdateActivity", "goHiappUpgrade error: " + e2.toString());
            this.i = true;
            Intent intent2 = new Intent();
            intent2.putExtra("status", 8);
            b.a().b(intent2);
            e(this.j);
        }
    }

    /* access modifiers changed from: private */
    public void a(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            this.m = 1;
            finish();
            return;
        }
        Intent intent = new Intent("com.huawei.appmarket.appmarket.intent.action.AppDetail.withdetailId");
        intent.setPackage("com.huawei.appmarket");
        intent.putExtra("appDetailId", str2);
        intent.putExtra("thirdId", str);
        intent.addFlags(268468224);
        try {
            startActivity(intent);
            if (!this.k) {
                finish();
            }
        } catch (ActivityNotFoundException unused) {
            d.a((a) this);
            d.a();
            Intent intent2 = new Intent();
            intent2.putExtra("status", 8);
            b.a().b(intent2);
            if (this.c != null) {
                this.c.c();
            }
        }
    }

    private void b() {
        if (this.c != null) {
            this.c.a((DialogInterface.OnDismissListener) new a());
            this.c.a((DialogInterface.OnShowListener) new b());
        }
    }

    /* access modifiers changed from: private */
    public void b(final String str) {
        this.b = com.huawei.updatesdk.support.f.a.a(this, (String) null, getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_third_app_dl_cancel_download_prompt_ex")));
        this.b.a((com.huawei.updatesdk.support.f.b) new com.huawei.updatesdk.support.f.b() {
            public void a() {
                AppUpdateActivity.this.d.setProgress(0);
                AppUpdateActivity.this.d.setMax(0);
                AppUpdateActivity.this.e.setText("");
                AppUpdateActivity.this.f();
                d.a(str);
                AppUpdateActivity.this.b.c();
                if (AppUpdateActivity.this.k) {
                    AppUpdateActivity.this.e(AppUpdateActivity.this.j);
                    return;
                }
                int unused = AppUpdateActivity.this.m = 4;
                AppUpdateActivity.this.finish();
            }

            public void b() {
                AppUpdateActivity.this.b.c();
            }
        });
        String string = getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_third_app_dl_sure_cancel_download"));
        this.b.a((a.b) new a.b() {
            public void a() {
                AppUpdateActivity.this.finish();
            }
        });
        this.b.a(a.C0020a.CONFIRM, string);
    }

    /* access modifiers changed from: private */
    public void c() {
        if (!com.huawei.updatesdk.support.c.a.b(this, "com.huawei.appmarket")) {
            if (com.huawei.updatesdk.sdk.service.a.a.a() == null) {
                com.huawei.updatesdk.sdk.service.a.a.a(this);
            }
            d.a((a) this);
            d.a();
            this.c.c();
            return;
        }
        a(this.j.getPackage_(), this.j.getDetailId_());
    }

    /* access modifiers changed from: private */
    public void c(int i2) {
        if (i2 == 5 || i2 == 4) {
            Toast.makeText(this, getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_third_app_dl_install_failed")), 0).show();
            g.a.a("com.huawei.appmarket", -1000001);
            finish();
        }
        if (i2 == 7) {
            g.a.a("com.huawei.appmarket", -1000001);
            if (this.h) {
                e(this.j);
            } else {
                finish();
            }
        }
    }

    private void c(final ApkUpgradeInfo apkUpgradeInfo) {
        final com.huawei.updatesdk.support.f.a a2 = com.huawei.updatesdk.support.f.a.a(this, (String) null, getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_install")));
        a2.a((com.huawei.updatesdk.support.f.b) new com.huawei.updatesdk.support.f.b() {
            public void a() {
                if (!com.huawei.updatesdk.sdk.a.d.c.b.a((Context) AppUpdateActivity.this)) {
                    Toast.makeText(AppUpdateActivity.this, com.huawei.updatesdk.support.e.d.b(AppUpdateActivity.this, "upsdk_no_available_network_prompt_toast"), 0).show();
                    AppUpdateActivity.this.finish();
                    return;
                }
                d.a(apkUpgradeInfo);
                a2.c();
            }

            public void b() {
                a2.c();
                if (AppUpdateActivity.this.k) {
                    AppUpdateActivity.this.e(AppUpdateActivity.this.j);
                    return;
                }
                int unused = AppUpdateActivity.this.m = 4;
                AppUpdateActivity.this.finish();
            }
        });
        String string = getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_app_download_info_new"));
        a2.a((a.b) new a.b() {
            public void a() {
                AppUpdateActivity.this.finish();
            }
        });
        a2.a(a.C0020a.CONFIRM, string);
        a2.a((DialogInterface.OnKeyListener) new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4 || keyEvent.getAction() != 0) {
                    return false;
                }
                if (AppUpdateActivity.this.k) {
                    a2.c();
                    AppUpdateActivity.this.e(AppUpdateActivity.this.j);
                    return true;
                }
                int unused = AppUpdateActivity.this.m = 4;
                AppUpdateActivity.this.finish();
                return true;
            }
        });
    }

    private void c(final String str) {
        if (this.a == null || !this.a.isShowing()) {
            this.a = new AlertDialog.Builder(this).create();
            View inflate = LayoutInflater.from(this).inflate(com.huawei.updatesdk.support.e.d.c(this, "upsdk_app_dl_progress_dialog"), (ViewGroup) null);
            this.d = (ProgressBar) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "third_app_dl_progressbar"));
            this.d.setMax(100);
            this.e = (TextView) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "third_app_dl_progress_text"));
            this.f = (ImageView) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "cancel_imageview"));
            this.g = (RelativeLayout) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "cancel_bg"));
            this.g.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AppUpdateActivity.this.b(str);
                }
            });
            this.a.setView(inflate);
            this.a.setCancelable(false);
            this.a.setCanceledOnTouchOutside(false);
            TextView textView = (TextView) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "third_app_warn_text"));
            if ("com.huawei.appmarket".equals(str)) {
                textView.setText(getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_app_dl_installing")));
            }
            if (!com.huawei.updatesdk.support.e.a.a(this)) {
                this.a.show();
            }
            this.e.setText(com.huawei.updatesdk.support.b.d.a(0));
        }
    }

    private long d(ApkUpgradeInfo apkUpgradeInfo) {
        return apkUpgradeInfo.getDiffSize_() > 0 ? (long) apkUpgradeInfo.getDiffSize_() : (long) apkUpgradeInfo.getSize_();
    }

    private void d() {
        int b2 = com.huawei.updatesdk.support.e.c.a().b();
        if (b2 >= 11 && b2 < 17) {
            this.c.a(com.huawei.updatesdk.support.e.d.d(this, "upsdk_update_all_button"), com.huawei.updatesdk.support.e.d.e(this, "upsdk_white"));
        }
    }

    private void e() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme(WVConfigManager.CONFIGNAME_PACKAGE);
        this.q = new c();
        com.huawei.updatesdk.support.e.a.a(this, intentFilter, this.q);
    }

    /* access modifiers changed from: private */
    public void e(ApkUpgradeInfo apkUpgradeInfo) {
        if (apkUpgradeInfo == null) {
            finish();
            return;
        }
        String string = TextUtils.isEmpty(apkUpgradeInfo.getNewFeatures_()) ? getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_choice_update")) : apkUpgradeInfo.getNewFeatures_();
        String a2 = com.huawei.updatesdk.support.b.d.a(this, d(apkUpgradeInfo));
        String version_ = apkUpgradeInfo.getVersion_();
        String name_ = apkUpgradeInfo.getName_();
        String string2 = getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_ota_title"));
        String string3 = getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_ota_notify_updatebtn"));
        String string4 = getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_ota_cancel"));
        View inflate = LayoutInflater.from(this).inflate(com.huawei.updatesdk.support.e.d.c(this, "upsdk_ota_update_view"), (ViewGroup) null);
        ((TextView) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "content_textview"))).setText(string);
        ((TextView) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "version_textview"))).setText(version_);
        ((TextView) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "appsize_textview"))).setText(a2);
        ((TextView) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "name_textview"))).setText(name_);
        a(apkUpgradeInfo, (TextView) inflate.findViewById(com.huawei.updatesdk.support.e.d.a(this, "allsize_textview")));
        a(inflate);
        this.c = com.huawei.updatesdk.support.f.a.a(this, string2, (CharSequence) null);
        this.c.a(inflate);
        if (1 == apkUpgradeInfo.getIsCompulsoryUpdate_()) {
            string4 = getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_ota_force_cancel_new"));
            if (this.l) {
                this.c.a();
            }
            this.h = true;
        }
        this.c.a((com.huawei.updatesdk.support.f.b) new com.huawei.updatesdk.support.f.b() {
            public void a() {
                int unused = AppUpdateActivity.this.o = 101;
                if (!com.huawei.updatesdk.sdk.a.d.c.b.a((Context) AppUpdateActivity.this)) {
                    Toast.makeText(AppUpdateActivity.this, com.huawei.updatesdk.support.e.d.b(AppUpdateActivity.this, "upsdk_no_available_network_prompt_toast"), 0).show();
                    int unused2 = AppUpdateActivity.this.m = 2;
                    AppUpdateActivity.this.finish();
                    return;
                }
                AppUpdateActivity.this.c();
            }

            public void b() {
                AppUpdateActivity.this.c.c();
                int unused = AppUpdateActivity.this.m = 4;
                int unused2 = AppUpdateActivity.this.o = 100;
                AppUpdateActivity.this.finish();
            }
        });
        b();
        this.c.a((a.b) new a.b() {
            public void a() {
                AppUpdateActivity.this.finish();
            }
        });
        if (this.h) {
            this.c.a(false);
        } else {
            this.c.a((DialogInterface.OnKeyListener) new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (i != 4 || keyEvent.getAction() != 0) {
                        return false;
                    }
                    int unused = AppUpdateActivity.this.m = 4;
                    AppUpdateActivity.this.finish();
                    return true;
                }
            });
        }
        this.c.a(a.C0020a.CONFIRM, string3);
        this.c.a(a.C0020a.CANCEL, string4);
        d();
    }

    /* access modifiers changed from: private */
    public void f() {
        try {
            if (this.a != null && this.a.isShowing()) {
                this.a.dismiss();
                this.a = null;
            }
        } catch (IllegalArgumentException unused) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("AppUpdateActivity", "progressDialog dismiss IllegalArgumentException");
        }
    }

    public void a(int i2) {
        Toast.makeText(this, getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_getting_message_fail_prompt_toast")), 0).show();
        b.a().a(i2);
        finish();
    }

    public void a(int i2, final com.huawei.updatesdk.sdk.service.b.b bVar) {
        if (bVar != null) {
            if (i2 == 0) {
                Bundle a2 = bVar.a();
                if (a2 != null) {
                    int i3 = a2.getInt("downloadtask.status", -1);
                    b.a().a(a(-1, -1, i3));
                    switch (i3) {
                        case 3:
                        case 4:
                        case 7:
                            f();
                            return;
                        case 5:
                        case 6:
                        case 8:
                            f();
                            Toast.makeText(this, getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_third_app_dl_install_failed")), 0).show();
                            finish();
                            return;
                        default:
                            return;
                    }
                }
            } else if (1 == i2) {
                DownloadTask a3 = DownloadTask.a(bVar.a("downloadtask.all"));
                if (a3 != null) {
                    int z = a3.z();
                    if (this.d != null) {
                        this.d.setProgress(z);
                        this.e.setText(com.huawei.updatesdk.support.b.d.a((int) ((((float) this.d.getProgress()) / ((float) this.d.getMax())) * 100.0f)));
                    }
                }
            } else if (2 == i2) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        Bundle a2 = bVar.a();
                        if (a2 != null) {
                            int i = a2.getInt("INSTALL_STATE");
                            b.a().a(AppUpdateActivity.this.a(i, a2.getInt("INSTALL_TYPE"), -1));
                            AppUpdateActivity.this.c(i);
                        }
                    }
                });
            }
        }
    }

    public void a(ApkUpgradeInfo apkUpgradeInfo) {
        if (apkUpgradeInfo != null) {
            a((Context) this);
            c(apkUpgradeInfo);
            return;
        }
        Toast.makeText(this, getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_getting_message_fail_prompt_toast")), 0).show();
        finish();
    }

    public void b(int i2) {
        Toast.makeText(this, getString(com.huawei.updatesdk.support.e.d.b(this, "upsdk_connect_server_fail_prompt_toast")), 0).show();
        b.a().a(i2);
        finish();
    }

    public void b(ApkUpgradeInfo apkUpgradeInfo) {
        e();
        c(apkUpgradeInfo.getPackage_());
    }

    public void finish() {
        this.p = new Intent();
        this.p.putExtra("status", this.m);
        this.p.putExtra(UpdateKey.FAIL_CODE, this.n);
        this.p.putExtra(UpdateKey.MUST_UPDATE, this.h);
        this.p.putExtra(UpdateKey.BUTTON_STATUS, this.o);
        super.finish();
    }

    public void onActivityResult(int i2, int i3, Intent intent) {
        if (i2 == 1002) {
            if (intent != null) {
                com.huawei.updatesdk.sdk.service.b.b a2 = com.huawei.updatesdk.sdk.service.b.b.a(intent);
                this.m = i3;
                this.n = a2.a("installResultCode", -99);
                if (this.j.getIsCompulsoryUpdate_() == 1) {
                    this.h = a2.a(UpdateKey.MUST_UPDATE, false);
                }
            }
            if (this.j.getIsCompulsoryUpdate_() == 1 && i3 == 4) {
                this.h = true;
            }
            this.o = i3 == 4 ? 100 : 101;
            if (!this.i) {
                finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        getWindow().addFlags(67108864);
        super.onCreate(bundle);
        Bundle a2 = com.huawei.updatesdk.sdk.service.b.b.a(getIntent()).a();
        if (a2 == null) {
            super.finish();
            return;
        }
        Serializable serializable = a2.getSerializable("app_update_parm");
        if (serializable == null || !(serializable instanceof ApkUpgradeInfo)) {
            this.m = 3;
            finish();
            return;
        }
        this.j = (ApkUpgradeInfo) serializable;
        this.l = a2.getBoolean("app_must_btn", false);
        if (this.j.getIsCompulsoryUpdate_() == 1) {
            this.k = true;
        }
        if (this.j.getDevType_() == 1 && com.huawei.updatesdk.support.c.a.a((Context) this, "com.huawei.appmarket") == a.C0019a.INSTALLED) {
            a(this.j.getPackage_());
        } else {
            e(this.j);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.b != null) {
            this.b.c();
            this.b = null;
        }
        if (this.c != null) {
            this.c.c();
            this.c = null;
        }
        f();
        a();
        d.a((a) null);
        super.onDestroy();
        finishActivity(1002);
        if (this.p != null) {
            b.a().b(this.p);
        }
    }
}
