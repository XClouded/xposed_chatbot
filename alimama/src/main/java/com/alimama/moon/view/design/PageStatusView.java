package com.alimama.moon.view.design;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import com.alimama.moon.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class PageStatusView extends FrameLayout {
    private static final String TAG = "PageStatusView";
    private View mContentView;
    private int mContentViewResId;
    private SimpleDraweeView mLoadingAnimatedView;
    @Nullable
    private View.OnClickListener mRefreshClickedListener;
    private Button mRetryAction;
    private ImageView mStatusImage;
    private TextView mStatusMessage;
    private TextView mStatusTitle;

    public PageStatusView(Context context) {
        this(context, (AttributeSet) null);
    }

    public PageStatusView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PageStatusView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContentViewResId = -1;
        initViews(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (this.mContentViewResId != -1) {
            try {
                this.mContentView = findViewById(this.mContentViewResId);
            } catch (Exception unused) {
                Log.e(TAG, "onFinishInflate failed to retrieve view by id " + this.mContentViewResId);
            }
        }
    }

    public void setRefreshListener(View.OnClickListener onClickListener) {
        this.mRefreshClickedListener = onClickListener;
        this.mRetryAction.setOnClickListener(this.mRefreshClickedListener);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0092  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initViews(@androidx.annotation.NonNull android.content.Context r4, @androidx.annotation.Nullable android.util.AttributeSet r5) {
        /*
            r3 = this;
            r0 = 2130903256(0x7f0300d8, float:1.7413325E38)
            inflate(r4, r0, r3)
            r4 = 2131695321(0x7f0f16d9, float:1.9019824E38)
            android.view.View r4 = r3.findViewById(r4)
            com.facebook.drawee.view.SimpleDraweeView r4 = (com.facebook.drawee.view.SimpleDraweeView) r4
            r3.mLoadingAnimatedView = r4
            r4 = 2131695420(0x7f0f173c, float:1.9020024E38)
            android.view.View r4 = r3.findViewById(r4)
            android.widget.ImageView r4 = (android.widget.ImageView) r4
            r3.mStatusImage = r4
            r4 = 2131695421(0x7f0f173d, float:1.9020026E38)
            android.view.View r4 = r3.findViewById(r4)
            android.widget.TextView r4 = (android.widget.TextView) r4
            r3.mStatusTitle = r4
            r4 = 2131695422(0x7f0f173e, float:1.9020028E38)
            android.view.View r4 = r3.findViewById(r4)
            android.widget.TextView r4 = (android.widget.TextView) r4
            r3.mStatusMessage = r4
            r4 = 2131695423(0x7f0f173f, float:1.902003E38)
            android.view.View r4 = r3.findViewById(r4)
            android.widget.Button r4 = (android.widget.Button) r4
            r3.mRetryAction = r4
            android.net.Uri$Builder r4 = new android.net.Uri$Builder
            r4.<init>()
            java.lang.String r0 = "res"
            android.net.Uri$Builder r4 = r4.scheme(r0)
            r0 = 2130837841(0x7f020151, float:1.7280647E38)
            java.lang.String r0 = java.lang.String.valueOf(r0)
            android.net.Uri$Builder r4 = r4.path(r0)
            android.net.Uri r4 = r4.build()
            com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder r0 = com.facebook.drawee.backends.pipeline.Fresco.newDraweeControllerBuilder()
            r1 = 1
            com.facebook.drawee.controller.AbstractDraweeControllerBuilder r0 = r0.setAutoPlayAnimations(r1)
            com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder r0 = (com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder) r0
            com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder r4 = r0.setUri((android.net.Uri) r4)
            com.facebook.drawee.controller.AbstractDraweeController r4 = r4.build()
            com.facebook.drawee.view.SimpleDraweeView r0 = r3.mLoadingAnimatedView
            r0.setController(r4)
            if (r5 == 0) goto L_0x0096
            r4 = 0
            android.content.Context r0 = r3.getContext()     // Catch:{ all -> 0x008f }
            int[] r1 = com.alimama.moon.R.styleable.PageStatusView     // Catch:{ all -> 0x008f }
            android.content.res.TypedArray r5 = r0.obtainStyledAttributes(r5, r1)     // Catch:{ all -> 0x008f }
            r4 = 0
            r0 = -1
            int r4 = r5.getResourceId(r4, r0)     // Catch:{ all -> 0x008a }
            r3.mContentViewResId = r4     // Catch:{ all -> 0x008a }
            if (r5 == 0) goto L_0x0096
            r5.recycle()
            goto L_0x0096
        L_0x008a:
            r4 = move-exception
            r2 = r5
            r5 = r4
            r4 = r2
            goto L_0x0090
        L_0x008f:
            r5 = move-exception
        L_0x0090:
            if (r4 == 0) goto L_0x0095
            r4.recycle()
        L_0x0095:
            throw r5
        L_0x0096:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.view.design.PageStatusView.initViews(android.content.Context, android.util.AttributeSet):void");
    }

    public void onLoading() {
        this.mLoadingAnimatedView.setVisibility(0);
        this.mStatusMessage.setVisibility(0);
        this.mStatusMessage.setText(R.string.loading_more_text_info);
        this.mStatusImage.setVisibility(8);
        this.mStatusTitle.setVisibility(8);
        this.mRetryAction.setVisibility(8);
        if (this.mContentView != null) {
            this.mContentView.setVisibility(8);
        }
    }

    public void onEmptyData(@StringRes int i) {
        onEmptyData(i, 0);
    }

    public void onEmptyData(@StringRes int i, @StringRes int i2) {
        this.mStatusImage.setVisibility(0);
        this.mStatusImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.empty_data_status));
        this.mStatusTitle.setVisibility(0);
        this.mStatusTitle.setText(i);
        if (i2 != 0) {
            this.mStatusMessage.setVisibility(0);
            this.mStatusMessage.setText(i2);
        } else {
            this.mStatusMessage.setVisibility(8);
        }
        this.mRetryAction.setVisibility(8);
        this.mLoadingAnimatedView.setVisibility(8);
        if (this.mContentView != null) {
            this.mContentView.setVisibility(8);
        }
    }

    public void onServerError() {
        this.mStatusImage.setVisibility(0);
        this.mStatusImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.system_error_status));
        this.mStatusTitle.setVisibility(0);
        this.mStatusTitle.setText(R.string.server_error_title);
        this.mStatusMessage.setText(R.string.server_error_message);
        this.mRetryAction.setVisibility(0);
        this.mLoadingAnimatedView.setVisibility(8);
        if (this.mContentView != null) {
            this.mContentView.setVisibility(8);
        }
    }

    public void onNetworkError() {
        this.mStatusImage.setVisibility(0);
        this.mStatusImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.network_error_status));
        this.mStatusTitle.setVisibility(0);
        this.mStatusTitle.setText(R.string.network_error_title);
        this.mStatusMessage.setText(R.string.network_error_message);
        this.mRetryAction.setVisibility(0);
        this.mLoadingAnimatedView.setVisibility(8);
        if (this.mContentView != null) {
            this.mContentView.setVisibility(8);
        }
    }

    public void onContentLoaded() {
        if (this.mContentView != null) {
            this.mContentView.setVisibility(0);
        }
        this.mLoadingAnimatedView.setVisibility(8);
        this.mStatusImage.setVisibility(8);
        this.mStatusTitle.setVisibility(8);
        this.mStatusMessage.setVisibility(8);
        this.mRetryAction.setVisibility(8);
    }
}
