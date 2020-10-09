package com.uc.webview.export.extension;

import android.content.Context;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.bw;
import com.uc.webview.export.internal.setup.l;
import com.uc.webview.export.internal.utility.k;
import java.util.Map;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class a implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ Map b;
    final /* synthetic */ Callable c;
    final /* synthetic */ String d;
    final /* synthetic */ Map e;

    a(Context context, Map map, Callable callable, String str, Map map2) {
        this.a = context;
        this.b = map;
        this.c = callable;
        this.d = str;
        this.e = map2;
    }

    public final void run() {
        int i = 10;
        while (true) {
            if (SDKFactory.b() || SDKFactory.j) {
                break;
            }
            int i2 = i - 1;
            if (i <= 0) {
                i = i2;
                break;
            }
            try {
                Thread.sleep(200);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            i = i2;
        }
        if (i > 0) {
            try {
                UCAsyncTask uCAsyncTask = new UCAsyncTask((Integer) 0);
                bw bwVar = new bw();
                ((l) ((l) ((l) ((l) ((l) ((l) ((l) bwVar.setup("CONTEXT", (Object) this.a.getApplicationContext())).setParent(uCAsyncTask)).setup(UCCore.OPTION_UCM_ZIP_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_USE_SDK_SETUP, (Object) true)).setup(UCCore.OPTION_CHECK_MULTI_CORE, (Object) true)).onEvent(UCCore.EVENT_DOWNLOAD_EXCEPTION, new c(this))).onEvent(UCCore.EVENT_UPDATE_PROGRESS, new b(this));
                if (this.c != null) {
                    bwVar.setup(UCCore.OPTION_DOWNLOAD_CHECKER, (Object) this.c);
                }
                if (!k.a(this.d)) {
                    bwVar.setup(UCCore.OPTION_UCM_UPD_URL, (Object) this.d);
                }
                if (this.e != null && !this.e.isEmpty()) {
                    for (Map.Entry entry : this.e.entrySet()) {
                        bwVar.setup((String) entry.getKey(), entry.getValue());
                    }
                }
                bwVar.start(2000);
                uCAsyncTask.start();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        } else {
            throw new UCSetupException("Waiting timeout for UCCore initialization finish!");
        }
    }
}
