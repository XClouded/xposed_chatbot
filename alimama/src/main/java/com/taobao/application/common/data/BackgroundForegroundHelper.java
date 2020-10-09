package com.taobao.application.common.data;

public class BackgroundForegroundHelper extends AbstractHelper {
    public void setIsInBackground(boolean z) {
        this.preferences.putBoolean("isInBackground", z);
    }

    public void setIsInFullBackground(boolean z) {
        this.preferences.putBoolean("isFullInBackground", z);
    }
}
