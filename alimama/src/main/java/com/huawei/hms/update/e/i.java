package com.huawei.hms.update.e;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huawei.hms.c.h;
import java.text.NumberFormat;

/* compiled from: DownloadProgress */
public class i extends b {
    private ProgressBar a;
    private TextView b;
    private int c = 0;
    private DialogInterface.OnKeyListener d = new a();

    public AlertDialog a() {
        AlertDialog.Builder builder = new AlertDialog.Builder(f(), g());
        View inflate = View.inflate(f(), h.a("hms_download_progress"), (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(false);
        builder.setOnKeyListener(this.d);
        this.a = (ProgressBar) inflate.findViewById(h.b("download_info_progress"));
        this.b = (TextView) inflate.findViewById(h.b("hms_progress_text"));
        b(this.c);
        return builder.create();
    }

    public void a(int i) {
        this.c = i;
    }

    /* access modifiers changed from: package-private */
    public void b(int i) {
        Activity f = f();
        if (f == null || f.isFinishing()) {
            com.huawei.hms.support.log.a.c("DownloadProgress", "In setDownloading, The activity is null or finishing.");
        } else if (this.b != null && this.a != null) {
            this.a.setProgress(i);
            this.b.setText(NumberFormat.getPercentInstance().format((double) (((float) i) / 100.0f)));
        }
    }

    /* compiled from: DownloadProgress */
    private static class a implements DialogInterface.OnKeyListener {
        private a() {
        }

        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            return i == 4 && keyEvent.getRepeatCount() == 0;
        }
    }
}
