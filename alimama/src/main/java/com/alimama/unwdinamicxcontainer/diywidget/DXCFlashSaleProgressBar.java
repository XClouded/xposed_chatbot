package com.alimama.unwdinamicxcontainer.diywidget;

import alimama.com.unwviewbase.tool.DensityUtil;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.alimama.unwdinamicxcontainer.R;

public class DXCFlashSaleProgressBar extends ProgressBar {
    /* access modifiers changed from: private */
    public int mCurProgress = 0;
    /* access modifiers changed from: private */
    public int mDelayTime = 20;
    /* access modifiers changed from: private */
    public Handler mHandler = null;
    private boolean mIsProgressDone = false;

    public DXCFlashSaleProgressBar(Context context) {
        super(context);
        init();
    }

    public DXCFlashSaleProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public DXCFlashSaleProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void setDelayTime(int i) {
        this.mDelayTime = i;
    }

    private void init() {
        this.mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            public boolean handleMessage(Message message) {
                int i = message.what;
                if (i > DXCFlashSaleProgressBar.this.mCurProgress) {
                    return true;
                }
                DXCFlashSaleProgressBar.this.setProgress(i);
                DXCFlashSaleProgressBar.this.setSecondaryProgress(i);
                if (DXCFlashSaleProgressBar.this.mHandler == null) {
                    return true;
                }
                DXCFlashSaleProgressBar.this.mHandler.sendEmptyMessageDelayed(i + 2, (long) DXCFlashSaleProgressBar.this.mDelayTime);
                return true;
            }
        });
        render();
    }

    private void deinit() {
        this.mHandler = null;
        this.mIsProgressDone = false;
    }

    private void changeProgressDrawable() {
        if (isMValid()) {
            LayerDrawable layerDrawable = (LayerDrawable) getProgressDrawable();
            BitmapShader bitmapShader = new BitmapShader(((BitmapDrawable) getResources().getDrawable(R.drawable.common_flash_sale_progress_indicator)).getBitmap(), Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            ShapeDrawable shapeDrawable = new ShapeDrawable(getCustomDrawableShape());
            shapeDrawable.getPaint().setShader(bitmapShader);
            ((ScaleDrawable) layerDrawable.findDrawableByLayerId(layerDrawable.getId(1))).setDrawable(shapeDrawable);
        }
    }

    private boolean isMValid() {
        return Build.VERSION.SDK_INT >= 23;
    }

    private Shape getCustomDrawableShape() {
        int dimension = (int) getResources().getDimension(R.dimen.common_padding_8);
        float[] fArr = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        for (int i = 0; i < fArr.length; i++) {
            fArr[i] = (float) DensityUtil.dip2px(getContext(), (float) dimension);
        }
        return new RoundRectShape(fArr, (RectF) null, fArr);
    }

    private void render() {
        if (this.mCurProgress >= 0 && this.mHandler != null && !this.mIsProgressDone) {
            this.mHandler.sendEmptyMessageAtTime(0, (long) this.mDelayTime);
            this.mIsProgressDone = true;
        }
    }

    public void setCurProgress(int i) {
        this.mCurProgress = i;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        deinit();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }
}
