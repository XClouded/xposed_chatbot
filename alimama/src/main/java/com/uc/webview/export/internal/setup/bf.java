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
public class bf extends av {
    public void run() {
        b.a(281);
        if (!af.b() || getUCM() == null) {
            a(k.b((Context) this.mOptions.get("CONTEXT"), (ConcurrentHashMap<String, Object>) this.mOptions));
        } else {
            b(getUCM());
        }
        b.a(282);
    }

    /* access modifiers changed from: protected */
    public final bs a(br brVar) {
        return new a(this, brVar);
    }

    /* compiled from: U4Source */
    class a extends bs {
        int a = 0;
        boolean b;
        final ValueCallback<Object> c = new bg(this);

        public a(UCSubSetupTask uCSubSetupTask, br brVar) {
            super(uCSubSetupTask, brVar);
            Integer num = (Integer) this.f.getOption(UCCore.OPTION_VERIFY_POLICY);
            if (num != null) {
                this.a = num.intValue();
            }
            this.b = !k.b((Boolean) this.f.getOption(UCCore.OPTION_USE_SDK_SETUP));
        }

        /* access modifiers changed from: protected */
        public final boolean a() {
            b.a(298);
            this.f.getOption("CONTEXT");
            this.f.getOption(UCCore.OPTION_VERIFY_POLICY);
            if ((this.a & 4) != 0) {
                int i = ae.d.b;
                ae.b bVar = ae.b.VERIFY_CORE_JAR;
                ae a2 = ae.a();
                a2.getClass();
                a(i, bVar, new ae.a(new bh(this), this.c), this.c);
                b.a(SecExceptionCode.SEC_ERROR_STA_STORE_UNKNOWN_ERROR);
                return true;
            }
            b.a(SecExceptionCode.SEC_ERROR_STA_STORE_UNKNOWN_ERROR);
            return false;
        }

        /* access modifiers changed from: protected */
        public final void b() {
            b.a(300);
            b.a(302);
            int i = ae.d.b;
            ae.b bVar = ae.b.CHECK_OLD_KERNEL;
            ae a2 = ae.a();
            a2.getClass();
            a(i, bVar, new ae.a(new bi(this), this.c), this.c);
            b.a(303);
            if ((this.a & 1) != 0) {
                int i2 = ae.d.b;
                ae.b bVar2 = ae.b.VERIFY_SDK_SHELL;
                ae a3 = ae.a();
                a3.getClass();
                a(i2, bVar2, new ae.a(new bj(this), this.c), this.c);
            }
            b.a(304);
            int i3 = ae.d.b;
            ae.b bVar3 = ae.b.LOAD_SDK_SHELL;
            ae a4 = ae.a();
            a4.getClass();
            a(i3, bVar3, new ae.a(new bk(this), this.c), this.c);
            b.a(301);
        }

        static /* synthetic */ void a(a aVar) {
            if (!k.b((Boolean) aVar.f.getOption(UCCore.OPTION_USE_SDK_SETUP))) {
                if (ae.d.b != ae.d.b) {
                    ae.a().a(new ae.b[]{ae.b.LOAD_SDK_SHELL});
                }
                int i = ae.d.b;
                ae.b bVar = ae.b.CHECK_VERSION;
                ae a2 = ae.a();
                a2.getClass();
                aVar.a(i, bVar, new ae.a(new bl(aVar), aVar.c), aVar.c);
            }
        }

        static /* synthetic */ void b(a aVar) {
            if (ae.d.b != ae.d.b) {
                ae.a().a(new ae.b[]{ae.b.LOAD_SDK_SHELL});
            }
            if ((aVar.a & 8) != 0) {
                int i = ae.d.b;
                ae.b bVar = ae.b.CHECK_SO;
                ae a2 = ae.a();
                a2.getClass();
                aVar.a(i, bVar, new ae.a(new bm(aVar), aVar.c), aVar.c);
            }
        }

        static /* synthetic */ void c(a aVar) {
            if (ae.d.b != ae.d.b) {
                ae.a().a(new ae.b[]{ae.b.LOAD_SDK_SHELL});
            }
            if ((aVar.a & 32) != 0) {
                int i = ae.d.b;
                ae.b bVar = ae.b.CHECK_PAK;
                ae a2 = ae.a();
                a2.getClass();
                aVar.a(i, bVar, new ae.a(new bn(aVar), aVar.c), aVar.c);
            }
        }
    }
}
