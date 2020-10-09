package com.taobao.weex.analyzer.core.cpu;

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
import com.taobao.weex.analyzer.core.cpu.CpuTaskEntity;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.chart.DynamicChartViewController;
import com.taobao.weex.analyzer.view.chart.TimestampLabelFormatter;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.PermissionOverlayView;
import com.taobao.weex.el.parse.Operators;

public class CpuSampleView extends PermissionOverlayView {
    private DynamicChartViewController mChartViewController;
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    private SampleCpuTask mSampleCpuTask;

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public CpuSampleView(Context context, Config config) {
        super(context, true, config);
        this.mWidth = -1;
        this.mHeight = (int) ViewUtils.dp2px(context, 150);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        this.mChartViewController = new DynamicChartViewController.Builder(this.mContext).title(this.mContext.getResources().getString(R.string.wxt_cpu)).titleOfAxisX((String) null).titleOfAxisY("cpu(%)").labelColor(-1).backgroundColor(Color.parseColor("#ba000000")).lineColor(Color.parseColor("#BACDDC39")).isFill(true).fillColor(Color.parseColor("#BACDDC39")).numXLabels(5).minX(0.0d).maxX(20.0d).numYLabels(5).minY(0.0d).maxY((double) 40).labelFormatter(new TimestampLabelFormatter()).maxDataPoints(22).build();
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        frameLayout.addView(this.mChartViewController.getChartView(), new FrameLayout.LayoutParams(-1, -1));
        TextView textView = new TextView(this.mContext);
        textView.setTextColor(-1);
        textView.setText(this.mContext.getResources().getString(R.string.wxt_close));
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CpuSampleView.this.mOnCloseListener != null && CpuSampleView.this.isViewAttached) {
                    CpuSampleView.this.mOnCloseListener.close(CpuSampleView.this);
                    CpuSampleView.this.dismiss();
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
        if (this.mSampleCpuTask != null) {
            this.mSampleCpuTask.stop();
            this.mSampleCpuTask = null;
        }
        this.mSampleCpuTask = new SampleCpuTask(this.mChartViewController, SDKUtils.isDebugMode(this.mContext));
        this.mSampleCpuTask.start();
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mSampleCpuTask != null) {
            this.mSampleCpuTask.stop();
            this.mSampleCpuTask = null;
        }
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_CPU);
    }

    private static class SampleCpuTask extends AbstractLoopTask {
        private static final float LOAD_FACTOR = 0.75f;
        private boolean isDebug = false;
        /* access modifiers changed from: private */
        public int mAxisXValue = -1;
        /* access modifiers changed from: private */
        public DynamicChartViewController mController;
        private CpuTaskEntity mEntity;

        SampleCpuTask(DynamicChartViewController dynamicChartViewController, boolean z) {
            super(false, 1000);
            this.mController = dynamicChartViewController;
            this.isDebug = z;
            this.mEntity = new CpuTaskEntity();
        }

        /* access modifiers changed from: protected */
        public void onStart() {
            this.mEntity.onTaskInit();
        }

        /* access modifiers changed from: protected */
        public void onRun() {
            CpuTaskEntity.CpuInfo onTaskRun = this.mEntity.onTaskRun();
            final double d = onTaskRun.pidCpuUsage;
            double d2 = onTaskRun.pidUserCpuUsage;
            double d3 = onTaskRun.pidKernelCpuUsage;
            if (this.isDebug) {
                Log.d("weex-analyzer", "cpu usage:" + d + "% [user " + d2 + ",kernel " + d3 + Operators.ARRAY_END_STR);
            }
            this.mAxisXValue++;
            runOnUIThread(new Runnable() {
                public void run() {
                    if (SampleCpuTask.this.checkIfNeedUpdateYAxis(d)) {
                        SampleCpuTask.this.mController.updateAxisY(SampleCpuTask.this.mController.getMinY(), Math.max(100.0d, (SampleCpuTask.this.mController.getMaxY() - SampleCpuTask.this.mController.getMinY()) + 10.0d), 0);
                    }
                    SampleCpuTask.this.mController.appendPointAndInvalidate((double) SampleCpuTask.this.mAxisXValue, d);
                }
            });
        }

        /* access modifiers changed from: private */
        public boolean checkIfNeedUpdateYAxis(double d) {
            return (this.mController.getMaxY() - this.mController.getMinY()) * 0.75d <= d;
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            this.mEntity.onTaskStop();
        }
    }
}
