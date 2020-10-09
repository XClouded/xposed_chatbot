package com.uc.webview.export.internal.android;

import android.content.Intent;
import android.os.Build;
import com.uc.webview.export.WebChromeClient;
import com.uc.webview.export.internal.utility.k;

/* compiled from: U4Source */
final class l extends WebChromeClient.FileChooserParams {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ i c;

    public final String getFilenameHint() {
        return "";
    }

    public final int getMode() {
        return 0;
    }

    public final CharSequence getTitle() {
        return "";
    }

    l(i iVar, String str, String str2) {
        this.c = iVar;
        this.a = str;
        this.b = str2;
    }

    public final String[] getAcceptTypes() {
        String[] strArr = new String[1];
        strArr[0] = k.a(this.a) ? "*/*" : this.a;
        return strArr;
    }

    public final boolean isCaptureEnabled() {
        return !k.a(this.b);
    }

    public final Intent createIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        if (Build.VERSION.SDK_INT >= 16) {
            intent.setTypeAndNormalize(getAcceptTypes()[0]);
        } else {
            intent.setType(getAcceptTypes()[0]);
        }
        return intent;
    }
}
