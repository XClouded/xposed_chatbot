package com.alimama.unwdinamicxcontainer.diywidget.model;

import java.text.SimpleDateFormat;

public class CountDownTipItem {
    public long endTime;
    public String rightTips;

    public CountDownTipItem(String str, String str2) {
        this.rightTips = str2;
        try {
            this.endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str).getTime();
        } catch (Exception unused) {
        }
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long j) {
        this.endTime = j;
    }
}
