package com.ali.telescope.internal.plugins.startPref;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.plugins.cpu.CpuRecord;
import com.ali.telescope.internal.plugins.memory.MemoryRecord;
import com.ali.telescope.internal.plugins.memory.MemoryTracker;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import com.ali.telescope.util.process.CpuTracker;

public class StartUpEndBean implements IReportRawByteBean {
    public static long boot1EndTimeStamp;
    public static long boot1StartTimeStamp;
    public static long bootDuration1;
    public static long bootDuration2;
    public static byte bootType;
    public static boolean isColdBoot;
    static long preparePidTime;
    public CpuRecord cpuRecord;
    public MemoryRecord memoryRecord;
    private long starttime;

    public StartUpEndBean(long j) {
        this.starttime = j;
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
        return this.starttime;
    }

    public short getType() {
        return ProtocolConstants.EVENT_APP_START_UP_END;
    }

    public byte[] getBody() {
        byte[] long2Bytes = ByteUtils.long2Bytes(bootDuration1 + bootDuration2);
        byte[] long2Bytes2 = ByteUtils.long2Bytes(boot1StartTimeStamp);
        byte[] long2Bytes3 = ByteUtils.long2Bytes(bootDuration1);
        byte[] int2Bytes = ByteUtils.int2Bytes(this.memoryRecord.totalPss);
        byte[] int2Bytes2 = ByteUtils.int2Bytes(this.memoryRecord.nativePss);
        byte[] int2Bytes3 = ByteUtils.int2Bytes(this.memoryRecord.dalvikPss);
        byte[] short2Bytes = ByteUtils.short2Bytes(this.cpuRecord.myPidCpuPercent);
        byte[] short2Bytes2 = ByteUtils.short2Bytes(this.cpuRecord.sysTotalCpuPercent);
        return ByteUtils.merge(long2Bytes, new byte[]{bootType}, long2Bytes2, long2Bytes3, int2Bytes, int2Bytes2, int2Bytes3, short2Bytes, short2Bytes2);
    }
}
