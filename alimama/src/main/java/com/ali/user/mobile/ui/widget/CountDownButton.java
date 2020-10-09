package com.ali.user.mobile.ui.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;
import com.ali.user.mobile.ui.R;

public class CountDownButton extends AppCompatButton {
    /* access modifiers changed from: private */
    public boolean isCountDowning;
    protected CountListener mCountListener;
    protected int mGetCodeTitleRes;
    protected int mTickTitleRes;
    private TimeCountDown mTimeCountDown;

    public interface CountListener {
        void onTick(long j);
    }

    public CountDownButton(Context context) {
        super(context);
        this.isCountDowning = false;
        this.isCountDowning = false;
    }

    public CountDownButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isCountDowning = false;
        this.isCountDowning = false;
    }

    public void startCountDown(long j, long j2) {
        this.mTimeCountDown = new TimeCountDown(j, j2);
        this.mTimeCountDown.start();
        this.isCountDowning = true;
    }

    public void setTickListener(CountListener countListener) {
        this.mCountListener = countListener;
    }

    public void cancelCountDown() {
        if (this.mTimeCountDown != null) {
            this.mTimeCountDown.cancel();
        }
        this.isCountDowning = false;
    }

    private class TimeCountDown extends CountDownTimer {
        TimeCountDown(long j, long j2) {
            super(j, j2);
        }

        public void onTick(long j) {
            long j2 = (j / 1000) + 1;
            if (CountDownButton.this.mTickTitleRes != 0) {
                CountDownButton.this.setText(CountDownButton.this.getResources().getString(CountDownButton.this.mTickTitleRes, new Object[]{String.valueOf(j2)}));
            } else {
                CountDownButton countDownButton = CountDownButton.this;
                countDownButton.setText(String.valueOf(j2) + CountDownButton.this.getContext().getString(R.string.aliuser_signup_verification_reGetCode));
            }
            CountDownButton.this.setEnabled(false);
            if (CountDownButton.this.mCountListener != null) {
                CountDownButton.this.mCountListener.onTick(j);
            }
        }

        public void onFinish() {
            if (CountDownButton.this.mGetCodeTitleRes != 0) {
                CountDownButton.this.setText(CountDownButton.this.getContext().getString(CountDownButton.this.mGetCodeTitleRes));
            } else {
                CountDownButton.this.setText(CountDownButton.this.getContext().getString(R.string.aliuser_signup_verification_getCode));
            }
            CountDownButton.this.setEnabled(true);
            boolean unused = CountDownButton.this.isCountDowning = false;
        }
    }

    public void setGetCodeTitle(int i) {
        this.mGetCodeTitleRes = i;
    }

    public void setTickTitleRes(int i) {
        this.mTickTitleRes = i;
    }

    public boolean isCountDowning() {
        return this.isCountDowning;
    }
}
