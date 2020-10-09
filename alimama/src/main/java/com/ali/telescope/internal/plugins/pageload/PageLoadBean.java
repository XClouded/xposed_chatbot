package com.ali.telescope.internal.plugins.pageload;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.List;

public class PageLoadBean implements IReportRawByteBean {
    public byte[] body;
    public long time;

    public PageLoadBean(long j, List<PageLoadRecord> list) {
        this.time = j;
        int size = list.size();
        if (size > 0) {
            try {
                int i = 0;
                for (PageLoadRecord pageLoadRecord : list) {
                    i = i + 4 + pageLoadRecord.pageName.getBytes().length + 4 + 4 + 8;
                }
                this.body = new byte[(16 + i)];
                int fill = ByteUtils.fill(this.body, ByteUtils.long2Bytes(this.time), 0) + 0;
                int fill2 = fill + ByteUtils.fill(this.body, ByteUtils.int2Bytes(i), fill);
                int fill3 = fill2 + ByteUtils.fill(this.body, ByteUtils.int2Bytes(size), fill2);
                for (PageLoadRecord next : list) {
                    fill3 += ByteUtils.fill(this.body, ByteUtils.merge(ByteUtils.int2Bytes(next.pageName.getBytes().length), next.pageName.getBytes(), ByteUtils.int2Bytes(next.pageLoadTime), ByteUtils.int2Bytes(next.pageStayTime), ByteUtils.long2Bytes(next.pageStartTime)), fill3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ACTIVITY_OPEN_PREF;
    }

    public byte[] getBody() {
        return this.body;
    }
}
