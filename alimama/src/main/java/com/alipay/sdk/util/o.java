package com.alipay.sdk.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

final class o implements DownloadListener {
    final /* synthetic */ Context a;

    o(Context context) {
        this.a = context;
    }

    public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.setFlags(268435456);
            this.a.startActivity(intent);
        } catch (Throwable unused) {
        }
    }
}
