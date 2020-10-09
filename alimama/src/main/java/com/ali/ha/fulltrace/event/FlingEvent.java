package com.ali.ha.fulltrace.event;

import com.ali.ha.fulltrace.IReportRawByteEvent;
import com.ali.ha.fulltrace.TimeUtils;

public class FlingEvent implements IReportRawByteEvent {
    public byte direction;
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
