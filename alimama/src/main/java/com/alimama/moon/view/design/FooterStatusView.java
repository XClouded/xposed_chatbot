package com.alimama.moon.view.design;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimama.moon.view.IFooterLoading;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

public class FooterStatusView extends FrameLayout implements IFooterLoading {
    private View mLoadingStatusView;
    private View mNoMoreStatusView;

    public FooterStatusView(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public FooterStatusView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FooterStatusView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        inflate(context, R.layout.merge_footer_status_view, this);
        this.mLoadingStatusView = findViewById(R.id.ll_loading_more_container);
        this.mNoMoreStatusView = findViewById(R.id.ll_no_more_container);
        ((SimpleDraweeView) findViewById(R.id.iv_loading_animated_view)).setController(((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true)).setUri(new Uri.Builder().scheme("res").path(String.valueOf(R.drawable.loading_animated)).build()).build());
        onLoadingMore();
    }

    public void onNoMoreItems() {
        this.mLoadingStatusView.setVisibility(8);
        this.mNoMoreStatusView.setVisibility(0);
    }

    public void onLoadingMore() {
        this.mLoadingStatusView.setVisibility(0);
        this.mNoMoreStatusView.setVisibility(8);
    }
}
