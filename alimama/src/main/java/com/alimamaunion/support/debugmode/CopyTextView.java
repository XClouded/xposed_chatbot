package com.alimamaunion.support.debugmode;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class CopyTextView extends LinearLayout implements View.OnClickListener {
    private TextView mCopy;
    private TextView mTextContent;

    private void initView() {
    }

    public void onClick(View view) {
    }

    public void setContent(String str) {
    }

    public CopyTextView(Context context) {
        super(context);
        initView();
    }

    public CopyTextView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public CopyTextView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }
}
