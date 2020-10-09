package com.taobao.application.common.data;

public class ActivityCountHelper extends AbstractHelper {
    public void setActivityCount(int i) {
        this.preferences.putInt("aliveActivityCount", i);
    }
}
