package com.taobao.weex.analyzer.core.weex.v2;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.weex.v2.PerformanceV2Repository;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.PermissionOverlayView;

public class WXPerformanceV2OverlayView extends PermissionOverlayView implements PerformanceV2Repository.OnDataChangedListener, View.OnClickListener {
    private static final int COLOR_SELECTED = Color.parseColor("#bccddc39");
    private static final int COLOR_UN_SELECTED = Color.parseColor("#00ffffff");
    private View btnInteraction;
    private View btnIssue;
    private View btnOutline;
    private View btnStages;
    private View btnStats;
    /* access modifiers changed from: private */
    public TextView collapse;
    /* access modifiers changed from: private */
    public View container;
    int i = 0;
    /* access modifiers changed from: private */
    public boolean isCollapsed = true;
    /* access modifiers changed from: private */
    public volatile PerformanceV2Repository.APMInfo mApmInfo;
    /* access modifiers changed from: private */
    public DisplayInteractionView mDisplayInteractionView;
    /* access modifiers changed from: private */
    public DisplayIssueView mDisplayIssueView;
    /* access modifiers changed from: private */
    public DisplayOulineView mDisplayOulineView;
    /* access modifiers changed from: private */
    public DisplayStagesView mDisplayStagesView;
    /* access modifiers changed from: private */
    public DisplayStatsView mDisplayStatsView;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public String mInstanceId;
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    private PerformanceV2Repository mRepository;

