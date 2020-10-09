package com.ali.ha.fulltrace.event;

import com.ali.ha.fulltrace.ByteUtils;
import com.ali.ha.fulltrace.IReportRawByteEvent;
import com.ali.ha.fulltrace.ProtocolConstants;
import com.ali.ha.fulltrace.TimeUtils;

public class FPSEvent implements IReportRawByteEvent {
    public float averageLoadFps;
    public float averageUseFps;
    public long time = TimeUtils.currentTimeMillis();

    public byte[] getBody() {
        return ByteUtils.merge(ByteUtils.floatToBytes(this.averageLoadFps), ByteUtils.floatToBytes(this.averageUseFps));
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_FPS;
    }
}
