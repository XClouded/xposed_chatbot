package com.vivo.push.c;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.vivo.push.p;
import com.vivo.push.sdk.PushMessageCallback;
import com.vivo.push.util.t;
import com.vivo.push.v;
import com.vivo.push.y;
import java.security.PublicKey;

/* compiled from: OnReceiveTask */
public abstract class ab extends v {
    protected PushMessageCallback b;

    ab(y yVar) {
        super(yVar);
    }

    public final void a(PushMessageCallback pushMessageCallback) {
        this.b = pushMessageCallback;
    }

    public final boolean a(PublicKey publicKey, String str, String str2) {
        if (!p.a().d()) {
            com.vivo.push.util.p.d("OnVerifyCallBackCommand", "vertify is not support , vertify is ignore");
            return true;
        } else if (publicKey == null) {
            com.vivo.push.util.p.d("OnVerifyCallBackCommand", "vertify key is null");
            return false;
        } else if (TextUtils.isEmpty(str)) {
            com.vivo.push.util.p.d("OnVerifyCallBackCommand", "contentTag is null");
            return false;
        } else if (!TextUtils.isEmpty(str2)) {
            try {
                com.vivo.push.util.p.d("OnVerifyCallBackCommand", str.hashCode() + " = " + str2);
                if (t.a(str.getBytes("UTF-8"), publicKey, Base64.decode(str2, 2))) {
                    com.vivo.push.util.p.d("OnVerifyCallBackCommand", "vertify id is success");
                    return true;
                }
                com.vivo.push.util.p.d("OnVerifyCallBackCommand", "vertify fail srcDigest is " + str);
                Context context = this.a;
                com.vivo.push.util.p.c(context, "vertify fail srcDigest is " + str);
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                com.vivo.push.util.p.d("OnVerifyCallBackCommand", "vertify exception");
                return false;
            }
        } else {
            com.vivo.push.util.p.d("OnVerifyCallBackCommand", "vertify id is null");
            return false;
        }
    }
}
