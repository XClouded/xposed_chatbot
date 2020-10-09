package com.alimama.union.app.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.alimama.moon.R;

public class AlertMsgDialog extends Dialog {
    /* access modifiers changed from: private */
    @Nullable
    public DialogInterface.OnClickListener mActionClickListener;
    private TextView mContent;
    private Button mNegativeActionButton;
    private Button mPositiveActionButton;
    private TextView mTitle;

    public AlertMsgDialog(Context context) {
        super(context, R.style.common_dialog_style);
        initViews();
    }

    public AlertMsgDialog title(@StringRes int i) {
        this.mTitle.setText(i);
        return this;
    }

    public AlertMsgDialog content(@StringRes int i) {
        this.mContent.setText(i);
        return this;
    }

    public AlertMsgDialog content(String str) {
        this.mContent.setText(str);
        return this;
    }

    public AlertMsgDialog clickListener(DialogInterface.OnClickListener onClickListener) {
        this.mActionClickListener = onClickListener;
        return this;
    }

    public AlertMsgDialog positiveButtonText(@StringRes int i) {
        this.mPositiveActionButton.setText(i);
        this.mPositiveActionButton.setVisibility(0);
        return this;
    }

    public AlertMsgDialog negativeButtonText(@StringRes int i) {
        this.mNegativeActionButton.setText(i);
        this.mNegativeActionButton.setVisibility(0);
        return this;
    }

    public AlertMsgDialog canceledOnClickOutside(boolean z) {
        setCanceledOnTouchOutside(z);
        return this;
    }

    private void initViews() {
        setContentView(R.layout.dialog_alert_msg);
        this.mTitle = (TextView) findViewById(R.id.tv_title);
        this.mContent = (TextView) findViewById(R.id.tv_content);
        this.mPositiveActionButton = (Button) findViewById(R.id.btn_positive);
        this.mNegativeActionButton = (Button) findViewById(R.id.btn_negative);
        this.mPositiveActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AlertMsgDialog.this.mActionClickListener != null) {
                    AlertMsgDialog.this.mActionClickListener.onClick(AlertMsgDialog.this, -1);
                } else {
                    AlertMsgDialog.this.dismiss();
                }
            }
        });
        this.mNegativeActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AlertMsgDialog.this.mActionClickListener != null) {
                    AlertMsgDialog.this.mActionClickListener.onClick(AlertMsgDialog.this, -2);
                } else {
                    AlertMsgDialog.this.dismiss();
                }
            }
        });
    }
}
