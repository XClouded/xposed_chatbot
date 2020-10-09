package com.alimama.union.app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;

public class ISViewContainer extends ViewGroup {
    private boolean mAutoLoading;
    private LinearLayout mButtonRefresh;
    /* access modifiers changed from: private */
    public IViewContainerRefreshListener mContainerRefrshListener;
    private View mContentView;
    private String mDataTag;
    private ImageView mFailedImageView;
    private boolean mHasLoaded;
    private View mLoadingLayout;
    private long mLoadingStartTime;
    private ImageView mLoadingView;
    private View mTagView;
    private TextView mTextDesView;
    private TextView mTextView;

    public interface IViewContainerRefreshListener {
        void refreshPage();
    }

    public ISViewContainer(Context context) {
        this(context, (AttributeSet) null);
    }

    public ISViewContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ISViewContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHasLoaded = false;
        this.mAutoLoading = true;
        init(context, attributeSet, i);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        this.mTagView = LayoutInflater.from(context).inflate(R.layout.is_views_status_view, (ViewGroup) null);
        this.mFailedImageView = (ImageView) this.mTagView.findViewById(R.id.is_status_failed_image_view);
        this.mTextView = (TextView) this.mTagView.findViewById(R.id.is_status_loading_text_view);
        this.mTextDesView = (TextView) this.mTagView.findViewById(R.id.is_status_loading_text_des_view);
        ((TextView) this.mTagView.findViewById(R.id.is_status_refresh_text_view)).getPaint().setFakeBoldText(true);
        this.mButtonRefresh = (LinearLayout) this.mTagView.findViewById(R.id.is_status_loading_btn_refresh);
        this.mButtonRefresh.setOnClickListener(new RefreshOnClickListener());
        this.mLoadingLayout = this.mTagView.findViewById(R.id.is_loading_layout);
        this.mLoadingView = (ImageView) this.mTagView.findViewById(R.id.is_status_loading_image_view);
        addView(this.mTagView, new ViewGroup.LayoutParams(-1, -1));
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ISViewContainer, i, 0);
        this.mAutoLoading = obtainStyledAttributes.getBoolean(0, true);
        obtainStyledAttributes.recycle();
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
        if (this.mAutoLoading) {
            startLoading();
            return;
        }
        this.mTagView.setVisibility(8);
        this.mLoadingView.clearAnimation();
        this.mLoadingLayout.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        try {
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

    private void startLoading() {
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
        setBackgroundColor(getResources().getColor(R.color.white));
        this.mLoadingLayout.setVisibility(0);
        this.mLoadingView.clearAnimation();
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        if (loadAnimation != null) {
            this.mLoadingView.startAnimation(loadAnimation);
        }
        this.mTextView.setVisibility(8);
        this.mTextDesView.setVisibility(8);
        this.mButtonRefresh.setVisibility(8);
        this.mFailedImageView.setVisibility(8);
    }

    public void onDataLoaded() {
        if (!this.mHasLoaded) {
            this.mHasLoaded = true;
            LoadingUIModel.getInstance().addStat(System.currentTimeMillis() - this.mLoadingStartTime);
            int currentTimeMillis = (int) (0 - (System.currentTimeMillis() - this.mLoadingStartTime));
            if (currentTimeMillis <= 0) {
                performDataLoaded();
            } else {
                postDelayed(new Runnable() {
                    public void run() {
                        ISViewContainer.this.performDataLoaded();
                    }
                }, (long) currentTimeMillis);
            }
        }
    }

    /* access modifiers changed from: private */
    public void performDataLoaded() {
        this.mTagView.setVisibility(8);
        this.mLoadingView.clearAnimation();
        this.mLoadingLayout.setVisibility(8);
        this.mContentView.setVisibility(0);
    }

    public void onEmptyData(String str) {
        displayMessage(str);
    }

    public void setDataTag(String str) {
        this.mDataTag = str;
    }

    public void displayMessage(String str) {
        this.mTextView.setText(str);
        this.mLoadingView.clearAnimation();
        this.mLoadingLayout.setVisibility(8);
    }

    public void onError() {
        this.mTextDesView.setVisibility(0);
        this.mLoadingView.clearAnimation();
        this.mLoadingLayout.setVisibility(8);
    }

    public void onDataLoadError() {
        setBackgroundColor(-1);
        this.mHasLoaded = false;
        this.mTagView.setVisibility(0);
        this.mContentView.setVisibility(8);
        this.mTextView.setText(getResources().getString(R.string.is_base_container_failed_title));
        this.mTextView.setVisibility(0);
        this.mTextDesView.setVisibility(0);
        this.mButtonRefresh.setVisibility(0);
        this.mFailedImageView.setImageResource(R.drawable.view_container_error_view_image);
        this.mFailedImageView.setVisibility(0);
        this.mLoadingView.clearAnimation();
        this.mLoadingLayout.setVisibility(8);
        onError();
    }

    public void onDataEmpty() {
        setBackgroundColor(-1);
        this.mHasLoaded = false;
        this.mTagView.setVisibility(0);
        this.mContentView.setVisibility(8);
        this.mTextView.setText(getResources().getString(R.string.tab_data_empty_title));
        this.mTextView.setVisibility(0);
        this.mTextDesView.setVisibility(8);
        this.mButtonRefresh.setVisibility(8);
        this.mFailedImageView.setImageResource(R.drawable.tab_data_empty);
        this.mFailedImageView.setVisibility(0);
        this.mLoadingView.clearAnimation();
        this.mLoadingLayout.setVisibility(8);
    }

    public void setContainerRefreshListener(IViewContainerRefreshListener iViewContainerRefreshListener) {
        this.mContainerRefrshListener = iViewContainerRefreshListener;
    }

    public class RefreshOnClickListener implements View.OnClickListener {
        public RefreshOnClickListener() {
        }

        public void onClick(View view) {
            if (ISViewContainer.this.mContainerRefrshListener != null) {
                ISViewContainer.this.mContainerRefrshListener.refreshPage();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mContainerRefrshListener = null;
    }
}
