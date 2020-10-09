package com.alimama.unionwl.uiframe.views.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.unionwl.uiframe.R;
import com.alimama.unionwl.utils.CommonUtils;
import in.srain.cube.ptr.util.RotationAnimation;

public class ISViewContainer extends ViewGroup implements View.OnClickListener {
    private LinearLayout mButtonRefresh;
    private View mContentView;
    private String mDataTag;
    private ImageView mFailedImageView;
    private boolean mHasLoaded = false;
    private ImageView mImageView;
    private RefreshListener mListener;
    private ISLoadingDrawable mLoadingDrawable;
    private long mLoadingStartTime;
    private View mTagView;
    private TextView mTextView;

    public interface RefreshListener {
        void onRefresh();
    }

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
        this.mTagView = LayoutInflater.from(getContext()).inflate(R.layout.is_views_status_view, (ViewGroup) null);
        this.mImageView = (ImageView) this.mTagView.findViewById(R.id.is_status_loading_image_view);
        this.mFailedImageView = (ImageView) this.mTagView.findViewById(R.id.is_status_failed_image_view);
        this.mTextView = (TextView) this.mTagView.findViewById(R.id.is_status_loading_text_view);
        ((TextView) this.mTagView.findViewById(R.id.is_status_refresh_text_view)).getPaint().setFakeBoldText(true);
        this.mButtonRefresh = (LinearLayout) this.mTagView.findViewById(R.id.is_status_loading_btn_refresh);
        this.mButtonRefresh.setOnClickListener(this);
        this.mLoadingDrawable = new ISLoadingDrawable(getContext(), this.mImageView);
        this.mLoadingDrawable.setColor(getResources().getColor(R.color.is_main_color));
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

    public void setRefreshLisenter(RefreshListener refreshListener) {
        this.mListener = refreshListener;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContentView = getChildAt(0);
        if (this.mContentView == this.mTagView) {
            this.mContentView = getChildAt(1);
        }
        startLoading();
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
        setBackgroundColor(getResources().getColor(R.color.is_white));
        this.mImageView.setImageDrawable(this.mLoadingDrawable);
        this.mLoadingDrawable.start();
        this.mTextView.setText(getResources().getString(R.string.is_container_loading));
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
        setBackgroundColor(-1118482);
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

    public void onDataEmpty(String str, int i) {
        onDataLoadError(str);
        this.mFailedImageView.setImageResource(i);
        this.mButtonRefresh.setVisibility(8);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.is_status_loading_btn_refresh) {
            ImageView imageView = (ImageView) view.findViewById(R.id.is_status_refresh_image_view);
            RotationAnimation rotationAnimation = new RotationAnimation(imageView, RotationAnimation.RotationOrientation.UNCLOCKWIS);
            if (CommonUtils.isNetworkAvailable(view.getContext())) {
                rotationAnimation.setRepeatCount(15);
            } else {
                rotationAnimation.setRepeatCount(2);
            }
            imageView.startAnimation(rotationAnimation);
        }
        if (this.mListener != null) {
            this.mListener.onRefresh();
        }
    }
}
