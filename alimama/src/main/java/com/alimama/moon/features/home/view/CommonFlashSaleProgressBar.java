package com.alimama.moon.features.home.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CommonFlashSaleProgressBar extends ProgressBar {
    /* access modifiers changed from: private */
    public int mCurProgress = 0;
    /* access modifiers changed from: private */
    public int mDelayTime = 20;
    /* access modifiers changed from: private */
    public Handler mHandler = null;
    private boolean mIsProgressDone = false;

    public CommonFlashSaleProgressBar(Context context) {
        super(context);
        init();
    }

    public CommonFlashSaleProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CommonFlashSaleProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            public boolean handleMessage(Message message) {
                int i = message.what;
                if (i > CommonFlashSaleProgressBar.this.mCurProgress) {
                    return true;
                }
                CommonFlashSaleProgressBar.this.setSecondaryProgress(i);
                if (CommonFlashSaleProgressBar.this.mHandler == null) {
                    return true;
                }
                CommonFlashSaleProgressBar.this.mHandler.sendEmptyMessageDelayed(i + 2, (long) CommonFlashSaleProgressBar.this.mDelayTime);
                return true;
            }
        });
        render();
    }

    private void deinit() {
        this.mHandler = null;
        this.mIsProgressDone = false;
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
