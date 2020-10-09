package com.taobao.weex.analyzer.core.traffic;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.AbstractLoopTask;
import com.taobao.weex.analyzer.core.traffic.TrafficTaskEntity;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.chart.ChartView;
import com.taobao.weex.analyzer.view.chart.DynamicChartViewController;
import com.taobao.weex.analyzer.view.chart.LegendRenderer;
import com.taobao.weex.analyzer.view.chart.TimestampLabelFormatter;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.PermissionOverlayView;

public class TrafficSampleView extends PermissionOverlayView {
    private DynamicChartViewController mChartViewController;
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    private SampleTrafficTask mSampleTrafficTask;

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public TrafficSampleView(Context context, Config config) {
        super(context, true, config);
        this.mWidth = -1;
        this.mHeight = (int) ViewUtils.dp2px(context, 150);
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_TRAFFIC);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        this.mChartViewController = new DynamicChartViewController.Builder(this.mContext).title(this.mContext.getResources().getString(R.string.wxt_traffic)).titleOfAxisX((String) null).titleOfAxisY("kb/s").labelColor(-1).backgroundColor(Color.parseColor("#ba000000")).lineColor(Color.parseColor("#BACDDC39")).lineTitle("rx").lineTitle2("tx").lineColor2(Color.parseColor("#6B673AB7")).isFill(true).fillColor(Color.parseColor("#BACDDC39")).fillColor2(Color.parseColor("#6B673AB7")).numXLabels(5).minX(0.0d).maxX(20.0d).numYLabels(5).minY(0.0d).maxY((double) 64).labelFormatter(new TimestampLabelFormatter()).maxDataPoints(22).build();
        LegendRenderer legendRenderer = ((ChartView) this.mChartViewController.getChartView()).getLegendRenderer();
        legendRenderer.setTextColor(-1);
        legendRenderer.setVisible(true);
        legendRenderer.setBackgroundColor(0);
        legendRenderer.setAlign(LegendRenderer.LegendAlign.TOP);
        legendRenderer.setMargin((int) ViewUtils.dp2px(this.mContext, 10));
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        frameLayout.addView(this.mChartViewController.getChartView(), new FrameLayout.LayoutParams(-1, -1));
        TextView textView = new TextView(this.mContext);
        textView.setTextColor(-1);
        textView.setText(this.mContext.getResources().getString(R.string.wxt_close));
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TrafficSampleView.this.mOnCloseListener != null && TrafficSampleView.this.isViewAttached) {
                    TrafficSampleView.this.mOnCloseListener.close(TrafficSampleView.this);
                    TrafficSampleView.this.dismiss();
                }
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) ViewUtils.dp2px(this.mContext, 50), (int) ViewUtils.dp2px(this.mContext, 30));
        layoutParams.gravity = 5;
        frameLayout.addView(textView, layoutParams);
        return frameLayout;
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        if (this.mSampleTrafficTask != null) {
            this.mSampleTrafficTask.stop();
            this.mSampleTrafficTask = null;
        }
        this.mSampleTrafficTask = new SampleTrafficTask(this.mChartViewController, SDKUtils.isDebugMode(this.mContext));
        this.mSampleTrafficTask.start();
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mSampleTrafficTask != null) {
            this.mSampleTrafficTask.stop();
            this.mSampleTrafficTask = null;
        }
    }

    private static class SampleTrafficTask extends AbstractLoopTask {
        private static final int DELAY_IN_MILLIS = 1000;
        private static final float LOAD_FACTOR = 0.5f;
        private boolean isDebug;
        /* access modifiers changed from: private */
        public int mAxisXValue = -1;
        /* access modifiers changed from: private */
        public DynamicChartViewController mController;
        private TrafficTaskEntity mEntity;

        SampleTrafficTask(DynamicChartViewController dynamicChartViewController, boolean z) {
            super(false, 1000);
            this.isDebug = z;
            this.mController = dynamicChartViewController;
            this.mEntity = new TrafficTaskEntity(1000);
        }

        /* access modifiers changed from: protected */
        public void onStart() {
            this.mEntity.onTaskInit();
        }

        /* access modifiers changed from: protected */
        public void onRun() {
            TrafficTaskEntity.TrafficInfo onTaskRun = this.mEntity.onTaskRun();
            final double d = onTaskRun.txSpeed;
            final double d2 = onTaskRun.rxSpeed;
            if (this.isDebug) {
                Log.d("weex-analyzer", "network[tx:" + d + "kb/s,rx:" + d2 + "kb/s]");
            }
            this.mAxisXValue++;
            runOnUIThread(new Runnable() {
                public void run() {
                    if (SampleTrafficTask.this.checkIfNeedUpdateYAxis(Math.max(d2, d))) {
                        SampleTrafficTask.this.mController.updateAxisY(SampleTrafficTask.this.mController.getMinY(), (SampleTrafficTask.this.mController.getMaxY() - SampleTrafficTask.this.mController.getMinY()) * 2.0d, 0);
                    }
                    SampleTrafficTask.this.mController.appendPointAndInvalidate((double) SampleTrafficTask.this.mAxisXValue, d2);
                    SampleTrafficTask.this.mController.appendPointAndInvalidate2((double) SampleTrafficTask.this.mAxisXValue, d);
                }
            });
        }

        /* access modifiers changed from: private */
        public boolean checkIfNeedUpdateYAxis(double d) {
            return (this.mController.getMaxY() - this.mController.getMinY()) * 0.5d <= d;
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            this.mEntity.onTaskStop();
        }
    }
}
