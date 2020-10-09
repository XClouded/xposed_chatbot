package com.alimama.moon.update;

import com.alibaba.android.update4mtl.data.ResponseUpdateInfo;
import com.alimama.moon.eventbus.IEvent;

public class NewVersionRemindEvent implements IEvent {
    private final ResponseUpdateInfo info;

    public NewVersionRemindEvent(ResponseUpdateInfo responseUpdateInfo) {
        this.info = responseUpdateInfo;
    }

    public ResponseUpdateInfo getInfo() {
        return this.info;
    }
}
