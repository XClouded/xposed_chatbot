package com.ali.telescope.internal.plugins.pageload;

import android.content.Context;
import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.plugins.cpu.CpuRecord;
import com.ali.telescope.internal.plugins.memory.MemoryRecord;
import com.ali.telescope.internal.plugins.memory.MemoryTracker;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import com.ali.telescope.util.process.CpuTracker;

public class PageOpenBean implements IReportRawByteBean {
    public CpuRecord cpuRecord;
    public MemoryRecord memoryRecord;
    public String page;
    public String pageHashCode;
    private long startTime;

    public PageOpenBean(Context context, long j, String str, String str2) {
        this.startTime = j;
        this.page = str == null ? "" : str;
        this.pageHashCode = str2 == null ? "" : str2;
        MemoryRecord cachedStatus = MemoryTracker.getCachedStatus();
        if (cachedStatus != null) {
            this.memoryRecord = cachedStatus;
        } else {
            this.memoryRecord = new MemoryRecord();
            this.memoryRecord.timeStamp = j;
            this.memoryRecord.dalvikPss = -1;
            this.memoryRecord.nativePss = -1;
            this.memoryRecord.totalPss = -1;
        }
        CpuRecord cpuStat = CpuTracker.getCpuStat();
        if (cpuStat != null) {
            this.cpuRecord = cpuStat;
            return;
        }
        this.cpuRecord = new CpuRecord();
        this.cpuRecord.timeStamp = j;
        this.cpuRecord.myPidCpuPercent = -1;
        this.cpuRecord.sysTotalCpuPercent = -1;
    }

    public long getTime() {
        return this.startTime;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ACTIVITY_PAGE_OPEN;
    }

    public byte[] getBody() {
        return ByteUtils.merge(ByteUtils.int2Bytes(this.page.getBytes().length), this.page.getBytes(), ByteUtils.int2Bytes(this.pageHashCode.getBytes().length), this.pageHashCode.getBytes(), ByteUtils.int2Bytes(this.memoryRecord.totalPss), ByteUtils.int2Bytes(this.memoryRecord.nativePss), ByteUtils.int2Bytes(this.memoryRecord.dalvikPss), ByteUtils.short2Bytes(this.cpuRecord.myPidCpuPercent), ByteUtils.short2Bytes(this.cpuRecord.sysTotalCpuPercent));
    }
}
