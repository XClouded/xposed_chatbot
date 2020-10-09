package com.taobao.uikit.extend.component.refresh;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.component.refresh.TBLoadMoreFooter;
import com.taobao.uikit.feature.features.internal.pullrefresh.CustomProgressBar;

public class TBDefaultLoadMoreFooter extends TBLoadMoreFooter {
    private String[] mDefaultLoadMoreTips = {getContext().getString(R.string.uik_load_more), getContext().getString(R.string.uik_release_to_load), getContext().getString(R.string.uik_loading), getContext().getString(R.string.uik_load_more_finished)};
    private String[] mLoadMoreTips = {getContext().getString(R.string.uik_load_more), getContext().getString(R.string.uik_release_to_load), getContext().getString(R.string.uik_loading), getContext().getString(R.string.uik_load_more_finished)};
    private TextView mLoadMoreView;
    private CustomProgressBar mProgressbar;
    private TBLoadMoreFooter.LoadMoreState mState = TBLoadMoreFooter.LoadMoreState.NONE;

    public void setProgress(float f) {
    }

    public TBDefaultLoadMoreFooter(Context context) {
        super(context);
        float f = getResources().getDisplayMetrics().density;
        LinearLayout linearLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        linearLayout.setGravity(1);
        int i = (int) (12.0f * f);
        linearLayout.setPadding(0, i, 0, 0);
        addView(linearLayout, layoutParams);
        this.mProgressbar = new CustomProgressBar(context);
        this.mProgressbar.setId(R.id.uik_load_more_footer_progress);
        int i2 = (int) (f * 28.0f);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(i2, i2);
        layoutParams2.rightMargin = i;
        this.mProgressbar.setVisibility(8);
        linearLayout.addView(this.mProgressbar, layoutParams2);
        this.mLoadMoreView = new TextView(context);
        this.mLoadMoreView.setId(R.id.uik_load_more_footer_text);
        this.mLoadMoreView.setText(R.string.uik_load_more);
        this.mLoadMoreView.setTextSize(1, 10.0f);
        this.mLoadMoreView.setGravity(16);
        this.mLoadMoreView.setTextColor(Color.parseColor("#444444"));
        linearLayout.addView(this.mLoadMoreView, new LinearLayout.LayoutParams(-2, i2));
        setBackgroundResource(R.color.uik_load_more_footer_bg);
        changeToState(TBLoadMoreFooter.LoadMoreState.NONE);
    }

    public void changeToState(TBLoadMoreFooter.LoadMoreState loadMoreState) {
        if (this.mLoadMoreView != null && this.mState != loadMoreState) {
            if (this.mPushLoadMoreListener != null) {
                this.mPushLoadMoreListener.onLoadMoreStateChanged(this.mState, loadMoreState);
            }
            this.mState = loadMoreState;
            switch (this.mState) {
                case NONE:
                    this.mProgressbar.stopLoadingAnimation();
                    this.mProgressbar.setVisibility(8);
                    this.mLoadMoreView.setText(this.mLoadMoreTips == null ? this.mDefaultLoadMoreTips[3] : this.mLoadMoreTips[3]);
                    return;
                case PUSH_TO_LOAD:
                    this.mProgressbar.stopLoadingAnimation();
                    this.mProgressbar.setVisibility(8);
                    this.mLoadMoreView.setText(this.mLoadMoreTips == null ? this.mDefaultLoadMoreTips[0] : this.mLoadMoreTips[0]);
                    setVisibility(0);
                    return;
                case RELEASE_TO_LOAD:
                    this.mProgressbar.stopLoadingAnimation();
                    this.mProgressbar.setVisibility(8);
                    this.mLoadMoreView.setText(this.mLoadMoreTips == null ? this.mDefaultLoadMoreTips[1] : this.mLoadMoreTips[1]);
                    setVisibility(0);
                    return;
                case LOADING:
                    this.mProgressbar.setVisibility(0);
                    this.mProgressbar.startLoadingAnimaton();
                    this.mLoadMoreView.setText(this.mLoadMoreTips == null ? this.mDefaultLoadMoreTips[2] : this.mLoadMoreTips[2]);
                    setVisibility(0);
                    return;
                default:
                    return;
            }
        }
    }

    public void setLoadMoreTips(String[] strArr) {
        if (strArr == null || strArr.length != 4) {
            this.mLoadMoreTips = null;
        }
        this.mLoadMoreTips = strArr;
    }

    public void setLoadMoreTipColor(@ColorInt int i) {
        if (this.mLoadMoreView != null) {
            this.mLoadMoreView.setTextColor(i);
        }
    }

    public TBLoadMoreFooter.LoadMoreState getCurrentState() {
        return this.mState;
    }

    public TextView getLoadMoreTipView() {
        return this.mLoadMoreView;
    }
}
