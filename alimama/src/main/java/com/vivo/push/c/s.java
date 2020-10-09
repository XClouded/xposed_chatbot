package com.vivo.push.c;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.vivo.push.model.InsideNotificationItem;
import com.vivo.push.util.l;
import com.vivo.push.util.p;
import com.vivo.push.util.q;

/* compiled from: OnNotificationArrivedReceiveTask */
final class s implements Runnable {
    final /* synthetic */ InsideNotificationItem a;
    final /* synthetic */ com.vivo.push.b.s b;
    final /* synthetic */ r c;

    s(r rVar, InsideNotificationItem insideNotificationItem, com.vivo.push.b.s sVar) {
        this.c = rVar;
        this.a = insideNotificationItem;
        this.b = sVar;
    }

    public final void run() {
        char c2;
        if (this.c.b.onNotificationMessageArrived(this.c.a, q.a(this.a))) {
            p.d("OnNotificationArrivedTask", "this notification has Intercept");
            return;
        }
        l lVar = new l(this.c.a, this.a, this.b.f(), this.c.b.isAllowNet(this.c.a));
        boolean isShowBigPicOnMobileNet = this.a.isShowBigPicOnMobileNet();
        String purePicUrl = this.a.getPurePicUrl();
        if (TextUtils.isEmpty(purePicUrl)) {
            purePicUrl = this.a.getCoverUrl();
        }
        if (!TextUtils.isEmpty(purePicUrl)) {
            p.c("OnNotificationArrivedTask", "showCode=" + isShowBigPicOnMobileNet);
            if (!isShowBigPicOnMobileNet) {
                p.a(this.c.a, "mobile net unshow");
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.c.a.getSystemService("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    int type = activeNetworkInfo.getType();
                    c2 = type == 1 ? 2 : type == 0 ? (char) 1 : 3;
                } else {
                    c2 = 0;
                }
                if (c2 == 1) {
                    purePicUrl = null;
                    this.a.clearCoverUrl();
                    this.a.clearPurePicUrl();
                }
            } else {
                p.a(this.c.a, "mobile net show");
            }
        }
        lVar.execute(new String[]{this.a.getIconUrl(), purePicUrl});
    }
}
