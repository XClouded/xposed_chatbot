package com.taobao.weex.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.utils.WXResourceUtils;

public class WXVideoView extends VideoView implements WXGestureObservable {
    private VideoPlayListener mVideoPauseListener;
    private WXGesture wxGesture;

    public interface VideoPlayListener {
        void onPause();

        void onStart();
    }

    public WXVideoView(Context context) {
        super(context);
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public void setOnVideoPauseListener(VideoPlayListener videoPlayListener) {
        this.mVideoPauseListener = videoPlayListener;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        return this.wxGesture != null ? onTouchEvent | this.wxGesture.onTouch(this, motionEvent) : onTouchEvent;
    }

    public void start() {
        super.start();
        if (this.mVideoPauseListener != null) {
            this.mVideoPauseListener.onStart();
        }
    }

    public void pause() {
        super.pause();
        if (this.mVideoPauseListener != null) {
            this.mVideoPauseListener.onPause();
        }
    }

    public static class Wrapper extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {
        private boolean mControls = true;
        private MediaController mMediaController;
        private MediaPlayer.OnCompletionListener mOnCompletionListener;
        private MediaPlayer.OnErrorListener mOnErrorListener;
        private MediaPlayer.OnPreparedListener mOnPreparedListener;
        private ProgressBar mProgressBar;
        private Uri mUri;
        private VideoPlayListener mVideoPlayListener;
        private WXVideoView mVideoView;

        public Wrapper(Context context) {
            super(context);
            init(context);
        }

        public Wrapper(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            init(context);
        }

        public Wrapper(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            init(context);
        }

        private void init(Context context) {
            setBackgroundColor(WXResourceUtils.getColor("#ee000000"));
            this.mProgressBar = new ProgressBar(context);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            this.mProgressBar.setLayoutParams(layoutParams);
            layoutParams.gravity = 17;
            addView(this.mProgressBar);
            getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        public ProgressBar getProgressBar() {
            return this.mProgressBar;
        }

        @Nullable
        public WXVideoView getVideoView() {
            return this.mVideoView;
        }

        @NonNull
        public WXVideoView createIfNotExist() {
            if (this.mVideoView == null) {
                createVideoView();
            }
            return this.mVideoView;
        }

        @Nullable
        public MediaController getMediaController() {
            return this.mMediaController;
        }

        public void setVideoURI(Uri uri) {
            this.mUri = uri;
            if (this.mVideoView != null) {
                this.mVideoView.setVideoURI(uri);
            }
        }

        public void start() {
            if (this.mVideoView != null) {
                this.mVideoView.start();
            }
        }

        public void pause() {
            if (this.mVideoView != null) {
                this.mVideoView.pause();
            }
        }

        public void stopPlayback() {
            if (this.mVideoView != null) {
                this.mVideoView.stopPlayback();
            }
        }

        public void resume() {
            if (this.mVideoView != null) {
                this.mVideoView.resume();
            }
        }

        public void setOnErrorListener(MediaPlayer.OnErrorListener onErrorListener) {
            this.mOnErrorListener = onErrorListener;
            if (this.mVideoView != null) {
                this.mVideoView.setOnErrorListener(onErrorListener);
            }
        }

        public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
            this.mOnPreparedListener = onPreparedListener;
            if (this.mVideoView != null) {
                this.mVideoView.setOnPreparedListener(onPreparedListener);
            }
        }

        public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
            this.mOnCompletionListener = onCompletionListener;
            if (this.mVideoView != null) {
                this.mVideoView.setOnCompletionListener(onCompletionListener);
            }
        }

        public void setOnVideoPauseListener(VideoPlayListener videoPlayListener) {
            this.mVideoPlayListener = videoPlayListener;
            if (this.mVideoView != null) {
                this.mVideoView.setOnVideoPauseListener(videoPlayListener);
            }
        }

        public void setControls(boolean z) {
            this.mControls = z;
            if (this.mVideoView != null && this.mMediaController != null) {
                if (!this.mControls) {
                    this.mMediaController.setVisibility(8);
                } else {
                    this.mMediaController.setVisibility(0);
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0064, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private synchronized void createVideoView() {
            /*
                r4 = this;
                monitor-enter(r4)
                com.taobao.weex.ui.view.WXVideoView r0 = r4.mVideoView     // Catch:{ all -> 0x0065 }
                if (r0 == 0) goto L_0x0007
                monitor-exit(r4)
                return
            L_0x0007:
                android.content.Context r0 = r4.getContext()     // Catch:{ all -> 0x0065 }
                com.taobao.weex.ui.view.WXVideoView r1 = new com.taobao.weex.ui.view.WXVideoView     // Catch:{ all -> 0x0065 }
                r1.<init>(r0)     // Catch:{ all -> 0x0065 }
                android.widget.FrameLayout$LayoutParams r2 = new android.widget.FrameLayout$LayoutParams     // Catch:{ all -> 0x0065 }
                r3 = -1
                r2.<init>(r3, r3)     // Catch:{ all -> 0x0065 }
                r3 = 17
                r2.gravity = r3     // Catch:{ all -> 0x0065 }
                r1.setLayoutParams(r2)     // Catch:{ all -> 0x0065 }
                r2 = 0
                r4.addView(r1, r2)     // Catch:{ all -> 0x0065 }
                android.media.MediaPlayer$OnErrorListener r3 = r4.mOnErrorListener     // Catch:{ all -> 0x0065 }
                r1.setOnErrorListener(r3)     // Catch:{ all -> 0x0065 }
                android.media.MediaPlayer$OnPreparedListener r3 = r4.mOnPreparedListener     // Catch:{ all -> 0x0065 }
                r1.setOnPreparedListener(r3)     // Catch:{ all -> 0x0065 }
                android.media.MediaPlayer$OnCompletionListener r3 = r4.mOnCompletionListener     // Catch:{ all -> 0x0065 }
                r1.setOnCompletionListener(r3)     // Catch:{ all -> 0x0065 }
                com.taobao.weex.ui.view.WXVideoView$VideoPlayListener r3 = r4.mVideoPlayListener     // Catch:{ all -> 0x0065 }
                r1.setOnVideoPauseListener(r3)     // Catch:{ all -> 0x0065 }
                android.widget.MediaController r3 = new android.widget.MediaController     // Catch:{ all -> 0x0065 }
                r3.<init>(r0)     // Catch:{ all -> 0x0065 }
                r3.setAnchorView(r4)     // Catch:{ all -> 0x0065 }
                r1.setMediaController(r3)     // Catch:{ all -> 0x0065 }
                r3.setMediaPlayer(r1)     // Catch:{ all -> 0x0065 }
                boolean r0 = r4.mControls     // Catch:{ all -> 0x0065 }
                if (r0 != 0) goto L_0x004d
                r0 = 8
                r3.setVisibility(r0)     // Catch:{ all -> 0x0065 }
                goto L_0x0050
            L_0x004d:
                r3.setVisibility(r2)     // Catch:{ all -> 0x0065 }
            L_0x0050:
                r4.mMediaController = r3     // Catch:{ all -> 0x0065 }
                r4.mVideoView = r1     // Catch:{ all -> 0x0065 }
                com.taobao.weex.ui.view.WXVideoView r0 = r4.mVideoView     // Catch:{ all -> 0x0065 }
                r1 = 1
                r0.setZOrderOnTop(r1)     // Catch:{ all -> 0x0065 }
                android.net.Uri r0 = r4.mUri     // Catch:{ all -> 0x0065 }
                if (r0 == 0) goto L_0x0063
                android.net.Uri r0 = r4.mUri     // Catch:{ all -> 0x0065 }
                r4.setVideoURI(r0)     // Catch:{ all -> 0x0065 }
            L_0x0063:
                monitor-exit(r4)
                return
            L_0x0065:
                r0 = move-exception
                monitor-exit(r4)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.view.WXVideoView.Wrapper.createVideoView():void");
        }

        @SuppressLint({"NewApi"})
        private void removeSelfFromViewTreeObserver() {
            if (Build.VERSION.SDK_INT >= 16) {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }

        public boolean createVideoViewIfVisible() {
            Rect rect = new Rect();
            if (this.mVideoView != null) {
                return true;
            }
            if (!getGlobalVisibleRect(rect) || rect.isEmpty()) {
                return false;
            }
            createVideoView();
            return true;
        }

        public void onGlobalLayout() {
            if (createVideoViewIfVisible()) {
                removeSelfFromViewTreeObserver();
            }
        }
    }
}
