package com.taobao.weex.devtools.inspector.elements.android;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.taobao.weex.devtools.common.LogUtil;
import com.taobao.weex.devtools.common.Util;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;

abstract class ViewHighlighter {
    public abstract void clearHighlight();

    public abstract void setHighlightedView(View view, int i);

    public static ViewHighlighter newInstance() {
        if (Build.VERSION.SDK_INT >= 18) {
            return new OverlayHighlighter();
        }
        LogUtil.w("Running on pre-JBMR2: View highlighting is not supported");
        return new NoopHighlighter();
    }

    protected ViewHighlighter() {
    }

    private static final class NoopHighlighter extends ViewHighlighter {
        public void clearHighlight() {
        }

        public void setHighlightedView(View view, int i) {
        }

        private NoopHighlighter() {
        }
    }

    @TargetApi(18)
    private static final class OverlayHighlighter extends ViewHighlighter {
        private AtomicInteger mContentColor = new AtomicInteger();
        private final Handler mHandler = new Handler(Looper.getMainLooper());
        private final ViewHighlightOverlays mHighlightOverlays = ViewHighlightOverlays.newInstance();
        private final Runnable mHighlightViewOnUiThreadRunnable = new Runnable() {
            public void run() {
                OverlayHighlighter.this.highlightViewOnUiThread();
            }
        };
        private View mHighlightedView;
        private AtomicReference<View> mViewToHighlight = new AtomicReference<>();

        public void clearHighlight() {
            setHighlightedViewImpl((View) null, 0);
        }

        public void setHighlightedView(View view, int i) {
            setHighlightedViewImpl((View) Util.throwIfNull(view), i);
        }

        private void setHighlightedViewImpl(@Nullable View view, int i) {
            this.mHandler.removeCallbacks(this.mHighlightViewOnUiThreadRunnable);
            this.mViewToHighlight.set(view);
            this.mContentColor.set(i);
            this.mHandler.postDelayed(this.mHighlightViewOnUiThreadRunnable, 100);
        }

        /* access modifiers changed from: private */
        public void highlightViewOnUiThread() {
            View andSet = this.mViewToHighlight.getAndSet((Object) null);
            if (andSet != this.mHighlightedView) {
                if (this.mHighlightedView != null) {
                    this.mHighlightOverlays.removeHighlight(this.mHighlightedView);
                }
                if (andSet != null) {
                    this.mHighlightOverlays.highlightView(andSet, this.mContentColor.get());
                }
                this.mHighlightedView = andSet;
            }
        }
    }
}
