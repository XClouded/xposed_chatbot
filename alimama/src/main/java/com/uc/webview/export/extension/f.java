package com.uc.webview.export.extension;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.d;
import java.util.Map;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class f implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;
    final /* synthetic */ Callable c;
    final /* synthetic */ Map d;

    f(Context context, String str, Callable callable, Map map) {
        this.a = context;
        this.b = str;
        this.c = callable;
        this.d = map;
    }

    public final void run() {
        while (!SDKFactory.b()) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (SDKFactory.c((Long) 1L).booleanValue()) {
            Log.i("ucmedia.UCCore", "force system webview, don't download uc player");
        } else if (SDKFactory.c(Long.valueOf(PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED)).booleanValue()) {
            Log.i("ucmedia.UCCore", "use ucmobile apollo, don't download uc player");
        } else if (!d.a().b(UCCore.OPTION_USE_UC_PLAYER)) {
            Log.i("ucmedia.UCCore", "sUseUCPlayer is false, don't download uc player");
        } else {
            try {
                UCCore.a(this.a, this.b, this.c, this.d);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
