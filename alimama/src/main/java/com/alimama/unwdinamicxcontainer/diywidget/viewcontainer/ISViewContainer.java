package com.alimama.unwdinamicxcontainer.diywidget.viewcontainer;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.unwdinamicxcontainer.R;
import com.alimama.unwdinamicxcontainer.diywidget.viewcontainer.RotationAnimation;
import com.alimama.unwdinamicxcontainer.presenter.dxcengine.IDXCErrorViewListener;

public class ISViewContainer extends ViewGroup {
    private LinearLayout mButtonRefresh;
    private View mContentView;
    private String mDataTag;
    private TextView mEmptyAction;
    /* access modifiers changed from: private */
    public IDXCErrorViewListener mErrorViewListener;
    private ImageView mFailedImageView;
    private boolean mHasLoaded = false;
    private ImageView mImageView;
    private ISLoadingDrawable mLoadingDrawable;
    private long mLoadingStartTime;
    private TextView mSecondTextView;
    private View mTagView;
    private TextView mTextView;

    public ISViewContainer(Context context) {
        super(context);
        init();
    }

    public ISViewContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ISViewContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.mTagView = LayoutInflater.from(getContext()).inflate(R.layout.is_view_container_layout, (ViewGroup) null);
        this.mImageView = (ImageView) this.mTagView.findViewById(R.id.is_status_loading_image_view);
        this.mFailedImageView = (ImageView) this.mTagView.findViewById(R.id.is_status_failed_image_view);
        this.mTextView = (TextView) this.mTagView.findViewById(R.id.is_status_loading_text_view);
        this.mSecondTextView = (TextView) this.mTagView.findViewById(R.id.is_second_text_view);
        this.mEmptyAction = (TextView) this.mTagView.findViewById(R.id.empty_action);
        ((TextView) this.mTagView.findViewById(R.id.is_status_refresh_text_view)).getPaint().setFakeBoldText(true);
        this.mButtonRefresh = (LinearLayout) this.mTagView.findViewById(R.id.is_status_loading_btn_refresh);
        this.mButtonRefresh.setOnClickListener(new RefreshOnClickListener(this));
        this.mLoadingDrawable = new ISLoadingDrawable(getContext(), this.mImageView);
        this.mLoadingDrawable.setColor(getResources().getColor(R.color.red_ff0033));
        addView(this.mTagView, new ViewGroup.LayoutParams(-1, -1));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = this.mTagView.getMeasuredWidth();
        int measuredHeight = this.mTagView.getMeasuredHeight();
        int measuredWidth2 = (getMeasuredWidth() - measuredWidth) / 2;
        int measuredHeight2 = (getMeasuredHeight() - measuredHeight) / 2;
        this.mTagView.layout(measuredWidth2, measuredHeight2, measuredWidth + measuredWidth2, measuredHeight + measuredHeight2);
        this.mContentView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContentView = getChildAt(0);
        if (this.mContentView == this.mTagView) {
            this.mContentView = getChildAt(1);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        try {
            setMeasuredDimension(i, i2);
            if (this.mContentView instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) this.mContentView;
                if (recyclerView.getLayoutManager() == null) {
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                }
            }
            measureChild(this.mTagView, i, i2);
            measureChild(this.mContentView, i, i2);
            super.onMeasure(i, i2);
        } catch (Exception unused) {
        }
    }

    public void startLoading() {
        this.mHasLoaded = false;
        this.mLoadingStartTime = System.currentTimeMillis();
        setBackgroundColor(-1);
        this.mContentView.setVisibility(4);
        this.mTagView.setVisibility(0);
        if (LoadingUIModel.getInstance().shouldShowLoading()) {
            showLoading();
        }
    }

    public void showLoading() {
        setBackgroundColor(getResources().getColor(R.color.white_ffffff));
        this.mImageView.setImageDrawable(this.mLoadingDrawable);
        this.mLoadingDrawable.start();
        this.mTextView.setText("努力加载中，请耐心等待");
        this.mButtonRefresh.setVisibility(8);
        this.mFailedImageView.setVisibility(8);
    }

    public void onDataLoaded() {
        if (!this.mHasLoaded) {
            this.mHasLoaded = true;
            LoadingUIModel.getInstance().addStat(System.currentTimeMillis() - this.mLoadingStartTime);
            performDataLoaded();
        }
    }

    private void performDataLoaded() {
        setBackgroundColor(getResources().getColor(R.color.gray_f5f5f5));
        this.mTagView.setVisibility(8);
        this.mLoadingDrawable.stop();
        this.mContentView.setVisibility(0);
    }

    public void onEmptyData(String str) {
        displayMessage(str);
    }

    public void setDataTag(String str) {
        this.mDataTag = str;
    }

    public void displayMessage(String str) {
        this.mLoadingDrawable.reset();
        this.mImageView.setVisibility(8);
        this.mTextView.setText(str);
    }

    public void onError(String str) {
        this.mLoadingDrawable.reset();
        this.mImageView.setVisibility(8);
        this.mTextView.setText(str);
    }

    public void onDataLoadError(String str) {
        setBackgroundColor(-1);
        this.mHasLoaded = false;
        this.mTagView.setVisibility(0);
        this.mContentView.setVisibility(8);
        this.mTextView.setVisibility(0);
        this.mButtonRefresh.setVisibility(0);
        this.mFailedImageView.setVisibility(0);
        onError(str);
    }

    public void onNetworkError(String str, int i) {
        onDataLoadError(str);
        this.mFailedImageView.setImageResource(i);
        this.mButtonRefresh.setVisibility(0);
    }

    public void onDataEmpty(String str, int i) {
        onDataLoadError(str);
        this.mFailedImageView.setImageResource(i);
        this.mButtonRefresh.setVisibility(8);
    }

    public void setErrorViewListener(IDXCErrorViewListener iDXCErrorViewListener) {
        this.mErrorViewListener = iDXCErrorViewListener;
    }

    public void onDataEmpty(String str, String str2, int i, View.OnClickListener onClickListener) {
        onDataEmpty(str, i);
        this.mTextView.setTextColor(getResources().getColor(R.color.black_333333));
        if (!TextUtils.isEmpty(str2)) {
            this.mSecondTextView.setVisibility(0);
            this.mSecondTextView.setText(str2);
            this.mEmptyAction.setVisibility(0);
            this.mEmptyAction.setOnClickListener(onClickListener);
            return;
        }
        this.mSecondTextView.setVisibility(8);
        this.mEmptyAction.setVisibility(8);
    }

    public static class RefreshOnClickListener implements View.OnClickListener {
        private ISViewContainer mContainer;

        public RefreshOnClickListener(ISViewContainer iSViewContainer) {
            this.mContainer = iSViewContainer;
        }

        public void onClick(View view) {
            if (view.getId() == R.id.is_status_loading_btn_refresh) {
                ImageView imageView = (ImageView) view.findViewById(R.id.is_status_refresh_image_view);
                RotationAnimation rotationAnimation = new RotationAnimation(imageView, RotationAnimation.RotationOrientation.UNCLOCKWIS);
                rotationAnimation.setRepeatCount(2);
                imageView.startAnimation(rotationAnimation);
            }
            if (this.mContainer.mErrorViewListener != null) {
                this.mContainer.mErrorViewListener.clickRefreshBtn();
            }
        }
    }
}
