package com.ali.telescope.internal.plugins.memory;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.List;

public class MemoryBean implements IReportRawByteBean {
    private static final int MEMORY_STAT_BYTES = 20;
    public byte[] body;
    public long time;

    public MemoryBean(long j, List<MemoryRecord> list) {
        this.time = j;
        int size = list.size();
        if (size > 0) {
            int i = (size * 20) + 8;
            this.body = new byte[i];
            int fill = ByteUtils.fill(this.body, ByteUtils.int2Bytes(i), 0) + 0;
            int fill2 = fill + ByteUtils.fill(this.body, ByteUtils.int2Bytes(size), fill);
            for (int i2 = 0; i2 < size; i2++) {
                MemoryRecord memoryRecord = list.get(i2);
                int fill3 = fill2 + ByteUtils.fill(this.body, ByteUtils.long2Bytes(memoryRecord.timeStamp), fill2);
                int fill4 = fill3 + ByteUtils.fill(this.body, ByteUtils.int2Bytes(memoryRecord.totalPss), fill3);
                int fill5 = fill4 + ByteUtils.fill(this.body, ByteUtils.int2Bytes(memoryRecord.nativePss), fill4);
                fill2 = fill5 + ByteUtils.fill(this.body, ByteUtils.int2Bytes(memoryRecord.dalvikPss), fill5);
            }
        }
    }

    public MemoryBean(long j, MemoryRecord memoryRecord) {
        this.time = j;
        this.body = new byte[20];
        int fill = ByteUtils.fill(this.body, ByteUtils.long2Bytes(memoryRecord.timeStamp), 0) + 0;
        int fill2 = fill + ByteUtils.fill(this.body, ByteUtils.int2Bytes(memoryRecord.totalPss), fill);
        int fill3 = fill2 + ByteUtils.fill(this.body, ByteUtils.int2Bytes(memoryRecord.nativePss), fill2);
        ByteUtils.fill(this.body, ByteUtils.int2Bytes(memoryRecord.dalvikPss), fill3);
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_MEMORY;
    }

    public byte[] getBody() {
        return this.body;
    }
}
