package com.alimama.union.app.share;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;

public class TextShareView extends ScrollView implements View.OnClickListener {
    private static final String TAG = "TextShareView";
    @Nullable
    private View.OnClickListener mOnCopyTextClickListener;
    private EditText mSharedInfoEditText;

    public TextShareView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TextShareView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TextShareView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        inflate(context, R.layout.fragment_text_share, this);
        this.mSharedInfoEditText = (EditText) findViewById(R.id.edt_share_detail);
        this.mSharedInfoEditText.setOnClickListener(this);
        findViewById(R.id.btn_copy_text).setOnClickListener(this);
    }

    public void setSharedText(@Nullable String str) {
        this.mSharedInfoEditText.setText(str);
    }

    public CharSequence getSharedEditText() {
        return this.mSharedInfoEditText.getText();
    }

    public void setOnCopyClickListener(@Nullable View.OnClickListener onClickListener) {
        this.mOnCopyTextClickListener = onClickListener;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_share_detail:
                this.mSharedInfoEditText.setCursorVisible(true);
                return;
            case R.id.btn_copy_text:
                UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_SHARE_COPY_MSG);
                if (this.mOnCopyTextClickListener != null) {
                    this.mOnCopyTextClickListener.onClick(view);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
