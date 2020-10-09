package com.huawei.hms.update.e;

import android.app.AlertDialog;
import com.huawei.hms.c.h;

/* compiled from: PromptDialogs */
public final class q {

    /* compiled from: PromptDialogs */
    public static class b extends a {
        public b() {
            super();
        }

        public /* bridge */ /* synthetic */ AlertDialog a() {
            return super.a();
        }

        /* access modifiers changed from: protected */
        public int h() {
            return h.c("hms_check_failure");
        }
    }

    /* compiled from: PromptDialogs */
    public static class c extends a {
        public c() {
            super();
        }

        public /* bridge */ /* synthetic */ AlertDialog a() {
            return super.a();
        }

        /* access modifiers changed from: protected */
        public int h() {
            return h.c("hms_download_failure");
        }
    }

    /* compiled from: PromptDialogs */
    public static class d extends a {
        public d() {
            super();
        }

        public /* bridge */ /* synthetic */ AlertDialog a() {
            return super.a();
        }

        /* access modifiers changed from: protected */
        public int h() {
            return h.c("hms_download_no_space");
        }
    }

    /* compiled from: PromptDialogs */
    private static abstract class a extends b {
        /* access modifiers changed from: protected */
        public abstract int h();

        private a() {
        }

        public AlertDialog a() {
            AlertDialog.Builder builder = new AlertDialog.Builder(f(), g());
            builder.setMessage(h());
            builder.setPositiveButton(i(), new s(this));
            return builder.create();
        }

        /* access modifiers changed from: protected */
        public int i() {
            return h.c("hms_confirm");
        }
    }
}
