package com.alimama.unionwl.uiframe.views.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alimama.unionwl.uiframe.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import in.srain.cube.ptr.PtrFrameLayout;
import in.srain.cube.ptr.PtrUIHandler;
import in.srain.cube.ptr.indicator.PtrIndicator;

public class ISPtrHeaderView extends FrameLayout implements PtrUIHandler {
    public static String sRefreshBeginTitle;
    public static String sRefreshPrepareTitle;
    private SimpleDraweeView animatedView;
    private LinearLayout linearLayout;
    private TextView mTitleTextView;

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout ptrFrameLayout) {
    }

    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout ptrFrameLayout) {
    }

    public ISPtrHeaderView(Context context) {
        super(context);
        initViews((AttributeSet) null);
    }

    public ISPtrHeaderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews(attributeSet);
    }

    public ISPtrHeaderView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(attributeSet);
    }

    /* access modifiers changed from: protected */
    public void initViews(AttributeSet attributeSet) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.is_views_ptr_header, this);
        this.linearLayout = (LinearLayout) inflate.findViewById(R.id.ll);
        this.animatedView = (SimpleDraweeView) inflate.findViewById(R.id.iv_loading_animated_view);
        this.mTitleTextView = (TextView) inflate.findViewById(R.id.refresh_tv);
    }

    public void updatePtrHeaderViewBgByIntColor(int i, int i2) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getResources().getColor(i), getResources().getColor(i2)});
        if (this.linearLayout != null) {
            this.linearLayout.setBackgroundDrawable(gradientDrawable);
        }
    }

    public void updatePtrHeaderViewBgByStringColor(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(str), Color.parseColor(str2)});
                if (this.linearLayout != null) {
                    this.linearLayout.setBackgroundDrawable(gradientDrawable);
                }
            } catch (Exception unused) {
            }
        }
    }

    public void updatePtrHeaderViewTextColor(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mTitleTextView.setTextColor(Color.parseColor(str));
        }
    }

    private void showAnimated() {
        this.animatedView.setVisibility(0);
        this.animatedView.setController(((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true)).setUri(new Uri.Builder().scheme("res").path(String.valueOf(R.drawable.loading_animated)).build()).build());
    }

    public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {
        showAnimated();
        this.mTitleTextView.setVisibility(0);
        if (sRefreshPrepareTitle != null) {
            this.mTitleTextView.setText(sRefreshPrepareTitle);
        } else {
            this.mTitleTextView.setText(R.string.head_view_refresh_prepare);
        }
    }

    public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        showAnimated();
        this.mTitleTextView.setVisibility(0);
        if (sRefreshBeginTitle != null) {
            this.mTitleTextView.setText(sRefreshBeginTitle);
        } else {
            this.mTitleTextView.setText(R.string.head_view_refresh_begin);
        }
    }

    public void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout) {
        this.mTitleTextView.setVisibility(0);
    }

    public void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean z, byte b, PtrIndicator ptrIndicator) {
        int offsetToRefresh = ptrFrameLayout.getOffsetToRefresh();
        int currentPosY = ptrIndicator.getCurrentPosY();
        int lastPosY = ptrIndicator.getLastPosY();
        if (currentPosY >= offsetToRefresh || lastPosY < offsetToRefresh) {
            if (currentPosY > offsetToRefresh && lastPosY <= offsetToRefresh && z && b == 2) {
                crossRotateLineFromTopUnderTouch(ptrFrameLayout);
            }
        } else if (z && b == 2) {
            crossRotateLineFromBottomUnderTouch(ptrFrameLayout);
        }
    }

    public void onUIReset(PtrFrameLayout ptrFrameLayout) {
        this.animatedView.setVisibility(8);
    }

    public static String getRefreshPrepareTitle() {
        return sRefreshPrepareTitle;
    }

    public static void setRefreshPrepareTitle(String str) {
        sRefreshPrepareTitle = str;
    }

    public static String getRefreshBeginTitle() {
        return sRefreshBeginTitle;
    }

    public static void setRefreshBeginTitle(String str) {
        sRefreshBeginTitle = str;
    }
}
