package com.taobao.weex.analyzer.core.weex;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.AbstractLoopTask;
import com.taobao.weex.analyzer.core.cpu.CpuTaskEntity;
import com.taobao.weex.analyzer.core.fps.FPSSampler;
import com.taobao.weex.analyzer.core.memory.MemorySampler;
import com.taobao.weex.analyzer.core.memory.PSSMemoryInfoSampler;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.overlay.PermissionOverlayView;
import com.taobao.weex.el.parse.Operators;
import java.util.Locale;

public class PerfSampleOverlayView extends PermissionOverlayView {
    private InvalidateUITask mTask;

    public PerfSampleOverlayView(Context context, Config config) {
        super(context, true, config);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        View inflate = View.inflate(this.mContext, R.layout.wxt_perf_overlay_view, (ViewGroup) null);
        this.mX = (int) (((float) this.mContext.getResources().getDisplayMetrics().widthPixels) - ViewUtils.dp2px(this.mContext, 150));
        this.mY = (int) (((float) this.mContext.getResources().getDisplayMetrics().heightPixels) - ViewUtils.dp2px(this.mContext, 80));
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        if (this.mTask != null) {
            this.mTask.stop();
            this.mTask = null;
        }
        this.mTask = new InvalidateUITask(this.mWholeView);
        this.mTask.start();
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mTask != null) {
            this.mTask.stop();
            this.mTask = null;
        }
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_ALL_PERFORMANCE);
    }

    private static class InvalidateUITask extends AbstractLoopTask {
        private Context c;
        private CpuTaskEntity mCPUTaskEntity;
        /* access modifiers changed from: private */
        public TextView mCPUUsageText;
        /* access modifiers changed from: private */
        public TextView mFPSCheckText;
        private FPSSampler mFpsChecker;
        /* access modifiers changed from: private */
        public TextView mFpsValueText;
        /* access modifiers changed from: private */
        public TextView mFrameSkippedText;
        /* access modifiers changed from: private */
        public TextView mMemoryCheckText;
        /* access modifiers changed from: private */
        public double mMemoryInitValue;
        /* access modifiers changed from: private */
        public TextView mMemoryText;
        /* access modifiers changed from: private */
        public TextView mNativeMemoryText;
        /* access modifiers changed from: private */
        public long mStartTimeFPSBelow40 = -1;
        /* access modifiers changed from: private */
        public int mTotalFrameDropped = 0;

        @TargetApi(16)
        InvalidateUITask(@NonNull View view) {
            super(false);
            this.c = view.getContext();
            this.mDelayMillis = 1000;
            this.mFpsChecker = new FPSSampler(Choreographer.getInstance());
            this.mMemoryText = (TextView) view.findViewById(R.id.memory_usage);
            this.mNativeMemoryText = (TextView) view.findViewById(R.id.native_memory_usage);
            this.mFpsValueText = (TextView) view.findViewById(R.id.fps_value);
            this.mFrameSkippedText = (TextView) view.findViewById(R.id.frame_skiped);
            this.mCPUUsageText = (TextView) view.findViewById(R.id.cpu_usage);
            this.mMemoryCheckText = (TextView) view.findViewById(R.id.memory_ok);
            this.mFPSCheckText = (TextView) view.findViewById(R.id.fps_ok);
        }

        /* access modifiers changed from: protected */
        @TargetApi(16)
        public void onStart() {
            super.onStart();
            if (this.mFpsChecker == null) {
                this.mFpsChecker = new FPSSampler(Choreographer.getInstance());
            }
            if (this.mCPUTaskEntity == null) {
                this.mCPUTaskEntity = new CpuTaskEntity();
            }
            this.mFpsChecker.reset();
            this.mFpsChecker.start();
        }

        /* access modifiers changed from: protected */
        public void onRun() {
            double d;
            final double memoryUsage = MemorySampler.getMemoryUsage();
            final double d2 = PSSMemoryInfoSampler.getAppPssInfo(this.c).nativePss;
            final CpuTaskEntity.CpuInfo onTaskRun = this.mCPUTaskEntity.onTaskRun();
            if (Build.VERSION.SDK_INT >= 16) {
                double fps = this.mFpsChecker.getFPS();
                this.mTotalFrameDropped += Math.max(this.mFpsChecker.getExpectedNumFrames() - this.mFpsChecker.getNumFrames(), 0);
                this.mFpsChecker.reset();
                d = fps;
            } else {
                d = 0.0d;
            }
            if (this.mMemoryInitValue <= 0.0d) {
                this.mMemoryInitValue = memoryUsage;
            }
            final double d3 = d;
            runOnUIThread(new Runnable() {
                public void run() {
                    InvalidateUITask.this.mMemoryText.setText(String.format(Locale.CHINA, "java heap : %.2fMB", new Object[]{Double.valueOf(memoryUsage)}));
                    InvalidateUITask.this.mNativeMemoryText.setText(String.format(Locale.getDefault(), "native heap : %.2fMB", new Object[]{Double.valueOf(d2)}));
                    double d = onTaskRun.pidCpuUsage;
                    if (d >= 0.0d) {
                        InvalidateUITask.this.mCPUUsageText.setVisibility(0);
                        TextView access$200 = InvalidateUITask.this.mCPUUsageText;
                        access$200.setText(String.format(Locale.getDefault(), "cpu usage: %.2f", new Object[]{Double.valueOf(d)}) + Operators.MOD);
                    } else {
                        InvalidateUITask.this.mCPUUsageText.setVisibility(8);
                    }
                    double access$300 = memoryUsage - InvalidateUITask.this.mMemoryInitValue;
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format(Locale.getDefault(), "Java内存增量:%.2fMB ", new Object[]{Double.valueOf(access$300)}));
                    sb.append(access$300 > 30.0d ? ",不达标" : "，达标");
                    InvalidateUITask.this.mMemoryCheckText.setText(sb.toString());
                    if (Build.VERSION.SDK_INT >= 16) {
                        if (d3 <= 40.0d) {
                            InvalidateUITask.this.mFpsValueText.setTextColor(SupportMenu.CATEGORY_MASK);
                            if (InvalidateUITask.this.mStartTimeFPSBelow40 > 0) {
                                long currentTimeMillis = System.currentTimeMillis() - InvalidateUITask.this.mStartTimeFPSBelow40;
                                if (currentTimeMillis >= TBToast.Duration.MEDIUM) {
                                    InvalidateUITask.this.mFPSCheckText.setText("fps连续3秒低于40,不达标!");
                                } else {
                                    TextView access$700 = InvalidateUITask.this.mFPSCheckText;
                                    access$700.setText("fps连续" + currentTimeMillis + "ms低于40!");
                                }
                            } else {
                                long unused = InvalidateUITask.this.mStartTimeFPSBelow40 = System.currentTimeMillis();
                                InvalidateUITask.this.mFPSCheckText.setText("fps达标");
                            }
                        } else {
                            InvalidateUITask.this.mFpsValueText.setTextColor(-1);
                            long unused2 = InvalidateUITask.this.mStartTimeFPSBelow40 = -1;
                            InvalidateUITask.this.mFPSCheckText.setText("fps达标");
                        }
                        InvalidateUITask.this.mFpsValueText.setText(String.format(Locale.CHINA, "fps : %.2f", new Object[]{Double.valueOf(d3)}));
                        InvalidateUITask.this.mFrameSkippedText.setText(String.format(Locale.getDefault(), "skipped frames : %d", new Object[]{Integer.valueOf(InvalidateUITask.this.mTotalFrameDropped)}));
                        return;
                    }
                    InvalidateUITask.this.mFpsValueText.setText("fps : ??");
                    InvalidateUITask.this.mFrameSkippedText.setText("skipped frames : ??");
                }
            });
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            this.mTotalFrameDropped = 0;
            this.mFpsChecker.stop();
            this.mFpsChecker = null;
        }
    }
}
