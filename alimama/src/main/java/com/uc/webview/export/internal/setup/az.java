package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.webkit.ValueCallback;
import com.alibaba.wireless.security.SecExceptionCode;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.k;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public final class az extends av {
    public final void run() {
        b.a(279);
        if (!af.b() || getUCM() == null) {
            a(k.a((Context) this.mOptions.get("CONTEXT"), (ConcurrentHashMap<String, Object>) this.mOptions));
        } else {
            b(getUCM());
        }
        b.a(280);
    }

    /* access modifiers changed from: protected */
    public final bs a(br brVar) {
        return new a(this, brVar);
    }

    /* compiled from: U4Source */
    class a extends bs {
        final ValueCallback<Object> a = new ba(this);

        public a(UCSubSetupTask uCSubSetupTask, br brVar) {
            super(uCSubSetupTask, brVar);
        }

        /* access modifiers changed from: protected */
        public final boolean a() {
            b.a(298);
            Context context = (Context) this.f.getOption("CONTEXT");
            boolean z = !k.b((Boolean) az.this.mOptions.get(UCCore.OPTION_USE_SDK_SETUP));
            if (z) {
                int i = ae.d.b;
                ae.b bVar = ae.b.CHECK_VERSION;
                ae a2 = ae.a();
                a2.getClass();
                a(i, bVar, new ae.a(new bb(this, context), this.a), this.a);
            }
            int i2 = ae.d.b;
            ae.b bVar2 = ae.b.CHECK_OLD_KERNEL;
            ae a3 = ae.a();
            a3.getClass();
            a(i2, bVar2, new ae.a(new bc(this, context, z), this.a), this.a);
            b.a(SecExceptionCode.SEC_ERROR_STA_STORE_UNKNOWN_ERROR);
            return true;
        }

        /* access modifiers changed from: protected */
        public final void b() {
            b.a(300);
            Context context = (Context) this.f.getOption("CONTEXT");
            Integer num = (Integer) this.f.getOption(UCCore.OPTION_VERIFY_POLICY);
            b.a(302);
            b.a(303);
            if (!(num == null || (num.intValue() & 8) == 0)) {
                int i = ae.d.b;
                ae.b bVar = ae.b.CHECK_SO;
                ae a2 = ae.a();
                a2.getClass();
                a(i, bVar, new ae.a(new bd(this, context, num), this.a), this.a);
            }
            b.a(304);
            if (!(num == null || (num.intValue() & 32) == 0)) {
                int i2 = ae.d.b;
                ae.b bVar2 = ae.b.CHECK_PAK;
                ae a3 = ae.a();
                a3.getClass();
                a(i2, bVar2, new ae.a(new be(this, context, num), this.a), this.a);
            }
            b.a(301);
        }
    }

    /* access modifiers changed from: protected */
    public final void a(String str, Object obj) {
        if (!k.a(str) && obj != null) {
            try {
                if (!((Boolean) ((UCCore.Callable) obj).call(str)).booleanValue()) {
                    throw new UCSetupException(4031, "inject failed.");
                }
            } catch (Exception e) {
                throw new UCSetupException(4031, (Throwable) e);
            }
        }
    }
}
