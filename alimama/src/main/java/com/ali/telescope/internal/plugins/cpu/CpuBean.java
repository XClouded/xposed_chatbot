package com.ali.telescope.internal.plugins.cpu;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.List;

public class CpuBean implements IReportRawByteBean {
    private static final int CPU_STAT_BYTES = 12;
    public byte[] body;
    public long time;

    public CpuBean(long j, List<CpuRecord> list) {
        this.time = j;
        int size = list.size();
        if (size > 0) {
            int i = (size * 12) + 8;
            this.body = new byte[i];
            int fill = ByteUtils.fill(this.body, ByteUtils.int2Bytes(i), 0) + 0;
            int fill2 = fill + ByteUtils.fill(this.body, ByteUtils.int2Bytes(size), fill);
            for (int i2 = 0; i2 < size; i2++) {
                CpuRecord cpuRecord = list.get(i2);
                int fill3 = fill2 + ByteUtils.fill(this.body, ByteUtils.long2Bytes(cpuRecord.timeStamp), fill2);
                int fill4 = fill3 + ByteUtils.fill(this.body, ByteUtils.short2Bytes(cpuRecord.myPidCpuPercent), fill3);
                fill2 = fill4 + ByteUtils.fill(this.body, ByteUtils.short2Bytes(cpuRecord.sysTotalCpuPercent), fill4);
            }
        }
    }

    public CpuBean(long j, CpuRecord cpuRecord) {
        this.time = j;
        this.body = new byte[12];
        int fill = ByteUtils.fill(this.body, ByteUtils.long2Bytes(cpuRecord.timeStamp), 0) + 0;
        int fill2 = fill + ByteUtils.fill(this.body, ByteUtils.short2Bytes(cpuRecord.myPidCpuPercent), fill);
        ByteUtils.fill(this.body, ByteUtils.short2Bytes(cpuRecord.sysTotalCpuPercent), fill2);
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_CPU;
    }

    public byte[] getBody() {
        return this.body;
    }
}
