package com.ali.telescope.internal.plugins.appevent;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.HashMap;

public class ActivityEventBean implements IReportRawByteBean {
    private static HashMap<Integer, Short> sMap = new HashMap<>();
    public byte[] body;
    public String pageHashCode;
    public String pageName;
    public long time;
    public short type;

    static {
        sMap.put(1, Short.valueOf(ProtocolConstants.EVENT_ACTIVITY_CREATE));
        sMap.put(2, Short.valueOf(ProtocolConstants.EVENT_ACTIVITY_START));
        sMap.put(3, Short.valueOf(ProtocolConstants.EVENT_ACTIVITY_RESUME));
        sMap.put(4, Short.valueOf(ProtocolConstants.EVENT_ACTIVITY_PAUSE));
        sMap.put(5, Short.valueOf(ProtocolConstants.EVENT_ACTIVITY_STOP));
        sMap.put(6, Short.valueOf(ProtocolConstants.EVENT_ACTIVITY_DESOTRY));
    }

    public ActivityEventBean(long j, String str, String str2, int i) {
        this.time = j;
        this.pageName = str == null ? "" : str;
        this.pageHashCode = str2 == null ? "" : str2;
        this.type = sMap.get(Integer.valueOf(i)).shortValue();
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return this.type;
    }

    public byte[] getBody() {
        return ByteUtils.merge(ByteUtils.int2Bytes(this.pageName.getBytes().length), this.pageName.getBytes(), ByteUtils.int2Bytes(this.pageHashCode.getBytes().length), this.pageHashCode.getBytes());
    }
}
