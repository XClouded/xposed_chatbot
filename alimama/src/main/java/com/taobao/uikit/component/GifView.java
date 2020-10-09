package com.taobao.uikit.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.view.TView;

public class GifView extends TView {
    private static final int SUBTLE_PARAM = 1;
    private boolean mAutoPlay;
    private int mCanvasHeight;
    private int mCanvasWidth;
    private DecodeTask mDecodeTask;
    /* access modifiers changed from: private */
    public OnDecodedListener mDecodedListener;
    private OnPlayEndListener mEndListener;
    /* access modifiers changed from: private */
    public String mGifFilePath;
    /* access modifiers changed from: private */
    public int mGifResId;
    private boolean mIsPlayed;
    private boolean mIsPlaying;
    /* access modifiers changed from: private */
    public Movie mMovie;
    /* access modifiers changed from: private */
    public int mMovieHeight;
    private long mMovieStart;
    /* access modifiers changed from: private */
    public int mMovieWidth;

    public interface OnDecodedListener {
        void OnDecoded();
    }

    public interface OnPlayEndListener {
        void OnPlayEnd();
    }

    public GifView(Context context) {
        this(context, (AttributeSet) null);
    }

    public GifView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @TargetApi(11)
    public GifView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, (Paint) null);
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.GifView);
        if (obtainStyledAttributes != null) {
            this.mAutoPlay = obtainStyledAttributes.getBoolean(R.styleable.GifView_uik_auto_play, false);
            this.mGifResId = obtainStyledAttributes.getResourceId(R.styleable.GifView_uik_gif_src, 0);
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mMovie != null) {
            canvas.save();
            fixCanvas(canvas);
            if (this.mAutoPlay) {
                playMovie(canvas);
                invalidate();
            } else if (this.mIsPlaying) {
                if (playMovie(canvas)) {
                    this.mIsPlaying = false;
                    this.mIsPlayed = true;
                }
                invalidate();
            } else {
                if (this.mIsPlayed) {
                    this.mMovie.setTime(this.mMovie.duration() - 1);
                } else {
                    this.mMovie.setTime(0);
                }
                this.mMovie.draw(canvas, 0.0f, 0.0f);
                if (this.mIsPlayed && this.mEndListener != null) {
                    this.mEndListener.OnPlayEnd();
                }
            }
            canvas.restore();
        }
    }

    private void fixCanvas(Canvas canvas) {
        float f;
        float f2;
        float f3;
        int i = this.mMovieWidth;
        int i2 = this.mMovieHeight;
        int i3 = this.mCanvasWidth;
        int i4 = this.mCanvasHeight;
        if (i * i4 > i3 * i2) {
            f = ((float) i4) / ((float) i2);
            f3 = (((float) i3) - (((float) i) * f)) * 0.5f;
            f2 = 0.0f;
        } else {
            float f4 = ((float) i3) / ((float) i);
            f2 = (((float) i4) - (((float) i2) * f4)) * 0.5f;
            f = f4;
            f3 = 0.0f;
        }
        canvas.translate((float) ((int) (f3 + 0.5f)), (float) ((int) (f2 + 0.5f)));
        canvas.scale(f, f);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4 = 1;
        if (this.mMovie == null) {
            this.mMovieWidth = -1;
            this.mMovieHeight = -1;
            i4 = 0;
            i3 = 0;
        } else {
            i3 = this.mMovieWidth;
            int i5 = this.mMovieHeight;
            if (i3 <= 0) {
                i3 = 1;
            }
            if (i5 > 0) {
                i4 = i5;
            }
        }
        this.mCanvasWidth = resolveSize(i3, i);
        this.mCanvasHeight = resolveSize(i4, i2);
        setMeasuredDimension(this.mCanvasWidth, this.mCanvasHeight);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        decode();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (this.mDecodeTask != null) {
            this.mDecodeTask.cancel(true);
        }
        this.mMovie = null;
        super.onDetachedFromWindow();
    }

    private boolean playMovie(Canvas canvas) {
        long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        if (this.mMovieStart == 0) {
            this.mMovieStart = currentAnimationTimeMillis;
        }
        boolean z = true;
        int duration = this.mMovie.duration() - 1;
        if (duration == 0) {
            duration = 1000;
        }
        int i = (int) (currentAnimationTimeMillis - this.mMovieStart);
        if (i >= duration) {
            this.mMovieStart = 0;
            i = duration;
        } else {
            z = false;
        }
        this.mMovie.setTime(i);
        this.mMovie.draw(canvas, 0.0f, 0.0f);
        return z;
    }

    public void setGifResource(int i) {
        if (i != 0 && i != this.mGifResId) {
            this.mGifResId = i;
            this.mGifFilePath = null;
            decode();
        }
    }

    public void setGifFilePath(String str) {
        if (str != null && !TextUtils.equals(str, this.mGifFilePath)) {
            this.mGifFilePath = str;
            this.mGifResId = 0;
            decode();
        }
    }

    private void decode() {
        if (this.mMovie == null) {
            if (this.mDecodeTask != null) {
                this.mDecodeTask.cancel(true);
            }
            this.mDecodeTask = new DecodeTask();
            this.mDecodeTask.execute(new Void[0]);
        }
    }

    public void play() {
        this.mMovieStart = 0;
        this.mIsPlaying = true;
        this.mIsPlayed = false;
        this.mAutoPlay = false;
        invalidate();
    }

    public void autoPlay() {
        this.mAutoPlay = true;
        invalidate();
    }

    public void stop() {
        this.mIsPlaying = false;
        this.mAutoPlay = false;
        this.mIsPlayed = false;
        invalidate();
    }

    public void setPlayEndListener(OnPlayEndListener onPlayEndListener) {
        this.mEndListener = onPlayEndListener;
    }

    public void setDecodedListener(OnDecodedListener onDecodedListener) {
        this.mDecodedListener = onDecodedListener;
    }

    private class DecodeTask extends AsyncTask<Void, Void, Boolean> {
        private DecodeTask() {
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0050 A[SYNTHETIC, Splitter:B:18:0x0050] */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x0082 A[SYNTHETIC, Splitter:B:24:0x0082] */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0093 A[SYNTHETIC, Splitter:B:33:0x0093] */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x009d A[SYNTHETIC, Splitter:B:38:0x009d] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Boolean doInBackground(java.lang.Void... r6) {
            /*
                r5 = this;
                r6 = 0
                r0 = 0
                com.taobao.uikit.component.GifView r1 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                int r1 = r1.mGifResId     // Catch:{ Exception -> 0x008d }
                if (r1 == 0) goto L_0x001c
                com.taobao.uikit.component.GifView r1 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                android.content.res.Resources r1 = r1.getResources()     // Catch:{ Exception -> 0x008d }
                com.taobao.uikit.component.GifView r2 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                int r2 = r2.mGifResId     // Catch:{ Exception -> 0x008d }
                java.io.InputStream r1 = r1.openRawResource(r2)     // Catch:{ Exception -> 0x008d }
            L_0x001a:
                r0 = r1
                goto L_0x004e
            L_0x001c:
                com.taobao.uikit.component.GifView r1 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                java.lang.String r1 = r1.mGifFilePath     // Catch:{ Exception -> 0x008d }
                boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x008d }
                if (r1 != 0) goto L_0x004e
                java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x008d }
                java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x008d }
                com.taobao.uikit.component.GifView r3 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                java.lang.String r3 = r3.mGifFilePath     // Catch:{ Exception -> 0x008d }
                r2.<init>(r3)     // Catch:{ Exception -> 0x008d }
                r1.<init>(r2)     // Catch:{ Exception -> 0x008d }
                boolean r0 = r1.markSupported()     // Catch:{ Exception -> 0x0049, all -> 0x0046 }
                if (r0 == 0) goto L_0x001a
                int r0 = r1.available()     // Catch:{ Exception -> 0x0049, all -> 0x0046 }
                r1.mark(r0)     // Catch:{ Exception -> 0x0049, all -> 0x0046 }
                goto L_0x001a
            L_0x0046:
                r6 = move-exception
                r0 = r1
                goto L_0x009b
            L_0x0049:
                r0 = move-exception
                r4 = r1
                r1 = r0
                r0 = r4
                goto L_0x008e
            L_0x004e:
                if (r0 == 0) goto L_0x0080
                com.taobao.uikit.component.GifView r1 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                android.graphics.Movie r2 = android.graphics.Movie.decodeStream(r0)     // Catch:{ Exception -> 0x008d }
                android.graphics.Movie unused = r1.mMovie = r2     // Catch:{ Exception -> 0x008d }
                com.taobao.uikit.component.GifView r1 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                android.graphics.Movie r1 = r1.mMovie     // Catch:{ Exception -> 0x008d }
                if (r1 == 0) goto L_0x007f
                com.taobao.uikit.component.GifView r1 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                com.taobao.uikit.component.GifView r2 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                android.graphics.Movie r2 = r2.mMovie     // Catch:{ Exception -> 0x008d }
                int r2 = r2.width()     // Catch:{ Exception -> 0x008d }
                int unused = r1.mMovieWidth = r2     // Catch:{ Exception -> 0x008d }
                com.taobao.uikit.component.GifView r1 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                com.taobao.uikit.component.GifView r2 = com.taobao.uikit.component.GifView.this     // Catch:{ Exception -> 0x008d }
                android.graphics.Movie r2 = r2.mMovie     // Catch:{ Exception -> 0x008d }
                int r2 = r2.height()     // Catch:{ Exception -> 0x008d }
                int unused = r1.mMovieHeight = r2     // Catch:{ Exception -> 0x008d }
            L_0x007f:
                r6 = 1
            L_0x0080:
                if (r0 == 0) goto L_0x0096
                r0.close()     // Catch:{ IOException -> 0x0086 }
                goto L_0x0096
            L_0x0086:
                r0 = move-exception
                r0.printStackTrace()
                goto L_0x0096
            L_0x008b:
                r6 = move-exception
                goto L_0x009b
            L_0x008d:
                r1 = move-exception
            L_0x008e:
                r1.printStackTrace()     // Catch:{ all -> 0x008b }
                if (r0 == 0) goto L_0x0096
                r0.close()     // Catch:{ IOException -> 0x0086 }
            L_0x0096:
                java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)
                return r6
            L_0x009b:
                if (r0 == 0) goto L_0x00a5
                r0.close()     // Catch:{ IOException -> 0x00a1 }
                goto L_0x00a5
            L_0x00a1:
                r0 = move-exception
                r0.printStackTrace()
            L_0x00a5:
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.component.GifView.DecodeTask.doInBackground(java.lang.Void[]):java.lang.Boolean");
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            if (bool.booleanValue()) {
                GifView.this.requestLayout();
                GifView.this.invalidate();
                if (GifView.this.mDecodedListener != null) {
                    GifView.this.mDecodedListener.OnDecoded();
                }
            }
        }
    }
}
