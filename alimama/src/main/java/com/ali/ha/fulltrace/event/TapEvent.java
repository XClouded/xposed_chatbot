package com.ali.ha.fulltrace.event;

import com.ali.ha.fulltrace.IReportRawByteEvent;
import com.ali.ha.fulltrace.TimeUtils;

public class TapEvent implements IReportRawByteEvent {
    public boolean isLongTouch;
    public long time = TimeUtils.currentTimeMillis();
    public float x;
    public float y;

    public long getTime() {
        return 0;
    }

    public short getType() {
        return 0;
    }

    public byte[] getBody() {
        return new byte[0];
    }
}
