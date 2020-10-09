package com.ali.telescope.internal.plugins.smooth;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;

public class SmoothBean implements IReportRawByteBean {
    public int activityTotalBadSmCount;
    public int activityTotalBadSmUsedTime;
    public int activityTotalSmCount;
    public int activityTotalSmUsedTime;
    public int avgSm;
    public int dragFlingCount;
    public String pageHashCode;
    public String pageName;
    public long time;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_FPS;
    }

    public byte[] getBody() {
        if (this.pageName == null) {
            this.pageName = "";
        }
        if (this.pageHashCode == null) {
            this.pageHashCode = "";
        }
        return ByteUtils.merge(ByteUtils.short2Bytes((short) this.avgSm), ByteUtils.int2Bytes(this.pageName.length()), this.pageName.getBytes(), ByteUtils.int2Bytes(this.pageHashCode.length()), this.pageHashCode.getBytes(), ByteUtils.short2Bytes((short) this.dragFlingCount), ByteUtils.short2Bytes((short) this.activityTotalSmCount), ByteUtils.int2Bytes((short) this.activityTotalSmUsedTime), ByteUtils.short2Bytes((short) this.activityTotalBadSmCount), ByteUtils.int2Bytes((short) this.activityTotalBadSmUsedTime));
    }
}
