package com.alimama.moon.ui.splashad;

import android.text.TextUtils;

public class SplashAdInfo {
    private String displayTime;
    private String fetchDelayTime;
    private String img;
    private String url;

    public boolean isPass() {
        return !TextUtils.isEmpty(this.img) && !TextUtils.isEmpty(this.displayTime) && !TextUtils.isEmpty(this.fetchDelayTime) && Integer.valueOf(this.displayTime).intValue() > 0 && Integer.valueOf(this.fetchDelayTime).intValue() > 0;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String str) {
        this.img = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getFetchDelayTime() {
        return this.fetchDelayTime;
    }

    public void setFetchDelayTime(String str) {
        this.fetchDelayTime = str;
    }

    public String getDisplayTime() {
        return this.displayTime;
    }

    public void setDisplayTime(String str) {
        this.displayTime = str;
    }
}
