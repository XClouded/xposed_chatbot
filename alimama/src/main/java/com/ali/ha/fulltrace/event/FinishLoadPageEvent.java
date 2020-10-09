package com.ali.ha.fulltrace.event;

import com.ali.ha.fulltrace.ByteUtils;
import com.ali.ha.fulltrace.IReportRawByteEvent;
import com.ali.ha.fulltrace.ProtocolConstants;
import com.ali.ha.fulltrace.TimeUtils;

public class FinishLoadPageEvent implements IReportRawByteEvent {
    public float cpuUsageOfApp;
    public float cpuUsageOfDevice;
    public float duration;
    public float freeMemory;
    public String pageName;
    public float residentMemory;
    public long time = TimeUtils.currentTimeMillis();
    public float virtualMemory;

    public byte[] getBody() {
        return ByteUtils.merge(ByteUtils.int2Bytes(this.pageName == null ? 0 : this.pageName.length()), this.pageName == null ? new byte[0] : this.pageName.getBytes(), ByteUtils.floatToBytes(this.duration), ByteUtils.floatToBytes(this.freeMemory), ByteUtils.floatToBytes(this.residentMemory), ByteUtils.floatToBytes(this.virtualMemory), ByteUtils.floatToBytes(this.cpuUsageOfApp), ByteUtils.floatToBytes(this.cpuUsageOfDevice));
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_FINISH_LOAD_PAGE;
    }
}
