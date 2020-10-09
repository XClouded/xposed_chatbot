package com.taobao.weex.analyzer.core.weex;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.view.alert.PermissionAlertView;
import java.util.List;

public class WXPerformanceAnalysisView extends PermissionAlertView {
    private Performance mCurPerformance;
    private List<Performance> mHistoryPerfList;
    /* access modifiers changed from: private */
    public WXPerfHistoryItemView mPerfHistoryItemView;
    /* access modifiers changed from: private */
    public WXPerfItemView mPerfItemView;

    public WXPerformanceAnalysisView(Context context, @NonNull Performance performance, @NonNull List<Performance> list, Config config) {
        super(context, config);
        this.mCurPerformance = performance;
        this.mHistoryPerfList = list;
    }

    /* access modifiers changed from: protected */
    public void onInitView(@NonNull Window window) {
        this.mPerfItemView = (WXPerfItemView) window.findViewById(R.id.panel_cur_perf);
        this.mPerfHistoryItemView = (WXPerfHistoryItemView) window.findViewById(R.id.panel_history_perf);
        final TextView textView = (TextView) window.findViewById(R.id.btn_cur_panel);
        final TextView textView2 = (TextView) window.findViewById(R.id.btn_history_panel);
        window.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WXPerformanceAnalysisView.this.dismiss();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                textView2.setTextColor(-16777216);
                textView2.setBackgroundColor(-1);
                textView.setTextColor(-1);
                textView.setBackgroundColor(Color.parseColor("#03A9F4"));
                WXPerformanceAnalysisView.this.mPerfItemView.setVisibility(0);
                WXPerformanceAnalysisView.this.mPerfHistoryItemView.setVisibility(8);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                textView.setTextColor(-16777216);
                textView.setBackgroundColor(-1);
                textView2.setTextColor(-1);
                textView2.setBackgroundColor(Color.parseColor("#03A9F4"));
                WXPerformanceAnalysisView.this.mPerfHistoryItemView.setVisibility(0);
                WXPerformanceAnalysisView.this.mPerfItemView.setVisibility(8);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        this.mPerfItemView.inflateData(this.mCurPerformance);
        this.mPerfHistoryItemView.inflateData(this.mHistoryPerfList);
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_weex_perf_analysis_view;
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_WEEX_PERFORMANCE_STATISTICS);
    }
}
