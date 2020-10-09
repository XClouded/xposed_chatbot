package com.alibaba.aliweex.bundle;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import com.alibaba.aliweex.R;

public class WXErrorController {
    private Context mContext;
    private TextView mErrorText;
    private View mErrorView;
    /* access modifiers changed from: private */
    public View.OnClickListener mOnClickListener;

    public WXErrorController(Context context, View view) {
        this.mContext = context;
        this.mErrorView = ((ViewStub) view.findViewById(R.id.wx_fragment_error)).inflate();
        this.mErrorText = (TextView) view.findViewById(R.id.wa_common_error_text);
    }

    private void setErrorText(String str) {
        if (this.mErrorText != null && str != null) {
            this.mErrorText.setText(str);
        }
    }

    public void setRetryListener(View.OnClickListener onClickListener) {
        if (this.mErrorView != null) {
            this.mOnClickListener = onClickListener;
            this.mErrorView.setClickable(true);
            this.mErrorView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (WXErrorController.this.mOnClickListener != null) {
                        WXErrorController.this.mOnClickListener.onClick(view);
                    }
                }
            });
        }
    }

    public void show() {
        show((String) null);
    }

    public void show(String str) {
        if (this.mErrorView != null && this.mContext != null) {
            if (TextUtils.isEmpty(str)) {
                str = getResourcesString(R.string.weex_common_error_data);
            }
            setErrorText(str);
            this.mErrorView.setVisibility(0);
        }
    }

    private String getResourcesString(int i) {
        return this.mContext.getResources().getString(i);
    }

    public void hide() {
        if (this.mErrorView != null) {
            this.mErrorView.setVisibility(8);
        }
    }

    public void destroy() {
        this.mErrorView = null;
    }
}
