package com.alimama.union.app.share;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.R;

public class SharePanel extends LinearLayout {
    public SharePanel(Context context) {
        this(context, (AttributeSet) null);
    }

    public SharePanel(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SharePanel(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        inflate(context, R.layout.share_panel, this);
    }
}
