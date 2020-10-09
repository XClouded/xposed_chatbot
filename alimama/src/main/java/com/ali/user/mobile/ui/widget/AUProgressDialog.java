package com.ali.user.mobile.ui.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ali.user.mobile.R;

public class AUProgressDialog extends ProgressDialog {
    private boolean mIndeterminate;
    private CharSequence mMessage;
    private TextView mMessageView;
    private ProgressBar mProgress;
    private boolean mProgressVisiable;

    public AUProgressDialog(Context context) {
        super(context);
    }

    public AUProgressDialog(Context context, int i) {
        super(context, i);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.aliuser_progress_dialog);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(17170445);
        }
        this.mProgress = (ProgressBar) findViewById(16908301);
        this.mMessageView = (TextView) findViewById(R.id.aliuser_toast_message);
        setMessageAndView();
        setIndeterminate(this.mIndeterminate);
    }

    private void setMessageAndView() {
        this.mMessageView.setText(this.mMessage);
        int i = 8;
        if (this.mMessage == null || "".equals(this.mMessage)) {
            this.mMessageView.setVisibility(8);
        }
        ProgressBar progressBar = this.mProgress;
        if (this.mProgressVisiable) {
            i = 0;
        }
        progressBar.setVisibility(i);
    }

    public void setMessage(CharSequence charSequence) {
        this.mMessage = charSequence;
    }

    public void setProgressVisiable(boolean z) {
        this.mProgressVisiable = z;
    }

    public void setIndeterminate(boolean z) {
        if (this.mProgress != null) {
            this.mProgress.setIndeterminate(z);
        } else {
            this.mIndeterminate = z;
        }
    }
}
