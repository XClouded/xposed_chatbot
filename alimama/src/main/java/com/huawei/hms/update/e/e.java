package com.huawei.hms.update.e;

import android.app.AlertDialog;
import com.huawei.hms.c.h;

/* compiled from: ConfirmDialogs */
public final class e {

    /* compiled from: ConfirmDialogs */
    public static class b extends a {
        public b() {
            super();
        }

        public /* bridge */ /* synthetic */ AlertDialog a() {
            return super.a();
        }

        /* access modifiers changed from: protected */
        public int h() {
            return h.c("hms_download_retry");
        }

        /* access modifiers changed from: protected */
        public int i() {
            return h.c("hms_retry");
        }

        /* access modifiers changed from: protected */
        public int j() {
            return h.c("hms_cancel");
        }
    }

    /* compiled from: ConfirmDialogs */
    public static class c extends a {
        public c() {
            super();
        }

        public /* bridge */ /* synthetic */ AlertDialog a() {
            return super.a();
        }

        /* access modifiers changed from: protected */
        public int h() {
            return h.c("hms_abort_message");
        }

        /* access modifiers changed from: protected */
        public int i() {
            return h.c("hms_abort");
        }

        /* access modifiers changed from: protected */
        public int j() {
            return h.c("hms_cancel");
        }
    }

    /* compiled from: ConfirmDialogs */
    private static abstract class a extends b {
        /* access modifiers changed from: protected */
        public abstract int h();

        /* access modifiers changed from: protected */
        public abstract int i();

        /* access modifiers changed from: protected */
        public abstract int j();

        private a() {
        }

        public AlertDialog a() {
            AlertDialog.Builder builder = new AlertDialog.Builder(f(), g());
            builder.setMessage(h());
            builder.setPositiveButton(i(), new g(this));
            builder.setNegativeButton(j(), new h(this));
            return builder.create();
        }
    }
}