    public boolean isPermissionGranted(@NonNull Config config) {
        return true;
    }

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public WXPerformanceV2OverlayView(Context context, PerformanceV2Repository performanceV2Repository) {
        super(context);
        this.mWidth = -1;
        this.mRepository = performanceV2Repository;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        View inflate = View.inflate(this.mContext, R.layout.wxt_performance_v2_view, (ViewGroup) null);
        this.mDisplayStagesView = (DisplayStagesView) inflate.findViewById(R.id.display_stages);
        this.mDisplayOulineView = (DisplayOulineView) inflate.findViewById(R.id.display_outline);
        this.mDisplayStatsView = (DisplayStatsView) inflate.findViewById(R.id.display_stats);
        this.mDisplayInteractionView = (DisplayInteractionView) inflate.findViewById(R.id.display_interaction);
        this.mDisplayIssueView = (DisplayIssueView) inflate.findViewById(R.id.display_issue);
        this.container = inflate.findViewById(R.id.container);
        this.collapse = (TextView) inflate.findViewById(R.id.collapse);
        this.collapse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (WXPerformanceV2OverlayView.this.isCollapsed) {
                    boolean unused = WXPerformanceV2OverlayView.this.isCollapsed = false;
                    WXPerformanceV2OverlayView.this.collapse.setText("展开");
                    WXPerformanceV2OverlayView.this.container.setVisibility(8);
                    return;
                }
                boolean unused2 = WXPerformanceV2OverlayView.this.isCollapsed = true;
                WXPerformanceV2OverlayView.this.collapse.setText("收起");
                WXPerformanceV2OverlayView.this.container.setVisibility(0);
            }
        });
        this.btnOutline = inflate.findViewById(R.id.btn_display_outline);
        this.btnStages = inflate.findViewById(R.id.btn_display_stages);
        this.btnStats = inflate.findViewById(R.id.btn_display_stats);
        this.btnInteraction = inflate.findViewById(R.id.btn_display_interaction);
        this.btnIssue = inflate.findViewById(R.id.btn_display_issue);
        this.btnOutline.setOnClickListener(this);
        this.btnStages.setOnClickListener(this);
        this.btnStats.setOnClickListener(this);
        this.btnInteraction.setOnClickListener(this);
        this.btnIssue.setOnClickListener(this);
        inflate.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (WXPerformanceV2OverlayView.this.isViewAttached) {
                    if (WXPerformanceV2OverlayView.this.mOnCloseListener != null) {
                        WXPerformanceV2OverlayView.this.mOnCloseListener.close(WXPerformanceV2OverlayView.this);
                    }
                    WXPerformanceV2OverlayView.this.dismiss();
                }
            }
        });
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        if (this.mRepository != null && !TextUtils.isEmpty(this.mInstanceId)) {
            this.mRepository.subscribe(this.mInstanceId, this);
        }
    }

    public void bindInstance(@Nullable WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance != null) {
            this.mInstanceId = wXSDKInstance.getInstanceId();
            if (!TextUtils.isEmpty(this.mInstanceId) && this.mRepository != null) {
                this.mRepository.subscribe(this.mInstanceId, this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mRepository != null && !TextUtils.isEmpty(this.mInstanceId)) {
            this.mRepository.unSubscribe(this.mInstanceId);
        }
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mDisplayInteractionView.onDetach();
    }

    public void onDataChanged(@Nullable final String str, @NonNull final PerformanceV2Repository.APMInfo aPMInfo) {
        this.mHandler.post(new Runnable() {
            public void run() {
                PerformanceV2Repository.APMInfo unused = WXPerformanceV2OverlayView.this.mApmInfo = aPMInfo;
                if (WXPerformanceV2OverlayView.this.isViewAttached) {
                    if (str == null) {
                        WXPerformanceV2OverlayView.this.mDisplayStagesView.render(aPMInfo);
                        WXPerformanceV2OverlayView.this.mDisplayOulineView.render(aPMInfo);
                        WXPerformanceV2OverlayView.this.mDisplayStatsView.render(aPMInfo);
                        WXPerformanceV2OverlayView.this.mDisplayInteractionView.render(aPMInfo, WXPerformanceV2OverlayView.this.mInstanceId);
                    } else if ("stage".equals(str)) {
                        WXPerformanceV2OverlayView.this.mDisplayStagesView.render(aPMInfo);
                    } else if ("wxinteraction".equals(str)) {
                        WXPerformanceV2OverlayView.this.mDisplayInteractionView.render(aPMInfo, WXPerformanceV2OverlayView.this.mInstanceId);
                    } else if ("properties".equals(str)) {
                        WXPerformanceV2OverlayView.this.mDisplayOulineView.render(aPMInfo);
                    } else if ("stats".equals(str)) {
                        WXPerformanceV2OverlayView.this.mDisplayStatsView.render(aPMInfo);
                        WXPerformanceV2OverlayView.this.mDisplayStagesView.render(aPMInfo);
                    } else if ("details".equals(str)) {
                        WXPerformanceV2OverlayView.this.mDisplayIssueView.render(aPMInfo, WXPerformanceV2OverlayView.this.mInstanceId);
                    }
                }
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_display_stages) {
            this.btnStages.setBackgroundColor(COLOR_SELECTED);
            this.btnOutline.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnStats.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnInteraction.setBackgroundColor(COLOR_UN_SELECTED);
            this.mDisplayStagesView.setVisibility(0);
            this.mDisplayOulineView.setVisibility(8);
            this.mDisplayStatsView.setVisibility(8);
            this.mDisplayInteractionView.setVisibility(8);
        } else if (view.getId() == R.id.btn_display_stats) {
            this.btnStages.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnOutline.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnStats.setBackgroundColor(COLOR_SELECTED);
            this.btnInteraction.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnIssue.setBackgroundColor(COLOR_UN_SELECTED);
            this.mDisplayStatsView.setVisibility(0);
            this.mDisplayStagesView.setVisibility(8);
            this.mDisplayOulineView.setVisibility(8);
            this.mDisplayInteractionView.setVisibility(8);
            this.mDisplayIssueView.setVisibility(8);
        } else if (view.getId() == R.id.btn_display_outline) {
            this.btnStages.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnOutline.setBackgroundColor(COLOR_SELECTED);
            this.btnStats.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnInteraction.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnIssue.setBackgroundColor(COLOR_UN_SELECTED);
            this.mDisplayStagesView.setVisibility(8);
            this.mDisplayOulineView.setVisibility(0);
            this.mDisplayStatsView.setVisibility(8);
            this.mDisplayInteractionView.setVisibility(8);
            this.mDisplayIssueView.setVisibility(8);
        } else if (view.getId() == R.id.btn_display_interaction) {
            this.btnInteraction.setBackgroundColor(COLOR_SELECTED);
            this.btnOutline.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnStats.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnStages.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnIssue.setBackgroundColor(COLOR_UN_SELECTED);
            this.mDisplayInteractionView.setVisibility(0);
            this.mDisplayStagesView.setVisibility(8);
            this.mDisplayOulineView.setVisibility(8);
            this.mDisplayStatsView.setVisibility(8);
            this.mDisplayIssueView.setVisibility(8);
        } else if (view.getId() == R.id.btn_display_issue) {
            this.btnIssue.setBackgroundColor(COLOR_SELECTED);
            this.btnInteraction.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnOutline.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnStats.setBackgroundColor(COLOR_UN_SELECTED);
            this.btnStages.setBackgroundColor(COLOR_UN_SELECTED);
            this.mDisplayIssueView.setVisibility(0);
            this.mDisplayInteractionView.setVisibility(8);
            this.mDisplayStagesView.setVisibility(8);
            this.mDisplayOulineView.setVisibility(8);
            this.mDisplayStatsView.setVisibility(8);
        }
    }
}
