package com.alimama.union.app.share;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.R;

public class ShareCommissionInfoView extends LinearLayout {
    private TextView mRebateInfo;

    public ShareCommissionInfoView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ShareCommissionInfoView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ShareCommissionInfoView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        inflate(context, R.layout.include_rebate_info, this);
        this.mRebateInfo = (TextView) findViewById(R.id.tv_rebate_info);
    }

    public void setCommissionInfo(String str) {
        this.mRebateInfo.setText(str);
    }
}
