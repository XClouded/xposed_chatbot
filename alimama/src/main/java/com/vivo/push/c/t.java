package com.vivo.push.c;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import com.vivo.push.b.aa;
import com.vivo.push.b.r;
import com.vivo.push.model.InsideNotificationItem;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.util.NotifyAdapterUtil;
import com.vivo.push.util.p;
import com.vivo.push.util.q;
import com.vivo.push.util.z;
import com.vivo.push.w;
import com.vivo.push.y;
import java.util.HashMap;
import java.util.Map;

/* compiled from: OnNotificationClickTask */
final class t extends ab {
    t(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        r rVar = (r) yVar;
        InsideNotificationItem f = rVar.f();
        if (f == null) {
            p.d("OnNotificationClickTask", "current notification item is null");
            return;
        }
        UPSNotificationMessage a = q.a(f);
        boolean equals = this.a.getPackageName().equals(rVar.d());
        if (equals) {
            NotifyAdapterUtil.cancelNotify(this.a);
        }
        if (equals) {
            aa aaVar = new aa(1030);
            HashMap hashMap = new HashMap();
            hashMap.put("type", "2");
            hashMap.put("messageID", String.valueOf(rVar.e()));
            hashMap.put("platform", this.a.getPackageName());
            String b = z.b(this.a, this.a.getPackageName());
            if (!TextUtils.isEmpty(b)) {
                hashMap.put("remoteAppId", b);
            }
            aaVar.a(hashMap);
            com.vivo.push.p.a().a((y) aaVar);
            p.d("OnNotificationClickTask", "notification is clicked by skip type[" + a.getSkipType() + Operators.ARRAY_END_STR);
            boolean z = true;
            switch (a.getSkipType()) {
                case 1:
                    new Thread(new y(this, this.a, a.getParams())).start();
                    w.b(new u(this, a));
                    return;
                case 2:
                    String skipContent = a.getSkipContent();
                    if (!skipContent.startsWith("http://") && !skipContent.startsWith("https://")) {
                        z = false;
                    }
                    if (z) {
                        Uri parse = Uri.parse(skipContent);
                        Intent intent = new Intent("android.intent.action.VIEW", parse);
                        intent.setFlags(268435456);
                        b(intent, a.getParams());
                        try {
                            this.a.startActivity(intent);
                        } catch (Exception unused) {
                            p.a("OnNotificationClickTask", "startActivity error : " + parse);
                        }
                    } else {
                        p.a("OnNotificationClickTask", "url not legal");
                    }
                    w.b(new v(this, a));
                    return;
                case 3:
                    w.b(new w(this, a));
                    return;
                case 4:
                    String skipContent2 = a.getSkipContent();
                    try {
                        Intent parseUri = Intent.parseUri(skipContent2, 1);
                        String str = parseUri.getPackage();
                        if (TextUtils.isEmpty(str) || this.a.getPackageName().equals(str)) {
                            String packageName = parseUri.getComponent() == null ? null : parseUri.getComponent().getPackageName();
                            if (TextUtils.isEmpty(packageName) || this.a.getPackageName().equals(packageName)) {
                                parseUri.setPackage(this.a.getPackageName());
                                parseUri.addFlags(268435456);
                                b(parseUri, a.getParams());
                                this.a.startActivity(parseUri);
                                w.b(new x(this, a));
                                return;
                            }
                            p.a("OnNotificationClickTask", "open activity component error : local pkgName is " + this.a.getPackageName() + "; but remote pkgName is " + parseUri.getPackage());
                            return;
                        }
                        p.a("OnNotificationClickTask", "open activity error : local pkgName is " + this.a.getPackageName() + "; but remote pkgName is " + parseUri.getPackage());
                        return;
                    } catch (Exception e) {
                        p.a("OnNotificationClickTask", "open activity error : " + skipContent2, e);
                    }
                    break;
                default:
                    p.a("OnNotificationClickTask", "illegitmacy skip type error : " + a.getSkipType());
                    return;
            }
        } else {
            p.a("OnNotificationClickTask", "notify is " + a + " ; isMatch is " + equals);
        }
    }

    /* access modifiers changed from: private */
    public static Intent b(Intent intent, Map<String, String> map) {
        if (map == null || map.entrySet() == null) {
            return intent;
        }
        for (Map.Entry next : map.entrySet()) {
            if (!(next == null || next.getKey() == null)) {
                intent.putExtra((String) next.getKey(), (String) next.getValue());
            }
        }
        return intent;
    }
}
