package com.taobao.weex.analyzer.core.memory;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.AbstractLoopTask;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.chart.ChartView;
import com.taobao.weex.analyzer.view.chart.DynamicChartViewController;
import com.taobao.weex.analyzer.view.chart.LegendRenderer;
import com.taobao.weex.analyzer.view.chart.TimestampLabelFormatter;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.PermissionOverlayView;
import com.taobao.weex.utils.WXLogUtils;

public class MemorySampleView extends PermissionOverlayView {
    private DynamicChartViewController mChartViewController;
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    private SampleMemoryTask mTask;

    public MemorySampleView(Context context, Config config) {
        super(context, true, config);
        this.mWidth = -1;
        this.mHeight = (int) ViewUtils.dp2px(context, 220);
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_MEMORY);
    }

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.wxt_memory_view, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MemorySampleView.this.mOnCloseListener != null && MemorySampleView.this.isViewAttached) {
                    MemorySampleView.this.mOnCloseListener.close(MemorySampleView.this);
                    MemorySampleView.this.dismiss();
                }
            }
        });
        ((TextView) inflate.findViewById(R.id.gc)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {
                    /* access modifiers changed from: protected */
                    public Void doInBackground(Void... voidArr) {
                        try {
                            MemorySampler.tryForceGC();
                            return null;
                        } catch (Exception e) {
                            WXLogUtils.e(e.getMessage());
                            return null;
                        }
                    }
                }.execute(new Void[0]);
            }
        });
        MemorySampler.maxMemory();
        MemorySampler.totalMemory();
        double dalvikPss = (double) MemoryTracker.getDalvikPss(inflate.getContext());
        Double.isNaN(dalvikPss);
        double nativePss = (double) MemoryTracker.getNativePss(inflate.getContext());
        Double.isNaN(nativePss);
        this.mChartViewController = new DynamicChartViewController.Builder(this.mContext).titleOfAxisX((String) null).titleOfAxisY("MB").labelColor(-1).backgroundColor(Color.parseColor("#ba000000")).lineColor(Color.parseColor("#BACDDC39")).isFill(true).lineTitle("java").lineTitle2("native").fillColor(Color.parseColor("#BACDDC39")).fillColor2(Color.parseColor("#6B673AB7")).lineColor(Color.parseColor("#BACDDC39")).lineColor2(Color.parseColor("#6B673AB7")).numXLabels(5).minX(0.0d).maxX(20.0d).numYLabels(5).minY(0.0d).maxY(ViewUtils.findSuitableVal(Math.max(dalvikPss * 1.5d, nativePss * 1.5d), 4)).labelFormatter(new TimestampLabelFormatter()).maxDataPoints(22).build();
        LegendRenderer legendRenderer = ((ChartView) this.mChartViewController.getChartView()).getLegendRenderer();
        legendRenderer.setTextColor(-1);
        legendRenderer.setVisible(true);
        legendRenderer.setBackgroundColor(0);
        legendRenderer.setAlign(LegendRenderer.LegendAlign.TOP);
        legendRenderer.setMargin((int) ViewUtils.dp2px(this.mContext, 10));
        ((FrameLayout) inflate.findViewById(R.id.container)).addView(this.mChartViewController.getChartView(), new FrameLayout.LayoutParams(-1, -1));
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        if (this.mTask != null) {
            this.mTask.stop();
            this.mTask = null;
        }
        this.mTask = new SampleMemoryTask(this.mChartViewController, this.mContext, SDKUtils.isDebugMode(this.mContext));
        this.mTask.start();
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mTask != null) {
            this.mTask.stop();
            this.mTask = null;
            this.mChartViewController = null;
        }
    }

    private static class SampleMemoryTask extends AbstractLoopTask {
        private static final float LOAD_FACTOR = 0.75f;
        private boolean isDebug;
        /* access modifiers changed from: private */
        public int mAxisXValue = -1;
        private Context mContext;
        /* access modifiers changed from: private */
        public DynamicChartViewController mController;
        private MemoryTaskEntity mEntity;

        SampleMemoryTask(@NonNull DynamicChartViewController dynamicChartViewController, Context context, boolean z) {
            super(false, 1000);
            this.mController = dynamicChartViewController;
            this.isDebug = z;
            this.mEntity = new MemoryTaskEntity();
            this.mContext = context;
        }

        /* access modifiers changed from: protected */
        public void onStart() {
            this.mEntity.onTaskInit();
        }

        /* access modifiers changed from: protected */
        public void onRun() {
            if (this.mController != null) {
                this.mAxisXValue++;
                final double doubleValue = this.mEntity.onTaskRun().doubleValue();
                final double d = PSSMemoryInfoSampler.getAppPssInfo(this.mContext).nativePss;
                if (this.isDebug) {
                    Log.d("weex-analyzer", "memory usage : " + doubleValue + "MB, native memory usage : " + d + "MB");
                }
                runOnUIThread(new Runnable() {
                    public void run() {
                        if (SampleMemoryTask.this.checkIfNeedUpdateYAxis(Math.max(doubleValue, d))) {
                            SampleMemoryTask.this.mController.updateAxisY(0.0d, (SampleMemoryTask.this.mController.getMaxY() - SampleMemoryTask.this.mController.getMinY()) * 2.0d, 0);
                        }
                        SampleMemoryTask.this.mController.appendPointAndInvalidate((double) SampleMemoryTask.this.mAxisXValue, doubleValue);
                        SampleMemoryTask.this.mController.appendPointAndInvalidate2((double) SampleMemoryTask.this.mAxisXValue, d);
                    }
                });
            }
        }

        /* access modifiers changed from: private */
        public boolean checkIfNeedUpdateYAxis(double d) {
            return (this.mController.getMaxY() - this.mController.getMinY()) * 0.75d <= d;
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            this.mEntity.onTaskStop();
            this.mController = null;
        }
    }
}
