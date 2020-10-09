package com.alibaba.android.bindingx.core.internal;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Choreographer;
import androidx.annotation.NonNull;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

abstract class AnimationFrame {

    interface Callback {
        void doFrame();
    }

    /* access modifiers changed from: package-private */
    public abstract void clear();

    /* access modifiers changed from: package-private */
    public abstract void requestAnimationFrame(@NonNull Callback callback);

    /* access modifiers changed from: package-private */
    public abstract void terminate();

    AnimationFrame() {
    }

    static AnimationFrame newInstance() {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ChoreographerAnimationFrameImpl();
        }
        return new HandlerAnimationFrameImpl();
    }

    @TargetApi(16)
    private static class ChoreographerAnimationFrameImpl extends AnimationFrame implements Choreographer.FrameCallback {
        private Callback callback;
        /* access modifiers changed from: private */
        public Choreographer choreographer;
        private boolean isRunning;

        @TargetApi(16)
        ChoreographerAnimationFrameImpl() {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        Choreographer unused = ChoreographerAnimationFrameImpl.this.choreographer = Choreographer.getInstance();
                        countDownLatch.countDown();
                    }
                });
                try {
                    if (!countDownLatch.await(500, TimeUnit.MILLISECONDS)) {
                        this.choreographer = Choreographer.getInstance();
                    }
                } catch (InterruptedException unused) {
                }
            } else {
                this.choreographer = Choreographer.getInstance();
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            if (this.choreographer != null) {
                this.choreographer.removeFrameCallback(this);
            }
            this.isRunning = false;
        }

        /* access modifiers changed from: package-private */
        public void terminate() {
            clear();
            this.choreographer = null;
        }

        /* access modifiers changed from: package-private */
        public void requestAnimationFrame(@NonNull Callback callback2) {
            this.callback = callback2;
            this.isRunning = true;
            if (this.choreographer != null) {
                this.choreographer.postFrameCallback(this);
            }
        }

        public void doFrame(long j) {
            if (this.callback != null) {
                this.callback.doFrame();
            }
            if (this.choreographer != null && this.isRunning) {
                this.choreographer.postFrameCallback(this);
            }
        }
    }

    private static class HandlerAnimationFrameImpl extends AnimationFrame implements Handler.Callback {
        private static final long DEFAULT_DELAY_MILLIS = 16;
        private static final int MSG_FRAME_CALLBACK = 100;
        private Callback callback;
        private boolean isRunning;
        private Handler mInnerHandler = new Handler(Looper.getMainLooper(), this);

        HandlerAnimationFrameImpl() {
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            if (this.mInnerHandler != null) {
                this.mInnerHandler.removeCallbacksAndMessages((Object) null);
            }
            this.isRunning = false;
        }

        /* access modifiers changed from: package-private */
        public void terminate() {
            clear();
            this.mInnerHandler = null;
        }

        /* access modifiers changed from: package-private */
        public void requestAnimationFrame(@NonNull Callback callback2) {
            this.callback = callback2;
            this.isRunning = true;
            if (this.mInnerHandler != null) {
                this.mInnerHandler.sendEmptyMessage(100);
            }
        }

        public boolean handleMessage(Message message) {
            if (message == null || message.what != 100 || this.mInnerHandler == null) {
                return false;
            }
            if (this.callback != null) {
                this.callback.doFrame();
            }
            if (!this.isRunning) {
                return true;
            }
            this.mInnerHandler.sendEmptyMessageDelayed(100, 16);
            return true;
        }
    }
}
