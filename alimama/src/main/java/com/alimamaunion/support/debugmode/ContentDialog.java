package com.alimamaunion.support.debugmode;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

public class ContentDialog extends Dialog implements View.OnClickListener {
    private LinearLayout mContentContainer;
    private TextView mCopy;
    private TextView mDismiss;
    private TextView mTitle;

    public void addContent(String str) {
    }

    public ContentDialog(@NonNull Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onClick(View view) {
        dismiss();
    }
}
