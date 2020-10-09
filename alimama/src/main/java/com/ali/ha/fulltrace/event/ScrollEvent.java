package com.ali.ha.fulltrace.event;

import com.ali.ha.fulltrace.IReportRawByteEvent;
import com.ali.ha.fulltrace.TimeUtils;

public class ScrollEvent implements IReportRawByteEvent {
    public float beginX;
    public float beginY;
    public float endX;
    public float endY;
    public long time = TimeUtils.currentTimeMillis();

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
