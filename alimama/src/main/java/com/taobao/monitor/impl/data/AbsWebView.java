package com.taobao.monitor.impl.data;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.taobao.monitor.impl.logger.Logger;
import com.taobao.monitor.impl.util.TimeUtils;

public abstract class AbsWebView implements IWebView {
    private static final String TAG = "AbsWebView";
    private int hashCode = 0;
    /* access modifiers changed from: private */
    public int progress = 0;
    /* access modifiers changed from: private */
    public long progressEndTime;
    private long startTime;

    public abstract int getProgress(View view);

    public abstract boolean isWebView(View view);

    public int webViewProgress(final View view) {
        if (view.hashCode() != this.hashCode) {
            this.hashCode = view.hashCode();
            this.progress = 0;
            this.startTime = TimeUtils.currentTimeMillis();
            this.progressEndTime = 0;
            return this.progress;
        }
        if (this.progress != 100) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    try {
                        int unused = AbsWebView.this.progress = AbsWebView.this.getProgress(view);
                        if (AbsWebView.this.progress == 100) {
                            long unused2 = AbsWebView.this.progressEndTime = TimeUtils.currentTimeMillis();
                        }
                    } catch (Exception e) {
                        Logger.throwException(e);
                        int unused3 = AbsWebView.this.progress = 0;
                    }
                }
            });
        }
        long currentTimeMillis = TimeUtils.currentTimeMillis();
        if (this.progressEndTime != 0) {
            double d = (double) (this.progressEndTime - this.startTime);
            Double.isNaN(d);
            if (((double) (currentTimeMillis - this.progressEndTime)) > d * 1.5d) {
                return this.progress;
            }
        }
        return this.progress - 1;
    }
}
