package com.taobao.weex.analyzer.core.fps;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.AbstractLoopTask;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.chart.DynamicChartViewController;
import com.taobao.weex.analyzer.view.chart.TimestampLabelFormatter;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.PermissionOverlayView;

public class FpsSampleView extends PermissionOverlayView {
    private DynamicChartViewController mChartViewController;
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    private SampleFPSTask mTask;

    public FpsSampleView(Context context, Config config) {
        super(context, true, config);
        this.mWidth = -1;
        this.mHeight = (int) ViewUtils.dp2px(context, 150);
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_FPS);
    }

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        this.mChartViewController = new DynamicChartViewController.Builder(this.mContext).title(this.mContext.getResources().getString(R.string.wxt_fps)).titleOfAxisX((String) null).titleOfAxisY(Config.TYPE_FPS).labelColor(-1).backgroundColor(Color.parseColor("#ba000000")).lineColor(Color.parseColor("#BACDDC39")).isFill(true).fillColor(Color.parseColor("#BACDDC39")).numXLabels(5).minX(0.0d).maxX(20.0d).numYLabels(5).minY(0.0d).maxY((double) 60).labelFormatter(new TimestampLabelFormatter()).maxDataPoints(22).build();
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        frameLayout.addView(this.mChartViewController.getChartView(), new FrameLayout.LayoutParams(-1, -1));
        TextView textView = new TextView(this.mContext);
        textView.setTextColor(-1);
        textView.setText(this.mContext.getResources().getString(R.string.wxt_close));
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FpsSampleView.this.mOnCloseListener != null && FpsSampleView.this.isViewAttached) {
                    FpsSampleView.this.mOnCloseListener.close(FpsSampleView.this);
                    FpsSampleView.this.dismiss();
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
        if (this.mTask != null) {
            this.mTask.stop();
            this.mTask = null;
        }
        this.mTask = new SampleFPSTask(this.mChartViewController, SDKUtils.isDebugMode(this.mContext));
        this.mTask.start();
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mTask != null) {
            this.mTask.stop();
            this.mTask = null;
        }
    }

    private static class SampleFPSTask extends AbstractLoopTask {
        private boolean isDebug;
        /* access modifiers changed from: private */
        public int mAxisXValue = -1;
        /* access modifiers changed from: private */
        public DynamicChartViewController mController;
        private FpsTaskEntity mEntity;

        SampleFPSTask(@NonNull DynamicChartViewController dynamicChartViewController, boolean z) {
            super(false, 1000);
            this.mController = dynamicChartViewController;
            this.isDebug = z;
            this.mEntity = new FpsTaskEntity();
        }

        /* access modifiers changed from: protected */
        public void onStart() {
            super.onStart();
            this.mEntity.onTaskInit();
        }

        /* access modifiers changed from: protected */
        public void onRun() {
            if (this.mController != null && Build.VERSION.SDK_INT >= 16) {
                this.mAxisXValue++;
                final double doubleValue = this.mEntity.onTaskRun().doubleValue();
                if (this.isDebug) {
                    Log.d("weex-analyzer", "current fps : " + doubleValue);
                }
                runOnUIThread(new Runnable() {
                    public void run() {
                        SampleFPSTask.this.mController.appendPointAndInvalidate((double) SampleFPSTask.this.mAxisXValue, doubleValue);
                    }
                });
            }
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            this.mController = null;
            this.mEntity.onTaskStop();
        }
    }
}
