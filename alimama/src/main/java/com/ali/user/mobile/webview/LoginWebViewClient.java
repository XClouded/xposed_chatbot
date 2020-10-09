package com.ali.user.mobile.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.ui.R;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebView;
import java.lang.ref.WeakReference;
import java.util.Properties;

public class LoginWebViewClient extends WVUCWebViewClient {
    protected boolean firstAlert = true;
    protected boolean proceed = false;
    protected WeakReference<Activity> reference;

    /* access modifiers changed from: protected */
    public boolean overrideUrlLoading(WebView webView, String str) {
        return true;
    }

    public LoginWebViewClient(Activity activity) {
        super(activity);
        this.reference = new WeakReference<>(activity);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x002a A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldOverrideUrlLoading(com.uc.webview.export.WebView r5, java.lang.String r6) {
        /*
            r4 = this;
            java.lang.ref.WeakReference<android.app.Activity> r0 = r4.reference
            java.lang.Object r0 = r0.get()
            android.app.Activity r0 = (android.app.Activity) r0
            if (r0 == 0) goto L_0x0026
            boolean r0 = r4.overrideUrlLoading(r5, r6)     // Catch:{ Exception -> 0x000f }
            goto L_0x0027
        L_0x000f:
            r0 = move-exception
            java.lang.String r1 = "WebViewActivity"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "webview内跳转地址有问题"
            r2.append(r3)
            r2.append(r6)
            java.lang.String r2 = r2.toString()
            com.ali.user.mobile.log.TLogAdapter.e(r1, r2, r0)
        L_0x0026:
            r0 = 0
        L_0x0027:
            r1 = 1
            if (r0 != r1) goto L_0x002b
            return r0
        L_0x002b:
            boolean r5 = super.shouldOverrideUrlLoading(r5, r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.webview.LoginWebViewClient.shouldOverrideUrlLoading(com.uc.webview.export.WebView, java.lang.String):boolean");
    }

    public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
        TLogAdapter.e("WebViewActivity", "已忽略证书校验的错误！");
        Properties properties = new Properties();
        if (webView.getUrl() != null) {
            properties.setProperty("url", webView.getUrl());
        }
        if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
            properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
        }
        UserTrackAdapter.sendUT("Event_ReceivedSslError", properties);
        Activity activity = (Activity) this.reference.get();
        if (this.firstAlert) {
            String string = webView.getContext().getResources().getString(R.string.aliuser_ssl_error_title);
            String string2 = webView.getContext().getResources().getString(R.string.aliuser_ssl_error_info);
            String string3 = webView.getContext().getResources().getString(R.string.aliuser_common_ok);
            AnonymousClass1 r6 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    sslErrorHandler.proceed();
                    LoginWebViewClient.this.proceed = true;
                }
            };
            String string4 = webView.getContext().getResources().getString(R.string.aliuser_confirm_cancel);
            AnonymousClass2 r8 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    sslErrorHandler.cancel();
                    LoginWebViewClient.this.proceed = false;
                }
            };
            if (activity instanceof BaseActivity) {
                ((BaseActivity) activity).alert(string, string2, string3, r6, string4, r8);
                this.firstAlert = false;
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
            builder.setPositiveButton(string3, r6);
            builder.setNeutralButton(string4, r8);
            try {
                AlertDialog create = builder.create();
                create.setTitle(string);
                create.setMessage(string2);
                create.show();
                this.firstAlert = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.proceed) {
            sslErrorHandler.proceed();
        } else {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }

    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
    }
}
