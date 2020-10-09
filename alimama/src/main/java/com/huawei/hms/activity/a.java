package com.huawei.hms.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

/* compiled from: IBridgeActivityDelegate */
public interface a {
    void onBridgeActivityCreate(Activity activity);

    void onBridgeActivityDestroy();

    boolean onBridgeActivityResult(int i, int i2, Intent intent);

    void onBridgeConfigurationChanged();

    void onKeyUp(int i, KeyEvent keyEvent);
}
