package com.taobao.android.dinamicx.widget.event;

import android.text.TextUtils;
import java.util.Map;

public class DXControlEvent {
    public Map<Object, Object> args;
    public String eventName;
    public Object sender;

    public Object getSender() {
        return this.sender;
    }

    public void setSender(Object obj) {
        this.sender = obj;
    }

    public boolean equals(DXControlEvent dXControlEvent) {
        return dXControlEvent != null && !TextUtils.isEmpty(this.eventName) && this.eventName.equals(dXControlEvent.eventName) && this.sender == dXControlEvent.sender;
    }
}
