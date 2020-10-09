package com.alimama.unwdinamicxcontainer.diywidget;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alimama.unwdinamicxcontainer.R;
import com.alimama.unwdinamicxcontainer.diywidget.model.CountDownTipItem;

public class DXCLimitRobCountDownTipView extends LinearLayout {
    /* access modifiers changed from: private */
    public Handler mHandler;
    private CountDownTipItem mItem;
    private TextView mTitle;

    public DXCLimitRobCountDownTipView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DXCLimitRobCountDownTipView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private void initView() {
        setOrientation(0);
        this.mTitle = (TextView) inflate(getContext(), R.layout.count_down_tip_title, this).findViewById(R.id.count_down_tip_title_tv);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        startCountDown();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mHandler = null;
        }
    }

    public void notifyData(CountDownTipItem countDownTipItem) {
        this.mItem = countDownTipItem;
        if (countDownTipItem.endTime - System.currentTimeMillis() < 0) {
            setVisibility(8);
            return;
        }
        setVisibility(0);
        updateTitleText();
    }

    private void startCountDown() {
        if (this.mItem != null) {
            if (this.mHandler == null) {
                this.mHandler = new Handler();
            } else {
                this.mHandler.removeCallbacksAndMessages((Object) null);
            }
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    DXCLimitRobCountDownTipView.this.updateTitleText();
                    DXCLimitRobCountDownTipView.this.mHandler.postDelayed(this, 1000);
                }
            }, 1000);
        }
    }

    /* access modifiers changed from: private */
    public void updateTitleText() {
        String str;
        long currentTimeMillis = this.mItem.endTime - System.currentTimeMillis();
        if (currentTimeMillis < 0) {
            setVisibility(8);
            return;
        }
        String[] time = getTime(currentTimeMillis);
        if (TextUtils.equals("0", time[0])) {
            str = "";
        } else {
            str = time[0] + "天";
        }
        this.mTitle.setText(this.mItem.rightTips.replace("{{TIME}}", str + time[1] + ":" + time[2] + ":" + time[3]));
    }

    private String[] getTime(long j) {
        long j2 = j / 1000;
        int i = (int) (j2 / 86400);
        long j3 = j2 - ((long) (((i * 24) * 60) * 60));
        int i2 = (int) (j3 / 3600);
        long j4 = j3 - ((long) ((i2 * 60) * 60));
        int i3 = (int) (j4 / 60);
        return new String[]{String.valueOf(i), getTimeStamp(i2), getTimeStamp(i3), getTimeStamp((int) (j4 - ((long) (i3 * 60))))};
    }

    private String getTimeStamp(int i) {
        String valueOf = String.valueOf(i);
        if (valueOf.length() != 1) {
            return valueOf;
        }
        return "0" + valueOf;
    }

    public static class HomeCountDownItem {
        public long curTime;
        public long diffTime;
        public long endTime;
        public long startTime;

        public HomeCountDownItem(long j, long j2, long j3) {
            this.startTime = j;
            this.endTime = j2;
            this.curTime = j3;
            this.diffTime = System.currentTimeMillis() - j3;
        }
    }
}
