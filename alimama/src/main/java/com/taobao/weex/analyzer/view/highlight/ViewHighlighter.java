package com.taobao.weex.analyzer.view.highlight;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ViewHighlighter {
    public abstract void clearHighlight();

    public abstract boolean hasHighlight(View view);

    public abstract void setHighlightedView(View view, int i);

    public static ViewHighlighter newInstance() {
        if (Build.VERSION.SDK_INT >= 18) {
            return new OverlayHighlighter();
        }
        return new NoopHighlighter();
    }

    protected ViewHighlighter() {
    }

    private static final class NoopHighlighter extends ViewHighlighter {
        public void clearHighlight() {
        }

        public boolean hasHighlight(View view) {
            return false;
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
            setHighlightedViewImpl(view, i);
        }

        public boolean hasHighlight(View view) {
            return this.mHighlightedView != null && this.mHighlightedView.equals(view);
        }

        private void setHighlightedViewImpl(@Nullable View view, int i) {
            this.mHandler.removeCallbacks(this.mHighlightViewOnUiThreadRunnable);
            this.mViewToHighlight.set(view);
            this.mContentColor.set(i);
            this.mHandler.postDelayed(this.mHighlightViewOnUiThreadRunnable, 5);
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
