package com.taobao.weex.analyzer.view.highlight;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MutipleViewHighlighter {
    public abstract void addHighlightedView(View view);

    public abstract void clearHighlight();

    public abstract void setColor(@ColorInt int i);

    public static MutipleViewHighlighter newInstance() {
        if (Build.VERSION.SDK_INT >= 18) {
            return new OverlayHighlighter();
        }
        return new NoOpHighlighter();
    }

    public boolean isSupport() {
        return Build.VERSION.SDK_INT >= 18;
    }

    private static final class NoOpHighlighter extends MutipleViewHighlighter {
        public void addHighlightedView(View view) {
        }

        public void clearHighlight() {
        }

        public void setColor(@ColorInt int i) {
        }

        private NoOpHighlighter() {
        }
    }

    private static class HighLightedView {
        ViewHighlightOverlays highlightOverlays;
        WeakReference<View> viewRef;

        private HighLightedView() {
        }
    }

    @TargetApi(18)
    private static final class OverlayHighlighter extends MutipleViewHighlighter {
        private AtomicInteger mContentColor = new AtomicInteger();
        private final Handler mHandler = new Handler(Looper.getMainLooper());
        private CopyOnWriteArrayList<HighLightedView> mViewListToHighlight = new CopyOnWriteArrayList<>();

        OverlayHighlighter() {
        }

        public void clearHighlight() {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    OverlayHighlighter.this.hideHighlightViewOnUiThread();
                }
            }, 0);
        }

        public void addHighlightedView(View view) {
            if (view != null) {
                if (this.mViewListToHighlight == null || !contains(view)) {
                    this.mHandler.removeCallbacksAndMessages((Object) null);
                    HighLightedView highLightedView = new HighLightedView();
                    highLightedView.viewRef = new WeakReference<>(view);
                    highLightedView.highlightOverlays = ViewHighlightOverlays.newInstance();
                    this.mViewListToHighlight.add(highLightedView);
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            OverlayHighlighter.this.highlightViewOnUiThread();
                        }
                    }, 0);
                }
            }
        }

        public void setColor(@ColorInt int i) {
            this.mContentColor.set(i);
        }

        private boolean contains(@NonNull View view) {
            if (this.mViewListToHighlight == null || this.mViewListToHighlight.isEmpty()) {
                return false;
            }
            Iterator<HighLightedView> it = this.mViewListToHighlight.iterator();
            while (it.hasNext()) {
                HighLightedView next = it.next();
                if (next.viewRef != null && view.equals(next.viewRef.get())) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: private */
        public void highlightViewOnUiThread() {
            Iterator<HighLightedView> it = this.mViewListToHighlight.iterator();
            while (it.hasNext()) {
                HighLightedView next = it.next();
                if (!(next == null || next.highlightOverlays == null || next.viewRef == null || next.viewRef.get() == null)) {
                    next.highlightOverlays.removeHighlight((View) next.viewRef.get());
                    next.highlightOverlays.highlightView((View) next.viewRef.get(), this.mContentColor.get());
                }
            }
        }

        /* access modifiers changed from: private */
        public void hideHighlightViewOnUiThread() {
            Iterator<HighLightedView> it = this.mViewListToHighlight.iterator();
            while (it.hasNext()) {
                HighLightedView next = it.next();
                if (!(next == null || next.highlightOverlays == null || next.viewRef == null || next.viewRef.get() == null)) {
                    next.highlightOverlays.removeHighlight((View) next.viewRef.get());
                }
            }
            this.mViewListToHighlight.clear();
        }
    }
}
