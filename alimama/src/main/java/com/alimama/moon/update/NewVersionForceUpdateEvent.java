package com.alimama.moon.update;

import com.alibaba.android.update4mtl.data.ResponseUpdateInfo;
import com.alimama.moon.eventbus.IEvent;

public class NewVersionForceUpdateEvent implements IEvent {
    private final ResponseUpdateInfo info;

    public NewVersionForceUpdateEvent(ResponseUpdateInfo responseUpdateInfo) {
        this.info = responseUpdateInfo;
    }

    public ResponseUpdateInfo getInfo() {
        return this.info;
    }
}
