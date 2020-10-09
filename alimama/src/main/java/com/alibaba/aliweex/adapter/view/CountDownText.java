package com.alibaba.aliweex.adapter.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXViewUtils;

public class CountDownText implements Runnable {
    private String dataFormat;
    private long day;
    private int dayIndex = -1;
    private long hour;
    private int hourIndex = -1;
    private TextView mCountDownView;
    private Handler mHandler;
    private long min;
    private int minIndex = -1;
    private int oneDay = 86400;
    private int oneHour = 3600;
    private int oneMin = 60;
    private long remainingTime;
    private boolean reset;
    private long sec;
    private int secIndex = -1;
    private String textColor;
    private int textSize = 22;
    private String timeColor;
    private int timeSize = 22;

    public CountDownText(Context context) {
        init(context);
    }

    public TextView getView() {
        return this.mCountDownView;
    }

    private void init(Context context) {
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mCountDownView = new TextView(context);
        this.mCountDownView.setGravity(17);
        this.mCountDownView.setIncludeFontPadding(false);
    }

    public void setTime(String str) {
        this.reset = true;
        try {
            this.remainingTime = Long.parseLong(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setDateFormat(String str) {
        this.dataFormat = str;
        this.dayIndex = str.indexOf("dd");
        this.hourIndex = str.indexOf("hh");
        this.minIndex = str.indexOf("mm");
        this.secIndex = str.indexOf("ss");
    }

    public void start() {
        this.reset = false;
        run();
    }

    public void run() {
        if (this.reset) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.reset = false;
            this.mHandler.post(this);
            return;
        }
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.remainingTime--;
        if (!WXViewUtils.onScreenArea(this.mCountDownView)) {
            this.mHandler.postDelayed(this, 1000);
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("countdown stop");
                return;
            }
            return;
        }
        computeTime();
        setCountTimeText();
        if (this.remainingTime >= 0) {
            this.mHandler.postDelayed(this, 1000);
        }
    }

    private void setCountTimeText() {
        String str;
        String str2;
        String str3;
        String str4;
        if (!TextUtils.isEmpty(this.dataFormat)) {
            String str5 = new String(this.dataFormat);
            if (this.day >= 0 && this.day < 10) {
                str = "0" + this.day;
            } else if (this.day < 0) {
                str = "00";
            } else {
                str = String.valueOf(this.day);
            }
            String replace = str5.replace("dd", str);
            if (this.hour >= 0 && this.hour < 10) {
                str2 = "0" + this.hour;
            } else if (this.hour < 0) {
                str2 = "00";
            } else {
                str2 = String.valueOf(this.hour);
            }
            String replace2 = replace.replace("hh", str2);
            if (this.min >= 0 && this.min < 10) {
                str3 = "0" + this.min;
            } else if (this.min < 0) {
                str3 = "00";
            } else {
                str3 = String.valueOf(this.min);
            }
            String replace3 = replace2.replace("mm", str3);
            if (this.sec >= 0 && this.sec < 10) {
                str4 = "0" + this.sec;
            } else if (this.sec < 0) {
                str4 = "00";
            } else {
                str4 = String.valueOf(this.sec);
            }
            String replace4 = replace3.replace("ss", str4);
            SpannableString spannableString = new SpannableString(replace4);
            if (this.dayIndex > -1) {
                spannableString.setSpan(new ForegroundColorSpan(getTextColor()), 0, this.dayIndex, 34);
                spannableString.setSpan(new AbsoluteSizeSpan(this.textSize, false), this.dayIndex, this.dayIndex + 2, 33);
                spannableString.setSpan(new ForegroundColorSpan(getTimeColor()), this.dayIndex, this.dayIndex + 2, 34);
                spannableString.setSpan(new StyleSpan(1), this.dayIndex, this.dayIndex + 2, 33);
                spannableString.setSpan(new AbsoluteSizeSpan(this.timeSize, false), this.dayIndex, this.dayIndex + 2, 33);
                spannableString.setSpan(new ForegroundColorSpan(getTextColor()), this.dayIndex + 2, this.hourIndex, 34);
                spannableString.setSpan(new AbsoluteSizeSpan(this.textSize, false), this.dayIndex + 2, this.hourIndex, 33);
            } else {
                spannableString.setSpan(new ForegroundColorSpan(getTextColor()), 0, this.hourIndex, 34);
                spannableString.setSpan(new AbsoluteSizeSpan(this.textSize, false), this.hourIndex, this.hourIndex + 2, 33);
            }
            spannableString.setSpan(new ForegroundColorSpan(getTimeColor()), this.hourIndex, this.hourIndex + 2, 34);
            spannableString.setSpan(new StyleSpan(1), this.hourIndex, this.hourIndex + 2, 33);
            spannableString.setSpan(new AbsoluteSizeSpan(this.timeSize), this.hourIndex, this.hourIndex + 2, 33);
            spannableString.setSpan(new ForegroundColorSpan(getTextColor()), this.hourIndex + 2, this.minIndex, 34);
            spannableString.setSpan(new AbsoluteSizeSpan(this.textSize, false), this.hourIndex + 2, this.minIndex, 33);
            spannableString.setSpan(new ForegroundColorSpan(getTimeColor()), this.minIndex, this.minIndex + 2, 34);
            spannableString.setSpan(new StyleSpan(1), this.minIndex, this.minIndex + 2, 33);
            spannableString.setSpan(new AbsoluteSizeSpan(this.timeSize), this.minIndex, this.minIndex + 2, 33);
            spannableString.setSpan(new ForegroundColorSpan(getTextColor()), this.minIndex + 2, this.secIndex, 34);
            spannableString.setSpan(new AbsoluteSizeSpan(this.textSize, false), this.minIndex + 2, this.secIndex, 33);
            spannableString.setSpan(new ForegroundColorSpan(getTimeColor()), this.secIndex, this.secIndex + 2, 34);
            spannableString.setSpan(new StyleSpan(1), this.secIndex, this.secIndex + 2, 33);
            spannableString.setSpan(new AbsoluteSizeSpan(this.timeSize, false), this.secIndex, this.secIndex + 2, 33);
            spannableString.setSpan(new ForegroundColorSpan(getTextColor()), this.secIndex + 2, this.dataFormat.length(), 34);
            spannableString.setSpan(new AbsoluteSizeSpan(this.textSize, false), this.secIndex + 2, replace4.length(), 33);
            this.mCountDownView.setText(spannableString);
        }
    }

    private void computeTime() {
        this.day = this.remainingTime / ((long) this.oneDay);
        this.hour = (this.remainingTime - (this.day * ((long) this.oneDay))) / ((long) this.oneHour);
        this.min = ((this.remainingTime - (this.day * ((long) this.oneDay))) - (this.hour * ((long) this.oneHour))) / ((long) this.oneMin);
        this.sec = (((this.remainingTime - (this.day * ((long) this.oneDay))) - (this.hour * ((long) this.oneHour))) - (this.min * ((long) this.oneMin))) % 60;
    }

    public void setTimeColor(String str) {
        this.timeColor = str;
    }

    public void setCountDownTextColor(String str) {
        this.textColor = str;
    }

    public int getTimeColor() {
        int color;
        if (TextUtils.isEmpty(this.timeColor) || !this.timeColor.startsWith("#") || (color = WXResourceUtils.getColor(this.timeColor)) == Integer.MIN_VALUE) {
            return 0;
        }
        return color;
    }

    public int getTextColor() {
        int color;
        if (TextUtils.isEmpty(this.textColor) || !this.textColor.startsWith("#") || (color = WXResourceUtils.getColor(this.textColor)) == Integer.MIN_VALUE) {
            return 0;
        }
        return color;
    }

    public void setFontSize(int i, int i2) {
        this.textSize = i2;
    }

    public void setTimeFontSize(int i, int i2) {
        this.timeSize = i2;
    }

    public void destroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mHandler.removeCallbacks((Runnable) null);
        }
    }
}
