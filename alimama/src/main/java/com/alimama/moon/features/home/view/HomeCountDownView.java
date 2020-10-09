package com.alimama.moon.features.home.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import java.util.Calendar;

public class HomeCountDownView extends LinearLayout {
    private TextView mHour;
    private TextView mMin;
    private TextView mSec;
    /* access modifiers changed from: private */
    public Handler sHandler;

    public HomeCountDownView(Context context) {
        super(context);
        initView();
    }

    public HomeCountDownView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public HomeCountDownView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    private void initView() {
        setOrientation(0);
        View inflate = inflate(getContext(), R.layout.home_count_down_view, this);
        this.mHour = (TextView) inflate.findViewById(R.id.home_count_down_hour);
        this.mMin = (TextView) inflate.findViewById(R.id.home_count_down_min);
        this.mSec = (TextView) inflate.findViewById(R.id.home_count_down_sec);
        startCountDown();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        startCountDown();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.sHandler != null) {
            this.sHandler.removeCallbacksAndMessages((Object) null);
            this.sHandler = null;
        }
    }

    private void startCountDown() {
        if (this.sHandler == null) {
            this.sHandler = new Handler();
        } else {
            this.sHandler.removeCallbacksAndMessages((Object) null);
            this.sHandler = new Handler();
        }
        updateTime();
        this.sHandler.postDelayed(new Runnable() {
            public void run() {
                HomeCountDownView.this.updateTime();
                HomeCountDownView.this.sHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    /* access modifiers changed from: private */
    public void updateTime() {
        String[] countDownClock = getCountDownClock();
        this.mHour.setText(countDownClock[0]);
        this.mMin.setText(countDownClock[1]);
        this.mSec.setText(countDownClock[2]);
    }

    private String[] getCountDownClock() {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(11);
        int i2 = instance.get(12);
        int i3 = 60 - instance.get(13);
        int i4 = 59 - i2;
        int i5 = 23 - i;
        if (i3 == 60) {
            i4++;
            if (i4 == 60) {
                i5++;
                i3 = 0;
                i4 = 0;
            } else {
                i3 = 0;
            }
        }
        return new String[]{getTimeStamp(i5), getTimeStamp(i4), getTimeStamp(i3)};
    }

    private String getTimeStamp(int i) {
        String valueOf = String.valueOf(i);
        if (valueOf.length() != 1) {
            return valueOf;
        }
        return "0" + valueOf;
    }
}
