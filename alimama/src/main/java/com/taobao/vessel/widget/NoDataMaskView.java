package com.taobao.vessel.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.taobao.android.vesselview.R;
import com.taobao.uikit.feature.features.internal.pullrefresh.CustomProgressBar;

public class NoDataMaskView extends RelativeLayout {
    private CustomProgressBar mProgressBar;
    private TextView mTextViewErrorTips;

    public NoDataMaskView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NoDataMaskView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NoDataMaskView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void loadView() {
        View inflate = View.inflate(getContext(), R.layout.mask_view_no_data, this);
        if (inflate != null) {
            this.mProgressBar = (CustomProgressBar) inflate.findViewById(R.id.mask_view_no_data_progressbar);
            this.mTextViewErrorTips = (TextView) inflate.findViewById(R.id.mask_view_no_data_error_tips);
        }
    }

    private void init() {
        loadView();
    }

    public void startLoading() {
        if (getVisibility() == 8) {
            setVisibility(0);
        }
        if (this.mProgressBar != null) {
            this.mProgressBar.startLoadingAnimaton();
        }
        setErrorTextVisible(false, (String) null);
    }

    public void finish() {
        setProgressBarVisible(false);
        setErrorTextVisible(true, "");
        setVisibility(8);
    }

    public void setProgressBarVisible(boolean z) {
        if (z) {
            this.mProgressBar.startLoadingAnimaton();
        } else {
            this.mProgressBar.stopLoadingAnimation();
        }
        this.mProgressBar.isInitShow(z);
    }

    public void setErrorTextVisible(boolean z, String str) {
        if (getVisibility() == 8) {
            setVisibility(0);
        }
        if (z) {
            if (TextUtils.isEmpty(str)) {
                str = getResources().getString(R.string.error_network_retry);
            }
            this.mTextViewErrorTips.setText(str);
            this.mTextViewErrorTips.setVisibility(0);
            return;
        }
        this.mTextViewErrorTips.setVisibility(8);
    }

    public boolean isRetry() {
        return this.mTextViewErrorTips.getVisibility() == 0;
    }
}
