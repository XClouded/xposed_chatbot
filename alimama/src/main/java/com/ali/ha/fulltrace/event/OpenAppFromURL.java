package com.ali.ha.fulltrace.event;

import com.ali.ha.fulltrace.ByteUtils;
import com.ali.ha.fulltrace.IReportRawByteEvent;
import com.ali.ha.fulltrace.ProtocolConstants;
import com.ali.ha.fulltrace.TimeUtils;

public class OpenAppFromURL implements IReportRawByteEvent {
    public long time = TimeUtils.currentTimeMillis();
    public String url;

    public byte[] getBody() {
        if (this.url == null || this.url.length() == 0) {
            return ByteUtils.int2Bytes(0);
        }
        byte[] bytes = this.url.getBytes();
        return ByteUtils.merge(ByteUtils.int2Bytes(bytes.length), bytes);
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_OPEN_APP_FROM_URL;
    }
}
