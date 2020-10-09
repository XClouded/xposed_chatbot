package com.taobao.uikit.extend.component;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import androidx.core.content.ContextCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.taobao.uikit.extend.R;

public class TBProgressBar extends ProgressBar {
    /* access modifiers changed from: private */
    public int mCurrentProgress;
    private ValueAnimator mDismissAlphaAnimator;
    private ValueAnimator mLightAnimator;
    /* access modifiers changed from: private */
    public int mLightStartX;
    private ValueAnimator mProgressAnimator;
    /* access modifiers changed from: private */
    public Bitmap mProgressLight;
    private int mScreenWidth;

    public TBProgressBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public TBProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public TBProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurrentProgress = 0;
        this.mLightStartX = 0;
        setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.uik_progress_drawable));
        new AsyncTask<Resources, Void, Bitmap>() {
            /* access modifiers changed from: protected */
            public Bitmap doInBackground(Resources... resourcesArr) {
                Bitmap decodeResource = BitmapFactory.decodeResource(resourcesArr[0], R.drawable.uik_progress_light);
                return decodeResource != null ? Bitmap.createScaledBitmap(decodeResource, (int) resourcesArr[0].getDimension(R.dimen.uik_progress_light_width), (int) resourcesArr[0].getDimension(R.dimen.uik_progress_light_height), false) : decodeResource;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                Bitmap unused = TBProgressBar.this.mProgressLight = bitmap;
            }
        }.execute(new Resources[]{getResources()});
        if (this.mProgressLight != null && this.mLightAnimator == null) {
            Display defaultDisplay = ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            this.mScreenWidth = point.x;
            this.mLightAnimator = ValueAnimator.ofInt(new int[]{0, this.mScreenWidth});
            this.mLightAnimator.setInterpolator(PathInterpolatorCompat.create(0.42f, 0.0f, 0.58f, 1.0f));
            this.mLightAnimator.setDuration(1600);
            this.mLightAnimator.setRepeatMode(1);
            this.mLightAnimator.setRepeatCount(Integer.MAX_VALUE);
        }
        if (this.mLightAnimator != null) {
            this.mLightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int unused = TBProgressBar.this.mLightStartX = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    TBProgressBar.this.invalidate();
                }
            });
            this.mLightAnimator.start();
        }
        double max = (double) getMax();
        Double.isNaN(max);
        setCurrentProgress((int) (max * 0.02d));
    }

    public synchronized void setCurrentProgress(int i) {
        if (i >= getProgress()) {
            if (this.mProgressAnimator != null && this.mProgressAnimator.isRunning()) {
                this.mProgressAnimator.cancel();
            }
            this.mProgressAnimator = ValueAnimator.ofInt(new int[]{this.mCurrentProgress, i});
            this.mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    TBProgressBar.this.setProgress(((Integer) valueAnimator.getAnimatedValue()).intValue());
                    if (TBProgressBar.this.getProgress() == TBProgressBar.this.getMax()) {
                        TBProgressBar.this.dismiss();
                    }
                }
            });
            this.mProgressAnimator.addListener(new Animator.AnimatorListener() {
                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    int unused = TBProgressBar.this.mCurrentProgress = TBProgressBar.this.getProgress();
                }

                public void onAnimationCancel(Animator animator) {
                    int unused = TBProgressBar.this.mCurrentProgress = TBProgressBar.this.getProgress();
                }
            });
            this.mProgressAnimator.setInterpolator(PathInterpolatorCompat.create(0.0f, 0.0f, 0.2f, 1.0f));
            this.mProgressAnimator.setDuration(1000);
            this.mProgressAnimator.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            if (this.mProgressAnimator != null) {
                this.mProgressAnimator.start();
            }
            if (this.mLightAnimator != null) {
                this.mLightAnimator.start();
                return;
            }
            return;
        }
        if (this.mProgressAnimator != null) {
            this.mProgressAnimator.end();
        }
        if (this.mLightAnimator != null) {
            this.mLightAnimator.end();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (this.mProgressAnimator != null) {
            this.mProgressAnimator.removeAllUpdateListeners();
            this.mProgressAnimator.end();
        }
        if (this.mLightAnimator != null) {
            this.mLightAnimator.removeAllUpdateListeners();
            this.mLightAnimator.end();
        }
        if (this.mDismissAlphaAnimator != null) {
            this.mDismissAlphaAnimator.removeAllUpdateListeners();
            this.mDismissAlphaAnimator.end();
        }
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void dismiss() {
        if (this.mDismissAlphaAnimator == null) {
            this.mDismissAlphaAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            this.mDismissAlphaAnimator.setDuration(200);
            this.mDismissAlphaAnimator.setInterpolator(PathInterpolatorCompat.create(0.0f, 0.0f, 0.2f, 1.0f));
            this.mDismissAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    TBProgressBar.this.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    if (TBProgressBar.this.getAlpha() == 0.0f) {
                        TBProgressBar.this.setVisibility(4);
                    }
                }
            });
        }
        if (!this.mDismissAlphaAnimator.isRunning()) {
            this.mDismissAlphaAnimator.start();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float progress = ((float) getProgress()) / ((float) getMax());
        if (progress > 0.0f && this.mProgressLight != null && ((float) this.mLightStartX) < (((float) this.mScreenWidth) * progress) - ((float) (this.mProgressLight.getWidth() * 2))) {
            canvas.drawBitmap(this.mProgressLight, (float) this.mLightStartX, 0.0f, (Paint) null);
        }
    }

    public void resetProgress() {
        setVisibility(0);
        setAlpha(1.0f);
        invalidate();
        setProgress(0);
        this.mCurrentProgress = 0;
        if (this.mLightAnimator != null && !this.mLightAnimator.isRunning()) {
            this.mLightAnimator.start();
        }
        double max = (double) getMax();
        Double.isNaN(max);
        setCurrentProgress((int) (max * 0.02d));
    }
}
