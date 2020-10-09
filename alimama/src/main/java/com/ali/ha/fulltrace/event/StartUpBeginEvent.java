package com.ali.ha.fulltrace.event;

import com.ali.ha.fulltrace.ByteUtils;
import com.ali.ha.fulltrace.IReportRawByteEvent;
import com.ali.ha.fulltrace.ProtocolConstants;
import com.ali.ha.fulltrace.TimeUtils;

public class StartUpBeginEvent implements IReportRawByteEvent {
    public boolean firstInstall;
    public boolean isBackgroundLaunch = false;
    public String launchType;
    public long time = TimeUtils.currentTimeMillis();

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_APP_START_UP_BEGIN;
    }

    public byte[] getBody() {
        byte[] bArr;
        byte[] bArr2 = {this.firstInstall ? (byte) 1 : 0, this.isBackgroundLaunch ? (byte) 1 : 0};
        if (this.launchType == null || this.launchType.length() == 0) {
            bArr = ByteUtils.int2Bytes(0);
        } else {
            byte[] bytes = this.launchType.getBytes();
            bArr = ByteUtils.merge(ByteUtils.int2Bytes(bytes.length), bytes);
        }
        return ByteUtils.merge(bArr2, bArr);
    }
}
