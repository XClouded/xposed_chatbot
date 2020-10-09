package com.alibaba.motu.crashreporter;

public class ThreadCpuInfo {
    private static int count;
    private long cpuTime;
    public String cpupercent;
    private int index = 0;
    public String iow;
    private long iowtime;
    public String irq;
    private long irqtime;
    private long itime;
    private long lastSecondCpuTime;
    private long ntime;
    private long oldIowtime;
    private long oldIrqtime;
    private long oldItime;
    private long oldNtime;
    private long oldSirqtime;
    private long oldStime;
    private long oldUtime;
    public String pcy;
    public String pidstring;
    public String pr;
    public String proc;
    public String rss;
    public String s;
    private long sirqtime;
    private final long startTime;
    private long stime;
    public String system;
    public String thread;
    private final String threadName;
    private int tid;
    public String tidstring;
    public String uid;
    public String user;
    private long utime;
    public String vss;

    public ThreadCpuInfo(int i, String str, long j) {
        this.tid = i;
        this.threadName = str == null ? "noname" : str;
        this.startTime = j;
        int i2 = count;
        count = i2 + 1;
        this.index = i2;
    }

    public int getTid() {
        return this.tid;
    }

    public String getThreadName() {
        return this.threadName;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getCpuTime() {
        return this.cpuTime;
    }

    public void setUtime(long j) {
        this.oldUtime = this.utime;
        this.utime = j;
    }

    public void setStime(long j) {
        this.oldStime = this.stime;
        this.stime = j;
    }

    public void setNtime(long j) {
        this.oldNtime = this.ntime;
        this.ntime = j;
    }

    public void setItime(long j) {
        this.oldItime = this.itime;
        this.itime = j;
    }

    public void setIowtime(long j) {
        this.oldIowtime = this.iowtime;
        this.iowtime = j;
    }

    public void setIrqtime(long j) {
        this.oldIrqtime = this.irqtime;
        this.irqtime = j;
    }

    public void setSirqtime(long j) {
        this.oldSirqtime = this.sirqtime;
        this.sirqtime = j;
    }

    public void setCpuTime(long j) {
        this.lastSecondCpuTime = j - this.cpuTime;
        this.cpuTime = j;
    }

    public long getUtime() {
        return this.utime;
    }

    public long getOldUtime() {
        return this.oldUtime;
    }

    public long getStime() {
        return this.stime;
    }

    public long getOldStime() {
        return this.oldStime;
    }

    public long getNtime() {
        return this.ntime;
    }

    public long getOldNtime() {
        return this.oldNtime;
    }

    public long getItime() {
        return this.itime;
    }

    public long getOldItime() {
        return this.oldItime;
    }

    public long getIowtime() {
        return this.iowtime;
    }

    public long getOldIowtime() {
        return this.oldIowtime;
    }

    public long getIrqtime() {
        return this.irqtime;
    }

    public long getOldIrqtime() {
        return this.oldIrqtime;
    }

    public long getSirqtime() {
        return this.sirqtime;
    }

    public long getOldSirqtime() {
        return this.oldSirqtime;
    }

    public long getLastSecondCpuTime() {
        return this.lastSecondCpuTime;
    }

    public int getIndex() {
        return this.index;
    }

    public ThreadCpuInfo setIndex(int i) {
        this.index = i;
        return this;
    }

    public String toString() {
        return "ThreadCpuInfo{index=" + this.index + ",threadName='" + this.threadName + '\'' + ", startTime=" + this.startTime + ", cpuTime=" + this.cpuTime + ", lastSecondCpuTime=" + this.lastSecondCpuTime + '}';
    }
}
